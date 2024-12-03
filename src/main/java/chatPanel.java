import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class chatPanel extends JPanel {
    private JTextArea messageHistoryArea; // prior texts display
    private JTextField messageInputField; // text box to send message
    private JButton sendButton; // send message button
    private Client client; // client object
    private String selectedUser; // The desired user to chat with

    public chatPanel(Client client) {
        this.client = client;

        this.setLayout(new BorderLayout());

        messageHistoryArea = new JTextArea();
        messageHistoryArea.setEditable(false);
        messageHistoryArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageHistoryArea);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageInputField = new JTextField();
        messageInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new JButton("Send");
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
}
