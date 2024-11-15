import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * A framework to run public test cases for Database
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 *         lab sec 12
 *
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

    public static class TestCase {
        @Test(timeout = 1000)
        public void testBasicUser() {
            User u1 = new User("test1", "test1");
            User u2 = new User("test2", "test2");
            Assert.assertEquals("test1", u1.getUsername());
            Assert.assertEquals("test2", u2.getPassword());
        }

        @Test(timeout = 1000)
        public void testFriendsAndBlocks() {
            User u1 = new User("test1", "test1");
            u1.addBlock("badGuy");
            u1.addFriend("bestie");
            u1.addFriend("bestie2");
            ArrayList<String> friends = new ArrayList<>();
            friends.add("bestie");
            friends.add("bestie2");
            Assert.assertArrayEquals(friends.toArray(), u1.getFriends().toArray());
            Assert.assertEquals(2, u1.getFriends().size());
            Assert.assertEquals(1, u1.getBlocked().size());
        }

        @Test(timeout = 1000)
        public void testUserProfile() {
            User u2 = new User("test2", "test2");
            u2.setBio("test bio");
            u2.setBirthday(new int[] { 9, 15, 2005 });
            u2.setFriendsOnly(true);
            Assert.assertEquals("test bio", u2.getBio());
            Assert.assertArrayEquals(new int[] { 9, 15, 2005 }, u2.getBirthday());
            Assert.assertTrue(u2.isFriendsOnly());
        }

        @Test(timeout = 1000)
        public void testInvalidBirthday() {
            User u = new User("test", "test");
            u.setBirthday(new int[] { 13, 32, 2025 }); // Invalid month and day
            Assert.assertNull(u.getBirthday());
        }

        @Test(timeout = 1000)
        public void testEmptyUser() {
            User u = new User();
            Assert.assertNull(u.getUsername());
            Assert.assertNull(u.getPassword());
            Assert.assertNotNull(u.getFriends());
            Assert.assertNotNull(u.getBlocked());
        }

        @Test(timeout = 1000)
        public void testDuplicateFriends() {
            User u = new User("test", "test");
            u.addFriend("friend1");
            u.addFriend("friend1");
            Assert.assertEquals(2, u.getFriends().size()); // ArrayList allows duplicates
        }

        @Test(timeout = 1000)
        public void testCompleteUserProfile() {
            User u = new User("test", "test", "John", "Doe", "Bio",
                    new int[] { 1, 1, 2000 }, "pic.jpg",
                    new ArrayList<>(), new ArrayList<>(), true);
            Assert.assertEquals("John", u.getFirstName());
            Assert.assertEquals("Doe", u.getLastName());
            Assert.assertEquals("Bio", u.getBio());
            Assert.assertEquals("pic.jpg", u.getProfilePic());
            Assert.assertTrue(u.isFriendsOnly());
        }
    }
}