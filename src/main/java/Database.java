import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Class that stores all of the Users and MessageHistories.
 * Manages all loading, saving, and accessing of data and some data validation.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public class Database implements DatabaseInterface {
    private final static Object USER_KEY = new Object();
    private final static Object MESSAGE_KEY = new Object();
    private final static Object PHOTO_KEY = new Object();
    private ArrayList<User> userList;
    private ArrayList<MessageHistory> allChats;
    private final char fileSeparator = 28;
    private final char groupSeparator = 29;
    private ArrayList<String> photosPath;

    /**
     * Initializes the Database class, setting up the user list, message history
     * list, and photo paths.
     */
    public Database() {
        this.userList = new ArrayList<>();
        this.allChats = new ArrayList<>();
        this.photosPath = new ArrayList<>();
    }

    /**
     * Adds a new user to the database if the username is valid
     *
     * @param user User to add
     * @return true if user was added
     */
    @Override
    public boolean addUser(User user) {
        synchronized (USER_KEY) {
            return validateNewUser(user) && userList.add(user);
        }
    }

    /**
     * Checks if a username valid for a new user
     *
     * @param user User to validate.
     * @return true if username passes test cases
     */
    @Override
    public boolean validateNewUser(User user) {

        // Check for unique username
        synchronized (USER_KEY) {
            for (User u : this.userList) {
                if (u.getUsername().equals(user.getUsername())) {
                    return false;
                }
            }
        }

        // Check if username or password is null/empty
        if (user.getUsername() == null || user.getUsername().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            return false;
        }

        // Check if password contains username (case insensitive)
        if (user.getPassword().toLowerCase().contains(user.getUsername().toLowerCase())) {
            return false;
        }

        // Check password requirements
        String password = user.getPassword();
        boolean lengthOk = password.length() >= 6;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return lengthOk && hasUpper && hasLower && hasSpecial;
    }

    /**
     * @return allChats of MessageHistory
     */
    @Override
    public ArrayList<MessageHistory> getAllChats() {
        synchronized (MESSAGE_KEY) {
            return this.allChats;
        }
    }

    public String[] getAllUserChats(String username) {
        synchronized (MESSAGE_KEY) {
            ArrayList<String> chatList = new ArrayList<>();
            for (int i = 0; i < this.allChats.size(); i++) {
                MessageHistory mh = this.allChats.get(i);
                if (mh.getSender().equals(username)) {
                    chatList.add(mh.getRecipient());
                } else if (mh.getRecipient().equals(username)) {
                    chatList.add(mh.getSender());
                }
            }
            return chatList.toArray(new String[0]);
        }
    }

    /**
     * @return userList of Users
     */
    @Override
    public ArrayList<User> getUsers() {
        synchronized (USER_KEY) {
            return this.userList;
        }
    }

    /**
     * Accesses a User based on username
     *
     * @param username Username of user
     * @return User with matching username
     */
    @Override
    public User getUser(String username) {
        // if User implements Comparable, we can sort userList and make this more
        // efficient
        synchronized (USER_KEY) {
            for (User u : this.userList) {
                if (u.getUsername().equals(username)) {
                    return u;
                }
            }
        }
        return null;
    }

    /**
     * Returns the MessageHistory from AllChats for two users
     *
     * @param user1 One username in the MessageHistory
     * @param user2 Other username in the MessageHistory
     * @return MessageHistory for the two users
     * @throws IllegalArgumentException If user1 and user2 are the same
     */
    @Override
    public MessageHistory getMessages(String user1, String user2) throws IllegalArgumentException {
        if (user1.equals(user2)) {
            return null;
        }

        synchronized (MESSAGE_KEY) {
            for (MessageHistory mh : this.allChats) {
                if (mh.equals(new MessageHistory(new String[] { user1, user2 }))) {
                    return mh;
                }
            }
        }

        return null;
    }

    /**
     * adds a MessageHistory to AllChats
     *
     * @param messageHistory MessageHistory to add
     * @return true if MessageHistory was added
     */
    @Override
    public boolean addMessageHistory(MessageHistory messageHistory) {
        synchronized (MESSAGE_KEY) {
            for (MessageHistory mh : this.allChats) {
                if (mh.equals(messageHistory)) {
                    return false;
                }
            }
        }
        if (messageHistory.getUsernames().length == 2) {
            synchronized (MESSAGE_KEY) {
                this.allChats.add(messageHistory);
            }
            return true;
        }
        return false;
    }

    public boolean deleteChat(String user1, String user2) {
        synchronized (MESSAGE_KEY) {
            for (int i = 0; i < this.allChats.size(); i++) {
                MessageHistory mh = this.allChats.get(i);
                if (mh.getSender().equals(user1) && mh.getRecipient().equals(user2)) {
                    this.allChats.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a message to a MessageHistory based upon the message and receiver.
     * Includes checks for friends only and blocked users
     *
     * @param message  Message to add
     * @param receiver Username of receiver
     * @return true if message was added
     **/
    public boolean addMessage(Message message, String receiver) {
        User u1 = this.getUser(message.getSender());
        User u2 = this.getUser(receiver);

        if (u1 == null || u2 == null) {
            return false;
        }

        ArrayList<String> u1Blocked = u1.getBlocked();
        ArrayList<String> u2Blocked = u2.getBlocked();

        if (u1Blocked.contains(receiver) || u2Blocked.contains(message.getSender())) {
            return false;
        }

        if (u1.isFriendsOnly()) {
            ArrayList<String> u1Friends = u1.getFriends();
            if (!u1Friends.contains(receiver)) {
                return false;
            }
        }

        if (u2.isFriendsOnly()) {
            ArrayList<String> u2Friends = u2.getFriends();
            if (!u2Friends.contains(u1.getUsername())) {
                return false;
            }
        }

        synchronized (MESSAGE_KEY) {
            for (int i = 0; i < this.allChats.size(); i++) {
                MessageHistory mh = this.allChats.get(i);
                if (mh.equals(new MessageHistory(new String[] { message.getSender(), receiver }))) {
                    mh.addMessage(message);
                    this.allChats.set(i, mh);
                    return true;
                }
            }
        }

        MessageHistory mh = new MessageHistory(message, receiver);
        synchronized (MESSAGE_KEY) {
            this.allChats.add(mh);
            return true;
        }
    }

    /**
     * Deletes a message from a MessageHistory based upon the message and receiver.
     *
     * @param message  Message to add
     * @param receiver Username of receiver
     * @return true if message was added
     **/
    public boolean deleteMessage(Message message, String receiver) {
        User u1 = this.getUser(message.getSender());
        User u2 = this.getUser(receiver);
        if (u1 == null || u2 == null) {
            return false;
        }
        MessageHistory mh = this.getMessages(u1.getUsername(), u2.getUsername());
        if (mh == null) {
            return false;
        }
        synchronized (MESSAGE_KEY) {
            for (Message m : mh.getMessageHistory()) {
                if (m.getMessage().equals(message.getMessage()) && m.getSender().equals(message.getSender())) {
                    mh.deleteMessage(m);
                    this.allChats.set(this.allChats.indexOf(mh), mh);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds user 2 as a friend of user 1.
     *
     * @param user1 user to be changed
     * @param user2 user to be added
     * @return if the friend was added (both users must exist)
     */
    @Override
    public boolean addFriend(String user1, String user2) {
        User u1 = this.getUser(user1);
        User u2 = this.getUser(user2);

        // Check if either user doesn't exist
        if (u1 == null || u2 == null) {
            return false;
        }

        // Prevent self-friending
        if (user1.equals(user2)) {
            return false;
        }

        // Check if already friends
        if (u1.getFriends().contains(user2)) {
            return false;
        }

        // Check if either user has blocked the other
        if (u1.getBlocked().contains(user2) || u2.getBlocked().contains(user1)) {
            return false;
        }

        u1.addFriend(user2);
        return true;
    }

    /**
     * Returns an array of friends for a user
     *
     * @param username username of user to get friends of
     * @return array of friends
     */
    public String[] getFriends(String username) {
        User u = this.getUser(username);
        if (u == null) {
            return null;
        }
        return u.getFriends().toArray(new String[u.getFriends().size()]);
    }

    /**
     * Adds user 2 as a friend of user 1.
     *
     * @param user1 user to be changed
     * @param user2 user to be added
     * @return if the friend was added (both users must exist)
     */
    public boolean removeFriend(String user1, String user2) {
        User u1 = this.getUser(user1);
        User u2 = this.getUser(user2);
        if (u1 == null || u2 == null) {
            return false;
        }
        u1.removeFriend(user2);
        return true;
    }

    /**
     * Adds user 2 as a block of user 1.
     *
     * @param user1 user to be changed
     * @param user2 user to be added
     * @return if the block was added (both users must exist)
     */
    public boolean blockUser(String user1, String user2) {
        User u1 = this.getUser(user1);
        User u2 = this.getUser(user2);
        if (u1 == null || u2 == null) {
            return false;
        }
        u1.addBlock(user2);
        return true;
    }

    /**
     * Returns an array of blocks for a user
     *
     * @param username username of user to get blocks of
     * @return array of blocks
     */
    public String[] getBlockList(String username) {
        User u = this.getUser(username);
        if (u == null) {
            return null;
        }
        return u.getBlocked().toArray(new String[u.getBlocked().size()]);
    }

    /**
     * Removes user 2 as a block of user 1
     *
     * @param user1 user to be changed
     * @param user2 user to be removed
     * @return if the block was removed (both users must exist)
     */
    public boolean unblockUser(String user1, String user2) {
        User u1 = this.getUser(user1);
        User u2 = this.getUser(user2);
        if (u1 == null || u2 == null) {
            return false;
        }

        if (!u1.getBlocked().contains(user2)) {
            return false;
        }

        u1.unblock(user2);
        return true;
    }

    /**
     * Saves all users in the `userList` to a file named `usersHistory.txt`.
     * Each user's information (username, password, first name, last name, bio,
     * and birthday) is written to the file, with fields separated by
     * the `groupSeparator` character, and user entries separated by
     * the `fileSeparator` character. If the file does not exist, it will be
     * created.
     *
     * @return true if users were successfully saved to the file, false if an
     *         exception occurred.
     */
    @Override
    public boolean saveUsers() {
        // write to a backup file the contents of userList
        try {
            File f = new File("usersHistory.txt");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            }

            // Use try-with-resources for BufferedWriter
            try (BufferedWriter bfr = new BufferedWriter(new FileWriter(f))) {
                synchronized (USER_KEY) {
                    for (User users : userList) {
                        bfr.write(fileSeparator);
                        bfr.write("username: ");
                        bfr.write(users.getUsername());
                        bfr.write(groupSeparator);
                        bfr.write("password: ");
                        bfr.write(users.getPassword());
                        bfr.write(groupSeparator);
                        if (users.getFirstName() != null) {
                            bfr.write("First Name: ");
                            bfr.write(users.getFirstName());
                            bfr.write(groupSeparator);
                        }
                        if (users.getLastName() != null) {
                            bfr.write("Last Name: ");
                            bfr.write(users.getLastName());
                            bfr.write(groupSeparator);
                        }
                        if (users.getBio() != null) {
                            bfr.write("Bio: ");
                            bfr.write(users.getBio());
                            bfr.write(groupSeparator);
                        }
                        if (users.getBirthday() != null && users.getBirthday().length == 3) {
                            bfr.write("Birthday: ");
                            bfr.write(users.getBirthday()[0] + "/" + users.getBirthday()[1] + "/"
                                    + users.getBirthday()[2]); // fix
                            bfr.write(groupSeparator);
                        }
                        if (users.getProfilePic() != null) {
                            bfr.write("Profile Picture: ");
                            bfr.write(users.getProfilePic());
                            bfr.write(groupSeparator);
                        }
                        if (!users.getFriends().isEmpty()) {
                            bfr.write("Friends: ");
                            for (String friend : users.getFriends()) {
                                bfr.write(friend);
                                bfr.write(":");
                            }
                            bfr.write(groupSeparator);
                        }
                        if (!users.getBlocked().isEmpty()) {
                            bfr.write("Blocks: ");
                            for (String blocked : users.getBlocked()) {
                                bfr.write(blocked);
                                bfr.write(":");
                            }
                            bfr.write(groupSeparator);
                        }
                        bfr.write("Friends Only: ");
                        bfr.write("" + users.isFriendsOnly());
                        bfr.write(groupSeparator);
                        bfr.write(fileSeparator);
                        bfr.write('\n');
                    }
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Loads user data from a file named `usersHistory.txt` into the `userList`.
     * Reads each user's information, which is expected to be formatted with
     * fields separated by the `groupSeparator` character and user entries
     * separated by the `fileSeparator` character. For each valid user entry,
     * a new `User` object is created and added to the `userList`.
     *
     * @return true if users were successfully loaded from the file, false if
     *         the file does not exist or an error occurs during reading.
     */
    @Override
    public boolean loadUsers() {
        File f = new File("usersHistory.txt");
        if (!f.exists()) {
            System.out.println("No data file found.");
            return false;
        }
        try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                try {
                    // Skip empty lines
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    // Check for minimum required format
                    if (!line.contains("username:") || !line.contains("password:")) {
                        System.out.println("Invalid format: missing username or password");
                        return false;
                    }

                    String temp;
                    while (line.charAt(line.length() - 1) != fileSeparator && bfr.ready()) {
                        temp = bfr.readLine();
                        if (temp == null)
                            break;
                        line += temp;
                    }

                    String[] elements = line.split(String.valueOf(groupSeparator));
                    if (elements.length < 2) {
                        System.out.println("Invalid format: insufficient elements");
                        return false; // maybe change this to continue
                    }

                    // Extract username and password with error checking
                    String username = null;
                    String password = null;
                    try {
                        username = elements[0].replace("username: ", "").trim();
                        password = elements[1].replace("password: ", "").trim();
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Invalid format: malformed username/password");
                        return false;
                    }

                    if (username.isEmpty() || password.isEmpty()) {
                        System.out.println("Invalid format: empty username or password");
                        return false;
                    }

                    User user = new User(username, password);

                    // Process optional fields
                    for (int i = 2; i < elements.length - 1; i++) {
                        try {
                            String[] parts = elements[i].split(":");
                            if (parts.length < 2)
                                continue;

                            String command = parts[0].trim();
                            String value = parts[1].trim();

                            switch (command) {
                                case "First Name":
                                    user.setFirstName(value);
                                    break;
                                case "Last Name":
                                    user.setLastName(value);
                                    break;
                                case "Bio":
                                    user.setBio(value);
                                    break;
                                case "Birthday":
                                    try {
                                        String[] birthday = value.split("/");
                                        int[] birthdayInt = new int[3];
                                        for (int j = 0; j < 3; j++) {
                                            birthdayInt[j] = Integer.parseInt(birthday[j]);
                                        }
                                        user.setBirthday(birthdayInt);
                                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                        System.out.println("Invalid birthday format");
                                    }
                                    break;
                                case "Profile Picture":
                                    user.setProfilePic(value);
                                    break;
                                case "Friends":
                                    String[] friends = value.split(":");
                                    ArrayList<String> friendsList = new ArrayList<>();
                                    for (String friend : friends) {
                                        if (!friend.trim().isEmpty()) {
                                            friendsList.add(friend);
                                        }
                                    }
                                    user.setFriends(friendsList);
                                    break;
                                case "Blocks":
                                    String[] blocked = value.split(":");
                                    ArrayList<String> blockedList = new ArrayList<>();
                                    for (String block : blocked) {
                                        if (!block.trim().isEmpty()) {
                                            blockedList.add(block);
                                        }
                                    }
                                    user.setBlocked(blockedList);
                                    break;
                                case "Friends Only":
                                    user.setFriendsOnly(Boolean.parseBoolean(value));
                                    break;
                            }
                        } catch (Exception e) {
                            System.out.println("Error processing optional field: " + e.getMessage());
                        }
                    }
                    this.userList.add(user);
                } catch (Exception e) {
                    System.out.println("Error processing line: " + e.getMessage());
                    return false; // MAYBE change this to continue
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves all of the MessageHistories to a file.
     *
     * @return if the messages were properly saved to the file
     */
    @Override
    public boolean saveMessages() {
        // Checks if the File does not yet exist and creates one if so.
        File messagesFile = new File("messageHistory.txt");
        if (!messagesFile.exists()) {
            try {
                messagesFile.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        // Writes output to the file.
        try (FileWriter fw = new FileWriter(messagesFile, false)) {
            synchronized (MESSAGE_KEY) {
                for (MessageHistory mh : this.allChats) {
                    fw.write(fileSeparator);
                    fw.write(mh.toString() + "\n");
                    for (Message m : mh.getMessageHistory()) {
                        fw.write(m.toString() + groupSeparator + "\n");
                    }
                }
                fw.write(fileSeparator);
            }
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Loads all of the MessageHistories from a file.
     *
     * @return if the messages were properly loaded from the file
     */
    @Override
    public boolean loadMessages() {
        File messagesFile = new File("messageHistory.txt");
        if (!messagesFile.exists()) {
            return false;
        }
        // Reads the file.
        try (BufferedReader br = new BufferedReader(new FileReader(messagesFile))) {
            ArrayList<Message> messages = new ArrayList<>();
            String line = br.readLine();
            String[] usernames = new String[2];
            while (br.ready()) {
                // Get the usernames from the first line of a new MessageHistory (if it's a new
                // fileSeparator)
                if (line.charAt(0) == fileSeparator) {
                    line = line.substring(1);
                    usernames = line.split(" ");
                    line = br.readLine();
                }
                /*
                 * Add to the line until the next group separator (which is at the end of a
                 * line/message)
                 * Or until the next file separator (which is at the beginning of a
                 * line/messageHistory)
                 */
                while (line.charAt(0) != fileSeparator && line.indexOf(groupSeparator) == -1) {
                    line = line + "\n" + br.readLine();
                }
                // Creates a new Message and adds it to the list
                Message m = new Message(line.substring(line.indexOf(' ') + 1, line.length() - 1),
                    line.substring(line.indexOf(':') + 1).substring(0, line.substring(line.indexOf(':') + 1).indexOf(':')), 
                    Long.parseLong(line.substring(0, line.indexOf(':'))));
                        
                messages.add(m);
                line = br.readLine();

                // Add the messageHistory to the allChats list if it's a new fileSeparator and
                // reset messages
                if (line.charAt(0) == fileSeparator) {
                    MessageHistory mh = new MessageHistory(usernames);
                    mh.setMessageHistory(messages);
                    this.allChats.add(mh);
                    messages = new ArrayList<>();
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading messages: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Loads photo paths from a file named `UsersPhotos.txt` and stores them in
     * `photosPath`.
     * Reads each line in the file as a separate path and adds it to the list.
     *
     * @return true if the photo paths were successfully loaded, false if an error
     *         occurred.
     */
    @Override
    public boolean loadPhotos() {
        try (BufferedReader bfr = new BufferedReader(new FileReader("UsersPhotos.txt"))) {
            String line = bfr.readLine();
            while (true) {
                if (line == null) {
                    break;
                }
                photosPath.add(line);
                line = bfr.readLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Saves the current photo paths in `photosPath` to a file named
     * `UsersPhotos.txt`.
     * Each path is written to a new line in the file.
     *
     * @return true if the photo paths were successfully saved, false if an error
     *         occurred.
     */
    @Override
    public boolean savePhotos() {
        try {
            File f = new File("UsersPhotos.txt");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            }
            try (BufferedWriter bfw = new BufferedWriter(new FileWriter(f))) {
                synchronized (PHOTO_KEY) {
                    for (String path : photosPath) {
                        bfw.write(path + "\n");
                    }
                }
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Adds a new photo path to the `photosPath` list.
     *
     * @param path The file path of the photo to add.
     */
    @Override
    public void addPhotos(String path) {
        synchronized (PHOTO_KEY) {
            photosPath.add(path);
        }
    }

    /**
     * Displays a photo in a new JFrame window using the given file path.
     * The window shows the image and can be closed by the user.
     *
     * @param path The file path of the photo to display.
     */
    @Override
    public void displayPhotos(String path) {
        // Create a JFrame Window
        JFrame frame = new JFrame(path);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // Load image using ImageIcon
        ImageIcon imageIcon = new ImageIcon(path);

        // Add image to the window
        JLabel label = new JLabel(imageIcon);

        // add label to frame(window)
        frame.add(label);

        // make visible
        frame.setVisible(true);
    }

    /**
     * Sets the list of photos.
     *
     * @param photoPath An ArrayList of String path objects to be set.
     */
    @Override
    public void setPhotos(ArrayList<String> photoPath) {
        synchronized (PHOTO_KEY) {
            this.photosPath = photoPath;
        }
    }

    @Override
    public ArrayList<String> getPhotos() {
        synchronized (PHOTO_KEY) {
            return photosPath;
        }
    }

    public void setUser(User user) {
        synchronized (USER_KEY) {
            for (int i = 0; i < this.userList.size(); i++) {
                if (this.userList.get(i).getUsername().equals(user.getUsername())) {
                    this.userList.set(i, user);
                    return;
                }
            }
        }
    }
    /**
     * Sets the list of users.
     *
     * @param newUserList An ArrayList of User objects to be set.
     */
    @Override
    public void setUsersList(ArrayList<User> newUserList) {
        synchronized (USER_KEY) {
            this.userList = newUserList;
        }
    }

    /**
     * Sets the list of all message histories.
     *
     * @param allChats An ArrayList of MessageHistory objects to be set.
     */
    @Override
    public void setAllChats(ArrayList<MessageHistory> allChats) {
        synchronized (MESSAGE_KEY) {
            this.allChats = allChats;
        }
    }
}
