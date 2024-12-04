import java.util.Date;
/**
 * Interface that defines the requirements for a Photo object in the social media platform.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public interface PhotoInterface {
    /**
     * Gets the file path of the photo
     * @return String representing the photo's file path
     */
    String getPhotoPath();

    /**
     * Gets the username of the photo sender
     * @return String representing the sender's username
     */
    String getSender();

    /**
     * Gets the caption of the photo
     * @return String containing the photo caption
     */
    String getCaption();

    /**
     * Gets the timestamp when the photo was sent
     * @return Date object representing when the photo was sent
     */
    Date getTimeStamp();

    /**
     * Converts photo information to string format
     * @return String containing formatted photo information
     */
    String toString();
}