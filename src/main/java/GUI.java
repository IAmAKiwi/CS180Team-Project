import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI implements Runnable {
    private JFrame frame;
    private ChatListPanel chatListPanel;
    private LoginPanel loginPanel;
    private chatPanel chatPanel;
    private ProfilePanel profilePanel;
    private JButton logoutButton;
    private Client client;

    public GUI(LoginPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    public void run() {
        frame = new JFrame("Chatter");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        logoutButton = new JButton("Logout");
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
        //NEW CHANGE TESTING
        chatListPanel.addPropertyChangeListener("selectedChat", evt -> {
            String selectedUser = (String) evt.getNewValue();
            if (selectedUser != null) {
                try {
                    chatPanel.refreshChat(selectedUser);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        refreshChats();

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
        gbc.insets = new Insets(10, 10, 10, 10);
        logoutButton.addActionListener(e -> logout());
        logoutButton.setForeground(Color.RED);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 10));
        //frame.add(logoutButton, gbc);
        JPanel logoutPanel = new JPanel(new BorderLayout(0, 0));
        logoutPanel.add(logoutButton, BorderLayout.LINE_END);
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
        /*chatListPanel.refreshChats(chatsArray);
        String selectedChat = chatListPanel.getSelectedChat();
        if (selectedChat != null && !selectedChat.isEmpty()) {
            try {
                chatPanel.refreshChat(selectedChat);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }*/
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
        //chatPanel.updateChat(chat);
    }

    public void displayMessage(String message) {
        //chatPanel.displayMessage();
    }

    public void displayError(String error) {
        JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void scheduleUpdates() {
        Timer timer = new Timer(1000, new ActionListener() { // Check every 1 second
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
