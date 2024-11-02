import java.util.ArrayList;
/**
 * Class that stores messages between two (or more if implemented) users.
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * 
 * @version Nov 2, 2024
 */
public class MessageHistory implements MessageHistoryInterface {
    private String[] userMessagers; // the users involved in the conversation represented by their usernames
    private ArrayList<Message> messageHistory; // each Message element of the arraylist will contain username and content
    /**
     * Default constructor
     */
    public MessageHistory() {
        userMessagers = null;
        messageHistory = null;
    }

    /**
     * Typical constructor for a new conversation between two users.
     * @param message First sent message in the new conversation. Contains one of the users' usernames.
     * @param recipient The second messager's username, the receipient of the first message in the conversation.
     */
    public MessageHistory(Message message, String recipient) {
        userMessagers = new String[2];
        userMessagers[0] = recipient;
        userMessagers[1] = message.getSender();
        messageHistory = new ArrayList<Message>();
        messageHistory.add(message);
    }

    /**
     * Alternate constructor that supports more than two users.
     * @param users Array of strings representing some Users' usernames.
     */
    public MessageHistory(String[] users) {
        userMessagers = users;
        messageHistory = new ArrayList<Message>();
    }

    // Getter for messageHistory
    public ArrayList<Message> getMessageHistory() {
        return this.messageHistory;
    }

    // Getter for userMessagers
    public String[] getUsernames() {
        return this.userMessagers;
    }

    //idk if we need these setters yet

    public void setMessageHistory(ArrayList<Message> messageHistory) {
        this.messageHistory = messageHistory;
    }

    public void setUserMessagers(String[] userMessagers) {
        this.userMessagers = userMessagers;
    }

    /**
     * Gives the file formatted string for this MessageHistory. Only the two usernames.
     * Each message must be looped through to get individual toStrings.
     */
    @Override
    public String toString() {
        return String.format("%s %s", this.userMessagers[0], this.userMessagers[1]);
    }
}
