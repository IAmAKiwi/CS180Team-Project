import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public class Server implements Runnable, ServerInterface {
    private static ServerSocket serverSocket;
    private static Database db;
    private static int clientCount = 0;
    protected Socket clientSocket;
    protected User currentUser;
    private final char groupSeparatorChar = (char) 29;

    public Server(Socket socket) {
        clientSocket = socket;
    }

    public Server() {
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

        boolean running = true;
        String line = "";
        while (running) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) {
                break;
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
                case "getChatList":
                    result = getChatList();
                    break;
                case "createChat":
                    result = createChat(content);
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
                // case "updateProfile":
                // result = updateProfile(content);
                // break;
                case "saveProfile":
                    result = saveProfile(content);
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
                case "deleteChat":
                    result = deleteChat(content);
                    break;
                case "sendImage":
                    result = sendImage(content);
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
                        clientCount--;
                    } else {
                        result = "false";
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid command: " + command);
            }
            if (result != null) {
                send(result, writer);
            }
        }
    }

    public String deleteMessage(String content) {
        String[] info = content.split(groupSeparatorChar + "");
        if (info.length != 2 || !info[0].contains(":")) {
            return "false";
        }
        String sender = info[0].substring(0, info[0].indexOf(":"));
        String messageContent = info[0].substring(info[0].indexOf(":") + 2);
        Message message = new Message(messageContent, sender);
        String otherUser = info[1];
        if (db.deleteMessage(message, otherUser)) {
            return "true";
        }
        return "false";
    }

    @Deprecated
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

    public String saveProfile(String content) {
        /*
         * Format input as:
         * usernameGSfirstnameGSlastnameGSbioGSbirthdayasMM/DD/
         * YYYYGSprofilepicGSfriendsonly
         */
        try {
            String[] fields = content.split((char) 29 + "");
            User user = db.getUser(fields[0]);
            if (user == null) {
                return "false";
            }

            // Store original birthday to check if validation passed
            int[] originalBirthday = user.getBirthday();

            user.setFirstName(fields[1]);
            user.setLastName(fields[2]);
            user.setBio(fields[3]);

            // Birthday validation
            String[] birthdaystr = fields[4].split("/");
            if (birthdaystr.length != 3) {
                return "false";
            }

            int[] birthday = new int[3];
            try {
                for (int i = 0; i < 3; i++) {
                    birthday[i] = Integer.parseInt(birthdaystr[i]);
                }

                // Use User's setBirthday method for validation
                user.setBirthday(birthday);

                // If birthday didn't change, validation failed
                if (user.getBirthday() == null ||
                        (originalBirthday != null && user.getBirthday() == originalBirthday)) {
                    return "false";
                }
            } catch (NumberFormatException e) {
                return "false";
            }

            user.setProfilePic(fields[5]);
            user.setFriendsOnly(Boolean.parseBoolean(fields[6].trim()));
            return "true";
        } catch (Exception e) {
            return "false";
        }
    }

    public String accessProfile() {
        return currentUser.toString();
    }

    public String deleteChat(String user) {
        try {
            // Check if user is logged in
            if (currentUser == null) {
                return "false";
            }

            if (db.deleteChat(currentUser.getUsername(), user)) {
                return "true";
            }
            return "false";
        } catch (Exception e) {
            return "false";
        }
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

    public String login(String content) {
        String[] credentials = splitContent(content);
        String username = credentials[0];
        String password = credentials[1];
        for (User u : db.getUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                return "true";
            }
        }
        return "false";
    }

    public String register(String content) {
        try {
            String[] credentials = splitContent(content);
            if (credentials.length < 2) {
                return "false";
            }
            String username = credentials[0];
            String password = credentials[1];
            if (db.addUser(new User(username, password))) {
                currentUser = new User(username, password);
                return "true";
            }
            return "false";
        } catch (Exception e) {
            return "false";
        }
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
            userList += users.get(i).getUsername() + groupSeparatorChar;
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
        MessageHistory mh = db.getMessages(currentUser.getUsername(), content);
        if (mh == null) {
            return "";
        }
        ArrayList<Message> messages = mh.getMessageHistory();
        String chat = "";
        char endChar = (char) 29;
        for (int i = 0; i < messages.size(); i++) {
            chat += messages.get(i).toString();
            chat += endChar;
        }
        return chat;
    }

    public String getChatList() {
        String[] chats = db.getAllUserChats(currentUser.getUsername());
        String chatList = "";
        for (int i = 0; i < chats.length; i++) {
            chatList += chats[i] + groupSeparatorChar;
        }
        return chatList;
    }

    public String createChat(String content) {
        try {
            String[] parts = splitContent(content);
            String userTwo = parts[0];
            String[] users = {currentUser.getUsername(), userTwo};
            MessageHistory mh = new MessageHistory(users);
            return String.valueOf(db.addMessageHistory(mh));
        } catch (Exception e) {
            return "false";
        }
    }

    @Override
    public String sendMessage(String content) {
        try {
            String[] parts = splitContent(content);
            // Check if we have both recipient and message
            if (parts.length < 2) {
                return "false";
            }

            String userTwo = parts[0];
            String message = parts[1];

            // Check for self-messaging
            if (currentUser != null && userTwo.equals(currentUser.getUsername())) {
                return "false";
            }

            // Check for null/empty message
            if (message == null || message.isEmpty() || message.contains("\0")) {
                return "false";
            }

            Message mes = new Message(message, currentUser.getUsername());
            return String.valueOf(db.addMessage(mes, userTwo));
        } catch (Exception e) {
            return "false";
        }
    }

    public String sendImage(String content) {
        try {
            String[] parts = splitContent(content);
            String userTwo = parts[0];
            String path = parts[1];
            db.addPhotos(path);
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
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

    public void send(String result, PrintWriter writer) {
        writer.println(result);
        writer.flush();
    }

    public static void setDatabase(Database database) {
        db = database;
    }

    public static Database getDatabase() {
        return db;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setClientSocket(Socket socket) {
        this.clientSocket = socket;
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    // Helper method to split content with group separator
    private String[] splitContent(String content) {
        if (content == null || content.isEmpty()) {
            return new String[0];
        }
        try {
            return content.split(String.valueOf(groupSeparatorChar));
        } catch (Exception e) {
            return new String[0];
        }
    }

    public static void main(String[] args) {
        db = new Database();
        db.loadMessages();
        db.loadUsers();
        if (!(args.length > 0)) {
            Runtime.getRuntime().addShutdownHook(new Thread(new DataSaver(db)));
        }

        try {
            serverSocket = new ServerSocket(4242);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                clientCount++;
                new Thread(new Server(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
