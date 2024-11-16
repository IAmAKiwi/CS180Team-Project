# <p align="center">Message App User DATABASE</p>

This repository serves as a database for storing user information and profiles for a messaging application. It can store:

- User database and message history
- User's friends and blocked friends
- User's first name, last name, bio, and birthday details
- User's photos that are sent to another user, and a method that can also display photo

**Additionally**, the `SimpleDIYTester.java` file partially demonstrates the capabilities of the code within this repository (Highly recommended to check it out).

# Database Class

This java class is the barebone of our database, it stores the username, password and everything that the user input in their profile. It also contains the message history between different users, two specifically. In the future, we aim to do group messaging which will requires more handling of the data. The class also valdates username and password, ensuring uniqueness and security among each user. 

## Features

- **User Management**: Add new users with unique usernames and validate their credentials.
- **Message History Management**: Store and retrieve message histories between users.
- **File Operations**: Save user and message history data to text files and load it back into the application.
- **Photos Display** : Save user's photos sent in the messaging application in the database and can be displayed whenever need to

## Class Overview

### Database

This class implements the `DatabaseInterface` and contains methods to manage users and message histories. It uses the following key data structures:

- `ArrayList<User>`: Stores the list of users.
- `ArrayList<MessageHistory>`: Stores all chat histories.
- `ArrayList<String>`: Manages the paths to user photos.
- `Object`: Key objects used for synchronization. 
- `char`: chars used for file I/O

### Methods

- **addUser(User user)**: Adds a new user if the username is valid.
- **validateNewUser(User user)**: Validates the new user's username and password.
- **getAllChats()**: Returns the list of all message histories.
- **getUsers()**: Returns the list of all users.
- **getUser(String username)**: Retrieves a user by their username.
- **getMessages(String user1, String user2)**: Retrieves the message history between two users.
- **addMessageHistory(MessageHistory messageHistory)**: Adds a message history to the chat records.
- **deleteChat(String user1, String user2)**: Deletes the message history between two users.
- **addMessage(Message message, String receiver)**: Adds a message to a message history.
- **addFriend(String user1, String user2)**: Adds a friend to a user's friends list
- **getFriends(String username)**: Retrieves a list of friends for a user.
- **removeFriend(String user1, String user2)**: Removes a friend from a user's friends list.
- **blockUser(String user)**: Adds a user to the blocked list.
- **getBlockList(String username)**: Retrieves a list of blocked users for a user (if existing)
- **unblockUser(String user1, String user2)**: Removes a user from a user's blocked list.
- **saveUsers()**: Saves the list of users to a file named `usersHistory.txt`.
- **loadUsers()**: Loads users from the `usersHistory.txt` file.
- **saveMessages()**: Saves all message histories to a file named `messageHistory.txt`.
- **loadMessages()**: Loads message histories from the `messageHistory.txt` file.
- **loadPhotos()**: Loads photo paths from `UsersPhotos.txt`.
- **savePhotos()**: Saves current photo paths to `UsersPhotos.txt`.
- **addPhotos(String path)**: Adds a new photo path to the database.
- **displayPhotos(String path)**: Displays a photo from a given path (if existing)
- **getPhotos()**: Retrieves the list of photo paths associated with the database
- **setUsersList(ArrayList<User> newUserList)**: Sets the list of users for the database.
- **setAllChats(ArrayList<MessageHistory> allChats)**: Sets the list of all chat histories for the database.

## Usage

1. **Initialization**: Create an instance of the `Database` class.
2. **User Management**: Use `addUser()` to add new users, and `getUser()` to retrieve existing users.
3. **Message Management**: Use `addMessageHistory()` to add new message histories and `getMessages()` to retrieve them.
4. **Data Persistence**: Call `saveUsers()` and `saveMessages()` to persist data and `loadUsers()` and `loadMessages()` to retrieve data when starting the application.

# Database Test Cases

This repository contains a Java framework designed to run public test cases for a simple `Database` system. The tests cover various functionalities related to user management and message history.


## Description

The `DatabaseTestCases` class provides unit tests using JUnit 5. These tests ensure that the `Database` class correctly handles user creation, retrieval, validation, and message history management.


## Usage

