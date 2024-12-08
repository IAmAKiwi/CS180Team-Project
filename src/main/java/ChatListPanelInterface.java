import java.io.IOException;

/**
 * Interface defining methods for chat list panel functionality
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface ChatListPanelInterface {
    /**
     * Refreshes the list of chats
     * 
     * @param chats Array of chat usernames to display
     * @throws IOException If chat data cannot be loaded
     */
    void refreshChats(String[] chats) throws IOException;

    /**
     * Adds a new chat to the list
     * 
     * @param chat Username to add
     */
    void addChat(String chat);

    /**
     * Removes a chat from the list
     * 
     * @param chat Username to remove
     */
    void removeChat(String chat);

    /**
     * Gets the currently selected chat
     * 
     * @return Username of selected chat or null if none selected
     */
    String getSelectedChat();

    /**
     * Updates the chat list UI components
     */
    void updateUI();
}