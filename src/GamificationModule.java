import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class: GamificationModule
 * Creator: Zeti Nur Aimar binti Ali
 * Tester: G04/SE Group 14
 *
 * Description:
 * Rewards and gamification module for the HEALIVERSE SDG 3 mental health app.
 * This module reads learning progress and quiz scores from text files, then displays
 * points, stars, badges, level progress, and leaderboard ranking.
 *
 * OOP concepts used:
 * - Interface implementation: Reward, LeaderboardProvider
 * - Abstraction: abstract Achievement class
 * - Inheritance: LearningAchievement, QuizAchievement, ComboAchievement
 * - Polymorphism/Overriding: isUnlocked() method
 * - Overloading: calculatePoints() methods
 * - Custom exception: GamificationDataException
 */
public class GamificationModule extends JPanel implements Reward, LeaderboardProvider {

    private static final String SCORES_FILE = "scores.txt";
    private static final String PROGRESS_FILE = "learning_progress.txt";

    private static final int SCREEN_WIDTH = 390;
    private static final int SCREEN_HEIGHT = 620;
    private static final int CARD_WIDTH = 336;

    private static final Color INK = HealiverseTheme.DARK_PURPLE;
    private static final Color MUTED = HealiverseTheme.MUTED_PURPLE;
    private static final Color PAPER = HealiverseTheme.CREAM;
    private static final Color SURFACE = HealiverseTheme.SURFACE;
    private static final Color LINE = HealiverseTheme.LINE;
    private static final Color PINK = HealiverseTheme.PASTEL_PINK;
    private static final Color MINT = HealiverseTheme.MINT;
    private static final Color BUTTER = HealiverseTheme.SOFT_YELLOW;
    private static final Color BLUE = HealiverseTheme.BABY_BLUE;
    private static final Color LOCKED = new Color(221, 214, 228);

    private final String username;
    private final List<Achievement> achievements;
    private final JPanel contentPanel;
    private final JLabel statusLabel;

    private GameProfile profile;
    private List<ScoreEntry> leaderboard;

    public GamificationModule(String username) {
        this.username = cleanUsername(username);
        this.achievements = new ArrayList<>();
        this.contentPanel = new JPanel();
        this.statusLabel = new JLabel(" ");

        buildAchievements();
        buildUI();
        refreshData();
    }

    public GamificationModule() {
        this("guest");
    }

    private void buildAchievements() {
        achievements.add(new LearningAchievement(
                "First Steps",
                "Start the SDG 3 mental health lesson",
                "L",
                PINK,
                0.10));

        achievements.add(new LearningAchievement(
                "Growth",
                "Reach 50% lesson progress",
                "G",
                MINT,
                0.50));

        achievements.add(new LearningAchievement(
                "Helper",
                "Complete all learning pages",
                "H",
                PINK,
                true));

        achievements.add(new QuizAchievement(
                "Quiz Star",
                "Try one mental health quiz",
                "Q",
                BUTTER,
                1,
                0));

        achievements.add(new QuizAchievement(
                "Top Score",
                "Score 80% or higher",
                "T",
                BUTTER,
                1,
                80));

        achievements.add(new ComboAchievement(
                "Wellness Warrior",
                "Complete learning and score 80% or higher",
                "W",
                BLUE));
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(PAPER);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        add(createHeader(), BorderLayout.NORTH);

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PAPER);
        contentPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(PAPER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        add(scrollPane, BorderLayout.CENTER);

     // redundancy hehe
     // statusLabel.setFont(HealiverseTheme.bodyFont(10));
     // statusLabel.setForeground(MUTED);
    // statusLabel.setBorder(new EmptyBorder(4, 12, 6, 12));
     // add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setBackground(HealiverseTheme.HEADER_GREEN);
        header.setBorder(new CompoundBorder(
                new LineBorder(LINE, 1),
                new EmptyBorder(8, 12, 8, 12)));

        JButton back = pixelButton("<");
        HealiverseTheme.setFixedSize(back, 34, 28);
        back.setEnabled(false);

        JLabel title = new JLabel("REWARDS", SwingConstants.CENTER);
        title.setFont(HealiverseTheme.titleFont(14));
        title.setForeground(INK);

        JButton refresh = pixelButton("♥");
        HealiverseTheme.setFixedSize(refresh, 34, 28);
        refresh.setToolTipText("Refresh rewards");
        refresh.addActionListener(e -> refreshData());

        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(refresh, BorderLayout.EAST);

        return header;
    }

