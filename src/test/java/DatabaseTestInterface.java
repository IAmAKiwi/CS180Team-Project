import java.io.IOException;
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
    void testSaveUsersFileOutput() throws IOException;
    void testSaveUsersCreatesFileIfNotExists();
    void testSaveMessagesFileOutput() throws IOException;
    void testLoadMessagesFileInput() throws IOException;
    void testAddMessageWithBlockedUser();
    void testAddMessageWithFriendsOnlyMode();
    void testGetMessagesNonExistentUsers();
    void testLoadUsersWithInvalidFormat();
} 