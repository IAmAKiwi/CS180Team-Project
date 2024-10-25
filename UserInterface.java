public interface UserInterface {
    // Methods we definitely need
    String getUsername();
    String getPassword();

    //Methods we might need (depends on implementation)
    void setUsername(String username);
    void setPassword(String password);
    void setProfile(UserProfile profile);
    UserProfile getProfile();
}
