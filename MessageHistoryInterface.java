public interface MessageHistoryInterface {
    void addMessage(Message message);
    ArrayList<Message> getMessages();
    String[] getUsernames();
}
