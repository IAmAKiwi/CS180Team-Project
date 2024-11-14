import org.junit.jupiter.api.*;
import org.mockito.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    private Server server;
    private Socket mockSocket;
    private BufferedReader mockReader;
    private PrintWriter mockWriter;
    private Database mockDatabase;
    private User mockUser;
    private MessageHistory mockMessageHistory;

    @BeforeEach
    public void setUp() throws IOException {
        mockSocket = mock(Socket.class);
        mockReader = mock(BufferedReader.class);
        mockWriter = mock(PrintWriter.class);
        mockDatabase = mock(Database.class);
        mockUser = mock(User.class);
        mockMessageHistory = mock(MessageHistory.class);

        // mock socket input and output streams
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        server = new Server(mockSocket);
        // DOESNT WORK SINCE ITS NOT VISIBLE
        // currentUser and clientSocket aren't visible either and need getters/setters
        // none of this matters when we switch to scanners
        Server.db = mockDatabase; 

    }

    @AfterEach
    public void tearDown() throws IOException {
        mockSocket.close();
    }

    @Test
    public void testLoginSuccess() throws IOException {
        String username = "testUser";
        String password = "testPass";
        String command = "login:" + username + ":" + password;

        when(mockDatabase.getUsers()).thenReturn(new ArrayList<User>() {{
            add(mockUser);
        }});
        when(mockUser.getUsername()).thenReturn(username);
        when(mockUser.getPassword()).thenReturn(password);

        String result = server.login(command);
        assertEquals("true", result);
    }

    @Test
    public void testLoginFailure() {
        String command = "login:wrongUser:wrongPass";

        when(mockDatabase.getUsers()).thenReturn(new ArrayList<User>() {{
            add(mockUser);
        }});
        when(mockUser.getUsername()).thenReturn("testUser");
        when(mockUser.getPassword()).thenReturn("testPass");

        String result = server.login(command);
        assertEquals("false", result);
    }

    @Test
    public void testRegisterSuccess() {
        String username = "newUser";
        String password = "newPass";
        String command = "register:" + username + ":" + password;

        when(mockDatabase.addUser(any(User.class))).thenReturn(true);

        String result = server.register(command);
        assertEquals("true", result);
    }

    @Test
    public void testRegisterFailure() {
        String command = "register:existingUser:password";

        when(mockDatabase.addUser(any(User.class))).thenReturn(false);

        String result = server.register(command);
        assertEquals("false", result);
    }

    @Test
    public void testSendMessageSuccess() {
        String sender = "senderUser";
        String receiver = "receiverUser";
        String message = "Hello there!";
        String command = "sendMessage:" + receiver + ":" + message;

        when(mockUser.getUsername()).thenReturn(sender);
        when(mockDatabase.addMessage(any(Message.class), eq(receiver))).thenReturn(true);

        String result = server.sendMessage(command);
        assertEquals("true", result);
    }

    @Test
    public void testSendMessageFailure() {
        String command = "sendMessage:receiverUser:Message content";

        when(mockDatabase.addMessage(any(Message.class), anyString())).thenReturn(false);

        String result = server.sendMessage(command);
        assertEquals("false", result);
    }
    @Test
    public void testGetUserList() {
        when(mockDatabase.getUsers()).thenReturn(new ArrayList<User>() {{
            add(new User("user1", "pass1"));
            add(new User("user2", "pass2"));
        }});

        String result = server.getUserList();
        assertEquals("user1:user2:", result);
    }

    @Test
    public void testGetChat() {
        String otherUsername = "friend";
        String command = "getChat:" + otherUsername;
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("Hi", "user1"));
        messages.add(new Message("Hello", "friend"));

        when(mockDatabase.getMessages(anyString(), eq(otherUsername))).thenReturn(mockMessageHistory);
        when(mockMessageHistory.getMessageHistory()).thenReturn(messages);

        String result = server.getChat(command);
        assertEquals("user1: Hi" + (char) 29 + "friend: Hello" + (char) 29, result);
    }

    @Test
    public void testUpdateProfile() {
        String command = "updateProfile:oldUser:newUser:newPass:First:Last:bio:12/31/2000:profilePic:user1,user2:block1:block2:true";
        when(mockDatabase.getUser("oldUser")).thenReturn(mockUser);

        String result = server.updateProfile(command);
        verify(mockUser).setUsername("newUser");
        verify(mockUser).setPassword("newPass");
        verify(mockUser).setFirstName("First");
        verify(mockUser).setLastName("Last");
        verify(mockUser).setBio("bio");
        assertEquals("true", result);
    }

    @Test
    public void testLogout() {
        server.currentUser = mockUser;
        String result = server.logout();
        assertNull(server.currentUser);
        assertEquals("true", result);
    }

    @Test
    public void testDisconnect() throws IOException {
        server.clientSocket = mockSocket;
        boolean result = server.disconnect();
        verify(mockSocket).close();
        assertTrue(result);
    }

    @Test
    public void testBlockUser() {
        String command = "blockUser:otherUser";
        when(mockDatabase.blockUser(anyString(), eq("otherUser"))).thenReturn(true);

        String result = server.blockUser(command);
        assertEquals("true", result);
    }

    @Test
    public void testUnblockUser() {
        String command = "unblockUser:otherUser";
        when(mockDatabase.unblockUser(anyString(), eq("otherUser"))).thenReturn(true);

        String result = server.unblockUser(command);
        assertEquals("true", result);
    }

        @Test
    public void testGetFriendList() {
        when(mockDatabase.getFriends(anyString())).thenReturn(new String[]{"friend1", "friend2"});

        String result = server.getFriendList();
        assertEquals("friend1:friend2:", result);
    }

    @Test
    public void testGetBlockList() {
        when(mockDatabase.getBlockList(anyString())).thenReturn(new String[]{"block1", "block2"});

        String result = server.getBlockList();
        assertEquals("block1:block2:", result);
    }
}




