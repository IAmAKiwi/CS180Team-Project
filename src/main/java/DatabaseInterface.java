import java.util.ArrayList;

/**
 * Interface that defines the required methods for Database implementation.
 * Handles data persistence and retrieval for the social media platform.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public interface DatabaseInterface {

    /**
     * Adds a new user to the database.
     *
     * @param user The User object to be added.
     * @return true if the user was added successfully, false otherwise.
     */
    boolean addUser(User user);

    /**
     * Retrieves a list of all users from the database.
     *
     * @return An ArrayList of User objects representing all users.
     */
    ArrayList<User> getUsers();

    /**
     * Retrieves a user from the database by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The User object associated with the specified username, or null if
     * not found.
     */
    User getUser(String username);

    /**
     * Validates a new user before adding them to the database.
     *
     * @param user The User object to be validated.
     * @return true if the user is valid and can be added, false otherwise.
     */
    boolean validateNewUser(User user);

    /**
     * Retrieves the message history between two users.
     *
     * @param user1 The username of the first user.
     * @param user2 The username of the second user.
     * @return A MessageHistory object containing the messages exchanged between the
     * two users.
     * @throws IllegalArgumentException If either user does not exist.
     */
    MessageHistory getMessages(String user1, String user2) throws IllegalArgumentException;


    /**
     * Deletes the message history between two users.
     *
     * @param user1 The username of the first user.
     * @param user2 The username of the second user.
     * @return true if the message history was deleted successfully, false otherwise.
     */
     boolean deleteChat(String user1, String user2);

    /**
     * Saves the current list of users to persistent storage.
     *
     * @return true if users were saved successfully, false otherwise.
     */
    boolean saveUsers();

    /**
     * Saves the current messages to persistent storage.
     *
     * @return true if messages were saved successfully, false otherwise.
     */
    boolean saveMessages();

    /**
     * Loads the users from persistent storage into the database.
     *
     * @return true if users were loaded successfully, false otherwise.
     */
    boolean loadUsers();

    /**
     * Loads messages from persistent storage into the application.
     *
     * @return true if messages were loaded successfully, false otherwise.
     */
    boolean loadMessages();

    /**
     * Retrieves a list of all chat histories in the database.
     *
     * @return An ArrayList of MessageHistory objects representing all chats.
     */
    ArrayList<MessageHistory> getAllChats();

    /**
     * Adds a message history record to the database.
     *
     * @param messageHistory The MessageHistory object to be added.
     * @return true if the message history was added successfully, false otherwise.
     */
    boolean addMessageHistory(MessageHistory messageHistory);

    /**
     * Adds a new message to the database.
     * Will create a new MessageHistory object if one does not exist.
     *
     * @param message  The Message object to be added.
     * @param receiver The username of the receiver of the message.
     * @return true if the message was added successfully, false otherwise.
     */
    public boolean addMessage(Message message, String receiver);

    /**
     * Loads photos associated with users or messages from persistent storage.
     *
     * @return true if photos were loaded successfully, false otherwise.
     */
    boolean loadPhotos();

    /**
     * Saves photos associated with users or messages to persistent storage.
     *
     * @return true if photos were saved successfully, false otherwise.
     */
    boolean savePhotos();

    /**
     * Adds a new photo path to the database.
     *
     * @param path The file path of the photo to be added.
     */
    void addPhotos(String path);

    /**
     * Displays a photo from a given path.
     *
     * @param path The file path of the photo to be displayed.
     */
    void displayPhotos(String path);

    /**
     * Sets the list of photo paths associated with the database.
     *
     * @param photosPath An ArrayList of photo paths to be set.
     */
    void setPhotos(ArrayList<String> photosPath);

    /**
     * Retrieves the list of photo paths associated with the database.
     *
     * @return An ArrayList of strings representing the photo paths.
     */
    ArrayList<String> getPhotos();

    /**
     * Sets the list of users for the database.
     *
     * @param userList An ArrayList of User objects to be set.
     */
    void setUsersList(ArrayList<User> userList);

    /**
     * Sets the list of all chat histories for the database.
     *
     * @param allChats An ArrayList of MessageHistory objects to be set.
     */
    void setAllChats(ArrayList<MessageHistory> allChats);

    /**
     * Adds a friend to the database.
     *
     * @param user1 The username of the first user.
     * @param user2 The username of the second user.
     * @return true if the friend was added successfully, false otherwise.
     */
    boolean addFriend(String user1, String user2);

    /**
     * Retrieves a list of friends for a user.
     *
     * @param username The username of the user.
     * @return An array of strings representing the usernames of the user's friends.
     */
    String[] getFriends(String username);

    /**
     * Removes a friend from the database.
     *
     * @param user1 The username of the first user.
     * @param user2 The username of the second user.
     * @return true if the friend was removed successfully, false otherwise.
     */
    boolean removeFriend(String user1, String user2);

    /**
     * Blocks a user in the database.
     *
     * @param user1 The username of the first user.
     * @param user2 The username of the second user.
     * @return true if the user was blocked successfully, false otherwise.
     */
    boolean blockUser(String user1, String user2);

    /**
     * Retrieves a list of blocked users for a user.
     *
     * @param username The username of the user.
     * @return An array of strings representing the usernames of the user's blocked users.
     */
    String[] getBlockList(String username);

    /**
     * Unblocks a user in the database.
     *
     * @param user1 The username of the first user.
     * @param user2 The username of the second user.
     * @return true if the user was unblocked successfully, false otherwise.
     */
    boolean unblockUser(String user1, String user2);
}
