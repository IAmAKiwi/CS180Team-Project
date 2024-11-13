public interface ServerInterface {
    public String login(String content);

    public String register(String content);

    public String sendMessage(String otherUsername);

    public String sendImage(String content);

    public String openChat(String otherUsername);

    public String addFriend(String otherUsername);

    public String getFriendList();

    public String blockUser(String otherUsername);

    public String getBlockList();

    public String removeFriend(String otherUsername);

    public String removeBlock(String otherUsername);

    public String isFriendsOnly();

    public String setFriendsOnly(boolean friendsOnly);

    public String setProfilePic(String profilePic);

    public String logout();

}
