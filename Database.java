import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedWriter;

/**
 *
 */
public class Database implements DatabaseInterface {
    private ArrayList<User> userList;
    private ArrayList<MessageHistory> allChats;
    private final char fileSeparator = 28;
    private final char groupSeparator = 29;

    public Database() {
        this.userList = new ArrayList<User>();
        this.allChats = new ArrayList<MessageHistory>();
    }

    public boolean addUser(User user) {
        if (this.validateNewUser(user)) {
            this.userList.add(user);
            return true;
        }
        return false;
    }

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

    public ArrayList<User> getUsers() {
        return this.userList;
    }

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
            for (User users : userList) {
                bfr.write(users.toString());
                bfr.newLine();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadUsers() {
        // TODO: read backup file into userList
        File f = new File("usersHistory.txt");
        FileReader fr = new FileReader(f);
        BufferedReader bfr = new BufferedReader(fr);
        String line = bfr.readLine();
        ArrayList<String> data;
        while (true) {
            if (line == null) {
                break;
            }
            data.add(line);
            line = bfr.readLine();
        }
        if (data == null) {
            System.out.println("No data is put in");
            return false;
        }
        /*
         * username password bio .......
         * fileSeparator username: ... groupSeparator password: .... groupSeperator bio:
         * ....
         */
        User user;
        String userName;
        String passWord;
        String[] element;
        Character character = (Character) groupSeparator;
        String cha = character.toString();
        for (String item : data) {
            element = item.split(cha);
            userName = element[0].replace("username: ", "");
            passWord = element[1].replace("password: ", "");
            user = new User(userName, passWord);
            userList.add(user);
        }
        if (userList == null) {
            System.out.println("no data is put in userList");
        }
        return true;
    }

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

    public boolean loadMessages() {
        // TODO: read backup file into allChats
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
                Message m = new Message(line.substring(line.indexOf(':') + 1, line.length() - 1),
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
