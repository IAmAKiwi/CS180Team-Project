import java.io.IOException;

public interface ClientInterface {
    void run();

    String requestData(String command) throws IOException;

    boolean boolCommand(String command) throws IOException;

    boolean login(String username, String password);

    // when sending message, GUI takes that data and send to client, client sends to
    // server, server stores in database, server also send that content to another
    // client
    boolean receiveMessage();

    // When receiving the message, server sends content to the receiver's client and
    // client sends data to GUI
    boolean sendMessage();

    boolean removeFriend();

    boolean addFriend();

    boolean unblockUser();

    boolean deleteMessage();

    boolean sendPhoto();

    boolean accessingProfile();

    boolean updateProfile();

    boolean requestActive();

    boolean deleteChat();

    public boolean sendMessage(String otherUsername);

    public boolean sendImage(String otherUsername, String image);

    public boolean openChat(String otherUsername);

    public String[] getFriendList();

    public boolean addBlock(String otherUsername);

    public String[] getBlockList();

    public boolean isFriendsOnly(String otherUsername);

    public boolean setFriendsOnly(boolean friendsOnly);

    public boolean setProfilePic(String profilePic);

    public boolean logout();
}
