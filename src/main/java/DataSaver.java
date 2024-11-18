/**
 * Class to automatically save database when server is stopped.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 *
 * @version Nov 17, 2024
 */
public class DataSaver implements Runnable, DataSaverInterface {
    private Database db;

    public DataSaver(Database db) {
        this.db = db;
    }

    public void run() {
        db.saveMessages();
        db.saveUsers();
        System.out.println("Data successfully saved"); // for testing
    }
}
