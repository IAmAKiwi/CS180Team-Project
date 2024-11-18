/**
 * Interface that defines the required test methods for Server functionality.
 * Ensures that all server operations are tested for correctness and
 * reliability.
 * 
 * Each method represents a specific test case for a feature or functionality
 * within the Server class.
 * 
 * @version Nov 2, 2024
 */
public interface ServerTestInterface {
    void testRegister();

    void testLoginAndLogout();

    void testUserList();

    void testChatting();

    void testDeleteMessage();

    void testFriends();

    void testFriendsOnly();

    void testProfile();

    void testInvalidRegistration();

    void testProfileValidation();
}