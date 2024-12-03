import javax.swing.*;
import javax.swing.border.AbstractBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class LoginPanel extends JComponent implements Runnable {
    private JFrame frame;
    private Container content;
    private JPanel contentPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Client client;
    private LoginPanel loginPanel;
    private JLabel logoLabel;
    private String username;
    private String password;

    public LoginPanel(Client client) {
        super();
        this.client = client;
        loginPanel = this;
    }

    public void addPlaceHolderStyle(JTextField textField) {
        Font font = textField.getFont();
        font = font.deriveFont(Font.ITALIC);
        textField.setFont(font);
        textField.setForeground(Color.GRAY);
    }

    public void removePlaceholderStyle(JTextField textField) {
        Font font = textField.getFont();
        font = font.deriveFont(Font.PLAIN);
        textField.setFont(font);
        textField.setForeground(Color.BLACK);
    }

    // Add this inner class to your LoginPanel:
    private static class RoundedTextField extends JTextField {
        private Color backgroundColor;
        private int radius;

        public RoundedTextField(int columns, int radius) {
            super(columns);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    private static class RoundedButton extends JButton {
        private Color backgroundColor;
        private Color hoverBackgroundColor;
        private Color pressedBackgroundColor;
        private int radius;
        private boolean isHovered = false;
        private boolean isPressed = false;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);

            // Add hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Get current background color
            Color background = getBackground();
            if (isPressed) {
                background = background.darker();
            } else if (isHovered) {
                background = background.brighter();
            }

            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Add this class for password field:
    private static class RoundedPasswordField extends JPasswordField {
        private Color backgroundColor;
        private int radius;

        public RoundedPasswordField(int columns, int radius) {
            super(columns);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public void run() {
        try {
            if (client.isLoggedIn()) {
                loginPanel = null;
                return;
            }
        } catch (IOException e) {}
        frame = new JFrame("Login/Register");
        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Set frame size to screen size
        frame.setSize(screenSize.width, screenSize.height);

        // Set frame to maximized state
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Ensure frame starts at top-left corner
        frame.setLocation(0, 0);
        content = frame.getContentPane();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        // contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // contentPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Load custom font
        Font customFont = null;
        try {
            customFont = Font
                    .createFont(Font.TRUETYPE_FONT,
                            new File(
                                    "C:/Users/peter/Github/CS180Team-Project/images/TheHeartOfEverythingDemo-KRdD.ttf"))
                    .deriveFont(48f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // Add text field with custom font
        logoLabel = new JLabel("CS180");
        if (customFont != null) {
            logoLabel.setFont(customFont);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new RoundedTextField(20, 15);
        passwordField = new RoundedPasswordField(20, 15);

        // After initializing the text fields and before adding focus listeners:
        usernameField.setFocusable(false);
        passwordField.setFocusable(false);

        // Add mouse listeners
        usernameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                usernameField.setFocusable(true);
                usernameField.requestFocus();
            }
        });

        passwordField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passwordField.setFocusable(true);
                passwordField.requestFocus();
            }
        });
        usernameField.setText("Username");
        passwordField.setText("Password");
        passwordField.setEchoChar((char) 0);
        addPlaceHolderStyle(usernameField);
        addPlaceHolderStyle(passwordField);
        // Add focus listeners
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (usernameField.getText().equals("Username")) {
                    usernameField.setText("");
                    usernameField.requestFocus();
                    removePlaceholderStyle(usernameField);
                }
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (usernameField.getText().isEmpty()) {
                    addPlaceHolderStyle(usernameField);
                    usernameField.setText("Username");
                }
            }

        });

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.requestFocus();
                    passwordField.setEchoChar('•');
                    removePlaceholderStyle(passwordField);
                }
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    addPlaceHolderStyle(passwordField);
                    passwordField.setText("Password");
                    passwordField.setEchoChar((char) 0);
                }
            }
        });

        loginButton = new RoundedButton("Login", 15);
        registerButton = new RoundedButton("Register", 15);
        // // Set alignment
        // usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        // passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Set sizes - both preferred and maximum

        int radius = 15; // Adjust this value to change corner roundness
        Color borderColor1 = new Color(220, 220, 220);

        // Create rounded border with padding
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(radius, borderColor1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(radius, borderColor1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Set background color
        Color bgColor = new Color(250, 250, 250);
        usernameField.setBackground(bgColor);
        passwordField.setBackground(bgColor);
        usernameField.setMaximumSize(new Dimension(400, 30));
        passwordField.setMaximumSize(new Dimension(400, 30));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        loginButton.setBackground(new Color(0, 149, 246));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(0, 149, 246), 1));
        loginButton.setFocusPainted(false);
        loginButton.setMaximumSize(new Dimension(400, 30));
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(radius, borderColor1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(new Color(0, 149, 246));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(0, 149, 246), 1));
        registerButton.setFocusPainted(false);
        registerButton.setMaximumSize(new Dimension(300, 30));
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(radius, borderColor1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Add "Forgot password?" link
        JLabel forgotPasswordLabel = new JLabel(
                "<html><a style='color: #03a5fc; font-style: italic; text-decoration: none;'>Forgot password?</a></html>");
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(actionListener);
        registerButton.addActionListener(actionListener);

        contentPanel.add(logoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(usernameField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(passwordField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(loginButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(registerButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(forgotPasswordLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        content.add(Box.createGlue(), gbc); // Add glue to push the contentPanel to the center

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        content.add(contentPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        content.add(Box.createGlue(), gbc); // Add glue to push the contentPanel to the center

        frame.setSize(650, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                try {
                    if (client.login(usernameField.getText() + (char) 29 + String.valueOf(passwordField.getPassword()))) {
                        // Successful login
                        username = usernameField.getText();
                        password = String.valueOf(passwordField.getPassword());
                        frame.setVisible(false);
                        frame.dispose();
                        loginPanel = null;
                    } else {
                        // Unsuccessful login
                        passwordField.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid username or password", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    frame.dispose();
                    loginPanel = null;
                }
            } else if (e.getSource() == registerButton) {
                /*if (client.register(usernameField.getText() +
                        (char) 29 + String.valueOf(passwordField.getPassword()))) {
                    // Successful registry (and login?)
                    username = usernameField.getText();
                    password = String.valueOf(passwordField.getPassword());
                    frame.setVisible(false);*/

                // Create a new dialog to edit the profile
                JDialog editDialog = new JDialog(frame, "New Profile", true);
                editDialog.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                    }

                    @Override
                    public void windowClosing(WindowEvent e) {
                        client = null;
                        editDialog.dispose();
                        frame.setVisible(false);
                        frame.dispose();
                        loginPanel = null;
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                    }

                    @Override
                    public void windowIconified(WindowEvent e) {
                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {
                    }

                    @Override
                    public void windowActivated(WindowEvent e) {
                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {
                    }
                });

                // Create fields to edit the profile
                JTextField usernameField = new JTextField("", 20);
                JPasswordField passwordField = new JPasswordField("", 20);
                passwordField.setEchoChar('•');
                JTextField firstNameField = new JTextField("", 20);
                JTextField lastNameField = new JTextField("", 20);
                JTextField bioField = new JTextField("", 20);
                JTextField birthdayFieldMonth = new JTextField("", 20);
                JTextField birthdayFieldDay = new JTextField("", 20);
                JTextField birthdayFieldYear = new JTextField("", 20);
                JCheckBox friendsOnlyCheckBox = new JCheckBox("Messages limited to friends?",
                        false);

                // Create a panel to hold the fields
                JPanel editPanel = new JPanel();
                editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                editPanel.setLayout(new GridLayout(9, 2));

                // Add the fields to the panel
                editPanel.add(new JLabel("Username:"));
                editPanel.add(usernameField);
                editPanel.add(new JLabel("Password:"));
                editPanel.add(passwordField);
                editPanel.add(new JLabel("First Name:"));
                editPanel.add(firstNameField);
                editPanel.add(new JLabel("Last Name:"));
                editPanel.add(lastNameField);
                editPanel.add(new JLabel("Bio:"));
                editPanel.add(bioField);
                editPanel.add(new JLabel("Birth Month (MM):"));
                editPanel.add(birthdayFieldMonth);
                editPanel.add(new JLabel("Birth Day (DD):"));
                editPanel.add(birthdayFieldDay);
                editPanel.add(new JLabel("Birth Year (YYYY):"));
                editPanel.add(birthdayFieldYear);
                editPanel.add(new JLabel("Friends Only:"));
                editPanel.add(friendsOnlyCheckBox);

                // Create a button to save the changes
                JButton saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (client.register(usernameField.getText() + (char) 29 +
                                    String.valueOf(passwordField.getPassword()))) {
                                // Save the changes
                                char groupSeparator = (char) 29;
                                String content = usernameField.getText().trim() + groupSeparator + firstNameField.getText().trim() +
                                        groupSeparator + lastNameField.getText().trim() + groupSeparator + bioField.getText().trim() +
                                        groupSeparator + birthdayFieldMonth.getText().trim() + "/" + birthdayFieldDay.getText().trim()
                                        + "/" + birthdayFieldYear.getText().trim() + groupSeparator + "profile.png" + groupSeparator +
                                        ((Boolean) friendsOnlyCheckBox.isSelected()).toString().trim();
                                if (client.saveProfile(content)) {
                                    editDialog.dispose();
                                } else {
                                    // Show an error message
                                    JOptionPane.showMessageDialog(null, "Invalid profile information, " +
                                            "try again", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                // Unsuccessful registry
                                passwordField.setText("");
                                usernameField.setText("");
                                JOptionPane.showMessageDialog(null, "Invalid, unavailable, or insecure username or password",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            frame.dispose();
                            loginPanel = null;
                        }
                    }
                });

                // Add the panel and button to the dialog
                editDialog.add(editPanel, BorderLayout.CENTER);
                editDialog.add(saveButton, BorderLayout.SOUTH);

                // Set the size of the dialog
                editDialog.setSize(500, 600);

                // Center the dialog
                editDialog.setLocationRelativeTo(frame);

                // Make the dialog visible
                editDialog.setVisible(true);

                frame.dispose();
                loginPanel = null;
            }
        }
    };

    public Client getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isDone() {
        return loginPanel == null;
    }

    public boolean isConnected() {
        try {
            client.login("");
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}