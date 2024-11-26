public class profilePanel {
    private JFrame frame;
    private JPanel panel;
    private JLabel usernameLabel;  
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel bioLabel;
    private JButton editButton;
    private JButton logoutButton;

    public profilePanel(String username) {
        frame = new JFrame("Profile");
        panel = new JPanel();
        usernameLabel = new JLabel(username);
    }

    public void start() {
        frame.add(panel);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void refreshProfile(String username, String email, String phone, String bio) {
        usernameLabel.setText(username);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
        bioLabel.setText(bio);
    }

    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public void addLogoutButtonListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }

    private void createComponents() {
        usernameLabel = new JLabel(username);
        emailLabel = new JLabel(email);
        phoneLabel = new JLabel(phone);
        bioLabel = new JLabel(bio);
        editButton = new JButton("Edit");
        logoutButton = new JButton("Logout");
    }

    private void addComponentsToPanel() {
        panel.add(usernameLabel);
        panel.add(emailLabel);
        panel.add(phoneLabel);
        panel.add(bioLabel);
        panel.add(editButton);
        panel.add(logoutButton);
    }
    
}
