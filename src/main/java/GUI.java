import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements Runnable {
    private JFrame frame;
    private ChatListPanel chatListPanel;
    private LoginPanel loginPanel;
    //private chatPanel chatPanel;
    private ProfilePanel profilePanel;
    private JButton logoutButton;
    private Client client;

    public GUI(LoginPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    public void run() {
        frame = new JFrame("Chatter");
        logoutButton = new JButton("Logout");
        client = loginPanel.getClient();
        String username = loginPanel.getUsername();
        //chatPanel = new chatPanel(client);
        profilePanel = new ProfilePanel(username, client);
        updateProfilePanel();
        chatListPanel = new ChatListPanel(client);
        refreshChats();
        frame.add(chatListPanel, BorderLayout.WEST);
        //frame.add(chatPanel, BorderLayout.CENTER);
        frame.add(profilePanel, BorderLayout.EAST);
        logoutButton.addActionListener(e -> logout());
        frame.add(logoutButton, BoxLayout.LINE_AXIS);
        JPanel logoutPanel = new JPanel(new BorderLayout(0, 0));
        logoutPanel.add(logoutButton, BorderLayout.LINE_END);
        frame.add(logoutPanel, BorderLayout.SOUTH);
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
        String chats = client.getChatList();
        String[] chatsArray = chats.split("" + (char) 29);
        chatListPanel.refreshChats(chatsArray);
    }

    public void updateProfilePanel() {
        String profile = client.accessProfile();
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
        String profilePic = profileParts[5].substring(profileParts[5].indexOf(":") + 1);
        String friendsOnly = profileParts[6].substring(profileParts[6].indexOf(":") + 1);

        profilePanel.refreshProfile(username, firstName, lastName, bio, birthday, profilePic, friendsOnly);
    }

    public void updateFriendsAndBlocks() {
        String friends = client.getFriendList();
        String[] friendsArray = friends.split("" + (char) 29);
        String blocks = client.getBlockList();
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
        Timer timer = new Timer(1000, new ActionListener() { // Check every 5 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update GUI components
                if (loginPanel == null) {
                    return;
                }
                updateGUI();
            }
        });
        timer.start();
    }


    public void updateGUI() {
        updateProfilePanel();
        updateFriendsAndBlocks();
        refreshChats();
    }

    public void logout() {
        client.logout();
        this.loginPanel = null;
        frame.setVisible(false);
        frame.dispose();
    }

    public void disconnect() {
        client.disconnect();
        frame.setVisible(false);
        this.loginPanel = null;
        frame.dispose();
    }

    public boolean isDone() {
        return loginPanel == null;
    }

    public static void main(String[] args) {
        while (true) {
            LoginPanel loginPanel = new LoginPanel(new Client());
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