    private void refreshData() {
        List<String> warnings = new ArrayList<>();

        profile = loadProfile(username, warnings);

        try {
            leaderboard = loadLeaderboard();
        } catch (GamificationDataException e) {
            leaderboard = new ArrayList<>();
            warnings.add(e.getMessage());
        }

        renderDashboard();

        if (!warnings.isEmpty()) {
    JOptionPane.showMessageDialog(this,
            warnings.get(0),
            "Data Notice",
            JOptionPane.WARNING_MESSAGE);
}
    }

    private void renderDashboard() {
        contentPanel.removeAll();

        contentPanel.add(createPlayerCard());
        contentPanel.add(space(8));
        contentPanel.add(createStatsCard());
        contentPanel.add(space(8));
        contentPanel.add(createBadgePanel());
        contentPanel.add(space(8));
        contentPanel.add(createLeaderboardPanel());
        contentPanel.add(space(8));
        contentPanel.add(createMotivationBanner());

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private Component space(int height) {
        return Box.createVerticalStrut(height);
    }

    // -------------------------------------------------------------------------
    // Player Card
    // -------------------------------------------------------------------------

    private JPanel createPlayerCard() {
        JPanel card = cleanCard(new BorderLayout(12, 0));
        fixedHeight(card, 112);

        JLabel avatar = imageLabel("Bunny Wave.png", 66, 66);
        if (avatar.getIcon() == null) {
            avatar = new JLabel(new AvatarIcon(62));
        }
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(72, 76));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(username);
        name.setFont(HealiverseTheme.buttonFont(14));
        name.setForeground(INK);

        JLabel level = new JLabel("Level " + getLevel());
        level.setFont(HealiverseTheme.buttonFont(12));
        level.setForeground(new Color(202, 78, 132));

        JProgressBar expBar = smallProgress(getExpIntoLevel(), getExpNeeded(), MINT);
        expBar.setString(getExpIntoLevel() + " / " + getExpNeeded() + " XP");
        expBar.setStringPainted(true);
        expBar.setMaximumSize(new Dimension(170, 14));

        JLabel note = new JLabel("Good Health and Well-Being");
        note.setFont(HealiverseTheme.bodyFont(10));
        note.setForeground(MUTED);

        info.add(Box.createVerticalStrut(2));
        info.add(name);
        info.add(Box.createVerticalStrut(3));
        info.add(level);
        info.add(Box.createVerticalStrut(5));
        info.add(expBar);
        info.add(Box.createVerticalStrut(4));
        info.add(note);

        JLabel trophy = imageLabel("Trophy Badge.png", 58, 58);
        if (trophy.getIcon() == null) {
            trophy = new JLabel(new BadgeIcon(BUTTER, "★"));
        }
        trophy.setHorizontalAlignment(SwingConstants.CENTER);
        trophy.setPreferredSize(new Dimension(62, 62));

        card.add(avatar, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(trophy, BorderLayout.EAST);

        return card;
    }

    // -------------------------------------------------------------------------
    // Stats Card
    // -------------------------------------------------------------------------

    private JPanel createStatsCard() {
        JPanel card = cleanCard(new GridLayout(1, 3, 0, 0));
        fixedHeight(card, 96);

        card.add(statBox("Heart Badge.png", "Points", String.valueOf(calculatePoints())));
        card.add(statBox("Star Badge.png", "Stars", String.valueOf(calculateStars())));
        card.add(statBox("Leaf Badge.png", "Badges", String.valueOf(getUnlockedBadges().size())));

        return card;
    }

    private JPanel statBox(String imageName, String label, String value) {
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(0, 4, 0, 4));

        JLabel icon = imageLabel(imageName, 28, 28);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel text = new JLabel(label);
        text.setFont(HealiverseTheme.bodyFont(10));
        text.setForeground(new Color(116, 84, 142));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel number = new JLabel(value);
        number.setFont(HealiverseTheme.buttonFont(13));
        number.setForeground(INK);
        number.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(icon);
        box.add(Box.createVerticalStrut(3));
        box.add(text);
        box.add(Box.createVerticalStrut(2));
        box.add(number);

        return box;
    }

