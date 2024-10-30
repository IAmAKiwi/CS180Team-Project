import java.util.ArrayList;

public interface MessageHistoryInterface {
    String[] getUsernames();
    public ArrayList<Message> getMessageHistory();
    public void setMessageHistory(ArrayList<Message> messageHistory);
    public void setUserMessagers(String[] userMessagers);
}
