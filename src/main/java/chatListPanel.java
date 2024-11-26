import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class chatListPanel extends JPanel {
    private JList<String> chatList;
    private DefaultListModel<String> listModel;
    private JButton newChatButton;
    private JTextField searchField;
    private Client client;
    
    public chatListPanel(Client client) {
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
        newChatButton.setIcon(new ImageIcon("C:/Users/peter/Github/CS180Team-Project/images/icons8-add-to-chat-50.png")); // Add your icon
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
        searchField.setPreferredSize(new Dimension(200, 30));
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
            String chat = client.getChat(username);
            // Parse and return last message preview
            return chat != null ? chat.substring(0, Math.min(30, chat.length())) + "..." : "";
        }
        
        private String getLastMessageTime(String username) {
            // Get and format last message timestamp
            return "12:34 PM"; // Replace with actual timestamp
        }
    }
    
    private void filterChats() {
        String searchText = searchField.getText().toLowerCase();
        listModel.clear();
        
        for (String chat : client.getUserList().split(":")) {
            if (chat.toLowerCase().contains(searchText)) {
                listModel.addElement(chat);
            }
        }
    }
    
    private void showNewChatDialog() {
        String[] users = client.getUserList().split(":");
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
            addChat(selectedUser);
            chatList.setSelectedValue(selectedUser, true);
        }
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
        listModel.clear();
        for (String chat : chats) {
            listModel.addElement(chat);
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