    // -------------------------------------------------------------------------
    // Badges Card
    // -------------------------------------------------------------------------

    private JPanel createBadgePanel() {
        JPanel card = cleanCard(new BorderLayout(0, 8));
        fixedHeight(card, 210);

        JPanel titleRow = sectionTitleRow("Badges", getUnlockedBadges().size() + " / " + achievements.size());

        JPanel badgeGrid = new JPanel(new GridLayout(2, 3, 8, 8));
        badgeGrid.setOpaque(false);

        for (Achievement achievement : achievements) {
            badgeGrid.add(badgeTile(achievement, achievement.isUnlocked(profile)));
        }

        card.add(titleRow, BorderLayout.NORTH);
        card.add(badgeGrid, BorderLayout.CENTER);

        return card;
    }

    private JPanel badgeTile(Achievement achievement, boolean unlocked) {
        JPanel tile = new JPanel(new BorderLayout(0, 2));
        tile.setOpaque(false);
        tile.setBorder(new EmptyBorder(1, 1, 1, 1));

        JLabel icon = imageLabel(getBadgeAssetFile(achievement), 44, 44);
        if (icon.getIcon() == null) {
            icon = new JLabel(new BadgeIcon(unlocked ? achievement.getColor() : LOCKED,
                    unlocked ? achievement.getIconText() : "?"));
            icon.setHorizontalAlignment(SwingConstants.CENTER);
        }

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel name = badgeText(achievement.getName(), HealiverseTheme.buttonFont(8),
                unlocked ? INK : MUTED, 84, 24);

        JLabel status = badgeText(unlocked ? "Unlocked" : "Locked",
                HealiverseTheme.bodyFont(7),
                unlocked ? new Color(45, 128, 83) : MUTED,
                84,
                12);

        textPanel.add(name);
        textPanel.add(status);

        tile.add(icon, BorderLayout.CENTER);
        tile.add(textPanel, BorderLayout.SOUTH);

        return tile;
    }

    private JLabel badgeText(String text, Font font, Color color, int width, int height) {
        JLabel label = new JLabel("<html><center>" + text + "</center></html>", SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(width, height));
        label.setMinimumSize(new Dimension(width, height));
        label.setMaximumSize(new Dimension(width, height));
        return label;
    }

    private String getBadgeAssetFile(Achievement achievement) {
        String name = achievement.getName();

        if ("First Steps".equals(name)) {
            return "Book Badge.png";
        }
        if ("Growth".equals(name)) {
            return "Leaf Badge.png";
        }
        if ("Helper".equals(name)) {
            return "Heart Badge.png";
        }
        if ("Quiz Star".equals(name)) {
            return "Star Badge.png";
        }
        if ("Top Score".equals(name)) {
            return "Trophy Badge.png";
        }
        if ("Wellness Warrior".equals(name)) {
            return "Cloud Badge.png";
        }

        return "Cloud Badge.png";
    }

    // -------------------------------------------------------------------------
    // Leaderboard Card
    // -------------------------------------------------------------------------

    private JPanel createLeaderboardPanel() {
        JPanel card = cleanCard(new BorderLayout(0, 8));
        fixedHeight(card, 150);

        JPanel titleRow = sectionTitleRow("Leaderboard", "Best Scores");

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new GridLayout(3, 1, 0, 4));

        if (leaderboard.isEmpty()) {
            rows.add(emptyLeaderboardLabel());
            rows.add(blankRow());
            rows.add(blankRow());
        } else {
            int limit = Math.min(3, leaderboard.size());

            for (int i = 0; i < limit; i++) {
                rows.add(leaderboardRow(i + 1, leaderboard.get(i)));
            }

            for (int i = limit; i < 3; i++) {
                rows.add(blankLeaderboardRow(i + 1));
            }
        }

