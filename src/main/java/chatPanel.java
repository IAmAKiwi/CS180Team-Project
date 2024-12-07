import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.DefaultCaret;

public class chatPanel extends JPanel {
    private RoundedTextArea messageHistoryArea; // prior texts display
    private RoundedScrollPane scrollPane; // pane storing messageHistoryArea
    private RoundedTextField messageInputField; // text box to send message
    private JList<String> currentMessages;
    private RoundedButton sendButton; // send message button
    private Client client; // client object
    private String selectedUser; // The desired user to chat with

    public chatPanel(Client client) {
        this.client = client;
        this.setPreferredSize(new Dimension(400, 500));

        // Add margins to create space on right
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));

        this.setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setVisible(true);

        messageHistoryArea = new RoundedTextArea(15);
        messageHistoryArea.setEditable(false);
        messageHistoryArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageHistoryArea.setLineWrap(true);
        messageHistoryArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        DefaultCaret caret = (DefaultCaret) messageHistoryArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        scrollPane = new RoundedScrollPane(messageHistoryArea, 15);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageInputField = new RoundedTextField("", 20, 15);
        messageInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new RoundedButton("Send", 15);
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 149, 246));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        inputPanel.add(messageInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        this.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();
                } catch (Exception bruh) {
                    // TODO: handle exception
                }
            }
        });

        RoundedButton uploadImageButton = new RoundedButton("Upload Image", 15);
        uploadImageButton.setFont(new Font("Arial", Font.BOLD, 14));
        uploadImageButton.setBackground(new Color(0, 149, 246));
        uploadImageButton.setForeground(Color.WHITE);
        uploadImageButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        inputPanel.add(uploadImageButton, BorderLayout.WEST);
    
        uploadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif"));
                int result = fileChooser.showOpenDialog(chatPanel.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        sendImageMessage(selectedFile);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(chatPanel.this, "Failed to send image.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void sendImageMessage(File imageFile) throws IOException {
        if (selectedUser == null) return;

        // convert the image to Base64
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        // send the image as a special message type
        String result = Boolean.toString(client.sendMessage(selectedUser + (char) 29 + "[IMAGE]" + encodedImage));
        if ("true".equals(result)) {
            refreshChat(selectedUser);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refreshes the message history area with messages between the current user
     * and the selected user.
     */
    public void refreshChat(String selectedUser) throws IOException {
        this.selectedUser = selectedUser;
        String chatHistory = client.getChat(selectedUser);

        if (chatHistory == null || chatHistory.isEmpty()) {
            messageHistoryArea.setText(" No messages yet with " + selectedUser + ".");
            return;
        }

        String[][] messages = getMessageHistory(chatHistory);
        StringBuilder message = new StringBuilder();
        for (int i = messages.length - 1; i > -1; i--) {
            message.append(" ");
            message.append(getMessageTime(messages[i][0]));
            message.append(" ");
            message.append(messages[i][1] + ": " + messages[i][2] + "\n");
        }

        messageHistoryArea.setText(message.toString());
    }

    private String[][] getMessageHistory(String messageContent) {
        if (messageContent == null || messageContent.isEmpty()) {
            return null;
        }
        ArrayList<String[]> messageHistory = new ArrayList<String[]>();
        while (messageContent.contains("" + (char) 29)) {
            messageContent = messageContent.substring(0, messageContent.lastIndexOf((char) 29));
            // Adds the parts of a message (time, user, content) to an array length 3
            if (messageContent.contains("" + (char) 29)) {
                String[] content = new String[3];
                String currentMessage = messageContent.substring(messageContent.lastIndexOf((char) 29) + 1);
                content[0] = currentMessage.substring(0, currentMessage.indexOf((':')));
                currentMessage = currentMessage.substring(currentMessage.indexOf((':')) + 1);
                content[1] = currentMessage.substring(0, currentMessage.indexOf((':')));
                currentMessage = currentMessage.substring(currentMessage.indexOf((':')) + 1);
                content[2] = currentMessage;
                messageHistory.add(content);
            }
        }
        // Adds the final message.
        String[] content = new String[3];
        content[0] = messageContent.substring(0, messageContent.indexOf((':')));
        messageContent = messageContent.substring(messageContent.indexOf((':')) + 1);
        content[1] = messageContent.substring(0, messageContent.indexOf((':')));
        messageContent = messageContent.substring(messageContent.indexOf((':')) + 1);
        content[2] = messageContent;
        messageHistory.add(content);
        // Return the 2d array of messages.
        return messageHistory.toArray(new String[messageHistory.size()][]);
    }

    private String getMessageTime(String timeLong) {
        Message msg = new Message("temp", "temp", Long.parseLong(timeLong));
        Date currTime = Date.from(Instant.now());
        Date msgTime = msg.getTimeStamp();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyDDD");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        if (dateFormat.format(currTime).equals(dateFormat.format(msgTime))) {
            return timeFormat.format(msgTime);
        }
        int currYearDay = Integer.parseInt(dateFormat.format(currTime));
        int msgYearDay = Integer.parseInt(dateFormat.format(msgTime));
        if (msgYearDay == currYearDay - 1) {
            return "Yesterday";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, msgYearDay);
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
        if (((currYearDay - 7) < msgYearDay) && (msgYearDay < (currYearDay - 1))) {
            return dayOfWeekFormat.format(calendar.getTime());
        }
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("d MMM yyyy");
        calendar.set(Calendar.DAY_OF_YEAR, msgYearDay);
        return dateFormat1.format(calendar.getTime());
    }

    /**
     * sends a message to the selected user and updates the chat history.
     */
    private void sendMessage() throws IOException {
        String message = messageInputField.getText().trim();
        if (!message.isEmpty() && selectedUser != null) {
            String result = Boolean.toString(client.sendMessage(selectedUser + (char) 29 + message)); // group separator
                                                                                                      // char
            if ("true".equals(result)) {
                refreshChat(selectedUser); // refresh chat after successful message send
                messageInputField.setText(""); // clears input field
            } else {
                messageInputField.setText("");
                JOptionPane.showMessageDialog(this, "Failed to send message.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Displays a message in the chat panel.
     */
    public void displayMessage(String message) {
        messageHistoryArea.append("\n" + message);
        messageHistoryArea.setCaretPosition(messageHistoryArea.getDocument().getLength()); // Auto-scroll to bottom
    }

    public void refreshChatAutoScroll(String selectedUser) throws IOException {
        DefaultCaret caret = (DefaultCaret) messageHistoryArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        refreshChat(selectedUser);
        caret = (DefaultCaret) messageHistoryArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
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

    private static class RoundedTextArea extends JTextArea {
        private int radius;
        private Color backgroundColor;

        public RoundedTextArea(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }

        public RoundedTextArea(String text, int radius) {
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

            // Draw border
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();

            // Paint text
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Don't paint default border
        }

    }

    private static class RoundedScrollPane extends JScrollPane {
        private int radius;
        private Color backgroundColor;

        public RoundedScrollPane(Component view, int radius) {
            super(view);
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());

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

    // Usage:
    // RoundedScrollPane scrollPane = new RoundedScrollPane(messageHistoryArea, 15);
    // scrollPane.setBackground(Color.WHITE);
}
