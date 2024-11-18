/**
 * Interface defining test cases for Database class.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
 * @version Nov 2, 2024
 */
public interface DatabaseTestInterface {
    void testAddUserUniqueUsername();

    void testValidateNewUserUsernameInPassword();

    void testGetUserExistingUser();

    void testGetUserNonExistentUser();

    void testGetUsers();

    void testGetMessagesValidConversation();

    void testGetMessagesSelfMessagingException();

    void testSaveUsersFileOutput();

    void testSaveUsersCreatesFileIfNotExists();

    void testSaveMessagesFileOutput();

    void testLoadMessagesFileInput();

    void testAddMessageWithBlockedUser();

    void testAddMessageWithFriendsOnlyMode();

    void testGetMessagesNonExistentUsers();

    void testLoadUsersWithInvalidFormat();
} 