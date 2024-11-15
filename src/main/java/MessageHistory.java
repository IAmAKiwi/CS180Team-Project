import java.util.ArrayList;

/**
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * 
 * @version Nov 2, 2024
 */
public class MessageHistory implements MessageHistoryInterface {
    private String[] userMessagers;
    private ArrayList<Message> messageHistory; // in each element of the arraylist will contain username: content
                                               // content

    /**
     * Default constructor
     */
    public MessageHistory() {
        userMessagers = null;
        messageHistory = new ArrayList<Message>();
    }

    /**
     * Typical constructor for a new conversation between two users.
     * 
     * @param message   First sent message in the new conversation. Contains one of
     *                  the users' usernames.
     * @param recipient The second messager's username, the receipient of the first
     *                  message in the conversation.
     */
    public MessageHistory(Message message, String recipient) {
        userMessagers = new String[2];
        messageHistory = new ArrayList<Message>();
        userMessagers[1] = recipient;
        userMessagers[0] = message.getSender();
        messageHistory.add(message);
    }

    public String getRecipient() {
        return userMessagers[1];
    }

    public String getSender() {
        return userMessagers[0];
    }

    /**
     * Alternate constructor that supports more than two users.
     * 
     * @param users Array of strings representing some Users' usernames.
     */
    public MessageHistory(String[] users) {
        userMessagers = users;
        messageHistory = new ArrayList<Message>();
    }

    public void deleteMessage(Message message) {
        messageHistory.remove(message);
    }

    // Getter for messageHistory
    public ArrayList<Message> getMessageHistory() {
        return messageHistory;
    }

    // Getter for userMessagers
    public String[] getUsernames() {
        return userMessagers;
    }

    /**
     * Adds a message to the message history
     * 
     * @param message Message to be added
     */
    public void addMessage(Message message) {
        messageHistory.add(message);
    }

    // idk if we need these yet
    // idk if we need these setters yet

    public void setMessageHistory(ArrayList<Message> messageHistory) {
        this.messageHistory = messageHistory;
    }

    public void setUserMessagers(String[] userMessagers) {
        this.userMessagers = userMessagers;
    }

    /**
     * Checks if two MessageHistories are equal (if they are between the same two
     * users).
     * 
     * @param other
     * @return true if they are between the same users.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (!(other instanceof MessageHistory)) {
            return false;
        }

        MessageHistory mh = (MessageHistory) other;
        // Check if both users match (in either order)
        return (mh.userMessagers[0].equals(this.userMessagers[0]) && mh.userMessagers[1].equals(this.userMessagers[1]))
                ||
                (mh.userMessagers[0].equals(this.userMessagers[1])
                        && mh.userMessagers[1].equals(this.userMessagers[0]));
    }

    /**
     * Gives the file formatted string for this MessageHistory. Only the two
     * usernames.
     * Each message must be looped through to get individual toStrings.
     */
    @Override
    public String toString() {
        return String.format("%s %s", this.userMessagers[0], this.userMessagers[1]);
    }
}