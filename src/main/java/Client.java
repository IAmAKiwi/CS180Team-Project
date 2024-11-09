import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
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

    public boolean addBlock(String content) {
        String command = "addBlock:" + content;
        return sendCommand(command);
    }

    public String getBlockList() {
        return requestData("getBlockList:");
    }

    public boolean isFriendsOnly() {
        return boolCommand("isFriendsOnly:");
    }

    public boolean setFriendsOnly(String content) {
        String command = "setFriendsOnly:" + content;
        return sendCommand(command);
    }

    public boolean setProfilePic(String content) {
        String command = "setProfilePic" + content;
        return sendCommand(content);
    }

    public boolean logout() {

    retrun boolCommand("logout:");
    }

    public synchronized boolean sendCommand(String command) {
        try {
            serverWriter.write(command);
            serverWriter.flush();
        } catch (Exception e) {
            return false;
        }
        return true;
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

    public String[]


    public synchronized String requestData(String command) {
        try {
            serverWriter.println(command);
            serverWriter.flush();
            return serverReader.readLine();
        } catch (Exception ioe) {
            e.printStackTrace();
        }
    }

    // maybe add synchronized or not
    public synchronized boolean sendCommand(String command) {

    public boolean boolCommand(String command) {
        return Boolean.parseBoolean(this.requestData(command).trim()); // The trim is key.
    }

    // IMPORTANT!! Command format is lowercase command name, colon (:), arguments
    // seperated by semicolons (username;password).
    // NO SPACES!!
    // In addition, use the above functions to send and read the commands.
    // Abstraction is good.
    // TODO: format all remaining methods in this fashion

    public boolean receivelogin(String username, String password) {
        try {
            return boolCommand(String.format(
                    "login:%s;%s", username, password));
        } catch (Exception ioe) {
            return false;
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(new Client());
        t.start();
    }

}