public interface ClientInterface {
    void run();

    String requestData(String command);

    boolean sendCommand(String command);

    boolean login(String content);

    boolean register(String content);

    boolean logout();

    boolean disconnect();

    String getUserList();

    String accessProfile();

    boolean saveProfile(String content);

    boolean sendMessage(String content);

    boolean deleteMessage(String content);

    String getChat(String otherUsername);

    boolean deleteChat(String user);

    boolean sendImage(String content);

    boolean removeFriend(String friend);

    boolean addFriend(String friend);

    String getFriendList();

    boolean unblockUser(String user);

    boolean blockUser(String otherUsername);

    String getBlockList();

    boolean isFriendsOnly();

    boolean setFriendsOnly(String booleanValue);

    boolean setProfilePic(String profilePic);

    String getProfilePic();

    boolean requestActive(String otherUser);
}
