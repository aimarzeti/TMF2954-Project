import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Class: GamificationModule
// Creator: Zeti Nur Aimar binti Ali
// Tester: G04/SE Group 14
// Description: Cozy pixel-art gamification screen for SDG 3 Mental Health.
// Reads quiz scores and learning progress from text files, then displays
// points, stars, badges, a calm timer, progress tracker, and leaderboard.

public class GamificationModule extends JPanel implements Reward, LeaderboardProvider {

    private static final String SCORES_FILE = "scores.txt";
    private static final String PROGRESS_FILE = "learning_progress.txt";

    private static final Color INK = HealiverseTheme.DARK_PURPLE;
    private static final Color PAPER = HealiverseTheme.CREAM;
    private static final Color PANEL = HealiverseTheme.SURFACE;
    private static final Color PINK = HealiverseTheme.PASTEL_PINK;
    private static final Color SOFT_PINK = HealiverseTheme.PASTEL_PINK;
    private static final Color MINT = HealiverseTheme.MINT;
    private static final Color BUTTER = HealiverseTheme.SOFT_YELLOW;
    private static final Color LAVENDER = HealiverseTheme.LAVENDER;
    private static final Color BLUE = HealiverseTheme.BABY_BLUE;
    private static final Color LOCKED = new Color(221, 214, 228);

    private final String username;
    private final List<Achievement> achievements;
    private final JPanel contentPanel;
    private final JLabel timerLabel;
    private final JProgressBar timerProgress;
    private final JLabel statusLabel;

    private GameProfile profile;
    private List<ScoreEntry> leaderboard;
    private javax.swing.Timer calmTimer;
    private int secondsRemaining;

    public GamificationModule(String username) {
        this.username = cleanUsername(username);
        this.achievements = new ArrayList<>();
        this.contentPanel = new JPanel();
        this.timerLabel = new JLabel("02:00", SwingConstants.CENTER);
        this.timerProgress = new JProgressBar(0, 120);
        this.statusLabel = new JLabel(" ");
        this.secondsRemaining = 120;

        buildAchievements();
        buildUI();
        refreshData();
    }

    public GamificationModule() {
        this("guest");
    }

    private void buildAchievements() {
        if (useCroppedBadgeSet()) {
            addCroppedAchievements();
            return;
        }

        achievements.add(new LearningAchievement(
                "Stress Slayer",
                "Start learning",
                "♥",
                SOFT_PINK,
                0.10));

        achievements.add(new LearningAchievement(
                "Hydration Helper",
                "Reach 50% lesson progress",
                "D",
                SOFT_PINK,
                0.50));

        achievements.add(new LearningAchievement(
                "Mood Hero",
                "Finish lessons",
                "☁",
                BLUE,
                true));

        achievements.add(new QuizAchievement(
                "Knowledge Seeker",
                "Try one quiz",
                "✦",
                MINT,
                1,
                0));

        achievements.add(new QuizAchievement(
                "Sleep Master",
                "Score 60%+",
                "S",
                LAVENDER,
                1,
                60));

        achievements.add(new QuizAchievement(
                "Wellness Warrior",
                "Score 80%+",
                "★",
                BUTTER,
                1,
                80));

        achievements.add(new QuizAchievement(
                "Self Care Champion",
                "Try 3 quizzes",
                "♦",
                LAVENDER,
                3,
                0));

        achievements.add(new ComboAchievement(
                "Consistency King",
                "Learn + score 80%+",
                "♛",
                PINK));
    }

    private boolean useCroppedBadgeSet() {
        return true;
    }

