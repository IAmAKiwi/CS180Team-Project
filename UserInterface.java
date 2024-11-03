import java.util.ArrayList;

/**
 * Interface for User
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 *
 * @version Nov 2, 2024
 */
public interface UserInterface {
    // Methods we definitely need
    String getUsername();
    String getPassword();

    //Methods we might need (depends on implementation)
    void setUsername(String username);
    void setPassword(String password);
    // void setProfile(UserProfile profile);
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setBio(String bio);
    void setBirthday(int[] birthday);
    // void setProfilePic();
    
    String getFirstName();
    String getLastName();
    String getBio();
    int[] getBirthday();
    // String getProfilePic();
    ArrayList<String> getFriends();
    ArrayList<String> getBlocked();
    void addFriend(String username);
    void addBlock(String username);
    void removeFriend(String username);
    // void block(String username);
    // void unblock(String username);
    boolean isFriendsOnly(); // Returns true if they only take messages from friends, false for all users
    void setFriendsOnly(boolean friendsOnly);
}

