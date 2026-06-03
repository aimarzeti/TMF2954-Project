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
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE); 
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
            header.add(titleLabel, BorderLayout.CENTER);
            panel.add(header, BorderLayout.NORTH);

            JPanel centre = new JPanel();
            centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
            centre.setBackground(Color.WHITE); 
            centre.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/" + imagePath));
                    Image scaled = icon.getImage().getScaledInstance(300, 180, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaled));
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
                    centre.add(imageLabel);
                } catch (Exception e) {
                    // Image not found - log and continue without image 
                }
            }

            JTextArea textArea = new JTextArea(bodyText);
            textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.setBackground(Color.WHITE); 
            textArea.setBorder(null);
            textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            centre.add(textArea);

            JScrollPane scroll = new JScrollPane(centre);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setValue(0);
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
            "Mental health includes our emotional, psychological, and social well-being." + 
            "It affects how we think, feel, and act in everyday life.\n\n" +
            "According to the World Health Organization (WHO), mental health is a state of" +
            "well-being in which an individual realise their own potential, can cope with the" +
            "normal stresses of life, can work productively, and is able to make a contribution." +
            "to their community.\n\n" +
            "Key Facts:\n" +
            "- 1 in 4 people worlwide will experience a mental health condition at some point.\n" +
            "- Mental health problems account for 16% of the global burden of disease.\n" +
            "- Mental health is NOT just the absense of mental illness.\n\n" +
            "Common myths:\n" +
            "Myth: Mental illness is a sign of weakness.\n" +
            "Fact: Mental health conditions are medical issues, not character flaws.",
            "images/mental_health_intro.png",
            "Introduction to mental health: definition, WHO stats, and myth-busting.",
            PAGE_COLOURS[0]
        ));

        pages.add(new LearningPage(
            "The Mental Health Spectrum",
            "Mental health exists on a spectrum — it is not simply 'healthy' or 'ill'.\n\n" +
            "The spectrum ranges from:\n\n" +
            "THRIVING\n" +
            "Feeling good, functioning well, resilient, positive outlook.\n\n" +
            "STRUGGLING\n" +
            "Feeling stressed, anxious, or low. Functioning, but with difficulty.\n\n" +
            "IN CRISIS\n" +
            "Unable to function. Needs immediate professional support.\n\n" +
            "Why this matters:\n" +
            "Everyone moves along this spectrum throughout their life. Recognising where you " +
            "are helps you take action early, before reaching a crisis point.\n\n" +
            "Check-in question: Where on this spectrum are you right now?",
            "images/spectrum.png",
            "The mental health spectrum from thriving to crisis.",
            PAGE_COLOURS[1]
        ));

        pages.add(new LearningPage(
            "Common Mental Health Conditions",
            "Here are three of the most common mental health conditions:\n\n" +
            "ANXIETY DISORDERS\n" +
            "Excessive worry, fear, or nervousness that interferes with daily life.\n" +
            "Types: Generalised Anxiety, Social Anxiety, Panic Disorder\n\n" +
            "DEPRESSION\n" +
            "Persistent low mood, loss of interest, fatigue, and feelings of hopelessness.\n" +
            "Affects over 280 million people globally (WHO, 2023).\n\n" +
            "STRESS\n" +
            "The body's response to pressure. Short-term stress can be useful, but chronic " +
            "stress damages physical and mental health.\n\n" +
            "Important note:\n" +
            "These conditions are treatable. With the right support, most people recover " +
            "and lead fulfilling lives. Early help = better outcomes.",
            "images/conditions.png",
            "Overview of anxiety, depression, and stress.",
            PAGE_COLOURS[2]
        ));

        pages.add(new LearningPage(
            "Warning Signs to Watch For",
            "Recognising early warning signs in yourself or others can prevent a crisis.\n\n" +
            "8 Warning Signs:\n\n" +
            "1. Withdrawal — avoiding friends, family, and activities you used to enjoy\n\n" +
            "2. Mood changes — unusual irritability, sadness, or mood swings\n\n" +
            "3. Sleep problems — sleeping too much or too little, insomnia\n\n" +
            "4. Appetite changes — eating significantly more or less than usual\n\n" +
            "5. Difficulty concentrating — trouble focusing at work or school\n\n" +
            "6. Feelings of worthlessness — negative self-talk, excessive guilt\n\n" +
            "7. Physical complaints — unexplained headaches, stomach aches\n\n" +
            "8. Loss of motivation — not caring about things that mattered before\n\n" +
            "If you notice these in someone, check in with them kindly.",
            "images/warning_signs.png",
            "8 early warning signs of mental health struggles.",
            PAGE_COLOURS[3]
        ));

        pages.add(new LearningPage(
            "The Brain and Mental Health",
            "Understanding the biology of mental health helps reduce stigma.\n\n" +
            "Key neurotransmitters involved:\n\n" +
            "SEROTONIN\n" +
            "Regulates mood, sleep, and appetite. Low serotonin is linked to depression.\n\n" +
            "DOPAMINE\n" +
            "The 'reward' chemical. Drives motivation and pleasure. Imbalance linked " +
            "to addiction and mood disorders.\n\n" +
            "CORTISOL\n" +
            "The stress hormone. Released during threat. Chronically high cortisol " +
            "damages memory, immune function, and mood.\n\n" +
            "GABA\n" +
            "The brain's 'calm down' signal. Low GABA is linked to anxiety.\n\n" +
            "Why medication works:\n" +
            "Antidepressants like SSRIs work by increasing serotonin availability in the brain. " +
            "Mental illness has a biological basis — it is not 'all in your head'.",
            "images/brain.png",
            "Neurotransmitters: serotonin, dopamine, cortisol, and GABA.",
            PAGE_COLOURS[4]
        ));

        pages.add(new LearningPage(
            "Mental Health in Malaysia",
            "Malaysia has a growing mental health awareness movement, but stigma remains.\n\n" +
            "Statistics:\n" +
            "- 1 in 3 Malaysians experience a mental health issue (NHMS 2019)\n" +
            "- Only 1 in 10 Malaysians with mental illness receive treatment\n" +
            "- Depression and anxiety are the most common conditions reported\n" +
            "- University students are among the most affected groups\n\n" +
            "Barriers to seeking help in Malaysia:\n" +
            "- Social stigma (malu / shame)\n" +
            "- Fear of being seen as 'crazy'\n" +
            "- Lack of awareness about available services\n" +
            "- Cultural belief that mental issues should be handled privately\n" +
            "- Financial cost of therapy\n\n" +
            "The good news:\n" +
            "Public awareness is increasing. More universities now offer free counselling. " +
            "Telehealth services make it easier to access support discreetly.",
            "images/malaysia.png",
            "Mental health statistics and challenges specific to Malaysia.",
            PAGE_COLOURS[5]
        ));

        pages.add(new LearningPage(
            "Healthy Coping Strategies",
            "Coping strategies are the tools we use to manage stress and maintain mental health.\n\n" +
            "Evidence-based strategies:\n\n" +
            "SLEEP (7-9 hours)\n" +
            "Sleep deprivation worsens anxiety, depression, and concentration. " +
            "Consistent sleep schedule is key.\n\n" +
            "PHYSICAL EXERCISE\n" +
            "30 minutes of moderate exercise, 5x/week, reduces depression symptoms " +
            "as effectively as some medications.\n\n" +
            "JOURNALING\n" +
            "Writing thoughts and feelings reduces emotional intensity and improves " +
            "self-understanding.\n\n" +
            "SOCIAL SUPPORT\n" +
            "Talking to trusted friends or family is protective against mental illness.\n\n" +
            "DIGITAL DETOX\n" +
            "Excessive social media use is linked to anxiety and low self-esteem.\n\n" +
            "CREATIVE ACTIVITIES\n" +
            "Art, music, cooking, or any hobby engages the reward system positively.",
            "images/coping.png",
            "Six evidence-based coping strategies for mental wellness.",
            PAGE_COLOURS[6]
        ));

        pages.add(new LearningPage(
            "Mindfulness and Breathing",
            "Mindfulness is the practice of paying attention to the present moment — " +
            "non-judgmentally. It reduces anxiety and improves emotional regulation.\n\n" +
            "Try this now: The 4-7-8 Breathing Technique\n\n" +
            "Step 1: Breathe IN through your nose for 4 seconds\n" +
            "Step 2: HOLD your breath for 7 seconds\n" +
            "Step 3: Breathe OUT through your mouth for 8 seconds\n" +
            "Step 4: Repeat 3-4 times\n\n" +
            "This activates your parasympathetic nervous system, calming the " +
            "fight-or-flight response.\n\n" +
            "The 5-4-3-2-1 Grounding Technique\n" +
            "When feeling overwhelmed, name:\n" +
            "5 things you can SEE\n" +
            "4 things you can TOUCH\n" +
            "3 things you can HEAR\n" +
            "2 things you can SMELL\n" +
            "1 thing you can TASTE\n\n" +
            "This anchors you to the present and interrupts anxiety spirals.",
            "images/mindfulness.png",
            "4-7-8 breathing and 5-4-3-2-1 grounding techniques.",
            PAGE_COLOURS[7]
        ));

        pages.add(new LearningPage(
            "When to Seek Professional Help",
            "It is okay to ask for help. Seeking support is a sign of strength, not weakness.\n\n" +
            "Seek help if:\n" +
            "- Symptoms have lasted more than 2 weeks\n" +
            "- You are unable to function at school, work, or home\n" +
            "- You are using substances to cope\n" +
            "- You are having thoughts of harming yourself or others\n\n" +
            "Types of professional support:\n\n" +
            "COUNSELLOR\n" +
            "Talk therapy, suitable for stress, life transitions, mild-moderate issues.\n\n" +
            "PSYCHOLOGIST\n" +
            "Provides therapy (CBT, DBT) for moderate-severe conditions.\n\n" +
            "PSYCHIATRIST\n" +
            "Medical doctor who can diagnose and prescribe medication.\n\n" +
            "How to start:\n" +
            "1. Talk to a trusted person about how you feel\n" +
            "2. Visit your university health centre or GP\n" +
            "3. Ask for a referral to a mental health professional",
            "images/seek_help.png",
            "When and how to seek professional mental health support.",
            PAGE_COLOURS[8]
        ));

        pages.add(new LearningPage(
            "Resources and Support",
            "You are not alone. Help is available.\n\n" +
            "Crisis Helplines (Malaysia):\n\n" +
            "BEFRIENDERS KUALA LUMPUR\n" +
            "Tel: 03-7627 2929  (24 hours)\n" +
            "For emotional support and crisis intervention\n\n" +
            "MIASA (Mental Illness Awareness & Support Association)\n" +
            "Tel: 03-2780 6803\n" +
            "Support for those affected by mental illness\n\n" +
            "TALIAN KASIH 15999\n" +
            "Government welfare hotline (24 hours)\n\n" +
            "MHPSS (Ministry of Health Psychological First Aid)\n" +
            "Tel: 03-2935 9935\n\n" +
            "Online resources:\n" +
            "- MindaKita: www.mindakita.my\n" +
            "- WHO Mental Health: www.who.int/mental_health\n\n" +
            "On campus:\n" +
            "Visit your university's counselling centre.\n" +
            "Most universities offer FREE, confidential counselling services.\n\n" +
            "You made it through this module! Take the quiz to test your knowledge.",
            "images/resources.png",
            "Malaysian mental health helplines, online resources, and campus support.",
            PAGE_COLOURS[9]
        ));

    }

    // ── UI construction ───────────────────────────────────────────

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 700));

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
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}