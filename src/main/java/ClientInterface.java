import java.io.IOException;

public interface ClientInterface {
    void run();

    String requestData(String command) throws IOException;

    boolean boolCommand(String command) throws IOException;

    boolean login(String username, String password);


    boolean receiveMessage();

    boolean sendMessage();

    boolean removeFriend();

    boolean addFriend();

    boolean unblockUser(); // Peter do here

    boolean deleteMessage();

    boolean accessingProfile();

    boolean updateProfile();

    boolean requestActive();

    boolean deleteChat();

    public boolean sendImage(String otherUsername, String image);

    public boolean openChat(String otherUsername);
    // changed from String[] to String because requesting Data only receieve String
    public String[] getFriendList();

    public boolean addBlock(String otherUsername);

    public String[] getBlockList();

    public boolean isFriendsOnly(String otherUsername);

    public boolean setFriendsOnly(boolean friendsOnly);

    public boolean setProfilePic(String profilePic);

    public boolean logout();
}
