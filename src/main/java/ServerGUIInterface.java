import java.awt.event.ActionListener;

public interface ServerGUIInterface {
    /**
     * Check if the Server GUI is ready (port and host have been set).
     * 
     * @return true if the Server GUI is ready; false otherwise.
     */
    boolean isReady();

    /**
     * Runs the Server GUI and initializes the main components.
     */
    void run();
}
