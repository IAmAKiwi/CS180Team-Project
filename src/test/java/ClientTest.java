import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        // wrap InputStream and OutputStream in BufferedReader and PrintWriter for the Client instance
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
    void testDisconnect() {
        boolean result = client.disconnect();
        assertTrue(result);
    }
}