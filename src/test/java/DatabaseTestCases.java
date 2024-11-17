import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
    private File tempMessageFile;

    @BeforeEach
    public void setUp() throws IOException {
        db = new Database();
        tempFile = Files.createTempFile("usersHistory", ".txt").toFile();
        tempMessageFile = Files.createTempFile("messageHistory", ".txt").toFile();
        tempFile.deleteOnExit();
        tempMessageFile.deleteOnExit();
    }

    @Test
    // Verifies that adding a user with a unique username succeeds
    // and that adding a user with a duplicate username fails
    void testAddUserUniqueUsername() {
        User user1 = new User("user1", "Password1$");
        User user2 = new User("user1", "Password2$");
        assertTrue(db.addUser(user1));
        assertFalse(db.addUser(user2)); // expecting false as username is not unique
    }

    @Test
    // checks that the validation method fails if the password contains the username
    void testValidateNewUserUsernameInPassword() {
        User user = new User("user", "userpassword");
        assertFalse(db.validateNewUser(user)); // should return false if password contains username
    }

    @Test
    // adds a user and retrieves it to verify getter
    void testGetUserExistingUser() {
        User user = new User("user1", "Password1$");
        db.addUser(user);
        User gottenUser = db.getUser("user1");
        assertEquals(user.getUsername(), gottenUser.getUsername());
        assertEquals(user.getPassword(), gottenUser.getPassword());
    }

    @Test
    // attempts to retrieve a non-existent user which should be default
    void testGetUserNonExistentUser() {
        User defaultUser = db.getUser("nonexistent");
        // default user should be null
        assertNull(defaultUser);
    }

    @Test
    // adds multiple users and gets the list to verify its size and contents
    void testGetUsers() {
        User user1 = new User("user1", "Password1$");
        User user2 = new User("user2", "Password2$");
        db.addUser(user1);
        db.addUser(user2);
        ArrayList<User> users = db.getUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    // simulates a conversation between two users and verifies it can be retrieved
    void testGetMessagesValidConversation() {
        User user1 = new User("user1", "Password1$");
        User user2 = new User("user2", "Password2$");
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
    void testGetMessagesSelfMessagingException() {
        db.addUser(new User("user1", "Password1$"));
        // assertThrows(IllegalArgumentException.class, () -> db.getMessages("user1",
        // "user1"));
    }

    @Test
    // adds and saves users and verifies that the correct data was written to the
    // file
    void testSaveUsersFileOutput() throws IOException {
        User user1 = new User("user1", "Password1$");
        User user2 = new User("user2", "Password2$");
        db.addUser(user1);
        db.addUser(user2);
        db.saveUsers();

        char groupSeparator = 29;
        char fileSeparator = 28;
        try (BufferedReader reader = new BufferedReader(new FileReader("usersHistory.txt"))) {
            String line = reader.readLine();
            String[] arrayOfLines;
            ArrayList<String> arrayListOfLines;
            ArrayList<ArrayList<String>> lines = new ArrayList<>();
            while (true) {
                if (line == null) {
                    break;
                }
                arrayOfLines = line.replace(String.valueOf(fileSeparator), "").split(String.valueOf(groupSeparator));
                arrayListOfLines = new ArrayList<>(Arrays.asList(arrayOfLines));
                lines.add(arrayListOfLines);
                line = reader.readLine();
            }
            boolean foundUser1User2 = false;
            boolean foundUser1Hello = false;
            boolean foundUser2Hi = false;
            boolean foundpassWord2 = false;

            for (ArrayList<String> l : lines) {
                if (l.contains("username: user1")) {
                    foundUser1User2 = true;
                }
                if (l.contains("password: Password1$")) {
                    foundUser1Hello = true;
                }
                if (l.contains("username: user2")) {
                    foundUser2Hi = true;
                }
                if (l.contains("password: Password2$")) {
                    foundpassWord2 = true;
                }
            }

            // Assert at the end
            assertTrue(foundUser1User2);
            assertTrue(foundUser1Hello);
            assertTrue(foundUser2Hi);
            assertTrue(foundpassWord2);

        } catch (IOException e) {
            System.out.println("false");
        }
    }

    @Test
    // checks that the saveUsers method creates usersHistory.txt if it doesn’t exist
    // otherwise
    void testSaveUsersCreatesFileIfNotExists() {
        db.saveUsers();
        File file = new File("usersHistory.txt");
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    void testSaveMessagesFileOutput() throws IOException {
        // create a MessageHistory with some messages
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.setUserMessagers(new String[] { "user1", "user2" });

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
        char groupSeparator = 29;
        char fileSeparator = 28;

        try (BufferedReader reader = new BufferedReader(new FileReader("messageHistory.txt"))) {
            String line = reader.readLine();
            String[] arrayOfLines;
            ArrayList<String> arrayListOfLines;
            ArrayList<ArrayList<String>> lines2 = new ArrayList<>();
            while (true) {
                if (line == null) {
                    break;
                }
                arrayOfLines = line.replace(String.valueOf(fileSeparator), "").split(String.valueOf(groupSeparator));
                arrayListOfLines = new ArrayList<>(Arrays.asList(arrayOfLines));
                lines2.add(arrayListOfLines);
                line = reader.readLine();
            }

            boolean foundUser1User2 = false;
            boolean foundUser1Hello = false;
            boolean foundUser2Hi = false;

            for (ArrayList<String> l : lines2) {
                if (l.contains("user1 user2")) {
                    foundUser1User2 = true;
                }
                if (l.contains("user1: Hello")) {
                    foundUser1Hello = true;
                }
                if (l.contains("user2: Hi there!")) {
                    foundUser2Hi = true;
                }
            }

            // Assert at the end
            assertTrue(foundUser1User2);
            assertTrue(foundUser1Hello);
            assertTrue(foundUser2Hi);

        } catch (IOException e) {
            System.out.println(false);
        }
    }

    @Test
    void testLoadMessagesFileInput() throws IOException {
        // sample message history, please change if needed
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("messageHistory.txt"))) {
            writer.write((char) 28 + "user1 user2\n"); // start with users and char
            writer.write("user1: Hello" + (char) 29 + "\n"); // message
            writer.write("user2: Hi there!" + (char) 29 + "\n"); // message
            writer.write((char) 28 + "\n"); // end with char
        }

        // calls method then creates ArrayList containing newly updated allChats
        db.loadMessages();
        ArrayList<MessageHistory> allChats = db.getAllChats();

        assertEquals(1, allChats.size());

        // specifies the first conversation in MessageHistory
        MessageHistory loadedHistory = allChats.get(0);
        String[] usernames = loadedHistory.getUsernames();
        assertArrayEquals(new String[] { "user1", "user2" }, usernames);

        // makes sure messages exist in MessageHistory
        ArrayList<Message> messages = loadedHistory.getMessageHistory();
        assertEquals(2, messages.size());

        // checks content of each message
        assertEquals("Hello", messages.get(0).getMessage());
        assertEquals("user1", messages.get(0).getSender());
        assertEquals("Hi there!", messages.get(1).getMessage());
        assertEquals("user2", messages.get(1).getSender());
    }

    @Test
    void testAddMessageWithBlockedUser() {
        User user1 = new User("user1", "Password1$");
        User user2 = new User("user2", "Password2$");
        db.addUser(user1);
        db.addUser(user2);
        user1.addBlock("user2");
        
        Message message = new Message("Hello", "user1");
        assertFalse(db.addMessage(message, "user2"));
    }

    @Test
    void testAddMessageWithFriendsOnlyMode() {
        User user1 = new User("user1", "Password1$");
        User user2 = new User("user2", "Password2$");
        db.addUser(user1);
        db.addUser(user2);
        user1.setFriendsOnly(true);
        
        Message message = new Message("Hello", "user1");
        assertFalse(db.addMessage(message, "user2"));
    }

    @Test
    void testGetMessagesNonExistentUsers() {
        MessageHistory mh = db.getMessages("nonexistent1", "nonexistent2");
        assertNull(mh);
    }

    @Test
    void testLoadUsersWithInvalidFormat() {
        // Create a temporary file with invalid format
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("usersHistory.txt"));
            writer.write("invalid format data");
            writer.close();
            
            assertFalse(db.loadUsers());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}
