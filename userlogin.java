import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class LoginApplication extends JFrame {
    
    // Store user credentials (username -> password)
    private HashMap<String, String> userDatabase;
    
    // GUI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private JLabel messageLabel;
    
    public LoginApplication() {
        // Initialize user database
        userDatabase = new HashMap<>();
        // Add default admin account
        userDatabase.put("admin", "password123");
        
        // Setup GUI
        setTitle("Login Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Label
        JLabel titleLabel = new JLabel("Welcome to the Application", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");
        
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Message Label
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        mainPanel.add(messageLabel, BorderLayout.NORTH);
        
        add(mainPanel);
        
        // Add Action Listeners
        loginButton.addActionListener(new LoginAction());
        signUpButton.addActionListener(new SignUpAction());
    }
    
    // Login Action Handler
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password!");
                messageLabel.setForeground(Color.RED);
                return;
            }
            
            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                messageLabel.setText("Login Successful! Redirecting to dashboard...");
                messageLabel.setForeground(Color.GREEN);
                
                // Open Dashboard
                new Dashboard(username).setVisible(true);
                dispose(); // Close login window
            } else {
                messageLabel.setText("Login Failed! Invalid username or password.");
                messageLabel.setForeground(Color.RED);
                passwordField.setText("");
            }
        }
    }
    
    // Sign Up Action Handler
    private class SignUpAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create Sign Up Dialog
            JDialog signUpDialog = new JDialog(LoginApplication.this, "Sign Up", true);
            signUpDialog.setSize(350, 250);
            signUpDialog.setLocationRelativeTo(LoginApplication.this);
            signUpDialog.setLayout(new BorderLayout(10, 10));
            
            JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel newUsernameLabel = new JLabel("New Username:");
            JTextField newUsernameField = new JTextField();
            
            JLabel newPasswordLabel = new JLabel("New Password:");
            JPasswordField newPasswordField = new JPasswordField();
            
            JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
            JPasswordField confirmPasswordField = new JPasswordField();
            
            formPanel.add(newUsernameLabel);
            formPanel.add(newUsernameField);
            formPanel.add(newPasswordLabel);
            formPanel.add(newPasswordField);
            formPanel.add(confirmPasswordLabel);
            formPanel.add(confirmPasswordField);
            
            // Button Panel
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton registerButton = new JButton("Register");
            JButton cancelButton = new JButton("Cancel");
            
            buttonPanel.add(registerButton);
            buttonPanel.add(cancelButton);
            
            // Message Label
            JLabel signUpMessage = new JLabel(" ", SwingConstants.CENTER);
            
            signUpDialog.add(formPanel, BorderLayout.CENTER);
            signUpDialog.add(buttonPanel, BorderLayout.SOUTH);
            signUpDialog.add(signUpMessage, BorderLayout.NORTH);
            
            // Register Action
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newUsername = newUsernameField.getText();
                    String newPassword = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());
                    
                    if (newUsername.isEmpty() || newPassword.isEmpty()) {
                        signUpMessage.setText("Username and password cannot be empty!");
                        signUpMessage.setForeground(Color.RED);
                        return;
                    }
                    
                    if (userDatabase.containsKey(newUsername)) {
                        signUpMessage.setText("Username already exists! Please choose another.");
                        signUpMessage.setForeground(Color.RED);
                        return;
                    }
                    
                    if (!newPassword.equals(confirmPassword)) {
                        signUpMessage.setText("Passwords do not match!");
                        signUpMessage.setForeground(Color.RED);
                        return;
                    }
                    
                    if (newPassword.length() < 6) {
                        signUpMessage.setText("Password must be at least 6 characters!");
                        signUpMessage.setForeground(Color.RED);
                        return;
                    }
                    
                    // Register new user
                    userDatabase.put(newUsername, newPassword);
                    signUpMessage.setText("Registration Successful! You can now login.");
                    signUpMessage.setForeground(Color.GREEN);
                    
                    // Clear fields after 2 seconds and close dialog
                    new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            signUpDialog.dispose();
                        }
                    }).start();
                }
            });
            
            cancelButton.addActionListener(e1 -> signUpDialog.dispose());
            
            signUpDialog.setVisible(true);
        }
    }
    
    // Dashboard Class
    private class Dashboard extends JFrame {
        public Dashboard(String username) {
            setTitle("Dashboard");
            setSize(500, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            
            // Welcome Message
            JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
            
            JLabel infoLabel = new JLabel("You have successfully logged in.", SwingConstants.CENTER);
            infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            infoLabel.setForeground(Color.GRAY);
            
            // Logout Button
            JButton logoutButton = new JButton("Logout");
            logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
            logoutButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginApplication().setVisible(true);
                }
            });
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(logoutButton);
            
            panel.add(welcomeLabel, BorderLayout.NORTH);
            panel.add(infoLabel, BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);
            
            add(panel);
        }
    }
    
    // Main Method
    public static void main(String[] args) {
        // Set Look and Feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch application
        SwingUtilities.invokeLater(() -> {
            new LoginApplication().setVisible(true);
        });
    }
}