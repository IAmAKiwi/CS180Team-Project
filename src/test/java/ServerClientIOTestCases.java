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
 *         lab sec 12
 *
 * @version Nov 15, 2024
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerClientIOTestCases {
    char groupSeparator = (char) 29;
    static Client client;

    @BeforeAll
    public static void setUp() throws IOException {
        client = new Client();
        client.register("testUser" + ((char) 29)+ "testPass1$");
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
        boolean result = client.register("newUserL" + groupSeparator + "newPass1$");
        assertTrue(result);
    }

    // Log out of the registered user, then log back in to test login.
    @Test
    @Order(2)
    public void testReLogin() {
        client.logout();
        boolean login = client.login("newUser" + groupSeparator + "newPass1$");
        assertTrue(login);
    }

    @Test
    @Order(3)
    public void testSendMessage() {
        boolean sent = client.sendMessage("newUser" + groupSeparator + "hello");
        assertTrue(sent);
    }

    @Test
    @Order(4)
    public void testGetChat() {
        String chat = client.getChat("newUser");
        assertEquals("testUser: hello", chat.trim());
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

<<<<<<< Updated upstream
=======
    @Test
    @Order(8)
    public void testDeletion() {
        assertEquals("testUser: hello" + groupSeparator + "testUser: we are not friends" + groupSeparator,
                client.getChat("newUser"));
        assertTrue(client.deleteMessage("newUser" + groupSeparator + "hello"));
        assertFalse(client.deleteMessage("newUser" + groupSeparator + "hello"));
        assertEquals("testUser: we are not friends" + groupSeparator, client.getChat("newUser"));
        assertTrue(client.deleteChat("newUser"));
        assertFalse(client.deleteChat("newUser"));
        assertEquals("", (client.getChat("newUser")));
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
        assertEquals("testUser" + groupSeparator + "jack" + groupSeparator + "shields" +
                groupSeparator + "epic bio" + groupSeparator + "5/15/2000" + groupSeparator +
                "profile.png" + groupSeparator + "true", client.accessProfile());
    }

    @Test
    @Order(7)
    public void testMultipleMessages() {
        // Test sending multiple messages in sequence
        assertTrue(client.sendMessage("newUser" + groupSeparator + "message 1"));
        assertTrue(client.sendMessage("newUser" + groupSeparator + "message 2"));
        assertTrue(client.sendMessage("newUser" + groupSeparator + "message 3"));
        
        String chat = client.getChat("newUser");
        assertTrue(chat.contains("message 1"));
        assertTrue(chat.contains("message 2"));
        assertTrue(chat.contains("message 3"));
    }

    @Test
    @Order(8)
    public void testInvalidMessages() {
        // Test sending messages to non-existent users
        assertFalse(client.sendMessage("nonexistentUser" + groupSeparator + "hello"));
        
        // Test sending empty messages
        assertFalse(client.sendMessage("newUser" + groupSeparator + ""));
        
        // Test sending messages with invalid format
        assertFalse(client.sendMessage("newUser" + "invalid_separator" + "hello"));
    }

    @Test
    @Order(9)
    public void testProfileOperations() {
        // Test setting and getting profile information
        String profileInfo = "testUser" + groupSeparator + 
                           "John" + groupSeparator + 
                           "Doe" + groupSeparator + 
                           "Test Bio" + groupSeparator + 
                           "1/1/2000" + groupSeparator + 
                           "profile.jpg" + groupSeparator + 
                           "false";
        
        assertTrue(client.saveProfile(profileInfo));
        String profile = client.accessProfile();
        assertTrue(profile.contains("John"));
        assertTrue(profile.contains("Doe"));
        assertTrue(profile.contains("Test Bio"));
    }

    @Test
    @Order(10)
    public void testUserListOperations() {
        // Register multiple users
        client.logout();
        assertTrue(client.register("user1" + groupSeparator + "Pass1$"));
        assertTrue(client.register("user2" + groupSeparator + "Pass2$"));
        
        // Get user list and verify
        client.login("testUser" + groupSeparator + "testPass1$");
        String userList = client.getUserList();
        assertTrue(userList.contains("user1"));
        assertTrue(userList.contains("user2"));
        assertTrue(userList.contains("testUser"));
    }

    @Test
    @Order(11)
    public void testFriendsOnlyMode() {
        // Set friends only mode
        assertTrue(client.setFriendsOnly("true"));
        assertTrue(client.isFriendsOnly());
        
        // Try to message without being friends
        assertFalse(client.sendMessage("user1" + groupSeparator + "hello"));
        
        // Add friend and try again
        assertTrue(client.addFriend("user1"));
        assertTrue(client.sendMessage("user1" + groupSeparator + "hello"));
        
        // Disable friends only mode
        assertTrue(client.setFriendsOnly("false"));
        assertFalse(client.isFriendsOnly());
    }

    @Test
    @Order(12)
    public void testChatDeletion() {
        // Send some messages
        assertTrue(client.sendMessage("user1" + groupSeparator + "message 1"));
        assertTrue(client.sendMessage("user1" + groupSeparator + "message 2"));
        
        // Delete specific message
        assertTrue(client.deleteMessage("user1" + groupSeparator + "message 1"));
        String chat = client.getChat("user1");
        assertFalse(chat.contains("message 1"));
        assertTrue(chat.contains("message 2"));
        
        // Delete entire chat
        assertTrue(client.deleteChat("user1"));
        assertEquals("", client.getChat("user1"));
    }

    @Test
    @Order(13)
    public void testBlockingBehavior() {
        // Test blocking and messaging
        assertTrue(client.blockUser("user1"));
        assertFalse(client.sendMessage("user1" + groupSeparator + "blocked message"));
        
        // Test that blocked user can't message back
        client.logout();
        client.login("user1" + groupSeparator + "Pass1$");
        assertFalse(client.sendMessage("testUser" + groupSeparator + "reply message"));
        
        // Unblock and verify messaging works again
        client.logout();
        client.login("testUser" + groupSeparator + "testPass1$");
        assertTrue(client.unblockUser("user1"));
        assertTrue(client.sendMessage("user1" + groupSeparator + "unblocked message"));
    }

    @Test
    @Order(14)
    public void testProfilePicture() {
        // Test setting and getting profile picture
        String picturePath = "path/to/picture.jpg";
        assertTrue(client.setProfilePic(picturePath));
        assertEquals(picturePath, client.getProfilePic());
        
        // Test updating profile picture
        String newPicturePath = "path/to/new/picture.jpg";
        assertTrue(client.setProfilePic(newPicturePath));
        assertEquals(newPicturePath, client.getProfilePic());
    }

    @Test
    @Order(15)
    public void testLogoutBehavior() {
        // Test operations after logout
        client.logout();
        assertFalse(client.sendMessage("user1" + groupSeparator + "message"));
        assertFalse(client.addFriend("user1"));
        assertFalse(client.blockUser("user1"));
        
        // Test login required for operations
        assertTrue(client.login("testUser" + groupSeparator + "testPass1$"));
        assertTrue(client.sendMessage("user1" + groupSeparator + "message"));
    }

    @Test
    @Order(16)
    public void testConcurrentOperations() {
        // Test multiple operations in sequence
        assertTrue(client.addFriend("user1"));
        assertTrue(client.sendMessage("user1" + groupSeparator + "hello"));
        assertTrue(client.blockUser("user2"));
        assertTrue(client.setFriendsOnly("true"));
        
        // Verify state
        String friends = client.getFriendList();
        String blocks = client.getBlockList();
        assertTrue(friends.contains("user1"));
        assertTrue(blocks.contains("user2"));
        assertTrue(client.isFriendsOnly());
    }

    @Test
    @Order(17)
    public void testInvalidRegistrationScenarios() {
        client.logout();
        // Test registration with empty credentials
        assertFalse(client.register("" + groupSeparator + ""));
        assertFalse(client.register("user" + groupSeparator + ""));
        assertFalse(client.register("" + groupSeparator + "password"));
        
        // Test registration with invalid password formats
        assertFalse(client.register("user3" + groupSeparator + "short")); // too short
        assertFalse(client.register("user3" + groupSeparator + "nouppercase1!")); // no uppercase
        assertFalse(client.register("user3" + groupSeparator + "NOLOWERCASE1!")); // no lowercase
        assertFalse(client.register("user3" + groupSeparator + "NoSpecial1")); // no special char
        
        // Test registration with username in password
        assertFalse(client.register("testuser" + groupSeparator + "testuser123!"));
        
        // Test duplicate username
        assertFalse(client.register("testUser" + groupSeparator + "ValidPass1!"));
    }

    @Test
    @Order(18)
    public void testInvalidLoginScenarios() {
        client.logout();
        // Test login with empty credentials
        assertFalse(client.login("" + groupSeparator + ""));
        assertFalse(client.login("user" + groupSeparator + ""));
        assertFalse(client.login("" + groupSeparator + "password"));
        
        // Test login with non-existent user
        assertFalse(client.login("nonexistent" + groupSeparator + "Password1!"));
        
        // Test login with wrong password
        assertFalse(client.login("testUser" + groupSeparator + "wrongpass"));
        
        // Test login with case sensitivity
        assertFalse(client.login("TESTUSER" + groupSeparator + "testPass1$"));
    }

    @Test
    @Order(19)
    public void testInvalidMessageScenarios() {
        // Test messages with special characters and long content
        String longMessage = "a".repeat(1000); // Very long message
        assertFalse(client.sendMessage("user1" + groupSeparator + longMessage));
        
        // Test messages with null or special characters
        assertFalse(client.sendMessage("user1" + groupSeparator + null));
        assertFalse(client.sendMessage("user1" + groupSeparator + "\0")); // null character
        
        // Test messages to self
        assertFalse(client.sendMessage("testUser" + groupSeparator + "self message"));
        
        // Test messages with invalid separators
        assertFalse(client.sendMessage("user1,message")); // wrong separator
        assertFalse(client.sendMessage("user1" + ((char)30) + "message")); // wrong separator char
    }

    @Test
    @Order(20)
    public void testInvalidProfileOperations() {
        // Test invalid birthday formats
        assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator + 
            "Doe" + groupSeparator + "Bio" + groupSeparator + "13/13/2000" + groupSeparator + 
            "pic.jpg" + groupSeparator + "false")); // invalid month
            
        assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator + 
            "Doe" + groupSeparator + "Bio" + groupSeparator + "12/32/2000" + groupSeparator + 
            "pic.jpg" + groupSeparator + "false")); // invalid day
            
        assertFalse(client.saveProfile("testUser" + groupSeparator + "John" + groupSeparator + 
            "Doe" + groupSeparator + "Bio" + groupSeparator + "12/25/2025" + groupSeparator + 
            "pic.jpg" + groupSeparator + "false")); // future date
    }

    @Test
    @Order(21)
    public void testInvalidFriendOperations() {
        // Test adding self as friend
        assertFalse(client.addFriend("testUser"));
        
        // Test adding already added friend
        assertTrue(client.addFriend("user1"));
        assertFalse(client.addFriend("user1"));
        
        // Test removing non-friend
        assertFalse(client.removeFriend("nonexistent"));
        
        // Test removing already removed friend
        assertTrue(client.removeFriend("user1"));
        assertFalse(client.removeFriend("user1"));
    }

    @Test
    @Order(22)
    public void testInvalidBlockOperations() {
        // Test blocking self
        assertFalse(client.blockUser("testUser"));
        
        // Test blocking already blocked user
        assertTrue(client.blockUser("user1"));
        assertFalse(client.blockUser("user1"));
        
        // Test unblocking non-blocked user
        assertTrue(client.unblockUser("user1"));
        assertFalse(client.unblockUser("user1"));
        
        // Test blocking/unblocking non-existent user
        assertFalse(client.blockUser("nonexistent"));
        assertFalse(client.unblockUser("nonexistent"));
    }

    @Test
    @Order(23)
    public void testInvalidChatOperations() {
        // Test deleting non-existent chat
        assertFalse(client.deleteChat("nonexistent"));
        
        // Test deleting already deleted chat
        assertTrue(client.sendMessage("user1" + groupSeparator + "test"));
        assertTrue(client.deleteChat("user1"));
        assertFalse(client.deleteChat("user1"));
        
        // Test deleting non-existent message
        assertFalse(client.deleteMessage("user1" + groupSeparator + "nonexistent"));
        
        // Test getting chat with invalid user
        assertEquals("", client.getChat("nonexistent"));
    }

    @Test
    @Order(24)
    public void testEdgeCaseOperations() {
        // Test operations with special characters in username
        assertFalse(client.register("user!@#" + groupSeparator + "Password1!"));
        assertFalse(client.login("user!@#" + groupSeparator + "Password1!"));
        
        // Test operations with very long inputs
        String longString = "a".repeat(256);
        assertFalse(client.register(longString + groupSeparator + "Password1!"));
        assertFalse(client.sendMessage("user1" + groupSeparator + longString));
        
        // Test rapid sequential operations
        for (int i = 0; i < 10; i++) {
            client.sendMessage("user1" + groupSeparator + "message" + i);
            client.getChat("user1");
        }
    }

>>>>>>> Stashed changes
}
