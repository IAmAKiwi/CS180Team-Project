import java.io.IOException;

/**
 * Interface that handles all of the client-side functionality.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface ClientInterface {
    void run(); // to be overridden in GUI

    // User authentication
    boolean login(String content) throws IOException; // username [groupSeparator] password

    boolean register(String content) throws IOException; // username [groupSeparator] password

    boolean logout() throws IOException;

    boolean disconnect() throws IOException;

    // User management
    String getUserList() throws IOException; // username [groupSeparator] username [groupSeparator] etc

    String accessProfile() throws IOException; // username [groupSeparator] first name [groupSeparator] last name
                                               // [groupSeparator] etc

    boolean saveProfile(String content) throws IOException; // username [groupSeparator] first name [groupSeparator] etc (above)

    // Messaging
    boolean sendMessage(String content) throws IOException; // otherUsername [groupSeparator] message

    boolean deleteMessage(String content) throws IOException; // otherUsername [groupSeparator] message

    String getChat(String otherUsername) throws IOException; // otherUsername

    boolean deleteChat(String user) throws IOException; // otherUsername

    boolean sendImage(String content) throws IOException; // otherUsername [groupSeparator] path

    // Friend management
    boolean removeFriend(String friend) throws IOException; // friendUsername

    boolean addFriend(String friend) throws IOException; // friendUsername

    String getFriendList() throws IOException; // friendUsername [groupSeparator] friendUsername [groupSeparator] etc

    // Block management
    boolean unblockUser(String user) throws IOException; // blockedUsername

    boolean blockUser(String otherUsername) throws IOException; // blockedUsername

    String getBlockList() throws IOException; // blockedUsername [groupSeparator] blockedUsername [groupSeparator] etc

    // Settings
    boolean isFriendsOnly() throws IOException;

    boolean setFriendsOnly(String booleanValue) throws IOException; // "true" or "false"

    boolean setProfilePic(String profilePic) throws IOException; // path

    String getProfilePic() throws IOException;
}
