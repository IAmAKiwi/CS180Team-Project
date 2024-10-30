import java.util.ArrayList;

public class MessageHistory implements MessageHistoryInterface {
    private String[] userMessagers;
    private ArrayList<Message> messageHistory; // in each element of the arraylist will contain username: content, time 
    public MessageHistory() {
        userMessagers = null;
        messageHistory = null;
    }

    public MessageHistory(Message message, String recipient) {
        userMessagers = new String[2];
        userMessagers[0] = recipient;
        userMessagers[1] = message.getSender();
        messageHistory = new ArrayList<Message>();
        messageHistory.add(message);
    }

    public ArrayList<Message> getMessageHistory() {
        return messageHistory;
    }

    public String[] getUserMessagers() {
        return userMessagers;
    }

    //idk if we need these yet

    public void setMessageHistory(ArrayList<Message> messageHistory) {
        this.messageHistory = messageHistory;
    }

    public void setUserMessagers(String[] userMessagers) {
        this.userMessagers = userMessagers;
    }
}
