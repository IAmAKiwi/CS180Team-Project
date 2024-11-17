import java.util.ArrayList;

/**
 * Interface that defines the required methods for MessageHistory objects.
 * Handles the storage and management of messages between users.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public interface MessageHistoryInterface {
    String[] getUsernames();
    public ArrayList<Message> getMessageHistory();
    public void setMessageHistory(ArrayList<Message> messageHistory);
    public void setUserMessagers(String[] userMessagers);
    void deleteMessage(Message message);
    void addMessage(Message message);
    String getRecipient();
    String getSender();
    boolean equals(Object other);
    String toString();
}
