import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;



public class ChatListPanel extends JPanel {
    private boolean searching = false;
    private JList<String> chatList;
    private DefaultListModel<String> listModel;
    private RoundedTextField searchField;
    private Client client;

    public ChatListPanel(Client client) {
        this.client = client;
        initializeComponents();
        setupLayout();
        addListeners();
        setupStyling();
    }
    private static class RoundedScrollPane extends JScrollPane {
    private int radius;
    private Color backgroundColor;

    public RoundedScrollPane(Component view, int radius) {
        super(view);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));        
        // Style the scrollbars
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200);
                this.trackColor = new Color(245, 245, 245);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Set rendering hints
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Fill background
        if (backgroundColor != null) {
            g2.setColor(backgroundColor);
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 200, 200));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}

    class RoundedTextField extends JTextField {
        private int radius;

        public RoundedTextField(String text, int columns, int radius) {
            super(text, columns);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }
    private static class RoundedLabel extends JLabel {
        private int radius;
        private Color backgroundColor;
    
        public RoundedLabel(String text, int radius) {
            super(text);
            this.radius = radius;
            setOpaque(false);
        }
    
        @Override
        public void setBackground(Color bg) {
            this.backgroundColor = bg;
            repaint();
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            if (backgroundColor != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }
    
    // Usage:
    // RoundedLabel label = new RoundedLabel("Text", 15);
    // label.setBackground(new Color(245, 245, 245));
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

    
    

    


    private void initializeComponents() {
        // Initialize list model and chat list
        listModel = new DefaultListModel<>();
        chatList = new JList<>(listModel);
        chatList.setCellRenderer(new ChatListCellRenderer());
        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create search field with placeholder
        searchField = new RoundedTextField("", 10, 15);
        searchField.putClientProperty("RoundedTextField.placeholderText", "Search chats...");
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for search and new chat button
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.add(searchField, BorderLayout.CENTER);

        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(new RoundedScrollPane(chatList,15), BorderLayout.CENTER);
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


        // List styling
        chatList.setFixedCellHeight(60);
        chatList.setBorder(null);
    }

    private void addListeners() {
        // Search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                try {
                filterChats();
            } catch (IOException ex) {
                return;
            }
            }
            public void removeUpdate(DocumentEvent e) {
                try {
                    filterChats();
                } catch (IOException ex) {
                    return;
                }
            }
            public void changedUpdate(DocumentEvent e) {
                try {
                    filterChats();
                } catch (IOException ex) {
                    return;
                }
            }
        });

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                try {
                    filterChats();
                } catch (IOException ex) {
                    return;
                }
                searching = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                searching = false;
            }
        });

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
            try {
                lastMessageLabel.setText(getLastMessage(username));
                timeLabel.setText(getLastMessageTime(username));
            } catch (IOException ex) {
                return this;
            }

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

        private String getLastMessage(String username) throws IOException {
            // Get last message from chat history
            String chat = "";
            chat = client.getChat(username);
            if (chat == null || chat.isEmpty()) {
                return "";
            }
            // Parse and return last message preview
            // Get to the beginning of the last message
            chat = chat.substring(0, chat.lastIndexOf((char) 29));
            return chat.substring(chat.lastIndexOf(":") + 1);
        }

        private String getLastMessageTime(String username) throws IOException {
            // Get and format last message timestamp
            String chat = "NULL";
            chat = (client.getChat(username));
            if (chat == null || chat.isEmpty()) {
                return "";
            }
            // Get to the beginning of the last message
            chat = chat.substring(0, chat.lastIndexOf((char) 29));
            if (chat.contains("" + (char) 29)) {
                chat = chat.substring(0, chat.lastIndexOf((char) 29));
            }
            String temp = chat.substring(chat.indexOf(":") + 1);
            String lastMessageContent = temp.substring(temp.indexOf(":") + 2);
            String[] arguments = chat.split(":");
            if (arguments[0].charAt(0) == (char) 28) {
                arguments[0] = arguments[0].substring(1);
            }
            Message msg = new Message(arguments[1], lastMessageContent, Long.parseLong(arguments[0]));
            Date currTime = Date.from(Instant.now());
            Date msgTime = msg.getTimeStamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyDDD");
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            if (dateFormat.format(currTime).equals(dateFormat.format(msgTime))) {
                return timeFormat.format(msgTime);
            }
            int currYearDay = Integer.parseInt(dateFormat.format(currTime));
            int msgYearDay = Integer.parseInt(dateFormat.format(msgTime));
            if (msgYearDay == currYearDay - 1)
            {
                return "Yesterday";
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, msgYearDay);
            SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
            if (((currYearDay - 7) < msgYearDay) && (msgYearDay < (currYearDay - 1)))
            {
                return dayOfWeekFormat.format(calendar.getTime());
            }
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("d MMM yyyy");
            calendar.set(Calendar.DAY_OF_YEAR, msgYearDay);
            return dateFormat1.format(calendar.getTime());
        }
    }

    private void filterChats() throws IOException {
        String searchText = searchField.getText().toLowerCase();
        String username = client.getUsername();
        listModel.clear();
        for (String chat : client.getUserList().split("" + (char) 29)) {
            if (!chat.equals(username) && chat.toLowerCase().contains(searchText)) {
                listModel.addElement(chat);
            }
        }
    }

    @Deprecated
    private void showNewChatDialog() throws IOException {
        String[] users = {};
        users = client.getUserList().split("" + (char) 29);
        JFrame newChatDialog = new JFrame("Select User");
        newChatDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newChatDialog.setSize(550, 100);
        newChatDialog.setLocationRelativeTo(this.getParent());

        JComboBox<String> userComboBox = new JComboBox<>(users);

        JPanel buttonPanel = new JPanel(new FlowLayout(0, 0, 0));

        RoundedButton addFriendButton = new RoundedButton("Add/Remove Friend",15);
        RoundedButton blockButton = new RoundedButton("Block/Unblock",15);
        RoundedButton openChatButton = new RoundedButton("Open Chat",15);
        RoundedButton cancelButton = new RoundedButton("Cancel",15);


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
                return;
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
                return;
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
                return;
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
        RoundedLabel unreadLabel = new RoundedLabel(String.valueOf(count),15);
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
    public void refreshChats(String[] chats) throws IOException {
        if (searchField.getText().trim().isEmpty() && !searching && chatList.isSelectionEmpty()) {
            listModel.clear();
            if (chats != null) {
                for (String chat : chats) {
                    if (chat != null && !chat.equals(client.getUsername())) {
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