import java.time.Year;
import java.util.ArrayList;
/**
 * Class that contains all information that a User of
 * the social media platform could store, including
 * the username and password.
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
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

        //After here are simple constructors that define
        //getters and setters for the various fields of User,
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
        //     this.profilePic = profilePic;
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
