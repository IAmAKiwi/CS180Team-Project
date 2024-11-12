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
 *
 * @version Nov 2, 2024
 */
public class Database implements DatabaseInterface {
    private static Object userKey;
    private static Object messageKey;
    private static Object photoKey;
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
        this.userKey = new Object();
        this.messageKey = new Object();
        this.photoKey = new Object();
    }

    /**
     * Adds a new user to the database if the username is valid
     *
     * @param user User to add
     * @return true if user was added
     */
    @Override
    public boolean addUser(User user) {
        synchronized (this.userKey) {
            if (this.validateNewUser(user)) {
                this.userList.add(user);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a username valid for a new user
     *
     * @param user User to validate.
     * @return true if username passes test cases
     */
    @Override
    public boolean validateNewUser(User user) {
        // verifies that a username unique from all
        // current others was provided.
        for (User u : this.userList) {
            if (u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        if (user.getPassword().toLowerCase().contains(user.getUsername().toLowerCase())) {
            return false;
        } else if (!user.getPassword().matches(".*[A-Z].*") || !user.getPassword().matches(".*[a-z].*")
                || !user.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return false;
        } else if (user.getPassword().length() < 6) {
            return false;
        }
        return true;

    }

    /**
     * @return allChats of MessageHistory
     */
    @Override
    public ArrayList<MessageHistory> getAllChats() {
        return this.allChats;
    }

    /**
     * @return userList of Users
     */
    @Override
    public ArrayList<User> getUsers() {
        return this.userList;
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
        for (User u : this.userList) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        User noEqual = new User();
        return noEqual;
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
            throw new IllegalArgumentException("No such self-messaging history.");
        }

        for (MessageHistory mh : this.allChats) {
            if (mh.equals(new MessageHistory(new String[] { user1, user2 }))) {
                return mh;
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
        for (MessageHistory mh : this.allChats) {
            if (mh.equals(messageHistory)) {
                return false;
            }
        }
        if (messageHistory.getUsernames().length == 2) {
            synchronized (this.messageKey) {
                this.allChats.add(messageHistory);
            }
            return true;
        }
        return false;
    }

    /**
     * Adds a message to a MessageHistory based upon the message and receiver.
     * Includes checks for friends only and blocked users
     * @param message Message to add
     * @param receiver Username of receiver
     * @return true if message was added
     **/
    public boolean addMessage(Message message, String receiver) {
        User u1 = this.getUser(message.getSender());
        User u2 = this.getUser(receiver);
        ArrayList<String> u1Blocked = u1.getBlocked();
        ArrayList<String> u2Blocked =u2.getBlocked();

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

        for (int i = 0; i < this.allChats.size(); i++) {
            MessageHistory mh = this.allChats.get(i);
            if (mh.equals(new MessageHistory(new String[] { message.getSender(), receiver }))) {
                mh.addMessage(message);
                synchronized (this.messageKey) {
                    this.allChats.set(i, mh);
                }
                return true;
            }
        }

        MessageHistory mh = new MessageHistory(message, receiver);
        synchronized (this.messageKey) {
        this.allChats.add(mh);
        }
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
        // TODO: write to a backup file the contents of userList
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
                synchronized (this.userKey) {
                    bfr.write(fileSeparator);
                    for (User users : userList) {
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
                            bfr.write(users.getBirthday()[0] + " " + users.getBirthday()[1] + " " + users.getBirthday()[2]); // fix
                            bfr.write(fileSeparator);
                        }
                    }
                    bfr.write(fileSeparator);
                    bfr.newLine();
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
                String[] elements = line.split(String.valueOf(groupSeparator));
                if (elements.length < 2)
                    continue;

                String username = elements[0].replace("username: ", "").trim();
                String password = elements[1].replace("password: ", "").trim();
                User user = new User(username, password);
                this.userList.add(user);
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
            synchronized (this.messageKey) {
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
        // Writes output to the file.
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
                        line.substring(0, line.indexOf(':')));
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
                synchronized (this.photoKey) {
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
        synchronized (this.photoKey) {
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
        synchronized (this.photoKey) {
            this.photosPath = photoPath;
        }
    }

    @Override
    public ArrayList<String> getPhotos() {
        return photosPath;
    }

    /**
     * Sets the list of users.
     *
     * @param userList An ArrayList of User objects to be set.
     */
    @Override
    public void setUsersList(ArrayList<User> userList) {
        synchronized (this.userKey) {
            this.userList = userList;
        }
    }

    /**
     * Sets the list of all message histories.
     *
     * @param allChats An ArrayList of MessageHistory objects to be set.
     */
    @Override
    public void setAllChats(ArrayList<MessageHistory> allChats) {
        synchronized (this.messageKey) {
            this.allChats = allChats;
        }
    }
}
