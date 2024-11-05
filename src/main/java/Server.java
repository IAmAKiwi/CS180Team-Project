import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;


public class Server implements Runnable, ServerInterface {
    private static ServerSocket serverSocket;
    private static Database db;
    private Socket clientSocket;
    private User loggedInUser;
    private MessageHistory currentChat; // Maybe, have to update our chat a bunch.

    public Server(Socket socket) {
        clientSocket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: implement server
        // Client will send commands to server based on the GUI


    }


    public boolean login(String username, String password) {
        for (User u : db.getUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                loggedInUser = u;
                return true;
            }
        }
        return false;
    }

    public boolean register(String username, String password) {
        if (db.addUser(new User(username, password))) {
            loggedInUser = new User(username, password);
            return true;
        }
        return false;
    }

    public boolean sendMessage(String otherUsername);
    public boolean sendImage(String otherUsername, String image);
    public boolean openChat(String otherUsername);

    public boolean addFriend(String otherUsername);
    public String[] getFriendList();
    public boolean addBlock(String otherUsername);
    public String[] getBlockList();
    public boolean removeFriend(String otherUsername);
    public boolean removeBlock(String otherUsername);

    public boolean isFriendsOnly(String otherUsername);
    public boolean setFriendsOnly(boolean friendsOnly);

    public boolean setProfilePic(String profilePic);
    // A billion more setters and getters

    public boolean logout();

    public static void main(String[] args) {
        db = new Database();
        db.loadMessages();
        db.loadUsers();

        try {
            serverSocket = new ServerSocket(4242);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new Server(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

