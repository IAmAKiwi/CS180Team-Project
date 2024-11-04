
void testSaveUsers_FileOutput() throws IOException {
    User user1 = new User("user1", "Password1$");
    User user2 = new User("user2", "Password2$");
    db.addUser(user1);
    db.addUser(user2);
    db.saveUsers();

    char groupSeparator = 29;
    char fileSeparator = 28;

    try (BufferedReader reader = new BufferedReader(new FileReader("usersHistory.txt"))) {
        String line = reader.readLine();
        String[] liness = line.replace(String.valueOf(fileSeparator), "").split(String.valueOf(groupSeparator));
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(liness));
        assertTrue(lines.contains("username: user1"));
        assertTrue(lines.contains("password: Password1$"));
        assertTrue(lines.contains("username: user2"));
        assertTrue(lines.contains("password: Password2$"));
    } catch (IOException e) {
        e.printStackTrace(); // Handle exceptions as needed
    }
}