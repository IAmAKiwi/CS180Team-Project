import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ProfilePanel extends JPanel {
    private GridBagConstraints constraints;
    private JLabel fullNameLabel;
    private JLabel usernameLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel bioLabel;
    private JLabel birthdayMonthLabel;
    private JLabel birthdayDayLabel;
    private JLabel birthdayYearLabel;
    private JLabel friendsOnlyLabel;
    private JLabel profilePic;
    private RoundedButton addFriendButton;
    private RoundedButton blockButton;
    private Client client;

    public String getProfilePic() {
        return profilePic.getText().replaceAll("\\s+", "");
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
                byte[] imageBytes = Base64.getDecoder().decode(imagePath);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                BufferedImage buffered = ImageIO.read(byteArrayInputStream);
                ImageIcon icon = new ImageIcon(buffered);
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
                byte[] imageBytes = Base64.getDecoder().decode(imagePath.trim());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

                // Convert the byte array into a BufferedImage
                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);


                // Load and process new image
                ImageIcon icon = new ImageIcon(bufferedImage);
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
        try {
            this.client = client;
            this.setLayout(null);
            this.usernameLabel = new JLabel(username);
            char groupSeparator = (char) 29;
            String profileInput = this.client.accessProfile();
            String[] profileInfo = profileInput.split(groupSeparator + "");
            // String profilepic = profileInfo[5].substring(profileInfo[5].indexOf(":") +
            // 2);

            profilePic = new JLabel(profileInfo[5].substring(profileInfo[5].indexOf(":") + 2));
            // JLabel pic = new JLabel(profilePic.getText());
            // if (pic.getText().equals("profile.png") || pic.getText().isEmpty() ||
            // pic.getText().equals("")) {
            // pic.setText("0.jpg");
            // }
            firstNameLabel = new JLabel(profileInfo[1].substring(profileInfo[1].indexOf(":") + 1).trim());
            lastNameLabel = new JLabel(profileInfo[2].substring(profileInfo[2].indexOf(":") + 1).trim());
            bioLabel = new JLabel(profileInfo[3].substring(profileInfo[3].indexOf(":") + 1).trim());
            String birthday = profileInfo[4].substring(profileInfo[4].indexOf(":") + 1).trim();
            String[] birthdayParts = birthday.split("/");
            birthdayMonthLabel = new JLabel(birthdayParts[0]);
            birthdayDayLabel = new JLabel(birthdayParts[1]);
            birthdayYearLabel = new JLabel(birthdayParts[2]);
            friendsOnlyLabel = new JLabel(profileInfo[6].substring(profileInfo[6].indexOf(":") + 1));
            // Get screen dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            // Create and setup black background
            JPanel blackBackground = new RoundedPanel(30);
            blackBackground.setLayout(null);
            blackBackground.setBackground(Color.BLACK);
            int blackPanelWidth = (int) (screenSize.width * 0.6); // 60% width
            blackBackground.setBounds(0, 10, blackPanelWidth, screenSize.height - 180);
            CircularImagePanel imagePanel = new CircularImagePanel(
                    profilePic.getText().replaceAll("\\s+", ""), 150);
            imagePanel.setBounds(40, 80, 100, 100);
            this.add(imagePanel);
            Font labelFont = new Font("Monospaced", Font.BOLD, 16);
            Color textColor = Color.WHITE;
            int fieldWidth = 400;
            int fieldHeight = 30;
            int startX = 50;
            int startY = 250;
            int verticalGap = 25;

            // Full Name
            JLabel fullNameLabel = new JLabel(profileInfo[1].substring(profileInfo[1].indexOf(":") + 1).trim() + " "
                    + profileInfo[2].substring(profileInfo[2].indexOf(":") + 1).trim());
            fullNameLabel.setFont(labelFont);
            fullNameLabel.setForeground(textColor);
            fullNameLabel.setBounds(startX, startY, fieldWidth, fieldHeight); // maybe change height to fieldHeight

            // First Name
            firstNameLabel.setFont(labelFont);
            firstNameLabel.setForeground(textColor);
            firstNameLabel.setBounds(startX, startY + verticalGap, fieldWidth, fieldHeight);

            // Last Name
            lastNameLabel.setFont(labelFont);
            lastNameLabel.setForeground(textColor);
            lastNameLabel.setBounds(startX, startY + (verticalGap * 2), fieldWidth, fieldHeight);

            // Bio
            bioLabel.setFont(labelFont);
            bioLabel.setForeground(textColor);
            bioLabel.setBounds(startX, startY + (verticalGap * 3), fieldWidth, fieldHeight);

            // Birthday Month
            birthdayMonthLabel.setFont(labelFont);
            birthdayMonthLabel.setForeground(textColor);
            birthdayMonthLabel.setBounds(startX, startY + (verticalGap * 4), fieldWidth, fieldHeight);

            // Birthday Day
            birthdayDayLabel.setFont(labelFont);
            birthdayDayLabel.setForeground(textColor);
            birthdayDayLabel.setBounds(startX + 30, startY + (verticalGap * 4), fieldWidth, fieldHeight);

            // Birthday Year
            birthdayYearLabel.setFont(labelFont);
            birthdayYearLabel.setForeground(textColor);
            birthdayYearLabel.setBounds(startX + 60, startY + (verticalGap * 4), fieldWidth, fieldHeight);

            // Friends Only
            if (friendsOnlyLabel.getText().equals("true")) {
                ImageIcon appIcon = new ImageIcon(
                        "C:\\Users\\peter\\Github\\CS180Team-Project\\symbols\\icons8-lock-48.png");
                Image scaledIcon = appIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
                iconLabel.setBounds(40, 20, 40, 40); // x, y, width, height
                this.add(iconLabel);
            } else {
                ImageIcon appIcon = new ImageIcon(
                        "C:\\Users\\peter\\Github\\CS180Team-Project\\symbols\\icons8-unlock-48.png");
                Image scaledIcon = appIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
                iconLabel.setBounds(40, 20, 40, 40); // x, y, width, height
                this.add(iconLabel);
            }

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
            // JLabel messagesNumber = new
            // JLabel(client.accessMessagesFromUser(profileInfo[0].replace("username:
            // ","")));
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
            addFriendButton = new RoundedButton("Add Friend", 16);
            addFriendButton.setBackground(instagramBlue);
            addFriendButton.setForeground(Color.WHITE);
            addFriendButton.setFont(new Font("Arial", Font.BOLD, 14));
            addFriendButton.setBounds(startX - 5, buttonsY, buttonWidth, buttonHeight);
            addFriendButton.setFocusPainted(false);

            // Create Block button
            blockButton = new RoundedButton("Block", 16);
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

            String photosInfo = this.client.accessPhotosFromUser(username);
            String[] photos;
            if (photosInfo.trim().equals("[]")) {
                photos = new String[0]; // Create an empty array
            } else {
                photos = photosInfo.trim().replace("[", "").replace("]", "").split(",");
            }
            String[] imagePaths = new String[6];
            if (photos.length > 0) {
                if (photos.length < 6) {
                    for (int i = 0; i < photos.length; i++) {
                        char gs = 29;
                        String[] lmao = photos[i].trim().replace("[", "").replace("]", "").split(gs + "");
                        imagePaths[i] = lmao[1];
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        char gs = 29;
                        String[] lmao = photos[i].trim().replace("[", "").replace("]", "").split(gs + "");
                        imagePaths[i] = lmao[0];
                    }
                }
            }

            // Add 6 image panels to the grid
            for (String encodedImage : imagePaths) {
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
                            byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

                            // Convert the byte array into a BufferedImage
                            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                            ImageIcon icon = new ImageIcon(bufferedImage);
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
            this.add(blackBackground);
            this.add(gridPanel);
            this.add(addFriendButton);
            this.add(blockButton);
            this.add(messagesNumber);
            this.add(messagesLabel);
            this.add(friendsNumber);
            this.add(friendsLabel);
            this.add(blockedNumber);
            this.add(blockedLabel);
            this.add(fullNameLabel);
            this.add(firstNameLabel);
            this.add(lastNameLabel);
            this.add(bioLabel);
            this.add(birthdayMonthLabel);
            this.add(birthdayDayLabel);
            this.add(birthdayYearLabel);
            this.add(blackBackground);
            this.setMaximumSize(new Dimension(screenSize.width, screenSize.height));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ProfilePanel(String profile, String photosInfo) {
        try {
            this.setLayout(null);
            char groupSeparator = (char) 29;
            String[] profileInfo = profile.split(groupSeparator + "");

            initializeLabels(profileInfo);

            // Get screen dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            // Create and setup black background
            JPanel blackBackground = createBlackBackgroundPanel(screenSize);

            // Create the profilePicture panel
            CircularImagePanel imagePanel = new CircularImagePanel(
                    profilePic.getText(), 150);
            imagePanel.setBounds(40, 80, 100, 100);
            this.add(imagePanel);

            // Format the labels
            formatLabels();

            // Friends Only
            if (friendsOnlyLabel.getText().equals("true")) {
                ImageIcon appIcon = new ImageIcon(getPath("icons8-lock-48.png", "symbols"));
                Image scaledIcon = appIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
                iconLabel.setBounds(40, 20, 40, 40); // x, y, width, height
                this.add(iconLabel);
            } else {
                ImageIcon appIcon = new ImageIcon(getPath("icons8-unlock-48.png", "symbols"));
                Image scaledIcon = appIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
                iconLabel.setBounds(40, 20, 40, 40); // x, y, width, height
                this.add(iconLabel);
            }

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
            // JLabel messagesNumber = new
            // JLabel(client.accessMessagesFromUser(profileInfo[0].replace("username:
            // ","")));
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

            createFriendAndBlockButtons();

            // Formatting integers.
            int startY = 250;
            int verticalGap = 25;
            int buttonHeight = 35;
            int buttonsY = startY + (verticalGap * 5) + 18; // Below birthday section

            // After adding the buttons:
            int gridStartY = buttonsY + buttonHeight + 20; // Below buttons with some spacing
            double gridWidth = screenSize.width / 2.5; // Half screen width
            int gridHeight = screenSize.height - gridStartY - 95; // Fill remaining height

            // Create panel for grid of pictures
            JPanel gridPanel = new JPanel(new GridLayout(2, 3, 2, 2)); // 2 rows, 3 columns, 10px gaps
            gridPanel.setBounds(0, gridStartY, (int) gridWidth, gridHeight);
            gridPanel.setBackground(Color.BLACK);

            String[] photos;
            if (photosInfo.trim().equals("[]")) {
                photos = new String[0]; // Create an empty array
            } else {
                photos = photosInfo.trim().replace("[", "").replace("]", "").split(",");
            }
            String[] imagePaths = new String[6];
            if (photos.length > 0) {
                if (photos.length < 6) {
                    for (int i = 0; i < photos.length; i++) {
                        char gs = 29;
                        String[] lmao = photos[i].trim().replace("[", "").replace("]", "").split(gs + "");
                        imagePaths[i] = lmao[1];
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        char gs = 29;
                        String[] lmao = photos[i].trim().replace("[", "").replace("]", "").split(gs + "");
                        imagePaths[i] = lmao[0];
                    }
                }
            }

            // Add 6 image panels to the grid
            for (String encodedImage : imagePaths) {
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
                            byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

                            // Convert the byte array into a BufferedImage
                            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                            ImageIcon icon = new ImageIcon(bufferedImage);
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

            // add everything to panel
            this.add(gridPanel);
            this.add(addFriendButton);
            this.add(blockButton);
            this.add(messagesNumber);
            this.add(messagesLabel);
            this.add(friendsNumber);
            this.add(friendsLabel);
            this.add(blockedNumber);
            this.add(blockedLabel);
            this.add(fullNameLabel);
            this.add(firstNameLabel);
            this.add(lastNameLabel);
            this.add(bioLabel);
            this.add(birthdayMonthLabel);
            this.add(birthdayDayLabel);
            this.add(birthdayYearLabel);
            this.add(blackBackground);
            this.setMaximumSize(new Dimension(screenSize.width, screenSize.height));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JButton[] getFriendAndBlockButtons() {
        return new JButton[] { addFriendButton, blockButton };
    }

    public void createComponent(ActionListener saveCallback, ArrayList<String> friendList,
            ArrayList<String> blockList) {
        try {
            char groupSeparator = (char) 29;
            String profileInput = this.client.accessProfile();
            String[] profileInfo = profileInput.split(groupSeparator + "");

            initializeLabels(profileInfo);
            formatLabels();

            JFrame frame = new JFrame("My Profile");
            // JDialog editDialog = new JDialog(frame, "Edit Profile", true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            JPanel mainPanel = new JPanel(null);
            JPanel friendAndBlockPanel = new JPanel(null);
            JPanel blackBackground = createBlackBackgroundPanel(new Dimension(screenSize.width, screenSize.height));
            blackBackground.setBounds(0, 0, (int) (screenSize.width / 2.5), screenSize.height - 40);

            CircularImagePanel imagePanel = new CircularImagePanel(
                    profilePic.getText(), 150);
            imagePanel.setBounds(40, 80, 100, 100);
            mainPanel.add(imagePanel);

            // Setup friends and blocks lists.
            RoundedTextField friendsText = new RoundedTextField("", 50, 15);
            friendsText.setFont(new Font("Arial", Font.PLAIN, 14));
            friendsText.setEditable(false);
            friendsText.setPreferredSize(new Dimension(100, 100));

            RoundedTextField blocksText = new RoundedTextField("", 50, 15);
            blocksText.setFont(new Font("Arial", Font.PLAIN, 14));
            blocksText.setEditable(false);
            blocksText.setPreferredSize(new Dimension(100, 100));

            if (friendList != null && friendList.size() > 0) {
                String friendString = "";
                for (String friend : friendList) {
                    friendString += friend + "\n";
                }
                friendsText.setText(friendString);
            }

            if (blockList != null && blockList.size() > 0) {
                String blockString = "";
                for (String block : blockList) {
                    blockString += block + "\n";
                }
                blocksText.setText(blockString);
            }

            // Friends Only
            if (friendsOnlyLabel.getText().equals("true")) {
                ImageIcon appIcon = new ImageIcon(getPath("icons8-lock-48.png", "symbols"));
                Image scaledIcon = appIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
                iconLabel.setBounds(40, 20, 40, 40); // x, y, width, height
                mainPanel.add(iconLabel);
            } else {
                ImageIcon appIcon = new ImageIcon(getPath("icons8-unlock-48.png", "symbols"));
                Image scaledIcon = appIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
                iconLabel.setBounds(40, 20, 40, 40); // x, y, width, height
                mainPanel.add(iconLabel);
            }

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
            // JLabel messagesNumber = new
            // JLabel(client.accessMessagesFromUser(profileInfo[0].replace("username:
            // ","")));
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

            createFriendAndBlockButtons();

            // Formatting integers.
            int startY = 250;
            int verticalGap = 25;
            int buttonHeight = 35;
            int buttonsY = startY + (verticalGap * 5) + 18; // Below birthday section

            // After adding the buttons:
            int gridStartY = buttonsY + buttonHeight + 20; // Below buttons with some spacing
            double gridWidth = screenSize.width / 2.5; // Half screen width
            int gridHeight = screenSize.height - gridStartY - 95; // Fill remaining height

            // Create panel for grid of pictures
            JPanel gridPanel = new JPanel(new GridLayout(2, 3, 2, 2)); // 2 rows, 3 columns, 10px gaps
            gridPanel.setBounds(0, gridStartY, (int) gridWidth, gridHeight + 50);
            gridPanel.setBackground(Color.BLACK);

            String photosInfo = client.accessPhotosFromUser(client.getUsername());
            String[] photos;
            if (photosInfo.trim().equals("[]")) {
                photos = new String[0]; // Create an empty array
            } else {
                photos = photosInfo.trim().replace("[", "").replace("]", "").split(",");
            }
            String[] imagePaths = new String[6];
            if (photos.length > 0) {
                if (photos.length < 6) {
                    for (int i = 0; i < photos.length; i++) {
                        char gs = 29;
                        String[] lmao = photos[i].trim().replace("[", "").replace("]", "").split(gs + "");
                        imagePaths[i] = lmao[1];
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        char gs = 29;
                        String[] lmao = photos[i].trim().replace("[", "").replace("]", "").split(gs + "");
                        imagePaths[i] = lmao[1];

                    }
                }
            }

            // Add 6 image panels to the grid
            for (String encodedImage : imagePaths) {
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
                            byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

                            // Convert the byte array into a BufferedImage
                            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                            ImageIcon icon = new ImageIcon(bufferedImage);
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
            if (saveCallback != null) {

                String currentFullName = fullNameLabel.getText();
                String currentFirstName = firstNameLabel.getText();
                String currentLastName = lastNameLabel.getText();
                String currentBio = bioLabel.getText();
                String currentBirthdayMonth = birthdayMonthLabel.getText();
                String currentBirthdayDay = birthdayDayLabel.getText();
                String currentBirthdayYear = birthdayYearLabel.getText();
                String currentFriendsOnly = friendsOnlyLabel.getText();
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
                RoundedTextField firstNameField = new RoundedTextField(currentFirstName, 20, 15); // Set current value
                                                                                                  // and
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
                            try {
                                // Read file data
                                byte[] imageData = Files.readAllBytes(Paths.get(selectedFile.getPath().trim()));

                                // Encode the binary data to a string (e.g., Base64)
                                String profileEncoded = java.util.Base64.getEncoder().encodeToString(imageData);

                                // Update profile picture display
                                profilePic.setText(profileEncoded);

                                // Update the existing panel
                                imagePanel.updateImage(profileEncoded, 150); // Pass encoded string here

                                // Ensure proper bounds and visibility
                                imagePanel.setBounds(40, 80, 150, 150);

                                // Force container to refresh
                                mainPanel.repaint();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Failed to load the image file.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
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

                // Create a button to save the changes
                RoundedButton saveButton = new RoundedButton("Save", 16);
                Color instagramBlue = new Color(0, 149, 246);
                saveButton.setBackground(instagramBlue);
                saveButton.setForeground(Color.WHITE);
                saveButton.setFont(new Font("Arial", Font.BOLD, 14));
                saveButton.setBounds(40, buttonsY + buttonHeight + 20, 270, buttonHeight); // Position below the //
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
                                + "/" + birthdayFieldYear.getText().trim() + groupSeparator + profilePic.getText() +
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
                                profilePic.setText(profilePic.getText());
                                friendsOnlyLabel
                                        .setText(((Boolean) friendsOnlyCheckBox.isSelected()).toString().trim());
                                // Close the dialog
                                if (saveCallback != null) {
                                    saveCallback.actionPerformed(
                                            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Save"));
                                }
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
                frame.add(saveButton, BorderLayout.SOUTH);
            } else {
                gbc.gridheight = 1;
                gbc.gridwidth = 1;
                friendsLabel.setForeground(Color.BLACK);
                JLabel newFriendsLabel = new JLabel("Friends:");
                newFriendsLabel.setFont(new Font("Arial", Font.BOLD, 14));
                newFriendsLabel.setForeground(Color.BLACK);
                rightPanel.add(newFriendsLabel, gbc);
                gbc.gridy++;
                rightPanel.add(friendsText, gbc);
                gbc.gridy++;
                JLabel newBlockedLabel = new JLabel("Blocked:");
                newBlockedLabel.setForeground(Color.BLACK);
                newBlockedLabel.setFont(new Font("Arial", Font.BOLD, 14));
                rightPanel.add(newBlockedLabel, gbc);
                gbc.gridy++;
                rightPanel.add(blocksText, gbc);
                gbc.gridy++;
            }

            // Add grid panel to main panel
            mainPanel.add(gridPanel);
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
            // Set the size of the dialog
            frame.setSize(screenSize.width, screenSize.height);
            // Set frame to maximized state
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // Center the dialog
            frame.setLocation(0, 0);

            // Make the dialog visible
            frame.setVisible(true);

            // Add the panel and button to the dialog
            friendAndBlockPanel.setBackground(Color.WHITE);
            friendAndBlockPanel.setBounds((int) (screenSize.width / 2.5), 0, screenSize.width -
                    (int) (screenSize.width / 2.5), screenSize.height);
            // mainPanel.add(friendAndBlockPanel);
            frame.add(mainPanel);
            // c.weightx = 0.3;
            // c.gridx = 1;
            // frame.add(friendAndBlockPanel, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set up labels based on profile information
    public void initializeLabels(String[] profileInfo) {
        this.usernameLabel = new JLabel(profileInfo[0]);
        fullNameLabel = new JLabel(profileInfo[1].substring(profileInfo[1].indexOf(":") + 1).trim() + " "
                + profileInfo[2].substring(profileInfo[2].indexOf(":") + 1).trim());
        firstNameLabel = new JLabel(profileInfo[1].substring(profileInfo[1].indexOf(":") + 1).trim());
        lastNameLabel = new JLabel(profileInfo[2].substring(profileInfo[2].indexOf(":") + 1).trim());
        bioLabel = new JLabel(profileInfo[3].substring(profileInfo[3].indexOf(":") + 1).trim());
        String birthday = profileInfo[4].substring(profileInfo[4].indexOf(":") + 1).trim();
        profilePic = new JLabel(profileInfo[5].substring(profileInfo[5].indexOf(":") + 2));
        friendsOnlyLabel = new JLabel(profileInfo[6].substring(profileInfo[6].indexOf(":") + 1));
        String[] birthdayParts = birthday.split("/");
        birthdayMonthLabel = new JLabel(birthdayParts[0]);
        birthdayDayLabel = new JLabel(birthdayParts[1]);
        birthdayYearLabel = new JLabel(birthdayParts[2]);
    }

    public JPanel createBlackBackgroundPanel(Dimension screenSize) {
        JPanel blackBackground = new RoundedPanel(30);
        blackBackground.setLayout(null);
        blackBackground.setBackground(Color.BLACK);
        int blackPanelWidth = (int) (screenSize.width * 0.6); // 60% width
        blackBackground.setBounds(0, 10, blackPanelWidth, screenSize.height - 180);
        return blackBackground;
    }

    public void formatLabels() {
        // Create Font and formatting variables
        Font labelFont = new Font("Monospaced", Font.BOLD, 16);
        Color textColor = Color.WHITE;
        int fieldWidth = 400;
        int fieldHeight = 30;
        int startX = 50;
        int startY = 250;
        int verticalGap = 25;

        fullNameLabel.setFont(labelFont);
        fullNameLabel.setForeground(textColor);
        fullNameLabel.setBounds(startX, startY, fieldWidth, fieldHeight); // maybe change height to fieldHeight

        // First Name
        firstNameLabel.setFont(labelFont);
        firstNameLabel.setForeground(textColor);
        firstNameLabel.setBounds(startX, startY + verticalGap, fieldWidth, fieldHeight);

        // Last Name
        lastNameLabel.setFont(labelFont);
        lastNameLabel.setForeground(textColor);
        lastNameLabel.setBounds(startX, startY + (verticalGap * 2), fieldWidth, fieldHeight);

        // Bio
        bioLabel.setFont(labelFont);
        bioLabel.setForeground(textColor);
        bioLabel.setBounds(startX, startY + (verticalGap * 3), fieldWidth, fieldHeight);

        // Birthday Month
        birthdayMonthLabel.setFont(labelFont);
        birthdayMonthLabel.setForeground(textColor);
        birthdayMonthLabel.setBounds(startX, startY + (verticalGap * 4), fieldWidth, fieldHeight);

        // Birthday Day
        birthdayDayLabel.setFont(labelFont);
        birthdayDayLabel.setForeground(textColor);
        birthdayDayLabel.setBounds(startX + 30, startY + (verticalGap * 4), fieldWidth, fieldHeight);

        // Birthday Year
        birthdayYearLabel.setFont(labelFont);
        birthdayYearLabel.setForeground(textColor);
        birthdayYearLabel.setBounds(startX + 60, startY + (verticalGap * 4), fieldWidth, fieldHeight);
    }

    public void createFriendAndBlockButtons() {
        // After birthday section:
        // Button styling
        int startX = 50;
        int startY = 250;
        int verticalGap = 25;
        Color instagramBlue = new Color(0, 149, 246);
        int buttonWidth = 270; // Wider rectangle
        int buttonHeight = 35;
        int buttonGap = 10;
        int buttonsY = startY + (verticalGap * 5) + 18; // Below birthday section
        // Create Add Friend button
        addFriendButton = new RoundedButton("Add Friend", 16);
        addFriendButton.setBackground(instagramBlue);
        addFriendButton.setForeground(Color.WHITE);
        addFriendButton.setFont(new Font("Arial", Font.BOLD, 14));
        addFriendButton.setBounds(startX - 5, buttonsY, buttonWidth, buttonHeight);
        addFriendButton.setFocusPainted(false);

        // Create Block button
        blockButton = new RoundedButton("Block", 16);
        blockButton.setBackground(instagramBlue);
        blockButton.setForeground(Color.WHITE);
        blockButton.setFont(new Font("Arial", Font.BOLD, 14));
        blockButton.setBounds(startX + buttonWidth + buttonGap - 10, buttonsY, buttonWidth, buttonHeight);
        blockButton.setFocusPainted(false);
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
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    public String getPath(String item, String folder) {
        Path path = Paths.get(folder);
        String thePath = path.resolve(item).toString();
        return thePath;
    }
}
