import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;

public class ProfilePanel extends JPanel {
    private GridBagConstraints constraints;
    private JLabel usernameLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel bioLabel;
    private JLabel birthdayMonthLabel;
    private JLabel birthdayDayLabel;
    private JLabel birthdayYearLabel;
    private JLabel friendsOnlyLabel;
    private JLabel profilePic  = new JLabel("C:/Users/peter/Github/CS180Team-Project/images/default-image.jpg");
    private RoundedButton editButton;
    private RoundedJList<String> friendsList;
    private RoundedScrollPane friendsListScrollPane;
    private RoundedJList<String> blocksList;
    private RoundedScrollPane blocksListScrollPane;
    private Client client;

    public String getProfilePic() {
        return profilePic.getText();
    }

    private static class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
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

    private static class CircularImagePanel extends JPanel {
        private Image image;
        private int size;
        private int a = getX();
        private int b = getY();

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

        public void updateImage(String imagePath, int size) {
            this.size = size;
            try {
                // Load and process new image
                ImageIcon icon = new ImageIcon(imagePath);
                Image originalImage = icon.getImage();
                int originalWidth = originalImage.getWidth(null);
                int originalHeight = originalImage.getHeight(null);

                // Crop to square
                int cropSize = Math.min(originalWidth, originalHeight);
                int x = (originalWidth - cropSize) / 2;
                int y = (originalHeight - cropSize) / 2;

                // Create new cropped image
                BufferedImage croppedImage = new BufferedImage(cropSize, cropSize, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = croppedImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.drawImage(originalImage, 0, 0, cropSize, cropSize, x, y, x + cropSize, y + cropSize, null);
                g2d.dispose();

                // Scale and set new image
                this.image = croppedImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);

                // Update panel size

                // Update panel bounds
                setPreferredSize(new Dimension(size, size));

                // Force complete repaint
                revalidate();
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public ProfilePanel(String username, Client client) {
        this.client = client;
        this.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        usernameLabel = new JLabel(username);
        firstNameLabel = new JLabel();
        lastNameLabel = new JLabel();
        bioLabel = new JLabel();
        birthdayMonthLabel = new JLabel();
        birthdayDayLabel = new JLabel();
        birthdayYearLabel = new JLabel();
        friendsOnlyLabel = new JLabel();

        JLabel friendsLabel = new JLabel("Friends:");
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        friendsList = new RoundedJList<>(15);
        friendsListScrollPane = new RoundedScrollPane(friendsList,15);
        friendsListScrollPane.setPreferredSize(new Dimension(100, 100));

        JLabel blocksLabel = new JLabel("Blocks:");
        blocksLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        blocksList = new RoundedJList<>(15);
        blocksListScrollPane = new RoundedScrollPane(blocksList,15);
        blocksListScrollPane.setPreferredSize(new Dimension(100, 100));
        editButton = new RoundedButton("Edit",15);

        createComponents();

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);
        this.add(editButton, constraints);

        this.add(friendsLabel, constraints);

        constraints.weighty = .9;

        this.add(friendsList, constraints);

        constraints.weighty = .1;

        this.add(blocksLabel, constraints);

        constraints.weighty = .9;

        this.add(blocksListScrollPane, constraints);

        addActionListeners();
    }

    public void refreshProfile(String username, String firstName, String lastName, String bio, String month, String day,
            String year,
            String profilePic, String friendsOnly) {
        usernameLabel.setText(username);
        firstNameLabel.setText(firstName);
        lastNameLabel.setText(lastName);
        bioLabel.setText(bio);
        birthdayMonthLabel.setText(month);
        birthdayDayLabel.setText(day);
        birthdayYearLabel.setText(year);
        friendsOnlyLabel.setText(friendsOnly);
    }

    public void updateFriendsAndBlocks(String[] friends, String[] blocks) {
        friendsList.setListData(friends);
        blocksList.setListData(blocks);
    }

    class RoundedTextField extends JTextField {
        private int radius;

        public RoundedTextField(String text, int columns, int radius) {
            super(text, columns);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
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
        birthdayMonthLabel = new JLabel(birthdayMonthLabel.getText());
        birthdayMonthLabel.setFont(font);
        birthdayDayLabel = new JLabel(birthdayDayLabel.getText());
        birthdayDayLabel.setFont(font);
        birthdayYearLabel = new JLabel(birthdayYearLabel.getText());
        birthdayYearLabel.setFont(font);
        friendsOnlyLabel = new JLabel(friendsOnlyLabel.getText());
        friendsOnlyLabel.setFont(font);
        friendsList.setFont(font);
        blocksList.setFont(font);
        editButton = new RoundedButton("Edit",15);
        editButton.setBackground(new Color(0, 149, 246));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBorder(BorderFactory.createLineBorder(new Color(0, 149, 246), 1));
        editButton.setFocusPainted(false);
        editButton.setPreferredSize(new Dimension(100, 20));
        editButton.setMaximumSize(new Dimension(400, 50));
    }

    private void editProfile() {
        try {
            // TODO: add cancel button
            // Create a new dialog to edit the profile
            JFrame frame = new JFrame("Edit Profile");
            // JDialog editDialog = new JDialog(frame, "Edit Profile", true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            JPanel mainPanel = new JPanel(null);
            JPanel blackBackground = new RoundedPanel(30);
            blackBackground.setBackground(Color.BLACK);
            blackBackground.setBounds(0, 0, (int) (screenSize.width / 2.5), screenSize.height - 98);
            char groupSeparator = (char) 29;
            String profileInput = client.accessProfile();
            String[] profileInfo = profileInput.split(groupSeparator + "");
            profilePic = new JLabel(profileInfo[5].substring(profileInfo[5].indexOf(":") + 2));
            JLabel pic = new JLabel(profilePic.getText());
            if (pic.getText().equals("profile.png")) {
                pic.setText("C:/Users/peter/Github/CS180Team-Project/images/4.jpg");
            }
            CircularImagePanel imagePanel = new CircularImagePanel(
                    pic.getText(), 150);
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
            JLabel fullNameStr = new JLabel(profileInfo[1].substring(profileInfo[1].indexOf(":") + 1).trim() + " "
                    + profileInfo[2].substring(profileInfo[2].indexOf(":") + 1).trim());
            JLabel firstNameStr = new JLabel(profileInfo[1].substring(profileInfo[1].indexOf(":") + 1).trim());
            JLabel lastNameStr = new JLabel(profileInfo[2].substring(profileInfo[2].indexOf(":") + 1).trim());
            JLabel bioStr = new JLabel(profileInfo[3].substring(profileInfo[3].indexOf(":") + 1).trim());
            String birthday = profileInfo[4].substring(profileInfo[4].indexOf(":") + 1).trim();
            String[] birthdayParts = birthday.split("/");
            JLabel birthdayMonthStr = new JLabel(birthdayParts[0]);
            JLabel birthdayDayStr = new JLabel(birthdayParts[1]);
            JLabel birthdayYearStr = new JLabel(birthdayParts[2]);
            JLabel friendsOnlyStr = new JLabel(profileInfo[6].substring(profileInfo[6].indexOf(":") + 1));
            // Full Name
            JLabel fullNameLabel = new JLabel(fullNameStr.getText());
            fullNameLabel.setFont(labelFont);
            fullNameLabel.setForeground(textColor);
            fullNameLabel.setBounds(startX, startY, fieldWidth, fieldHeight); // maybe change height to fieldHeight

            // First Name
            JLabel firstNameLabel = new JLabel(firstNameStr.getText());
            firstNameLabel.setFont(labelFont);
            firstNameLabel.setForeground(textColor);
            firstNameLabel.setBounds(startX, startY + verticalGap, fieldWidth, fieldHeight);

            // Last Name
            JLabel lastNameLabel = new JLabel(lastNameStr.getText());
            lastNameLabel.setFont(labelFont);
            lastNameLabel.setForeground(textColor);
            lastNameLabel.setBounds(startX, startY + (verticalGap * 2), fieldWidth, fieldHeight);

            // Bio
            JLabel bioLabel = new JLabel(bioStr.getText());
            bioLabel.setFont(labelFont);
            bioLabel.setForeground(textColor);
            bioLabel.setBounds(startX, startY + (verticalGap * 3), fieldWidth, fieldHeight);

            // Birthday Month
            JLabel birthdayMonthLabel = new JLabel(birthdayMonthStr.getText());
            birthdayMonthLabel.setFont(labelFont);
            birthdayMonthLabel.setForeground(textColor);
            birthdayMonthLabel.setBounds(startX, startY + (verticalGap * 4), fieldWidth, fieldHeight);

            // Birthday Day
            JLabel birthdayDayLabel = new JLabel(birthdayDayStr.getText());
            birthdayDayLabel.setFont(labelFont);
            birthdayDayLabel.setForeground(textColor);
            birthdayDayLabel.setBounds(startX + 30, startY + (verticalGap * 4), fieldWidth, fieldHeight);

            // Birthday Year
            JLabel birthdayYearLabel = new JLabel(birthdayYearStr.getText());
            birthdayYearLabel.setFont(labelFont);
            birthdayYearLabel.setForeground(textColor);
            birthdayYearLabel.setBounds(startX + 60, startY + (verticalGap * 4), fieldWidth, fieldHeight);

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

            String photosInfo = client.accessPhotosFromUser();
            String[] photos = photosInfo.split(",");
            String[] imagePaths = new String[6];
            for (int i = 0; i < imagePaths.length; i++) {
                imagePaths[i] = "C:/Users/peter/Github/CS180Team-Project/images/default-image.jpg";
            }
            if (photos.length < 6) {
                for (int i = 0; i < photos.length; i++) {
                    char gs = 29;
                    String[] lmao = photos[i].trim().replace("[","").replace("]","").split(gs + "");
                    imagePaths[i] = lmao[1];
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    char gs = 29;
                    String[] lmao = photos[i].trim().replace("[","").replace("]","").split(gs + "");
                    imagePaths[i] = lmao[1];
                }
            }

            // Add 6 image panels to the grid
            for (String imagePath : imagePaths) {
                JPanel imagePanel2 = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
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
            // Create right panel for editing fields
            JPanel rightPanel = new JPanel(new GridBagLayout());
            rightPanel.setBackground(Color.WHITE);
            rightPanel.setBounds((int) (screenSize.width / 2.5), 0, screenSize.width - (int) (screenSize.width / 2.5),
                    screenSize.height);
            mainPanel.add(rightPanel);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;

            String currentFullName = fullNameStr.getText();
            String currentFirstName = firstNameStr.getText();
            String currentLastName = lastNameStr.getText();
            String currentBio = bioStr.getText();
            String currentBirthdayMonth = birthdayMonthStr.getText();
            String currentBirthdayDay = birthdayDayStr.getText();
            String currentBirthdayYear = birthdayYearStr.getText();
            String currentFriendsOnly = friendsOnlyStr.getText();
            // Full Name
            JLabel fullName = new JLabel("Full Name:");
            fullName.setPreferredSize(new Dimension(200, 30));
            rightPanel.add(fullName, gbc);
            gbc.gridx = 1;
            RoundedTextField fullNameField = new RoundedTextField(currentFullName, 20, 15);
            rightPanel.add(fullNameField, gbc);

            // First Name
            gbc.gridx = 0; // Reset to first column
            gbc.gridy++;
            JLabel firstName = new JLabel("First Name:");
            firstName.setPreferredSize(new Dimension(200, 30));
            rightPanel.add(firstName, gbc);
            gbc.gridx = 1;
            RoundedTextField firstNameField = new RoundedTextField(currentFirstName, 20, 15); // Set current value and
                                                                                              // rounded corners
            rightPanel.add(firstNameField, gbc);

            // Last Name
            gbc.gridx = 0;
            gbc.gridy++;
            JLabel lastName = new JLabel("Last Name:");
            lastName.setPreferredSize(new Dimension(200, 30)); // Increased width
            rightPanel.add(lastName, gbc);
            gbc.gridx = 1;
            RoundedTextField lastNameField = new RoundedTextField(currentLastName, 20, 15);
            rightPanel.add(lastNameField, gbc);

            // Bio
            gbc.gridx = 0;
            gbc.gridy++;
            JLabel bio = new JLabel("Bio:");
            bio.setPreferredSize(new Dimension(200, 30)); // Increased width
            rightPanel.add(bio, gbc);
            gbc.gridx = 1;
            RoundedTextField bioField = new RoundedTextField(currentBio, 20, 15);
            rightPanel.add(bioField, gbc);

            // Birthday
            gbc.gridx = 0;
            gbc.gridy++;
            JLabel birthdayMonth = new JLabel("Birth Month:");
            birthdayMonth.setPreferredSize(new Dimension(200, 30)); // Increased width
            rightPanel.add(birthdayMonth, gbc);
            gbc.gridx = 1;
            RoundedTextField birthdayFieldMonth = new RoundedTextField(currentBirthdayMonth, 20, 15);
            rightPanel.add(birthdayFieldMonth, gbc);

            // Birthday
            gbc.gridx = 0;
            gbc.gridy++;
            JLabel birthdayDay = new JLabel("Birth Day:");
            birthdayDay.setPreferredSize(new Dimension(200, 30)); // Increased width
            rightPanel.add(birthdayDay, gbc);
            gbc.gridx = 1;
            RoundedTextField birthdayFieldDay = new RoundedTextField(currentBirthdayDay, 20, 15);
            rightPanel.add(birthdayFieldDay, gbc);

            // Birthday
            gbc.gridx = 0;
            gbc.gridy++;
            JLabel birthdayYear = new JLabel("Birth Year:");
            birthdayYear.setPreferredSize(new Dimension(200, 30)); // Increased width
            rightPanel.add(birthdayYear, gbc);
            gbc.gridx = 1;
            RoundedTextField birthdayFieldYear = new RoundedTextField(currentBirthdayYear, 20, 15);
            rightPanel.add(birthdayFieldYear, gbc);

            // Profile Picture
            JButton browseButton = new JButton("Browse Files");
            browseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter(
                            "Image files", "jpg", "jpeg", "png", "gif"));

                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        pic.setText(selectedFile.getPath());
                        // Update existing panel instead of creating new one
                        imagePanel.updateImage(selectedFile.getPath(), 150);

                        // Ensure proper bounds and visibility
                        imagePanel.setBounds(40, 80, 150, 150);

                        // Force container to refresh
                        // mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                }
            });

            gbc.gridx = 0;
            gbc.gridy++;
            JLabel profilePicture = new JLabel("Profile Picture: ");
            profilePicture.setPreferredSize(new Dimension(200, 30));
            rightPanel.add(profilePicture, gbc);
            gbc.gridx = 1;
            rightPanel.add(browseButton, gbc);

            // Friends Only
            gbc.gridx = 0;
            gbc.gridy++;
            JLabel friendsOnly = new JLabel("Messages limited to friends:");
            friendsOnly.setPreferredSize(new Dimension(200, 30)); // Increased width
            rightPanel.add(friendsOnly, gbc);
            gbc.gridx = 1;
            JCheckBox friendsOnlyCheckBox = new JCheckBox();
            if (currentFriendsOnly.equals("true")) {
                friendsOnlyCheckBox.setSelected(true);
            } else {
                friendsOnlyCheckBox.setSelected(false);
            }
            rightPanel.add(friendsOnlyCheckBox, gbc);

            // Add listeners to update profile on the left in real-time

            // Add listeners to update profile on the left in real-time
            fullNameField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateProfile();
                }

                public void updateProfile() {
                    fullNameLabel.setText(fullNameField.getText());
                }
            });

