import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class: LoginApplication
 * Creator: Keweil Anak Bansa
 * Tester: G04/SE Group 14
 * Description: Login and sign-up screen for the HEALIVERSE app.
 * Implements UserSession to manage login state and user credentials.
 * Connects to MentalHealthGameApp (Main Dashboard) after successful login.
 * Part of TMF2954 Java Programming Project — SDG 3: Good Health & Well-Being.
 */

public class LoginApplication extends JFrame implements UserSession {

    // --- User credentials storage ---
    private HashMap<String, String> userDatabase;

    // --- Current session info ---
    private String currentUser = null;
    private static final String USERS_FILE = "users.txt";

    // --- GUI Components ---
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private static final Color SKY_TOP = HealiverseTheme.SKY_TOP;
    private static final Color SKY_BOTTOM = HealiverseTheme.SKY_BOTTOM;
    private static final Color CARD_BG = HealiverseTheme.SURFACE;
    private static final Color INK = HealiverseTheme.DARK_PURPLE;
    private static final Color MUTED = HealiverseTheme.MUTED_PURPLE;
    private static final Color BORDER = HealiverseTheme.PIXEL_PURPLE;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    public LoginApplication() {
        userDatabase = new HashMap<>();
        userDatabase.put("admin", "password123"); // default test account
        loadUsersFromFile();

        buildUI();
    }

    // -------------------------------------------------------------------------
    // UserSession interface implementation
    // -------------------------------------------------------------------------
    @Override
    public String getUsername() {
        return currentUser != null ? currentUser : "";
    }

    @Override
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @Override
    public void logout() {
        currentUser = null;
        dispose();
        new LoginApplication().setVisible(true);
    }

    // -------------------------------------------------------------------------
    // Build the UI
    // -------------------------------------------------------------------------
    private void buildUI() {
        setTitle("HEALIVERSE - TMF2954 Java Programming (G14 - G04/SE)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        PixelSkyPanel shell = new PixelSkyPanel();
        shell.setLayout(new BorderLayout());
        shell.setPreferredSize(new Dimension(HealiverseTheme.PHONE_WIDTH, HealiverseTheme.PHONE_HEIGHT));
        shell.setBorder(new LineBorder(BORDER, 1));

        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(18, 20, 14, 20));

        main.add(buildCleanHeaderPanel());
        main.add(Box.createVerticalStrut(14));
        main.add(buildCleanLoginCard());
        main.add(Box.createVerticalGlue());
        main.add(buildFooterLabel());

        shell.add(main, BorderLayout.CENTER);

        setContentPane(shell);
        pack();
        setLocationRelativeTo(null);
    }

    // -------------------------------------------------------------------------
    // Header - HEALIVERSE branding
    // -------------------------------------------------------------------------
    private JPanel buildCleanHeaderPanel() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        HealiverseTheme.setFixedSize(header, HealiverseTheme.LOGIN_CARD_WIDTH, 160);

        JLabel logo = new JLabel("HEALIVERSE", SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setFont(HealiverseTheme.titleFont(31));
        logo.setForeground(INK);
        logo.setMaximumSize(new Dimension(HealiverseTheme.LOGIN_CARD_WIDTH, 92));
        header.add(logo);
        header.add(Box.createVerticalStrut(8));

        JLabel sdgTag = new JLabel("SDG 3: Good Health & Well-Being", SwingConstants.CENTER);
        sdgTag.setFont(HealiverseTheme.buttonFont(11));
        sdgTag.setForeground(new Color(41, 110, 79));
        sdgTag.setOpaque(true);
        sdgTag.setBackground(new Color(221, 242, 228));
        sdgTag.setBorder(new CompoundBorder(
                new LineBorder(new Color(143, 187, 155), 1),
                new EmptyBorder(5, 12, 5, 12)));
        sdgTag.setAlignmentX(Component.CENTER_ALIGNMENT);
        sdgTag.setMaximumSize(new Dimension(286, 30));
        header.add(sdgTag);
        header.add(Box.createVerticalStrut(8));

        JLabel tagline = new JLabel("Mental health education with learning, quiz, and rewards", SwingConstants.CENTER);
        tagline.setFont(HealiverseTheme.bodyFont(12));
        tagline.setForeground(MUTED);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagline.setMaximumSize(new Dimension(HealiverseTheme.LOGIN_CARD_WIDTH, 34));
        header.add(tagline);

        return header;
    }

