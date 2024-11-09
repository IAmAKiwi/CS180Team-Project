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


    public boolean sendMessage(String content) {
        String command = "sendMessage:" + content;
        return sendCommand(command);
    }

    public boolean deleteMessage(String content) {
        String command = "deleteMessage:" + content;
        return sendCommand(command);
    }

    public boolean sendPhoto(String content) {
        String command = "sendPhoto:" + content;
        return sendCommand(command);
    }

    public String accessProfile() {
        return requestData("accessProfile:");
    }

    public boolean updateProfile(String content) {
        String command = "updateProfile:" + content;
        return sendCommand(command);
    }

    public synchronized boolean sendCommand(String command) {
        try {
            serverWriter.write(comand);
            serverWriter.flush();
        } catch (Exception ioe) {
            return false;
        }
        return true;
    }

    public synchronized String requestData(String command) {
        try {
            serverWriter.println(command);
        } catch (Exception ioe) {
            e.printStackTrace();
        }
        serverWriter.flush();
        return serverReader.readLine();
    }

    public boolean boolCommand(String command) throws IOException {
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
