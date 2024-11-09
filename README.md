# Database Class

This java class is the barebone of our database, it stores the username, password and everything that the user input in their profile. It also contains the message history between different users, two specifically. In the future, we aim to do group messaging which will requires more handling of the data. The class also valdates username and password, ensuring uniqueness and security among each user. 

## Features

- **User Management**: Add new users with unique usernames and validate their credentials.
- **Message History Management**: Store and retrieve message histories between users.
- **File Operations**: Save user and message history data to text files and load it back into the application.

## Class Overview

### Database

This class implements the `DatabaseInterface` and contains methods to manage users and message histories. It uses the following key data structures:

- `ArrayList<User>`: Stores the list of users.
- `ArrayList<MessageHistory>`: Stores all chat histories.
- `ArrayList<String>`: Manages the paths to user photos.

### Methods

- **addUser(User user)**: Adds a new user if the username is valid.
- **validateNewUser(User user)**: Validates the new user's username and password.
- **getAllChats()**: Returns the list of all message histories.
- **getUsers()**: Returns the list of all users.
- **getUser(String username)**: Retrieves a user by their username.
- **getMessages(String user1, String user2)**: Retrieves the message history between two users.
- **addMessageHistory(MessageHistory messageHistory)**: Adds a message history to the chat records.
- **saveUsers()**: Saves the list of users to a file named `usersHistory.txt`.
- **loadUsers()**: Loads users from the `usersHistory.txt` file.
- **saveMessages()**: Saves all message histories to a file named `messageHistory.txt`.
- **loadMessages()**: Loads message histories from the `messageHistory.txt` file.
- **loadPhotos()**: Loads photo paths from `UsersPhotos.txt`.
- **savePhotos()**: Saves current photo paths to `UsersPhotos.txt`.

## Usage

1. **Initialization**: Create an instance of the `Database` class.
2. **User Management**: Use `addUser()` to add new users, and `getUser()` to retrieve existing users.
3. **Message Management**: Use `addMessageHistory()` to add new message histories and `getMessages()` to retrieve them.
4. **Data Persistence**: Call `saveUsers()` and `saveMessages()` to persist data and `loadUsers()` and `loadMessages()` to retrieve data when starting the application.

## Dependencies

Ensure you have the following Java libraries available in your project:

- Java Standard Library

## Authors

- **William Thain**
- **Fox Christiansen**
- **Jackson Shields**
- **Peter Bui**


## Version

Current version: **Nov 2, 2024**

## Contributing

If you'd like to contribute to this project, please fork the repository and submit a pull request.

# Database Test Cases

This repository contains a Java framework designed to run public test cases for a simple `Database` system. The tests cover various functionalities related to user management and message history.

## Table of Contents
- [Description](#description)
- [Requirements](#requirements)
- [Usage](#usage)
- [Test Cases](#test-cases)

## Description

The `DatabaseTestCases` class provides unit tests using JUnit 5. These tests ensure that the `Database` class correctly handles user creation, retrieval, validation, and message history management.

## Requirements

- Java Development Kit (JDK) 11 or higher
- JUnit 5 for testing

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


 







## Leave room for message history



# Message History Test Cases

This repository contains a Java class that implements a framework for running public test cases on the `MessageHistory` class. The tests ensure that the `MessageHistory` functionality operates as expected.

## Overview

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


## Leave room for message



# Message Test Cases

This repository contains a Java class that provides a framework for running public test cases on the `Message` class. The tests validate the behavior and functionality of the `Message` class, ensuring that it operates correctly.

## Overview

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

## Leave room for user

# User Test Cases

This repository contains a Java class designed to run public test cases for the `User` class. The tests validate the functionality and behavior of various user-related features, ensuring that the `User` class operates correctly.

## Overview

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