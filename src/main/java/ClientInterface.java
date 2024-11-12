
public interface ClientInterface {
    void run();

    String requestData(String command); // General case for all requests

    boolean sendCommand(String command); // General case for all commands

    boolean login(String username, String password); // Login command

    boolean sendMessage(String content); // Update message history. otherUser:message

    boolean removeFriend(String friend); // remove friend

    boolean addFriend(String friend); // add friend

    boolean unblockUser(String user); // unblock user

    boolean deleteMessage(String content); // delete message, otherUser:message

    boolean deleteChat(String user); // delete chat, otherUser

    boolean sendImage(String otherUsername, String imagePath); // Send image. otherUser:imagePath

    String accessProfile(); // get profile information

    boolean updateProfile(String content); // update profile information very complicated format!!!

    boolean requestActive(String otherUser);

    String getChat(String otherUsername); // Open a messageHistory, get

    String getFriendList();

    boolean blockUser(String otherUsername);

    String getBlockList();

    boolean isFriendsOnly(String otherUsername);

    boolean setFriendsOnly(String booleanValue);

    boolean setProfilePic(String profilePic);

    String getProfilePic();

    public boolean logout();

    public boolean disconnect();
}
