import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class: LearningModule
 * Creator: Reselda Anak Robie
 * Main learning module for SDG 3: Good Health and Well-being (Mental Health)
 * Contains all interfaces and functionalities for the learning module, 
 * including quizzes, informational content, and interactive activities.
 */
interface Displayable {
    void displayContent(JPanel panel);
    String showTitle();
    String getSummary();
}

interface Navigable {
    void nextPage();
    void prevPage();
    void goToPage(int index) throws LearningModule.InvalidPageException;
    int getCurrentPageIndex();
    int getTotalPages();
}

public class LearningModule extends JPanel implements Displayable, Navigable {
    static class InvalidPageException extends Exception {
        private final int attemptedIndex;

        public InvalidPageException(int index, int totalPages) {
            super("Invalid page index: " + index +
                  ". Module has " + totalPages + " pages (0 to " + (totalPages - 1) + ").");
            this.attemptedIndex = index;
        }

        public int getAttemptedIndex() {
            return attemptedIndex;
        }
    }

    static class LearningPage implements Displayable {
        private String pageTitle;
        private String bodyText;
        private String imagePath;
        private String summary;
        private Color accentColour; 

        // Overloaded constructors for different page types
        public LearningPage(String pageTitle, String bodyText, 
                            String imagePath, String summary, Color accentColour) { 
            this.pageTitle = pageTitle;
            this.bodyText = bodyText;
            this.imagePath = imagePath;
            this.summary = summary;
            this.accentColour = accentColour;
        }

        public LearningPage(String pageTitle, String bodyText, String summary) {
            this(pageTitle, bodyText, null, summary, new Color(76, 175, 140)); 
        }

        public LearningPage(String pageTitle, String bodyText,
                            String imagePath, String summary) {
            this(pageTitle, bodyText, imagePath, summary, new Color(76, 175, 140)); 
        }

        @Override
        public void displayContent(JPanel panel) {
            panel.removeAll();
            panel.setLayout(new BorderLayout(0, 0));
            panel.setBackground(Color.WHITE);
        
            // Header bar
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(accentColour);
            header.setPreferredSize(new Dimension(panel.getWidth(), 60));
            JLabel titleLabel = new JLabel("  " + pageTitle);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            titleLabel.setForeground(Color.WHITE); 
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
            header.add(titleLabel, BorderLayout.CENTER);
            panel.add(header, BorderLayout.NORTH);

            JPanel centre = new JPanel();
            centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
            centre.setBackground(Color.WHITE); 
            centre.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/" + imagePath));
                    
                    Image scaled = icon.getImage().getScaledInstance(
                        450,
                        250,
                        Image.SCALE_SMOOTH
                    );
                    
                    JLabel imageLabel = new JLabel(new ImageIcon(scaled));
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
                    centre.add(imageLabel);
                    
                } catch (Exception e) {
                    System.out.println("Image not found: " + imagePath);
                    // Image not found - log and continue without image 
                }
            }

