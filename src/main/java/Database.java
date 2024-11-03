import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;

import java.io.BufferedWriter;

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
    private ArrayList<User> userList;
    private ArrayList<MessageHistory> allChats;
    private final char fileSeparator = 28;
    private final char groupSeparator = 29;

    /**
     * Constructor for the Database class, initializes userList and allChats
     */
    public Database() {
        this.userList = new ArrayList<User>();
        this.allChats = new ArrayList<MessageHistory>();
    }

    /**
     * Adds a new user to the database if the username is valid
     *
     * @param user User to add
     * @return true if user was added
     */
    public boolean addUser(User user) {
        if (this.validateNewUser(user)) {
            this.userList.add(user);
            return true;
        }
        return false;
    }

    /**
     * Checks if a username valid for a new user
     *
     * @param user User to validate.
     * @return true if username passes test cases
     */
    public boolean validateNewUser(User user) {
        // verifies that a username unique from all
        // current others was provided.
        for (User u : this.userList) {
            if (u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        if (user.getPassword().contains(user.getUsername())) {
            return false;
        }
        return true;

        // TODO: implement more password safety verifications
    }

    /**
     * @return userList of Users
     */
    public ArrayList<User> getUsers() {
        return this.userList;
    }

    /**
     * Accesses a User based on username
     * 
     * @param username Username of user
     * @return User with matching username
     */
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
    public MessageHistory getMessages(String user1, String user2) throws IllegalArgumentException {
        if (user1.equals(user2)) {
            throw new IllegalArgumentException("No such self-messaging history.");
        }

        for (MessageHistory mh : this.allChats) {
            if ((mh.getUsernames()[0].equals(user1)) || (mh.getUsernames()[1].equals(user1))) {
                if ((mh.getUsernames()[0].equals(user2)) || (mh.getUsernames()[1].equals(user2))) {
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
    public boolean addMessageHistory(MessageHistory messageHistory) {
        for (MessageHistory mh : this.allChats) {
            if (mh.equals(messageHistory)) {
                return false;
            }
        }
        if (messageHistory.getUsernames().length == 2) {
            this.allChats.add(messageHistory);
            return true;
        }
        return false;
    }

    public boolean saveUsers() {
        // TODO: write to a backup file the contents of userList
        try {
            File f = new File("usersHistory.txt");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FileWriter fr = new FileWriter(f);
            BufferedWriter bfr = new BufferedWriter(fr);
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
            bfr.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadUsers() {
        // TODO: read backup file into userList

        File f = new File("usersHistory.txt");
        if (!f.exists()) {
            System.out.println("No data file found.");
            return false;
        }
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
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
            bfr.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves all of the MessageHistories to a file.
     * 
     * @return if the messages were properly saved to the file
     */
    public boolean saveMessages() {
        // TODO: write to a backup file the contents of allChats
        // Checks if the File does not yet exist and creates one if so.
        File messagesFile = new File("messageHistory.txt");
        if (!messagesFile.exists()) {
            try {
                messagesFile.createNewFile();
            } catch (Exception e) {
                return false;
            }
        }
        // Writes output to the file.
        try (FileWriter fw = new FileWriter(messagesFile, false)) {
            for (MessageHistory mh : this.allChats) {
                fw.write(fileSeparator);
                fw.write(mh.toString() + "\n");
                for (Message m : mh.getMessageHistory()) {
                    fw.write(m.toString() + groupSeparator + "\n");
                }
            }
            fw.write(fileSeparator);
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
    public boolean loadMessages() {
        File messagesFile = new File("messageHistory.txt");
        if (!messagesFile.exists()) {
            return false;
        }
        // Writes output to the file.
        try (BufferedReader br = new BufferedReader(new FileReader(messagesFile))) {
            ArrayList<Message> messages = new ArrayList<Message>();
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
                    messages = new ArrayList<Message>();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
