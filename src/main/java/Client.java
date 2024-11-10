import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable, ClientInterface {
    public BufferedReader serverReader;
    public PrintWriter serverWriter;

    public void run() {
        try {
            Socket socket = new Socket("localhost", 4242);
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Switch based on commands from GUI

    }

    public String getMessage() {
        return requestData("receiveMessage:");
    }

    public boolean sendMessage(String content) {
        String command = "sendMessage:" + content;
        return sendCommand(command);
    }

    public boolean deleteMessage(String content) {
        String command = "deleteMessage:" + content;
        return sendCommand(command);
    }

    public String accessProfile() {
        return requestData("accessProfile:");
    }

    public boolean updateProfile(String content) {
        String command = "updateProfile:" + content;
        return sendCommand(command);
    }

    public boolean removeFiend(String friend) {
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

    // String output of requestActive can be "active" or "inactive"
    public boolean requestActive(String otherUser) {
        String command = "requestActive:" + otherUser;
        return sendCommand(command);
    }

    // the other user
    public boolean deleteChat(String user) {
        String command = "deleteChat:" + user;
        return sendCommand(command);
    }

    public boolean sendImage(String otherUsername, String imagePath) {
        String command = "sendImage:" + otherUsername + "," + imagePath;
        return sendCommand(command);
    }

    public String openChat(String otherUsername) {
        String command = "openChat:" + otherUsername;
        return requestData(command);
    }

    public String getFriendList() {
        return requestData("getFriendList:");
    }

    public boolean addBlock(String otherUsername) {
        String command = "addBlock:" + otherUsername;
        return sendCommand(command);
    }

    public String getBlockList() {
        return requestData("getBlockList:");
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
        return requestData("getProfilePic:");
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
    // seperated by semicolons (username;password).
    // NO SPACES!!
    // In addition, use the above functions to send and read the commands.
    // Abstraction is good.
    // TODO: format all remaining methods in this fashion

    public boolean receiveLogin(String username, String password) {
        String command = "receiveLogin:" + username + ":" + password;
        return sendCommand(command);
    }

    public boolean receiveLogout() {
        return sendCommand("receiveLogout:");
    }

    public static void main(String[] args) {
        Thread t = new Thread(new Client());
        t.start();
    }

}