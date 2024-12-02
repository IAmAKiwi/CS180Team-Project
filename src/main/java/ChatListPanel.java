import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.Instant;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.Date;


public class ChatListPanel extends JPanel {
    private boolean searching = false;
    private JList<String> chatList;
    private DefaultListModel<String> listModel;
    private JButton newChatButton;
    private JTextField searchField;
    private Client client;

    public ChatListPanel(Client client) {
        this.client = client;
        initializeComponents();
        setupLayout();
        addListeners();
        setupStyling();
    }

    private void initializeComponents() {
        // Initialize list model and chat list
        listModel = new DefaultListModel<>();
        chatList = new JList<>(listModel);
        chatList.setCellRenderer(new ChatListCellRenderer());
        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create search field with placeholder
        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Search chats...");

        // Create new chat button with icon
        newChatButton = new JButton("New Chat");
        newChatButton.setIcon(new ImageIcon("./images/icons8-add-to-chat-50.png")); // Add your icon
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for search and new chat button
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(newChatButton, BorderLayout.EAST);

        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(chatList), BorderLayout.CENTER);
    }

    private void setupStyling() {
        // Panel styling
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(250, getHeight()));

        // Search field styling
        searchField.setPreferredSize(new Dimension(100, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Button styling
        newChatButton.setBackground(new Color(76, 175, 80));
        newChatButton.setForeground(Color.WHITE);
        newChatButton.setBorder(new RoundedBorder(8));
        newChatButton.setFocusPainted(false);

        // List styling
        chatList.setFixedCellHeight(60);
        chatList.setBorder(null);
    }

    private void addListeners() {
        // Search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterChats(); }
            public void removeUpdate(DocumentEvent e) { filterChats(); }
            public void changedUpdate(DocumentEvent e) { filterChats(); }
        });

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                filterChats();
                searching = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                searching = false;
            }
        });

        // New chat button action
        newChatButton.addActionListener(e -> showNewChatDialog());

        // Chat selection listener
        chatList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedChat = chatList.getSelectedValue();
                if (selectedChat != null) {
                    firePropertyChange("selectedChat", null, selectedChat);
                }
            }
        });
    }

    // Custom cell renderer for chat list items
    private class ChatListCellRenderer extends JPanel implements ListCellRenderer<String> {
        private JLabel nameLabel = new JLabel();
        private JLabel lastMessageLabel = new JLabel();
        private JLabel timeLabel = new JLabel();

        public ChatListCellRenderer() {
            setLayout(new BorderLayout(10, 5));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
            textPanel.setOpaque(false);
            textPanel.add(nameLabel);
            textPanel.add(lastMessageLabel);

            add(textPanel, BorderLayout.CENTER);
            add(timeLabel, BorderLayout.EAST);

            // Style labels
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            lastMessageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            lastMessageLabel.setForeground(Color.GRAY);
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            timeLabel.setForeground(Color.GRAY);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends String> list, String username,
                int index, boolean isSelected, boolean cellHasFocus) {
            // Set content
            if (username == null || username.isEmpty()) {
                listModel.clear();
                return this;
            }
            nameLabel.setText(username);
            lastMessageLabel.setText(getLastMessage(username));
            timeLabel.setText(getLastMessageTime(username));

            // Handle selection styling
            if (isSelected) {
                setBackground(new Color(240, 247, 250));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(76, 175, 80)),
                    BorderFactory.createEmptyBorder(5, 7, 5, 10)
                ));
            } else {
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            }

            return this;
        }

        private String getLastMessage(String username) {
            // Get last message from chat history
            String chat = "";
            try {
                chat = client.getChat(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Parse and return last message preview
            return chat != null ? chat.substring(0, Math.min(30, chat.length())) + "..." : "";
        }

        private String getLastMessageTime(String username) {
            // Get and format last message timestamp
            String chat = (client.getChat(username));
            String lastMessageString = chat.substring((chat.substring(0, chat.length() - 2)).indexOf((char)26));
            //String content = lastMessageString.substring(lastMessageString.indexOf(" "));
            String[] arguments = lastMessageString.split(":");
            Message msg = new Message(arguments[1], arguments[2], Long.parseLong(arguments[0]));
            Date currTime = Date.from(Instant.now());
            Date msgTime = msg.getTimeStamp();
            if (String.format("%Y%j", currTime, currTime).equals(
                String.format("%Y%j", msgTime, msgTime)))
            {
                return String.format("%l:%M %T", msgTime, msgTime, msgTime);
            }
            int currYearDay = Integer.parseInt(String.format("%Y%j", currTime, currTime));
            int msgYearDay = Integer.parseInt(String.format("%Y%j", msgTime, msgTime));
            if (msgYearDay == currYearDay - 1)
            {
                return "Yesterday";
            }
            if (((currYearDay - 7) < msgYearDay) && (msgYearDay < (currYearDay - 1)))
            {
                return String.format("%A", msgYearDay);
            }
            return String.format("%e %b %Y", msgYearDay, msgYearDay, msgYearDay);
        }
    }

    private void filterChats() {
        String searchText = searchField.getText().toLowerCase();
        listModel.clear();
        try {
            for (String chat : client.getUserList().split("" + (char) 29)) {
                if (chat.toLowerCase().contains(searchText)) {
                    listModel.addElement(chat);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNewChatDialog() {
        String[] users = {};
        try {
            users = client.getUserList().split("" + (char) 29);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame newChatDialog = new JFrame("Select User");
        newChatDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newChatDialog.setSize(550, 100);
        newChatDialog.setLocationRelativeTo(this.getParent());

        JComboBox<String> userComboBox = new JComboBox<>(users);

        JPanel buttonPanel = new JPanel(new FlowLayout(0, 0, 0));

        JButton addFriendButton = new JButton("Add/Remove Friend");
        JButton blockButton = new JButton("Block/Unblock");
        JButton openChatButton = new JButton("Open Chat");
        JButton cancelButton = new JButton("Cancel");


        addFriendButton.addActionListener(e -> {
            String selectedUser = (String) userComboBox.getSelectedItem();
            try {
                if (selectedUser != null) {
                    if (client.getFriendList().contains(selectedUser)) {
                        client.removeFriend(selectedUser);
                    } else {
                        client.addFriend(selectedUser);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            newChatDialog.dispose();
        });

        blockButton.addActionListener(e -> {
            String selectedUser = (String) userComboBox.getSelectedItem();
            try {
                if (selectedUser != null) {
                    if (client.getBlockList().contains(selectedUser)) {
                        client.unblockUser(selectedUser);
                    } else {
                        client.blockUser(selectedUser);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            newChatDialog.dispose();
        });

        openChatButton.addActionListener(e -> {
            String selectedUser = (String) userComboBox.getSelectedItem();
            try {
                if (client.getChat(selectedUser).isEmpty()) {
                    addChat(selectedUser);
                    client.createChat(selectedUser);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            chatList.setSelectedValue(selectedUser, true);
            newChatDialog.dispose();
        });

        cancelButton.addActionListener(e -> newChatDialog.dispose());

        buttonPanel.add(addFriendButton);
        buttonPanel.add(blockButton);
        buttonPanel.add(openChatButton);
        buttonPanel.add(cancelButton);

        newChatDialog.add(buttonPanel, BorderLayout.SOUTH);
        newChatDialog.add(userComboBox, BorderLayout.CENTER);

        newChatDialog.setVisible(true);

        /*
        String selectedUser = (String) JOptionPane.showInputDialog(
            this,
            "Select a user to chat with:",
            "New Chat",
            JOptionPane.PLAIN_MESSAGE,
            null,
            users,
            users[0]
        );

        if (selectedUser != null) {
            if (client.getChat(selectedUser).isEmpty()) {
                addChat(selectedUser);
                client.createChat(selectedUser);
            }
            chatList.setSelectedValue(selectedUser, true);
        }*/
    }

    private void addUnreadIndicator(String username, int count) {
        // Add a small circle with number of unread messages
        JLabel unreadLabel = new JLabel(String.valueOf(count));
        unreadLabel.setOpaque(true);
        unreadLabel.setBackground(new Color(76, 175, 80));
        unreadLabel.setForeground(Color.WHITE);
        unreadLabel.setBorder(new RoundedBorder(10));
    }
    
    // Custom rounded border for buttons
    private class RoundedBorder extends AbstractBorder {
        private int radius;
        
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }
    
    // Public methods
    public void refreshChats(String[] chats) {
        if (searchField.getText().trim().isEmpty() && !searching && chatList.isSelectionEmpty()) {
            listModel.clear();
            if (chats != null) {
                for (String chat : chats) {
                    if (chat != null) {
                        listModel.addElement(chat);
                    }
                }
            }
        }
    }
    
    public void addChat(String chat) {
        if (!listModel.contains(chat)) {
            listModel.addElement(chat);
        }
    }
    
    public void removeChat(String chat) {
        listModel.removeElement(chat);
    }
    
    public String getSelectedChat() {
        return chatList.getSelectedValue();
    }
}