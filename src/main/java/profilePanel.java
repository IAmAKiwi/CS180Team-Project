import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class profilePanel extends JPanel {
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
