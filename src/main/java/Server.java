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
            String result = "";
            switch (command) {
                case "login":
                    result = login(content);
                    break;
                case "register":
                    result = register(content);
                    break;
                case "getUserList":
                    result = getUserList();
                    break;
                case "getChat":
                    result = getChat(content);
                    break;
                case "sendMessage":
                    result = sendMessage(content);
                    break;
                case "deleteMessage":
                    result = deleteMessage(content);
                    break;
                case "accessProfile":
                    result = accessProfile();
                    break;
                case "updateProfile":
                    result = updateProfile(content);
                    break;
                case "removeFriend":
                    result = removeFriend(content);
                    break;
                case "addFriend":
                    result = addFriend(content);
                    break;
                case "unblockUser":
                    result = unblockUser(content);
                    break;
                case "blockUser":
                    result = blockUser(content);
                    break;
                case "requestActive":
                    result = requestActive(content);
                    break;
                case "deleteChat":
                    result = deleteChat(content);
                    break;
                case "sendImage":
                    result = sendImage(content);
                    break;
                case "openChat":
                    result = openChat(content);
                    break;
                case "getBlockList":
                    result = getBlockList();
                    break;
                case "getFriendList":
                    result = getFriendList();
                    break;
                case "isFriendsOnly":
                    result = isFriendsOnly();
                    break;
                case "setFriendsOnly":
                    result = setFriendsOnly(Boolean.parseBoolean(content));
                    break;
                case "setProfilePic":
                    result = setProfilePic(content);
                    break;
                case "getProfilePic":
                    result = getProfilePic();
                    break;
                case "logout":
                    result = logout();
                    break;
                case "disconnect":
                    if (disconnect()) {
                        running = false;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid command: " + command);
            }
            send(result);
        }
    }

    public String requestActive(String user) {
        return db.requestActive(user);
    }

    public String deleteMessage(String content) {
        MessageHistory mh = db.getMessages(currentUser.getUsername(), otherUser);
        String sender = content.substring(0, content.indexOf(':'));
        String message = content.substring(content.indexOf(':') + 1);
        for (Message m : mh.getMessageHistory()) {
            if (m.getMessage().equals(message) && m.getSender().equals(sender)) {
                mh.deleteMessage(m);
                return "true";
            }
        }
        return "false";
    }

    public String updateProfile(String content) {
        char groupSeparator = 29;
        String[] info = content.split(groupSeparator + "");
        String oldUsername = info[0];
        User user = db.getUser(oldUsername);
        String username = info[1];
        String password = info[2];
        String firstName = info[3];
        String lastName = info[4];
        String bio = info[5];
        String[] birthdaystr = info[6].split("/");
        int[] birthday = new int[3];
        for (int i = 0; i < 3; i++) {
            int b = Integer.parseInt(birthdaystr[i]);
            birthday[i] = b;
        }
        String profilePic = info[7];
        String[] friendsArray = info[8].split(",");
        ArrayList<String> friends = new ArrayList<String>();
        for (String friend : friendsArray) {
            friends.add(friend);
        }
        String[] blockedArray = info[9].split(",");
        ArrayList<String> blocked = new ArrayList<String>();
        for (String block : blockedArray) {
            blocked.add(block);
        }
        Boolean friendsOnly = Boolean.valueOf(info[10]);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBio(bio);
        user.setBirthday(birthday);
        user.setProfilePic(profilePic);
        user.setFriends(friends);
        user.setBlocked(blocked);
        user.setFriendsOnly(friendsOnly);
        return "true";
    }

    public String accessProfile() {
        return currentUser.toString();
    }

    public String deleteChat(String user) {
        db.deleteChat(currentUser.getUsername(), user);
        return "true";
    }

    public boolean disconnect() {
        try {
            clientSocket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getProfilePic() {
        return db.getUser(currentUser.getUsername()).getProfilePic();
    }

    // TODO: disallow a user to be logged into multiple devices simultaneously
    // (perhaps?)
    // Update this and other methods to be void. Each method will handle writing
    // back information.
    public String login(String content) {
        String username = content.substring(0, content.indexOf(':'));
        String password = content.substring(content.indexOf(':') + 1);
        for (User u : db.getUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                return "true";
            }
        }
        return "false";
    }

    public String register(String content) {
        String username = content.substring(0, content.indexOf(':'));
        String password = content.substring(content.indexOf(':') + 1);
        if (db.addUser(new User(username, password))) {
            currentUser = new User(username, password);
            return "true";
        }
        return "false";
    }

    /**
     * Returns a String of usernames of all users.
     * username:username:etc
     * 
     * @return String of users.
     */
    public String getUserList() {
        ArrayList<User> users = db.getUsers();
        String userList = "";
        for (int i = 0; i < users.size(); i++) {
            userList += users.get(i).getUsername() + ":";
        }
        return userList;
    }

    /**
     * Returns a String of messages using the toString() of each message in format
     * username: message[endChar]username: message[endChar] etc.
     * [endChar] = (char) 29
     * 
     * @param content The username of the other user
     * @return String of all messages
     */
    public String getChat(String content) {
        ArrayList<Message> messages = db.getMessages(currentUser.getUsername(), content).getMessageHistory();
        String chat = "";
        char endChar = (char) 29;
        for (int i = 0; i < messages.size(); i++) {
            chat += messages.get(i).toString();
            chat += endChar;
        }
        return chat;
    }

    @Override
    public String sendMessage(String content) {
        try {
            String userTwo = content.substring(0, content.indexOf(':'));
            String message = content.substring(content.indexOf(':') + 1);
            Message mes = new Message(message, currentUser.getUsername());
            db.addMessage(mes, userTwo);
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    public String sendImage(String content) {
        try {
            String userTwo = content.substring(0, content.indexOf(','));
            String path = content.substring(content.indexOf(',') + 1);
            db.addPhotos(path);
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    public String openChat(String otherUsername) {
        currentChat = db.getMessages(currentUser.getUsername(), otherUsername);
        return "true";
    }

    public String addFriend(String otherUsername) {
        if (db.addFriend(currentUser.getUsername(), otherUsername)) {
            return "true";
        }
        return "false";
    }

    /**
     * Returns a String of usernames of all friends for the current user
     * username:username:etc
     * 
     * @return String of friends
     */
    public String getFriendList() {
        String[] friends = db.getFriends(currentUser.getUsername());
        String friendList = "";
        for (int i = 0; i < friends.length; i++) {
            friendList += friends[i] + ":";
        }
        return friendList;
    }

    public String blockUser(String otherUsername) {
        if (db.blockUser(currentUser.getUsername(), otherUsername)) {
            return "true";
        }
        return "false";
    }

    /**
     * Returns a String of usernames of all users blocked by the current user
     * username:username:etc.
     * 
     * @return
     */
    public String getBlockList() {
        String[] blocks = db.getBlockList(currentUser.getUsername());
        String blockList = "";
        for (int i = 0; i < blocks.length; i++) {
            blockList += blocks[i] + ":";
        }
        return blockList;
    }

    public String removeFriend(String otherUsername) {
        if (db.removeFriend(currentUser.getUsername(), otherUsername)) {
            return "true";
        }
        return "false";
    }

    public String unblockUser(String otherUsername) {
        if (db.unblockUser(currentUser.getUsername(), otherUsername)) {
            return "true";
        }
        return "false";
    }

    public String isFriendsOnly() {
        if (currentUser.isFriendsOnly()) {
            return "true";
        }
        return "false";
    }

    public String setFriendsOnly(boolean friendsOnly) {
        currentUser.setFriendsOnly(friendsOnly);
        return "true";
    }

    public String setProfilePic(String profilePic) {
        db.getUser(currentUser.getUsername()).setProfilePic(profilePic);
        return "true";
    }

    public String logout() {
        currentUser = null;
        return "true";
    }

    public void send(String result) {

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