    private void addCroppedAchievements() {
        achievements.add(new LearningAchievement(
                "Learning Explorer",
                "Start the SDG 3 mental health lesson",
                "L",
                LAVENDER,
                0.10));

        achievements.add(new LearningAchievement(
                "Growth Tracker",
                "Reach 50% lesson progress",
                "G",
                MINT,
                0.50));

        achievements.add(new LearningAchievement(
                "Calm Time",
                "Reach the mindfulness and breathing lesson",
                "C",
                BLUE,
                0.80));

        achievements.add(new LearningAchievement(
                "Heart Helper",
                "Complete all learning pages",
                "H",
                SOFT_PINK,
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
                "Complete learning and score 80%+",
                "W",
                MINT));
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(PAPER);
        setPreferredSize(new Dimension(HealiverseTheme.MODULE_WIDTH, HealiverseTheme.MODULE_HEIGHT));

        add(createHeader(), BorderLayout.NORTH);

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PAPER);
        contentPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(PAPER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        add(scrollPane, BorderLayout.CENTER);

        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        statusLabel.setForeground(new Color(111, 92, 130));
        statusLabel.setBorder(new EmptyBorder(3, 8, 5, 8));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(6, 0));
        header.setBackground(HealiverseTheme.HEADER_GREEN);
        header.setBorder(new CompoundBorder(
                new LineBorder(HealiverseTheme.LINE, 1),
                new EmptyBorder(6, 10, 6, 10)));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        brand.setOpaque(false);

        JLabel title = new JLabel("REWARDS");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(INK);
        brand.add(title);

        JButton refresh = pixelButton("R");
        HealiverseTheme.setFixedSize(refresh, 38, 28);
        refresh.setToolTipText("Refresh progress");
        refresh.addActionListener(e -> refreshData());

