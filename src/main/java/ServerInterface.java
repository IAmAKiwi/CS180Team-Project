/**
 * Interface that defines the required methods for Server implementation.
 * Specifies the contract for handling client requests and managing server operations.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public interface ServerInterface {
    // User authentication
    String login(String content);
    String register(String content);
    String logout();
    boolean disconnect();
    
    // User management
    String getUserList();
    String accessProfile();
    String saveProfile(String content);
    
    // Messaging
    String sendMessage(String content);
    String deleteMessage(String content);
    String getChat(String otherUsername);
    String deleteChat(String user);
    String sendImage(String content);
    
    // Friend management
    String removeFriend(String friend);
    String addFriend(String friend);
    String getFriendList();
    
    // Block management
    String unblockUser(String user);
    String blockUser(String otherUsername);
    String getBlockList();
    
    // Settings
    String isFriendsOnly();
    String setFriendsOnly(boolean friendsOnly);
    String setProfilePic(String profilePic);
    String getProfilePic();
}
