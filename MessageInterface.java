public interface MessageInterface {
    // Methods we definitely need
    String getMessage();
    String getSender();

    // Methods we may need (depends on implementation, could be a String[])
    //String getReceiver();
}
