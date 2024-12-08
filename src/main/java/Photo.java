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

    private String photoEncode;
    private String senderUsername;
    private Date timeStamp;
    private String caption;

    /**
     * Default constructor.
     */
    public Photo() {
        this.photoEncode = null;
        this.senderUsername = null;
        this.timeStamp = Date.from(Instant.now());
    }

    /**
     * Main constructor for the class Photo.
     *
     * @param photoPath The file path to the photo
     * @param senderUsername The username of the User that sent this Photo
     * @param timeStamp The time stamp of the photo
     */
    public Photo(String photoPath, String senderUsername, long timeStamp) {
        this();
        this.photoEncode = photoPath;
        this.senderUsername = senderUsername;
        this.timeStamp = new Date(timeStamp);
    }

    /**
     * Main constructor for the class Photo.
     *
     * @param photoPath The file path to the photo
     * @param senderUsername The username of the User that sent this Photo
     */
    public Photo(String photoPath, String senderUsername) {
        this();
        this.photoEncode = photoPath;
        this.senderUsername = senderUsername;
        this.timeStamp = Date.from(Instant.now());
    }

    /**
     * Getter for the photo path.
     */
    public String getphotoEncode() {
        return this.photoEncode;
    }

    public void setPhotoEncode(String photoPath) {
        this.photoEncode = photoPath;
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
        String gs = ":";
        return this.timeStamp.getTime() + gs + this.senderUsername + gs + this.photoEncode;
    }
}