import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ProfilePanel extends JPanel {
    private GridBagConstraints constraints;
    private JLabel usernameLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel bioLabel;
    private JLabel birthdayLabel;
    private JLabel friendsOnlyLabel;
    private JScrollPane friendsListScrollPane;
    private JTextArea friendsList;
    private JScrollPane blocksListScrollPane;
    private JTextArea blocksList;
    private JButton editButton;
    private Client client;

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

    private static class CircularImagePanel extends JPanel {
        private Image image;
        private int size;

        public CircularImagePanel(String imagePath, int size) {
            this.size = size;
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                Image originalImage = icon.getImage();
                int originalWidth = originalImage.getWidth(null);
                int originalHeight = originalImage.getHeight(null);

                int cropSize = Math.min(originalWidth, originalHeight);
                int x = (originalWidth - cropSize) / 2;
                int y = (originalHeight - cropSize) / 2;
                BufferedImage croppedImage = new BufferedImage(cropSize, cropSize, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = croppedImage.createGraphics();
                g2d.drawImage(originalImage, 0, 0, cropSize, cropSize, x, y, x + cropSize, y + cropSize, null);
                g2d.dispose();
                image = croppedImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setPreferredSize(new Dimension(size, size));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            // Draw white circle background
            g2.setColor(Color.WHITE);
            g2.fillOval(0, 0, size, size);

            // Create circular clip
            g2.setClip(new Ellipse2D.Float(1, 1, size - 2, size - 2));
            // Draw image if loaded
            if (image != null) {
                g2.drawImage(image, 0, 0, this);
            }

            g2.setClip(null);
            g2.setColor(new Color(200, 200, 200)); // Lighter gray for softer appearance
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(1, 1, size - 2, size - 2);
            g2.dispose();
        }
    }

    public profilePanel(String username, Client client) {
        this.client = client;
        this.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        usernameLabel = new JLabel(username);
        firstNameLabel = new JLabel();
        lastNameLabel = new JLabel();
        bioLabel = new JLabel();
        birthdayLabel = new JLabel();
        friendsOnlyLabel = new JLabel();
        friendsList = new JTextArea(5, 10);
        friendsList.setEditable(false);
        friendsListScrollPane = new JScrollPane(friendsList);
        blocksList = new JTextArea(5, 10);
        blocksList.setEditable(false);
        blocksListScrollPane = new JScrollPane(blocksList);
        editButton = new JButton("Edit");
        createComponents();
        addComponentsToPanel();
        addActionListeners();
    }

    public void refreshProfile(String username, String firstName, String lastName, String bio, String birthday,
            String profilePic, String friendsOnly) {
        usernameLabel.setText(username);
        firstNameLabel.setText(firstName);
        lastNameLabel.setText(lastName);
        bioLabel.setText(bio);
        birthdayLabel.setText(birthday);
        friendsOnlyLabel.setText(friendsOnly);
    }

    public void updateFriendsAndBlocks(String[] friends, String[] blocks) {
        friendsList.setText("");
        blocksList.setText("");
        for (String friend : friends) {
            friendsList.append(friend + "\n");
        }
        for (String block : blocks) {
            blocksList.append(block + "\n");
        }
    }

    private void createComponents() {
        Font font = new Font("Arial", Font.PLAIN, 16);
        usernameLabel = new JLabel(usernameLabel.getText());
        usernameLabel.setFont(font);
        firstNameLabel = new JLabel(firstNameLabel.getText());
        firstNameLabel.setFont(font);
        lastNameLabel = new JLabel(lastNameLabel.getText());
        lastNameLabel.setFont(font);
        bioLabel = new JLabel(bioLabel.getText());
        bioLabel.setFont(font);
        birthdayLabel = new JLabel(birthdayLabel.getText());
        birthdayLabel.setFont(font);
        friendsOnlyLabel = new JLabel(friendsOnlyLabel.getText());
        friendsOnlyLabel.setFont(font);
        friendsList.setFont(font);
        blocksList.setFont(font);
        editButton = new JButton("Edit");
    }

    private void addComponentsToPanel() {
        constraints.gridx = 1;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(editButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipady = 10;
        constraints.ipadx = 5;

        JLabel temp = new JLabel("Username:");
        Font f = new Font("Arial", Font.BOLD, 16);

        temp.setFont(f);
        this.add(temp, constraints);

        temp = new JLabel("First Name:");
        temp.setFont(f);
        constraints.gridy++;
        this.add(temp, constraints);

        temp = new JLabel("Last Name:");
        temp.setFont(f);
        constraints.gridy++;
        this.add(temp, constraints);

        temp = new JLabel("Bio:");
        temp.setFont(f);
        constraints.gridy++;
        this.add(temp, constraints);

        temp = new JLabel("Birthday:");
        temp.setFont(f);
        constraints.gridy++;
        this.add(temp, constraints);

        temp = new JLabel("Messages limited to friends:");
        temp.setFont(f);
        constraints.gridy++;
        this.add(temp, constraints);

        temp = new JLabel("Friends:");
        temp.setFont(f);
        constraints.gridy++;
        this.add(temp, constraints);

        temp = new JLabel("Blocked Users:");
        temp.setFont(f);
        constraints.gridy++;
        this.add(temp, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;

        this.add(usernameLabel, constraints);

        constraints.gridy++;
        this.add(firstNameLabel, constraints);

        constraints.gridy++;
        this.add(lastNameLabel, constraints);

        constraints.gridy++;
        this.add(bioLabel, constraints);

        constraints.gridy++;
        this.add(birthdayLabel, constraints);

        constraints.gridy++;
        this.add(friendsOnlyLabel, constraints);

        constraints.gridy++;
        this.add(friendsListScrollPane, constraints);

        constraints.gridy++;
        this.add(blocksListScrollPane, constraints);
    }

    private void editProfile() {
        // TODO: add cancel button
        // Create a new dialog to edit the profile
        JFrame frame = new JFrame("Edit Profile");
        // JDialog editDialog = new JDialog(frame, "Edit Profile", true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create fields to edit the profile
        JTextField firstNameField = new JTextField(firstNameLabel.getText(), 20);
        JTextField lastNameField = new JTextField(lastNameLabel.getText(), 20);
        JTextField bioField = new JTextField(bioLabel.getText(), 20);
        JTextField birthdayFieldMonth = new JTextField("", 20);
        JTextField birthdayFieldDay = new JTextField("", 20);
        JTextField birthdayFieldYear = new JTextField("", 20);
        JCheckBox friendsOnlyCheckBox = new JCheckBox("Messages limited to friends?",
                Boolean.parseBoolean(friendsOnlyLabel.getText()));

        // Create a panel to hold the fields
        JPanel editPanel = new JPanel();
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        editPanel.setLayout(new GridLayout(7, 2));
        // Add the fields to the panel
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
                // Save the changes
                char groupSeparator = (char) 29;
                String content = usernameLabel.getText().trim() + groupSeparator + firstNameField.getText().trim() +
                        groupSeparator + lastNameField.getText().trim() + groupSeparator + bioField.getText().trim() +
                        groupSeparator + birthdayFieldMonth.getText().trim() + "/" + birthdayFieldDay.getText().trim()
                        + "/" + birthdayFieldYear.getText().trim() + groupSeparator + "profile.png" + groupSeparator +
                        ((Boolean) friendsOnlyCheckBox.isSelected()).toString().trim();
                if (client.saveProfile(content)) {
                    firstNameLabel.setText(firstNameField.getText().trim());
                    lastNameLabel.setText(lastNameField.getText().trim());
                    bioLabel.setText(bioField.getText().trim());
                    birthdayLabel.setText(birthdayFieldMonth.getText().trim() + "/" + birthdayFieldDay.getText().trim()
                            + "/" + birthdayFieldYear.getText().trim());
                    friendsOnlyLabel.setText(((Boolean) friendsOnlyCheckBox.isSelected()).toString().trim());
                    // Close the dialog
                    frame.dispose();
                } else {
                    // Show an error message
                    JOptionPane.showMessageDialog(null, "Invalid profile information, " +
                            "try again", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel mainPanel = new JPanel(null);
        JPanel blackBackground = new JPanel();
        blackBackground.setBackground(Color.BLACK);
        blackBackground.setBounds(0, 0, (int) (screenSize.width / 2.5), screenSize.height - 98);
        CircularImagePanel imagePanel = new CircularImagePanel(
                "C:/Users/peter/Github/CS180Team-Project/images/donald-trump-gettyimages-687193180.jpg", 150);
        imagePanel.setBounds(40, 80, 100, 100);
        mainPanel.add(imagePanel);
        Font labelFont = new Font("Monospaced", Font.BOLD, 16);
        Color textColor = Color.WHITE;
        int fieldWidth = 400;
        int fieldHeight = 30;
        int startX = 50;
        int startY = 250;
        int verticalGap = 25;

        // Full Name
        JLabel fullNameLabel = new JLabel("Donald Trump");
        fullNameLabel.setFont(labelFont);
        fullNameLabel.setForeground(textColor);
        fullNameLabel.setBounds(startX, startY, fieldWidth, fieldHeight); // maybe change height to fieldHeight

        // First Name
        JLabel firstNameLabel = new JLabel("Donald");
        firstNameLabel.setFont(labelFont);
        firstNameLabel.setForeground(textColor);
        firstNameLabel.setBounds(startX, startY + verticalGap, fieldWidth, fieldHeight);

        // Last Name
        JLabel lastNameLabel = new JLabel("Trump");
        lastNameLabel.setFont(labelFont);
        lastNameLabel.setForeground(textColor);
        lastNameLabel.setBounds(startX, startY + (verticalGap * 2), fieldWidth, fieldHeight);

        // Bio
        JLabel bioLabel = new JLabel("I am the president of the United States.");
        bioLabel.setFont(labelFont);
        bioLabel.setForeground(textColor);
        bioLabel.setBounds(startX, startY + (verticalGap * 3), fieldWidth, fieldHeight);

        // Birthday
        JLabel birthdayLabel = new JLabel("June 14, 1946");
        birthdayLabel.setFont(labelFont);
        birthdayLabel.setForeground(textColor);
        birthdayLabel.setBounds(startX, startY + (verticalGap * 4), fieldWidth, fieldHeight);

        // After creating imagePanel but before other labels:
        // Stats styling
        Font statsNumberFont = new Font("Arial", Font.BOLD, 18);
        Font statsLabelFont = new Font("Arial", Font.PLAIN, 11);
        Color statsColor = Color.WHITE;

        // Calculate positions (align with profile picture)
        int statsStartX = 275;
        int statsY = 120; // Above profile picture
        int statsWidth = 80;
        int statsGap = 10;

        // Messages Stats
        JLabel messagesNumber = new JLabel("699");
        messagesNumber.setFont(statsNumberFont);
        messagesNumber.setForeground(statsColor);
        messagesNumber.setHorizontalAlignment(SwingConstants.CENTER);
        messagesNumber.setBounds(statsStartX, statsY, statsWidth, 25);

        JLabel messagesLabel = new JLabel("total messages");
        messagesLabel.setFont(statsLabelFont);
        messagesLabel.setForeground(statsColor);
        messagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messagesLabel.setBounds(statsStartX, statsY + 25, statsWidth, 20);

        // Friends Stats
        JLabel friendsNumber = new JLabel("966");
        friendsNumber.setFont(statsNumberFont);
        friendsNumber.setForeground(statsColor);
        friendsNumber.setHorizontalAlignment(SwingConstants.CENTER);
        friendsNumber.setBounds(statsStartX + statsWidth + statsGap, statsY, statsWidth, 25);

        JLabel friendsLabel = new JLabel("friends");
        friendsLabel.setFont(statsLabelFont);
        friendsLabel.setForeground(statsColor);
        friendsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        friendsLabel.setBounds(statsStartX + statsWidth + statsGap, statsY + 25, statsWidth, 20);

        // Blocked Stats
        JLabel blockedNumber = new JLabel("1M");
        blockedNumber.setFont(statsNumberFont);
        blockedNumber.setForeground(statsColor);
        blockedNumber.setHorizontalAlignment(SwingConstants.CENTER);
        blockedNumber.setBounds(statsStartX + (statsWidth + statsGap) * 2, statsY, statsWidth, 25);

        JLabel blockedLabel = new JLabel("blocked");
        blockedLabel.setFont(statsLabelFont);
        blockedLabel.setForeground(statsColor);
        blockedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        blockedLabel.setBounds(statsStartX + (statsWidth + statsGap) * 2, statsY + 25, statsWidth, 20);

        // After birthday section:
        // Button styling
        Color instagramBlue = new Color(0, 149, 246);
        int buttonWidth = 270; // Wider rectangle
        int buttonHeight = 35;
        int buttonGap = 10;
        int buttonsY = startY + (verticalGap * 5) + 18; // Below birthday section

        // Create Add Friend button
        RoundedButton addFriendButton = new RoundedButton("Add Friend", 16);
        addFriendButton.setBackground(instagramBlue);
        addFriendButton.setForeground(Color.WHITE);
        addFriendButton.setFont(new Font("Arial", Font.BOLD, 14));
        addFriendButton.setBounds(startX - 5, buttonsY, buttonWidth, buttonHeight);
        addFriendButton.setFocusPainted(false);

        // Create Block button
        RoundedButton blockButton = new RoundedButton("Block", 16);
        blockButton.setBackground(instagramBlue);
        blockButton.setForeground(Color.WHITE);
        blockButton.setFont(new Font("Arial", Font.BOLD, 14));
        blockButton.setBounds(startX + buttonWidth + buttonGap - 10, buttonsY, buttonWidth, buttonHeight);
        blockButton.setFocusPainted(false);

        // After adding the buttons:
        int gridStartY = buttonsY + buttonHeight + 20; // Below buttons with some spacing
        double gridWidth = screenSize.width / 2.5; // Half screen width
        int gridHeight = screenSize.height - gridStartY - 95; // Fill remaining height

        // Create panel for grid of pictures
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 2, 2)); // 2 rows, 3 columns, 10px gaps
        gridPanel.setBounds(0, gridStartY, (int) gridWidth, gridHeight);
        gridPanel.setBackground(Color.BLACK);

        // Paths to images
        String[] imagePaths = {
                "C:/Users/peter/Github/CS180Team-Project/images/1.jpg",
                "C:/Users/peter/Github/CS180Team-Project/images/2.jpg",
                "C:/Users/peter/Github/CS180Team-Project/images/donald-trump-gettyimages-687193180.jpg",
                "C:/Users/peter/Github/CS180Team-Project/images/4.jpg",
                "C:/Users/peter/Github/CS180Team-Project/images/5.jpg",
                "C:/Users/peter/Github/CS180Team-Project/images/6.jpg"
        };

        // Add 6 image panels to the grid
        for (String imagePath : imagePaths) {
            JPanel imagePanel2 = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    try {
                        ImageIcon icon = new ImageIcon(imagePath);
                        Image image = icon.getImage();
                        int imgWidth = image.getWidth(null);
                        int imgHeight = image.getHeight(null);
                        int panelWidth = getWidth();
                        int panelHeight = getHeight();
                        int cropSize = Math.min(imgWidth, imgHeight);
                        int x = (imgWidth - cropSize) / 2;
                        int y = (imgHeight - cropSize) / 2;
                        g2.drawImage(image, 0, 0, panelWidth, panelHeight, x, y, x + cropSize, y + cropSize, this);
                    } catch (Exception e) {
                        g2.setColor(Color.WHITE);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                    }
                    g2.dispose();
                }
            };
            imagePanel2.setBackground(Color.BLACK);
            gridPanel.add(imagePanel2);
        }

        // Add grid panel to main panel
        mainPanel.add(gridPanel);
        // Add buttons to panel
        mainPanel.add(addFriendButton);
        mainPanel.add(blockButton);
        mainPanel.add(messagesNumber);
        mainPanel.add(messagesLabel);
        mainPanel.add(friendsNumber);
        mainPanel.add(friendsLabel);
        mainPanel.add(blockedNumber);
        mainPanel.add(blockedLabel);
        mainPanel.add(fullNameLabel);
        mainPanel.add(firstNameLabel);
        mainPanel.add(lastNameLabel);
        mainPanel.add(bioLabel);
        mainPanel.add(birthdayLabel);
        mainPanel.add(blackBackground);
        mainPanel.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        // Add the panel and button to the dialog
        frame.add(mainPanel);
        // frame.add(editPanel, BorderLayout.CENTER);
        frame.add(saveButton, BorderLayout.SOUTH);

        // Set the size of the dialog
        frame.setSize(screenSize.width, screenSize.height);
        // Set frame to maximized state
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Center the dialog
        frame.setLocation(0, 0);

        // Make the dialog visible
        frame.setVisible(true);
    }

    private void addActionListeners() {
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProfile();
            }
        });
    }

}
