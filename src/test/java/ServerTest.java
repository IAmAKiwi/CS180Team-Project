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
 * A framework to run public test cases for Server
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 * lab sec 12
 * @version Nov 15, 2024
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerTest {
    public static char groupSeparator = (char) 29;
    public static Server server;

    @BeforeAll
    public static void setUp() {
        server = new Server();
        server.setDatabase(new Database());
        server.register("testUser" + groupSeparator + "ValidPass1!");
        server.logout();
    }

    @BeforeEach
    public void login() {
        server.login("testUser" + groupSeparator + "ValidPass1!");
    }

    @AfterEach
    public void logout() {
        server.logout();
    }

    @AfterAll
    public static void tearDown() {
        server = null;
    }

    @Test
    @Order(1)
    public void testRegister() {
        logout();
        boolean result = Boolean.parseBoolean(server.register("newUser" + groupSeparator + "ValidPass2!"));
        assertTrue(result);
        logout();

        result = Boolean.parseBoolean(server.register("newUser" + groupSeparator + "ValidPass2!"));
        assertFalse(result);

        result = Boolean.parseBoolean(server.register("newUser" + groupSeparator + "InvalidPass1!"));
        assertFalse(result);
    }

    @Test
    @Order(2)
    public void testLoginAndLogout() {
        logout();
        boolean result = Boolean.parseBoolean(server.login("newUser" + groupSeparator + "ValidPass2!"));
        assertTrue(result);
        logout();

        result = Boolean.parseBoolean(server.login("newUser" + groupSeparator + "InvalidPass1!"));
        assertFalse(result);

        result = Boolean.parseBoolean(server.login("testUser" + groupSeparator + "ValidPass1!"));
        assertTrue(result);
    }

    @Test
    @Order(3)
    public void testUserList() {
        String result = server.getUserList();
        assertEquals("testUser" + groupSeparator + "newUser" + groupSeparator, result);

        logout();
        server.register("newUser2" + groupSeparator + "ValidPass2!");
        result = server.getUserList();
        assertEquals("testUser" + groupSeparator + "newUser" + groupSeparator + "newUser2" + groupSeparator,
                result);
    }

    @Test
    @Order(4)
    public void testChatting() {
        // Successfully sent messages back and forth.
        boolean result = Boolean.parseBoolean(server.sendMessage("newUser" + groupSeparator + "hello"));
        assertTrue(result);
        String chat = server.getChat("newUser");
        assertEquals("testUser: hello" + groupSeparator, chat);
        logout();
        server.login("newUser" + groupSeparator + "ValidPass2!");
        result = Boolean.parseBoolean(server.sendMessage("testUser" + groupSeparator + "hello"));
        chat = server.getChat("testUser");
        assertEquals("testUser: hello" + groupSeparator + "newUser: hello" + groupSeparator, chat);


        // Failed to send messages.
        result = Boolean.parseBoolean(server.sendMessage("newUser" + groupSeparator + "hello"));
        assertFalse(result);

        result = Boolean.parseBoolean(server.sendMessage("fakeUser" + groupSeparator + "hello"));
        assertFalse(result);

        result = Boolean.parseBoolean(server.sendMessage("newUser" + groupSeparator + ""));
        assertFalse(result);

        result = Boolean.parseBoolean(server.sendMessage("newUser" + groupSeparator + "hello\0world"));
        assertFalse(result);
    }

    @Test
    @Order(5)
    public void testDeleteMessage() {
        boolean result = Boolean.parseBoolean(server.deleteMessage("testUser: hello" +
                groupSeparator + "newUser"));
        assertTrue(result);
        result = Boolean.parseBoolean(server.deleteMessage("testUser: hello" + groupSeparator + "newUser"));
        assertFalse(result);
    }

    @Test
    @Order(6)
    public void testFriends() {
        // test adding and removing friends
        assertTrue(Boolean.parseBoolean(server.addFriend("newUser")));
        String friends = server.getFriendList();
        assertTrue(friends.contains("newUser"));
        assertTrue(Boolean.parseBoolean(server.removeFriend("newUser")));

        // test adding and removing non-existent friends
        assertFalse(Boolean.parseBoolean(server.addFriend("nobody")));
        assertFalse(Boolean.parseBoolean(server.removeFriend("nobody")));
    }

    @Test
    @Order(7)
    public void testFriendsOnly() {
        assertTrue(Boolean.parseBoolean(server.setFriendsOnly(true)));

        assertFalse(Boolean.parseBoolean(server.sendMessage("newUser" + groupSeparator + "we are friends")));

        assertTrue(Boolean.parseBoolean(server.setFriendsOnly(false)));

        assertTrue(Boolean.parseBoolean(server.sendMessage("newUser" + groupSeparator + "we are not friends")));
    }

    @Test
    @Order(9)
    public void testProfile() {
        assertTrue(Boolean.parseBoolean(server.saveProfile("testUser" + groupSeparator
                + "jack" + groupSeparator + "shields" +
                groupSeparator + "epic bio" + groupSeparator + "5/15/2000" + groupSeparator +
                "profile.png" + groupSeparator + "true")));
        assertEquals("testUser" + "jack" + "shields" + "epic bio" + "5/15/2000" +
                "profile.png" + "true", server.accessProfile());
    }

    @Test
    @Order(10)
    public void testInvalidRegistration() {
        logout();
        // Test weak passwords
        assertFalse(Boolean.parseBoolean(server.register("user1" + groupSeparator
                + "weak"))); // too short
        assertFalse(Boolean.parseBoolean(server.register("user1" + groupSeparator
                + "nospecial123"))); // no special char
        assertFalse(Boolean.parseBoolean(server.register("user1" + groupSeparator
                + "nouppercase1!"))); // no uppercase
        assertFalse(Boolean.parseBoolean(server.register("user1" + groupSeparator
                + "NOLOWERCASE1!"))); // no lowercase

        // Test duplicate username
        assertFalse(Boolean.parseBoolean(server.register("testUser" + groupSeparator + "ValidPass1!")));

        // Test empty fields
        assertFalse(Boolean.parseBoolean(server.register("" + groupSeparator + "ValidPass1!")));
        assertFalse(Boolean.parseBoolean(server.register("user1" + groupSeparator + "")));
    }

    @Test
    @Order(11)
    public void testProfileValidation() {
        // Test invalid birthday formats
        assertFalse(Boolean.parseBoolean(server.saveProfile("testUser" + groupSeparator
                + "John" + groupSeparator +
                "Doe" + groupSeparator + "Bio" + groupSeparator + "13/1/2000" + groupSeparator +
                "pic.jpg" + groupSeparator + "false"))); // invalid month

        assertFalse(Boolean.parseBoolean(server.saveProfile("testUser" + groupSeparator + "John"
                + groupSeparator + "Doe" + groupSeparator + "Bio" + groupSeparator + "12/32/2000" + groupSeparator +
                "pic.jpg" + groupSeparator + "false"))); // invalid day

        assertFalse(Boolean.parseBoolean(server.saveProfile("testUser" + groupSeparator + "John"
                + groupSeparator + "Doe" + groupSeparator + "Bio" + groupSeparator + "12/25/2025" + groupSeparator +
                "pic.jpg" + groupSeparator + "false"))); // future date
    }
}