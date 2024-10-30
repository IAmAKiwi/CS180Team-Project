import java.util.ArrayList;

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
    
        public void setBirthday(int[] birthday) {
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

        public boolean friendsOnly() {
            return friendsOnly;
        }

        public void setFriendsOnly(boolean friendsOnly) {
            this.friendsOnly = friendsOnly;
        }
        

    }