To run the tests, you need to use Visual Studio Code and have JUnit prepared on your system, we have already provided most tools as possible in order to accomodate the testing.

## Test cases

- **`testAddUser_UniqueUsername`**: 
  - Verifies successful user addition with a unique username and failure with a duplicate username.

- **`testValidateNewUser_UsernameInPassword`**: 
  - Checks that passwords containing usernames are invalid.

- **`testGetUser_ExistingUser`**: 
  - Validates that a user can be retrieved after being added.

- **`testGetUser_NonExistentUser`**: 
  - Ensures retrieval of a non-existent user returns a default user.

- **`testGetUsers`**: 
  - Confirms that multiple users can be added and retrieved correctly.

- **`testGetMessages_ValidConversation`**: 
  - Tests message retrieval for valid conversations.

- **`testGetMessages_SelfMessagingException`**: 
  - Verifies that self-messaging throws an exception.

- **`testSaveUsers_FileOutput`**: 
  - Checks that user data is correctly saved to a file.

- **`testSaveUsers_CreatesFileIfNotExists`**: 
  - Confirms that the `usersHistory.txt` file is created if it does not exist.

- **`testSaveMessages_FileOutput`**: 
  - Tests that messages are correctly saved to a message history file.

- **`testLoadMessages_FileInput`**: 
  - Verifies that messages can be loaded from a file into the database.

# MessageHistory Class

The `MessageHistory` class stores a conversation history between users, represented by `Message` objects. It supports conversations between two or more users and provides methods to access and modify message history details.

## Class Overview

The `MessageHistory` class allows you to:
- Initialize a new conversation with one or multiple users.
- Retrieve or update the list of messages in a conversation.
- Get the usernames of users involved in the conversation.


### Constructors

- **`MessageHistory()`**: Default constructor initializing an empty conversation with no users.
- **`MessageHistory(Message message, String recipient)`**: Initializes a new conversation between two users with a starting message.
- **`MessageHistory(String[] users)`**: Initializes a conversation among multiple users, setting up usernames without any initial messages.

### Methods

- **`getMessageHistory()`**: Returns an `ArrayList` of `Message` objects representing the message history.
- **`getUsernames()`**: Returns an array of usernames involved in the conversation.
- **`setMessageHistory(ArrayList<Message> messageHistory)`**: Updates the message history.
- **`setUserMessagers(String[] userMessagers)`**: Updates the list of usernames in the conversation.
- **`toString()`**: Returns a formatted string of the first two usernames in `userMessagers` (for file representation).

## Usage Example

```java
Message firstMessage = new Message("Hello!", "User1");
MessageHistory conversation = new MessageHistory(firstMessage, "User2");

System.out.println(conversation.getUsernames()[0]);  // Outputs: User2
System.out.println(conversation.getMessageHistory()); // Outputs the message history with User1's message
System.out.println(conversation.toString());          // Outputs: User2 User1
```



# Message History Test Cases

This repository contains a Java class that implements a framework for running public test cases on the `MessageHistory` class. The tests ensure that the `MessageHistory` functionality operates as expected.

## Class Overview

The `MessageHistoryTestCases` class is designed to validate the behavior of the `MessageHistory` class, particularly focusing on the message creation, storage, and retrieval processes. The following tests are implemented:

### Test Cases

- **`MessageHistoryTest`**:
  - Tests the creation of a `MessageHistory` instance.
  - Validates the addition of messages to the message history.
  - Ensures messages can be accessed and displayed correctly.

## Structure

The test class is annotated with `@RunWith(Enclosed.class)` to allow for organization of the tests in a nested format. The main method runs the tests and outputs the results to the console, indicating whether the tests were successful or detailing any failures.

### Example Code

```java
public static void main(String[] args) {
    Result result = JUnitCore.runClasses(TestCase.class);
    if (result.wasSuccessful()) {
        System.out.println("Excellent - Test ran successfully");
    } else {
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}
```


# Message Class

The `Message` class represents a message object with a text content and sender's username. This class implements the `MessageInterface` and includes functionality to retrieve the message content and sender information, as well as a formatted string representation of the message.

## Class Overview

The `Message` class provides a way to:
- Create a message with specified text and sender.
- Retrieve the message content and sender's username.
- Represent the message in a formatted string.