    private JPanel buildCleanLoginCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setPreferredSize(new Dimension(HealiverseTheme.LOGIN_CARD_WIDTH, 440));
        card.setMaximumSize(new Dimension(HealiverseTheme.LOGIN_CARD_WIDTH, 440));
        card.setBorder(new CompoundBorder(
                new LineBorder(HealiverseTheme.LINE, 1),
                new EmptyBorder(18, 24, 18, 24)));

        JLabel title = new JLabel("Welcome Back!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(INK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel helper = new JLabel("Log in to continue your journey", SwingConstants.CENTER);
        helper.setFont(HealiverseTheme.bodyFont(11));
        helper.setForeground(MUTED);
        helper.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(helper);
        card.add(Box.createVerticalStrut(18));

        JLabel usernameLabel = makeFieldLabel("Username");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setMaximumSize(new Dimension(HealiverseTheme.LOGIN_FIELD_WIDTH, 18));
        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(6));

        usernameField = new PlaceholderTextField("Enter username");
        styleTextField(usernameField);
        JPanel usernameRow = buildInputRow(null, usernameField, null, "U");
        usernameRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(usernameRow);
        card.add(Box.createVerticalStrut(14));

        JLabel passwordLabel = makeFieldLabel("Password");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setMaximumSize(new Dimension(HealiverseTheme.LOGIN_FIELD_WIDTH, 18));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(6));

        passwordField = new PlaceholderPasswordField("Enter password");
        styleTextField(passwordField);
        JPanel passwordRow = buildPasswordRow(passwordField);
        passwordRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passwordRow);
        card.add(Box.createVerticalStrut(8));

        JPanel optionRow = new JPanel(new BorderLayout());
        optionRow.setOpaque(false);
        optionRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionRow.setMaximumSize(new Dimension(HealiverseTheme.LOGIN_FIELD_WIDTH, 26));

        JCheckBox rememberBox = new JCheckBox("Remember me");
        rememberBox.setOpaque(false);
        rememberBox.setFocusPainted(false);
        rememberBox.setFont(HealiverseTheme.bodyFont(10));
        rememberBox.setForeground(new Color(92, 73, 133));

        JLabel forgotLabel = new JLabel("Forgot?");
        forgotLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        forgotLabel.setForeground(new Color(117, 90, 188));

        optionRow.add(rememberBox, BorderLayout.WEST);
        optionRow.add(forgotLabel, BorderLayout.EAST);
        card.add(optionRow);
        card.add(Box.createVerticalStrut(8));

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(HealiverseTheme.bodyFont(11));
        messageLabel.setForeground(new Color(200, 50, 80));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setMaximumSize(new Dimension(HealiverseTheme.LOGIN_FIELD_WIDTH, 20));
        card.add(messageLabel);
        card.add(Box.createVerticalStrut(10));

        JButton loginBtn = makeActionButton("LOGIN", HealiverseTheme.PASTEL_PINK);
        HealiverseTheme.setFixedSize(loginBtn, HealiverseTheme.LOGIN_FIELD_WIDTH, 38);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(8));

        JButton signUpBtn = makeActionButton("SIGN UP", HealiverseTheme.MINT);
        HealiverseTheme.setFixedSize(signUpBtn, HealiverseTheme.LOGIN_FIELD_WIDTH, 38);
        signUpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(signUpBtn);
        card.add(Box.createVerticalStrut(8));

        JButton guestBtn = makeActionButton("CONTINUE AS GUEST", HealiverseTheme.BABY_BLUE);
        HealiverseTheme.setFixedSize(guestBtn, HealiverseTheme.LOGIN_FIELD_WIDTH, 36);
        guestBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(guestBtn);

        LoginAction loginAction = new LoginAction();
        loginBtn.addActionListener(loginAction);
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        signUpBtn.addActionListener(new SignUpAction());
        guestBtn.addActionListener(e -> {
            currentUser = "guest";
            new MentalHealthGameApp("guest").setVisible(true);
            dispose();
        });

        return card;
    }

    private JPanel buildHeaderPanel() {
        JPanel header = new JPanel(null);
        header.setOpaque(false);
        header.setBounds(0, 18, HealiverseTheme.PHONE_WIDTH, 260);

        JLabel leaf = createTinyIcon("Green Heart Floating.png", 22, 22, "*");
        leaf.setBounds(120, 18, 24, 24);
        header.add(leaf);

        JLabel heart = createTinyIcon("Heart.png", 18, 18, "H");
        heart.setBounds(292, 20, 22, 22);
        header.add(heart);

        JLabel logo = new JLabel("HEALIVERSE", SwingConstants.CENTER);
        ImageIcon logoIcon = HealiversePaths.loadPixelIcon("Healiverse.png", 320, 128);
        if (logoIcon != null) {
            logo.setIcon(logoIcon);
        } else {
            logo.setFont(HealiverseTheme.titleFont(31));
            logo.setForeground(new Color(82, 58, 125));
        }
        logo.setBounds(55, 32, 320, 128);
        header.add(logo);

        JLabel sdgTag = new JLabel("SDG 3: Good Health & Well-Being", SwingConstants.CENTER);
        sdgTag.setFont(HealiverseTheme.buttonFont(11));
        sdgTag.setForeground(new Color(41, 110, 79));
        sdgTag.setOpaque(true);
        sdgTag.setBackground(new Color(184, 236, 198));
        sdgTag.setBorder(new LineBorder(new Color(83, 150, 111), 2));
        sdgTag.setBounds(95, 166, 240, 29);
        header.add(sdgTag);

        JLabel tagline = new JLabel("Mental health education made gentle and fun", SwingConstants.CENTER);
        tagline.setFont(HealiverseTheme.bodyFont(11));
        tagline.setForeground(new Color(112, 83, 161));
        tagline.setBounds(55, 202, 320, 22);
        header.add(tagline);

        JLabel floatingHeart = createTinyIcon("Green Heart Floating.png", 78, 78, "♥");
        floatingHeart.setBounds(176, 228, 78, 74);
        header.add(floatingHeart);

        return header;
    }

    // -------------------------------------------------------------------------
    // Form panel - username, password, buttons, message
    // -------------------------------------------------------------------------
    private JPanel buildLoginCard() {
        JPanel card = new JPanel(null);
        card.setBackground(CARD_BG);
        card.setBounds(20, 316, HealiverseTheme.LOGIN_CARD_WIDTH, 392);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(143, 187, 155), 1),
                new CompoundBorder(
                        new LineBorder(new Color(255, 190, 218), 4),
                        new EmptyBorder(0, 0, 0, 0))));

        JLabel title = new JLabel("Welcome Back!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(INK);
        title.setBounds(30, 22, 290, 26);
        card.add(title);

        JLabel helper = new JLabel("Log in to continue your journey", SwingConstants.CENTER);
        helper.setFont(HealiverseTheme.bodyFont(11));
        helper.setForeground(MUTED);
        helper.setBounds(30, 49, 290, 20);
        card.add(helper);

        JLabel divider = new JLabel("────  ♥  ────", SwingConstants.CENTER);
        divider.setFont(new Font("SansSerif", Font.BOLD, 11));
        divider.setForeground(new Color(210, 160, 195));
        divider.setBounds(30, 73, 290, 16);
        card.add(divider);

        JLabel usernameLabel = makeFieldLabel("Username");
        usernameLabel.setBounds(30, 99, 290, 18);
        card.add(usernameLabel);

        usernameField = new PlaceholderTextField("Enter username");
        styleTextField(usernameField);
        JPanel usernameRow = buildInputRow("Heart.png", usernameField, null, "U");
        usernameRow.setBounds(30, 120, 290, 42);
        card.add(usernameRow);

        JLabel passwordLabel = makeFieldLabel("Password");
        passwordLabel.setBounds(30, 171, 290, 18);
        card.add(passwordLabel);

        passwordField = new PlaceholderPasswordField("Enter password");
        styleTextField(passwordField);
        JPanel passwordRow = buildPasswordRow(passwordField);
        passwordRow.setBounds(30, 192, 290, 42);
        card.add(passwordRow);

        JCheckBox rememberBox = new JCheckBox("Remember me");
        rememberBox.setOpaque(false);
        rememberBox.setFocusPainted(false);
        rememberBox.setFont(HealiverseTheme.bodyFont(10));
        rememberBox.setForeground(new Color(92, 73, 133));
        rememberBox.setBounds(28, 238, 130, 24);
        card.add(rememberBox);

        JLabel forgotLabel = new JLabel("Forgot?");
        forgotLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        forgotLabel.setForeground(new Color(117, 90, 188));
        forgotLabel.setBounds(252, 240, 62, 20);
        card.add(forgotLabel);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(HealiverseTheme.bodyFont(11));
        messageLabel.setForeground(new Color(200, 50, 80));
        messageLabel.setBounds(20, 263, 310, 18);
        card.add(messageLabel);

        JButton loginBtn = makeActionButton("LOGIN", HealiverseTheme.PASTEL_PINK);
        loginBtn.setBounds(30, 288, 290, 38);
        card.add(loginBtn);

        JButton signUpBtn = makeActionButton("SIGN UP", HealiverseTheme.MINT);
        signUpBtn.setBounds(30, 334, 290, 38);
        card.add(signUpBtn);

        LoginAction loginAction = new LoginAction();
        loginBtn.addActionListener(loginAction);
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        signUpBtn.addActionListener(new SignUpAction());

        return card;
    }

    // -------------------------------------------------------------------------
    // Footer label
    // -------------------------------------------------------------------------
    private JLabel buildFooterLabel() {
        JLabel footer = new JLabel("TMF2954 Java Programming · G04/SE Group 14", SwingConstants.CENTER);
        footer.setFont(HealiverseTheme.bodyFont(9));
        footer.setForeground(new Color(110, 88, 154));
        footer.setOpaque(false);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setMaximumSize(new Dimension(HealiverseTheme.LOGIN_CARD_WIDTH, 22));
        return footer;
    }

    // -------------------------------------------------------------------------
    // UI helper: labelled fields and buttons
    // -------------------------------------------------------------------------
    private JLabel makeFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HealiverseTheme.buttonFont(12));
        label.setForeground(INK);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(HealiverseTheme.bodyFont(13));
        field.setForeground(INK);
        field.setCaretColor(INK);
        field.setBorder(null);
        field.setOpaque(false);
        field.setBackground(new Color(0, 0, 0, 0));
    }

    private JButton makeActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(INK);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(143, 187, 155), 1),
                new EmptyBorder(8, 12, 8, 12)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel buildInputRow(String startIconName, JTextField field, String endIconName, String fallbackText) {
        JPanel row = new JPanel(null);
        row.setBackground(HealiverseTheme.CREAM);
        row.setBorder(HealiverseTheme.inputBorder());
        HealiverseTheme.setFixedSize(row, HealiverseTheme.LOGIN_FIELD_WIDTH, 42);

        JLabel leading = createTinyIcon(startIconName, 16, 16, fallbackText);
        leading.setBounds(10, 10, 18, 18);
        row.add(leading);

        field.setBounds(38, 8, 224, 26);
        row.add(field);

        if (endIconName != null) {
            JLabel ending = createTinyIcon(endIconName, 16, 16, "*");
            ending.setBounds(262, 10, 18, 18);
            row.add(ending);
        }

        return row;
    }

    private JPanel buildPasswordRow(JPasswordField field) {
        JPanel row = new JPanel(null);
        row.setBackground(HealiverseTheme.CREAM);
        row.setBorder(HealiverseTheme.inputBorder());
        HealiverseTheme.setFixedSize(row, HealiverseTheme.LOGIN_FIELD_WIDTH, 42);

        JLabel leading = createTinyIcon(null, 16, 16, "P");
        leading.setBounds(10, 10, 18, 18);
        row.add(leading);

        field.setBounds(38, 8, 180, 26);
        row.add(field);

        JButton toggle = new JButton("Show");
        toggle.setFont(HealiverseTheme.buttonFont(8));
        toggle.setForeground(BORDER);
        toggle.setBackground(new Color(255, 250, 255));
        toggle.setFocusPainted(false);
        toggle.setBorder(new LineBorder(HealiverseTheme.LINE, 1));
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.setBounds(229, 9, 50, 23);
        toggle.addActionListener(e -> {
            boolean showing = field.getEchoChar() == 0;
            field.setEchoChar(showing ? '•' : (char) 0);
            toggle.setText(showing ? "Show" : "Hide");
        });
        row.add(toggle);

        return row;
    }

    private JLabel createTinyIcon(String fileName, int width, int height, String fallback) {
        JLabel label = new JLabel();
        ImageIcon icon = HealiversePaths.loadPixelIcon(fileName, width, height);
        if (icon != null) {
            label.setIcon(icon);
        } else {
            label.setText(fallback);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setForeground(BORDER);
        }
        return label;
    }

    private void loadUsersFromFile() {
        File file = HealiversePaths.dataFile(USERS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2 && !parts[0].trim().isEmpty()) {
                    userDatabase.put(parts[0].trim(), parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load users: " + e.getMessage());
        }
    }

    private void saveUserToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                HealiversePaths.writableDataFile(USERS_FILE), true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Could not save user: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Login Action - preserves original logic
    // -------------------------------------------------------------------------
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password.");
                messageLabel.setForeground(new Color(200, 50, 80));
                return;
            }

            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                currentUser = username;
                messageLabel.setText("Login successful! Loading...");
                messageLabel.setForeground(new Color(32, 132, 100));

                // Open MentalHealthGameApp (main dashboard) after login
                new MentalHealthGameApp(username).setVisible(true);
                dispose();
            } else {
                messageLabel.setText("Invalid username or password. Try again.");
                messageLabel.setForeground(new Color(200, 50, 80));
                passwordField.setText("");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Sign Up Action - preserves original logic
    // -------------------------------------------------------------------------
    private class SignUpAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog(LoginApplication.this, "Create Account", true);
            dialog.setResizable(false);

            PixelSkyPanel shell = new PixelSkyPanel();
            shell.setLayout(null);
            shell.setPreferredSize(new Dimension(360, 420));
            shell.setBorder(new EmptyBorder(18, 18, 18, 18));

            JPanel form = new JPanel(null);
            form.setBackground(CARD_BG);
            form.setBounds(30, 34, 300, 340);
            form.setBorder(HealiverseTheme.cardBorder(new Color(200, 238, 206), 12, 18));

            JLabel heading = new JLabel("New Account", SwingConstants.CENTER);
            heading.setFont(HealiverseTheme.buttonFont(16));
            heading.setForeground(INK);
            heading.setBounds(30, 18, 240, 24);
            form.add(heading);

            JLabel newUserLabel = makeFieldLabel("New Username");
            newUserLabel.setBounds(30, 58, 240, 18);
            form.add(newUserLabel);

            JTextField newUsernameField = new PlaceholderTextField("Enter username");
            styleTextField(newUsernameField);
            JPanel newUserRow = buildInputRow("Heart.png", newUsernameField, null, "U");
            newUserRow.setBounds(30, 80, 240, 40);
            form.add(newUserRow);

            JLabel passLabel = makeFieldLabel("Password");
            passLabel.setBounds(30, 128, 240, 18);
            form.add(passLabel);

            JPasswordField newPasswordField = new PlaceholderPasswordField("Enter password");
            styleTextField(newPasswordField);
            JPanel newPassRow = buildInputRow("Cloud Sparkle.png", newPasswordField, null, "P");
            newPassRow.setBounds(30, 150, 240, 40);
            form.add(newPassRow);

            JLabel confirmLabel = makeFieldLabel("Confirm Password");
            confirmLabel.setBounds(30, 198, 240, 18);
            form.add(confirmLabel);

            JPasswordField confirmPasswordField = new PlaceholderPasswordField("Confirm password");
            styleTextField(confirmPasswordField);
            JPanel confirmRow = buildInputRow("Green Heart Floating.png", confirmPasswordField, null, "C");
            confirmRow.setBounds(30, 220, 240, 40);
            form.add(confirmRow);

            JLabel signUpMessage = new JLabel(" ", SwingConstants.CENTER);
            signUpMessage.setFont(HealiverseTheme.bodyFont(11));
            signUpMessage.setBounds(20, 264, 260, 22);
            form.add(signUpMessage);

            JButton registerBtn = makeActionButton("REGISTER", HealiverseTheme.MINT);
            registerBtn.setBounds(30, 290, 112, 34);
            form.add(registerBtn);

            JButton cancelBtn = makeActionButton("CANCEL", HealiverseTheme.SOFT_YELLOW);
            cancelBtn.setBounds(158, 290, 112, 34);
            form.add(cancelBtn);

            shell.add(form);
            dialog.setContentPane(shell);
            dialog.pack();
            dialog.setLocationRelativeTo(LoginApplication.this);

            // Register button logic - original validation preserved
            registerBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newUsername = newUsernameField.getText().trim();
                    String newPassword = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());

                    if (newUsername.isEmpty() || newPassword.isEmpty()) {
                        signUpMessage.setText("Username and password cannot be empty!");
                        signUpMessage.setForeground(new Color(200, 50, 80));
                        return;
                    }
                    if (userDatabase.containsKey(newUsername)) {
                        signUpMessage.setText("Username already taken. Choose another.");
                        signUpMessage.setForeground(new Color(200, 50, 80));
                        return;
                    }
                    if (!newPassword.equals(confirmPassword)) {
                        signUpMessage.setText("Passwords do not match!");
                        signUpMessage.setForeground(new Color(200, 50, 80));
                        return;
                    }
                    if (newPassword.length() < 6) {
                        signUpMessage.setText("Password must be at least 6 characters.");
                        signUpMessage.setForeground(new Color(200, 50, 80));
                        return;
                    }

                    userDatabase.put(newUsername, newPassword);
                    saveUserToFile(newUsername, newPassword);
                    signUpMessage.setText("Account created! You can now log in.");
                    signUpMessage.setForeground(new Color(32, 132, 100));

                    // Auto-close after 2 seconds
                    javax.swing.Timer closeTimer = new javax.swing.Timer(1200, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dialog.dispose();
                        }
                    });
                    closeTimer.setRepeats(false);
                    closeTimer.start();
                }
            });

            cancelBtn.addActionListener(e1 -> dialog.dispose());
            dialog.setVisible(true);
        }
    }

    // -------------------------------------------------------------------------
    // Static launcher - called by MentalHealthGameApp on logout
    // -------------------------------------------------------------------------
    /**
     * Static launcher used by MentalHealthGameApp when the user logs out.
     * Shows a brief loading message before the login window appears.
     */
    public static void launchWithLoadingScreen() {
        JWindow loading = new JWindow();

        PixelSkyPanel shell = new PixelSkyPanel();
        shell.setLayout(null);
        shell.setPreferredSize(new Dimension(HealiverseTheme.PHONE_WIDTH, HealiverseTheme.PHONE_HEIGHT));
        shell.setBorder(new LineBorder(new Color(143, 104, 204), 3));

        JLabel logo = new JLabel("HEALIVERSE", SwingConstants.CENTER);
        ImageIcon logoIcon = HealiversePaths.loadPixelIcon("Healiverse.png", 348, 148);
        if (logoIcon != null) {
            logo.setIcon(logoIcon);
        } else {
            logo.setFont(new Font("Monospaced", Font.BOLD, 38));
            logo.setForeground(new Color(82, 58, 125));
        }
        logo.setBounds(41, 90, 348, 148);
        shell.add(logo);

        JLabel sdg = new JLabel("SDG 3: Good Health & Well-Being", SwingConstants.CENTER);
        sdg.setFont(HealiverseTheme.buttonFont(13));
        sdg.setForeground(new Color(41, 110, 79));
        sdg.setOpaque(true);
        sdg.setBackground(new Color(184, 236, 198));
        sdg.setBorder(new LineBorder(new Color(83, 150, 111), 2));
        sdg.setBounds(66, 240, 298, 32);
        shell.add(sdg);

        JLabel iconLabel = new JLabel("", SwingConstants.CENTER);
        ImageIcon heartIcon = HealiversePaths.loadPixelIcon("Green Heart Floating.png", 188, 188);
        if (heartIcon != null) {
            iconLabel.setIcon(heartIcon);
        } else {
            iconLabel.setText("♥");
            iconLabel.setFont(new Font("SansSerif", Font.BOLD, 82));
            iconLabel.setForeground(new Color(88, 182, 126));
        }
        iconLabel.setBounds(111, 312, 208, 198);
        shell.add(iconLabel);

        JLabel subtitle = new JLabel("Loading your wellness journey...", SwingConstants.CENTER);
        subtitle.setFont(HealiverseTheme.buttonFont(15));
        subtitle.setForeground(new Color(100, 76, 151));
        subtitle.setBounds(48, 530, 334, 28);
        shell.add(subtitle);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setForeground(new Color(127, 214, 160));
        bar.setBackground(Color.WHITE);
        bar.setBorder(new LineBorder(new Color(143, 104, 204), 2));
        bar.setBounds(68, 590, 294, 20);
        shell.add(bar);

        JLabel percent = new JLabel("0%", SwingConstants.CENTER);
        percent.setFont(new Font("SansSerif", Font.BOLD, 14));
        percent.setForeground(new Color(82, 58, 125));
        percent.setBounds(68, 618, 294, 24);
        shell.add(percent);

        loading.setContentPane(shell);
        loading.pack();
        loading.setLocationRelativeTo(null);
        loading.setVisible(true);

        final int[] progress = {0};
        javax.swing.Timer timer = new javax.swing.Timer(35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress[0] += 4;
                if (progress[0] > 100) {
                    progress[0] = 100;
                }

                bar.setValue(progress[0]);
                percent.setText(progress[0] + "%");

                if (progress[0] >= 100) {
                    ((javax.swing.Timer) e.getSource()).stop();
                    loading.dispose();
                    new LoginApplication().setVisible(true);
                }
            }
        });
        timer.start();
    }

    // -------------------------------------------------------------------------
    // Main entry point
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system one fails
        }
        SwingUtilities.invokeLater(() -> new LoginApplication().setVisible(true));
    }

    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;

        PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setColor(new Color(135, 116, 160));
                g.setFont(getFont());
                g.drawString(placeholder, 2, 18);
                g.dispose();
            }
        }
    }

    private static class PlaceholderPasswordField extends JPasswordField {
        private final String placeholder;

        PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setEchoChar('•');
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            if (getPassword().length == 0 && !isFocusOwner()) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setColor(new Color(135, 116, 160));
                g.setFont(getFont());
                g.drawString(placeholder, 2, 18);
                g.dispose();
            }
        }
    }

    private static class PixelSkyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();

            for (int y = 0; y < getHeight(); y += 8) {
                float ratio = (float) y / Math.max(1, getHeight());
                int r = (int) (SKY_TOP.getRed() * (1 - ratio) + SKY_BOTTOM.getRed() * ratio);
                int gr = (int) (SKY_TOP.getGreen() * (1 - ratio) + SKY_BOTTOM.getGreen() * ratio);
                int b = (int) (SKY_TOP.getBlue() * (1 - ratio) + SKY_BOTTOM.getBlue() * ratio);
                g.setColor(new Color(r, gr, b));
                g.fillRect(0, y, getWidth(), 8);
            }

            g.setColor(new Color(207, 233, 214, 150));
            g.fillRect(0, getHeight() - 2, getWidth(), 2);

            g.dispose();
        }

        private void drawCloud(Graphics2D g, int x, int y, int width, int height) {
            g.fillRect(x + 8, y, width / 2, height / 2);
            g.fillRect(x, y + 6, width, height / 2);
            g.fillRect(x + 6, y + 12, width - 12, height / 2);
        }

        private void drawSparkle(Graphics2D g, int x, int y) {
            g.fillRect(x, y + 4, 2, 10);
            g.fillRect(x - 4, y + 8, 10, 2);
            g.fillRect(x + 1, y + 1, 2, 2);
            g.fillRect(x + 1, y + 15, 2, 2);
            g.fillRect(x - 7, y + 8, 2, 2);
            g.fillRect(x + 7, y + 8, 2, 2);
        }
    }
}
