import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DatabaseApp {
    public static void main(String[] args) {
        Database db = new Database();

        // Load existing users if available
        if (db.loadUsers()) {
            System.out.println("Users loaded successfully.");
        } else {
            System.out.println("No existing users found.");
        }

        // Example of adding a new user
        User newUser = new User("john_doe", "securePassword123");
        if (db.addUser(newUser)) {
            System.out.println("User added successfully.");
        } else {
            System.out.println("Failed to add user: Username may already exist or password is insecure.");
        }

        // Save users
        try {
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("usersHistory.txt"))) {
                String line;
                char groupSeparator = 29;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    lines.add(line);
                }
            }
            if (db.saveUsers()) {
                System.out.println("Users saved successfully.");
                for (String lai : lines) {
                    System.out.println("line: " + lai);

                }
                System.out.println(lines.contains("username: john_doe"));
            } else {
                System.out.println("Failed to save users.");
            }
        } catch (IOException e) {
            System.out.println("Error creating temp file: " + e.getMessage());
        }

        // Example of retrieving a user
        User retrievedUser = db.getUser("john_doe");
        if (retrievedUser != null) {
            System.out.println("Retrieved user: " + retrievedUser.getUsername());
        } else {
            System.out.println("User not found.");
        }

        // Example of saving messages (if you implement the message saving part)
        // db.saveMessages();
        // db.loadMessages();
    }
}
