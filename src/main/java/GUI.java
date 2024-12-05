import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.IOException;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class GUI implements Runnable {
    private JFrame frame;
    private ChatListPanel chatListPanel;
    private LoginPanel loginPanel;
    private chatPanel chatPanel;
    private ProfilePanel profilePanel;
    private RoundedButton logoutButton;
    private RoundedButton addFriendButton;
    private RoundedButton addBlockButton;
    private Client client;
    private RoundedPanel headerPanel;
    private JPopupMenu profileMenu;
    private CircularButton profileButton;

    public GUI(LoginPanel loginPanel) {
        this.loginPanel = loginPanel;
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

    private void createHeader() {
        headerPanel = new RoundedPanel(15);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 60));
        // headerPanel.setBorder(new EmptyBorder(10,10,10,10));

        String profilePic = profilePanel.getProfilePic();
        if (profilePic == null || profilePic.isEmpty()) {
            profilePic = "C:/Users/peter/Github/CS180Team-Project/images/default-image.jpg";
        }

        profileButton = new CircularButton(profilePic, 50);
        profileButton.setBorder(new EmptyBorder(0, 0, 0, 10));
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonWrapper.setBackground(new Color(245, 245, 245));
        buttonWrapper.setBorder(new EmptyBorder(0, 0, 30, 150)); // top, left, bottom, right padding
        buttonWrapper.add(profileButton);
        headerPanel.add(buttonWrapper, BorderLayout.EAST);

        profileMenu = new JPopupMenu();
        profileMenu.setBackground(new Color(30, 30, 30));

        JMenuItem profileItem = new JMenuItem("My Profile");
        profileItem.setForeground(Color.WHITE);
        profileItem.setBackground(new Color(30, 30, 30));
        profileItem.addActionListener(e -> {
            profilePanel.setVisible(true);
            chatPanel.setVisible(false);
            chatListPanel.setVisible(false);
        });

        // JMenuItem editProfileItem = new JMenuItem("Edit Profile");
        // editProfileItem.setForeground(Color.WHITE);
        // editProfileItem.setBackground(new Color(30,30,30));
        // editProfileItem.addActionListener(e -> {
        // profilePanel.setVisible(true);
        // chatPanel.setVisible(false);
        // chatListPanel.setVisible(false);
        // profilePanel.editProfile();
        // });

        profileMenu.add(profileItem);
        // profileMenu.add(editProfileItem);

        profileButton.addActionListener(e -> {
            profileMenu.show(profileButton, 0, profileButton.getHeight());
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.gridy = 0;
        frame.add(headerPanel, gbc);
    }

    class CircularButton extends JButton {
        private Image image;
        private int size;

        public CircularButton(String imagePath, int size) {
            this.size = size;
            setPreferredSize(new Dimension(size, size));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            try {
                ImageIcon icon = new ImageIcon(imagePath);
                Image originalImage = icon.getImage();

                BufferedImage circularImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circularImage.createGraphics();
                g2.drawImage(originalImage, 0, 0, size, size, null);
                g2.dispose();

                this.image = circularImage;
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

    public void run() {
        frame = new JFrame("Chatter");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        logoutButton = new RoundedButton("Logout",15);
        logoutButton.setBackground(new Color(0, 149, 246));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(100, 20));

        addFriendButton = new RoundedButton("Friend",15);
        addFriendButton.setBackground(new Color(0, 149, 246));
        addFriendButton.setForeground(Color.WHITE);
        addFriendButton.setFont(new Font("Arial", Font.BOLD, 14));
        addFriendButton.setPreferredSize(new Dimension(150, 20));

        addBlockButton = new RoundedButton("Block",15);
        addBlockButton.setBackground(new Color(0, 149, 246));
        addBlockButton.setForeground(Color.WHITE);
        addBlockButton.setFont(new Font("Arial", Font.BOLD, 14));
        addBlockButton.setPreferredSize(new Dimension(150, 20));

        client = loginPanel.getClient();
        if (client == null) {
            frame.setVisible(false);
            this.loginPanel = null;
            frame.dispose();
            return;
        }
        if (!isConnected()) {
            displayError("Connection Lost");
            frame.setVisible(false);
            this.loginPanel = null;
            frame.dispose();
            return;
        }
        String username = loginPanel.getUsername();
        chatPanel = new chatPanel(client);
        profilePanel = new ProfilePanel(username, client);
        updateProfilePanel();
        chatListPanel = new ChatListPanel(client);
        // NEW CHANGE TESTING
        chatListPanel.addPropertyChangeListener("selectedChat", evt -> {
            String selectedUser = (String) evt.getNewValue();
            if (selectedUser != null) {
                try {
                    chatPanel.refreshChatAutoScroll(selectedUser);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        refreshChats();
        createHeader();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        frame.add(chatListPanel, gbc);

        gbc.weightx = 1.0;
        frame.add(chatPanel, gbc);

        gbc.weightx = .2;
        frame.add(profilePanel, gbc);

        gbc.weighty = 0;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 10);
        logoutButton.addActionListener(e -> logout());

        addFriendButton.addActionListener(e -> {
            try {
                String otherUsername = chatListPanel.getSelectedChat();
                if (otherUsername == null || otherUsername.isEmpty()) {
                    return;
                }
                if (!client.addFriend(otherUsername)) {
                    client.removeFriend(otherUsername);
                }
            } catch (IOException ex) {
                disconnect();
            }
        });

        addBlockButton.addActionListener(e -> {
            try {
                String otherUsername = chatListPanel.getSelectedChat();
                if (otherUsername == null || otherUsername.isEmpty()) {
                    return;
                }
                if (!client.blockUser(otherUsername)) {
                    client.unblockUser(otherUsername);
                }
            } catch (IOException ex) {
                disconnect();
            }
        });

        RoundedPanel logoutPanel = new RoundedPanel(15);
        logoutPanel.setLayout(new GridBagLayout());
        GridBagConstraints logoutConstraints = new GridBagConstraints();
        logoutConstraints.fill = GridBagConstraints.VERTICAL;
        logoutConstraints.weightx = 1.0;
        logoutConstraints.insets = new Insets(0, 50, 50, 10);
        logoutPanel.add(logoutButton, logoutConstraints);
        logoutConstraints.gridy = 1;
        logoutPanel.add(addFriendButton, logoutConstraints);
        logoutConstraints.gridy++;
        logoutPanel.add(addBlockButton, logoutConstraints);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy++;
        frame.add(logoutPanel, gbc);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logout();
                disconnect();
            }
        });
        updateGUI();
        frame.setVisible(true);
        scheduleUpdates();
    }

    public void refreshChats() {
        String chats = "";
        try {
            chats = client.getChatList();
        } catch (IOException e) {
            disconnect();
            return;
        }
        String[] chatsArray = chats.split("" + (char) 29);
        chatListPanel.refreshChats(chatsArray);
        String selectedChat = chatListPanel.getSelectedChat();
        if (selectedChat != null && !selectedChat.isEmpty()) {
            try {
                chatPanel.refreshChat(selectedChat);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public void updateProfilePanel() {
        if (!isConnected()) {
            return;
        }
        String profile = "";
        try {
            profile = client.accessProfile();
        } catch (IOException e) {
            disconnect();
            return;
        }
        if (profile == null) {
            disconnect();
            return;
        }
        String[] profileParts = profile.split("" + (char) 29);

        String username = profileParts[0].substring(profileParts[0].indexOf(":") + 1);
        String firstName = profileParts[1].substring(profileParts[1].indexOf(":") + 1);
        String lastName = profileParts[2].substring(profileParts[2].indexOf(":") + 1);
        String bio = profileParts[3].substring(profileParts[3].indexOf(":") + 1);
        String birthday = profileParts[4].substring(profileParts[4].indexOf(":") + 1);
        String[] birthdayParts = birthday.split("/");
        String day = birthdayParts[1];
        String month = birthdayParts[0];
        String year = birthdayParts[2];
        String profilePic = profileParts[5].substring(profileParts[5].indexOf(":") + 1);
        String friendsOnly = profileParts[6].substring(profileParts[6].indexOf(":") + 1);

        profilePanel.refreshProfile(username, firstName, lastName, bio, month, day, year, profilePic, friendsOnly);
    }

    public void updateFriendsAndBlocks() {
        String friends;
        try {
            friends = client.getFriendList();
        } catch (IOException e) {
            disconnect();
            return;
        }
        String[] friendsArray = friends.split("" + (char) 29);
        String blocks;
        try {
            blocks = client.getBlockList();
        } catch (IOException e) {
            disconnect();
            return;
        }
        String[] blocksArray = blocks.split("" + (char) 29);
        profilePanel.updateFriendsAndBlocks(friendsArray, blocksArray);
    }

    public void updateChat() {
        // chatPanel.updateChat(chat);
    }

    public void displayMessage(String message) {
        // chatPanel.displayMessage();
    }

    public void displayError(String error) {
        JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void scheduleUpdates() {
        Timer timer = new Timer(300, new ActionListener() { // Check every 1 second
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update GUI components
                if (loginPanel == null) {
                    return;
                }
                updateGUI();
            }
        });
        if (frame.isVisible()) {
            timer.start();
        }
    }

    public void updateGUI() {
        if (!isConnected()) {
            displayError("Connection Lost");
            disconnect();
            return;
        }
        updateProfilePanel();
        updateFriendsAndBlocks();
        refreshChats();
    }

    public void logout() {
        try {
            client.logout();
        } catch (IOException e) {
            displayError("Connection Lost");
            disconnect();
            return;
        }
        this.loginPanel = null;
        frame.setVisible(false);
        frame.dispose();
    }

    public void disconnect() {
        if (client != null) {
            client.disconnect();
            client = null;
        }
        frame.setVisible(false);
        frame.dispose();
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

    public static void main(String[] args) {
        LoginPanel loginPanel;
        int i = JOptionPane.showConfirmDialog(null, "Auto login to user1 Password1$?",
                "Auto Login", JOptionPane.YES_NO_OPTION);
        if (i == JOptionPane.YES_OPTION) {
            try {
                Client client = new Client();
                client.login("user3" + (char) 29 + "Password3$");
                loginPanel = new LoginPanel(client);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not connect to server",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            while (true) {
                SwingUtilities.invokeLater(loginPanel);
                while (!loginPanel.isDone()) { // Wait for login panel to finish
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                GUI gui = new GUI(loginPanel);
                SwingUtilities.invokeLater(gui);
                while (!gui.isDone()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int j = JOptionPane.showConfirmDialog(null, "continue?",
                        "Auto Login", JOptionPane.YES_NO_OPTION);
                if (j != JOptionPane.YES_OPTION) {
                    System.out.println("Disconnecting...");
                    gui.disconnect();
                    loginPanel = null;
                    return;
                }
            }
        } else if (i == JOptionPane.NO_OPTION) {
            while (true) {
                try {
                    loginPanel = new LoginPanel(new Client());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Could not connect to server",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!loginPanel.isConnected()) {
                    return;
                }
                SwingUtilities.invokeLater(loginPanel);
                while (!loginPanel.isDone()) { // Wait for login panel to finish
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                GUI gui = new GUI(loginPanel);
                SwingUtilities.invokeLater(gui);
                while (!gui.isDone()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
