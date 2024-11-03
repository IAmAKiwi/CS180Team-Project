public interface PhotoMessageInterface {
    // Methods we definitely need (TODO: Find class that holds the Photo)
    String getPhoto();
    String getSender();

    // Methods we may need (depends on implementation, could be a String[])
    String getReceiver();
}
