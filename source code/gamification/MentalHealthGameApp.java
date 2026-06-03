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

    private static final Color INK = new Color(58, 39, 86);
    private static final Color PAPER = new Color(255, 249, 242);
    private static final Color SKY = new Color(181, 224, 250);
    private static final Color SKY_LIGHT = new Color(218, 239, 255);
    private static final Color PINK = new Color(246, 133, 178);
    private static final Color PINK_DARK = new Color(202, 78, 132);
    private static final Color SOFT_PINK = new Color(255, 213, 229);
    private static final Color MINT = new Color(174, 220, 166);
    private static final Color BUTTER = new Color(255, 219, 126);
    private static final Color LAVENDER = new Color(203, 184, 238);
    private static final Color BLUE = new Color(151, 210, 244);
    private static final Color PURPLE = new Color(143, 104, 204);
    private static final Color SHADOW = new Color(110, 74, 143);

    private final String username;
    private final CardLayout cards;
    private final JPanel screens;
    private final JPanel bottomNav;

    public MentalHealthGameApp(String username) {
        this.username = cleanUsername(username);
        this.cards = new CardLayout();
        this.screens = new JPanel(cards);
        this.bottomNav = new JPanel(new GridLayout(1, 6, 4, 0));

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
                new LineBorder(PURPLE, 4),
                new EmptyBorder(6, 6, 6, 6)));

        screens.setBackground(PAPER);
        screens.add(createHomeScreen(), "HOME");

        bottomNav.setBackground(new Color(232, 215, 248));
        bottomNav.setBorder(new CompoundBorder(
                new LineBorder(PURPLE, 2),
                new EmptyBorder(4, 4, 4, 4)));
        bottomNav.add(navButton("HOME", new MiniIcon("home", PINK), () -> cards.show(screens, "HOME")));
        bottomNav.add(navButton("LEARN", new MiniIcon("book", LAVENDER), this::showLearning));
        bottomNav.add(navButton("QUIZ", new MiniIcon("quiz", MINT), this::showQuiz));
        bottomNav.add(navButton("BADGE", new MiniIcon("trophy", BUTTER), this::showRewards));
        bottomNav.add(navButton("RANK", new MiniIcon("rank", MINT), this::showRewards));
        bottomNav.add(navButton("EXIT", new MiniIcon("gear", SOFT_PINK), () -> dispose()));

        phone.add(screens, BorderLayout.CENTER);
        phone.add(bottomNav, BorderLayout.SOUTH);
        return phone;
    }

    private JPanel createHomeScreen() {
        SkyScenePanel home = new SkyScenePanel();
        home.setLayout(new BorderLayout());
        home.setBorder(new EmptyBorder(8, 10, 8, 10));

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        LogoPanel logo = new LogoPanel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.setMaximumSize(new Dimension(230, 236));
        menu.add(menuButton("START JOURNEY", PINK, new Color(214, 91, 145), this::showLearning));
        menu.add(Box.createVerticalStrut(9));
        menu.add(menuButton("LEARN", BUTTER, new Color(203, 139, 53), this::showLearning));
        menu.add(Box.createVerticalStrut(9));
        menu.add(menuButton("QUIZ BATTLE", MINT, new Color(91, 151, 93), this::showQuiz));
        menu.add(Box.createVerticalStrut(9));
        menu.add(menuButton("ACHIEVEMENTS", LAVENDER, new Color(128, 93, 178), this::showRewards));
        menu.add(Box.createVerticalStrut(9));
        menu.add(menuButton("EXIT", BLUE, new Color(87, 143, 190), () -> dispose()));

        center.add(createStatusBar());
        center.add(Box.createVerticalStrut(12));
        center.add(logo);
        center.add(Box.createVerticalStrut(20));
        center.add(menu);
        center.add(Box.createVerticalStrut(20));
        center.add(createDailyReminder());
        center.add(Box.createVerticalGlue());

        home.add(center, BorderLayout.CENTER);
        return home;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        bar.setOpaque(false);
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);
        bar.setMaximumSize(new Dimension(340, 34));
        bar.add(new StatChip("HP", "5", SOFT_PINK, new MiniIcon("heart", PINK)));
        bar.add(new StatChip("STAR", "120", BUTTER, new MiniIcon("star", BUTTER)));
        bar.add(new StatChip("COIN", "50", new Color(255, 200, 88), new MiniIcon("coin", BUTTER)));
        bar.add(new StatChip("GEM", "8", LAVENDER, new MiniIcon("gem", MINT)));
        return bar;
    }

    private JButton menuButton(String text, Color color, Color shadow, Runnable action) {
        PixelButton button = new PixelButton(text, color, shadow);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(224, 38));
        button.setPreferredSize(new Dimension(224, 38));
        button.addActionListener(e -> action.run());
        return button;
    }

    private JPanel createDailyReminder() {
        JPanel sign = new JPanel();
        sign.setLayout(new BoxLayout(sign, BoxLayout.Y_AXIS));
        sign.setAlignmentX(Component.CENTER_ALIGNMENT);
        sign.setMaximumSize(new Dimension(268, 88));
        sign.setBackground(new Color(255, 239, 219));
        sign.setBorder(new CompoundBorder(
                new LineBorder(new Color(120, 75, 91), 3),
                new EmptyBorder(8, 12, 8, 12)));

        JLabel title = new JLabel("DAILY REMINDER", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Monospaced", Font.BOLD, 12));
        title.setForeground(PINK_DARK);

        JLabel text = new JLabel("<html><center>Small steps every day<br>lead to big changes!</center></html>");
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        text.setFont(new Font("SansSerif", Font.BOLD, 12));
        text.setForeground(INK);

        sign.add(title);
        sign.add(Box.createVerticalStrut(6));
        sign.add(text);
        return sign;
    }

    private JButton navButton(String label, Icon icon, Runnable action) {
        JButton button = new JButton(label, icon);
        button.setFont(new Font("Monospaced", Font.BOLD, 9));
        button.setForeground(INK);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(new CompoundBorder(
                new LineBorder(new Color(174, 139, 211), 1),
                new EmptyBorder(2, 1, 1, 1)));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.addActionListener(e -> action.run());
        return button;
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
                new LineBorder(PURPLE, 2),
                new EmptyBorder(6, 8, 6, 8)));

        JButton back = new JButton("<");
        back.setFont(new Font("Monospaced", Font.BOLD, 18));
        back.setForeground(INK);
        back.setBackground(new Color(255, 248, 255));
        back.setFocusPainted(false);
        back.setBorder(new LineBorder(PURPLE, 1));
        back.addActionListener(e -> cards.show(screens, "HOME"));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", Font.BOLD, 14));
        label.setForeground(PINK_DARK);

        top.add(back, BorderLayout.WEST);
        top.add(label, BorderLayout.CENTER);

        content.setPreferredSize(new Dimension(PHONE_WIDTH - 58, 610));
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

    private static class PixelButton extends JButton {
        private final Color color;
        private final Color shadow;

        PixelButton(String text, Color color, Color shadow) {
            super(text);
            this.color = color;
            this.shadow = shadow;
            setFont(new Font("Monospaced", Font.BOLD, 12));
            setForeground(INK);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(6, 8, 8, 8));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            int press = getModel().isArmed() ? 2 : 0;
            int width = getWidth();
            int height = getHeight();

            g.setColor(shadow);
            g.fillRect(5, 7 + press, width - 8, height - 9);
            g.setColor(color);
            g.fillRect(2, 2 + press, width - 8, height - 9);
            g.setColor(Color.WHITE);
            g.drawLine(4, 4 + press, width - 10, 4 + press);
            g.setColor(INK);
            g.drawRect(2, 2 + press, width - 9, height - 10);
            g.drawRect(3, 3 + press, width - 11, height - 12);
            g.dispose();

            super.paintComponent(graphics);
        }
    }

    private static class StatChip extends JComponent {
        private final String label;
        private final String value;
        private final Color color;
        private final Icon icon;

        StatChip(String label, String value, Color color, Icon icon) {
            this.label = label;
            this.value = value;
            this.color = color;
            this.icon = icon;
            setPreferredSize(new Dimension(78, 30));
            setMaximumSize(new Dimension(78, 30));
            setFont(new Font("Monospaced", Font.BOLD, 10));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g.setColor(new Color(170, 104, 173));
            g.fillRect(3, 4, getWidth() - 4, getHeight() - 5);
            g.setColor(color);
            g.fillRect(0, 0, getWidth() - 4, getHeight() - 5);
            g.setColor(PINK);
            g.drawRect(0, 0, getWidth() - 5, getHeight() - 6);
            g.drawRect(1, 1, getWidth() - 7, getHeight() - 8);

            icon.paintIcon(this, g, 6, 6);
            g.setFont(getFont());
            g.setColor(INK);
            g.drawString(label, 27, 12);
            g.setFont(new Font("Monospaced", Font.BOLD, 12));
            g.drawString(value, 27, 24);
            g.dispose();
        }
    }

    private static class LogoPanel extends JPanel {
        LogoPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(330, 112));
            setMaximumSize(new Dimension(330, 112));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            int centerX = getWidth() / 2;
            drawCloud(g, centerX - 138, 10, 276, 56);
            new MiniIcon("heart", PINK).paintIcon(this, g, centerX - 10, 0);
            new MiniIcon("star", BUTTER).paintIcon(this, g, centerX - 108, 20);
            new MiniIcon("star", BUTTER).paintIcon(this, g, centerX + 92, 20);

            g.setFont(new Font("Monospaced", Font.BOLD, 34));
            FontMetrics titleMetrics = g.getFontMetrics();
            String title = "HEALIVERSE";
            int titleX = centerX - titleMetrics.stringWidth(title) / 2;
            int titleY = 64;

            g.setColor(new Color(105, 71, 164));
            g.drawString(title, titleX + 3, titleY + 3);
            g.setColor(new Color(239, 228, 255));
            g.drawString(title, titleX + 1, titleY);
            g.drawString(title, titleX - 1, titleY);
            g.drawString(title, titleX, titleY + 1);
            g.drawString(title, titleX, titleY - 1);
            g.setColor(PURPLE);
            g.drawString(title, titleX, titleY);

            int ribbonX = centerX - 112;
            int ribbonY = 75;
            g.setColor(new Color(188, 71, 128));
            g.fillRect(ribbonX + 6, ribbonY + 5, 224, 24);
            g.setColor(PINK);
            g.fillRect(ribbonX, ribbonY, 224, 24);
            g.setColor(PAPER);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            String subtitle = "Your Mental Wellness Journey";
            FontMetrics subtitleMetrics = g.getFontMetrics();
            g.drawString(subtitle, centerX - subtitleMetrics.stringWidth(subtitle) / 2, ribbonY + 16);

            g.dispose();
        }

        private void drawCloud(Graphics2D g, int x, int y, int width, int height) {
            g.setColor(new Color(165, 148, 219));
            g.fillRect(x + 18, y + 14, width - 36, height - 6);
            g.setColor(new Color(244, 240, 255));
            g.fillRect(x + 20, y + 10, width - 40, height - 10);
            g.fillRect(x + 44, y, 54, 24);
            g.fillRect(x + 104, y - 4, 76, 30);
            g.fillRect(x + 188, y + 2, 48, 24);
            g.setColor(new Color(218, 204, 244));
            g.drawRect(x + 20, y + 10, width - 41, height - 11);
        }
    }

    private static class SkyScenePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            g.setColor(SKY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(SKY_LIGHT);
            g.fillRect(0, 0, getWidth(), 88);

            drawCloud(g, 26, 46, 78, 34);
            drawCloud(g, 278, 70, 82, 36);
            drawCloud(g, 32, 172, 62, 28);
            drawCloud(g, 300, 210, 58, 26);
            drawRainbow(g, -34, 315);
            drawGround(g);
            drawFlowers(g);

            g.dispose();
        }

        private void drawCloud(Graphics2D g, int x, int y, int width, int height) {
            g.setColor(new Color(153, 184, 229));
            g.fillRect(x + 6, y + 8, width - 8, height - 4);
            g.setColor(new Color(250, 248, 255));
            g.fillRect(x + 8, y + 6, width - 12, height - 8);
            g.fillRect(x + width / 4, y, width / 3, height / 2);
            g.fillRect(x + width / 2, y + 4, width / 3, height / 2);
        }

        private void drawRainbow(Graphics2D g, int x, int y) {
            Color[] colors = {PINK, BUTTER, MINT, BLUE, LAVENDER};
            for (int i = 0; i < colors.length; i++) {
                g.setColor(colors[i]);
                g.drawArc(x - i * 5, y + i * 5, 140 + i * 10, 112 + i * 10, 0, 180);
                g.drawArc(x - i * 5, y + i * 5 + 1, 140 + i * 10, 112 + i * 10, 0, 180);
            }
        }

        private void drawGround(Graphics2D g) {
            int ground = getHeight() - 78;
            g.setColor(new Color(102, 185, 107));
            g.fillRect(0, ground, getWidth(), 78);
            g.setColor(new Color(74, 145, 81));
            g.fillRect(0, ground, getWidth(), 8);
            g.setColor(new Color(107, 78, 56));
            g.fillRect(0, ground + 45, getWidth(), 33);

            g.setColor(new Color(120, 76, 50));
            g.fillRect(34, ground - 72, 16, 76);
            g.setColor(new Color(240, 132, 180));
            g.fillRect(12, ground - 104, 62, 24);
            g.fillRect(0, ground - 86, 82, 24);
            g.fillRect(18, ground - 118, 42, 22);
            g.setColor(new Color(255, 180, 210));
            g.fillRect(24, ground - 112, 18, 10);

            g.setColor(new Color(95, 72, 58));
            g.fillRect(82, ground - 27, 54, 8);
            g.fillRect(88, ground - 18, 6, 18);
            g.fillRect(124, ground - 18, 6, 18);
            g.setColor(new Color(198, 139, 96));
            g.fillRect(80, ground - 32, 58, 6);
        }

        private void drawFlowers(Graphics2D g) {
            int ground = getHeight() - 78;
            g.setColor(new Color(83, 154, 84));
            g.fillRect(getWidth() - 86, ground - 38, 70, 38);
            g.fillRect(getWidth() - 72, ground - 56, 42, 32);
            g.setColor(SOFT_PINK);
            g.fillRect(getWidth() - 65, ground - 44, 8, 8);
            g.fillRect(getWidth() - 38, ground - 28, 8, 8);
            g.setColor(new Color(255, 245, 157));
            g.fillRect(getWidth() - 60, ground - 39, 4, 4);
            g.fillRect(getWidth() - 33, ground - 23, 4, 4);
        }
    }

    private static class MiniIcon implements Icon {
        private final String type;
        private final Color color;

        MiniIcon(String type, Color color) {
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
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            if ("heart".equals(type)) {
                drawHeart(g, x, y);
            } else if ("star".equals(type)) {
                drawStar(g, x, y);
            } else if ("coin".equals(type)) {
                drawCoin(g, x, y);
            } else if ("gem".equals(type)) {
                drawGem(g, x, y);
            } else if ("home".equals(type)) {
                drawHome(g, x, y);
            } else if ("book".equals(type)) {
                drawBook(g, x, y);
            } else if ("quiz".equals(type)) {
                drawQuiz(g, x, y);
            } else if ("trophy".equals(type)) {
                drawTrophy(g, x, y);
            } else if ("rank".equals(type)) {
                drawRank(g, x, y);
            } else {
                drawGear(g, x, y);
            }
            g.dispose();
        }

        private void drawHeart(Graphics2D g, int x, int y) {
            g.setColor(PINK_DARK);
            g.fillRect(x + 3, y + 3, 5, 5);
            g.fillRect(x + 10, y + 3, 5, 5);
            g.fillRect(x + 2, y + 7, 14, 5);
            g.fillRect(x + 5, y + 12, 8, 3);
            g.setColor(color);
            g.fillRect(x + 4, y + 4, 4, 4);
            g.fillRect(x + 10, y + 4, 4, 4);
            g.fillRect(x + 4, y + 8, 10, 3);
            g.fillRect(x + 7, y + 11, 4, 3);
        }

        private void drawStar(Graphics2D g, int x, int y) {
            Polygon star = new Polygon();
            star.addPoint(x + 9, y + 1);
            star.addPoint(x + 12, y + 6);
            star.addPoint(x + 17, y + 6);
            star.addPoint(x + 13, y + 10);
            star.addPoint(x + 15, y + 16);
            star.addPoint(x + 9, y + 13);
            star.addPoint(x + 3, y + 16);
            star.addPoint(x + 5, y + 10);
            star.addPoint(x + 1, y + 6);
            star.addPoint(x + 6, y + 6);
            g.setColor(new Color(170, 102, 46));
            g.fillPolygon(star);
            g.setColor(color);
            g.fillRect(x + 7, y + 5, 5, 7);
            g.fillRect(x + 4, y + 8, 11, 3);
        }

        private void drawCoin(Graphics2D g, int x, int y) {
            g.setColor(new Color(181, 112, 34));
            g.fillOval(x + 1, y + 1, 16, 16);
            g.setColor(color);
            g.fillOval(x + 3, y + 3, 12, 12);
            g.setColor(new Color(255, 241, 150));
            g.fillRect(x + 8, y + 5, 2, 8);
        }

        private void drawGem(Graphics2D g, int x, int y) {
            Polygon gem = new Polygon();
            gem.addPoint(x + 9, y + 1);
            gem.addPoint(x + 16, y + 6);
            gem.addPoint(x + 12, y + 17);
            gem.addPoint(x + 6, y + 17);
            gem.addPoint(x + 2, y + 6);
            g.setColor(new Color(70, 122, 89));
            g.fillPolygon(gem);
            g.setColor(color);
            g.fillRect(x + 5, y + 5, 9, 6);
            g.fillRect(x + 7, y + 11, 5, 4);
        }

        private void drawHome(Graphics2D g, int x, int y) {
            g.setColor(INK);
            Polygon roof = new Polygon();
            roof.addPoint(x + 1, y + 9);
            roof.addPoint(x + 9, y + 2);
            roof.addPoint(x + 17, y + 9);
            g.fillPolygon(roof);
            g.setColor(SOFT_PINK);
            g.fillPolygon(roof);
            g.setColor(PAPER);
            g.fillRect(x + 4, y + 9, 10, 7);
            g.setColor(PINK);
            g.fillRect(x + 8, y + 11, 3, 5);
            g.setColor(INK);
            g.drawRect(x + 4, y + 9, 10, 7);
        }

        private void drawBook(Graphics2D g, int x, int y) {
            g.setColor(INK);
            g.fillRect(x + 2, y + 4, 14, 11);
            g.setColor(PAPER);
            g.fillRect(x + 3, y + 5, 6, 9);
            g.fillRect(x + 10, y + 5, 5, 9);
            g.setColor(color);
            g.drawLine(x + 9, y + 5, x + 9, y + 14);
        }

        private void drawQuiz(Graphics2D g, int x, int y) {
            g.setColor(INK);
            g.fillRect(x + 3, y + 3, 12, 12);
            g.setColor(color);
            g.fillRect(x + 4, y + 4, 10, 10);
            g.setColor(PAPER);
            g.setFont(new Font("Monospaced", Font.BOLD, 9));
            g.drawString("Q", x + 6, y + 13);
        }

        private void drawTrophy(Graphics2D g, int x, int y) {
            g.setColor(new Color(146, 89, 38));
            g.fillRect(x + 4, y + 4, 10, 8);
            g.fillRect(x + 7, y + 12, 4, 3);
            g.fillRect(x + 5, y + 15, 8, 2);
            g.setColor(color);
            g.fillRect(x + 5, y + 3, 8, 8);
            g.fillRect(x + 2, y + 5, 3, 4);
            g.fillRect(x + 13, y + 5, 3, 4);
        }

        private void drawRank(Graphics2D g, int x, int y) {
            g.setColor(INK);
            g.fillRect(x + 2, y + 10, 3, 6);
            g.fillRect(x + 7, y + 6, 3, 10);
            g.fillRect(x + 12, y + 3, 3, 13);
            g.setColor(color);
            g.fillRect(x + 3, y + 11, 1, 4);
            g.fillRect(x + 8, y + 7, 1, 8);
            g.fillRect(x + 13, y + 4, 1, 11);
        }

        private void drawGear(Graphics2D g, int x, int y) {
            g.setColor(INK);
            g.fillRect(x + 7, y + 1, 4, 16);
            g.fillRect(x + 1, y + 7, 16, 4);
            g.fillRect(x + 4, y + 4, 10, 10);
            g.setColor(color);
            g.fillRect(x + 6, y + 6, 6, 6);
            g.setColor(PAPER);
            g.fillRect(x + 8, y + 8, 2, 2);
        }
    }
}
