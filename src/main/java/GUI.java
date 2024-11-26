public class GUI {
    private JFrame frame;
    private chatListPanel chatListPanel;
    private LoginPanel loginPanel;
    private chatPanel chatPanel;
    private profilePanel profilePanel;
    private JTabbedPane tabbedPane;

    public GUI(String username) {
        frame = new JFrame("Chatter");
        chatListPanel = new chatListPanel();
        loginPanel = new LoginPanel();
        chatPanel = new chatPanel(username);
        profilePanel = new profilePanel(username);
        tabbedPane = new JTabbedPane();
    }

    public void start() {
        frame.add(chatListPanel, BorderLayout.WEST);
        frame.add(chatPanel, BorderLayout.CENTER);
        frame.add(profilePanel, BorderLayout.EAST);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void refreshChats(String[] chats) {
        chatListPanel.refreshChats(chats);
    }

    public void addChat(String chat) {
        chatListPanel.addChat(chat);
    }

    public void removeChat(String chat) {
        chatListPanel.removeChat(chat);
    }

    public void updateProfile(String profile) {
        profilePanel.updateProfile(profile);
    }

    public void updateChat(String chat) {
        chatPanel.updateChat(chat);
    }

    public void displayMessage(String message) {
        chatPanel.displayMessage(message);
    }

    public void displayError(String error) {
        JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
    }


}
