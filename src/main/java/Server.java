import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;

/**
 * Typical use flow of a client/server connection:
 * Client connects to server.
 * Client either logs in or registers. (should not have to check if they are
 * logged in, should be done implicitly by GUI)
 * Client opens a chat (server sends back the data to display)
 * Client sends a message (server updates the data)
 * Whenever a
 *
 */

public class Server implements Runnable, ServerInterface {
    public static Object userKey = new Object(); // Synchronization used for Users access
    public static Object messageKey = new Object(); // Synchronization used for Messages access
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
        String line = "";
        while (running) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String command = line.substring(0, line.indexOf(':'));
            String content = line.substring(line.indexOf(':') + 1);
            switch (command) {
                case "login":
                    login(content);
                    break;
                case "register":
                    register(content);
                    break;
                case "getChat":
                    getChat(content);
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

    // TODO: disallow a user to be logged into multiple devices simultaneously
    // (perhaps?)
    // Update this and other methods to be void. Each method will handle writing
    // back information.
    public boolean login(String content) {
        String username = content.substring(0, content.indexOf(':'));
        String password = content.substring(content.indexOf(':') + 1);
        for (User u : db.getUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                return true;
            }
        }
        return false;
    }

    public boolean register(String content) {
        String username = content.substring(0, content.indexOf(':'));
        String password = content.substring(content.indexOf(':') + 1);
        if (db.addUser(new User(username, password))) {
            currentUser = new User(username, password);
            return true;
        }
        return false;
    }

    @Override
    public boolean sendMessage(String content) {
        try {
            String userTo = content.substring(0, content.indexOf(':'));
            String message = content.substring(content.indexOf(':') + 1);
            Message mes = new Message(message, currentUser.getUsername());
            db.addMessage(mes, userTo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendImage(String content) {
        try {
            String userTo = content.substring(0, content.indexOf(','));
            String path = content.substring(content.indexOf(',') + 1);
            db.addPhotos(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean openChat(String otherUsername) {
        otherUser = otherUsername;
        currentChat = db.getChat(currentUser.getUsername(), otherUsername);
        return true;
    }

    public boolean addFriend(String otherUsername) {
        return db.addFriend(currentUser.getUsername(), otherUsername);
    }

    public String[] getFriendList() {
        return db.getFriends(currentUser.getUsername());
    }

    public boolean addBlock(String otherUsername) {
        return db.addBlock(currentUser.getUsername(), otherUsername);
    }

    public String[] getBlockList() {
        return db.getBlockList(currentUser.getUsername());
    }

    public boolean removeFriend(String otherUsername) {
        return db.removeFriend(currentUser.getUsername(), otherUsername);
    }

    public boolean removeBlock(String otherUsername) {
        return db.removeBlock(currentUser.getUsername(), otherUsername);
    }

    public boolean isFriendsOnly(String otherUsername) {
        return db.isFriendsOnly(currentUser.getUsername(), otherUsername);
    }

    public boolean setFriendsOnly(boolean friendsOnly) {
        return db.setFriendsOnly(currentUser.getUsername(), otherUser, friendsOnly);
    }

    public boolean setProfilePic(String profilePic) {
        return db.setProfilePic(currentUser.getUsername(), profilePic);
    }

    public boolean logout() {
        currentUser = null;
        return true;
    }

    public void confirmExecution(Object o) {

    }

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
