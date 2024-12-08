import java.util.ArrayList;

/**
 * Manages photo sharing history between users
 * 
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui
 * @version Nov 2, 2024
 */
public class PhotoHistory implements PhotoHistoryInterface {
    private String[] userPhotographers;
    private ArrayList<Photo> photoHistory;

    /**
     * Default constructor
     */
    public PhotoHistory() {
        userPhotographers = null;
        photoHistory = new ArrayList<Photo>();
    }

    /**
     * Constructor for a new photo sharing history between two users
     * 
     * @param photo     First shared photo. Contains sender's username
     * @param recipient Username of photo recipient
     */
    public PhotoHistory(Photo photo, String recipient) {
        userPhotographers = new String[2];
        photoHistory = new ArrayList<Photo>();
        userPhotographers[1] = recipient;
        userPhotographers[0] = photo.getSender();
        photoHistory.add(photo);
    }

    /**
     * Constructor for multi-user photo sharing
     * 
     * @param users Array of usernames involved in sharing
     */
    public PhotoHistory(String[] users) {
        userPhotographers = users;
        photoHistory = new ArrayList<Photo>();
    }

    public String getRecipient() {
        return userPhotographers[1];
    }

    public String getSender() {
        return userPhotographers[0];
    }

    public void deletePhoto(Photo photo) {
        photoHistory.remove(photo);
    }

    public ArrayList<Photo> getPhotoHistory() {
        return photoHistory;
    }

    public String[] getUsernames() {
        return userPhotographers;
    }

    /**
     * Adds a photo to the history
     * 
     * @param photo Photo to be added
     */
    public void addPhoto(Photo photo) {
        photoHistory.add(photo);
    }

    public void setPhotoHistory(ArrayList<Photo> photoHistory) {
        this.photoHistory = photoHistory;
    }

    public void setUserPhotographers(String[] userPhotographers) {
        this.userPhotographers = userPhotographers;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (!(other instanceof PhotoHistory)) {
            return false;
        }

        PhotoHistory ph = (PhotoHistory) other;
        return (ph.userPhotographers[0].equals(this.userPhotographers[0]) &&
                ph.userPhotographers[1].equals(this.userPhotographers[1])) ||
                (ph.userPhotographers[0].equals(this.userPhotographers[1]) &&
                        ph.userPhotographers[1].equals(this.userPhotographers[0]));
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.userPhotographers[0], this.userPhotographers[1]);
    }
}