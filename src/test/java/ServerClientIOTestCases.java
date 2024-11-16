import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

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
        boolean result = client.register("newUser" + groupSeparator + "newPass1$");
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

}
