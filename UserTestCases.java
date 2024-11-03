import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * A framework to run public test cases for User.
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec 12
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
        public void UserTest() {
            // Testing name/password setting.
            User u1 = new User("test1", "test1");
            User u2 = new User("test2", "test2");
            Assert.assertEquals("test1", u1.getUsername());
            Assert.assertEquals("test2", u2.getPassword());

            //Testing adding friends and blocks
            u1.addBlock("badGuy");
            u1.addFriend("bestie");
            u1.addFriend("bestie2");
            ArrayList<String> arr = new ArrayList<String>();
            arr.add("bestie");
            arr.add("bestie2");
            Assert.assertArrayEquals(arr.toArray(), u1.getFriends().toArray());
            Assert.assertEquals(2, u1.getFriends().size());
            Assert.assertEquals(1, u1.getBlocked().size());

            // Testing bio/birthday/friendsOnly/removing friends.
            u2.setBio("test bio");
            u2.setBirthday(new int[]{9, 15, 2005});
            u2.setBirthday(new int[]{9, 15, 2025});
            u2.setFriendsOnly(true);
            u2.addFriend("fakeBestie");
            u2.removeFriend("fakeBestie");
            u2.addBlock("bestie");
            u2.unblock("bestie");

            Assert.assertEquals("test bio", u2.getBio());
            Assert.assertArrayEquals(new int[]{9, 15, 2005}, u2.getBirthday());
            Assert.assertTrue(u2.isFriendsOnly());
            int[] arr2 = new int[]{9, 15, 2005};
            Assert.assertArrayEquals(arr2, u2.getBirthday());
            Assert.assertEquals(0, u2.getFriends().size());
            Assert.assertEquals(0, u2.getBlocked().size());
        }
    }
}