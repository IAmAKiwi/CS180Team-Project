public interface ServerInterface {
    public boolean login(String username, String password);
    public boolean register(String username, String password);
    public boolean sendMessage(String otherUsername);
    public boolean sendImage(String otherUsername, String image);
    public boolean openChat(String otherUsername);

    public boolean addFriend(String otherUsername);
    public String[] getFriendList();
    public boolean addBlock(String otherUsername);
    public String[] getBlockList();
    public boolean removeFriend(String otherUsername);
    public boolean removeBlock(String otherUsername);

    public boolean isFriendsOnly(String otherUsername);
    public boolean setFriendsOnly(boolean friendsOnly);

    public boolean setProfilePic(String profilePic);

    public boolean logout();

}
