public class Message implements MessageInterface 
{
    
    private String contents;
    private String senderUsername;

    /**
     * 
     * Default constructor.
     */
    public Message()
    {
        this.contents = null;
        this.senderUsername = null;
    }

    /**
     * Main constructor for the class Message.
     * @param contents The text that makes up the Message.
     * @param senderUsername The username of the User that sent this Message.
     */
    public Message(String contents, String senderUsername)
    {
        this.contents = contents;
        this.senderUsername = senderUsername;
    }

    /**
     * 
     * Getter for the contents of this Message.
     */
    public String getMessage()
    {
        return this.contents;
    }

    /**
     * 
     * Getter for the username of the sender of this Message.
     */
    public String getSender()
    {
        return this.senderUsername;
    }
}
