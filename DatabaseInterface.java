import java.util.ArrayList;

public interface DatabaseInterface {
    // Methods we definitely need
    boolean addUser(User user);
    ArrayList<User> getUsers();
    User getUser(String username);
    boolean validateNewUser(User user);

    //void addMessage(Message message); // Could change based upon how we implement message/message history.
        // Depends on whether message holds the recipients.
        // Current implementation does not require this. Instead use
        // getMessages a MessageHistory and addMessage there.
    MessageHistory getMessages(String user1, String user2) throws IllegalArgumentException;

    boolean saveUsers();
    boolean saveMessages();
    boolean loadUsers();
    boolean loadMessages();

}
