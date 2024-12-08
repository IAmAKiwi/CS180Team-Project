
/**
 * Interface that defines the required methods for Server implementation.
 * Specifies the contract for handling client requests and managing server
 * operations.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
import java.io.PrintWriter;

public interface ServerInterface {
    // Core server operations
    String getUsername();

    String login(String content);

    String isLoggedIn();

    String register(String content);

    String getUserList();

    String getChat(String content);

    String getChatList();

    String createChat(String content);

    String sendMessage(String content);

    String deleteMessage(String content);

    String accessProfile();

    String accessUserProfile(String user);

    String accessPhotosFromUser(String user);

    String accessMessagesFromUser(String user);

    String saveProfile(String content);

    String removeFriend(String otherUsername);

    String addFriend(String otherUsername);

    String unblockUser(String otherUsername);

    String blockUser(String otherUsername);

    String deleteChat(String user);

    String sendImage(String content);

    String getBlockList();

    String getFriendList();

    String isFriendsOnly();

    String setFriendsOnly(boolean friendsOnly);

    String setProfilePic(String profilePic);

    String getProfilePic();

    String logout();

    boolean disconnect();

    // Optional utility methods for use in implementations
    default void send(String result, PrintWriter writer) {
        writer.println(result);
        writer.flush();
    }
}
