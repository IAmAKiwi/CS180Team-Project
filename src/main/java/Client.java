import java.io.*;
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

    public String requestData(String command) throws IOException
    {
        serverWriter.println(command);
        serverWriter.flush();
        return serverReader.readLine();
    }

    public boolean boolCommand(String command) throws IOException
    {
        return Boolean.parseBoolean(this.requestData(command).trim()); // The trim is key.
    }

    // IMPORTANT!! Command format is lowercase command name, colon (:), arguments seperated by semicolons (username;password).
    // NO SPACES!!
    // In addition, use the above functions to send and read the commands. Abstraction is good.
    //TODO: format all remaining methods in this fashion

    public boolean login(String username, String password)
    {
        try
        {
            return boolCommand(String.format(
                "login:%s;%s", username, password));
        } catch (IOException ioe)
        {
            return false;
        }
    }
    public static void main(String[] args) {
        Thread t = new Thread(new Client());
        t.start();
    }

}