        card.add(titleRow, BorderLayout.NORTH);
        card.add(rows, BorderLayout.CENTER);

        return card;
    }

    private JLabel emptyLeaderboardLabel() {
        JLabel empty = new JLabel("No quiz scores yet. Finish the quiz to enter ranking.", SwingConstants.CENTER);
        empty.setFont(HealiverseTheme.bodyFont(10));
        empty.setForeground(MUTED);
        empty.setOpaque(true);
        empty.setBackground(new Color(255, 252, 247));
        empty.setBorder(new LineBorder(LINE, 1));
        return empty;
    }

    private JPanel blankRow() {
        JPanel row = new JPanel();
        row.setBackground(new Color(255, 252, 247));
        row.setBorder(new LineBorder(LINE, 1));
        return row;
    }

    private JPanel blankLeaderboardRow(int rank) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(255, 252, 247));
        row.setBorder(new CompoundBorder(
                new LineBorder(LINE, 1),
                new EmptyBorder(5, 8, 5, 8)));

        JLabel text = new JLabel(rank + "   —");
        text.setFont(HealiverseTheme.bodyFont(10));
        text.setForeground(MUTED);

        row.add(text, BorderLayout.WEST);
        return row;
    }

    private JPanel leaderboardRow(int rank, ScoreEntry entry) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        boolean currentUser = entry.getUsername().equalsIgnoreCase(username);

        row.setBackground(currentUser ? new Color(207, 243, 213) : new Color(255, 252, 247));
        row.setBorder(new CompoundBorder(
                new LineBorder(currentUser ? new Color(110, 178, 132) : LINE, 1),
                new EmptyBorder(5, 8, 5, 8)));

        JLabel left = new JLabel(rank + "   " + entry.getUsername());
        left.setFont(HealiverseTheme.buttonFont(10));
        left.setForeground(INK);

        JLabel right = new JLabel(entry.getPercentage() + "%");
        right.setFont(HealiverseTheme.buttonFont(10));
        right.setForeground(INK);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        return row;
    }

    // -------------------------------------------------------------------------
    // Motivation Banner
    // -------------------------------------------------------------------------

    private JPanel createMotivationBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(new Color(255, 239, 188));
        banner.setBorder(new CompoundBorder(
                new LineBorder(new Color(242, 186, 76), 1),
                new EmptyBorder(8, 12, 8, 12)));
        fixedHeight(banner, 40);

        JLabel text = new JLabel("Keep going! Every step counts! ✶", SwingConstants.CENTER);
        text.setFont(HealiverseTheme.buttonFont(12));
        text.setForeground(INK);

        banner.add(text, BorderLayout.CENTER);
        return banner;
    }

    // -------------------------------------------------------------------------
    // UI Helpers
    // -------------------------------------------------------------------------

    private JPanel sectionTitleRow(String leftText, String rightText) {
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        JLabel left = new JLabel(leftText);
        left.setFont(HealiverseTheme.buttonFont(12));
        left.setForeground(INK);

        JLabel right = new JLabel(rightText);
        right.setFont(HealiverseTheme.bodyFont(10));
        right.setForeground(MUTED);

        titleRow.add(left, BorderLayout.WEST);
        titleRow.add(right, BorderLayout.EAST);

        return titleRow;
    }

    private JPanel cleanCard(LayoutManager layout) {
        JPanel card = new JPanel(layout);
        card.setBackground(SURFACE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(216, 194, 190), 1),
                new EmptyBorder(10, 12, 10, 12)));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(CARD_WIDTH, Integer.MAX_VALUE));
        card.setPreferredSize(new Dimension(CARD_WIDTH, card.getPreferredSize().height));
        return card;
    }

    private void fixedHeight(JComponent component, int height) {
        Dimension size = new Dimension(CARD_WIDTH, height);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JProgressBar smallProgress(int value, int maximum, Color color) {
        JProgressBar progress = new JProgressBar(0, maximum);
        progress.setValue(Math.max(0, Math.min(maximum, value)));
        progress.setForeground(color);
        progress.setBackground(new Color(238, 228, 244));
        progress.setBorder(new LineBorder(new Color(144, 95, 182), 1));
        progress.setPreferredSize(new Dimension(150, 13));
        progress.setFont(HealiverseTheme.buttonFont(8));
        return progress;
    }

    private JButton pixelButton(String text) {
        JButton button = new JButton(text);
        button.setFont(HealiverseTheme.buttonFont(10));
        button.setForeground(INK);
        button.setBackground(new Color(255, 250, 255));
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                new LineBorder(LINE, 1),
                new EmptyBorder(4, 7, 4, 7)));
        return button;
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

    // -------------------------------------------------------------------------
    // Reward Calculations
    // -------------------------------------------------------------------------

    private int getLevel() {
        return Math.max(1, (calculatePoints() / 250) + 1);
    }

    private int getExpIntoLevel() {
        return calculatePoints() % 250;
    }

    private int getExpNeeded() {
        return 250;
    }

    @Override
    public int calculatePoints() {
        return calculatePoints(profile.getBestPercentage(), profile.isCompletedLearning());
    }

    public int calculatePoints(int bestPercentage) {
        return calculatePoints(bestPercentage, false);
    }

    public int calculatePoints(int bestPercentage, boolean completedLearning) {
        int quizPoints = bestPercentage * 5;
        int learningPoints = (int) Math.round(profile.getLearningProgress() * 200);
        int attemptPoints = profile.getQuizAttempts() * 15;
        int badgePoints = getUnlockedBadges().size() * 25;
        int completionBonus = completedLearning ? 150 : 0;

        return quizPoints + learningPoints + attemptPoints + badgePoints + completionBonus;
    }

    @Override
    public int calculateStars() {
        int stars = profile.getBestPercentage() / 20;

        if (profile.isCompletedLearning()) {
            stars++;
        }

        if (profile.getQuizAttempts() > 0 && stars == 0) {
            stars = 1;
        }

        return Math.max(0, Math.min(5, stars));
    }

    @Override
    public List<String> getUnlockedBadges() {
        List<String> names = new ArrayList<>();

        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked(profile)) {
                names.add(achievement.getName());
            }
        }

        return names;
    }

    @Override
    public String getRewardSummary() {
        return calculatePoints() + " points, "
                + calculateStars() + " stars, "
                + getUnlockedBadges().size() + " badges unlocked.";
    }

    // -------------------------------------------------------------------------
    // Data Loading
    // -------------------------------------------------------------------------

    private GameProfile loadProfile(String targetUsername, List<String> warnings) {
        GameProfile loaded = new GameProfile(targetUsername);
        readProgress(loaded, warnings);
        readScores(loaded, warnings);
        return loaded;
    }

    private void readProgress(GameProfile loaded, List<String> warnings) {
        File file = HealiversePaths.dataFile(PROGRESS_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 5);

                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(loaded.getUsername())) {
                    loaded.setCurrentPage(parseInteger(parts[1], "progress page"));
                    loaded.setTotalPages(parseInteger(parts[2], "total pages"));
                    loaded.setLastProgressTime(parts[3].trim());
                    loaded.setCompletedLearning(Boolean.parseBoolean(parts[4].trim()));
                }
            }
        } catch (IOException | GamificationDataException e) {
            warnings.add("Progress file issue: " + e.getMessage());
        }
    }

    private void readScores(GameProfile loaded, List<String> warnings) {
        List<ScoreEntry> entries = readScoreEntries(warnings);

        for (ScoreEntry entry : entries) {
            if (entry.getUsername().equalsIgnoreCase(loaded.getUsername())) {
                loaded.recordScore(entry);
            }
        }
    }

    private List<ScoreEntry> readScoreEntries(List<String> warnings) {
        List<ScoreEntry> entries = new ArrayList<>();
        File file = HealiversePaths.dataFile(SCORES_FILE);

        if (!file.exists()) {
            return entries;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    entries.add(ScoreEntry.parse(line));
                } catch (GamificationDataException e) {
                    warnings.add("Skipped score line " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            warnings.add("Score file issue: " + e.getMessage());
        }

        return entries;
    }

    @Override
    public List<ScoreEntry> loadLeaderboard() throws GamificationDataException {
        List<String> warnings = new ArrayList<>();
        List<ScoreEntry> entries = readScoreEntries(warnings);

        if (!warnings.isEmpty()) {
            throw new GamificationDataException(warnings.get(0));
        }

        Collections.sort(entries);
        return entries;
    }

    public List<ScoreEntry> loadLeaderboard(int maximumRows) throws GamificationDataException {
        List<ScoreEntry> entries = loadLeaderboard();
        int limit = Math.min(maximumRows, entries.size());
        return new ArrayList<>(entries.subList(0, limit));
    }

    private int parseInteger(String value, String label) throws GamificationDataException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new GamificationDataException("Invalid " + label + ": " + value, e);
        }
    }

    private String cleanUsername(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "guest";
        }
        return name.trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gamification Module");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new GamificationModule(args.length > 0 ? args[0] : "guest"));
            frame.setSize(390, 720);
            frame.setMinimumSize(new Dimension(390, 720));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // -------------------------------------------------------------------------
    // Fallback Pixel Icons
    // -------------------------------------------------------------------------

    private static class AvatarIcon implements Icon {
        private final int size;

        AvatarIcon(int size) {
            this.size = size;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();

            g.setColor(new Color(86, 56, 72));
            g.fillRect(x + 14, y + 4, 34, 28);

            g.setColor(new Color(255, 213, 187));
            g.fillRect(x + 18, y + 14, 26, 28);

            g.setColor(PINK);
            g.fillRect(x + 12, y + 42, 40, 18);

            g.setColor(INK);
            g.fillRect(x + 23, y + 25, 4, 4);
            g.fillRect(x + 37, y + 25, 4, 4);

            g.setColor(new Color(112, 210, 128));
            g.fillRect(x + 10, y + 6, 12, 8);
            g.fillRect(x + 42, y + 6, 12, 8);

            g.dispose();
        }
    }

    private static class BadgeIcon implements Icon {
        private final Color color;
        private final String mark;

        BadgeIcon(Color color, String mark) {
            this.color = color;
            this.mark = mark;
        }

        @Override
        public int getIconWidth() {
            return 42;
        }

        @Override
        public int getIconHeight() {
            return 42;
        }

        @Override
        public void paintIcon(Component c, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();

            g.setColor(new Color(126, 83, 153));
            g.fillRect(x + 7, y + 2, 28, 38);
            g.fillRect(x + 2, y + 7, 38, 28);

            g.setColor(color);
            g.fillRect(x + 9, y + 6, 24, 30);
            g.fillRect(x + 6, y + 9, 30, 24);

            g.setColor(INK);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));

            FontMetrics metrics = g.getFontMetrics();
            int textX = x + (42 - metrics.stringWidth(mark)) / 2;
            int textY = y + 27;

            g.drawString(mark, textX, textY);
            g.dispose();
        }
    }
}

