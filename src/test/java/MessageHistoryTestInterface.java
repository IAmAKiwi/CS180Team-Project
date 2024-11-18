/**
 * Interface defining test cases for MessageHistory class.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface MessageHistoryTestInterface {
    void testBasicMessageHistory();

    void testEmptyMessageHistory();

    void testDeleteMessage();

    void testMessageHistoryEquality();

    void testNullMessageHistory();

    void testLargeMessageHistory();

    void testMultipleUsersMessageHistory();

    void testMessageHistoryWithNullMessage();

    void testDeleteNonExistentMessage();

    void testMessageHistoryConstructorWithMessage();
}