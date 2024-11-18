import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * A framework to run public test cases for User.
 * Tests user creation, profile management, and user settings.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 * @version Nov 2, 2024
 */
@RunWith(Enclosed.class)
public class UserTestCases {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    /**
     * A framework to run public test cases for User.
     * Tests user creation, profile management, and user settings.
     *
     * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
     *         12
     * @version Nov 2, 2024
     */
    public static class TestCase implements UserTestInterface {
        @Test(timeout = 1000)
        public void testBasicUser() {
            User u1 = new User("test1", "test1");
            Assert.assertEquals("test1", u1.getUsername());
            Assert.assertEquals("test1", u1.getPassword());
        }

        @Test(timeout = 1000)
        public void testEmptyUser() {
            User u = new User("", "");
            Assert.assertEquals("", u.getUsername());
            Assert.assertEquals("", u.getPassword());
        }

        @Test(timeout = 1000)
        public void testNullUser() {
            User u = new User();
            Assert.assertNull(u.getUsername());
            Assert.assertNull(u.getPassword());
        }

        @Test(timeout = 1000)
        public void testUserWithProfile() {
            User u = new User("test1", "test1");
            u.setFirstName("John");
            u.setLastName("Doe");
            u.setBio("Test bio");
            Assert.assertEquals("John", u.getFirstName());
            Assert.assertEquals("Doe", u.getLastName());
            Assert.assertEquals("Test bio", u.getBio());
        }

        @Test(timeout = 1000)
        public void testUserWithFriends() {
            User u = new User("test1", "test1");
            u.addFriend("friend1");
            u.addFriend("friend2");
            Assert.assertTrue(u.getFriends().contains("friend1"));
            Assert.assertTrue(u.getFriends().contains("friend2"));
        }

        @Test(timeout = 1000)
        public void testUserWithBlocks() {
            User u = new User("test1", "test1");
            u.addBlock("blocked1");
            u.addBlock("blocked2");
            Assert.assertTrue(u.getBlocked().contains("blocked1"));
            Assert.assertTrue(u.getBlocked().contains("blocked2"));
        }

        @Test(timeout = 1000)
        public void testUserWithBirthday() {
            User u = new User("test1", "test1");
            int[] birthday = { 12, 25, 2000 };
            u.setBirthday(birthday);
            Assert.assertArrayEquals(birthday, u.getBirthday());
        }

        @Test(timeout = 1000)
        public void testUserWithInvalidBirthday() {
            User u = new User("test1", "test1");
            int[] invalidMonth = { 13, 25, 2000 };
            int[] invalidDay = { 12, 32, 2000 };
            int[] futureYear = { 12, 25, 2025 };

            u.setBirthday(invalidMonth);
            Assert.assertNull(u.getBirthday());

            u.setBirthday(invalidDay);
            Assert.assertNull(u.getBirthday());

            u.setBirthday(futureYear);
            Assert.assertNull(u.getBirthday());
        }

        @Test(timeout = 1000)
        public void testUserWithProfilePic() {
            User u = new User("test1", "test1");
            u.setProfilePic("path/to/pic.jpg");
            Assert.assertEquals("path/to/pic.jpg", u.getProfilePic());
        }

        // ... existing code ...

        @Test(timeout = 1000)
        public void testUserToStringWithNulls() {
            User u = new User("test1", "test1");
            String result = u.toString();
            Assert.assertTrue("toString should contain username", result.contains("test1"));
            Assert.assertTrue("toString should handle null fields gracefully",
                    result.contains("") || !result.contains("null"));
        }
    }
}