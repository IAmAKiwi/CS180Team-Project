import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Interface defining public methods for profile panel functionality
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface ProfilePanelInterface {
    /**
     * Gets the profile picture path/data
     * 
     * @return String containing profile picture information
     */
    String getProfilePic();

    /**
     * Gets friend and block buttons
     * 
     * @return Array of JButtons containing friend and block buttons
     */
    JButton[] getFriendAndBlockButtons();

    /**
     * Creates component with friends and blocks lists
     * 
     * @param saveCallback Callback for save action
     * @param friendList   List of friends
     * @param blockList    List of blocked users
     */
    void createComponent(ActionListener saveCallback, ArrayList<String> friendList, ArrayList<String> blockList);

    /**
     * Initializes labels with profile information
     * 
     * @param profileInfo Array of profile information strings
     */
    void initializeLabels(String[] profileInfo);

    /**
     * Creates black background panel
     * 
     * @param screenSize Dimension object with screen size
     * @return JPanel configured as background
     */
    JPanel createBlackBackgroundPanel(Dimension screenSize);

    /**
     * Formats all labels with consistent styling
     */
    void formatLabels();

    /**
     * Creates friend and block buttons
     */
    void createFriendAndBlockButtons();

    /**
     * Refreshes profile with new information
     * 
     * @param username    Username
     * @param firstName   First name
     * @param lastName    Last name
     * @param bio         Bio text
     * @param month       Birth month
     * @param day         Birth day
     * @param year        Birth year
     * @param profilePic  Profile picture data
     * @param friendsOnly Friends only setting
     */
    void refreshProfile(String username, String firstName, String lastName, String bio,
            String month, String day, String year, String profilePic, String friendsOnly);

    /**
     * Gets resource path
     * 
     * @param item   Item name
     * @param folder Folder name
     * @return Full path to resource
     */
    String getPath(String item, String folder);
}