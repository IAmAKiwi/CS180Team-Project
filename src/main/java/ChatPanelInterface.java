import java.io.IOException;

/**
 * Interface defining public methods for chat panel functionality
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface ChatPanelInterface {
    /**
     * Refreshes the chat display with messages between current user and selected
     * user
     * 
     * @param selectedUser Username of selected chat partner
     * @throws IOException If chat data cannot be loaded
     */
    void refreshChat(String selectedUser) throws IOException;
}