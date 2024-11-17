# <p align="center">Message App User DATABASE</p>

This repository serves as a comprehensive database and testing framework for a messaging application. It includes:

- User database and message history
- User's friends and blocked friends
- User's first name, last name, bio, and birthday details
- User's photos that are sent to another user, and a method that can also display photo

## Core Components

### Classes and Interfaces

#### Database
- **Purpose**: Manages users and message histories.
- **Key Methods**:
  - `addUser(User user)`: Adds a new user if the username is valid.
  - `validateNewUser(User user)`: Validates the new user's username and password.
  - `getMessages(String user1, String user2)`: Retrieves the message history between two users.
  - `addMessage(Message message, String receiver)`: Adds a message to a message history.
  - `deleteChat(String user1, String user2)`: Deletes the message history between two users.

#### User
- **Purpose**: Represents a user profile with personal and social information.
- **Key Methods**:
  - `setBirthday(int[] birthday)`: Sets the user's birthday with validation.
  - `addFriend(String friendName)`: Adds a friend to the user's friend list.
  - `blockUser(String username)`: Blocks a user.
  - `setFriendsOnly(boolean friendsOnly)`: Sets the user's profile to friends-only mode.

#### Server
- **Purpose**: Handles server-side operations and client requests.
- **Key Methods**:
  - `login(String content)`: Authenticates a user.
  - `register(String content)`: Registers a new user.
  - `sendMessage(String content)`: Sends a message to another user.
  - `getChat(String otherUsername)`: Retrieves chat history with another user.

#### Client
- **Purpose**: Manages client-side operations and server communication.
- **Key Methods**:
  - `sendCommand(String command)`: Sends a command to the server.
  - `requestData(String command)`: Requests data from the server.
  - `getChat(String username)`: Retrieves chat history with a user.

### Interfaces

#### UserInterface
- Defines methods for managing user data and operations.

#### DatabaseInterface
- Specifies methods for database operations, including user and message management.

#### ServerInterface
- Outlines methods for handling client requests and managing server operations.

#### ClientInterface
- Handles client-side functionality, including user authentication and messaging.

#### MessageInterface
- Defines methods for message objects, including getting message content and sender.

#### MessageHistoryInterface
- Manages the storage and management of messages between users.

### Test Framework

#### ServerClientIOTestCases
- **Purpose**: Tests server-client communication and functionality.
- **Test Categories**:
  - Authentication Tests: Registration and login verification.
  - Messaging Tests: Message sending, retrieval, and deletion.
  - User Relationship Tests: Friend and block management.
  - Profile Tests: Profile updates and validation.
  - Multi-User Interaction Tests: Multi-user chat and privacy settings.

#### DatabaseTestCases
- **Purpose**: Validates database operations, including user and message management.

#### MessageTestCases
- **Purpose**: Tests message creation, validation, and formatting.

#### MessageHistoryTestCases
- **Purpose**: Ensures correct management of chat histories and message ordering.

## Usage

1. **Initialization**: Create an instance of the `Database` class.
2. **User Management**: Use `addUser()` to add new users, and `getUser()` to retrieve existing users.
3. **Message Management**: Use `addMessageHistory()` to add new message histories and `getMessages()` to retrieve them.
4. **Data Persistence**: Call `saveUsers()` and `saveMessages()` to persist data and `loadUsers()` and `loadMessages()` to retrieve data when starting the application.

## Running Tests

1. Start the Server:
```bash
java Server.java "args"  # Prevent data persistence
```

2. Run Tests:
```bash
mvn test -Dtest=ServerClientIOTestCases
```

## Authors

- **William Thain**
- **Fox Christiansen**
- **Jackson Shields**
- **Peter Bui (Bui Dinh Tuan Anh)**

## Version

Current version: **Nov 17, 2024**

## Contributing

If you'd like to contribute to this project, please fork the repository and submit a pull request.

