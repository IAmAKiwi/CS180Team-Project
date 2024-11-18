import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * A framework to run public test cases for Client
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 *         lab sec 12
 * @version Nov 17, 2024
 */
public class ClientTest implements ClientTestInterface {
    private Client client;
    private Socket mockSocket;
    private PrintWriter mockWriter;
    private BufferedReader mockReader;

    @BeforeEach
    public void setUp() throws IOException {
        mockSocket = mock(Socket.class);
        mockWriter = mock(PrintWriter.class);
        mockReader = mock(BufferedReader.class);
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        client = new Client();
        client.socket = mockSocket;
        client.serverWriter = mockWriter;
        client.serverReader = mockReader;
    }

    @Test
    public void testClientConstructor() {
        try {
            new Client(); // constructor should not throw exceptions
        } catch (Exception e) {
            fail("Constructor threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetUserList() throws IOException {
        when(mockReader.readLine()).thenReturn("user1:user2:");
        String result = client.getUserList();
        verify(mockWriter).println("getUserList: ");
        assertEquals("user1:user2:", result);
    }

    @Test
    public void testSendMessage() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.sendMessage("user1\031Hello!");
        verify(mockWriter).println("sendMessage:user1\031Hello!");
        assertTrue(result);
    }

    @Test
    public void testDeleteMessage() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.deleteMessage("user1\031Message1");
        verify(mockWriter).println("deleteMessage:user1\031Message1");
        assertTrue(result);
    }

    @Test
    public void testAccessProfile() throws IOException {
        when(mockReader.readLine()).thenReturn("ProfileData");
        String result = client.accessProfile();
        verify(mockWriter).println("accessProfile: ");
        assertEquals("ProfileData", result);
    }

    @Test
    public void testSaveProfile() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.saveProfile("ProfileContent");
        verify(mockWriter).println("saveProfile:ProfileContent");
        assertTrue(result);
    }

    @Test
    public void testRemoveFriend() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.removeFriend("friend1");
        verify(mockWriter).println("removeFriend:friend1");
        assertTrue(result);
    }

    @Test
    public void testAddFriend() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.addFriend("friend1");
        verify(mockWriter).println("addFriend:friend1");
        assertTrue(result);
    }

    @Test
    public void testUnblockUser() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.unblockUser("user1");
        verify(mockWriter).println("unblockUser:user1");
        assertTrue(result);
    }

    @Test
    public void testBlockUser() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.blockUser("user1");
        verify(mockWriter).println("blockUser:user1");
        assertTrue(result);
    }

    @Test
    public void testDeleteChat() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.deleteChat("user1");
        verify(mockWriter).println("deleteChat:user1");
        assertTrue(result);
    }

    @Test
    public void testSendImage() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.sendImage("user1\031imagePath");
        verify(mockWriter).println("sendImage:user1\031imagePath");
        assertTrue(result);
    }

    @Test
    public void testGetChat() throws IOException {
        when(mockReader.readLine()).thenReturn("ChatData");
        String result = client.getChat("user1");
        verify(mockWriter).println("getChat:user1");
        assertEquals("ChatData", result);
    }

    @Test
    public void testGetFriendList() throws IOException {
        when(mockReader.readLine()).thenReturn("friend1:friend2:");
        String result = client.getFriendList();
        verify(mockWriter).println("getFriendList: ");
        assertEquals("friend1:friend2:", result);
    }

    @Test
    public void testGetBlockList() throws IOException {
        when(mockReader.readLine()).thenReturn("user1:user2:");
        String result = client.getBlockList();
        verify(mockWriter).println("getBlockList: ");
        assertEquals("user1:user2:", result);
    }

    @Test
    public void testIsFriendsOnly() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.isFriendsOnly();
        verify(mockWriter).println("isFriendsOnly: ");
        assertTrue(result);
    }

    @Test
    public void testSetFriendsOnly() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.setFriendsOnly("true");
        verify(mockWriter).println("setFriendsOnly:true");
        assertTrue(result);
    }

    @Test
    public void testSetProfilePic() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.setProfilePic("path/to/profilePic");
        verify(mockWriter).println("setProfilePic:path/to/profilePic");
        assertTrue(result);
    }

    @Test
    public void testGetProfilePic() throws IOException {
        when(mockReader.readLine()).thenReturn("path/to/profilePic");
        String result = client.getProfilePic();
        verify(mockWriter).println("getProfilePic: ");
        assertEquals("path/to/profilePic", result);
    }

    @Test
    public void testLogin() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.login("username\031password");
        verify(mockWriter).println("login:username\031password");
        assertTrue(result);
    }

    @Test
    public void testRegister() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.register("username\031password");
        verify(mockWriter).println("register:username\031password");
        assertTrue(result);
    }

    @Test
    public void testLogout() throws IOException {
        when(mockReader.readLine()).thenReturn("true");
        boolean result = client.logout();
        verify(mockWriter).println("logout: ");
        assertTrue(result);
    }

    @Test
    public void testDisconnect() throws IOException {
        // Simulate the server closing the connection
        when(mockReader.readLine()).thenReturn(null);

        // Call the disconnect method
        boolean result = client.disconnect();

        // Verify that the socket was closed
        verify(mockSocket).close();

        // Assert that the disconnect method returned true
        assertTrue(result, "Expected disconnect to return true, but it returned false.");
    }
}
