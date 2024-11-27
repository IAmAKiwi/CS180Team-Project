import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class profilePanel extends JPanel {
    private JLabel usernameLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel bioLabel;
    private JLabel birthdayLabel;
    private JLabel friendsOnlyLabel;
    private JButton editButton;
    private Client client;

    public profilePanel(String username, Client client) {
        this.client = client;
        usernameLabel = new JLabel(username);
        firstNameLabel = new JLabel();
        lastNameLabel = new JLabel();
        bioLabel = new JLabel();
        birthdayLabel = new JLabel();
        friendsOnlyLabel = new JLabel();
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
    private void createComponents() {
        usernameLabel = new JLabel(usernameLabel.getText());
        firstNameLabel = new JLabel(firstNameLabel.getText());
        lastNameLabel = new JLabel(lastNameLabel.getText());
        bioLabel = new JLabel(bioLabel.getText());
        birthdayLabel = new JLabel(birthdayLabel.getText());
        friendsOnlyLabel = new JLabel(friendsOnlyLabel.getText());
        editButton = new JButton("Edit");
    }

    private void addComponentsToPanel() {
        this.add(usernameLabel);
        this.add(firstNameLabel);
        this.add(lastNameLabel);
        this.add(bioLabel);
        this.add(birthdayLabel);
        this.add(friendsOnlyLabel);
        this.add(editButton);
    }

    private void editProfile() {
        // Create a new dialog to edit the profile
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog editDialog = new JDialog(frame, "Edit Profile", true);
        editDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
                        ((Boolean)friendsOnlyCheckBox.isSelected()).toString().trim();
                if (client.saveProfile(content)) {
                    firstNameLabel.setText(firstNameField.getText().trim());
                    lastNameLabel.setText(lastNameField.getText().trim());
                    bioLabel.setText(bioField.getText().trim());
                    birthdayLabel.setText(birthdayFieldMonth.getText().trim() + "/" + birthdayFieldDay.getText().trim()
                            + "/" + birthdayFieldYear.getText().trim());
                    friendsOnlyLabel.setText(((Boolean)friendsOnlyCheckBox.isSelected()).toString().trim());
                    // Close the dialog
                    editDialog.dispose();
                } else {
                    // Show an error message
                    JOptionPane.showMessageDialog(null, "Invalid profile information, " +
                                    "try again", "Error", JOptionPane.ERROR_MESSAGE);
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
