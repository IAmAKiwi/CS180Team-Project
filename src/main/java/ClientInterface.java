/**
 * Interface that handles all of the client-side functionality.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 *
 * @version Nov 2, 2024
 */
public interface ClientInterface {
    void run(); // to be overridden in GUI

    // User authentication
    boolean login(String content); // username [groupSeparator] password
    boolean register(String content); // username [groupSeparator] password
    boolean logout();
    boolean disconnect();

    // User management
    String getUserList(); // username [groupSeparator] username [groupSeparator] etc
    String accessProfile(); // username [groupSeparator] first name [groupSeparator] last name [groupSeparator] etc
    boolean saveProfile(String content); // username [groupSeparator] first name [groupSeparator] etc (above)

    // Messaging
    boolean sendMessage(String content); // otherUsername [groupSeparator] message
    boolean deleteMessage(String content); // otherUsername [groupSeparator] message
    String getChat(String otherUsername); // otherUsername
    boolean deleteChat(String user); // otherUsername
    boolean sendImage(String content); // otherUsername [groupSeparator] path

    // Friend management
    boolean removeFriend(String friend); // friendUsername
    boolean addFriend(String friend); // friendUsername
    String getFriendList(); // friendUsername [groupSeparator] friendUsername [groupSeparator] etc

    // Block management
    boolean unblockUser(String user); // blockedUsername
    boolean blockUser(String otherUsername); // blockedUsername
    String getBlockList(); // blockedUsername [groupSeparator] blockedUsername [groupSeparator] etc

    // Settings
    boolean isFriendsOnly();
    boolean setFriendsOnly(String booleanValue); // "true" or "false"
    boolean setProfilePic(String profilePic); // path
    String getProfilePic();
}
