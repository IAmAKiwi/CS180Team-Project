import java.io.IOException;

/**
 * Interface that handles all of the client-side functionality.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface ClientInterface {
    // Methods for managing profile data
    boolean updateProfile(String content) throws IOException;

    boolean saveProfile(String content) throws IOException;

    boolean setProfilePic(String profilePic) throws IOException;

    String getProfilePic() throws IOException;

    // Methods for user information and profile
    String getUsername() throws IOException;

    String accessProfile() throws IOException;

    String accessUserProfile(String username) throws IOException;

    // Methods for managing friendships
    boolean addFriend(String friend) throws IOException;

    boolean removeFriend(String friend) throws IOException;

    String getFriendList() throws IOException;

    // Methods for managing blocks
    boolean blockUser(String username) throws IOException;

    boolean unblockUser(String username) throws IOException;

    String getBlockList() throws IOException;

    // Methods for messages
    boolean sendMessage(String content) throws IOException;

    boolean deleteMessage(String content) throws IOException;

    String getChat(String otherUsername) throws IOException;

    String getChatList() throws IOException;

    boolean createChat(String otherUsername) throws IOException;

    boolean deleteChat(String otherUsername) throws IOException;

    // Methods for handling user photos and messages
    String accessPhotosFromUser(String username) throws IOException;

    String accessMessagesFromUser(String username) throws IOException;

    // Methods for account management
    boolean login(String credentials) throws IOException;

    boolean register(String credentials) throws IOException;

    boolean isLoggedIn() throws IOException;

    boolean logout() throws IOException;

    boolean disconnect();

    // Privacy settings
    boolean isFriendsOnly() throws IOException;

    boolean setFriendsOnly(String booleanValue) throws IOException;
}