import javax.swing.JTextField;

/**
 * Interface defining public methods for login panel functionality
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface LoginPanelInterface extends Runnable {
    /**
     * Add placeholder style to text field
     * 
     * @param textField TextField to style
     */
    void addPlaceHolderStyle(JTextField textField);

    /**
     * Remove placeholder style from text field
     * 
     * @param textField TextField to update
     */
    void removePlaceholderStyle(JTextField textField);
    void addPlaceHolderStyle(JTextField textField);
    /**
     * Gets the client instance
     * 
     * @return Client object
     */
    Client getClient();

    /**
     * Gets the username
     * 
     * @return Current username
     */
    String getUsername();

    /**
     * Gets the password
     * 
     * @return Current password
     */
    String getPassword();

    /**
     * Checks if login panel is done/closed
     * 
     * @return true if panel is finished
     */
    boolean isDone();

    /**
     * Checks if client is connected
     * 
     * @return true if connected
     */
    boolean isConnected();

    /**
     * Gets resource path
     * 
     * @param item   Item name
     * @param folder Folder name
     * @return Full path to resource
     */
    String getPath(String item, String folder);
}