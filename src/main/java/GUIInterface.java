/**
 * Interface defining public methods for GUI functionality
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface GUIInterface extends Runnable {
    /**
     * Refreshes all chat displays
     */
    void refreshChats();

    /**
     * Updates the profile panel with latest data
     */
    void updateProfilePanel();

    /**
     * Updates the chat display
     */
    void updateChat();

    /**
     * Displays a message in the chat
     * 
     * @param message Message to display
     */
    void displayMessage(String message);

    /**
     * Displays an error dialog
     * 
     * @param error Error message to display
     */
    void displayError(String error);

    /**
     * Updates all GUI components
     */
    void updateGUI();

    /**
     * Logs out current user and closes frame
     */
    void logout();

    /**
     * Disconnects client and closes frame
     */
    void disconnect();

    /**
     * Checks if GUI is done/closed
     * 
     * @return true if GUI is finished
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