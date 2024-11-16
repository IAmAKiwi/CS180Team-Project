import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/*
class ClientTest {

    private Client client;
    private Socket mockSocket;
    private BufferedReader mockReader;
    private PrintWriter mockWriter;

    @BeforeEach
    void setUp() throws IOException {
        // mocking socket
        mockSocket = mock(Socket.class);

        // mock InputStream and OutputStream
        InputStream mockInputStream = new ByteArrayInputStream("mocked response".getBytes());
        OutputStream mockOutputStream = new ByteArrayOutputStream();

        // stubbing the socket's input and output streams
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        // wrap InputStream and OutputStream in BufferedReader and PrintWriter for the
        // Client instance
        mockReader = new BufferedReader(new InputStreamReader(mockInputStream));
        mockWriter = new PrintWriter(mockOutputStream, true);

        client = new Client();
        client.socket = mockSocket;
        client.serverReader = mockReader;
        client.serverWriter = mockWriter;
    }

    @Test
    void testGetUserList() {
        String response = client.getUserList();
        assertEquals("mocked response", response);
    }

    @Test
    void testSendMessage() {
        boolean result = client.sendMessage("Hello");
        assertTrue(result);
    }

    @Test
    void testDeleteMessage() {
        boolean result = client.deleteMessage("Message to delete");
        assertTrue(result);
    }

    @Test
    void testAccessProfile() {
        String response = client.accessProfile();
        assertEquals("mocked response", response);
    }

    @Test
    void testUpdateProfile() {
        boolean result = client.updateProfile("New profile content");
        assertTrue(result);
    }

    @Test
    void testRemoveFriend() {
        boolean result = client.removeFriend("friendUsername");
        assertTrue(result);
    }

    @Test
    void testAddFriend() {
        boolean result = client.addFriend("newFriendUsername");
        assertTrue(result);
    }

    @Test
    void testUnblockUser() {
        boolean result = client.unblockUser("blockedUsername");
        assertTrue(result);
    }

    @Test
    void testBlockUser() {
        boolean result = client.blockUser("userToBlock");
        assertTrue(result);
    }

    @Test
    void testRequestActive() {
        boolean result = client.requestActive("activeUser");
        assertTrue(result);
    }

    @Test
    void testDeleteChat() {
        boolean result = client.deleteChat("chatUser");
        assertTrue(result);
    }

    @Test
    void testSendImage() {
        boolean result = client.sendImage("user", "/path/to/image");
        assertTrue(result);
    }

    @Test
    void testGetChat() {
        String response = client.getChat("chatUser");
        assertEquals("mocked response", response);
    }

    @Test
    void testGetFriendList() {
        String response = client.getFriendList();
        assertEquals("mocked response", response);
    }

    @Test
    void testGetBlockList() {
        String response = client.getBlockList();
        assertEquals("mocked response", response);
    }

    @Test
    void testIsFriendsOnly() {
        boolean result = client.isFriendsOnly();
        assertTrue(result);
    }

    @Test
    void testSetFriendsOnly() {
        boolean result = client.setFriendsOnly("true");
        assertTrue(result);
    }

    @Test
    void testSetProfilePic() {
        boolean result = client.setProfilePic("/path/to/profilePic");
        assertTrue(result);
    }

    @Test
    void testGetProfilePic() {
        String response = client.getProfilePic();
        assertEquals("mocked response", response);
    }

    @Test
    void testLogin() {
        boolean result = client.login("username", "password");
        assertTrue(result);
    }

    @Test
    void testRegister() {
        boolean result = client.register("newUser", "newPass");
        assertTrue(result);
    }

    @Test
    void testLogout() {
        boolean result = client.logout();
        assertTrue(result);
    }

    @Test
    void testLoginWithInvalidCredentials() {
        try {
            when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("false".getBytes()));
            boolean result = client.login("wrongUser", "wrongPass");
            assertFalse(result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testRegisterWithExistingUsername() {
        try {
            when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("false".getBytes()));
            boolean result = client.register("existingUser", "password123");
            assertFalse(result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testSendMessageWithEmptyMessage() {
        boolean result = client.sendMessage("");
        assertFalse(result);
    }

    @Test
    void testGetChatWithInvalidUser() {
        try {
            when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
            String response = client.getChat("nonexistentUser");
            assertEquals("", response);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testUpdateProfileWithInvalidData() {
        boolean result = client.updateProfile("");
        assertFalse(result);
    }

    @Test
    void testDisconnectWithIOException() throws IOException {
        doThrow(new IOException()).when(mockSocket).close();
        boolean result = client.disconnect();
        assertFalse(result);
    }

    @Test
    void testSendImageWithInvalidPath() {
        boolean result = client.sendImage("user", "invalid/path/to/image");
        assertFalse(result);
    }

    @Test
    void testSetFriendsOnlyWithInvalidValue() {
        boolean result = client.setFriendsOnly("invalid");
        assertFalse(result);
    }

    @Test
    void testGetProfilePicWithNoImage() {
        try {
            when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
            String response = client.getProfilePic();
            assertEquals("", response);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testRequestActiveWithOfflineUser() {
        boolean result = client.requestActive("offlineUser");
        assertFalse(result);
    }

    @Test
    void testDeleteChatWithInvalidUser() {
        boolean result = client.deleteChat("");
        assertFalse(result);
    }

    @Test
    void testBlockAlreadyBlockedUser() {
        try {
            when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("false".getBytes()));
            boolean result = client.blockUser("alreadyBlockedUser");
            assertFalse(result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testUnblockNotBlockedUser() {
        try {
            when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("false".getBytes()));
            boolean result = client.unblockUser("notBlockedUser");
            assertFalse(result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testDisconnectAndRegister() {
        try {
            // Test register with existing username
            when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("false".getBytes()));
            boolean registerResult = client.register("existingUser", "password123");
            assertFalse(registerResult);

            // Test disconnect with IOException
            doThrow(new IOException()).when(mockSocket).close();
            boolean disconnectResult = client.disconnect();
            assertFalse(disconnectResult);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
}*/