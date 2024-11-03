import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A framework to run public test cases for Database
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 *         lab sec 12
 *
 * @version Nov 2, 2024
 */
class DatabaseTestCases {
    private Database db;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        db = new Database();
        tempFile = Files.createTempFile("usersHistory", ".txt").toFile();
        tempMessageFile = Files.createTempFile("messageHistory", ".txt").toFile();
        tempFile.deleteOnExit();
        tempMessageFile.deleteOnExit();
    }

    void tearDown() {
        tempFile.delete();
        tempMessageFile.delete();
    }

    @Test
    // Verifies that adding a user with a unique username succeeds
    // and that adding a user with a duplicate username fails
    void testAddUser_UniqueUsername() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user1", "password2");
        assertTrue(db.addUser(user1));
        assertFalse(db.addUser(user2)); // expecting false as username is not unique
    }

    @Test
    // checks that the validation method fails if the password contains the username
    void testValidateNewUser_UsernameInPassword() {
        User user = new User("user", "userpassword");
        assertFalse(db.validateNewUser(user)); // should return false if password contains username
    }

    @Test
    // adds a user and retrieves it to verify getter
    void testGetUser_ExistingUser() {
        User user = new User("user1", "password1");
        db.addUser(user);
        User gottenUser = db.getUser("user1");
        assertEquals(user.getUsername(), gottenUser.getUsername());
        assertEquals(user.getPassword(), gottenUser.getPassword());
    }

    @Test
    // attempts to retrieve a non-existent user which should be default
    void testGetUser_NonExistentUser() {
        User defaultUser = db.getUser("nonexistent");
        assertNotNull(defaultUser);
        assertNull(defaultUser.getUsername()); // assuming default user has null username and password
    }

    @Test
    // adds multiple users and gets the list to verify its size and contents
    void testGetUsers() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        db.addUser(user1);
        db.addUser(user2);
        ArrayList<User> users = db.getUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    // simulates a conversation between two users and verifies it can be retrieved
    void testGetMessages_ValidConversation() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        db.addUser(user1);
        db.addUser(user2);
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.setUserMessagers(new String[] { "user1", "user2" });
        db.addMessageHistory(messageHistory); // directly adding to create a chat history

        MessageHistory retrievedHistory = db.getMessages("user1", "user2");
        assertNotNull(retrievedHistory);
        assertArrayEquals(new String[] { "user1", "user2" }, retrievedHistory.getUsernames());
    }

    @Test
    // checks if self messaging throws exception
    void testGetMessages_SelfMessagingException() {
        db.addUser(new User("user1", "password1"));
        assertThrows(IllegalArgumentException.class, () -> db.getMessages("user1", "user1"));
    }

    @Test
    // adds and saves users and verifies that the correct data was written to the
    // file
    void testSaveUsers_FileOutput() throws IOException {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        db.addUser(user1);
        db.addUser(user2);
        db.saveUsers();

        char groupSeparator = 29;
        char fileSeparator = 28;

        try (BufferedReader reader = new BufferedReader(new FileReader("usersHistory.txt"))) {
            String line = reader.readLine();
            String[] liness = line.replace(String.valueOf(fileSeparator), "").split(String.valueOf(groupSeparator));
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(liness));
            assertTrue(lines.contains("username: user1"));
            assertTrue(lines.contains("password: password1"));
            assertTrue(lines.contains("username: user2"));
            assertTrue(lines.contains("password: password2"));
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
    }

    @Test
    // checks that the saveUsers method creates usersHistory.txt if it doesn’t exist
    // otherwise
    void testSaveUsers_CreatesFileIfNotExists() {
        db.saveUsers();
        File file = new File("usersHistory.txt");
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    void testSaveMessages_FileOutput() throws IOException {
        // create a MessageHistory with some messages
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.setUserMessagers(new String[] {"user1", "user2"});
        
        Message message1 = new Message("Hello", "user1");
        Message message2 = new Message("Hi there!", "user2");

        // add messages to the message history
        ArrayList<Message> messages = messageHistory.getMessageHistory();
        messages.add(message1);
        messages.add(message2);
        
        db.getAllChats().add(messageHistory);
        
        // run method
        db.saveMessages();

        // read the contents of the file to check for saved messages
        List<String> lines = Files.readAllLines(tempMessageFile.toPath());
        
        assertTrue(lines.contains("user1 user2"));
        assertTrue(lines.contains("user1: Hello"));
        assertTrue(lines.contains("user2: Hi there!"));
    }

    @Test
    void testLoadMessages_FileInput() throws IOException {
        // sample message history, please change if needed
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempMessageFile))) {
            writer.write((char) 28 + "user1 user2\n");  // start with users and char
            writer.write("user1: Hello" + (char) 29 + "\n");  // message
            writer.write("user2: Hi there!" + (char) 29 + "\n");  // message
            writer.write((char) 28 + "\n");  // end with char
        }

        // calls method then creates ArrayList containing newly updated allChats
        db.loadMessages();
        ArrayList<MessageHistory> allChats = db.getAllChats();

        assertEquals(1, allChats.size());
        
        // specifies the first conversation in MessageHistory
        MessageHistory loadedHistory = allChats.get(0);
        String[] usernames = loadedHistory.getUsernames();
        assertArrayEquals(new String[] {"user1", "user2"}, usernames);

        // makes sure messages exist in MessageHistory
        ArrayList<Message> messages = loadedHistory.getMessageHistory();
        assertEquals(2, messages.size());

        // checks content of each message
        assertEquals("Hello", messages.get(0).getMessage());
        assertEquals("user1", messages.get(0).getSender());
        assertEquals("Hi there!", messages.get(1).getMessage());
        assertEquals("user2", messages.get(1).getSender());
    }
}
