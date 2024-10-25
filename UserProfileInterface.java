public interface UserProfileInterface {
    // Methods we definitely need
    String getFirstName();
    String getLastName();
    String getBio();
    String getBirthday();
    String getProfilePic();
    String[] getFriends();
    String[] getBlocked();
    void addFriend(String username);
    void removeFriend(String username);
    void block(String username);
    void unblock(String username);
    boolean friendsOnly(); // Returns true if they only take messages from friends, false for all users
    void setFriendsOnly(boolean friendsOnly);
}
