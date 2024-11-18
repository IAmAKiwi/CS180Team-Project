# Messaging Application Framework

## Overview

This project provides a comprehensive framework for a messaging application, including server-client communication, user management, and message handling. It includes detailed explanations of each class, method, and test case, as well as how the client and server interact.

## Core Components

### Classes and Interfaces

#### 1. **Database**

- **Purpose**: Manages users and message histories.
- **Key Methods**:
  - `addUser(User user)`: Adds a new user if the username is valid.
  - `validateNewUser(User user)`: Validates the new user's username and password.
  - `getMessages(String user1, String user2)`: Retrieves the message history between two users.
  - `addMessage(Message message, String receiver)`: Adds a message to a message history.
  - `deleteChat(String user1, String user2)`: Deletes the message history between two users.
  - `saveUsers()`: Saves the current list of users to persistent storage.
  - `loadUsers()`: Loads users from persistent storage into the database.

#### 2. **User**

- **Purpose**: Represents a user profile with personal and social information.
- **Key Methods**:
  - `setBirthday(int[] birthday)`: Sets the user's birthday with validation.
  - `addFriend(String friendName)`: Adds a friend to the user's friend list.
  - `blockUser(String username)`: Blocks a user.
  - `setFriendsOnly(boolean friendsOnly)`: Sets the user's profile to friends-only mode.

#### 3. **Server**

- **Purpose**: Handles server-side operations and client requests.
- **Key Methods**:
  - `login(String content)`: Authenticates a user.
  - `register(String content)`: Registers a new user.
  - `sendMessage(String content)`: Sends a message to another user.
  - `getChat(String otherUsername)`: Retrieves chat history with another user.
  - `blockUser(String otherUsername)`: Blocks a specified user.
  - `setFriendsOnly(boolean friendsOnly)`: Sets the current user's profile to friends-only mode.

#### 4. **Client**

- **Purpose**: Manages client-side operations and server communication.
- **Key Methods**:
  - `sendCommand(String command)`: Sends a command to the server and processes the response.
  - `requestData(String command)`: Requests data from the server and returns the response.
  - `getChat(String username)`: Retrieves the chat history with a specified user.
  - `deleteChat(String username)`: Deletes the chat history with a specified user.
  - `login(String content)`: Logs in a user with their username and password.
  - `register(String content)`: Registers a new user with a unique username and valid password.

### Interfaces

#### 1. **UserInterface**

- Defines methods for managing user data and operations.

#### 2. **DatabaseInterface**

- Specifies methods for database operations, including user and message management.

#### 3. **ServerInterface**

- Outlines methods for handling client requests and managing server operations.

#### 4. **ClientInterface**

- Handles client-side functionality, including user authentication and messaging.

#### 5. **MessageInterface**

- Defines methods for message objects, including getting message content and sender.

#### 6. **MessageHistoryInterface**

- Manages the storage and management of messages between users.

#### 7. **ClientInterface**

- Defines methods for client-side operations and server communication.

#### 8. **ServerInterface**

- Defines methods for server-side operations and client requests.

## Test Framework

### Test Classes

#### 1. **ServerClientIOTestCases**

- **Purpose**: Tests server-client communication and functionality.
- **Test Categories**:
  - Authentication Tests: Registration and login verification.
  - Messaging Tests: Message sending, retrieval, and deletion.
  - User Relationship Tests: Friend and block management.
  - Profile Tests: Profile updates and validation.
  - Multi-User Interaction Tests: Multi-user chat and privacy settings.

#### 2. **DatabaseTestCases**

- **Purpose**: Validates database operations, including user and message management.

#### 3. **MessageTestCases**

- **Purpose**: Tests message creation, validation, and formatting.

#### 4. **MessageHistoryTestCases**

- **Purpose**: Ensures correct management of chat histories and message ordering.

## Client-Server Interaction

### How It Works

1. **Client Initialization**: The client initializes a connection to the server and sends commands for user actions such as login, registration, and messaging.

2. **Server Processing**: The server receives commands from the client, processes them, and interacts with the database to perform actions like user authentication, message storage, and retrieval.

3. **Response Handling**: The server sends responses back to the client, indicating the success or failure of the requested actions.

4. **Data Persistence**: The server manages data persistence by saving user and message data to files, ensuring that information is retained between sessions.

### Running the Application

1. **Start the Server**:
   - Open a terminal and navigate to the directory containing `Server.java`.
   - Run the server with arguments to prevent data persistence:
     ```bash
     java Server.java "args"
     ```

2. **Run the Test Cases**:
   - Open your IDE and locate the `ServerClientIOTestCases` class.
   - Click the "Run" button to execute the test cases.
   - The tests will automatically connect to the running server and validate the functionality.

## Authors

- **William Thain**
- **Fox Christiansen**
- **Jackson Shields**
- **Peter Bui (Bui Dinh Tuan Anh)**

## Version

Current version: **Nov 17, 2024**

## Contributing

If you'd like to contribute to this project, please fork the repository and submit a pull request. 