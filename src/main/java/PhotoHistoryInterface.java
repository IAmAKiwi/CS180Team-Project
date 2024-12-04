import java.util.ArrayList;

/**
 * Interface defining requirements for managing photo sharing history between users
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public interface PhotoHistoryInterface {
    /**
     * Gets the recipient username
     * @return String of recipient's username
     */
    String getRecipient();

    /**
     * Gets the sender username
     * @return String of sender's username
     */
    String getSender();

    /**
     * Removes a photo from history
     * @param photo Photo to be deleted
     */
    void deletePhoto(Photo photo);

    /**
     * Gets the complete photo history
     * @return ArrayList of Photos
     */
    ArrayList<Photo> getPhotoHistory();

    /**
     * Gets array of usernames involved in photo sharing
     * @return String array of usernames
     */
    String[] getUsernames();

    /**
     * Adds a new photo to history
     * @param photo Photo to be added
     */
    void addPhoto(Photo photo);

    /**
     * Sets the entire photo history
     * @param photoHistory New ArrayList of Photos
     */
    void setPhotoHistory(ArrayList<Photo> photoHistory);

    /**
     * Sets the users involved in photo sharing
     * @param userPhotographers Array of usernames
     */
    void setUserPhotographers(String[] userPhotographers);
}