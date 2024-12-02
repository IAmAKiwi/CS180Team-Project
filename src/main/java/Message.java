/**
 * Class that represents a message in the social media platform.
 * Contains the message content and sender information.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public class Message implements MessageInterface {

    /*private String date;
    private String time;*/

    private String contents;
    private String senderUsername;

    /**
     * Default constructor.
     */
    public Message() {
        this.contents = null;
        this.senderUsername = null;
    }

    /**
     * Main constructor for the class Message.
     *
     * @param contents       The text that makes up the Message.
     * @param senderUsername The username of the User that sent this Message.
     */
    public Message(String contents, String senderUsername) {
        this.contents = contents;
        this.senderUsername = senderUsername;
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
     * toString method for Message. It is in File Format.
     */
    @Override
    public String toString() {
        return String.format("%s: %s", this.senderUsername, this.contents);
        // return String.format(%s:%s:%s: %s, this.date, this.time, this.senderUsername, this.contents);
    }
}