            JTextArea textArea = new JTextArea(bodyText);
            textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.setBackground(Color.WHITE); 
            textArea.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            );
            textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            centre.add(textArea);

            JScrollPane scroll = new JScrollPane(centre);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            panel.add(scroll, BorderLayout.CENTER); 

            panel.revalidate();
            panel.repaint();
        }

        @Override
        public String showTitle()   { return pageTitle; }

        @Override
        public String getSummary()  { return summary; }

        public void setAccentColour(Color c) { this.accentColour = c; } 
        public Color getAccentColour()       { return accentColour; }   
        public void setBodyText(String t)     { this.bodyText = t; }
        public String getBodyText()           { return bodyText; }    
        public void setImagePath(String p)    { this.imagePath = p; }
        public String getImagePath()          { return imagePath; }
    }

    private List<LearningPage> pages;
    private int currentIndex;
    private String moduleName;
    private String username;

    // .txt file to store module completion records
    private static final String FILE_PATH = "learning_progress.txt";

    private JPanel contentArea;
    private JLabel progressLabel; 
    private JProgressBar progressBar;
    private JButton prevBtn, nextBtn;
    private JLabel pageIndicator;

    private static final Color[] PAGE_COLOURS = { 
        new Color(76, 175, 140),  // Teal
        new Color(100, 149, 237), // Cornflower Blue  
        new Color(147, 112, 219), // Medium Purple
        new Color(205, 92, 92),   // Indian Red
        new Color(70, 130, 180),  // Steel Blue
        new Color(60, 179, 113),  // Medium Sea Green
        new Color(255, 165, 0),   // Orange
        new Color(64, 224, 208),  // Turquoise
        new Color(176, 48, 176),  // Firebrick
        new Color(220, 53, 69),   // Crimson
    };

    /** Constructor called from Dashboard - receives logged-in username */
    public LearningModule(String username) {
        this.moduleName = "Mental Health - SDG 3: Good Health and Well-being";
        this.pages = new ArrayList<>();
        this.currentIndex = 0;
        this.username = username;
        buildPages();
        buildUI();
        loadProgress(); // resume from saved progress if available
        showCurrentPage();
    }

    /** No-argument constructor for standalone testing */
    public LearningModule() {
        this("guest");
    }

    // ── Page content ──────────────────────────────────────────────

    private void buildPages() {

        pages.add(new LearningPage(
            "What is Mental Health?",
            "Mental health affects how we think, feel, and act.\n\n" + 
            "Key Facts:\n" +
            "• Everyone has mental health.\n" +
            "• It affects emotions and behaviour.\n" +
            "• Mental health can change over time.\n" +
            "• Seeking help is normal.\n\n" +
            "Mental health is just as important as physical health.",
            "images/mental_health_intro.png",
            "Introduction to mental health.",
            PAGE_COLOURS[0]
        ));

        pages.add(new LearningPage(
            "The Mental Health Spectrum",
            "Mental health exists on a spectrum.\n\n" +
            "THRIVING\n" +
            "Feeling positive and coping well.\n\n" +
            "STRUGGLING\n" +
            "• Feeling stressed or overwhelmed.\n\n" +
            "IN CRISIS\n" +
            "• Difficulty functioning normally.\n\n" +
            "People can move between these stages throughout life.",
            "images/spectrum.png",
            "The mental health spectrum.",
            PAGE_COLOURS[1]
        ));

        pages.add(new LearningPage(
            "Common Mental Health Conditions",
            "Some common mental health conditions include:\n\n" +
            "• ANXIETY - excessive worry or fear.\n" +
            "• DEPRESSION - persistent sadness and low mood.\n" +
            "• STRESS - the body's response to pressure.\n\n" +
            "These conditions are treatable with the right support.", 
            "images/conditions.png",
            "Common mental health conditions.",
            PAGE_COLOURS[2]
        ));

        pages.add(new LearningPage(
            "Warning Signs to Watch For",
            "8 Warning Signs:\n\n" +
            "1. Withdrawal from friends and families\n" +
            "2. Mood changes\n" +
            "3. Sleep problems\n" +
            "4. Appetite changes\n" +
            "5. Difficulty concentrating\n" +
            "6. Feelings of worthlessness\n" +
            "7. Physical complaints\n" +
            "8. Loss of motivation\n" +
            "Recognising signs early is important.",
            "images/warning_signs.png",
            "Warning signs of mental health struggles.",
            PAGE_COLOURS[3]
        ));

        pages.add(new LearningPage(
            "The Brain and Mental Health",
            "The brain uses chemicals that affect emotions.\n\n" +
            "• SEROTONIN - mood and sleep\n" +
            "• DOPAMINE - motivation and reward\n" +
            "• CORTISOL - stress response\n" +
            "• GABA (Gamma-Aminobutyric Acid) - helps calm the brain\n\n" +
            "Mental health conditions can have biological causes.",
            "images/brain.png",
            "Brain chemicals and mental health.",
            PAGE_COLOURS[4]
        ));

        pages.add(new LearningPage(
            "Mental Health in Malaysia",
            "Mental health awareness is growing in Malaysia.\n\n" +
            "Challenges include:\n" +
            "• Social stigma\n" +
            "• Lack of awareness\n" +
            "• Fear of seeking help\n" +
            "• Cost of treatment\n\n" +
            "Many universities now provide counselling services.",
            "images/malaysia.png",
            "Mental health in Malaysia.",
            PAGE_COLOURS[5]
        ));

        pages.add(new LearningPage(
            "Healthy Coping Strategies",
            "Healthy ways to manage stress include:\n\n" +
            "• Getting  enough sleep\n" +
            "• Regular exercise\n" +
            "• Journaling\n" +
            "• Talking to trusted people\n" +
            "• Limiting screen time\n" +
            "• Enjoying hobbies\n\n" +
            "Small healthy habits can make a big difference.",
            "images/coping.png",
            "Healthy coping strategies.",
            PAGE_COLOURS[6]
        ));

        pages.add(new LearningPage(
            "Mindfulness and Breathing",
            "Mindfulness helpes you focus on the present moment.\n\n" +
            "4-7-8 Breathing Technique:\n\n" +
            "Step 1: INHALE 4 seconds\n" +
            "Step 2: HOLD your breath for 7 seconds\n" +
            "Step 3: EXHALE for 8 seconds\n" +
            "Step 4: Repeat 3-4 times\n\n" +
            "This technique helps your body relax." +
            "images/mindfulness.png",
            "Mindfulness technique.",
            PAGE_COLOURS[7]
        ));

        pages.add(new LearningPage(
            "When to Seek Professional Help",
            "Consider seeking help if.\n\n" +
            "• Symptoms last more than 2 weeks\n" +
            "• Daily activities become difficult\n" +
            "• You feel overwhelmed regularly\n" +
            "• You have thoughts of harming yourself\n\n" +
            "Professional support is available and effective.",
            "images/seek_help.png",
            "Seeking professional help.",
            PAGE_COLOURS[8]
        ));

        pages.add(new LearningPage(
            "Resources and Support",
            "Help is available.\n\n" +
            "Malaysia Resources:\n\n" +
            "• BEFRIENDERS KUALA LUMPUR\n" +
            "• MIASA (Mental Illness Awareness & Support Association)\n" +
            "• TALIAN KASIH 15999\n" +
            "• University Counselling Services.\n" +
            "Congratulations on completing this learning module!.\n" +
            "Proceed to the quiz to test your knowledge."
            "images/resources.png",
            "Support resources.",
            PAGE_COLOURS[9]
        ));

    }

    // ── UI construction ───────────────────────────────────────────

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(900, 700));

        // Top progress bar
        JPanel topBar = new JPanel(new BorderLayout(8, 0));
        topBar.setBackground(new Color(245, 245, 245)); 
        topBar.setBorder(new EmptyBorder(8, 12, 8, 12));

        progressLabel = new JLabel(moduleName);
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        progressLabel.setForeground(new Color(60, 60, 60)); 

        pageIndicator = new JLabel("1 / " + pages.size());
        pageIndicator.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pageIndicator.setForeground(new Color(100, 100, 100)); 
        pageIndicator.setHorizontalAlignment(SwingConstants.RIGHT);

        progressBar = new JProgressBar(0, pages.size());
        progressBar.setValue(1);
        progressBar.setStringPainted(false);
        progressBar.setForeground(new Color(76, 175, 140)); 
        progressBar.setBackground(new Color(220, 220, 220)); 
        progressBar.setPreferredSize(new Dimension(0, 6));

        topBar.add(progressLabel, BorderLayout.WEST);
        topBar.add(progressBar,   BorderLayout.CENTER);
        topBar.add(pageIndicator, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Content area
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Color.WHITE);
        add(contentArea, BorderLayout.CENTER);

        // Bottom navigation
        JPanel navBar = new JPanel(new BorderLayout(12, 0));
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(new EmptyBorder(8, 16, 12, 16));

        prevBtn = new JButton("< Back");
        prevBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        prevBtn.setBackground(new Color(230, 230, 230)); 
        prevBtn.setForeground(new Color(60, 60, 60));    
        prevBtn.setFocusPainted(false);
        prevBtn.setBorderPainted(false);
        prevBtn.setEnabled(false);
        prevBtn.addActionListener(e -> prevPage());

        nextBtn = new JButton("Next >");
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        nextBtn.setBackground(new Color(76, 175, 140)); 
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.setBorderPainted(false);
        nextBtn.addActionListener(e -> nextPage());

        navBar.add(prevBtn, BorderLayout.WEST);
        navBar.add(nextBtn, BorderLayout.EAST);
        add(navBar, BorderLayout.SOUTH);
    }

    // ── Displayable implementation ────────────────────────────────

    @Override
    public void displayContent(JPanel panel) {
        pages.get(currentIndex).displayContent(contentArea);
    }

    @Override
    public String showTitle() { return moduleName; }

    @Override
    public String getSummary() {
        return "10-screen mental health learning module covering SDG 3.";
    }

    // ── Navigable implementation ──────────────────────────────────

    @Override
    public void nextPage() {
        if (currentIndex < pages.size() - 1) {
            currentIndex++;
            showCurrentPage();
        }
    }

    public void prevPage() {
        if (currentIndex > 0) {
            currentIndex--;
            showCurrentPage();
        }
    }

    @Override
    public void goToPage(int index) throws InvalidPageException {
        if (index < 0 || index >= pages.size()) {
            throw new InvalidPageException(index, pages.size());
        }
        currentIndex = index;
        showCurrentPage();
    }

    @Override
    public int getCurrentPageIndex() { return currentIndex; }

    @Override
    public int getTotalPages() { return pages.size(); }

    // ── Internal helpers ───────────────────────────────────────────

    private void showCurrentPage() {
        pages.get(currentIndex).displayContent(contentArea);
        progressBar.setValue(currentIndex + 1);
        pageIndicator.setText((currentIndex + 1) + " / " + pages.size());
        prevBtn.setEnabled(currentIndex > 0);
        boolean isLastPage = (currentIndex == pages.size() - 1);
        nextBtn.setText(isLastPage ? "Quiz >" : "Next >");
        nextBtn.setBackground(isLastPage ? new Color(220, 53, 69) : new Color(76, 175, 140)); // Fix #1: was Colour

        // Save progress to .txt every time the page changes
        saveProgress();

        // When user reaches the last page, record module completion
        if (isLastPage) {
            saveCompletion(); 
        }
    }

    /**
     * Saves the current page index to learning_progress.txt
     * Format: username|pageIndex|totalPages|timestamp
     * Example line: johndoe|3|10|2024-06-15T14:30:00
     * This lets the user resume where they left off
     * No scores are hardcoded - all data comes from runtime
     */
    private void saveProgress() {
        try {
            // Read existing lines, updates or insert entry for current user
            List<String> lines = new ArrayList<>();
            File file = new File(FILE_PATH);

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                boolean found = false;
                while ((line = reader.readLine()) != null) {
                    // Each line: username,pageIndex,totalPages,timestamp,completed
                    // Fix #9: removed conflicting startsWith("|") outer check
                    String[] parts = line.split(",");
                    if (parts.length >= 1 && parts[0].equals(username)) {
                        // Update this user's progress line
                        String completed = (parts.length >= 5) ? parts[4] : "false"; // Fix #10: was comma
                        String timestamp = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        lines.add(username + "," + currentIndex + "," +
                                  pages.size() + "," + timestamp + "," + completed);
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
                reader.close();
                if (!found) {
                    String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    lines.add(username + "," + currentIndex + "," +
                              pages.size() + "," + timestamp + ",false");
                }
            } else {
                String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                lines.add(username + "," + currentIndex + "," +
                          pages.size() + "," + timestamp + ",false");
            } 

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.err.println("Could not save progress: " + e.getMessage());
        }
    }

    /**
     * Marks the module as completed in learning_progress.txt
     * Called when the user reaches the last page
     * The gamification module can read this file to award a badge
     */
    private void saveCompletion() {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(FILE_PATH);

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 1 && parts[0].equals(username)) {
                        String timestamp = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        // Mark completed = true
                        lines.add(username + "," + currentIndex + "," +
                                  pages.size() + "," + timestamp + ",true");
                    } else {
                        lines.add(line);
                    }
                }
                reader.close();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false)); // Fix #11: was missing variable name
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.err.println("Could not save completion: " + e.getMessage());
        }
    }

    /**
     * Loads the user's last saved page index from learning_progress.txt
     * If no record is found, starts from page 0
     * Prevents users from having to restart the module every session
     */
    private void loadProgress() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return; 

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    int savedIndex = Integer.parseInt(parts[1].trim());
                    // Validate index is still in range
                    if (savedIndex >= 0 && savedIndex < pages.size()) {
                        currentIndex = savedIndex;
                    }
                    break;
                }
            }
            reader.close();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Could not load progress: " + e.getMessage());
            currentIndex = 0; // safe fallback to start of module
        }
    }

    /**
     * Public method for the gamification module to check
     * whether this user has completed the learning module
     * Reads directly from the .txt file - no hardcoded values
     * 
     * @param username the username to check
     * @return true if the user has completed the module, false otherwise
     */
    public static boolean hasCompleted(String username) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return false;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(username)) {
                    reader.close();
                    return parts[4].trim().equalsIgnoreCase("true");
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read completion status: " + e.getMessage());
        }
        return false;
    }

    public double getProgress() {
        return (double)(currentIndex + 1) / pages.size();
    }

    // ── Main (for standalone testing) ────────────────────────────

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mental Health Learning Module - SDG 3: Good Health and Well-being"); // Fix #12: was broken string with - outside quotes
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new LearningModule("testuser")); // pass real username from Dashboard
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