class GamificationDataException extends Exception {
    public GamificationDataException(String message) {
        super(message);
    }

    public GamificationDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

class GameProfile {
    private final String username;
    private int bestScore;
    private int bestTotal;
    private int bestPercentage;
    private int quizAttempts;
    private int currentPage;
    private int totalPages;
    private boolean completedLearning;
    private String lastQuizTime;
    private String lastProgressTime;

    public GameProfile(String username) {
        this.username = username;
        this.bestScore = 0;
        this.bestTotal = 0;
        this.bestPercentage = 0;
        this.quizAttempts = 0;
        this.currentPage = -1;
        this.totalPages = 0;
        this.completedLearning = false;
        this.lastQuizTime = "";
        this.lastProgressTime = "";
    }

    public void recordScore(ScoreEntry entry) {
        quizAttempts++;
        lastQuizTime = entry.getTimestamp();

        if (entry.getPercentage() >= bestPercentage) {
            bestScore = entry.getScore();
            bestTotal = entry.getTotal();
            bestPercentage = entry.getPercentage();
        }
    }

    public String getUsername() {
        return username;
    }

    public int getBestScore() {
        return bestScore;
    }

    public int getBestTotal() {
        return bestTotal;
    }

    public int getBestPercentage() {
        return bestPercentage;
    }

