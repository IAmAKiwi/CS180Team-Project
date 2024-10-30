import java.util.ArrayList;
/**
 * 
 */
public class Database implements DatabaseInterface
{
    private ArrayList<User> userList;
    private ArrayList<MessageHistory> allChats;

    public Database()
    {
        this.userList = new ArrayList<User>();
        this.allChats = new ArrayList<MessageHistory>();
    }

    public boolean addUser(User user)
    {
        if (this.validateNewUser(user))
        {
            this.userList.add(user);
            return true;
        }
        return false;
    }

    public boolean validateNewUser(User user)
    {
        // verifies that a username unique from all
        // current others was provided.
        for (User u : this.userList)
        {
            if (u.getUsername().equals(user.getUsername()))
            {
                return false;
            }
        }


        if (user.getPassword().contains(user.getUsername()))
        {
            return false;
        }

        // TODO: implement more password safety verifications

        return true;
    }

    public ArrayList<User> getUsers()
    {
        return this.userList;
    }

    public User getUser(String username)
    {
        // if User implements Comparable, we can sort userList and make this more efficient
        for (User u : this.userList)
        {
            if (u.getUsername().equals(username))
            {
                return u;
            }
        }
    }

    public String getUserName(String username)
    {
        // TODO: clarify purpose of method.
        // How does providing a username and 
        // receiving a username have any function?
        return null;
    }

    public MessageHistory getMessages(String user1, String user2) throws IllegalArgumentException
    {
        if (user1.equals(user2))
        {
            throw new IllegalArgumentException("No such self-messaging history.")
        }

        for (MessageHistory mh : this.allChats)
        {
            if ((mh.getUsernames()[0].equals(user1)) || (mh.getUsernames()[1].equals(user1)))
            {
                if ((mh.getUsernames()[0].equals(user2)) || (mh.getUsernames()[1].equals(user2)))
                {
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
            return false;
        }

        public boolean loadUsers()
        {
            //TODO: read backup file into userList
            return false;
        }

        public boolean loadMessages()
        {
            //TODO: read backup file into allChats
        }


       
    }
}