### Constructors

- **`Message()`**: A default constructor that initializes `contents` and `senderUsername` to `null`.
- **`Message(String contents, String senderUsername)`**: Initializes a `Message` object with specified `contents` (text of the message) and `senderUsername` (username of the sender).

### Methods

- **`getMessage()`**: Returns the content of the message.
- **`getSender()`**: Returns the username of the sender of the message.
- **`toString()`**: Overrides `toString` to return the message in the format: `senderUsername: contents`.

## Usage Example

```java
Message message = new Message("Hello World!", "User1");
System.out.println(message.getMessage()); // Outputs: Hello World!
System.out.println(message.getSender());  // Outputs: User1
System.out.println(message.toString());   // Outputs: User1: Hello World!
```



# Message Test Cases

This repository contains a Java class that provides a framework for running public test cases on the `Message` class. The tests validate the behavior and functionality of the `Message` class, ensuring that it operates correctly.

## Class Overview

The `MessageTestCases` class is designed to test the core functionalities of the `Message` class, including message creation and data retrieval. It uses JUnit to structure and run the tests, providing a clear output of results.

### Test Cases

- **`MessageTest`**:
  - Tests the creation of `Message` instances.
  - Validates the output of the `toString()` method for correct message formatting.
  - Ensures that the sender and message content can be retrieved accurately.

## Structure

The test class utilizes the `@RunWith(Enclosed.class)` annotation to allow for the organization of tests in a nested manner. The main method runs the tests and outputs the results to the console, indicating whether the tests were successful or detailing any failures.

### Example Code

```java
public static void main(String[] args) {
    Result result = JUnitCore.runClasses(TestCase.class);
    if (result.wasSuccessful()) {
        System.out.println("Excellent - Test ran successfully");
    } else {
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}
```

# User Class

The `User` class represents a user profile on a social media platform, holding user-related information like username, password, personal details, and privacy settings. It provides various constructors and methods to manage and retrieve this data, enabling the simulation of a basic social network profile.

## Class Overview

The `User` class stores user-specific data, including:
- **Username** and **Password**
- Personal details like **First Name**, **Last Name**, **Bio**, **Birthday**, and **Profile Picture**
- Social connections like **Friends** and **Blocked Users**
- **Friends-Only Privacy** setting

### Constructors

- **`User()`**: Default constructor initializing a new user with empty or null attributes.
- **`User(String username, String password)`**: Constructor that initializes a user with a username and password, leaving other fields as null or empty.
- **`User(String username, String password, String firstName, String lastName, String bio, int[] birthday, String profilePic, ArrayList<String> friends, ArrayList<String> blocked, boolean friendsOnly)`**: Comprehensive constructor for initializing a user with detailed attributes. Throws an `IllegalArgumentException` if the birthday array does not have exactly three values (Month, Day, Year).

### Methods

#### Getters and Setters

- **`getUsername()`**, **`setUsername(String username)`**
- **`getPassword()`**, **`setPassword(String password)`**
- **`getFirstName()`**, **`setFirstName(String firstName)`**
- **`getLastName()`**, **`setLastName(String lastName)`**
- **`getBio()`**, **`setBio(String bio)`**
- **`getBirthday()`**, **`setBirthday(int[] birthday)`**: Sets the birthday only if the format is valid (Month, Day, Year).
- **`getProfilePic()`**
- **`getFriends()`**, **`setFriends(ArrayList<String> friends)`**
- **`getBlocked()`**, **`setBlocked(ArrayList<String> blocked)`**
- **`isFriendsOnly()`**, **`setFriendsOnly(boolean friendsOnly)`**

#### Friend and Block Management

- **`addFriend(String username)`**: Adds a user to the friends list.
- **`removeFriend(String username)`**: Removes a user from the friends list.
- **`addBlock(String username)`**: Adds a user to the blocked list.
- **`unblock(String username)`**: Removes a user from the blocked list.

#### toString Method

- **`toString()`**: Returns a formatted string containing user information in a CSV-like structure, including the username, password, name details, bio, birthday, profile picture, friends, blocked users, and friends-only privacy setting.

### Example Usage

