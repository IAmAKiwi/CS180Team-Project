import java.util.ArrayList;

public interface MessageHistoryInterface {
    String[] getUsernames();
    public ArrayList<Message> getMessageHistory();
    public void setMessageHistory(ArrayList<Message> messageHistory);
    public void setUserMessagers(String[] userMessagers);
    void deleteMessage(Message message);
    void addMessage(Message message);
    String getRecipient();
    String getSender();
    boolean equals(Object other);
    String toString();
}
