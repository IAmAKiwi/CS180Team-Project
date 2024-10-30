import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 */
public class Database implements DatabaseInterface {
    private ArrayList<User> userList;
    private ArrayList<MessageHistory> allChats;
    private final char fileSeparator = File.separatorChar;
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
        // if User implements Comparable, we can sort userList and make this more efficient
        for (User u : this.userList) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
    }

    public MessageHistory getMessages(String user1, String user2) throws IllegalArgumentException {
        if (user1.equals(user2)) {
            throw new IllegalArgumentException("No such self-messaging history.")
        }

        for (MessageHistory mh : this.allChats) {
            if ((mh.getUsernames()[0].equals(user1)) || (mh.getUsernames()[1].equals(user1))) {
                if ((mh.getUsernames()[0].equals(user2)) || (mh.getUsernames()[1].equals(user2))) {
                    return mh;
                }
            }
        }


        public boolean saveUsers()
        {
            //TODO: write to a backup file the contents of userList
            return false;
        }

        public boolean saveMessages()
        {
            //TODO: write to a backup file the contents of allChats
            File messagesFile = new File("messageHistory.txt");
            if (!messagesFile.exists())
            {
                try
                {
                    messagesFile.createNewFile();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return false;
        }

        public boolean loadUsers()
        {
            //TODO: read backup file into userList
            File f = new File("usersHistory.txt");
            FileReader fr = new FilReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            ArrayList<String> data = new ArrayList<>();
            while (true) {
                if (line == null) {
                    break;
                }
                data.add(line);
                line = bfr.readLine();
            }
            

            return false;
        }

        public boolean loadMessages()
        {
            //TODO: read backup file into allChats
        }


       
    }


    public boolean saveUsers() {
        //TODO: write to a backup file the contents of userList
        return false;
    }

    public boolean saveMessages() {
        //TODO: write to a backup file the contents of allChats
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
                    fw.write(groupSeparator);
                    fw.write(m.toString() + "\n");
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean loadUsers() {
        //TODO: read backup file into userList
        return false;
    }

    public boolean loadMessages() {
        //TODO: read backup file into allChats
        File messagesFile = new File("messageHistory.txt");
        if (!messagesFile.exists()) {
            return false;
        }
        // Writes output to the file.
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(messagesFile))) {
            ArrayList<Message> messages = new ArrayList<Message>();
            while (br.ready()) {

            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}

