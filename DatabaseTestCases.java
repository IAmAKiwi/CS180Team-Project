import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

class DatabaseTestCases {
    private Database db;
    private File tempFile;

    void setUp() throws IOException {
        db = new Database();
        tempFile = Files.createTempFile("usersHistory", ".txt").toFile();
        tempFile.deleteOnExit();
    }

    void tearDown() {
        tempFile.delete();
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
        db.getAllChats().add(messageHistory); // directly adding to create a chat history

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
    // adds and saves users and verifies that the correct data was written to the file
    void testSaveUsers_FileOutput() throws IOException {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        db.addUser(user1);
        db.addUser(user2);
        db.saveUsers();

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertTrue(lines.contains("username: user1"));
        assertTrue(lines.contains("password: password1"));
        assertTrue(lines.contains("username: user2"));
        assertTrue(lines.contains("password: password2"));
    }

    @Test
    // checks that the saveUsers method creates usersHistory.txt if it doesn’t exist otherwise
    void testSaveUsers_CreatesFileIfNotExists() {
        db.saveUsers();
        File file = new File("usersHistory.txt");
        assertTrue(file.exists());
        file.delete();
    }
}