        header.add(brand, BorderLayout.WEST);
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
        statusLabel.setText(warnings.isEmpty()
                ? "Keep going! Every step counts!"
                : "Note: " + warnings.get(0));
    }

    private void renderDashboard() {
        contentPanel.removeAll();
        contentPanel.add(createPlayerStatusPanel());
        contentPanel.add(space());
        contentPanel.add(createProgressTrackerPanel());
        contentPanel.add(space());
        contentPanel.add(createBadgePanel());
        contentPanel.add(space());
        contentPanel.add(createCalmQuestPanel());
        contentPanel.add(space());
        contentPanel.add(createLeaderboardPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private Component space() {
        return Box.createVerticalStrut(7);
    }

    private JPanel createPlayerStatusPanel() {
        JPanel panel = sectionPanel("PLAYER STATUS", SOFT_PINK, new BorderLayout(6, 6));

        JPanel avatarBox = new JPanel(new BorderLayout());
        avatarBox.setOpaque(false);
        avatarBox.add(new JLabel(new AvatarIcon(58)), BorderLayout.CENTER);

        JPanel stats = new JPanel();
        stats.setOpaque(false);
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(username);
        name.setFont(new Font("SansSerif", Font.BOLD, 13));
        name.setForeground(INK);

        JLabel level = new JLabel("Good Health and Well-Being  Lv." + getLevel());
        level.setFont(new Font("SansSerif", Font.BOLD, 11));
        level.setForeground(INK);

        JProgressBar exp = smallProgress(getExpIntoLevel(), getExpNeeded(), MINT);
        exp.setString(getExpIntoLevel() + " / " + getExpNeeded() + " EXP");
        exp.setStringPainted(true);

        stats.add(name);
        stats.add(level);
        stats.add(Box.createVerticalStrut(4));
        stats.add(exp);
        stats.add(Box.createVerticalStrut(3));
        stats.add(createRewardHint());

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);
        top.add(avatarBox, BorderLayout.WEST);
        top.add(stats, BorderLayout.CENTER);

        JPanel resources = new JPanel(new GridLayout(2, 2, 4, 4));
        resources.setOpaque(false);
        resources.add(resourceRow("Hearts", "5 / 5", SOFT_PINK));
        resources.add(resourceRow("Stars", String.valueOf(calculateStars()), BUTTER));
        resources.add(resourceRow("Coins", String.valueOf(calculatePoints()), new Color(255, 202, 91)));
        resources.add(resourceRow("Badges", String.valueOf(getUnlockedBadges().size()), LAVENDER));

        panel.add(top, BorderLayout.NORTH);
        panel.add(resources, BorderLayout.CENTER);
        return panel;
    }

    private JPanel resourceRow(String label, String value, Color color) {
        JPanel row = new JPanel(new BorderLayout(4, 0));
        row.setBackground(color);
        row.setBorder(new CompoundBorder(new LineBorder(INK, 1), new EmptyBorder(3, 4, 3, 4)));

        JLabel icon = imageLabel(getResourceAsset(label), 20, 20);
        JLabel text = new JLabel("<html><b>" + value + "</b><br>" + cleanResourceLabel(label) + "</html>");
        text.setForeground(INK);
        text.setFont(new Font("SansSerif", Font.BOLD, 9));

        row.add(icon, BorderLayout.WEST);
        row.add(text, BorderLayout.CENTER);
        return row;
    }

    private String getResourceAsset(String label) {
        String lower = label.toLowerCase();
        if (lower.contains("heart")) {
            return "Heart.png";
        }
        if (lower.contains("star")) {
            return "Star.png";
        }
        if (lower.contains("coin")) {
            return "XP Coin.png";
        }
        if (lower.contains("badge")) {
            return "Trophy.png";
        }
        return "Sparkle Purple.png";
    }

    private String cleanResourceLabel(String label) {
        if (label.toLowerCase().contains("heart")) {
            return "Hearts";
        }
        if (label.toLowerCase().contains("star")) {
            return "Stars";
        }
        if (label.toLowerCase().contains("coin")) {
            return "Points";
        }
        if (label.toLowerCase().contains("badge")) {
            return "Badges";
        }
        return label;
    }

    private JLabel createRewardHint() {
        JLabel hint = new JLabel("Next reward: learn + quiz", SwingConstants.LEFT);
        hint.setFont(new Font("SansSerif", Font.BOLD, 9));
        hint.setForeground(new Color(116, 84, 142));
        return hint;
    }

    private JPanel createCollectiblesPanel() {
        JPanel panel = sectionPanel("COLLECTIBLES", MINT, new BorderLayout(0, 6));
        JPanel grid = new JPanel(new GridLayout(0, 4, 8, 6));
        grid.setOpaque(false);

        grid.add(collectibleIcon("Heart.png", "CARE"));
        grid.add(collectibleIcon("Heart Green.png", "HEALTH"));
        grid.add(collectibleIcon("Health.png", "SDG 3"));
        grid.add(collectibleIcon("Health Wings.png", "PROTECT"));
        grid.add(collectibleIcon("Leaf.png", "GROW"));
        grid.add(collectibleIcon("Potion.png", "CALM"));
        grid.add(collectibleIcon("Crescent Moon.png", "REST"));
        grid.add(collectibleIcon("Star.png", "QUIZ"));
        grid.add(collectibleIcon("Trophy.png", "RANK"));
        grid.add(collectibleIcon("Sparkle Blue.png", "FOCUS"));
        grid.add(collectibleIcon("Sparkle Pink.png", "MOOD"));
        grid.add(collectibleIcon("Sparkle Yellow.png", "JOY"));

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel collectibleIcon(String asset, String label) {
        JPanel tile = new JPanel();
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setOpaque(false);
        tile.setPreferredSize(new Dimension(72, 48));

        JLabel icon = imageLabel(asset, 26, 26);
        if (icon.getIcon() == null) {
            icon = new JLabel(new CollectibleIcon(label));
        }
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel text = new JLabel(label);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        text.setFont(new Font("SansSerif", Font.BOLD, 7));
        text.setForeground(INK);

        tile.add(icon);
        tile.add(text);
        return tile;
    }

    private JPanel createBadgePanel() {
        JPanel panel = sectionPanel("BADGES", LAVENDER, new BorderLayout(0, 8));

        JPanel grid = new JPanel(new GridLayout(0, 2, 8, 8));
        grid.setOpaque(false);
        for (Achievement achievement : achievements) {
            grid.add(badgeTile(achievement, achievement.isUnlocked(profile)));
        }

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel badgeTile(Achievement achievement, boolean unlocked) {
        JPanel tile = new JPanel();
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setBackground(unlocked ? new Color(255, 250, 255) : new Color(241, 236, 246));
        tile.setBorder(new CompoundBorder(new LineBorder(INK, 1), new EmptyBorder(5, 3, 5, 3)));
        tile.setPreferredSize(new Dimension(150, 104));

        JLabel icon = unlocked ? imageLabel(getBadgeAssetFile(achievement), 56, 56) : new JLabel();
        if (icon.getIcon() == null) {
            icon = new JLabel(new BadgeIcon(unlocked ? achievement.getColor() : LOCKED,
                    unlocked ? achievement.getIconText() : "?"));
        }
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel("<html><center>" + achievement.getName() + "</center></html>");
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setFont(new Font("SansSerif", Font.BOLD, 9));
        name.setForeground(unlocked ? INK : new Color(128, 118, 138));

        tile.add(icon);
        tile.add(Box.createVerticalStrut(4));
        tile.add(name);
        return tile;
    }

    private JPanel createAchievementPopupPanel() {
        Achievement featured = findFeaturedAchievement();
        boolean unlocked = featured != null && featured.isUnlocked(profile);

        JPanel panel = sectionPanel(unlocked ? "NEW BADGE UNLOCKED" : "NEXT BADGE", PINK, new BorderLayout(8, 6));
        if (featured == null) {
            JLabel done = new JLabel("<html><center>All badges unlocked. Amazing progress!</center></html>");
            done.setFont(new Font("SansSerif", Font.BOLD, 12));
            done.setForeground(INK);
            panel.add(done, BorderLayout.CENTER);
            return panel;
        }

        JLabel badge = imageLabel(getBadgeAssetFile(featured), 78, 78);
        if (badge.getIcon() == null) {
            badge = new JLabel(new BadgeIcon(unlocked ? featured.getColor() : LOCKED,
                    unlocked ? featured.getIconText() : "?"));
        }
        panel.add(badge, BorderLayout.WEST);

        JLabel text = new JLabel("<html><b>" + featured.getName() + "</b><br>"
                + featured.getDescription() + "<br>"
                + (unlocked ? "Great job, keep going!" : "Complete more activities to unlock.") + "</html>");
        text.setFont(new Font("SansSerif", Font.PLAIN, 12));
        text.setForeground(INK);
        panel.add(text, BorderLayout.CENTER);
        return panel;
    }

    private Achievement findFeaturedAchievement() {
        Achievement firstLocked = null;
        Achievement latestUnlocked = null;
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked(profile)) {
                latestUnlocked = achievement;
            } else if (firstLocked == null) {
                firstLocked = achievement;
            }
        }
        return latestUnlocked != null ? latestUnlocked : firstLocked;
    }

    private JPanel createProgressTrackerPanel() {
        JPanel panel = sectionPanel("PROGRESS TRACKER", BLUE, new BorderLayout(0, 8));

        int overall = getOverallProgressPercent();
        JProgressBar overallBar = smallProgress(overall, 100, MINT);
        overallBar.setString(overall + "%");
        overallBar.setStringPainted(true);

        JPanel progressTop = new JPanel(new BorderLayout(8, 0));
        progressTop.setOpaque(false);

        JLabel trackerBadge = imageLabel("Growth Tracker Badge.png", 64, 72);

        JPanel progressCopy = new JPanel();
        progressCopy.setOpaque(false);
        progressCopy.setLayout(new BoxLayout(progressCopy, BoxLayout.Y_AXIS));
        JLabel summary = new JLabel("<html>Learning, quiz score, and badges combined.</html>");
        summary.setFont(new Font("SansSerif", Font.BOLD, 11));
        summary.setForeground(INK);
        overallBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressCopy.add(summary);
        progressCopy.add(Box.createVerticalStrut(4));
        progressCopy.add(overallBar);

        progressTop.add(trackerBadge, BorderLayout.WEST);
        progressTop.add(progressCopy, BorderLayout.CENTER);

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.add(progressRow("Understanding Stress", profile.getLearningProgress() > 0.0));
        rows.add(progressRow("Learning Module", profile.isCompletedLearning()));
        rows.add(progressRow("Quiz Battle", profile.getQuizAttempts() > 0));
        rows.add(progressRow("Score 80%+", profile.getBestPercentage() >= 80));

        panel.add(progressTop, BorderLayout.NORTH);
        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);
        top.add(imageLabel("Top Score Badge.png", 62, 70), BorderLayout.WEST);
        top.add(rows, BorderLayout.CENTER);

        panel.add(top, BorderLayout.CENTER);
        return panel;
    }

    private JLabel progressRow(String text, boolean done) {
        JLabel row = new JLabel((done ? "[x] " : "[ ] ") + text);
        row.setOpaque(true);
        row.setBackground(done ? new Color(235, 255, 235) : new Color(255, 246, 238));
        row.setForeground(INK);
        row.setFont(new Font("SansSerif", Font.BOLD, 10));
        row.setBorder(new CompoundBorder(new LineBorder(new Color(221, 202, 214), 1),
                new EmptyBorder(4, 6, 4, 6)));
        return row;
    }

    private JPanel createCalmQuestPanel() {
        JPanel panel = sectionPanel("CALM TIMER QUEST", MINT, new BorderLayout(8, 6));

        JLabel badge = imageLabel("Calm Time Badge.png", 64, 74);

        JLabel text = new JLabel("<html>2-minute breathing break before study mode.</html>");
        text.setFont(new Font("SansSerif", Font.BOLD, 11));
        text.setForeground(INK);

        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 21));
        timerLabel.setForeground(INK);
        timerProgress.setForeground(PINK);
        timerProgress.setBackground(Color.WHITE);
        timerProgress.setBorderPainted(false);
        timerProgress.setValue(120 - secondsRemaining);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 6, 0));
        buttons.setOpaque(false);
        JButton start = pixelButton("START");
        start.addActionListener(this::startCalmTimer);
        JButton reset = pixelButton("RESET");
        reset.addActionListener(e -> resetCalmTimer());
        buttons.add(start);
        buttons.add(reset);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(text);
        center.add(Box.createVerticalStrut(4));
        center.add(timerLabel);
        center.add(imageLabel("Crescent Moon.png", 28, 28));
        center.add(timerProgress);
        center.add(Box.createVerticalStrut(6));
        center.add(buttons);

        panel.add(badge, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLeaderboardPanel() {
        JPanel panel = sectionPanel("WELLNESS LEADERBOARD", BUTTER, new BorderLayout(0, 8));

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        if (leaderboard.isEmpty()) {
            JLabel empty = new JLabel("<html>No quiz scores yet.<br>Finish the quiz to enter the ranking.</html>");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 12));
            empty.setForeground(INK);
            rows.add(empty);
        } else {
            int limit = Math.min(5, leaderboard.size());
            for (int i = 0; i < limit; i++) {
                rows.add(leaderboardRow(i + 1, leaderboard.get(i)));
                if (i < limit - 1) {
                    rows.add(Box.createVerticalStrut(4));
                }
            }
        }

        panel.add(rows, BorderLayout.CENTER);
        return panel;
    }

    private JPanel leaderboardRow(int rank, ScoreEntry entry) {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        boolean currentUser = entry.getUsername().equalsIgnoreCase(username);
        row.setBackground(currentUser ? SOFT_PINK : Color.WHITE);
        row.setBorder(new CompoundBorder(new LineBorder(INK, 1), new EmptyBorder(5, 7, 5, 7)));

        JLabel left = new JLabel(rank + ". " + entry.getUsername());
        left.setFont(new Font("SansSerif", Font.BOLD, 11));
        left.setForeground(INK);

        JLabel right = new JLabel(entry.getPercentage() + "%");
        right.setFont(new Font("SansSerif", Font.BOLD, 11));
        right.setForeground(INK);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    private JPanel sectionPanel(String title, Color accent, LayoutManager layout) {
        JPanel outer = new JPanel(new BorderLayout(0, 5));
        outer.setBackground(PANEL);
        outer.setBorder(new CompoundBorder(
                new LineBorder(HealiverseTheme.LINE, 1),
                new EmptyBorder(7, 7, 7, 7)));
        outer.setAlignmentX(Component.LEFT_ALIGNMENT);
        outer.setMaximumSize(new Dimension(340, Integer.MAX_VALUE));

        JLabel ribbon = new JLabel(" " + title + " ", SwingConstants.CENTER);
        ribbon.setOpaque(true);
        ribbon.setBackground(accent);
        ribbon.setForeground(INK);
        ribbon.setFont(new Font("SansSerif", Font.BOLD, 11));
        ribbon.setBorder(new EmptyBorder(4, 6, 4, 6));

        JPanel body = new JPanel(layout);
        body.setOpaque(false);
        outer.add(ribbon, BorderLayout.NORTH);
        outer.add(body, BorderLayout.CENTER);
        return bodyWrapper(outer, body);
    }

    private JPanel bodyWrapper(JPanel outer, JPanel body) {
        outer.putClientProperty("body", body);
        SectionBridgePanel wrapper = new SectionBridgePanel(outer, body);
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(340, Integer.MAX_VALUE));
        return wrapper;
    }

    private JProgressBar smallProgress(int value, int maximum, Color color) {
        JProgressBar progress = new JProgressBar(0, maximum);
        progress.setValue(Math.max(0, Math.min(maximum, value)));
        progress.setForeground(color);
        progress.setBackground(new Color(238, 228, 244));
        progress.setBorder(new LineBorder(INK, 1));
        progress.setPreferredSize(new Dimension(150, 13));
        progress.setFont(new Font("SansSerif", Font.BOLD, 9));
        return progress;
    }

    private JButton pixelButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 10));
        button.setForeground(INK);
        button.setBackground(new Color(255, 250, 255));
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(new LineBorder(INK, 2), new EmptyBorder(4, 7, 4, 7)));
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

    private String getBadgeAssetFile(Achievement achievement) {
        String name = achievement.getName();
        if ("Learning Explorer".equals(name)) {
            return "Learning Explorer Badge.png";
        }
        if ("Growth Tracker".equals(name)) {
            return "Growth Tracker Badge.png";
        }
        if ("Calm Time".equals(name)) {
            return "Calm Time Badge.png";
        }
        if ("Heart Helper".equals(name)) {
            return "Heart Badge.png";
        }
        if ("Quiz Star".equals(name)) {
            return "Quiz Star Badge.png";
        }
        if ("Top Score".equals(name)) {
            return "Top Score Badge.png";
        }
        if ("Wellness Warrior".equals(name)) {
            return "Wellness Warrior Badge.png";
        }
        if ("Stress Slayer".equals(name)) {
            return "badge_stress_slayer.png";
        }
        if ("Hydration Helper".equals(name)) {
            return "badge_hydration_helper.png";
        }
        if ("Mood Hero".equals(name)) {
            return "badge_mood_hero.png";
        }
        if ("Knowledge Seeker".equals(name)) {
            return "badge_knowledge_seeker.png";
        }
        if ("Sleep Master".equals(name)) {
            return "badge_sleep_master.png";
        }
        if ("Wellness Warrior".equals(name)) {
            return "badge_wellness_warrior.png";
        }
        if ("Self Care Champion".equals(name)) {
            return "badge_self_care_champion.png";
        }
        if ("Consistency King".equals(name)) {
            return "badge_consistency_king.png";
        }
        return null;
    }

    private void startCalmTimer(ActionEvent event) {
        if (calmTimer != null && calmTimer.isRunning()) {
            return;
        }

        calmTimer = new javax.swing.Timer(1000, e -> {
            secondsRemaining--;
            updateTimerDisplay();
            if (secondsRemaining <= 0) {
                calmTimer.stop();
                JOptionPane.showMessageDialog(this,
                        "Calm quest complete! You earned a mindful break.",
                        "Quest Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        calmTimer.start();
    }

    private void resetCalmTimer() {
        if (calmTimer != null) {
            calmTimer.stop();
        }
        secondsRemaining = 120;
        updateTimerDisplay();
    }

    private void updateTimerDisplay() {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        timerProgress.setValue(120 - secondsRemaining);
    }

    private int getLevel() {
        return Math.max(1, (calculatePoints() / 250) + 1);
    }

    private int getExpIntoLevel() {
        return calculatePoints() % 250;
    }

    private int getExpNeeded() {
        return 250;
    }

    private int getOverallProgressPercent() {
        double learning = profile.getLearningProgress();
        double quiz = profile.getBestPercentage() / 100.0;
        double badges = getUnlockedBadges().size() / (double) achievements.size();
        return (int) Math.round(((learning + quiz + badges) / 3.0) * 100);
    }

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
        return calculatePoints() + " points, " + calculateStars() + " stars, "
                + getUnlockedBadges().size() + " badges unlocked.";
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
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static class SectionBridgePanel extends JPanel {
        private final JPanel outer;
        private final JPanel body;

        SectionBridgePanel(JPanel outer, JPanel body) {
            super(new BorderLayout());
            this.outer = outer;
            this.body = body;
            super.add(outer, BorderLayout.CENTER);
        }

        @Override
        public Component add(Component component) {
            return body.add(component);
        }

        @Override
        public void add(Component component, Object constraints) {
            body.add(component, constraints);
        }
    }

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
            g.fillRect(x + 14, y + 6, 36, 32);
            g.setColor(new Color(255, 213, 187));
            g.fillRect(x + 18, y + 16, 28, 28);
            g.setColor(PINK);
            g.fillRect(x + 12, y + 42, 40, 18);
            g.setColor(INK);
            g.fillRect(x + 23, y + 27, 4, 4);
            g.fillRect(x + 38, y + 27, 4, 4);
            g.setColor(SOFT_PINK);
            g.fillRect(x + 9, y + 11, 14, 9);
            g.fillRect(x + 43, y + 11, 14, 9);
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
            int textY = y + 26;
            g.drawString(mark, textX, textY);
            g.dispose();
        }
    }

    private static class CollectibleIcon implements Icon {
        private final String label;

        CollectibleIcon(String label) {
            this.label = label;
        }

        @Override
        public int getIconWidth() {
            return 24;
        }

        @Override
        public int getIconHeight() {
            return 24;
        }

        @Override
        public void paintIcon(Component c, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setColor(new Color(126, 83, 153));
            g.fillRect(x + 4, y + 2, 16, 20);
            g.fillRect(x + 2, y + 4, 20, 16);
            g.setColor(colorForLabel());
            g.fillRect(x + 6, y + 5, 12, 14);
            g.fillRect(x + 5, y + 7, 14, 10);
            g.setColor(INK);
            g.setFont(new Font("SansSerif", Font.BOLD, 9));
            String mark = label.substring(0, 1);
            FontMetrics metrics = g.getFontMetrics();
            int textX = x + (24 - metrics.stringWidth(mark)) / 2;
            g.drawString(mark, textX, y + 15);
            g.dispose();
        }

        private Color colorForLabel() {
            if (label.contains("STAR") || label.contains("MOOD") || label.contains("REST")) {
                return BUTTER;
            }
            if (label.contains("COIN") || label.contains("GOLD")) {
                return new Color(255, 202, 91);
            }
            if (label.contains("GEM")) {
                return MINT;
            }
            if (label.contains("FOCUS") || label.contains("MIND")) {
                return BLUE;
            }
            return SOFT_PINK;
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
