import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerGUI implements Runnable {
    private int port = -1;
    private String host;
    private ServerSocket serverSocket;
    private boolean running = false;
    private Thread serverThread;
    private ArrayList<Server> servers = new ArrayList<>();
    private ArrayList<Socket> serverSockets = new ArrayList<>();
    private JFrame frame;
    private JButton startButton;
    private JButton saveButton;
    private JButton stopButton;
    private JButton stopAndSaveButton;
    private Server server;

    public ServerGUI(Server server) {
        this.server = server;
        JFrame f = new JFrame();
        JLabel portLabel = new JLabel("Port:");
        JLabel hostLabel = new JLabel("Host:");
        JTextArea portField = new JTextArea();
        portField.setRows(1);
        JTextArea hostField = new JTextArea();
        hostField.setRows(1);
        portField.setText("4242");
        hostField.setText("localhost");

        JButton done = new JButton("Done");
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    port = Integer.parseInt(portField.getText());
                    host = hostField.getText();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid port number",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                f.dispose();
            }
        });

        f.setLayout(new GridLayout(3, 2, 10, 10));
        f.add(portLabel);
        f.add(portField);
        f.add(hostLabel);
        f.add(hostField);
        f.add(done);
        f.setSize(300, 200);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public boolean isReady() {
        return port != -1;
    }

    public void run() {
        frame = new JFrame("Server");
        frame.setLayout(new GridLayout(4, 1));
        startButton = new JButton("Start");
        saveButton = new JButton("Save");
        stopButton = new JButton("Stop");
        stopAndSaveButton = new JButton("Stop and Save");
        frame.add(startButton);
        frame.add(saveButton);
        frame.add(stopButton);
        frame.add(stopAndSaveButton);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Database db = new Database();
        db.loadMessages();
        db.loadUsers();
        Server.setDatabase(db);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    System.out.println("Starting server...");
                    try {
                        serverSocket = new ServerSocket(4242);
                    } catch (IOException eIO) {
                        eIO.printStackTrace();
                    }
                    serverThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                Socket socket;
                                try {
                                    socket = serverSocket.accept();
                                } catch (IOException eIO) {
                                    break;
                                }
                                serverSockets.add(socket);
                                server = new Server(socket);
                                servers.add(server);
                                Thread t = new Thread(server);
                                t.start();
                            }
                        }
                    });
                    serverThread.start();
                    running = true;
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.saveMessages();
                db.saveUsers();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    System.out.println("Stopping server...");
                    serverThread.interrupt();
                    running = false;
                    System.out.println("Stopping client connections...");
                    for (Socket s : serverSockets) {
                        try {
                            s.close();
                        } catch (IOException eIO) {
                            eIO.printStackTrace();
                        }
                    }
                    System.out.println("Closing server socket...");
                    try {
                        serverSocket.close();
                    } catch (IOException eIO) {
                        eIO.printStackTrace();
                    }
                }
            }
        });
        stopAndSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    System.out.println("Stopping server and saving data...");
                    serverThread.interrupt();
                    running = false;
                    System.out.println("Stopping client connections...");
                    for (Socket s : serverSockets) {
                        try {
                            s.close();
                        } catch (IOException eIO) {
                            eIO.printStackTrace();
                        }
                    }
                    db.saveMessages();
                    db.saveUsers();
                    System.out.println("Closing server socket...");
                    try {
                        serverSocket.close();
                    } catch (IOException eIO) {
                        eIO.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        Server server = new Server();
        ServerGUI gui = new ServerGUI(server);
        while (!gui.isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(gui);
    }
}
