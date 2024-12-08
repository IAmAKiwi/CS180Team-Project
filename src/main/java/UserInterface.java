import java.util.ArrayList;

/**
 * Interface that defines the required methods for User objects.
 * Specifies the contract for managing user data and operations.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface UserInterface {
    /**
     * Gets the username of the user.
     *
     * @return The username as a String
     */
    String getUsername();

    /**
     * Sets a new username for the user.
     *
     * @param newUsername The new username to set
     */
    void setUsername(String newUsername);

    /**
     * Gets the user's password.
     *
     * @return The password as a String
     */
    String getPassword();

    /**
     * Sets a new password for the user.
     *
     * @param newPassword The new password to set
     */
    void setPassword(String newPassword);

    /**
     * Gets the user's first name.
     *
     * @return The first name as a String
     */
    String getFirstName();

    /**
     * Sets the user's first name.
     *
     * @param firstName The new first name to set
     */
    void setFirstName(String firstName);

    /**
     * Gets the user's last name.
     *
     * @return The last name as a String
     */
    String getLastName();

    /**
     * Sets the user's last name.
     *
     * @param lastName The new last name to set
     */
    void setLastName(String lastName);

    /**
     * Gets the user's bio.
     *
     * @return The bio as a String
     */
    String getBio();

    /**
     * Sets the user's bio.
     *
     * @param bio The new bio to set
     */
    void setBio(String bio);

    /**
     * Gets the user's birthday.
     *
     * @return The birthday as an array of integers
     */
    int[] getBirthday();

    /**
     * Sets the user's birthday.
     *
     * @param birthday The new birthday to set
     */
    void setBirthday(int[] birthday);

    /**
     * Gets the user's profile picture.
     *
     * @return The profile picture as a String
     */
    String getProfilePic();

    /**
     * Sets the user's profile picture.
     *
     * @param profilePic The new profile picture to set
     */
    void setProfilePic(String profilePic);

    /**
     * Gets the list of friends.
     *
     * @return ArrayList of friend usernames
     */
    ArrayList<String> getFriends();

    /**
     * Sets the list of friends.
     *
     * @param friends The new list of friends to set
     */
    void setFriends(ArrayList<String> friends);

    /**
     * Gets the list of blocked users.
     *
     * @return ArrayList of blocked usernames
     */
    ArrayList<String> getBlocked();

    /**
     * Sets the list of blocked users.
     *
     * @param blocked The new list of blocked users to set
     */
    void setBlocked(ArrayList<String> blocked);

    /**
     * Adds a friend to the user's friend list.
     *
     * @param friendUsername The username of the friend to add
     */
    void addFriend(String friendUsername);

    /**
     * Removes a friend from the user's friend list.
     *
     * @param friendUsername The username of the friend to remove
     */
    void removeFriend(String friendUsername);

    /**
     * Blocks another user.
     *
     * @param blockedUsername The username of the user to block
     */
    void addBlock(String blockedUsername);

    /**
     * Unblocks a previously blocked user.
     *
     * @param blockedUsername The username of the user to unblock
     */
    void unblock(String blockedUsername);

    /**
     * Checks if the user's profile is set to friends-only mode.
     *
     * @return true if profile is friends-only, false otherwise
     */
    boolean isFriendsOnly();

    /**
     * Sets the friends-only mode for the user's profile.
     *
     * @param friendsOnly true to enable friends-only mode, false to disable
     */
    void setFriendsOnly(boolean friendsOnly);

    String toString();
}
