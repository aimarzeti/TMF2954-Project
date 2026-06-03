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

//Interface==============================================================================

interface Quizable {
    void startQuiz();
    boolean checkAnswer(String answer);
    int calculateScore();
    String getMotivationalMessage(int percentage);
    void resetQuiz();
}

//Main Class=============================================================================

public class QuizModule extends JPanel implements Quizable {

    //Constants

    private static final String SCORES_FILE = "scores.txt";
    private static final Color TEAL         = new Color(76, 175, 140);
    private static final Color LIGHT_GREY   = new Color(245, 245, 245);
    private static final Color DARK_TEXT    = new Color(40, 40, 40);
    private static final Color RED          = new Color(220, 53, 69);
    private static final Color WHITE        = Color.WHITE;

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

    // Result screen
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

        // Page 1 — What is Mental Health
        questions.add(new Question("TF",
            "According to the WHO, mental health is a state of well-being where a person can realise their own potential and contribute to their community.",
            "True"));

        questions.add(new Question("FITB",
            "Mental illness is NOT a sign of weakness — it is a ________ issue.",
            "medical",
            "Hint: Think about what doctors treat. [-------]"));

        // Page 2 — The Mental Health Spectrum
        questions.add(new Question("TF",
            "The mental health spectrum only has two states: healthy and ill.",
            "False"));

        questions.add(new Question("FITB",
            "When a person is unable to function and needs immediate professional support, they are said to be in ________.",
            "crisis",
            "Hint: The most severe point on the spectrum.[------]"));

        // Page 3 — Common Mental Health Conditions
        questions.add(new Question("TF",
            "Depression affects over 280 million people globally according to the WHO (2023).",
            "True"));

        questions.add(new Question("FITB",
            "Excessive worry or fear that interferes with daily life is called an ________ disorder.",
            "anxiety",
            "Hint: Feeling nervous or fearful.[-------]"));

        // Page 4 — Warning Signs
        questions.add(new Question("TF",
            "Sleeping too much or too little can be a warning sign of a mental health struggle.",
            "True"));

        questions.add(new Question("FITB",
            "Avoiding friends, family, and activities you used to enjoy is known as social ________.",
            "withdrawal",
            "Hint: Pulling away from others.[----------]"));

        // Page 5 — The Brain and Mental Health
        questions.add(new Question("TF",
            "High cortisol levels over a long period can damage memory and mood.",
            "True"));

        questions.add(new Question("FITB",
            "The neurotransmitter that regulates mood, sleep, and appetite is called ________.",
            "serotonin",
            "Hint: Low levels of this are linked to depression.[---------]"));

        // Page 6 — Mental Health in Malaysia
        questions.add(new Question("TF",
            "According to the NHMS 2019, 1 in 3 Malaysians experience a mental health issue.",
            "True"));

        questions.add(new Question("FITB",
            "One major barrier to seeking mental health help in Malaysia is social ________, also known as 'malu'.",
            "stigma",
            "Hint: Fear of being judged by others.[------]"));

        // Page 7 — Healthy Coping Strategies
        questions.add(new Question("TF",
            "30 minutes of moderate exercise 5 times a week can reduce depression symptoms as effectively as some medications.",
            "True"));

        questions.add(new Question("FITB",
            "Writing your thoughts and feelings in a diary is called ________.",
            "journaling",
            "Hint: A personal writing practice.[----------]"));

        // Page 8 — Mindfulness and Breathing
        questions.add(new Question("TF",
            "In the 4-7-8 breathing technique, you breathe out for 4 seconds.",
            "False"));

        questions.add(new Question("FITB",
            "The 5-4-3-2-1 grounding technique helps anchor you to the ________ moment.",
            "present",
            "Hint: The opposite of past or future.[-------]"));

        // Page 9 — When to Seek Professional Help
        questions.add(new Question("TF",
            "You should seek professional help if your symptoms have lasted more than 2 weeks.",
            "True"));

        questions.add(new Question("FITB",
            "A ________ is a medical doctor who can diagnose mental health conditions and prescribe medication.",
            "psychiatrist",
            "Hint: The most specialised mental health professional.[------------]"));

        // Page 10 — Resources and Support
        questions.add(new Question("TF",
            "Befrienders Kuala Lumpur operates a 24-hour emotional support hotline.",
            "True"));