    public int getQuizAttempts() {
        return quizAttempts;
    }

    public double getLearningProgress() {
        if (completedLearning) {
            return 1.0;
        }

        if (totalPages <= 0 || currentPage < 0) {
            return 0.0;
        }

        return Math.min(1.0, (currentPage + 1) / (double) totalPages);
    }

    public boolean isCompletedLearning() {
        return completedLearning;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setCompletedLearning(boolean completedLearning) {
        this.completedLearning = completedLearning;
    }

    public void setLastProgressTime(String lastProgressTime) {
        this.lastProgressTime = lastProgressTime;
    }

    public String getLastQuizTime() {
        return lastQuizTime;
    }

    public String getLastProgressTime() {
        return lastProgressTime;
    }
}

class ScoreEntry implements Comparable<ScoreEntry> {
    private final String username;
    private final int score;
    private final int total;
    private final int percentage;
    private final String timestamp;

    public ScoreEntry(String username, int score, int total, int percentage, String timestamp) {
        this.username = username;
        this.score = score;
        this.total = total;
        this.percentage = percentage;
        this.timestamp = timestamp;
    }

    public static ScoreEntry parse(String line) throws GamificationDataException {
        String[] parts = line.split(",", 5);

        if (parts.length < 5) {
            throw new GamificationDataException("Expected username,score,total,percentage,timestamp");
        }

        try {
            return new ScoreEntry(
                    parts[0].trim(),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim()),
                    Integer.parseInt(parts[3].trim()),
                    parts[4].trim());
        } catch (NumberFormatException e) {
            throw new GamificationDataException("Score values must be numbers", e);
        }
    }

