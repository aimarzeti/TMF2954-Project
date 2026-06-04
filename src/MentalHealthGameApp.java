// Class: MentalHealthGameApp
// Creator: Zeti Nur Aimar binti Ali
// Tester: G04/SE Group 14
// Description: Phone-sized desktop launcher that connects learning, quiz,
// and gamification screens with a pastel pixel wellness experience.

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MentalHealthGameApp extends JFrame {

    private static final int PHONE_WIDTH = HealiverseTheme.PHONE_WIDTH;
    private static final int PHONE_HEIGHT = HealiverseTheme.PHONE_HEIGHT;

    private static final Color INK = HealiverseTheme.DARK_PURPLE;
    private static final Color MUTED = HealiverseTheme.MUTED_PURPLE;
    private static final Color PAPER = HealiverseTheme.CREAM;
    private static final Color SURFACE = HealiverseTheme.SURFACE;
    private static final Color LINE = HealiverseTheme.LINE;
    private static final Color PRIMARY = HealiverseTheme.PASTEL_PINK;
    private static final Color PRIMARY_DARK = new Color(202, 78, 132);
    private static final Color SAGE = HealiverseTheme.MINT;
    private static final Color ROSE = HealiverseTheme.PASTEL_PINK;
    private static final Color GOLD = HealiverseTheme.SOFT_YELLOW;
    private static final Color BLUE = HealiverseTheme.BABY_BLUE;
    private static final Color LAVENDER = HealiverseTheme.LAVENDER;

    private final String username;
    private final CardLayout cards;
    private final JPanel screens;
    private final JPanel bottomNav;

    public MentalHealthGameApp(String username) {
        this.username = cleanUsername(username);
        this.cards = new CardLayout();
        this.screens = new JPanel(cards);
        this.bottomNav = new JPanel(new GridLayout(1, 5, 2, 0));

        setTitle("Healiverse - TMF2954 Java Programming Project (G14 - G04/SE)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(createPhoneShell());
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createPhoneShell() {
        JPanel phone = new JPanel(new BorderLayout());
        phone.setPreferredSize(new Dimension(PHONE_WIDTH, PHONE_HEIGHT));
        phone.setBackground(PAPER);
        phone.setBorder(new CompoundBorder(
                new LineBorder(new Color(143, 104, 204), 3),
                new EmptyBorder(7, 7, 7, 7)));

        screens.setBackground(PAPER);
        screens.add(createHomeScreen(), "HOME");

        bottomNav.setBackground(HealiverseTheme.SURFACE);
        bottomNav.setBorder(new CompoundBorder(
                new LineBorder(HealiverseTheme.LINE, 1),
                new EmptyBorder(5, 8, 5, 8)));
        bottomNav.setPreferredSize(new Dimension(PHONE_WIDTH - 20, 58));
        bottomNav.add(navButton("Home", new SimpleIcon("home", PRIMARY_DARK), () -> cards.show(screens, "HOME")));
        bottomNav.add(navButton("Learn", new SimpleIcon("book", PRIMARY_DARK), this::showLearning));
        bottomNav.add(navButton("Quiz", new SimpleIcon("quiz", PRIMARY_DARK), this::showQuiz));
        bottomNav.add(navButton("Rewards", new SimpleIcon("badge", PRIMARY_DARK), this::showRewards));
        bottomNav.add(navButton("Profile", new SimpleIcon("rank", PRIMARY_DARK), this::showProfile));

        phone.add(screens, BorderLayout.CENTER);
        phone.add(bottomNav, BorderLayout.SOUTH);
        return phone;
    }

    private JPanel createHomeScreen() {
        HomeBackdrop home = new HomeBackdrop();
        home.setLayout(new BorderLayout());
        home.setBorder(new EmptyBorder(8, 14, 8, 14));

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(createHeroPanel());
        center.add(Box.createVerticalStrut(6));
        center.add(createWelcomeBanner());
        center.add(Box.createVerticalStrut(8));
        center.add(createMenuPanel());
        center.add(Box.createVerticalStrut(8));
        center.add(createDailyReminder());
        center.add(Box.createVerticalGlue());

        home.add(center, BorderLayout.CENTER);
        return home;
    }
    
    private JPanel createQuickTips() {
        JPanel tips = new JPanel();
        tips.setOpaque(false);
        tips.setLayout(new BorderLayout());
        tips.setAlignmentX(Component.CENTER_ALIGNMENT);
        tips.setMaximumSize(new Dimension(350, 35));
        
        JLabel tipsLabel = new JLabel("<html><center>Tip: Complete daily challenges for bonus rewards!</center></html>");
        tipsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tipsLabel.setFont(new Font("SansSerif", Font.ITALIC, 9));
        tipsLabel.setForeground(MUTED);
        tipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        tips.add(tipsLabel, BorderLayout.CENTER);
        return tips;
    }

    private JPanel createHeroPanel() {
        JPanel hero = new JPanel();
        hero.setOpaque(false);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setAlignmentX(Component.CENTER_ALIGNMENT);
        hero.setMaximumSize(new Dimension(HealiverseTheme.CONTENT_WIDTH, 116));
        hero.setBorder(new EmptyBorder(2, 6, 0, 6));

        JLabel tag = new JLabel("SDG 3: Good Health and Well-Being");
        tag.setAlignmentX(Component.CENTER_ALIGNMENT);
        tag.setFont(new Font("SansSerif", Font.BOLD, 11));
        tag.setForeground(new Color(32, 132, 100));

        JLabel logo = new JLabel("HEALIVERSE", SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setFont(HealiverseTheme.titleFont(28));
        logo.setForeground(INK);
        logo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("<html><center>Mental health education made friendly and empowering</center></html>");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        subtitle.setForeground(MUTED);

        hero.add(tag);
        hero.add(Box.createVerticalStrut(4));
        hero.add(logo);
        hero.add(Box.createVerticalStrut(5));
        hero.add(subtitle);
        return hero;
    }

    private JPanel createWelcomeBanner() {
        JPanel banner = new JPanel(new BorderLayout(10, 0));
        banner.setAlignmentX(Component.CENTER_ALIGNMENT);
        banner.setMaximumSize(new Dimension(HealiverseTheme.CONTENT_WIDTH, 68));
        banner.setBackground(SURFACE);
        banner.setBorder(HealiverseTheme.thinPixelBorder(HealiverseTheme.LINE, 7));

        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome, Healer!");
        title.setFont(HealiverseTheme.buttonFont(14));
        title.setForeground(INK);

        JLabel message = new JLabel("<html>You're doing great today.</html>");
        message.setFont(HealiverseTheme.bodyFont(11));
        message.setForeground(MUTED);

        copy.add(title);
        copy.add(Box.createVerticalStrut(4));
        copy.add(message);

        JLabel mascot = new JLabel(new SimpleIcon("leaf", new Color(41, 110, 79)));

        banner.add(copy, BorderLayout.CENTER);
        banner.add(mascot, BorderLayout.EAST);
        return banner;
    }

    private JPanel createMenuPanel() {
        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new GridLayout(2, 2, 8, 8));
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.setMaximumSize(new Dimension(HealiverseTheme.CONTENT_WIDTH, 196));

        menu.add(featureButton("Learn", "Grow your mind", "Learn Button.png", SAGE, this::showLearning));
        menu.add(featureButton("Quiz", "Test and learn", "Quiz Button.png", BLUE, this::showQuiz));
        menu.add(featureButton("Rewards", "Earn and collect", "Rewards Button.png", ROSE, this::showRewards));
        menu.add(featureButton("Calm Time", "Relax and breathe", "Calm Time Button.png", LAVENDER, this::showRewards));
        return menu;
    }

    private JButton featureButton(String title, String subtitle, String iconName, Color color, Runnable action) {
        FeatureButton button = new FeatureButton(title, subtitle, color);
        button.setIcon(featureIcon(title));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        HealiverseTheme.setFixedSize(button, 166, 94);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.addActionListener(e -> action.run());
        return button;
    }

    private Icon featureIcon(String title) {
        if ("Learn".equals(title)) {
            return new SimpleIcon("book", INK);
        }
        if ("Quiz".equals(title)) {
            return new SimpleIcon("quiz", INK);
        }
        if ("Rewards".equals(title)) {
            return new SimpleIcon("badge", INK);
        }
        return new SimpleIcon("leaf", INK);
    }

    private JPanel createJourneyPanel() {
        JPanel journey = new JPanel(new BorderLayout(10, 0));
        journey.setAlignmentX(Component.CENTER_ALIGNMENT);
        journey.setMaximumSize(new Dimension(350, 70));
        journey.setBackground(new Color(230, 250, 242));
        journey.setBorder(new CompoundBorder(
                new LineBorder(new Color(100, 180, 160), 2),
                new EmptyBorder(10, 12, 10, 12)));

        JLabel icon = new JLabel(pixelIcon("Growth Tracker Badge.png", 36, 36, new SimpleIcon("leaf", PRIMARY)));
        icon.setPreferredSize(new Dimension(40, 40));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Good Health & Well-Being Progress");
        title.setFont(new Font("SansSerif", Font.BOLD, 11));
        title.setForeground(INK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(60);
        progress.setString("60% complete");
        progress.setStringPainted(true);
        progress.setFont(new Font("SansSerif", Font.BOLD, 8));
        progress.setForeground(SAGE);
        progress.setBackground(new Color(245, 240, 255));
        progress.setBorder(new LineBorder(new Color(150, 120, 200), 1));
        progress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));

        info.add(title);
        info.add(Box.createVerticalStrut(5));
        info.add(progress);

        journey.add(icon, BorderLayout.WEST);
        journey.add(info, BorderLayout.CENTER);
        return journey;
    }

    private JPanel createDailyReminder() {
        JPanel reminder = new JPanel(new BorderLayout(10, 0));
        reminder.setAlignmentX(Component.CENTER_ALIGNMENT);
        reminder.setMaximumSize(new Dimension(HealiverseTheme.CONTENT_WIDTH, 72));
        reminder.setBackground(new Color(255, 245, 230));
        reminder.setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 170, 120), 2),
                new EmptyBorder(8, 12, 8, 12)));

        JLabel icon = new JLabel(new SimpleIcon("leaf", PRIMARY_DARK));
        icon.setPreferredSize(new Dimension(50, 42));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Daily Reminder");
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        title.setForeground(PRIMARY_DARK);

        JLabel text = new JLabel("<html>Take a deep breath. You matter, and every small step counts.</html>");
        text.setFont(new Font("SansSerif", Font.PLAIN, 11));
        text.setForeground(new Color(89, 79, 120));

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(text);

        reminder.add(icon, BorderLayout.WEST);
        reminder.add(textPanel, BorderLayout.CENTER);
        return reminder;
    }

    private static JLabel imageLabel(String fileName, int maxWidth, int maxHeight) {
        JLabel label = new JLabel(loadPixelIcon(fileName, maxWidth, maxHeight));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private static Icon pixelIcon(String fileName, int maxWidth, int maxHeight, Icon fallback) {
        ImageIcon icon = loadPixelIcon(fileName, maxWidth, maxHeight);
        return icon == null ? fallback : icon;
    }

    private static ImageIcon loadPixelIcon(String fileName, int maxWidth, int maxHeight) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }
        return HealiversePaths.loadPixelIcon(fileName, maxWidth, maxHeight);
    }

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
        replaceScreen("REWARDS", wrapModule("Achievements", new GamificationModule(username)));
    }

    private void showProfile() {
        replaceScreen("PROFILE", createProfileScreen());
    }

    private JPanel createProfileScreen() {
        JPanel profile = new JPanel(new BorderLayout());
        profile.setBackground(PAPER);
        profile.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel logo = imageLabel("Healiverse Logo.png", 320, 136);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (logo.getIcon() == null) {
            logo.setText("Healiverse");
            logo.setFont(HealiverseTheme.titleFont(28));
            logo.setForeground(INK);
        }

        JLabel name = new JLabel("Profile: " + username, SwingConstants.CENTER);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setFont(HealiverseTheme.titleFont(20));
        name.setForeground(INK);

        JLabel sdg = new JLabel("<html><center>SDG 3: Good Health and Well-Being<br>Mental wellness learning app</center></html>");
        sdg.setAlignmentX(Component.CENTER_ALIGNMENT);
        sdg.setFont(HealiverseTheme.buttonFont(13));
        sdg.setForeground(MUTED);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SURFACE);
        card.setBorder(new CompoundBorder(new LineBorder(LINE, 2), new EmptyBorder(18, 18, 18, 18)));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(330, 210));

        JLabel note = new JLabel("<html><center>Your learning progress, quiz scores, and rewards are saved for this account.</center></html>");
        note.setFont(HealiverseTheme.bodyFont(13));
        note.setForeground(INK);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton home = actionButton("Back to Home", HealiverseTheme.BABY_BLUE);
        home.addActionListener(e -> cards.show(screens, "HOME"));
        JButton logout = actionButton("Log Out", HealiverseTheme.PASTEL_PINK);
        logout.addActionListener(e -> {
            dispose();
            new LoginApplication().setVisible(true);
        });
        JButton exit = actionButton("Exit App", HealiverseTheme.SOFT_YELLOW);
        exit.addActionListener(e -> System.exit(0));

        card.add(note);
        card.add(Box.createVerticalStrut(16));
        card.add(home);
        card.add(Box.createVerticalStrut(8));
        card.add(logout);
        card.add(Box.createVerticalStrut(8));
        card.add(exit);

        content.add(Box.createVerticalStrut(20));
        content.add(logo);
        content.add(Box.createVerticalStrut(10));
        content.add(name);
        content.add(Box.createVerticalStrut(8));
        content.add(sdg);
        content.add(Box.createVerticalStrut(20));
        content.add(card);
        content.add(Box.createVerticalGlue());

        profile.add(content, BorderLayout.CENTER);
        return profile;
    }

    private JButton actionButton(String text, Color color) {
        JButton button = new JButton(text);
        HealiverseTheme.stylePixelButton(button, color);
        button.setFont(HealiverseTheme.buttonFont(12));
        button.setForeground(INK);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        HealiverseTheme.setFixedSize(button, 252, HealiverseTheme.COMPACT_BUTTON_HEIGHT);
        return button;
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

    private JPanel wrapModule(String title, JComponent content) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(PAPER);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(239, 225, 252));
        top.setBorder(new CompoundBorder(
                new LineBorder(LINE, 1),
                new EmptyBorder(5, 8, 5, 8)));

        JButton back = new JButton("<");
        back.setFont(new Font("SansSerif", Font.BOLD, 16));
        back.setForeground(INK);
        back.setBackground(SURFACE);
        back.setFocusPainted(false);
        back.setBorder(new CompoundBorder(new LineBorder(LINE, 1), new EmptyBorder(2, 8, 2, 8)));
        HealiverseTheme.setFixedSize(back, 34, 28);
        back.addActionListener(e -> cards.show(screens, "HOME"));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(PRIMARY_DARK);

        top.add(back, BorderLayout.WEST);
        top.add(label, BorderLayout.CENTER);

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

    private static String cleanUsername(String value) {
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
            if (args.length > 0) {
                new MentalHealthGameApp(cleanUsername(args[0])).setVisible(true);
            } else {
                new LoginApplication().setVisible(true);
            }
        });
    }

    private static class FeatureButton extends JButton {
        private final Color color;
        private final String title;
        private final String subtitle;

        FeatureButton(String title, String subtitle, Color color) {
            super("");
            this.color = color;
            this.title = title;
            this.subtitle = subtitle;
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setForeground(INK);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setIconTextGap(0);
            setMargin(new Insets(0, 0, 0, 0));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int offset = getModel().isArmed() ? 1 : 0;
            g.setColor(color);
            g.fillRoundRect(0, offset, getWidth() - 1, getHeight() - 2, 6, 6);
            g.setColor(INK);
            g.drawRoundRect(0, offset, getWidth() - 2, getHeight() - 3, 6, 6);

            Icon icon = getIcon();
            if (icon != null) {
                int iconX = (getWidth() - icon.getIconWidth()) / 2;
                icon.paintIcon(this, g, iconX, 10 + offset);
            }

            g.setColor(INK);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            drawCentered(g, title, 62 + offset);
            g.setColor(new Color(89, 79, 120));
            g.setFont(new Font("SansSerif", Font.PLAIN, 10));
            drawCentered(g, subtitle, 80 + offset);
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
            setFont(new Font("SansSerif", Font.BOLD, 9));
            setForeground(new Color(89, 68, 158));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(4, 1, 2, 1));
            setIconTextGap(3);
            setMargin(new Insets(0, 0, 0, 0));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {
                g.setColor(new Color(255, 252, 247, 190));
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
            GradientPaint gradient = new GradientPaint(0, 0, new Color(248, 252, 249),
                    0, getHeight(), new Color(232, 244, 238));
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
            } else if ("leaf".equals(type)) {
                g.drawOval(x + 3, y + 3, 12, 9);
                g.drawLine(x + 6, y + 13, x + 14, y + 5);
            } else {
                g.drawLine(x + 5, y + 5, x + 13, y + 13);
                g.drawLine(x + 13, y + 5, x + 5, y + 13);
            }
            g.dispose();
        }
    }
}
