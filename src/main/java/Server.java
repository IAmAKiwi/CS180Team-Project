import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;

/**
 * Typical use flow of a client/server connection:
 * Client connects to server.
 * Client either logs in or registers. (should not have to check if they are logged in, should be done implicitly by GUI)
 * Client opens a chat (server sends back the data to display)
 * Client sends a message (server updates the data)
 * Whenever a
 *
 */

public class Server implements Runnable, ServerInterface {
    public static Object userKey = new Object(); // Synchronization used for Users access
    public static Object messageKey = new Object();  // Synchronization used for Messages access
    private static ServerSocket serverSocket;
    private static Database db;
    private Socket clientSocket;
    private User currentUser;
    private String otherUser;
    private MessageHistory currentChat; // Maybe, have to update our chat a bunch.


    public Server(Socket socket) {
        clientSocket = socket;
    }

    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;


        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: implement server
        // Client will send commands to server based on the GUI

        boolean running = true;
        String[] command = new String[2]; // [0] = command, [1] = argument
        while (running) {
            try {
               command = reader.readLine().split(":");
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (command[0]) {
                case "login":
                    login(command[1].substring(0, command[1].indexOf(":")),
                            command[1].substring(command[1].indexOf(":") + 1));
                    break;
                case "register":
                    register(command[1].substring(0, command[1].indexOf(":")),
                            command[1].substring(command[1].indexOf(":") + 1));
                    break;
                case "getMessage":
                    getMessage(content);
                    break;
                case "sendMessage":
                    sendMessage(content);
                    break;
                case "deleteMessage":
                    deleteMessage(content);
                    break;
                case "accessProfile":
                    accessProfile();
                    break;
                case "updateProfile":
                    updateProfile(content);
                    break;
                case "removeFriend":
                    removeFriend(content);
                    break;
                case "addFriend":
                    addFriend(content);
                    break;
                case "unblockUser":
                    unblockUser(content);
                    break;
                case "blockUser":
                    blockUser(content);
                    break;
                case "requestActive":
                    requestActive(content);
                    break;
                case "deleteChat":
                    deleteChat(content);
                    break;
                case "sendImage":
                    sendImage(content);
                    break;
                case "openChat":
                    openChat(content);
                    break;
                case "getBlockList":
                    getBlockList();
                    break;
                case "getFriendList":
                    getFriendList();
                    break;
                case "isFriendsOnly":
                    isFriendsOnly(content);
                    break;
                case "setFriendsOnly":
                    setFriendsOnly(content);
                    break;
                case "setProfilePic":
                    setProfilePic(content);
                    break;
                case "getProfilePic":
                    getProfilePic();
                    break;
                case "logout":
                    logout();
                    break;
                case "disconnect":
                    if (disconnect()) {
                        running = false;
                    }
                    break;
            }

        }
    }

    // TODO: disallow a user to be logged into multiple devices simultaneously (perhaps?)
    // Update this and other methods to be void. Each method will handle writing back information.
    public boolean login(String username, String password) {
        for (User u : db.getUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                return true;
            }
        }
        return false;
    }

    public boolean register(String username, String password) {
        if (db.addUser(new User(username, password))) {
            currentUser = new User(username, password);
            return true;
        }
        return false;
    }

    /*public boolean sendMessage(String otherUsername);
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

    public boolean logout();*/

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

