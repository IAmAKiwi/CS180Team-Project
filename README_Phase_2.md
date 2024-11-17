# Phase 2: Server and Client Implementation

## Overview

This phase focuses on the implementation of the server and client components of the messaging application. It includes detailed explanations of the `Server.java` and `Client.java` classes, their methods, and how to run the associated test cases.

## Server.java

### Purpose
The `Server` class handles server-side operations, managing client requests, user sessions, and data processing. It listens for client connections and processes commands sent by clients.

### Key Methods

- **`login(String content)`**: Authenticates a user using their username and password.
- **`register(String content)`**: Registers a new user with a unique username and valid password.
- **`sendMessage(String content)`**: Sends a message from one user to another, with validation checks.
- **`getChat(String otherUsername)`**: Retrieves the chat history between the current user and another user.
- **`blockUser(String otherUsername)`**: Blocks a specified user.
- **`setFriendsOnly(boolean friendsOnly)`**: Sets the current user's profile to friends-only mode.
- **`saveProfile(String content)`**: Saves the user's profile information, including personal details and privacy settings.
- **`disconnect()`**: Closes the client connection and cleans up resources.

## Client.java

### Purpose
The `Client` class manages client-side operations, including sending commands to the server, handling responses, and managing user interactions.

### Key Methods

- **`sendCommand(String command)`**: Sends a command to the server and processes the response.
- **`requestData(String command)`**: Requests data from the server and returns the response.
- **`getChat(String username)`**: Retrieves the chat history with a specified user.
- **`deleteChat(String username)`**: Deletes the chat history with a specified user.
- **`login(String content)`**: Logs in a user with their username and password.
- **`register(String content)`**: Registers a new user with a unique username and valid password.
- **`logout()`**: Logs out the current user.
- **`disconnect()`**: Disconnects from the server and closes the connection.

## Running ServerClientIOTestCases

### Prerequisites
- Ensure the server is running before executing the test cases.

### Steps to Run

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

### Important Notes
- The server must be running before executing the test cases.
- Use arguments when starting the server to prevent data persistence.
- Ensure the server is reset between test runs to avoid data contamination.

## Test Cases in ServerClientIOTestCases

1. **Authentication Tests**
   - `testRegister()`: Validates user registration.
   - `testReLogin()`: Tests login functionality.

2. **Messaging Tests**
   - `testSendMessage()`: Tests message sending.
   - `testGetChat()`: Verifies chat retrieval.

3. **User Relationship Tests**
   - `testFriends()`: Friend management.
   - `testBlocks()`: Blocking functionality.

4. **Profile Tests**
   - `testProfile()`: Profile creation and updates.

5. **Multi-User Interaction Tests**
   - `testMultiUserChat()`: Multi-user messaging.
   - `testBlockedUserInteractions()`: Blocked user behavior.
   - `testFriendsOnlyModeInteractions()`: Friends-only mode behavior.

## Authors

- **William Thain**
- **Fox Christiansen**
- **Jackson Shields**
- **Peter Bui (Bui Dinh Tuan Anh)**

## Version

Current version: **Nov 17, 2024**

## Contributing

If you'd like to contribute to this project, please fork the repository and submit a pull request. 