import java.util.ArrayList;

/**
 * Interface defining database operations for social media platform
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public interface DatabaseInterface {


    // User Management
    boolean addUser(User user);
    boolean validateNewUser(User user);
    User getUser(String username);
    ArrayList<User> getUsers();
    void setUsersList(ArrayList<User> userList);
    boolean addFriend(String user1, String user2);
    boolean removeFriend(String user1, String user2);
    boolean blockUser(String user1, String user2);
    boolean unblockUser(String user1, String user2);
    String[] getFriends(String username);
    String[] getBlockList(String username);
    ArrayList<Message> getAllMessagesFromUser(User u);

    // Message Operations
    ArrayList<MessageHistory> getAllChats();
    MessageHistory getMessages(String user1, String user2);
    boolean addMessageHistory(MessageHistory messageHistory);
    boolean addMessage(Message message, String receiver);
    boolean deleteMessage(Message message, String receiver);
    boolean deleteChat(String user1, String user2);
    String[] getAllUserChats(String username);
    void setAllChats(ArrayList<MessageHistory> allChats);

    // File Operations
    boolean saveUsers();
    boolean loadUsers();
    boolean saveMessages();
    boolean loadMessages();
}