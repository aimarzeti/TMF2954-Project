/**
 * Class: QuizModule
 * Creator: Noor Azuah binti Sawal (105404)
 * Tester: ________
 * Description: Quiz module for SDG 3: Good Health and Well-being (Mental Health)
 * Contains True/False and Fill in the Blank question types.
 * Saves score and username to scores.txt upon quiz completion.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

//Interface==============================================================================

//Main Class=============================================================================

public class QuizModule extends JPanel implements Quizable {

    //Constants

    private static final String SCORES_FILE = "scores.txt";
    private static final Color TEAL         = HealiverseTheme.MINT;
    private static final Color LIGHT_GREY   = HealiverseTheme.BABY_BLUE;
    private static final Color DARK_TEXT    = HealiverseTheme.DARK_PURPLE;
    private static final Color RED          = HealiverseTheme.PASTEL_PINK;
    private static final Color WHITE        = HealiverseTheme.CREAM;
    private static final Color SURFACE      = HealiverseTheme.SURFACE;
    private static final Color LINE         = HealiverseTheme.LINE;
    private static final Color MUTED_TEXT   = HealiverseTheme.MUTED_PURPLE;

    //Inner class: Question==============================================================
    //Represents a single quiz question.
    //type = "TF" for True/False, "FITB" for Fill in the Blank

    static class Question {
        String type;        // "TF" or "FITB"
        String questionText;
        String answer;      // "True", "False", or the blank answer
        String hint;        // shown for FITB questions

        public Question(String type, String questionText, String answer, String hint) {
            this.type         = type;
            this.questionText = questionText;
            this.answer       = answer;
            this.hint         = hint;
        }

        // Constructor for True/False
        public Question(String type, String questionText, String answer) {
            this(type, questionText, answer, "");
        }
    }

    //Fields=============================================================================

    private List<Question> questions;
    private int currentIndex;
    private int score;
    private String username;
    private String userAnswer; // stores current user answer

    //GUI Components=====================================================================

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Quiz screen
    private JLabel questionNumberLabel;
    private JProgressBar questionProgress;
    private JLabel questionTypeLabel;
    private JLabel questionTextLabel;
    private JPanel answerPanel;
    private JButton trueButton;
    private JButton falseButton;
    private JTextField fitbField;
    private JLabel hintLabel;
    private JButton nextButton;
    private JLabel feedbackLabel;
    private JLabel heartLabel;

    // Result screen
    private JLabel resultBadgeLabel;
    private JLabel scoreLabel;
    private JLabel percentageLabel;
    private JLabel messageLabel;
    private JLabel detailLabel;
    private JButton retryButton;
    private JButton menuButton;

    //Constructor=============================================================================

    public QuizModule(String username) {
        this.username  = username;
        this.questions = new ArrayList<>();
        this.currentIndex = 0;
        this.score        = 0;
        buildQuestions();
        buildUI();
    }

    //Guest Testing
    public QuizModule() {
        this("guest");
    }

    //Questions==============================================================================

    private void buildQuestions() {

        // What is Mental Health
        questions.add(new Question("TF",
            "According to the WHO, mental health is a state of well-being where a person can realise their own potential and contribute to their community.",
            "True"));

        questions.add(new Question("FITB",
            "Mental illness is NOT a sign of weakness — it is a _______ issue.",
            "medical",
            "Hint[7]: Think about what doctors treat."));

        // The Mental Health Spectrum
        questions.add(new Question("TF",
            "The mental health spectrum only has two states: healthy and ill.",
            "False"));

        questions.add(new Question("FITB",
            "When a person is unable to function and needs immediate professional support, they are said to be in ______.",
            "crisis",
            "Hint[6]: The most severe point on the spectrum."));

        // Common Mental Health Conditions
        questions.add(new Question("TF",
            "Depression affects over 280 million people globally according to the WHO (2023).",
            "True"));

        questions.add(new Question("FITB",
            "Excessive worry or fear that interferes with daily life is called an _______ disorder.",
            "anxiety",
            "Hint[7]: Feeling nervous or fearful."));

        // Warning Signs
        questions.add(new Question("TF",
            "Sleeping too much or too little can be a warning sign of a mental health struggle.",
            "True"));

        questions.add(new Question("FITB",
            "Avoiding friends, family, and activities you used to enjoy is known as social __________.",
            "withdrawal",
            "Hint[10]: Pulling away from others."));

        // The Brain and Mental Health
        questions.add(new Question("TF",
            "High cortisol levels over a long period can damage memory and mood.",
            "True"));

        questions.add(new Question("FITB",
            "The neurotransmitter that regulates mood, sleep, and appetite is called _________.",
            "serotonin",
            "Hint[9]: Low levels of this are linked to depression."));

        // Mental Health in Malaysia
        questions.add(new Question("TF",
            "According to the NHMS 2019, 1 in 3 Malaysians experience a mental health issue.",
            "True"));

        questions.add(new Question("FITB",
            "One major barrier to seeking mental health help in Malaysia is social ______, also known as 'malu'.",
            "stigma",
            "Hint[6]: Fear of being judged by others."));

        // Healthy Coping Strategies
        questions.add(new Question("TF",
            "30 minutes of moderate exercise 5 times a week can reduce depression symptoms as effectively as some medications.",
            "True"));

        questions.add(new Question("FITB",
            "Writing your thoughts and feelings in a diary is called __________.",
            "journaling",
            "Hint[10]: A personal writing practice."));

        // Mindfulness and Breathing
        questions.add(new Question("TF",
            "In the 4-7-8 breathing technique, you breathe out for 4 seconds.",
            "False"));

        questions.add(new Question("FITB",
            "The 5-4-3-2-1 grounding technique helps anchor you to the _______ moment.",
            "present",
            "Hint[7]: The opposite of past or future."));

        // When to Seek Professional Help
        questions.add(new Question("TF",
            "You should seek professional help if your symptoms have lasted more than 2 weeks.",
            "True"));

        questions.add(new Question("FITB",
            "A ____________ is a medical doctor who can diagnose mental health conditions and prescribe medication.",
            "psychiatrist",
            "Hint[12]: The most specialised mental health professional."));

        // Resources and Support
        questions.add(new Question("TF",
            "Befrienders Kuala Lumpur operates a 24-hour emotional support hotline.",
            "True"));

        questions.add(new Question("FITB",
            "Most university counselling services are offered for ____ to students.",
            "free",
            "Hint[4]: No cost involved."));
    }

    //UI Construction=============================================================================

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(WHITE);
        setPreferredSize(new Dimension(HealiverseTheme.MODULE_WIDTH, HealiverseTheme.MODULE_HEIGHT));

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(WHITE);

        mainPanel.add(buildQuizScreen(),   "QUIZ");
        mainPanel.add(buildResultScreen(), "RESULT");

        add(mainPanel, BorderLayout.CENTER);
    }

    //Quiz Screen===============================================================================

    private JPanel buildQuizScreen() {
        JPanel screen = new JPanel(new BorderLayout(0, 0));
        screen.setBackground(WHITE);

        // ── Top bar ──
        JPanel topBar = new JPanel(new BorderLayout(8, 4));
        topBar.setBackground(new Color(255, 226, 240));
        topBar.setBorder(new EmptyBorder(8, 12, 8, 12));

        questionNumberLabel = new JLabel("Question 1 of " + questions.size());
        questionNumberLabel.setFont(HealiverseTheme.buttonFont(12));
        questionNumberLabel.setForeground(DARK_TEXT);

        heartLabel = new JLabel("20 Questions", loadPixelIcon("Star.png", 20, 20), SwingConstants.RIGHT);
        heartLabel.setFont(HealiverseTheme.buttonFont(12));
        heartLabel.setForeground(MUTED_TEXT);

        questionProgress = new JProgressBar(0, questions.size());
        questionProgress.setValue(1);
        questionProgress.setForeground(TEAL);
        questionProgress.setBackground(LINE);
        questionProgress.setPreferredSize(new Dimension(0, 6));
        questionProgress.setBorder(new LineBorder(HealiverseTheme.PIXEL_PURPLE, 1));

        topBar.add(questionNumberLabel, BorderLayout.WEST);
        topBar.add(questionProgress, BorderLayout.SOUTH);
	topBar.add(heartLabel, BorderLayout.EAST);
        screen.add(topBar, BorderLayout.NORTH);

        // ── Content ──
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WHITE);
        content.setBorder(new EmptyBorder(10, 8, 10, 8));
        JScrollPane contentScroll = new JScrollPane(content);
        contentScroll.setBorder(null);
        contentScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentScroll.getViewport().setBackground(WHITE);
        contentScroll.getVerticalScrollBar().setUnitIncrement(14);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SURFACE);
        card.setBorder(HealiverseTheme.cardBorder(HealiverseTheme.BABY_BLUE, 12));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(318, Integer.MAX_VALUE));

        // Question type badge
        questionTypeLabel = new JLabel("TRUE / FALSE");
        questionTypeLabel.setFont(HealiverseTheme.buttonFont(11));
        questionTypeLabel.setForeground(Color.WHITE);
        questionTypeLabel.setOpaque(true);
        questionTypeLabel.setBackground(TEAL);
        questionTypeLabel.setBorder(new CompoundBorder(
            new LineBorder(TEAL, 1),
            new EmptyBorder(5, 10, 5, 10)
            ));
        questionTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mascotLabel = imageLabel("Brain Standing with Star.png", 118, 86);
        mascotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Question text
        questionTextLabel = new JLabel();
        questionTextLabel.setFont(HealiverseTheme.bodyFont(13));
        questionTextLabel.setForeground(DARK_TEXT);
        questionTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionTextLabel.setMaximumSize(new Dimension(286, Integer.MAX_VALUE));

        // Answer area (swaps between TF buttons and FITB field)
        answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setBackground(SURFACE);
        answerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerPanel.setMaximumSize(new Dimension(286, Integer.MAX_VALUE));

        // True/False buttons
        trueButton  = buildAnswerButton("TRUE",  TEAL);
        falseButton = buildAnswerButton("FALSE", RED);

        trueButton.addActionListener(e -> selectTF("True"));
        falseButton.addActionListener(e -> selectTF("False"));

        // Fill in the blank field
        fitbField = new JTextField();
        fitbField.setFont(HealiverseTheme.bodyFont(14));
        HealiverseTheme.setFixedSize(fitbField, 286, HealiverseTheme.BUTTON_HEIGHT);
        fitbField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LINE, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        fitbField.setAlignmentX(Component.LEFT_ALIGNMENT);

        hintLabel = new JLabel("");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hintLabel.setForeground(MUTED_TEXT);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Feedback label
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(HealiverseTheme.buttonFont(13));
        feedbackLabel.setForeground(TEAL);
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);        
        screen.add(contentScroll, BorderLayout.CENTER);

        //Card
        card.add(questionTypeLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(mascotLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(questionTextLabel);
        card.add(Box.createVerticalStrut(18));
        card.add(answerPanel);
        card.add(Box.createVerticalStrut(12));
        card.add(feedbackLabel);

        content.add(card);

        // ── Bottom nav ──
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(WHITE);
        navBar.setBorder(new EmptyBorder(8, 16, 12, 16));

        nextButton = new JButton("Next >");
        HealiverseTheme.stylePixelButton(nextButton, HealiverseTheme.PASTEL_PINK);
        nextButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, HealiverseTheme.COMPACT_BUTTON_HEIGHT));
        nextButton.addActionListener(e -> handleNext());

        navBar.add(nextButton, BorderLayout.CENTER);
        screen.add(navBar, BorderLayout.SOUTH);

        return screen;
    }

    private JButton buildAnswerButton(String text, Color bg) {
        JButton btn = new JButton(text);
        HealiverseTheme.stylePixelButton(btn, bg);
        HealiverseTheme.setFixedSize(btn, 286, HealiverseTheme.BUTTON_HEIGHT);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private JLabel imageLabel(String fileName, int maxWidth, int maxHeight) {
        JLabel label = new JLabel(loadPixelIcon(fileName, maxWidth, maxHeight));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private ImageIcon loadPixelIcon(String fileName, int maxWidth, int maxHeight) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }
        return HealiversePaths.loadPixelIcon(fileName, maxWidth, maxHeight);
    }

    //Result Screen=============================================================================

    private JPanel buildResultScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(255, 226, 240));
        header.setPreferredSize(new Dimension(HealiverseTheme.MODULE_WIDTH, 48));
        JLabel title = new JLabel("  Quiz Complete!", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(DARK_TEXT);
        title.setBorder(new EmptyBorder(0, 16, 0, 0));
        header.add(title, BorderLayout.CENTER);
        screen.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WHITE);
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel resultCard = new JPanel();
        resultCard.setLayout(new BoxLayout(resultCard, BoxLayout.Y_AXIS));
        resultCard.setBackground(SURFACE);
        resultCard.setBorder(HealiverseTheme.cardBorder(HealiverseTheme.SOFT_YELLOW, 12));
        resultCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultCard.setMaximumSize(new Dimension(334, Integer.MAX_VALUE));

        resultBadgeLabel = imageLabel("Quiz Star Badge.png", 112, 100);
        if (resultBadgeLabel.getIcon() == null) {
            resultBadgeLabel.setText("Quiz Summary");
            resultBadgeLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            resultBadgeLabel.setForeground(MUTED_TEXT);
        }
        resultBadgeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 38));
        scoreLabel.setForeground(TEAL);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        percentageLabel = new JLabel("", SwingConstants.CENTER);
        percentageLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        percentageLabel.setForeground(DARK_TEXT);
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        messageLabel.setForeground(TEAL);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailLabel = new JLabel("", SwingConstants.CENTER);
        detailLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        detailLabel.setForeground(MUTED_TEXT);
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        retryButton = new JButton("Try Again");
        HealiverseTheme.stylePixelButton(retryButton, TEAL);
        retryButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, HealiverseTheme.COMPACT_BUTTON_HEIGHT));
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.addActionListener(e -> resetQuiz());

        menuButton = new JButton("Back to Menu");
        HealiverseTheme.stylePixelButton(menuButton, LIGHT_GREY);
        menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, HealiverseTheme.COMPACT_BUTTON_HEIGHT));
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Note: MainApp (Member 1) will add ActionListener to menuButton
        // to navigate back to the main menu using CardLayout

        resultCard.add(Box.createVerticalStrut(4));
        resultCard.add(resultBadgeLabel);
        resultCard.add(Box.createVerticalStrut(6));
        resultCard.add(scoreLabel);
        resultCard.add(Box.createVerticalStrut(6));
        resultCard.add(percentageLabel);
        resultCard.add(Box.createVerticalStrut(12));
        resultCard.add(messageLabel);
        resultCard.add(Box.createVerticalStrut(8));
        resultCard.add(detailLabel);
        resultCard.add(Box.createVerticalStrut(18));
        resultCard.add(retryButton);
        resultCard.add(Box.createVerticalStrut(10));
        resultCard.add(menuButton);
        content.add(resultCard);

        screen.add(content, BorderLayout.CENTER);
        return screen;
    }

    //Quiz Logic=============================================================================

    private void loadQuestion() {
        if (currentIndex >= questions.size()) return;

        Question q = questions.get(currentIndex);
        userAnswer = null;
        feedbackLabel.setText(" ");
        nextButton.setEnabled(true);

        // Update progress
        questionNumberLabel.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        questionProgress.setValue(currentIndex + 1);

        // Update question text
        questionTextLabel.setText("<html><div style='width:230px;'>" + q.questionText + "</div></html>");

        // Update answer area
        answerPanel.removeAll();

        if (q.type.equals("TF")) {
            questionTypeLabel.setText("TRUE / FALSE");
            questionTypeLabel.setBackground(TEAL);
            trueButton.setBackground(TEAL);
            falseButton.setBackground(RED);
            answerPanel.add(trueButton);
            answerPanel.add(Box.createVerticalStrut(10));
            answerPanel.add(falseButton);
        } else {
            questionTypeLabel.setText("FILL IN THE BLANK");
            questionTypeLabel.setBackground(new Color(100, 82, 166));
            fitbField.setText("");
            hintLabel.setText(q.hint);
            answerPanel.add(fitbField);
            answerPanel.add(Box.createVerticalStrut(6));
            answerPanel.add(hintLabel);
        }

        answerPanel.revalidate();
        answerPanel.repaint();
    }

    private void selectTF(String selected) {
        userAnswer = selected;
        if (selected.equals("True")) {
            trueButton.setBackground(new Color(91, 151, 93));
            falseButton.setBackground(new Color(210, 210, 210));
        } else {
            falseButton.setBackground(new Color(202, 78, 132));
            trueButton.setBackground(new Color(210, 210, 210));
        }
    }

    private void handleNext() {
        Question q = questions.get(currentIndex);

        // Get answer based on question type
        if (q.type.equals("TF")) {
            if (userAnswer == null) {
                feedbackLabel.setText("Please select True or False!");
                feedbackLabel.setForeground(RED);
                return;
            }
        } else {
            userAnswer = fitbField.getText().trim();
            if (userAnswer.isEmpty()) {
                feedbackLabel.setText("Please type your answer!");
                feedbackLabel.setForeground(RED);
                return;
            }
        }

        // Check answer
        if (checkAnswer(userAnswer)) {
            score++;
            feedbackLabel.setText("Correct!");
            feedbackLabel.setForeground(new Color(91, 151, 93));
        } else {
            feedbackLabel.setText("Incorrect. Answer: " + q.answer);
            feedbackLabel.setForeground(RED);
        }

        // Disable buttons after answering
        nextButton.setEnabled(false);

        // Move to next question after short delay
        Timer timer = new Timer(900, e -> {
            currentIndex++;
            if (currentIndex >= questions.size()) {
                showResult();
            } else {
                loadQuestion();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showResult() {
        int total      = questions.size();
        int percentage = calculateScore();

        scoreLabel.setText(score + " / " + total);
        percentageLabel.setText(percentage + "%");
        resultBadgeLabel.setIcon(loadPixelIcon(
                percentage >= 80 ? "Top Score Badge.png" : "Quiz Star Badge.png",
                112,
                100));
        
        String stars;

        if (percentage >= 80)
            stars = "3 Stars";
        else if (percentage >= 50)
            stars = "2 Stars";
        else
            stars = "1 Star";

        messageLabel.setText(stars + " - " + getMotivationalMessage(percentage));

        detailLabel.setText(
            "<html><center>"
            + "Wellness Journey Complete<br><br>"
            + "You answered "
            + score + " out of "
            + total
            + " questions correctly."
            + "</center></html>"
);

        // Save score to scores.txt
        saveScore(username, score, total, percentage);

        cardLayout.show(mainPanel, "RESULT");
    }

    //Quizable Interface Implementation=============================================================================

    @Override
    public void startQuiz() {
        currentIndex = 0;
        score        = 0;
        loadQuestion();
        cardLayout.show(mainPanel, "QUIZ");
    }

    @Override
    public boolean checkAnswer(String answer) {
        Question q = questions.get(currentIndex);
        return answer.trim().equalsIgnoreCase(q.answer.trim());
    }

    @Override
    public int calculateScore() {
        if (questions.isEmpty()) return 0;
        return (int) Math.round((score * 100.0) / questions.size());
    }

    @Override
    public String getMotivationalMessage(int percentage) {
        if (percentage >= 80) return "Outstanding!";
        if (percentage >= 60) return "That's good!";
        if (percentage >= 40) return "Good try!";
        if (percentage >= 20) return "You can do better!";
        return "Don't give up!";
    }

    @Override
    public void resetQuiz() {
        currentIndex = 0;
        score        = 0;
        loadQuestion();
        cardLayout.show(mainPanel, "QUIZ");
    }

    //Data Storage=============================================================================

    //Saves the quiz result to scores.txt
    //Format: username,score,total,percentage,timestamp
    //Example: Azuah,16,20,80,2026-06-03 18:30:00
    //Called by Member 4 (GameManager): Zeti Nur Aimar binti Ali to read for leaderboard and history.
     
    private void saveScore(String username, int score, int total, int percentage) {
        try {
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String entry = username + "," + score + "," + total + "," + percentage + "," + timestamp;

            BufferedWriter writer = new BufferedWriter(new FileWriter(HealiversePaths.writableDataFile(SCORES_FILE), true)); // append mode
            writer.write(entry);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            System.err.println("Could not save score: " + e.getMessage());
        }
    }

    
    //Public getter for Member 1 (MainApp): Keweil Anak Bansa to attach a Back to Menu listener
    //MainApp calls: quizModule.getMenuButton().addActionListener(...)
    public JButton getMenuButton() {
        return menuButton;
    }

    //Public getter for Member 4 (GameManager): Zeti Nur Aimar binti Ali to get the final score
    //GameManager calls: quizModule.getFinalScore()
    
    public int getFinalScore() {
        return score;
    }

    //Public getter for Member 4 (GameManager): Zeti Nur Aimar binti Ali to get the final percentage
    //GameManager calls: quizModule.getFinalPercentage()
    
    public int getFinalPercentage() {
        return calculateScore();
    }

    //Main (for standalone testing)=============================================================================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quiz Module - SDG 3: Good Health and Well-being");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            QuizModule quiz = new QuizModule("testuser");
            quiz.startQuiz();
            frame.add(quiz);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