            firstNameField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateProfile();
                }

                public void updateProfile() {
                    firstNameLabel.setText(firstNameField.getText());
                }
            });

            lastNameField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateProfile();
                }

                public void updateProfile() {
                    lastNameLabel.setText(lastNameField.getText());
                }
            });

            bioField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateProfile();
                }

                public void updateProfile() {
                    bioLabel.setText(bioField.getText());
                }
            });

            birthdayFieldMonth.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateProfile();
                }

                public void updateProfile() {
                    birthdayMonthLabel.setText(birthdayFieldMonth.getText());
                }
            });

            birthdayFieldDay.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateProfile();
                }

                public void updateProfile() {
                    birthdayDayLabel.setText(birthdayFieldDay.getText());
                }
            });

            birthdayFieldYear.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateProfile();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateProfile();
                }

                public void updateProfile() {
                    birthdayYearLabel.setText(birthdayFieldYear.getText());
                }
            });
            firstNameField.setPreferredSize(new Dimension(400, 50)); // Increased size
            lastNameField.setPreferredSize(new Dimension(400, 50)); // Increased size
            bioField.setPreferredSize(new Dimension(400, 50)); // Increased size
            birthdayFieldMonth.setPreferredSize(new Dimension(400, 50)); // Increased size
            birthdayFieldDay.setPreferredSize(new Dimension(400, 50)); // Increased size
            birthdayFieldYear.setPreferredSize(new Dimension(400, 50)); // Increased size

            friendsOnlyCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    friendsOnlyLabel.setText(friendsOnlyCheckBox.isSelected() ? "Yes" : "No");
                }
            });

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
            mainPanel.add(birthdayMonthLabel);
            mainPanel.add(birthdayDayLabel);
            mainPanel.add(birthdayYearLabel);
            mainPanel.add(blackBackground);
            mainPanel.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
            // Add the panel and button to the dialog
            frame.add(mainPanel);

            // Create a button to save the changes
            RoundedButton saveButton = new RoundedButton("Save", 16);
            saveButton.setBackground(instagramBlue);
            saveButton.setForeground(Color.WHITE);
            saveButton.setFont(new Font("Arial", Font.BOLD, 14));
            saveButton.setBounds(40, buttonsY + buttonHeight + 20, buttonWidth, buttonHeight); // Position below the //
                                                                                               // other buttons
            saveButton.setFocusPainted(false);
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Save the changes
                    char groupSeparator = (char) 29;
                    String content = usernameLabel.getText().trim() + groupSeparator +
                            firstNameField.getText().trim() +
                            groupSeparator + lastNameField.getText().trim() + groupSeparator +
                            bioField.getText().trim() +
                            groupSeparator + birthdayFieldMonth.getText().trim() + "/" +
                            birthdayFieldDay.getText().trim()
                            + "/" + birthdayFieldYear.getText().trim() + groupSeparator + pic.getText() +
                            groupSeparator +
                            ((Boolean) friendsOnlyCheckBox.isSelected()).toString().trim();
                    try {
                        if (client.saveProfile(content)) {
                            firstNameLabel.setText(firstNameField.getText().trim());
                            lastNameLabel.setText(lastNameField.getText().trim());
                            bioLabel.setText(bioField.getText().trim());
                            birthdayMonthLabel.setText(birthdayFieldMonth.getText().trim());
                            birthdayDayLabel.setText(birthdayFieldDay.getText().trim());
                            birthdayYearLabel.setText(birthdayFieldYear.getText().trim());
                            pic.setText(profilePic.getText());
                            friendsOnlyLabel.setText(((Boolean) friendsOnlyCheckBox.isSelected()).toString().trim());
                            // Close the dialog
                            frame.dispose();
                        } else {
                            // Show an error message
                            JOptionPane.showMessageDialog(null, "Invalid profile information, " +
                                    "try again", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActionListeners() {
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProfile();
            }
        });
    }
    private static class RoundedScrollPane extends JScrollPane {
    private int radius;
    private Color backgroundColor;

    public RoundedScrollPane(Component view, int radius) {
        super(view);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        
        // Style the scrollbars
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200);
                this.trackColor = new Color(245, 245, 245);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Set rendering hints
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Fill background
        if (backgroundColor != null) {
            g2.setColor(backgroundColor);
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 200, 200));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}
private static class RoundedJList<E> extends JList<E> {
    private int radius;
    private Color backgroundColor;

    public RoundedJList(int radius) {
        super();
        this.radius = radius;
        init();
    }

    public RoundedJList(ListModel<E> model, int radius) {
        super(model);
        this.radius = radius;
        init();
    }

    private void init() {
        setOpaque(false);
        setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, 
                        index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    setBackground(new Color(0, 149, 246, 50));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Fill background
        if (backgroundColor != null) {
            g2.setColor(backgroundColor);
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 200, 200));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() -1, radius, radius);
        g2.dispose();
    }
}

// Usage:
// friendsList = new RoundedJList<>(15);
// friendsList.setBackground(Color.WHITE);

}