        questions.add(new Question("FITB",
            "Most university counselling services are offered for ________ to students.",
            "free",
            "Hint: No cost involved.[----]"));
    }

    //UI Construction=============================================================================

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(WHITE);
        setPreferredSize(new Dimension(400, 700));

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
        topBar.setBackground(LIGHT_GREY);
        topBar.setBorder(new EmptyBorder(10, 14, 10, 14));

        questionNumberLabel = new JLabel("Question 1 of " + questions.size());
        questionNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        questionNumberLabel.setForeground(DARK_TEXT);

        questionProgress = new JProgressBar(0, questions.size());
        questionProgress.setValue(1);
        questionProgress.setForeground(TEAL);
        questionProgress.setBackground(new Color(220, 220, 220));
        questionProgress.setPreferredSize(new Dimension(0, 6));
        questionProgress.setBorderPainted(false);

        topBar.add(questionNumberLabel, BorderLayout.WEST);
        topBar.add(questionProgress,    BorderLayout.SOUTH);
        screen.add(topBar, BorderLayout.NORTH);

        // ── Content ──
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WHITE);
        content.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Question type badge
        questionTypeLabel = new JLabel("TRUE / FALSE");
        questionTypeLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        questionTypeLabel.setForeground(WHITE);
        questionTypeLabel.setOpaque(true);
        questionTypeLabel.setBackground(TEAL);
        questionTypeLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        questionTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(questionTypeLabel);
        content.add(Box.createVerticalStrut(14));

        // Question text
        questionTextLabel = new JLabel("<html><body style='width:320px'></body></html>");
        questionTextLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        questionTextLabel.setForeground(DARK_TEXT);
        questionTextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(questionTextLabel);
        content.add(Box.createVerticalStrut(24));

        // Answer area (swaps between TF buttons and FITB field)
        answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setBackground(WHITE);
        answerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // True/False buttons
        trueButton  = buildAnswerButton("✓  TRUE",  new Color(40, 167, 69));
        falseButton = buildAnswerButton("✗  FALSE", RED);

        trueButton.addActionListener(e -> selectTF("True"));
        falseButton.addActionListener(e -> selectTF("False"));

        // Fill in the blank field
        fitbField = new JTextField();
        fitbField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        fitbField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        fitbField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        fitbField.setAlignmentX(Component.LEFT_ALIGNMENT);

        hintLabel = new JLabel("");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hintLabel.setForeground(new Color(150, 150, 150));
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(answerPanel);
        content.add(Box.createVerticalStrut(12));

        // Feedback label
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        feedbackLabel.setForeground(TEAL);
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(feedbackLabel);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        screen.add(scroll, BorderLayout.CENTER);

        // ── Bottom nav ──
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(WHITE);
        navBar.setBorder(new EmptyBorder(10, 16, 16, 16));

        nextButton = new JButton("Next >");
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        nextButton.setBackground(TEAL);
        nextButton.setForeground(WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
        nextButton.addActionListener(e -> handleNext());

        navBar.add(nextButton, BorderLayout.CENTER);
        screen.add(navBar, BorderLayout.SOUTH);

        return screen;
    }

    private JButton buildAnswerButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    //Result Screen=============================================================================

    private JPanel buildResultScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(TEAL);
        header.setPreferredSize(new Dimension(400, 60));
        JLabel title = new JLabel("  Quiz Complete!", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(WHITE);
        title.setBorder(new EmptyBorder(0, 16, 0, 0));
        header.add(title, BorderLayout.CENTER);
        screen.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WHITE);
        content.setBorder(new EmptyBorder(30, 24, 24, 24));

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        scoreLabel.setForeground(TEAL);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        percentageLabel = new JLabel("", SwingConstants.CENTER);
        percentageLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        percentageLabel.setForeground(DARK_TEXT);
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        messageLabel.setForeground(TEAL);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailLabel = new JLabel("", SwingConstants.CENTER);
        detailLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        detailLabel.setForeground(new Color(120, 120, 120));
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        retryButton = new JButton("Try Again");
        retryButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        retryButton.setBackground(TEAL);
        retryButton.setForeground(WHITE);
        retryButton.setFocusPainted(false);
        retryButton.setBorderPainted(false);
        retryButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.addActionListener(e -> resetQuiz());

        menuButton = new JButton("Back to Menu");
        menuButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        menuButton.setBackground(LIGHT_GREY);
        menuButton.setForeground(DARK_TEXT);
        menuButton.setFocusPainted(false);
        menuButton.setBorderPainted(false);
        menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Note: MainApp (Member 1) will add ActionListener to menuButton
        // to navigate back to the main menu using CardLayout

        content.add(Box.createVerticalStrut(10));
        content.add(scoreLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(percentageLabel);
        content.add(Box.createVerticalStrut(16));
        content.add(messageLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(detailLabel);
        content.add(Box.createVerticalStrut(30));
        content.add(retryButton);
        content.add(Box.createVerticalStrut(10));
        content.add(menuButton);

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
        questionTextLabel.setText("<html><body style='width:320px'>" + q.questionText + "</body></html>");

        // Update answer area
        answerPanel.removeAll();

        if (q.type.equals("TF")) {
            questionTypeLabel.setText("TRUE / FALSE");
            questionTypeLabel.setBackground(TEAL);
            trueButton.setBackground(new Color(40, 167, 69));
            falseButton.setBackground(RED);
            answerPanel.add(trueButton);
            answerPanel.add(Box.createVerticalStrut(10));
            answerPanel.add(falseButton);
        } else {
            questionTypeLabel.setText("FILL IN THE BLANK");
            questionTypeLabel.setBackground(new Color(100, 149, 237));
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
            trueButton.setBackground(new Color(20, 120, 50));
            falseButton.setBackground(new Color(180, 180, 180));
        } else {
            falseButton.setBackground(new Color(160, 30, 40));
            trueButton.setBackground(new Color(180, 180, 180));
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
            feedbackLabel.setText("✓ Correct!");
            feedbackLabel.setForeground(new Color(40, 167, 69));
        } else {
            feedbackLabel.setText("✗ Incorrect. Answer: " + q.answer);
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
        messageLabel.setText(getMotivationalMessage(percentage));
        detailLabel.setText("<html><center>You answered " + score + " out of " + total
                + " questions correctly.</center></html>");

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
        if (percentage >= 80) return "Outstanding! 🌟";
        if (percentage >= 60) return "That's good! 😊";
        if (percentage >= 40) return "Good try! 💪";
        if (percentage >= 20) return "You can do better! 📚";
        return "Don't give up! ❤️";
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
    //Called by Member 4 (GameManager) to read for leaderboard and history.
     
    private void saveScore(String username, int score, int total, int percentage) {
        try {
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String entry = username + "," + score + "," + total + "," + percentage + "," + timestamp;

            BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE, true)); // append mode
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
