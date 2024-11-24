import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * 
 * 
 * @author William Thain, lab sec 12
 * 
 * @version Nov 24, 2024
 */
public class LoginPanel extends JComponent
{
    private JFrame frame;
    private Container content;
    private JPanel contentPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Client client;
    private LoginPanel loginPanel;
    private JLabel message;

    public LoginPanel(Client client)
    {
        super();
        this.client = client;
        loginPanel = this;
        frame = new JFrame("Login/Register");
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        message = new JLabel("Insert our message here");

        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField.setMaximumSize(new Dimension(300,25));
        passwordField.setMaximumSize(new Dimension(300,25));

        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(actionListener);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(actionListener);

        contentPanel.add(message);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(usernameField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(passwordField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(loginButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(registerButton);
        
        content.add(contentPanel, BorderLayout.CENTER);
        
        frame.setSize(650, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    ActionListener actionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == loginButton)
            {
                if (client.login(usernameField.getText() + (char)29 + String.valueOf(passwordField.getPassword())))
                {
                    //Successful login

                    frame.dispose();
                    
                }
                else
                {
                    //Unsuccessful login

                    passwordField.setText("");
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (e.getSource() == registerButton)
            {
                if (client.register(usernameField.getText() + (char)29 + String.valueOf(passwordField.getPassword())))
                {
                    //Successful registry (and login?)

                    frame.dispose();
                }
                else
                {
                    //Unsuccessful registry

                    passwordField.setText("");
                    usernameField.setText("");
                    JOptionPane.showMessageDialog(null, "Invalid, unavailable, or insecure username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    
    };

    // for temporary testing
    //public static void main(String[] args) {
    //    Client c = new Client();
    //    Thread t = new Thread(c);
    //    new LoginPanel(c);
    //    t.run();
    //}
}
