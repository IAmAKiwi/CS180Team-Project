import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class that handles all of the client-side functionality.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 *
 * @version Nov 2, 2024
 */
// add methods to set first name, last name, bio, birthday, profile pic,
// friends, blocks separately.
public class Client implements Runnable, ClientInterface {
    public BufferedReader serverReader;
    public PrintWriter serverWriter;
    public Socket socket;
    private String resultt; // for testing

    public String getResult() {
        return resultt;
    }

    public Client() {
        try {
            socket = new Socket("localhost", 4242);
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                Scanner sc = new Scanner(System.in);
                String line = sc.nextLine();
                if (line == null) {
                    break;
                }
                char groupSeparator = 29;
                // take in the command that the GUI sends, differentiation by the word before
                // the first colon
                String command = line.substring(0, line.indexOf(':'));
                // split the rest of the line into an array of arguments, separated by
                // colons
                String content = line.substring(line.indexOf(':') + 1);
                String result = "";
                switch (command) {
                    case "login":
                        // Login example: login:username[groupSeparator]password
                        result = String.valueOf(login(content));
                        break;
                    case "register":
                        // Register example: register:username[groupSeparator]password
                        result = String.valueOf(register(content));
                        break;
                    case "getUserList":
                        // Get user list example: getUserList:
                        result = getUserList();
                        break;
                    case "getChat":
                        // Get chat example: getChat:username
                        result = getChat(line);
                        break;
                    case "getChatList":
                        // Get chat list example: getChatList:
                        result = getChatList();
                        break;
                    case "createChat":
                        // Create chat example: createChat:username
                        result = String.valueOf(createChat(content));
                        break;
                    case "sendMessage":
                        // Message example: sendMessage:username[groupSeparator]message
                        result = String.valueOf(sendMessage(content));
                        break;
                    case "deleteMessage":
                        // Delete message example:
                        // deleteMessage[groupSeparator]message[groupSeparator]otherUser
                        result = String.valueOf(deleteMessage(content));
                        break;
                    case "accessProfile":
                        // Access profile example: accessProfile:
                        result = accessProfile();
                        break;
                    case "saveProfile":
                        result = String.valueOf(saveProfile(content));
                        break;
                    case "removeFriend":
                        // Remove friend example: removeFriend:friend
                        result = String.valueOf(removeFriend(content));
                        break;
                    case "addFriend":
                        // Add friend example: addFriend:friend
                        result = String.valueOf(addFriend(content));
                        break;
                    case "unblockUser":
                        // Unblock user example: unblockUser:user
                        result = String.valueOf(unblockUser(content));
                        break;
                    case "blockUser":
                        // Block user example: blockUser:user
                        result = String.valueOf(blockUser(content));
                        break;
                    case "deleteChat":
                        // Delete chat example: deleteChat:username
                        result = String.valueOf(deleteChat(content));
                        break;
                    case "sendImage":
                        // Send image example: sendImage:username[groupSeparator]imagePath
                        result = String.valueOf(sendImage(content));
                        break;
                    case "getBlockList":
                        // Get block list example: getBlockList:
                        result = getBlockList();
                        break;
                    case "getFriendList":
                        // Get friend list example: getFriendList:
                        result = getFriendList();
                        break;
                    case "isFriendsOnly":
                        // Is friends only example: isFriendsOnly:
                        result = String.valueOf(isFriendsOnly());
                        break;
                    case "setFriendsOnly":
                        // Set friends only example: setFriendsOnly:booleanValue
                        result = String.valueOf(setFriendsOnly(content));
                        break;
                    case "setProfilePic":
                        // Set profile pic example: setProfilePic:profilePicPath
                        result = String.valueOf(setProfilePic(content));
                        break;
                    case "getProfilePic":
                        // Get profile pic example: getProfilePic:
                        result = getProfilePic();
                        break;
                    case "logout":
                        result = String.valueOf(logout());
                        break;
                    case "disconnect":
                        result = String.valueOf(disconnect());
                        if (result.equals("true")) {
                            running = false;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command: " + command);
                }
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Getusers

    public String getUserList() {
        return requestData("getUserList: ");
    }

    public boolean sendMessage(String content) {
        String command = "sendMessage:" + content; //
        return sendCommand(command);
    }

    public boolean deleteMessage(String content) {
        String command = "deleteMessage:" + content;
        return sendCommand(command);
    }

    public String accessProfile() {
        return requestData("accessProfile: ");
    }

    public boolean saveProfile(String content) {
        String command = "saveProfile:" + content;
        return sendCommand(command);
    }

    public boolean removeFriend(String friend) {
        String command = "removeFriend:" + friend;
        return sendCommand(command);
    }

    public boolean addFriend(String friend) {
        String command = "addFriend:" + friend;
        return sendCommand(command);
    }

    public boolean unblockUser(String user) {
        String command = "unblockUser:" + user;
        return sendCommand(command);
    }

    public boolean blockUser(String otherUsername) {
        String command = "blockUser:" + otherUsername;
        return sendCommand(command);
    }

    // the other user
    public boolean deleteChat(String otherUser) {
        String command = "deleteChat:" + otherUser;
        return sendCommand(command);
    }

    public boolean sendImage(String content) {
        String command = "sendImage:" + content;
        return sendCommand(command);
    }

    public String getChat(String otherUsername) {
        String command = "getChat:" + otherUsername;
        return requestData(command);
    }

    public String getChatList() {
        return requestData("getChatList: ");
    }

    public boolean createChat(String otherUsername) {
        String command = "createChat:" + otherUsername;
        return sendCommand(command);
    }

    public String getFriendList() {
        return requestData("getFriendList: ");
    }

    public String getBlockList() {
        return requestData("getBlockList: ");
    }

    public boolean isFriendsOnly() {
        String command = "isFriendsOnly: ";
        return sendCommand(command);
    }

    public boolean setFriendsOnly(String booleanValue) {
        return sendCommand("setFriendsOnly:" + booleanValue);
    }

    public boolean setProfilePic(String profilePic) {
        return sendCommand("setProfilePic:" + profilePic);
    }

    public String getProfilePic() {
        return requestData("getProfilePic: ");
    }

    public boolean login(String content) {
        String command = "login:" + content;
        return sendCommand(command);
    }

    public boolean register(String content) {
        String command = "register:" + content;
        return sendCommand(command);
    }

    public boolean logout() {
        return sendCommand("logout: ");
    }

    // May not be needed, potentially included in "logout".
    public boolean disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized String requestData(String command) {
        try {
            if (!socket.isConnected()) {
                throw new IOException("Socket is closed");
            }
            serverWriter.println(command);
            serverWriter.flush();
            String response = serverReader.readLine();
            if (response == null) {
                throw new IOException("Socket is closed");
            }
            return response;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    public synchronized boolean sendCommand(String command) {
        String response = this.requestData(command);
        return response != null && Boolean.parseBoolean(response.trim());
    }

    // IMPORTANT!! Command format is lowercase command name, colon (:), arguments
    // seperated by colon (username:password).
    // NO SPACES!!
    // In addition, use the above functions to send and read the commands.
    // Abstraction is good.
    // format all remaining methods in this fashion

    public boolean updateProfile(String content) {
        return sendCommand("updateProfile:" + content);
    }

    public static void main(String[] args) {
        Thread t = new Thread(new Client());
        t.start();
    }

}