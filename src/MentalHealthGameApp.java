/**
 * Class/Interface: MentalHealthGameApp
 * Creator: Keweil Anak Bansa
 * Tester: G04/SE Group 14
 * Description: Main phone-sized desktop launcher for the HEALIVERSE SDG 3 app.
 */

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MentalHealthGameApp extends JFrame {

    private static final int PHONE_WIDTH = HealiverseTheme.PHONE_WIDTH;
    private static final int PHONE_HEIGHT = HealiverseTheme.PHONE_HEIGHT;

    private static final Color INK = HealiverseTheme.DARK_PURPLE;
    private static final Color MUTED = HealiverseTheme.MUTED_PURPLE;
    private static final Color PAPER = HealiverseTheme.CREAM;
    private static final Color SURFACE = HealiverseTheme.SURFACE;
    private static final Color LINE = HealiverseTheme.LINE;

    private static final Color PINK = HealiverseTheme.PASTEL_PINK;
    private static final Color MINT = HealiverseTheme.MINT;
    private static final Color YELLOW = HealiverseTheme.SOFT_YELLOW;
    private static final Color LAVENDER = HealiverseTheme.LAVENDER;

    private static final Color GREEN_TEXT = new Color(41, 110, 79);
    private static final Color PINK_DARK = new Color(202, 78, 132);
    private static final Color PURPLE_TEXT = new Color(89, 68, 158);

    private final String username;
    private final CardLayout cards;
    private final JPanel screens;
    private final JPanel bottomNav;

    public MentalHealthGameApp(String username) {
        this.username = cleanUsername(username);
        this.cards = new CardLayout();
        this.screens = new JPanel(cards);
        this.bottomNav = new JPanel(new GridLayout(1, 6, 2, 0));

        setTitle("Healiverse - TMF2954 Java Programming Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(createPhoneShell());
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createPhoneShell() {
        JPanel phone = new JPanel(new BorderLayout());
        phone.setPreferredSize(new Dimension(PHONE_WIDTH, PHONE_HEIGHT));
        phone.setMinimumSize(new Dimension(PHONE_WIDTH, PHONE_HEIGHT));
        phone.setBackground(PAPER);
        phone.setBorder(new CompoundBorder(
                new LineBorder(new Color(143, 187, 155), 2),
                new EmptyBorder(6, 6, 6, 6)));

        screens.setBackground(PAPER);
        screens.add(createHomeScreen(), "HOME");

        bottomNav.setBackground(SURFACE);
        bottomNav.setBorder(new CompoundBorder(
                new LineBorder(LINE, 1),
                new EmptyBorder(5, 4, 5, 4)));
        bottomNav.setPreferredSize(new Dimension(PHONE_WIDTH - 16, 58));

        bottomNav.add(navButton("Home", new SimpleIcon("home", PINK_DARK), () -> cards.show(screens, "HOME")));
        bottomNav.add(navButton("Learn", new SimpleIcon("book", PINK_DARK), this::showLearning));
        bottomNav.add(navButton("Quiz", new SimpleIcon("quiz", PINK_DARK), this::showQuiz));
        bottomNav.add(navButton("Rewards", new SimpleIcon("badge", PINK_DARK), this::showRewards));
        bottomNav.add(navButton("Board", new SimpleIcon("rank", PINK_DARK), this::showLeaderboard));
        bottomNav.add(navButton("Logout", new SimpleIcon("exit", PINK_DARK), this::logoutUser));

        phone.add(screens, BorderLayout.CENTER);
        phone.add(bottomNav, BorderLayout.SOUTH);

        return phone;
    }

    // -------------------------------------------------------------------------
    // Home Screen
    // -------------------------------------------------------------------------

    private JPanel createHomeScreen() {
        HomeBackdrop home = new HomeBackdrop();
        home.setLayout(new BorderLayout());
        home.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(createHeroPanel());
        center.add(Box.createVerticalStrut(10));
        center.add(createWelcomeBanner());
        center.add(Box.createVerticalStrut(12));
        center.add(createMenuPanel());
        center.add(Box.createVerticalStrut(12));
        center.add(createDailyReminder());
        center.add(Box.createVerticalGlue());

        home.add(center, BorderLayout.CENTER);
        return home;
    }

    private JPanel createHeroPanel() {
        JPanel hero = new JPanel();
        hero.setOpaque(false);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setAlignmentX(Component.CENTER_ALIGNMENT);
        hero.setMaximumSize(new Dimension(HealiverseTheme.CONTENT_WIDTH, 108));
        hero.setBorder(new EmptyBorder(2, 4, 0, 4));

        JLabel sdg = new JLabel("SDG 3: Good Health and Well-Being");
        sdg.setAlignmentX(Component.CENTER_ALIGNMENT);
        sdg.setFont(HealiverseTheme.buttonFont(11));
        sdg.setForeground(GREEN_TEXT);

        JLabel logo = new JLabel("HEALIVERSE", SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setFont(HealiverseTheme.titleFont(28));
        logo.setForeground(INK);
        logo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("<html><center>Mental health learning, quiz, and rewards</center></html>");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(HealiverseTheme.bodyFont(11));
        subtitle.setForeground(MUTED);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        hero.add(sdg);
        hero.add(Box.createVerticalStrut(5));
        hero.add(logo);
        hero.add(Box.createVerticalStrut(5));
        hero.add(subtitle);

        return hero;
    }

    private JPanel createWelcomeBanner() {
        JPanel banner = cleanCard(new BorderLayout(10, 0), 84);

        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome, " + username + "!");
        title.setFont(HealiverseTheme.buttonFont(14));
        title.setForeground(INK);

        JLabel message = new JLabel("<html>Choose a module and continue your wellness journey.</html>");
        message.setFont(HealiverseTheme.bodyFont(11));
        message.setForeground(MUTED);

        copy.add(Box.createVerticalStrut(4));
        copy.add(title);
        copy.add(Box.createVerticalStrut(5));
        copy.add(message);

        JLabel mascot = imageLabel("Bunny Wave.png", 58, 58);
        if (mascot.getIcon() == null) {
            mascot = new JLabel(new SimpleIcon("leaf", GREEN_TEXT));
        }
        mascot.setHorizontalAlignment(SwingConstants.CENTER);
        mascot.setPreferredSize(new Dimension(62, 62));

        banner.add(copy, BorderLayout.CENTER);
        banner.add(mascot, BorderLayout.EAST);

        return banner;
    }

    private JPanel createMenuPanel() {
        JPanel menu = new JPanel(new GridLayout(2, 2, 8, 8));
        menu.setOpaque(false);
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension size = new Dimension(HealiverseTheme.CONTENT_WIDTH, 292);
        menu.setPreferredSize(size);
        menu.setMinimumSize(size);
        menu.setMaximumSize(size);

        menu.add(dashboardButton("Learn", "10 mental health pages", "Learn Button Dashboard.png", MINT, this::showLearning));
        menu.add(dashboardButton("Quiz", "20 questions", "Quiz Button Dashboard.png", LAVENDER, this::showQuiz));
        menu.add(dashboardButton("Rewards", "Badges and points", "Rewards Button Dashboard.png", PINK, this::showRewards));
        menu.add(dashboardButton("Leaderboard", "Best quiz scores", "Leaderboard Button Dashboard.png", YELLOW, this::showLeaderboard));

        return menu;
    }

    private JButton dashboardButton(String title, String subtitle, String imageName, Color fallbackColor, Runnable action) {
        DashboardButton button = new DashboardButton(title, subtitle, imageName, fallbackColor);
        HealiverseTheme.setFixedSize(button, 166, 142);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> action.run());
        return button;
    }

    private JPanel createDailyReminder() {
        JPanel reminder = cleanCard(new BorderLayout(10, 0), 86);
        reminder.setBackground(new Color(255, 245, 230));

        JLabel icon = imageLabel("Rabbit.png", 58, 58);
        if (icon.getIcon() == null) {
            icon = new JLabel(new SimpleIcon("heart", PINK_DARK));
        }
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setPreferredSize(new Dimension(62, 62));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Daily Reminder");
        title.setFont(HealiverseTheme.buttonFont(12));
        title.setForeground(PINK_DARK);

        JLabel text = new JLabel("<html>Take a deep breath. Every small step counts.</html>");
        text.setFont(HealiverseTheme.bodyFont(11));
        text.setForeground(MUTED);

        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(text);

        reminder.add(icon, BorderLayout.WEST);
        reminder.add(textPanel, BorderLayout.CENTER);

        return reminder;
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    private JButton navButton(String label, Icon icon, Runnable action) {
        JButton button = new FooterButton(label, icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void showLearning() {
        replaceScreen("LEARNING", wrapModule("Learning Module", createLinkedLearningModule()));
    }

    private void showQuiz() {
        replaceScreen("QUIZ", wrapModule("Quiz Challenge", createLinkedQuizModule()));
    }

    private void showRewards() {
        replaceScreen("REWARDS", wrapModule("Rewards", new GamificationModule(username)));
    }

    private void showLeaderboard() {
        replaceScreen("LEADERBOARD", wrapModule("Leaderboard", createLeaderboardScreen()));
    }

    private void logoutUser() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Log out and return to login screen?",
                "Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginApplication().setVisible(true);
        }
    }

    private LearningModule createLinkedLearningModule() {
        return new LearningModule(username) {
            @Override
            public void nextPage() {
                if (getCurrentPageIndex() >= getTotalPages() - 1) {
                    showQuiz();
                } else {
                    super.nextPage();
                }
            }
        };
    }

    private QuizModule createLinkedQuizModule() {
        QuizModule quiz = new QuizModule(username);
        quiz.startQuiz();
        quiz.getMenuButton().addActionListener(e -> cards.show(screens, "HOME"));
        return quiz;
    }

    private JPanel createLeaderboardScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(PAPER);
        screen.setBorder(new EmptyBorder(12, 14, 12, 14));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel image = imageLabel("Leaderboard Button Dashboard.png", 320, 132);
        image.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel card = cleanCard(new BorderLayout(0, 10), 300);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        JLabel title = new JLabel("Top Quiz Scores");
        title.setFont(HealiverseTheme.buttonFont(14));
        title.setForeground(INK);

        JLabel note = new JLabel("Best Scores");
        note.setFont(HealiverseTheme.bodyFont(10));
        note.setForeground(MUTED);

        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(note, BorderLayout.EAST);

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new GridLayout(5, 1, 0, 6));

        List<ScoreEntry> scores = new ArrayList<>();

        try {
            scores = new GamificationModule(username).loadLeaderboard(5);
        } catch (GamificationDataException e) {
            // Empty leaderboard will be shown if scores cannot be loaded.
        }

        if (scores.isEmpty()) {
            rows.add(emptyRankRow("No quiz scores yet."));
            rows.add(emptyRankRow("Finish the quiz to appear here."));
            rows.add(emptyRankRow("—"));
            rows.add(emptyRankRow("—"));
            rows.add(emptyRankRow("—"));
        } else {
            for (int i = 0; i < 5; i++) {
                if (i < scores.size()) {
                    rows.add(rankRow(i + 1, scores.get(i)));
                } else {
                    rows.add(emptyRankRow((i + 1) + "   —"));
                }
            }
        }

        card.add(titleRow, BorderLayout.NORTH);
        card.add(rows, BorderLayout.CENTER);

        JLabel bottom = new JLabel("Your quiz score is saved from the Quiz Module.", SwingConstants.CENTER);
        bottom.setFont(HealiverseTheme.bodyFont(10));
        bottom.setForeground(MUTED);
        bottom.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(image);
        content.add(Box.createVerticalStrut(12));
        content.add(card);
        content.add(Box.createVerticalStrut(10));
        content.add(bottom);
        content.add(Box.createVerticalGlue());

        screen.add(content, BorderLayout.CENTER);
        return screen;
    }

    private JPanel rankRow(int rank, ScoreEntry entry) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        boolean currentUser = entry.getUsername().equalsIgnoreCase(username);

        row.setBackground(currentUser ? new Color(207, 243, 213) : new Color(255, 252, 247));
        row.setBorder(new CompoundBorder(
                new LineBorder(currentUser ? new Color(110, 178, 132) : LINE, 1),
                new EmptyBorder(8, 10, 8, 10)));

        JLabel left = new JLabel(rank + "   " + entry.getUsername());
        left.setFont(HealiverseTheme.buttonFont(11));
        left.setForeground(INK);

        JLabel right = new JLabel(entry.getPercentage() + "%");
        right.setFont(HealiverseTheme.buttonFont(11));
        right.setForeground(INK);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        return row;
    }

    private JLabel emptyRankRow(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(255, 252, 247));
        label.setBorder(new LineBorder(LINE, 1));
        label.setFont(HealiverseTheme.bodyFont(10));
        label.setForeground(MUTED);
        return label;
    }

    private JPanel wrapModule(String title, JComponent content) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(PAPER);

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setBackground(new Color(239, 225, 252));
        top.setBorder(new CompoundBorder(
                new LineBorder(LINE, 1),
                new EmptyBorder(5, 8, 5, 8)));

        JButton back = new JButton("<");
        back.setFont(HealiverseTheme.buttonFont(13));
        back.setForeground(INK);
        back.setBackground(SURFACE);
        back.setFocusPainted(false);
        back.setBorder(new CompoundBorder(
                new LineBorder(LINE, 1),
                new EmptyBorder(2, 8, 2, 8)));
        HealiverseTheme.setFixedSize(back, 34, 28);
        back.addActionListener(e -> cards.show(screens, "HOME"));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(HealiverseTheme.buttonFont(13));
        label.setForeground(PINK_DARK);

        JLabel spacer = new JLabel(" ");
        HealiverseTheme.setFixedSize(spacer, 34, 28);

        top.add(back, BorderLayout.WEST);
        top.add(label, BorderLayout.CENTER);
        top.add(spacer, BorderLayout.EAST);

        wrapper.add(top, BorderLayout.NORTH);
        wrapper.add(content, BorderLayout.CENTER);

        return wrapper;
    }

    private void replaceScreen(String name, JPanel screen) {
        for (Component component : screens.getComponents()) {
            if (name.equals(component.getName())) {
                screens.remove(component);
                break;
            }
        }

        screen.setName(name);
        screens.add(screen, name);
        cards.show(screens, name);
        screens.revalidate();
        screens.repaint();
    }

    // -------------------------------------------------------------------------
    // UI Helpers
    // -------------------------------------------------------------------------

    private JPanel cleanCard(LayoutManager layout, int height) {
        JPanel card = new JPanel(layout);
        card.setBackground(SURFACE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(216, 194, 190), 1),
                new EmptyBorder(10, 12, 10, 12)));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension size = new Dimension(HealiverseTheme.CONTENT_WIDTH, height);
        card.setPreferredSize(size);
        card.setMinimumSize(size);
        card.setMaximumSize(size);

        return card;
    }

    private static JLabel imageLabel(String fileName, int maxWidth, int maxHeight) {
        JLabel label = new JLabel(loadPixelIcon(fileName, maxWidth, maxHeight));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private static ImageIcon loadPixelIcon(String fileName, int maxWidth, int maxHeight) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }
        return HealiversePaths.loadPixelIcon(fileName, maxWidth, maxHeight);
    }

    private static String cleanUsername(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "guest";
        }
        return value.trim();
    }

    // -------------------------------------------------------------------------
    // Main
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            if (args.length > 0) {
                new MentalHealthGameApp(cleanUsername(args[0])).setVisible(true);
            } else {
                new LoginApplication().setVisible(true);
            }
        });
    }

    // -------------------------------------------------------------------------
    // Custom Components
    // -------------------------------------------------------------------------

    private static class DashboardButton extends JButton {
        private final String title;
        private final String subtitle;
        private final String imageName;
        private final Color fallbackColor;

        DashboardButton(String title, String subtitle, String imageName, Color fallbackColor) {
            super("");
            this.title = title;
            this.subtitle = subtitle;
            this.imageName = imageName;
            this.fallbackColor = fallbackColor;

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(0, 0, 0, 0));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            int offset = getModel().isArmed() ? 1 : 0;
            ImageIcon icon = loadPixelIcon(imageName, getWidth() - 4, getHeight() - 4);

            if (icon != null) {
                int x = (getWidth() - icon.getIconWidth()) / 2;
                int y = (getHeight() - icon.getIconHeight()) / 2 + offset;
                icon.paintIcon(this, g, x, y);
            } else {
                g.setColor(fallbackColor);
                g.fillRoundRect(0, offset, getWidth() - 1, getHeight() - 2, 10, 10);
                g.setColor(PURPLE_TEXT);
                g.drawRoundRect(0, offset, getWidth() - 2, getHeight() - 3, 10, 10);

                g.setColor(INK);
                g.setFont(HealiverseTheme.buttonFont(16));
                drawCentered(g, title, 62 + offset);

                g.setColor(MUTED);
                g.setFont(HealiverseTheme.bodyFont(10));
                drawCentered(g, subtitle, 82 + offset);
            }

            g.dispose();
        }

        private void drawCentered(Graphics2D g, String text, int baseline) {
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(text)) / 2;
            g.drawString(text, x, baseline);
        }
    }

    private static class FooterButton extends JButton {
        FooterButton(String label, Icon icon) {
            super(label, icon);
            setFont(HealiverseTheme.buttonFont(7));
            setForeground(PURPLE_TEXT);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(4, 0, 2, 0));
            setIconTextGap(2);
            setMargin(new Insets(0, 0, 0, 0));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isRollover()) {
                g.setColor(new Color(255, 252, 247, 210));
                g.fillRoundRect(3, 2, getWidth() - 6, getHeight() - 4, 10, 10);
            }

            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class HomeBackdrop extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            GradientPaint gradient = new GradientPaint(
                    0,
                    0,
                    new Color(248, 252, 249),
                    0,
                    getHeight(),
                    new Color(232, 244, 238));

            g.setPaint(gradient);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.dispose();
        }
    }

    private static class SimpleIcon implements Icon {
        private final String type;
        private final Color color;

        SimpleIcon(String type, Color color) {
            this.type = type;
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 18;
        }

        @Override
        public void paintIcon(Component c, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(2f));
            g.setColor(color);

            if ("home".equals(type)) {
                Polygon roof = new Polygon();
                roof.addPoint(x + 2, y + 9);
                roof.addPoint(x + 9, y + 3);
                roof.addPoint(x + 16, y + 9);
                g.drawPolygon(roof);
                g.drawRect(x + 5, y + 9, 8, 6);
            } else if ("book".equals(type)) {
                g.drawRect(x + 3, y + 4, 12, 11);
                g.drawLine(x + 9, y + 4, x + 9, y + 15);
            } else if ("quiz".equals(type)) {
                g.drawOval(x + 3, y + 3, 12, 12);
                g.drawString("?", x + 7, y + 13);
            } else if ("badge".equals(type)) {
                g.drawOval(x + 4, y + 3, 10, 10);
                g.drawLine(x + 7, y + 13, x + 5, y + 17);
                g.drawLine(x + 11, y + 13, x + 13, y + 17);
            } else if ("rank".equals(type)) {
                g.drawLine(x + 4, y + 14, x + 4, y + 9);
                g.drawLine(x + 9, y + 14, x + 9, y + 5);
                g.drawLine(x + 14, y + 14, x + 14, y + 2);
            } else if ("exit".equals(type)) {
                g.drawRect(x + 3, y + 4, 9, 10);
                g.drawLine(x + 9, y + 9, x + 16, y + 9);
                g.drawLine(x + 13, y + 6, x + 16, y + 9);
                g.drawLine(x + 13, y + 12, x + 16, y + 9);
            } else {
                g.drawLine(x + 5, y + 5, x + 13, y + 13);
                g.drawLine(x + 13, y + 5, x + 5, y + 13);
            }

            g.dispose();
        }
    }
}
