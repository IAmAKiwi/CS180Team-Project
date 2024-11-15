import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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
    private static ServerSocket serverSocket;
    private static Database db;
    protected Socket clientSocket;
    protected User currentUser;
    private String otherUser;
    private MessageHistory currentChat; // Maybe, have to update our chat a bunch.
    private char GS = (char) 29;

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
                // case "requestActive":
                // result = requestActive(content);
                // break;
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
        db.saveMessages();
        db.saveUsers();
    }

    // public String requestActive(String user) {
    // return db.requestActive(user);
    // }

    public String deleteMessage(String content) {
        String[] parts = content.split(String.valueOf((char) 29));
        String sender = parts[0];
        String message = parts[1];
        MessageHistory mh = db.getMessages(currentUser.getUsername(), sender);
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
        String[] credentials = content.split(":");
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
        String[] credentials = content.split(String.valueOf(GS));
        String username = credentials[0];
        String password = credentials[1];
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
            String[] parts = content.split(String.valueOf((char) 29));
            String userTwo = parts[0];
            String message = parts[1];
            Message mes = new Message(message, currentUser.getUsername());
            return String.valueOf(db.addMessage(mes, userTwo));
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    public String sendImage(String content) {
        try {
            String[] parts = content.split(String.valueOf((char) 29));
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

    // public static void main(String[] args) {
    // db = new Database();
    // db.loadMessages();
    // db.loadUsers();

    // try {
    // serverSocket = new ServerSocket(4242);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }

    // while (true) {
    // try {
    // Socket socket = serverSocket.accept();
    // new Thread(new Server(socket)).start();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // }

    // Add these getter/setter methods for testing
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
        return content.split(String.valueOf((char) 29));
    }

    public static void main(String[] args) {
        db = new Database();
        db.loadMessages();
        db.loadUsers();
        char GS = (char) 29;
        Scanner scanner = new Scanner(System.in);
        Server server = new Server(null);
        System.out.println("Welcome to the server!");
        System.out.println("Register part: ");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        String combo = username + GS + password;
        server.register(combo);
        server.getCurrentUser();
        String commands = "1.login" + "\n" + "2.register" + "\n" + "3.getUserList" + "\n"
                + "4.getChat" + "\n" + "5.sendMessage" + "\n" + "6.deleteMessage" + "\n" + "7.accessProfile" + "\n"
                + "8.updateProfile" + "\n" + "9.removeFriend" + "\n" + "10.addFriend" + "\n" + "11.unblockUser" + "\n"
                + "12.blockUser" + "\n" + "13.deleteChat" + "\n" + "14.sendImage" + "\n" + "15.getBlockList" + "\n"
                + "16.getFriendList" + "\n" + "17.isFriendsOnly" + "\n" + "18.setFriendsOnly" + "\n"
                + "19.setProfilePic" + "\n" + "20.getProfilePic" + "\n" + "21.logout" + "\n" + "22.disconnect";
        System.out.println(commands);
        System.out.print("Enter a command: ");
        String command = scanner.nextLine();
        switch (command) {
            case "5":
                System.out.println("Enter the guy you want to send Message to: ");
                String userTo = scanner.nextLine();
                System.out.println("Enter the message you want to send: ");
                String message = scanner.nextLine();
                String response = userTo + GS + message;
                String result = server.sendMessage(response);
                break;
        }
        // sendMessage example: username[GS]message
    }
}
