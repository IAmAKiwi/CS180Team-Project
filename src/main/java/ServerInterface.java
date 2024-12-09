import java.io.PrintWriter;

public interface ServerInterface {

    String login(String content);

    String register(String content);

    String getUserList();

    String getChat(String content);

    String getMessages(String content);

    String getChatList();

    String createChat(String content);

    String sendMessage(String content);

    String deleteMessage(String content);

    String accessProfile();

    String saveProfile(String content);

    String deleteChat(String content);

    String sendImage(String content);

    String addFriend(String otherUsername);

    String getFriendList();

    String blockUser(String otherUsername);

    String getBlockList();

    String removeFriend(String otherUsername);

    String unblockUser(String otherUsername);

    String isFriendsOnly();

    String setFriendsOnly(boolean friendsOnly);

    String setProfilePic(String profilePic);

    String getProfilePic();

    String logout();

    boolean disconnect();

    void send(String result, PrintWriter writer);
}
