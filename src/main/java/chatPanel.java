import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.*;

import javax.swing.plaf.basic.BasicScrollBarUI;

public class chatPanel extends JPanel {
    private RoundedTextArea messageHistoryArea; // prior texts display
    private RoundedTextField messageInputField; // text box to send message
    private RoundedButton sendButton; // send message button
    private Client client; // client object
    private String selectedUser; // The desired user to chat with

    public chatPanel(Client client) {
        this.client = client;

        this.setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setVisible(true);

        messageHistoryArea = new RoundedTextArea(15);
        messageHistoryArea.setEditable(false);
        messageHistoryArea.setFont(new Font("Arial", Font.PLAIN, 14));
        RoundedScrollPane scrollPane = new RoundedScrollPane(messageHistoryArea,15);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageInputField = new RoundedTextField("", 20,15);
        messageInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new RoundedButton("Send",15);
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
    }

    /**
     * Refreshes the message history area with messages between the current user
     * and the selected user.
     */
    public void refreshChat(String selectedUser) throws IOException {
        this.selectedUser = selectedUser;
        String chatHistory = client.getChat(selectedUser);
        if (chatHistory == null || chatHistory.isEmpty()) {
            messageHistoryArea.setText("No messages yet with " + selectedUser + ".");
        } else {
            messageHistoryArea.setText(chatHistory);
        }
        messageHistoryArea.setCaretPosition(messageHistoryArea.getDocument().getLength()); // Auto-scroll to bottom
        this.repaint();
    }

    /**
     * sends a message to the selected user and updates the chat history.
     */
    private void sendMessage() throws IOException {
        String message = messageInputField.getText().trim();
        if (!message.isEmpty() && selectedUser != null) {
            String result = Boolean.toString(client.sendMessage(selectedUser + (char) 29 + message)); // group separator char
            if ("true".equals(result)) {
                refreshChat(selectedUser); // refresh chat after successful message send
                messageInputField.setText(""); // clears input field
            } else {
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
