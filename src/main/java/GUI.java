import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI {
    private CardLayout cardLayout;
    private GUI gui;
    private JFrame frame;
    private chatListPanel chatListPanel;
    private LoginPanel loginPanel;
    //private chatPanel chatPanel;
    private profilePanel profilePanel;
    private JTabbedPane tabbedPane;
    private Client client;

    public GUI() {
        gui = this;
        cardLayout = new CardLayout();
        frame = new JFrame("Chatter");
        client = new Client();
        loginPanel = new LoginPanel(client);
        do  {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!loginPanel.isDone());
        client = loginPanel.getClient();
        String username = loginPanel.getUsername();
        //chatPanel = new chatPanel(client);
        profilePanel = new profilePanel(username, client);
        updateProfilePanel();
        chatListPanel = new chatListPanel(client);
        refreshChats();
        tabbedPane = new JTabbedPane();
        scheduleUpdates();
        gui.start();
    }

    public void start() {
        frame.add(chatListPanel, BorderLayout.WEST);
        //frame.add(chatPanel, BorderLayout.CENTER);
        frame.add(profilePanel, BorderLayout.EAST);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void refreshChats() {
        String chats = client.getChatList();
        String[] chatsArray = chats.split("" + (char) 29);
        chatListPanel.refreshChats(chatsArray);
    }

    public void addChat(String chat) {
        chatListPanel.addChat(chat);
    }

    public void removeChat(String chat) {
        chatListPanel.removeChat(chat);
    }

    public void updateProfilePanel() {
        String profile = client.accessProfile();
        String[] profileParts = profile.split("" + (char) 29);

        String username = profileParts[0].substring(profileParts[0].indexOf(":") + 1);
        String firstName = profileParts[1].substring(profileParts[1].indexOf(":") + 1);
        String lastName = profileParts[2].substring(profileParts[2].indexOf(":") + 1);
        String bio = profileParts[3].substring(profileParts[3].indexOf(":") + 1);
        String birthday = profileParts[4].substring(profileParts[4].indexOf(":") + 1);
        String profilePic = profileParts[5].substring(profileParts[5].indexOf(":") + 1);
        String friendsOnly = profileParts[6].substring(profileParts[6].indexOf(":") + 1);

        profilePanel.refreshProfile(username, firstName, lastName, bio, birthday, profilePic, friendsOnly);
        frame.repaint();
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
        Timer timer = new Timer(5000, new ActionListener() { // Check every 5 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetch new data or perform updates
                updateData();
                // Update GUI components
                updateGUI();
            }
        });
        timer.start();
    }

    public void updateData() {
        // Fetch new data or perform updates
    }

    public void updateGUI() {
        // TODO: fix the selection
        updateProfilePanel();
        refreshChats();

    }

    public void logout() {

    }

    public static void main(String[] args) {
        new GUI();
    }

}
