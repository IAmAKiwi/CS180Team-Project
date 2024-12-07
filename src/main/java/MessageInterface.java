import java.util.Date;
/**
 * Interface that defines the required methods for Message objects.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface MessageInterface {
    // Methods we definitely need
    String getMessage();
    Date getTimeStamp();
    String getSender();
    
    String toString();
}
