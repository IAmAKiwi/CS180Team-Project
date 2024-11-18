
public class DataSaver implements Runnable, DataSaverInterface {
    private Database db;

    public DataSaver(Database db) {
        this.db = db;
    }

    public void run() {
        db.saveMessages();
        db.saveUsers();
        System.out.println("Data successfully saved"); //for testing
    }
}
