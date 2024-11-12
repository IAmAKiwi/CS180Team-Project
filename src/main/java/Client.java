import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable, ClientInterface {
    public BufferedReader serverReader;
    public PrintWriter serverWriter;
    public Socket socket;

    public Client() {
        try {
            socket = new Socket("localhost", 4242);
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

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

    public boolean updateProfile(String content) {
        String command = "updateProfile:" + content;
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
        String command = "addBlock:" + otherUsername;
        return sendCommand(command);
    }

    // String output of requestActive can be "active" or "inactive"
    public boolean requestActive(String otherUser) {
        String command = "requestActive:" + otherUser;
        return sendCommand(command);
    }

    // the other user
    public boolean deleteChat(String otherUser) {
        String command = "deleteChat:" + otherUser;
        return sendCommand(command);
    }

    public boolean sendImage(String otherUsername, String imagePath) {
        String command = "sendImage:" + otherUsername + "," + imagePath;
        return sendCommand(command);
    }

    public String getChat(String otherUsername) {
        String command = "getChat:" + otherUsername;
        return requestData(command);
    }

    public String getFriendList() {
        return requestData("getFriendList: ");
    }

    public String getBlockList() {
        return requestData("getBlockList: ");
    }

    public boolean isFriendsOnly(String otherUsername) {
        String command = "isFriendsOnly:" + otherUsername;
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

    public boolean login(String username, String password) {
        String command = "login:" + username + ":" + password;
        return sendCommand(command);
    }

    public boolean register(String username, String password) {
        String command = "register:" + username + ":" + password;
        return sendCommand(command);
    }

    public boolean logout() {
        return sendCommand("logout: ");
    }

    // May not be needed, potentially included in "logout".
    public boolean disconnect() {
        boolean disconnected = sendCommand("disconnect: ");
        if (disconnected) {
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public synchronized String requestData(String command) {
        try {
            serverWriter.println(command);
            serverWriter.flush();
            return serverReader.readLine();
        } catch (IOException ioe) {
            return null;
        }
    }

    // maybe add synchronized or not
    public synchronized boolean sendCommand(String command) {
        return Boolean.parseBoolean(this.requestData(command).trim()); // The trim is key.
    }

    // IMPORTANT!! Command format is lowercase command name, colon (:), arguments
    // seperated by colon (username:password).
    // NO SPACES!!
    // In addition, use the above functions to send and read the commands.
    // Abstraction is good.
    // TODO: format all remaining methods in this fashion



    public static void main(String[] args) {
        Thread t = new Thread(new Client());
        t.start();
    }

}