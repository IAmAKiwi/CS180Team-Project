import java.util.Date;
import java.time.Instant;
/**
 * Class that represents a message in the social media platform.
 * Contains the message content and sender information.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public class Message implements MessageInterface {

    private String contents;
    private String senderUsername;
    private Date timeStamp;

    /**
     * Default constructor.
     */
    public Message() {
        this.contents = null;
        this.senderUsername = null;
        this.timeStamp = Date.from(Instant.now());
        //(GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getTimeZone("America/Indiana/Indianapolis"));
    }

    /**
     * Main constructor for the class Message.
     *
     * @param contents       The text that makes up the Message.
     * @param senderUsername The username of the User that sent this Message.
     */
    public Message(String contents, String senderUsername) {
        this();
        this.contents = contents;
        this.senderUsername = senderUsername;
    }

    /**
     * Constructor implementing timeStamp setting.
     * 
     * @param contents      The text that makes up the Message.
     * @param senderUsername The username of the User that sent this Message.
     * @param timeStamp The long variable that will set the timeStamp.
     */
    public Message(String contents, String senderUsername, long timeStamp)
    {
        this(contents, senderUsername);
        this.timeStamp = new Date(timeStamp);
    }

    /**
     * Getter for the contents of this Message.
     */
    public String getMessage() {
        return this.contents;
    }

    /**
     * Getter for the username of the sender of this Message.
     */
    public String getSender() {
        return this.senderUsername;
    }

    /**
     * Getter for the time stamp of this Message.
     */
    public Date getTimeStamp()
    {
        return this.timeStamp;
    }

    /**
     * toString method for Message. It is in File Format.
     */
    @Override
    public String toString() {
        return String.format("%d:%s: %s", this.timeStamp.getTime(), this.senderUsername, this.contents);
    }
}
