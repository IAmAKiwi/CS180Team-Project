public interface ClientInterface {
    void run();

    String requestData(String command); // General case for all requests

    boolean sendCommand(String command); // General case for all commands

    boolean login(String content); // Login command with combined username:password

    String getUserList();

    boolean sendMessage(String content); // Update message history. otherUser:message

    boolean removeFriend(String friend); // remove friend

    boolean addFriend(String friend); // add friend

    boolean unblockUser(String user); // unblock user

    boolean deleteMessage(String content); // delete message, otherUser:message

    boolean deleteChat(String user); // delete chat, otherUser

    boolean sendImage(String content); // Send image. otherUser:imagePath

    String accessProfile(); // get profile information

    boolean saveProfile(String[] content); // update profile information very complicated format!!!

    boolean requestActive(String otherUser);

    String getChat(String otherUsername); // Open a messageHistory, get

    String getFriendList();

    boolean blockUser(String otherUsername);

    String getBlockList();

    boolean isFriendsOnly();

    boolean setFriendsOnly(String booleanValue);

    boolean setProfilePic(String profilePic);

    String getProfilePic();

    boolean register(String content); // Register command with combined username:password

    boolean logout();

    boolean disconnect();
}