    @Override
    public int compareTo(ScoreEntry other) {
        int byPercentage = Integer.compare(other.percentage, percentage);

        if (byPercentage != 0) {
            return byPercentage;
        }

        int byScore = Integer.compare(other.score, score);

        if (byScore != 0) {
            return byScore;
        }

        return other.timestamp.compareTo(timestamp);
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

abstract class Achievement {
    private final String name;
    private final String description;
    private final String iconText;
    private final Color color;

    public Achievement(String name, String description) {
        this(name, description, "*", new Color(230, 230, 230));
    }

    public Achievement(String name, String description, String iconText, Color color) {
        this.name = name;
        this.description = description;
        this.iconText = iconText;
        this.color = color;
    }

    public abstract boolean isUnlocked(GameProfile profile);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconText() {
        return iconText;
    }

    public Color getColor() {
        return color;
    }
}

class LearningAchievement extends Achievement {
    private final double requiredProgress;
    private final boolean requiresCompletion;

    public LearningAchievement(String name, String description, String iconText, Color color,
                               double requiredProgress) {
        super(name, description, iconText, color);
        this.requiredProgress = requiredProgress;
        this.requiresCompletion = false;
    }

    public LearningAchievement(String name, String description, String iconText, Color color,
                               boolean requiresCompletion) {
        super(name, description, iconText, color);
        this.requiredProgress = 1.0;
        this.requiresCompletion = requiresCompletion;
    }

    @Override
    public boolean isUnlocked(GameProfile profile) {
        if (requiresCompletion) {
            return profile.isCompletedLearning();
        }

        return profile.getLearningProgress() >= requiredProgress;
    }
}

class QuizAchievement extends Achievement {
    private final int requiredAttempts;
    private final int requiredPercentage;

    public QuizAchievement(String name, String description, String iconText, Color color,
                           int requiredAttempts, int requiredPercentage) {
        super(name, description, iconText, color);
        this.requiredAttempts = requiredAttempts;
        this.requiredPercentage = requiredPercentage;
    }

    @Override
    public boolean isUnlocked(GameProfile profile) {
        return profile.getQuizAttempts() >= requiredAttempts
                && profile.getBestPercentage() >= requiredPercentage;
    }
}

class ComboAchievement extends Achievement {
    public ComboAchievement(String name, String description, String iconText, Color color) {
        super(name, description, iconText, color);
    }

    @Override
    public boolean isUnlocked(GameProfile profile) {
        return profile.isCompletedLearning() && profile.getBestPercentage() >= 80;
    }
}