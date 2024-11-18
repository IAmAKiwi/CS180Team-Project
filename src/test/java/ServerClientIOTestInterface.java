
/**
 * A framework to run public test cases for Server and Client IO.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 *         lab sec 12
 * @version Nov 17, 2024
 */
public interface ServerClientIOTestInterface {
    void testRegister();

    void testReLogin();

    void testSendMessage();

    void testGetChat();

    void testFriends();

    void testBlocks();

    void testFriendsOnly();

    void testDeletion();

    void testProfile();

    void testInvalidRegistration();

    void testMessageValidation();

    void testProfileValidation();

    void testMultiUserChat();

    void testBlockedUserInteractions();

    void testFriendsOnlyModeInteractions();
}