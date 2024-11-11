import java.io.IOException;

public interface ClientInterface {
    void run();

    String requestData(String command);

    boolean sendCommand(String command);

    boolean login(String username, String password);

    // when sending message, GUI takes that data and send to client, client sends to
    // server, server stores in database, server also send that content to another
    // client
    String getMessage();

    // When receiving the message, server sends content to the receiver's client and
    // client sends data to GUI
    boolean sendMessage(String content);

    boolean removeFriend(String friend);

    boolean addFriend(String friend);

    boolean unblockUser(String user); // Peter do here

    boolean deleteMessage(String content);

    boolean deleteChat(String user);

    boolean sendImage(String otherUsername, String imagePath);

    String accessProfile();

    boolean updateProfile(String content);

    boolean requestActive(String otherUser);

    String openChat(String otherUsername);

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
