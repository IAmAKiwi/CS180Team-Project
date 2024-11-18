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
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 * lab sec 12
 * @version Nov 15, 2024
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerClientIOTestCases {
    char groupSeparator = (char) 29;
    static Client client;

    @BeforeAll
    public static void setUp() throws IOException {
        client = new Client();
        client.register("testUser" + ((char) 29) + "testPass1$");
        client.logout();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        client.disconnect();
    }

    @BeforeEach
    public void login() {
        client.login("testUser" + groupSeparator + "testPass1$");
    }

    @AfterEach
    public void logout() {
        client.logout();
    }

    // Test if register is successful.
    @Test
    @Order(1)
    public void testRegister() {
        client.logout();
        boolean result = client.register("newUser" + groupSeparator + "newPass1$");
        assertTrue(result);
        client.login("testUser" + groupSeparator + "testPass1$");
        client.deleteChat("newUser");
    }

    // Log out of the registered user, then log back in to test login.
    @Test
    @Order(2)
    public void testReLogin() {
        assertFalse(client.login("newUser" + groupSeparator + "wrongPass1$"));
        assertTrue(client.login("newUser" + groupSeparator + "newPass1$"));
    }

    @Test
    @Order(3)
    public void testSendMessage() {
        client.deleteChat("newUser");

        boolean sent = client.sendMessage("newUser" + groupSeparator + "hello");
        assertTrue(sent);

        String chat = client.getChat("newUser");
        assertEquals("testUser: hello" + groupSeparator, chat);
    }

    @Test
    @Order(4)
    public void testGetChat() {
        String chat = client.getChat("newUser");
        assertEquals("testUser: hello" + groupSeparator, chat);
    }

    @Test
    @Order(5)
    public void testFriends() {
        // test adding and removing friends
        assertTrue(client.addFriend("newUser"));
        String friends = client.getFriendList();
        assertTrue(friends.contains("newUser"));
        assertTrue(client.removeFriend("newUser"));

        // test adding and removing non-existent friends
        assertFalse(client.addFriend("nobody"));
        assertFalse(client.removeFriend("nobody"));
    }

    @Test
    @Order(6)
    public void testBlocks() {
        // test blocking
        assertTrue(client.blockUser("newUser"));
        String blocks = client.getBlockList();
        assertTrue(blocks.contains("newUser"));

        // test messaging blocked users from either client
        assertFalse(client.sendMessage("newUser" + groupSeparator + "we are blocked"));
        client.logout();
        client.login("newUser" + groupSeparator + "newPass1$");
        assertFalse(client.sendMessage("testUser" + groupSeparator + "we are blocked"));

        // make sure this block is empty, though it should still fail.
        assertFalse(client.unblockUser("newUser"));

        assertFalse(client.blockUser("nobody"));
        assertFalse(client.unblockUser("nobody"));

        client.logout();
        client.login("testUser" + groupSeparator + "testPass1$");

        // test unblocking
        assertTrue(client.unblockUser("newUser"));
        blocks = client.getBlockList();
        assertFalse(blocks.contains("newUser"));
    }

    @Test
    @Order(7)
    public void testFriendsOnly() {
        assertTrue(client.setFriendsOnly("true"));

        assertFalse(client.sendMessage("newUser" + groupSeparator + "we are friends"));

        assertTrue(client.setFriendsOnly("false"));

        assertTrue(client.sendMessage("newUser" + groupSeparator + "we are not friends"));
    }

    @Test
    @Order(8)
    public void testDeletion() {
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
    }

    /*
     * Format input as:
     * usernameGSfirstnameGSlastnameGSbioGSbirthdayasMM/DD/
     * YYYYGSprofilepicGSfriendsonly
     */
    @Test
    @Order(9)
    public void testProfile() {
        assertTrue(client.saveProfile("testUser" + groupSeparator + "jack" + groupSeparator + "shields" +
                groupSeparator + "epic bio" + groupSeparator + "5/15/2000" + groupSeparator +
                "profile.png" + groupSeparator + "true"));
        assertEquals("testUser" + "jack" + "shields" + "epic bio" + "5/15/2000" +
                "profile.png" + "true", client.accessProfile());
    }

    @Test
    @Order(10)
    public void testInvalidRegistration() {
        logout();
        // Test weak passwords
        assertFalse(client.register("user1" + groupSeparator + "weak")); // too short
        assertFalse(client.register("user1" + groupSeparator + "nospecial123")); // no special char
        assertFalse(client.register("user1" + groupSeparator + "nouppercase1!")); // no uppercase
        assertFalse(client.register("user1" + groupSeparator + "NOLOWERCASE1!")); // no lowercase

        // Test duplicate username
        assertFalse(client.register("testUser" + groupSeparator + "ValidPass1!"));

        // Test empty fields
        assertFalse(client.register("" + groupSeparator + "ValidPass1!"));
        assertFalse(client.register("user1" + groupSeparator + ""));
    }

    @Test
    @Order(11)
    public void testMessageValidation() {
        // Test empty message
        assertFalse(client.sendMessage("newUser" + groupSeparator + ""));

        // Test null character in message
        assertFalse(client.sendMessage("newUser" + groupSeparator + "test\0message"));

        // Test messaging non-existent user
        assertFalse(client.sendMessage("nonexistentUser" + groupSeparator + "hello"));
    }

    @Test
    @Order(12)
    public void testProfileValidation() {
        // Test invalid birthday formats
        assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator +
                "Doe" + groupSeparator + "Bio" + groupSeparator + "13/1/2000" + groupSeparator +
                "pic.jpg" + groupSeparator + "false")); // invalid month

        assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator +
                "Doe" + groupSeparator + "Bio" + groupSeparator + "12/32/2000" + groupSeparator +
                "pic.jpg" + groupSeparator + "false")); // invalid day

        assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator +
                "Doe" + groupSeparator + "Bio" + groupSeparator + "12/25/2025" + groupSeparator +
                "pic.jpg" + groupSeparator + "false")); // future date
    }

    @Test
    @Order(13)
    public void testMultiUserChat() {
        // Clear any existing chats first
        client.deleteChat("newUser");
        client.deleteChat("user3");

        // Register user3 if not exists
        client.logout();
        client.register("user3" + groupSeparator + "Pass3$");

        // Log back in as testUser
        client.login("testUser" + groupSeparator + "testPass1$");

        // Reset user state
        client.setFriendsOnly("false");

        // Clear any blocks
        String blocks = client.getBlockList();
        if (blocks.contains("newUser")) {
            client.unblockUser("newUser");
        }
        if (blocks.contains("user3")) {
            client.unblockUser("user3");
        }

        // Send new messages
        assertTrue(client.sendMessage("newUser" + groupSeparator + "test message 1"));
        assertTrue(client.sendMessage("user3" + groupSeparator + "test message 2"));

        // Get and verify chats
        String chatWithNewUser = client.getChat("newUser");
        String chatWithUser3 = client.getChat("user3");

        assertTrue(chatWithNewUser.contains("test message 1"));
        assertTrue(chatWithUser3.contains("test message 2"));
        assertFalse(chatWithNewUser.contains("test message 2")); // Verify messages don't cross-contaminate
        assertFalse(chatWithUser3.contains("test message 1"));
    }

    @Test
    @Order(14)
    public void testBlockedUserInteractions() {
        // Clear any existing blocks first
        String blocks = client.getBlockList();
        if (blocks.contains("user3")) {
            client.unblockUser("user3");
        }

        // Block user3
        assertTrue(client.blockUser("user3"));

        // Switch to user3 and verify blocked behavior
        client.logout();
        client.login("user3" + groupSeparator + "Pass3$");
        assertFalse(client.sendMessage("testUser" + groupSeparator + "blocked message"));
        assertFalse(client.addFriend("testUser"));

        // Switch back and verify reverse blocking
        client.logout();
        client.login("testUser" + groupSeparator + "testPass1$");
        assertFalse(client.sendMessage("user3" + groupSeparator + "message to blocked"));
    }

    @Test
    @Order(15)
    public void testFriendsOnlyModeInteractions() {
        // Reset state
        client.setFriendsOnly("false");
        String blocks = client.getBlockList();
        if (blocks.contains("newUser")) {
            client.unblockUser("newUser");
        }

        // Enable friends only mode and add friend
        assertTrue(client.setFriendsOnly("true"));
        assertTrue(client.addFriend("newUser"));

        // Test messaging
        assertTrue(client.sendMessage("newUser" + groupSeparator + "friend message"));
        assertFalse(client.sendMessage("user3" + groupSeparator + "non-friend message"));

        // Remove friend and verify
        assertTrue(client.removeFriend("newUser"));
        assertFalse(client.sendMessage("newUser" + groupSeparator + "after unfriend"));

        // Clean up
        client.setFriendsOnly("false");
    }

}
