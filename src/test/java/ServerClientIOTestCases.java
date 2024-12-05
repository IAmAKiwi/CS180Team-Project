import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * A framework to run public test cases for Server and Client IO.
 * This class tests the interaction between the client and server,
 * ensuring that all functionalities such as registration, login,
 * messaging, and user management work as expected.
 * 
 * The tests are ordered to ensure dependencies are respected.
 * 
 * @version Nov 15, 2024
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerClientIOTestCases implements ServerClientIOTestInterface {
    char groupSeparator = (char) 29;
    static Client client;

    /**
     * Sets up the client and registers a test user before all tests.
     * 
     * @throws IOException if an I/O error occurs when setting up the client.
     */
    @BeforeAll
    public static void setUp() throws IOException {
        client = new Client();
        client.register("testUser" + ((char) 29) + "testPass1$");
        client.logout();
    }

    /**
     * Disconnects the client after all tests are completed.
     * 
     * @throws IOException if an I/O error occurs when disconnecting the client.
     */
    @AfterAll
    public static void tearDown() throws IOException {
        client.disconnect();
    }

    /**
     * Logs in as the test user before each test.
     */
    @BeforeEach
    public void login() {
        try {
            client.login("testUser" + groupSeparator + "testPass1$");
        } catch (IOException e) {}
    }

    /**
     * Logs out the test user after each test.
     */
    @AfterEach
    public void logout() {
        try {
            client.logout();
        } catch (IOException e) {}
    }

    /**
     * Tests user registration functionality.
     * Ensures that a new user can be registered successfully.
     */
    @Test
    @Order(1)
    public void testRegister() {
        try {
            client.logout();
            boolean result = client.register("newUser" + groupSeparator + "newPass1$");
            assertTrue(result);
            client.login("testUser" + groupSeparator + "testPass1$");
            client.deleteChat("newUser");
        } catch (IOException e) {}
    }

    /**
     * Tests re-login functionality.
     * Ensures that a user can log in with correct credentials and fails with incorrect ones.
     */
    @Test
    @Order(2)
    public void testReLogin() {
        try {
            assertFalse(client.login("newUser" + groupSeparator + "wrongPass1$"));
            assertTrue(client.login("newUser" + groupSeparator + "newPass1$"));
        } catch (IOException e) {}
    }

    /**
     * Tests message sending functionality.
     * Ensures that a message can be sent and retrieved correctly.
     */
    @Test
    @Order(3)
    public void testSendMessage() {
        try {
            client.deleteChat("newUser");

            boolean sent = client.sendMessage("newUser" + groupSeparator + "hello");
            assertTrue(sent);

            String chat = client.getChat("newUser");
            assertEquals("testUser: hello" + groupSeparator, chat);
        } catch (IOException e) {}
    }

    /**
     * Tests chat retrieval functionality.
     * Ensures that the chat history can be retrieved correctly.
     */
    @Test
    @Order(4)
    public void testGetChat() {
        try {
            String chat = client.getChat("newUser");
            assertEquals("testUser: hello" + groupSeparator, chat);
        } catch (IOException e) {}
    }

    /**
     * Tests friend management functionality.
     * Ensures that friends can be added and removed correctly.
     */
    @Test
    @Order(5)
    public void testFriends() {
        try {
            assertTrue(client.addFriend("newUser"));
            String friends = client.getFriendList();
            assertTrue(friends.contains("newUser"));
            assertTrue(client.removeFriend("newUser"));

            assertFalse(client.addFriend("nobody"));
            assertFalse(client.removeFriend("nobody"));
        } catch (IOException e) {}
    }

    /**
     * Tests block management functionality.
     * Ensures that users can be blocked and unblocked correctly.
     */
    @Test
    @Order(6)
    public void testBlocks() {
        try {
            assertTrue(client.blockUser("newUser"));
            String blocks = client.getBlockList();
            assertTrue(blocks.contains("newUser"));

            assertFalse(client.sendMessage("newUser" + groupSeparator + "we are blocked"));
            client.logout();
            client.login("newUser" + groupSeparator + "newPass1$");
            assertFalse(client.sendMessage("testUser" + groupSeparator + "we are blocked"));

            assertFalse(client.unblockUser("newUser"));

            assertFalse(client.blockUser("nobody"));
            assertFalse(client.unblockUser("nobody"));

            client.logout();
            client.login("testUser" + groupSeparator + "testPass1$");

            assertTrue(client.unblockUser("newUser"));
            blocks = client.getBlockList();
            assertFalse(blocks.contains("newUser"));
        } catch (IOException e) {}
    }

    /**
     * Tests friends-only mode functionality.
     * Ensures that messaging is restricted when friends-only mode is enabled.
     */
    @Test
    @Order(7)
    public void testFriendsOnly() {
        try {
            assertTrue(client.setFriendsOnly("true"));

            assertFalse(client.sendMessage("newUser" + groupSeparator + "we are friends"));

            assertTrue(client.setFriendsOnly("false"));

            assertTrue(client.sendMessage("newUser" + groupSeparator + "we are not friends"));
        } catch (IOException e) {}
    }

    /**
     * Tests message and chat deletion functionality.
     * Ensures that messages and chats can be deleted correctly.
     */
    @Test
    @Order(8)
    public void testDeletion() {
        try {
            String currentChat = client.getChat("newUser");
            assertEquals("testUser: hello" + groupSeparator + "testUser: we are not friends" + groupSeparator,
                    currentChat);

            assertTrue(client.deleteMessage("testUser: hello" + groupSeparator + "newUser"));
            assertFalse(client.deleteMessage("testUser: hello" + groupSeparator + "hello"));

            currentChat = client.getChat("newUser");
            assertEquals("testUser: we are not friends" + groupSeparator, currentChat);

            assertTrue(client.deleteChat("newUser"));
            assertFalse(client.deleteChat("newUser"));
            assertEquals("", client.getChat("newUser"));
        } catch (IOException e) {}
    }

    /**
     * Tests profile management functionality.
     * Ensures that user profiles can be saved and accessed correctly.
     */
    @Test
    @Order(9)
    public void testProfile() {
        try {
            assertTrue(client.saveProfile("testUser" + groupSeparator + "jack" + groupSeparator + "shields" +
                    groupSeparator + "epic bio" + groupSeparator + "5/15/2000" + groupSeparator +
                    "profile.png" + groupSeparator + "true"));
            assertEquals("username: testUser" + groupSeparator + "firstName: jack" + groupSeparator
                    + "lastName: shields" + groupSeparator + "bio: epic bio" + groupSeparator +
                    "birthday: 5/15/2000" + groupSeparator + "profilePic: profile.png" + +groupSeparator +
                    "friendsOnly: true" + groupSeparator, client.accessProfile());
        } catch (IOException e) {}
    }

    /**
     * Tests invalid registration scenarios.
     * Ensures that registration fails with weak passwords, duplicate usernames, and empty fields.
     */
    @Test
    @Order(10)
    public void testInvalidRegistration() {
        try {
            logout();
            assertFalse(client.register("user1" + groupSeparator + "weak"));
            assertFalse(client.register("user1" + groupSeparator + "nospecial123"));
            assertFalse(client.register("user1" + groupSeparator + "nouppercase1!"));
            assertFalse(client.register("user1" + groupSeparator + "NOLOWERCASE1!"));

            assertFalse(client.register("testUser" + groupSeparator + "ValidPass1!"));

            assertFalse(client.register("" + groupSeparator + "ValidPass1!"));
            assertFalse(client.register("user1" + groupSeparator + ""));
        } catch (IOException e) {}
    }

    /**
     * Tests message validation scenarios.
     * Ensures that invalid messages are not sent.
     */
    @Test
    @Order(11)
    public void testMessageValidation() {
        try {
            assertFalse(client.sendMessage("newUser" + groupSeparator + ""));
            assertFalse(client.sendMessage("newUser" + groupSeparator + "test\0message"));
            assertFalse(client.sendMessage("nonexistentUser" + groupSeparator + "hello"));
        } catch (IOException e) {}
    }

    /**
     * Tests profile validation scenarios.
     * Ensures that invalid profile data is not saved.
     */
    @Test
    @Order(12)
    public void testProfileValidation() {
        try {
            assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator +
                    "Doe" + groupSeparator + "Bio" + groupSeparator + "13/1/2000" + groupSeparator +
                    "pic.jpg" + groupSeparator + "false"));
            assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator +
                    "Doe" + groupSeparator + "Bio" + groupSeparator + "12/32/2000" + groupSeparator +
                    "pic.jpg" + groupSeparator + "false"));
            assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator +
                    "Doe" + groupSeparator + "Bio" + groupSeparator + "12/25/2025" + groupSeparator +
                    "pic.jpg" + groupSeparator + "false"));
        } catch (IOException e) {}
    }

    /**
     * Tests multi-user chat functionality.
     * Ensures that messages can be sent and received correctly between multiple users.
     */
    @Test
    @Order(13)
    public void testMultiUserChat() {
        try {
            client.deleteChat("newUser");
            client.deleteChat("user3");

            client.logout();
            client.register("user3" + groupSeparator + "Pass3$");

            client.login("testUser" + groupSeparator + "testPass1$");

            client.setFriendsOnly("false");

            String blocks = client.getBlockList();
            if (blocks.contains("newUser")) {
                client.unblockUser("newUser");
            }
            if (blocks.contains("user3")) {
                client.unblockUser("user3");
            }

            assertTrue(client.sendMessage("newUser" + groupSeparator + "test message 1"));
            assertTrue(client.sendMessage("user3" + groupSeparator + "test message 2"));

            String chatWithNewUser = client.getChat("newUser");
            String chatWithUser3 = client.getChat("user3");

            assertTrue(chatWithNewUser.contains("test message 1"));
            assertTrue(chatWithUser3.contains("test message 2"));
            assertFalse(chatWithNewUser.contains("test message 2"));
            assertFalse(chatWithUser3.contains("test message 1"));
        } catch (IOException e) {}
    }

    /**
     * Tests blocked user interactions.
     * Ensures that blocked users cannot send messages or add friends.
     */
    @Test
    @Order(14)
    public void testBlockedUserInteractions() {
        try {
            String blocks = client.getBlockList();
            if (blocks.contains("user3")) {
                client.unblockUser("user3");
            }

            assertTrue(client.blockUser("user3"));

            client.logout();
            client.login("user3" + groupSeparator + "Pass3$");
            assertFalse(client.sendMessage("testUser" + groupSeparator + "blocked message"));
            assertFalse(client.addFriend("testUser"));

            client.logout();
            client.login("testUser" + groupSeparator + "testPass1$");
            assertFalse(client.sendMessage("user3" + groupSeparator + "message to blocked"));
        } catch (IOException e) {}
    }

    /**
     * Tests friends-only mode interactions.
     * Ensures that messaging is restricted to friends when friends-only mode is enabled.
     */
    @Test
    @Order(15)
    public void testFriendsOnlyModeInteractions() {
        try {
            client.setFriendsOnly("false");
            String blocks = client.getBlockList();
            if (blocks.contains("newUser")) {
                client.unblockUser("newUser");
            }

            assertTrue(client.setFriendsOnly("true"));
            assertTrue(client.addFriend("newUser"));

            assertTrue(client.sendMessage("newUser" + groupSeparator + "friend message"));
            assertFalse(client.sendMessage("user3" + groupSeparator + "non-friend message"));

            assertTrue(client.removeFriend("newUser"));
            assertFalse(client.sendMessage("newUser" + groupSeparator + "after unfriend"));

            client.setFriendsOnly("false");
        } catch (IOException e) {}
    }
}
