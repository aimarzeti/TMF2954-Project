/**
 * Class/Interface: QuizModule
 * Creator: Noor Azuah binti Sawal (105404)
 * Tester: G04/SE Group 14
 * Description: Quiz module for SDG 3: Good Health and Well-being (Mental Health).
 * Contains True/False and Fill in the Blank question types.
 * Saves score and username to scores.txt upon quiz completion.
 */

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class QuizModule extends JPanel implements Quizable {

    private static final String SCORES_FILE = "scores.txt";

    private static final Color TEAL = HealiverseTheme.MINT;
    private static final Color LIGHT_BLUE = HealiverseTheme.BABY_BLUE;
    private static final Color DARK_TEXT = HealiverseTheme.DARK_PURPLE;
    private static final Color PINK = HealiverseTheme.PASTEL_PINK;
    private static final Color WHITE = HealiverseTheme.CREAM;
    private static final Color SURFACE = HealiverseTheme.SURFACE;
    private static final Color LINE = HealiverseTheme.LINE;
    private static final Color MUTED_TEXT = HealiverseTheme.MUTED_PURPLE;
    private static final Color PURPLE = new Color(100, 82, 166);
    private static final Color POPUP_BG = new Color(255, 252, 247);

    private static final int MODULE_WIDTH = HealiverseTheme.MODULE_WIDTH;
    private static final int MODULE_HEIGHT = HealiverseTheme.MODULE_HEIGHT;
    private static final int CARD_WIDTH = 320;
    private static final int ANSWER_WIDTH = 286;

    static class Question {
        String type;
        String questionText;
        String answer;
        String hint;

        public Question(String type, String questionText, String answer, String hint) {
            this.type = type;
            this.questionText = questionText;
            this.answer = answer;
            this.hint = hint;
        }

        public Question(String type, String questionText, String answer) {
            this(type, questionText, answer, "");
        }
    }

    private List<Question> questions;
    private int currentIndex;
    private int score;
    private String username;
    private String userAnswer;

    private boolean quizStarterShown;
    private boolean brainBoosterShown;
    private boolean smartSproutShown;
    private boolean wellnessProShown;
    private boolean perfectMindShown;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JLabel questionNumberLabel;
    private JProgressBar questionProgress;
    private JLabel questionTypeLabel;
    private JTextArea questionTextArea;
    private JPanel answerPanel;
    private JButton trueButton;
    private JButton falseButton;
    private JTextField fitbField;
    private JLabel hintLabel;
    private JButton nextButton;
    private JLabel feedbackLabel;
    private JLabel heartLabel;

    private JLabel resultBadgeLabel;
    private JLabel scoreLabel;
    private JLabel percentageLabel;
    private JLabel messageLabel;
    private JLabel detailLabel;
    private JButton retryButton;
    private JButton menuButton;

    public QuizModule(String username) {
        this.username = cleanUsername(username);
        this.questions = new ArrayList<>();
        this.currentIndex = 0;
        this.score = 0;

        buildQuestions();
        buildUI();
        resetLiveBadges();
    }

    public QuizModule() {
        this("guest");
    }

    private void buildQuestions() {
        questions.add(new Question("TF",
                "According to the WHO, mental health is a state of well-being where a person can realise their own potential and contribute to their community.",
                "True"));

        questions.add(new Question("FITB",
                "Mental illness is NOT a sign of weakness — it is a _______ issue.",
                "medical",
                "Hint: Think about what doctors treat."));

        questions.add(new Question("TF",
                "The mental health spectrum only has two states: healthy and ill.",
                "False"));

        questions.add(new Question("FITB",
                "When a person is unable to function and needs immediate professional support, they are said to be in ______.",
                "crisis",
                "Hint: The most severe point on the spectrum."));

        questions.add(new Question("TF",
                "Depression affects over 280 million people globally according to the WHO.",
                "True"));

        questions.add(new Question("FITB",
                "Excessive worry or fear that interferes with daily life is called an _______ disorder.",
                "anxiety",
                "Hint: Feeling nervous or fearful."));

        questions.add(new Question("TF",
                "Sleeping too much or too little can be a warning sign of a mental health struggle.",
                "True"));

        questions.add(new Question("FITB",
                "Avoiding friends, family, and activities you used to enjoy is known as social __________.",
                "withdrawal",
                "Hint: Pulling away from others."));

        questions.add(new Question("TF",
                "High cortisol levels over a long period can damage memory and mood.",
                "True"));

        questions.add(new Question("FITB",
                "The neurotransmitter that regulates mood, sleep, and appetite is called _________.",
                "serotonin",
                "Hint: Low levels of this are linked to depression."));

        questions.add(new Question("TF",
                "According to the NHMS 2019, 1 in 3 Malaysians experience a mental health issue.",
                "True"));

        questions.add(new Question("FITB",
                "One major barrier to seeking mental health help in Malaysia is social ______, also known as 'malu'.",
                "stigma",
                "Hint: Fear of being judged by others."));

        questions.add(new Question("TF",
                "30 minutes of moderate exercise 5 times a week can reduce depression symptoms.",
                "True"));

        questions.add(new Question("FITB",
                "Writing your thoughts and feelings in a diary is called __________.",
                "journaling",
                "Hint: A personal writing practice."));

        questions.add(new Question("TF",
                "In the 4-7-8 breathing technique, you breathe out for 4 seconds.",
                "False"));

        questions.add(new Question("FITB",
                "The 5-4-3-2-1 grounding technique helps anchor you to the _______ moment.",
                "present",
                "Hint: The opposite of past or future."));

        questions.add(new Question("TF",
                "You should seek professional help if your symptoms have lasted more than 2 weeks.",
                "True"));

        questions.add(new Question("FITB",
                "A ____________ is a medical doctor who can diagnose mental health conditions and prescribe medication.",
                "psychiatrist",
                "Hint: A specialised mental health doctor."));

        questions.add(new Question("TF",
                "Befrienders Kuala Lumpur operates a 24-hour emotional support hotline.",
                "True"));

        questions.add(new Question("FITB",
                "Most university counselling services are offered for ____ to students.",
                "free",
                "Hint: No cost involved."));
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(WHITE);
        setPreferredSize(new Dimension(MODULE_WIDTH, MODULE_HEIGHT));
        setMinimumSize(new Dimension(MODULE_WIDTH, MODULE_HEIGHT));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(WHITE);

        mainPanel.add(buildQuizScreen(), "QUIZ");
        mainPanel.add(buildResultScreen(), "RESULT");

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel buildQuizScreen() {
        JPanel screen = new JPanel(new BorderLayout(0, 0));
        screen.setBackground(WHITE);

        JPanel topBar = new JPanel(new BorderLayout(8, 4));
        topBar.setBackground(new Color(255, 226, 240));
        topBar.setBorder(new EmptyBorder(8, 12, 8, 12));

        questionNumberLabel = new JLabel("Question 1 of " + questions.size());
        questionNumberLabel.setFont(HealiverseTheme.buttonFont(12));
        questionNumberLabel.setForeground(DARK_TEXT);

        heartLabel = new JLabel("20 Questions", loadPixelIcon("Star Badge.png", 20, 20), SwingConstants.RIGHT);
        heartLabel.setFont(HealiverseTheme.buttonFont(11));
        heartLabel.setForeground(MUTED_TEXT);

        questionProgress = new JProgressBar(0, questions.size());
        questionProgress.setValue(1);
        questionProgress.setForeground(TEAL);
        questionProgress.setBackground(LINE);
        questionProgress.setPreferredSize(new Dimension(0, 7));
        questionProgress.setBorder(new LineBorder(HealiverseTheme.PIXEL_PURPLE, 1));

        topBar.add(questionNumberLabel, BorderLayout.WEST);
        topBar.add(heartLabel, BorderLayout.EAST);
        topBar.add(questionProgress, BorderLayout.SOUTH);

        screen.add(topBar, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WHITE);
        content.setBorder(new EmptyBorder(10, 8, 10, 8));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SURFACE);
        card.setBorder(HealiverseTheme.cardBorder(LIGHT_BLUE, 12));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setPreferredSize(new Dimension(CARD_WIDTH, 400));
        card.setMinimumSize(new Dimension(CARD_WIDTH, 400));
        card.setMaximumSize(new Dimension(CARD_WIDTH, 430));

        questionTypeLabel = new JLabel("TRUE / FALSE");
        questionTypeLabel.setFont(HealiverseTheme.buttonFont(11));
        questionTypeLabel.setForeground(Color.WHITE);
        questionTypeLabel.setOpaque(true);
        questionTypeLabel.setBackground(TEAL);
        questionTypeLabel.setBorder(new CompoundBorder(
                new LineBorder(TEAL, 1),
                new EmptyBorder(5, 10, 5, 10)));
        questionTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mascotLabel = imageLabel("Brain Standing with Star.png", 92, 68);
        if (mascotLabel.getIcon() == null) {
            mascotLabel.setText("Quiz Time!");
            mascotLabel.setFont(HealiverseTheme.buttonFont(13));
            mascotLabel.setForeground(PURPLE);
        }
        mascotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionTextArea = new JTextArea();
        questionTextArea.setFont(HealiverseTheme.bodyFont(13));
        questionTextArea.setForeground(DARK_TEXT);
        questionTextArea.setBackground(SURFACE);
        questionTextArea.setEditable(false);
        questionTextArea.setFocusable(false);
        questionTextArea.setOpaque(true);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionTextArea.setBorder(new EmptyBorder(2, 2, 2, 2));
        questionTextArea.setPreferredSize(new Dimension(ANSWER_WIDTH, 84));
        questionTextArea.setMinimumSize(new Dimension(ANSWER_WIDTH, 84));
        questionTextArea.setMaximumSize(new Dimension(ANSWER_WIDTH, 110));

        answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setBackground(SURFACE);
        answerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answerPanel.setPreferredSize(new Dimension(ANSWER_WIDTH, 116));
        answerPanel.setMaximumSize(new Dimension(ANSWER_WIDTH, 140));

        trueButton = buildAnswerButton("TRUE", TEAL);
        falseButton = buildAnswerButton("FALSE", PINK);

        trueButton.addActionListener(e -> selectTF("True"));
        falseButton.addActionListener(e -> selectTF("False"));

        fitbField = new JTextField();
        fitbField.setFont(HealiverseTheme.bodyFont(14));
        fitbField.setForeground(DARK_TEXT);
        HealiverseTheme.setFixedSize(fitbField, 245, HealiverseTheme.BUTTON_HEIGHT);
        fitbField.setHorizontalAlignment(JTextField.CENTER);
        fitbField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1, true),
                new EmptyBorder(8, 12, 8, 12)));

        hintLabel = new JLabel(" ");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel.setForeground(MUTED_TEXT);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintLabel.setPreferredSize(new Dimension(ANSWER_WIDTH, 28));
        hintLabel.setMaximumSize(new Dimension(ANSWER_WIDTH, 34));

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(HealiverseTheme.buttonFont(12));
        feedbackLabel.setForeground(TEAL);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setPreferredSize(new Dimension(ANSWER_WIDTH, 34));
        feedbackLabel.setMaximumSize(new Dimension(ANSWER_WIDTH, 40));

        card.add(questionTypeLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(mascotLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(questionTextArea);
        card.add(Box.createVerticalStrut(12));
        card.add(answerPanel);
        card.add(Box.createVerticalStrut(8));
        card.add(feedbackLabel);

        content.add(card);
        content.add(Box.createVerticalGlue());

        screen.add(scrollPane, BorderLayout.CENTER);

        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(WHITE);
        navBar.setBorder(new EmptyBorder(8, 16, 12, 16));

        nextButton = new JButton("Next >");
        HealiverseTheme.stylePixelButton(nextButton, PINK);
        HealiverseTheme.setFixedSize(nextButton, 300, HealiverseTheme.COMPACT_BUTTON_HEIGHT);
        nextButton.addActionListener(e -> handleNext());

        navBar.add(nextButton, BorderLayout.CENTER);
        screen.add(navBar, BorderLayout.SOUTH);

        return screen;
    }

    private JButton buildAnswerButton(String text, Color bg) {
        JButton button = new JButton(text);
        HealiverseTheme.stylePixelButton(button, bg);
        HealiverseTheme.setFixedSize(button, ANSWER_WIDTH, HealiverseTheme.BUTTON_HEIGHT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private JPanel buildResultScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(255, 226, 240));
        header.setPreferredSize(new Dimension(MODULE_WIDTH, 48));

        JLabel title = new JLabel("Quiz Complete!", SwingConstants.CENTER);
        title.setFont(HealiverseTheme.buttonFont(16));
        title.setForeground(DARK_TEXT);
        header.add(title, BorderLayout.CENTER);

        screen.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WHITE);
        content.setBorder(new EmptyBorder(14, 14, 14, 14));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        JPanel resultCard = new JPanel();
        resultCard.setLayout(new BoxLayout(resultCard, BoxLayout.Y_AXIS));
        resultCard.setBackground(SURFACE);
        resultCard.setBorder(HealiverseTheme.cardBorder(HealiverseTheme.SOFT_YELLOW, 12));
        resultCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultCard.setPreferredSize(new Dimension(320, 430));
        resultCard.setMinimumSize(new Dimension(320, 430));
        resultCard.setMaximumSize(new Dimension(320, 460));

        resultBadgeLabel = imageLabel("Quiz Star Badge.png", 106, 94);
        if (resultBadgeLabel.getIcon() == null) {
            resultBadgeLabel.setText("Quiz Summary");
            resultBadgeLabel.setFont(HealiverseTheme.buttonFont(15));
            resultBadgeLabel.setForeground(MUTED_TEXT);
        }
        resultBadgeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = centerLabel("", new Font("SansSerif", Font.BOLD, 36), TEAL);
        percentageLabel = centerLabel("", new Font("SansSerif", Font.BOLD, 20), DARK_TEXT);
        messageLabel = centerLabel("", HealiverseTheme.buttonFont(15), TEAL);
        detailLabel = centerLabel("", HealiverseTheme.bodyFont(12), MUTED_TEXT);

        retryButton = new JButton("Try Again");
        HealiverseTheme.stylePixelButton(retryButton, TEAL);
        HealiverseTheme.setFixedSize(retryButton, 260, HealiverseTheme.COMPACT_BUTTON_HEIGHT);
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.addActionListener(e -> resetQuiz());

        menuButton = new JButton("Back to Menu");
        HealiverseTheme.stylePixelButton(menuButton, LIGHT_BLUE);
        HealiverseTheme.setFixedSize(menuButton, 260, HealiverseTheme.COMPACT_BUTTON_HEIGHT);
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultCard.add(Box.createVerticalStrut(4));
        resultCard.add(resultBadgeLabel);
        resultCard.add(Box.createVerticalStrut(4));
        resultCard.add(scoreLabel);
        resultCard.add(Box.createVerticalStrut(4));
        resultCard.add(percentageLabel);
        resultCard.add(Box.createVerticalStrut(10));
        resultCard.add(messageLabel);
        resultCard.add(Box.createVerticalStrut(8));
        resultCard.add(detailLabel);
        resultCard.add(Box.createVerticalStrut(16));
        resultCard.add(retryButton);
        resultCard.add(Box.createVerticalStrut(8));
        resultCard.add(menuButton);

        content.add(resultCard);
        content.add(Box.createVerticalGlue());

        screen.add(scrollPane, BorderLayout.CENTER);

        return screen;
    }

    private JLabel centerLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(286, 32));
        label.setMaximumSize(new Dimension(286, 60));
        return label;
    }

    private void loadQuestion() {
        if (currentIndex >= questions.size()) {
            return;
        }

        Question question = questions.get(currentIndex);
        userAnswer = null;
        feedbackLabel.setText(" ");
        nextButton.setEnabled(true);

        questionNumberLabel.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        questionProgress.setValue(currentIndex + 1);

        questionTextArea.setText(question.questionText);
        questionTextArea.setCaretPosition(0);

        answerPanel.removeAll();

        if ("TF".equals(question.type)) {
            questionTypeLabel.setText("TRUE / FALSE");
            questionTypeLabel.setBackground(TEAL);

            trueButton.setBackground(TEAL);
            falseButton.setBackground(PINK);

            answerPanel.add(trueButton);
            answerPanel.add(Box.createVerticalStrut(10));
            answerPanel.add(falseButton);
        } else {
            questionTypeLabel.setText("FILL IN THE BLANK");
            questionTypeLabel.setBackground(PURPLE);

            fitbField.setText("");
            hintLabel.setText("<html><center>" + question.hint + "</center></html>");

            JPanel inputWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
            inputWrapper.setOpaque(false);
            inputWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
            inputWrapper.setMaximumSize(new Dimension(ANSWER_WIDTH, 46));
            inputWrapper.add(fitbField);

            answerPanel.add(inputWrapper);
            answerPanel.add(Box.createVerticalStrut(6));
            answerPanel.add(hintLabel);
        }

        answerPanel.revalidate();
        answerPanel.repaint();
    }

    private void selectTF(String selected) {
        userAnswer = selected;

        if ("True".equals(selected)) {
            trueButton.setBackground(new Color(91, 151, 93));
            falseButton.setBackground(new Color(210, 210, 210));
        } else {
            falseButton.setBackground(new Color(202, 78, 132));
            trueButton.setBackground(new Color(210, 210, 210));
        }
    }

    private void handleNext() {
        Question question = questions.get(currentIndex);

        if ("TF".equals(question.type)) {
            if (userAnswer == null) {
                feedbackLabel.setText("<html><center>Please select True or False!</center></html>");
                feedbackLabel.setForeground(PINK);
                return;
            }
        } else {
            userAnswer = fitbField.getText().trim();

            if (userAnswer.isEmpty()) {
                feedbackLabel.setText("<html><center>Please type your answer!</center></html>");
                feedbackLabel.setForeground(PINK);
                return;
            }
        }

        if (checkAnswer(userAnswer)) {
            score++;
            feedbackLabel.setText("<html><center>Correct!</center></html>");
            feedbackLabel.setForeground(new Color(91, 151, 93));
            checkLiveBadgeUnlock();
        } else {
            feedbackLabel.setText("<html><center>Incorrect. Answer: " + question.answer + "</center></html>");
            feedbackLabel.setForeground(PINK);
        }

        nextButton.setEnabled(false);

        javax.swing.Timer timer = new javax.swing.Timer(850, e -> {
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

    private void checkLiveBadgeUnlock() {
        if (score >= 1 && !quizStarterShown) {
            quizStarterShown = true;
            showLiveBadgePopup("Quiz Starter", "You answered your first question correctly!");
        } else if (score >= 5 && !brainBoosterShown) {
            brainBoosterShown = true;
            showLiveBadgePopup("Brain Booster", "You reached 5 correct answers!");
        } else if (score >= 10 && !smartSproutShown) {
            smartSproutShown = true;
            showLiveBadgePopup("Smart Sprout", "You reached 10 correct answers!");
        } else if (score >= 15 && !wellnessProShown) {
            wellnessProShown = true;
            showLiveBadgePopup("Wellness Pro", "You reached 15 correct answers!");
        } else if (score >= 20 && !perfectMindShown) {
            perfectMindShown = true;
            showLiveBadgePopup("Perfect Mind", "Perfect score! Amazing work!");
        }
    }

    private void showLiveBadgePopup(String badgeName, String message) {
        final JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Quiz Reward", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(POPUP_BG);
        outer.setBorder(new CompoundBorder(
                new LineBorder(new Color(202, 176, 230), 2),
                new EmptyBorder(14, 16, 14, 16)));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Badge Unlocked!", SwingConstants.CENTER);
        title.setFont(HealiverseTheme.buttonFont(16));
        title.setForeground(DARK_TEXT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setPreferredSize(new Dimension(250, 24));
        title.setMaximumSize(new Dimension(250, 24));

        JLabel badgeImage = imageLabel(getLiveBadgeImage(badgeName), 150, 150);
        badgeImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        badgeImage.setPreferredSize(new Dimension(160, 160));
        badgeImage.setMinimumSize(new Dimension(160, 160));
        badgeImage.setMaximumSize(new Dimension(160, 160));

        JLabel badgeNameLabel = new JLabel("<html><center>" + badgeName + "</center></html>", SwingConstants.CENTER);
        badgeNameLabel.setFont(HealiverseTheme.buttonFont(14));
        badgeNameLabel.setForeground(PURPLE);
        badgeNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        badgeNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        badgeNameLabel.setPreferredSize(new Dimension(250, 28));
        badgeNameLabel.setMaximumSize(new Dimension(250, 28));

        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        messageLabel.setFont(HealiverseTheme.bodyFont(11));
        messageLabel.setForeground(MUTED_TEXT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setPreferredSize(new Dimension(250, 34));
        messageLabel.setMaximumSize(new Dimension(250, 40));

        JLabel footerLabel = new JLabel("<html><center>Check Rewards for points, stars, badges, and leaderboard.</center></html>", SwingConstants.CENTER);
        footerLabel.setFont(HealiverseTheme.bodyFont(10));
        footerLabel.setForeground(MUTED_TEXT);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerLabel.setPreferredSize(new Dimension(250, 42));
        footerLabel.setMaximumSize(new Dimension(250, 48));

        JButton okButton = new JButton("OK");
        HealiverseTheme.stylePixelButton(okButton, PINK);
        HealiverseTheme.setFixedSize(okButton, 180, HealiverseTheme.COMPACT_BUTTON_HEIGHT);
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> dialog.dispose());

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(badgeImage);
        content.add(Box.createVerticalStrut(8));
        content.add(badgeNameLabel);
        content.add(Box.createVerticalStrut(4));
        content.add(messageLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(footerLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(okButton);

        outer.add(content, BorderLayout.CENTER);

        dialog.setContentPane(outer);
        dialog.pack();
        dialog.setSize(310, 390);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String getLiveBadgeImage(String badgeName) {
        if ("Quiz Starter".equals(badgeName)) {
            return "Quiz Starter Badge.png";
        }
        if ("Brain Booster".equals(badgeName)) {
            return "Brain Booster Badge.png";
        }
        if ("Smart Sprout".equals(badgeName)) {
            return "Smart Sprout Badge.png";
        }
        if ("Wellness Pro".equals(badgeName)) {
            return "Wellness Pro Badge.png";
        }
        if ("Perfect Mind".equals(badgeName)) {
            return "Perfect Mind Badge.png";
        }

        return "Quiz Starter Badge.png";
    }

    private void resetLiveBadges() {
        quizStarterShown = false;
        brainBoosterShown = false;
        smartSproutShown = false;
        wellnessProShown = false;
        perfectMindShown = false;
    }

    private void showResult() {
        int total = questions.size();
        int percentage = calculateScore();

        scoreLabel.setText(score + " / " + total);
        percentageLabel.setText(percentage + "%");

        resultBadgeLabel.setIcon(loadPixelIcon(
                percentage >= 80 ? "Top Score Badge.png" : "Quiz Star Badge.png",
                106,
                94));

        String stars;

        if (percentage >= 80) {
            stars = "3 Stars";
        } else if (percentage >= 50) {
            stars = "2 Stars";
        } else {
            stars = "1 Star";
        }

        messageLabel.setText("<html><center>" + stars + " - " + getMotivationalMessage(percentage) + "</center></html>");

        detailLabel.setText(
                "<html><center>"
                        + "Wellness Journey Complete<br><br>"
                        + "You answered " + score + " out of " + total + " questions correctly."
                        + "</center></html>");

        saveScore(username, score, total, percentage);
        cardLayout.show(mainPanel, "RESULT");
    }

    @Override
    public void startQuiz() {
        currentIndex = 0;
        score = 0;
        resetLiveBadges();
        loadQuestion();
        cardLayout.show(mainPanel, "QUIZ");
    }

    @Override
    public boolean checkAnswer(String answer) {
        Question question = questions.get(currentIndex);
        return answer.trim().equalsIgnoreCase(question.answer.trim());
    }

    @Override
    public int calculateScore() {
        if (questions.isEmpty()) {
            return 0;
        }

        return (int) Math.round((score * 100.0) / questions.size());
    }

    @Override
    public String getMotivationalMessage(int percentage) {
        if (percentage >= 80) {
            return "Outstanding!";
        }
        if (percentage >= 60) {
            return "That’s good!";
        }
        if (percentage >= 40) {
            return "Good try!";
        }
        if (percentage >= 20) {
            return "You can do better!";
        }
        return "Don’t give up!";
    }

    @Override
    public void resetQuiz() {
        currentIndex = 0;
        score = 0;
        resetLiveBadges();
        loadQuestion();
        cardLayout.show(mainPanel, "QUIZ");
    }

    private void saveScore(String username, int score, int total, int percentage) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String entry = username + "," + score + "," + total + "," + percentage + "," + timestamp;

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(HealiversePaths.writableDataFile(SCORES_FILE), true));

            writer.write(entry);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("Could not save score: " + e.getMessage());
        }
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public int getFinalScore() {
        return score;
    }

    public int getFinalPercentage() {
        return calculateScore();
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

    private String cleanUsername(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "guest";
        }

        return value.trim();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
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
