# Message App Testing Framework

## Core Components

### Test Classes
1. **ServerClientIOTestCases**
   - Tests server-client communication
   - Ordered test execution (@Order annotation)
   - Covers user authentication, messaging, and relationships

2. **DatabaseTestCases**
   - Tests database operations
   - User management
   - Message history storage
   - Data persistence

3. **MessageTestCases**
   - Tests message creation and validation
   - Message formatting
   - Content handling

4. **MessageHistoryTestCases**
   - Tests chat history management
   - Message ordering
   - Multi-user conversations

### Implementation Classes

1. **Server**
   - Network communication handling
   - Command processing
   - User session management
   Key Methods:
   - `login(String content)`
   - `register(String content)`
   - `sendMessage(String content)`
   - `getChat(String content)`

2. **Client**
   - Server communication
   - Command sending
   - Response handling
   Key Methods:
   - `requestData(String command)`
   - `sendCommand(String command)`
   - `getChat(String otherUsername)`

3. **Database**
   - Data persistence
   - User management
   - Message storage
   Key Methods:
   - `validateNewUser(User user)`
   - `addMessage(Message message, String receiver)`
   - `getMessages(String user1, String user2)`

4. **User**
   - User profile management
   - Friend/block lists
   - Privacy settings
   Key Methods:
   - `setBirthday(int[] birthday)`
   - `addFriend(String friendName)`
   - `blockUser(String username)`

### Test Categories

1. **Authentication Tests**
   - Registration validation
   - Login verification
   - Password requirements
   ```java
   @Test
   @Order(1)
   public void testRegister()
   
   @Test
   @Order(2)
   public void testReLogin()
   ```

2. **Messaging Tests**
   - Message sending/receiving
   - Chat history
   - Message deletion
   ```java
   @Test
   @Order(3)
   public void testSendMessage()
   
   @Test
   @Order(4)
   public void testGetChat()
   ```

3. **User Relationship Tests**
   - Friend management
   - Blocking functionality
   - Friends-only mode
   ```java
   @Test
   @Order(5)
   public void testFriends()
   
   @Test
   @Order(6)
   public void testBlocks()
   ```

4. **Profile Tests**
   - Profile updates
   - Birthday validation
   - Privacy settings
   ```java
   @Test
   @Order(9)
   public void testProfile()
   ```

### Running Tests

1. Start Server:
```bash
java Server.java "args"
```

2. Run Tests:
```bash
mvn test -Dtest=ServerClientIOTestCases
```

### Test Dependencies
- JUnit 5
- Mockito
- TestNG

## File Structure
```
src/
├── main/java/
│   ├── Server.java
│   ├── Client.java
│   ├── Database.java
│   └── User.java
└── test/java/
    ├── ServerClientIOTestCases.java
    ├── DatabaseTestCases.java
    ├── MessageTestCases.java
    └── MessageHistoryTestCases.java
```

## Authors
- William Thain
- Fox Christiansen
- Jackson Shields
- Peter Bui (Bui Dinh Tuan Anh)

## Version
Current version: **Nov 17, 2024** 