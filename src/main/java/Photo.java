import java.time.Instant;
import java.util.Date;

/**
 * Class that represents a photo in the social media platform.
 * Contains the photo path and sender information.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public class Photo implements PhotoInterface {

    private String photoPath;
    private String senderUsername;
    private Date timeStamp;
    private String caption;

    /**
     * Default constructor.
     */
    public Photo() {
        this.photoPath = null;
        this.senderUsername = null;
        this.timeStamp = Date.from(Instant.now());
    }

    /**
     * Main constructor for the class Photo.
     *
     * @param photoPath The file path to the photo
     * @param senderUsername The username of the User that sent this Photo
     * @param caption Optional caption for the photo
     */
    public Photo(String photoPath, String senderUsername) {
        this();
        this.photoPath = photoPath;
        this.senderUsername = senderUsername;
        this.timeStamp = Date.from(Instant.now());
    }

    /**
     * Getter for the photo path.
     */
    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    /**
     * Getter for the username of the sender.
     */
    public String getSender() {
        return this.senderUsername;
    }

    /**
     * Getter for the caption.
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * Getter for the time stamp.
     */
    public Date getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * toString method for Photo in File Format.
     */
    @Override
    public String toString() {
        char gs = 29;
        return this.senderUsername + gs + this.photoPath + gs + this.timeStamp.getTime();
    }
}