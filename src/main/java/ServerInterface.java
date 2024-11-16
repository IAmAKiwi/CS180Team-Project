public interface ServerInterface {
    // User authentication
    String login(String content); // username [groupSeparator] password
    String register(String content); // username [groupSeparator] password
    String logout();
    boolean disconnect();
    
    // User management
    String getUserList(); // username [groupSeparator] username [groupSeparator] etc
    String accessProfile(); // username [groupSeparator] first name [groupSeparator] etc
    String saveProfile(String content); // username [groupSeparator] first name [groupSeparator] etc (above)
    
    // Messaging
    String sendMessage(String content); // otherUsername [groupSeparator] message
    String deleteMessage(String content); // otherUsername [groupSeparator] message
    String getChat(String otherUsername); // otherUsername
    String deleteChat(String user); // otherUsername
    String sendImage(String content); // path
    
    // Friend management
    String removeFriend(String friend); // friendUsername
    String addFriend(String friend); // friendUsername
    String getFriendList(); // username [groupSeparator] username [groupSeparator] etc
    
    // Block management
    String unblockUser(String user); // blockUsername
    String blockUser(String otherUsername); // blockUsername
    String getBlockList(); // username [groupSeparator] username [groupSeparator] etc
    
    // Settings
    String isFriendsOnly(); // true or false
    String setFriendsOnly(boolean friendsOnly); // true or false
    String setProfilePic(String profilePic); // path
    String getProfilePic(); // path
}
