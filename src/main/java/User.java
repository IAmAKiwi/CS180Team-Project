import java.time.Year;
import java.util.ArrayList;

/**
 * Class that contains all information that a User of
 * the social media platform could store, including
 * the username and password.
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * 
 * @version Nov 2, 2024
 */
public class User implements UserInterface {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String bio;
    private int[] birthday;
    private String profilePic;
    private ArrayList<String> friends;
    private ArrayList<String> blocked;
    private boolean friendsOnly;

    /**
     * Default constructor
     */
    public User() {
        this.username = null;
        this.password = null;
        firstName = null;
        lastName = null;
        bio = null;
        birthday = null;
        profilePic = null;
        friends = new ArrayList<String>();
        blocked = new ArrayList<String>();
    }

    /**
     * Typical barebones constructor
     * 
     * @param username The username used to construct the new User.
     * @param password The password used to construct the new User.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        firstName = null;
        lastName = null;
        bio = null;
        birthday = null;
        profilePic = null;
        friends = new ArrayList<String>();
        blocked = new ArrayList<String>();
        friendsOnly = false;
    }

    /**
     * More comprehensive constructor for use in loadUsers from Database.
     * Use nulls in uninitiatied parameters.
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param bio
     * @param birthday
     * @param profilePic
     * @param friends
     * @param blocked
     * @param friendsOnly
     */
    public User(String username, String password, String firstName, String lastName,
        String bio, int[] birthday, String profilePic, ArrayList<String> friends,
        ArrayList<String> blocked, boolean friendsOnly) throws IllegalArgumentException
    {
        if (birthday != null)
        {
            if (birthday.length != 3)
            {
                throw new IllegalArgumentException("Illegal size of \"birthday\" parameter");
            }
        }
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.setBirthday(birthday);
        this.profilePic = profilePic;
        if (friends.equals(null))
        {
            this.friends = new ArrayList<String>();
        } else this.friends = friends;
        if (blocked.equals(null))
        {
            this.blocked = new ArrayList<String>();
        } else this.blocked = blocked;
        this.friendsOnly = friendsOnly;
    }

    public String toString()
    {
        int birthdayVal0;
        int birthdayVal1;
        int birthdayVal2;
        if (this.birthday.length != 3)
        {
            birthdayVal0 = 0;
            birthdayVal1 = 0;
            birthdayVal2 = 0;
        } else
        {
            birthdayVal0 = this.birthday[0];
            birthdayVal1 = this.birthday[1];
            birthdayVal2 = this.birthday[2];
        }
        return String.format("%s,%s,%s,%s,%s,%d/%d/%d,%s,%s,%s,%b",
            this.username, this.password, this.firstName, this.lastName,
            this.bio, birthdayVal0, birthdayVal1, birthdayVal2, this.profilePic,
            this.friends.toString(), this.blocked.toString(), this.friendsOnly);
    }

    // After here are simple constructors that define
    // getters and setters for the various fields of User,
    // or adders and removers for the ArrayList fields,
    // save the setBirthday method, which checks for validity
    // before completing the set.

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int[] getBirthday() {
        return birthday;
    }

    /*
     * Month, Day, Year.
     */
    public void setBirthday(int[] birthday) {
        if (birthday.length != 3) {
            return;
        }
        if (birthday[0] < 1 || birthday[0] > 12) {
            return;
        }
        if (birthday[1] < 1 || birthday[1] > 31) {
            return;
        }
        if (Year.now().getValue() < birthday[2]) {
            return;
        }
        this.birthday = birthday;
    }

    public String getProfilePic() {
        return profilePic;
    }

    // public void setProfilePic(String profilePic) {
    // this.profilePic = profilePic;
    // }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getBlocked() {
        return blocked;
    }

    public void setBlocked(ArrayList<String> blocked) {
        this.blocked = blocked;
    }

    public void addFriend(String username) {
        friends.add(username);
    }

    public void removeFriend(String username) {
        friends.remove(username);
    }

    public void addBlock(String username) {
        blocked.add(username);
    }

    public void unblock(String username) {
        blocked.remove(username); // double check if this is right
    }

    public boolean isFriendsOnly() {
        return friendsOnly;
    }

    public void setFriendsOnly(boolean friendsOnly) {
        this.friendsOnly = friendsOnly;
    }

}