```java
User user1 = new User("user123", "pass456");
user1.setFirstName("John");
user1.setLastName("Doe");
user1.setBio("Hello, I'm John!");
user1.setBirthday(new int[]{5, 14, 2000});
user1.addFriend("friendUser");
user1.addBlock("blockedUser");

System.out.println(user1.toString()); 
// Outputs: user123,pass456,John,Doe,Hello, I'm John!,5/14/2000,null,[friendUser],[blockedUser],false
```

# User Test Cases

This repository contains a Java class designed to run public test cases for the `User` class. The tests validate the functionality and behavior of various user-related features, ensuring that the `User` class operates correctly.

## Class Overview

The `UserTestCases` class utilizes JUnit to structure and run test cases focused on the `User` class. The tests cover aspects such as user properties, friend management, and blocking functionality.

### Test Cases

- **`UserTest`**:
  - Tests setting and retrieving usernames and passwords.
  - Validates the functionality of adding and removing friends and blocked users.
  - Checks the correct handling of user biography, birthday, and privacy settings.

### Key Tests Included

- **Name and Password Setting**:
  - Validates that usernames and passwords are set correctly during user creation.
  
- **Friends and Blocking Management**:
  - Tests the ability to add and remove friends and blocked users.
  - Verifies the size of friends and blocked lists.

- **User Properties**:
  - Checks the ability to set and retrieve bio and birthday information.
  - Tests the `friendsOnly` setting for user privacy.

### Example Code

The main method executes the tests and outputs the results to the console:

```java
public static void main(String[] args) {
    Result result = JUnitCore.runClasses(TestCase.class);
    if (result.wasSuccessful()) {
        System.out.println("Excellent - Test ran successfully");
    } else {
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}
```

# SimpleDIYTester

This Java class, `SimpleDIYTester`, is a simple console-based tool that allows users to create a custom `User` object, validate it, and store it in a database file (`usersHistory.txt`). Through interactive prompts, users can provide input for various fields associated with the `User` class.

## Class Overview

The `SimpleDIYTester` class provides a way to:
1. Prompt users to create a new `User` with a username, password, and optional fields like first name, last name, bio, birthday, and privacy settings.
2. Validate the new user’s password according to set rules.
3. Save the new `User` object to a database (`Database` class) and store user information in a text file (`usersHistory.txt`).

### Key Features

- **User Input Prompting**: The program prompts users to enter specific details for a new user profile.
- **Password Validation**: Ensures the password meets criteria before proceeding with user creation.
- **Optional Fields**: Allows users to include or skip additional information such as bio, birthday, and privacy settings.
- **Data Persistence**: Saves the user profile to a database and writes the data to `usersHistory.txt`.

## How to Use

1. **Run the Program**: Execute `SimpleDIYTester` from a terminal or IDE.
2. **Follow Prompts**: Enter values as prompted by the program:
   - Username and password (password is validated).
   - Optionally, provide details such as first name, last name, bio, and birthday.
   - Set privacy settings with the "friendsOnly" mode.
3. **Data Saving**: After entering all desired data, the program saves the user profile to the `Database` and writes to `usersHistory.txt`.
4. **Completion Message**: Once completed, a message confirms the user’s data has been saved.

## ServerClientIOTestCases

This java class, `ServerClientIOTestCases` uses JUnit to help test server functionality and IO.

### Key Features

- **Server and Client IO Testing**: Will use a network connection between Server and Client to execute
- **Test Cases**: Will use JUnit to test Server and Client IO

### How to use IMPORTANT

- **Run Server WITH ARGUMENTS**: java Server.java "args". Running with arguments prevents the results being saved
- **Run ServerClientIOTestCases**: It will automatically connect to the running server and run the tests using IO.
- **Ending test**: Server must be forced to stop at this point. Normally it automatically saves on forced stop.
Future versions will include a GUI to stop the server. 

## Dependencies

Ensure you have the following Java libraries available in your project:

- Java Standard Library
- JUnit Test

## Authors

- **William Thain**
- **Fox Christiansen**
- **Jackson Shields**
- **Peter Bui(Bui Dinh Tuan Anh)**


## Version

Current version: **Nov 2, 2024**

## Contributing

If you'd like to contribute to this project, please fork the repository and submit a pull request.

