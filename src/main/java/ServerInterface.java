public interface ServerInterface {
    public String login(String username, String password);
    public String register(String username, String password);
    public String sendMessage(String otherUsername);
    public String sendImage(String otherUsername, String image);
    public String openChat(String otherUsername);

    public String addFriend(String otherUsername);
    public String getFriendList();
    public String addBlock(String otherUsername);
    public String getBlockList();
    public String removeFriend(String otherUsername);
    public String removeBlock(String otherUsername);

    public String isFriendsOnly(String otherUsername);
    public String setFriendsOnly(boolean friendsOnly);

    public String setProfilePic(String profilePic);

    public String logout();

}
