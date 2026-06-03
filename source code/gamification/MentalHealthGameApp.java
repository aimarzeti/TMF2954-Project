import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Class: MentalHealthGameApp
 * Creator: Zeti Nur Aimar binti Ali
 * Tester: G04/SE Group 14
 * Description: Phone-sized desktop launcher that connects learning, quiz,
 * and gamification screens with a cozy pixel-game experience.
 */
public class MentalHealthGameApp extends JFrame {

    private static final int PHONE_WIDTH = 390;
    private static final int PHONE_HEIGHT = 720;

    private static final Color INK = new Color(60, 42, 88);
    private static final Color PAPER = new Color(255, 248, 239);
    private static final Color SKY = new Color(185, 224, 252);
    private static final Color PINK = new Color(246, 135, 178);
    private static final Color SOFT_PINK = new Color(255, 211, 225);
    private static final Color MINT = new Color(181, 226, 174);
    private static final Color BUTTER = new Color(255, 219, 129);
    private static final Color LAVENDER = new Color(205, 187, 239);
    private static final Color BLUE = new Color(156, 211, 245);
    private static final String PIXEL_DIR = "images/pixel/";

    private final String username;
    private final CardLayout cards;
    private final JPanel screens;
    private final JPanel bottomNav;

    public MentalHealthGameApp(String username) {
        this.username = cleanUsername(username);
        this.cards = new CardLayout();
        this.screens = new JPanel(cards);
        this.bottomNav = new JPanel(new GridLayout(1, 5, 4, 0));

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
                new LineBorder(new Color(145, 103, 188), 4),
                new EmptyBorder(6, 6, 6, 6)));

        screens.setBackground(PAPER);
        screens.add(createHomeScreen(), "HOME");

        bottomNav.setBackground(new Color(232, 214, 248));
        bottomNav.setBorder(new CompoundBorder(
                new LineBorder(new Color(145, 103, 188), 2),
                new EmptyBorder(5, 5, 5, 5)));
        bottomNav.add(navButton("⌂", "HOME", () -> cards.show(screens, "HOME")));
        bottomNav.add(navButton("▤", "LEARN", this::showLearning));
        bottomNav.add(navButton("?", "QUIZ", this::showQuiz));
        bottomNav.add(navButton("★", "BADGES", this::showRewards));
        bottomNav.add(navButton("♛", "RANK", this::showRewards));

        phone.add(screens, BorderLayout.CENTER);
        phone.add(bottomNav, BorderLayout.SOUTH);
        return phone;
    }

    private JPanel createHomeScreen() {
        SkyScenePanel home = new SkyScenePanel();
        home.setLayout(new BorderLayout(0, 8));
        home.setBorder(new EmptyBorder(8, 8, 8, 8));

        home.add(createStatusBar(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JComponent logoHeader = createLogoHeader();

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.setMaximumSize(new Dimension(190, 180));
        menu.add(menuButton("START JOURNEY", PINK, this::showLearning));
        menu.add(Box.createVerticalStrut(6));
        menu.add(menuButton("LEARN", BUTTER, this::showLearning));
        menu.add(Box.createVerticalStrut(6));
        menu.add(menuButton("QUIZ BATTLE", MINT, this::showQuiz));
        menu.add(Box.createVerticalStrut(6));
        menu.add(menuButton("ACHIEVEMENTS", LAVENDER, this::showRewards));

        center.add(logoHeader);
        center.add(Box.createVerticalStrut(12));
        center.add(menu);
        center.add(Box.createVerticalStrut(10));
        center.add(createMascotReminder());

        home.add(center, BorderLayout.CENTER);
        return home;
    }

    private JComponent createLogoHeader() {
        JLabel logo = imageLabel("healiverse_logo.png", 305, 150);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (logo.getIcon() != null) {
            return logo;
        }

        JPanel fallback = new JPanel();
        fallback.setOpaque(false);
        fallback.setLayout(new BoxLayout(fallback, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("HEALIVERSE", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Monospaced", Font.BOLD, 33));
        title.setForeground(new Color(133, 99, 204));

        JLabel subtitle = new JLabel("Your Mental Wellness Journey", SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Monospaced", Font.BOLD, 11));
        subtitle.setForeground(new Color(100, 82, 166));

        fallback.add(title);
        fallback.add(subtitle);
        return fallback;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new GridLayout(1, 4, 5, 0));
        bar.setOpaque(false);
        bar.add(statusPill("♥", "5", SOFT_PINK));
        bar.add(statusPill("★", "120", BUTTER));
        bar.add(statusPill("●", "50", new Color(255, 202, 91)));
        bar.add(statusPill("⚙", "", LAVENDER));
        return bar;
    }

    private JLabel statusPill(String icon, String value, Color color) {
        JLabel label = new JLabel(icon + (value.isEmpty() ? "" : "  " + value), SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(INK);
        label.setFont(new Font("Monospaced", Font.BOLD, 13));
        label.setBorder(new CompoundBorder(new LineBorder(PINK, 2), new EmptyBorder(3, 4, 3, 4)));
        return label;
    }

    private JButton menuButton(String text, Color color, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 30));
        button.setPreferredSize(new Dimension(180, 30));
        button.setFont(new Font("Monospaced", Font.BOLD, 12));
        button.setForeground(INK);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(new LineBorder(INK, 2), new EmptyBorder(5, 8, 5, 8)));
        button.addActionListener(e -> action.run());
        return button;
    }

    private JPanel createDailyReminder() {
        JPanel sign = new JPanel();
        sign.setLayout(new BoxLayout(sign, BoxLayout.Y_AXIS));
        sign.setAlignmentX(Component.CENTER_ALIGNMENT);
        sign.setMaximumSize(new Dimension(210, 94));
        sign.setBackground(new Color(255, 239, 218));
        sign.setBorder(new CompoundBorder(
                new LineBorder(new Color(121, 79, 89), 3),
                new EmptyBorder(8, 12, 8, 12)));

        JLabel title = new JLabel("DAILY REMINDER", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Monospaced", Font.BOLD, 11));
        title.setForeground(new Color(154, 87, 112));

        JLabel text = new JLabel("<html><center>Small steps every day<br>lead to big changes!</center></html>");
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        text.setFont(new Font("SansSerif", Font.BOLD, 12));
        text.setForeground(INK);

        sign.add(title);
        sign.add(Box.createVerticalStrut(5));
        sign.add(text);
        return sign;
    }

    private JPanel createMascotReminder() {
        JPanel row = new JPanel(new BorderLayout(4, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(330, 118));

        JLabel girl = imageLabel("girl_wave.png", 80, 112);
        if (girl.getIcon() == null) {
            girl.setPreferredSize(new Dimension(74, 96));
        }

        JLabel bunny = imageLabel("bunny.png", 58, 80);
        if (bunny.getIcon() == null) {
            bunny.setPreferredSize(new Dimension(54, 80));
        }

        row.add(girl, BorderLayout.WEST);
        row.add(createDailyReminder(), BorderLayout.CENTER);
        row.add(bunny, BorderLayout.EAST);
        return row;
    }

    private JButton navButton(String icon, String label, Runnable action) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + label + "</center></html>");
        button.setFont(new Font("Monospaced", Font.BOLD, 10));
        button.setForeground(INK);
        button.setBackground(new Color(250, 241, 255));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(177, 145, 214), 1));
        button.addActionListener(e -> action.run());
        return button;
    }

    private JLabel imageLabel(String fileName, int maxWidth, int maxHeight) {
        JLabel label = new JLabel(loadPixelIcon(fileName, maxWidth, maxHeight));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private ImageIcon loadPixelIcon(String fileName, int maxWidth, int maxHeight) {
        ImageIcon icon = new ImageIcon(PIXEL_DIR + fileName);
        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return null;
        }

        double ratio = Math.min(maxWidth / (double) icon.getIconWidth(),
                maxHeight / (double) icon.getIconHeight());
        int width = Math.max(1, (int) Math.round(icon.getIconWidth() * ratio));
        int height = Math.max(1, (int) Math.round(icon.getIconHeight() * ratio));
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void showLearning() {
        replaceScreen("LEARNING", wrapModule("LEARNING", createLinkedLearningModule()));
    }

    private void showQuiz() {
        replaceScreen("QUIZ", wrapModule("QUIZ BATTLE", createLinkedQuizModule()));
    }

    private void showRewards() {
        replaceScreen("REWARDS", wrapModule("ACHIEVEMENTS", new GamificationModule(username)));
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
        top.setBackground(new Color(235, 213, 250));
        top.setBorder(new CompoundBorder(
                new LineBorder(new Color(145, 103, 188), 2),
                new EmptyBorder(6, 8, 6, 8)));

        JButton back = new JButton("‹");
        back.setFont(new Font("Monospaced", Font.BOLD, 18));
        back.setForeground(INK);
        back.setBackground(new Color(255, 248, 255));
        back.setFocusPainted(false);
        back.setBorder(new LineBorder(new Color(145, 103, 188), 1));
        back.addActionListener(e -> cards.show(screens, "HOME"));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", Font.BOLD, 14));
        label.setForeground(new Color(187, 67, 139));

        top.add(back, BorderLayout.WEST);
        top.add(label, BorderLayout.CENTER);

        content.setPreferredSize(new Dimension(PHONE_WIDTH - 34, 610));
        JScrollPane viewport = new JScrollPane(content);
        viewport.setBorder(null);
        viewport.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        viewport.getVerticalScrollBar().setUnitIncrement(14);

        wrapper.add(top, BorderLayout.NORTH);
        wrapper.add(viewport, BorderLayout.CENTER);
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
            String username = args.length > 0
                    ? args[0]
                    : JOptionPane.showInputDialog(null, "Enter username:", "guest");
            new MentalHealthGameApp(cleanUsername(username)).setVisible(true);
        });
    }

    private static class SkyScenePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            g.setColor(SKY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(218, 238, 255));
            g.fillRect(0, 0, getWidth(), 72);

            drawCloud(g, 24, 48);
            drawCloud(g, 290, 78);
            drawCloud(g, 70, 122);
            drawCloud(g, 318, 156);
            drawRainbow(g, 0, 238);
            drawHills(g);

            g.dispose();
        }

        private void drawCloud(Graphics2D g, int x, int y) {
            g.setColor(Color.WHITE);
            g.fillRect(x, y + 12, 54, 18);
            g.fillRect(x + 12, y, 22, 30);
            g.fillRect(x + 33, y + 6, 28, 24);
            g.setColor(new Color(224, 219, 251));
            g.fillRect(x + 8, y + 30, 42, 4);
        }

        private void drawRainbow(Graphics2D g, int x, int y) {
            Color[] colors = {PINK, BUTTER, MINT, BLUE, LAVENDER};
            for (int i = 0; i < colors.length; i++) {
                g.setColor(colors[i]);
                g.drawArc(x - i * 5, y + i * 5, 112 + i * 10, 96 + i * 10, 0, 180);
                g.drawArc(x - i * 5, y + i * 5 + 1, 112 + i * 10, 96 + i * 10, 0, 180);
            }
        }

        private void drawHills(Graphics2D g) {
            int ground = getHeight() - 58;
            g.setColor(new Color(106, 186, 109));
            g.fillRect(0, ground, getWidth(), 58);
            g.setColor(new Color(72, 144, 82));
            g.fillRect(0, ground, getWidth(), 8);
            g.setColor(new Color(110, 78, 54));
            g.fillRect(0, ground + 36, getWidth(), 22);
        }

        private void drawCharacter(Graphics2D g, int x, int y) {
            g.setColor(new Color(86, 56, 72));
            g.fillRect(x + 10, y, 42, 42);
            g.setColor(new Color(255, 213, 187));
            g.fillRect(x + 16, y + 12, 30, 30);
            g.setColor(new Color(255, 132, 176));
            g.fillRect(x + 12, y + 44, 38, 34);
            g.setColor(new Color(82, 58, 79));
            g.fillRect(x + 21, y + 24, 5, 5);
            g.fillRect(x + 36, y + 24, 5, 5);
            g.setColor(new Color(255, 188, 208));
            g.fillRect(x + 7, y + 8, 15, 11);
            g.fillRect(x + 40, y + 8, 15, 11);
            g.setColor(INK);
            g.fillRect(x + 18, y + 78, 10, 14);
            g.fillRect(x + 34, y + 78, 10, 14);
        }

        private void drawBunny(Graphics2D g, int x, int y) {
            g.setColor(Color.WHITE);
            g.fillRect(x + 12, y, 10, 30);
            g.fillRect(x + 30, y, 10, 30);
            g.fillRect(x + 8, y + 22, 38, 38);
            g.setColor(SOFT_PINK);
            g.fillRect(x + 15, y + 4, 4, 20);
            g.fillRect(x + 33, y + 4, 4, 20);
            g.setColor(INK);
            g.fillRect(x + 19, y + 38, 4, 4);
            g.fillRect(x + 33, y + 38, 4, 4);
            g.setColor(PINK);
            g.fillRect(x + 26, y + 46, 6, 4);
        }
    }
}
