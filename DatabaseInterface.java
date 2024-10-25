public interface DatabaseInterface {
    // Methods we definitely need
    void addUser(User user);
    ArrayList<User> getUsers();
    User getUser(String username);
    String getUserName(String username);

    void addMessage(Message message); // Could change based upon how we implement message/message history.
        // Depends on whether message holds the recipients.
    MessageHistory getMessages();

    boolean saveUsers();
    boolean saveMessages();
    boolean loadUsers();
    boolean loadMessages();

    // Methods we may need (depends on implementation)
    boolean createUser(String username, String password);
}
