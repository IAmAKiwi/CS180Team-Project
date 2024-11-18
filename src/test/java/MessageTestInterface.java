/**
 * Interface defining test cases for Message class.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
public interface MessageTestInterface {
    void testBasicMessage();

    void testEmptyMessage();

    void testNullMessage();

    void testLongMessage();

    void testSpecialCharacters();

    void testUnicodeCharacters();

    void testMessageWithOnlySpaces();

    void testMessageWithMultipleLines();

    void testMessageWithEmptySender();

    void testMessageWithNullSender();
}