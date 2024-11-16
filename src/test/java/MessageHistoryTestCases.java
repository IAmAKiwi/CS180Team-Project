import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * A framework to run public test cases for MessageHistory
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Peter Bui: lab sec
 *         12
 *
 * @version Nov 2, 2024
 */
@RunWith(Enclosed.class)
public class MessageHistoryTestCases {

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
        public void testBasicMessageHistory() {
            ArrayList<Message> messageHistory = new ArrayList<>();
            String[] users = { "User1", "User2" };
            Message m1 = new Message("Hello World!", "User1");
            Message m2 = new Message("Goodbye World!", "User2");
            messageHistory.add(m1);
            messageHistory.add(m2);
            MessageHistory mh = new MessageHistory(users);
            mh.setMessageHistory(messageHistory);
            Assert.assertEquals("User1: Hello World!", mh.getMessageHistory().get(0).toString());
            Assert.assertEquals("User2", mh.getMessageHistory().get(1).getSender());
            Assert.assertEquals("Goodbye World!", mh.getMessageHistory().get(1).getMessage());
            Assert.assertEquals("User1 User2", mh.toString());
        }

        @Test(timeout = 1000)
        public void testEmptyMessageHistory() {
            String[] users = { "User1", "User2" };
            MessageHistory mh = new MessageHistory(users);
            Assert.assertEquals(0, mh.getMessageHistory().size());
        }

        @Test(timeout = 1000)
        public void testDeleteMessage() {
            String[] users = { "User1", "User2" };
            MessageHistory mh = new MessageHistory(users);
            Message m1 = new Message("Hello", "User1");
            mh.addMessage(m1);
            Assert.assertEquals(1, mh.getMessageHistory().size());
            mh.deleteMessage(m1);
            Assert.assertEquals(0, mh.getMessageHistory().size());
        }

        @Test(timeout = 1000)
        public void testMessageHistoryEquality() {
            String[] users1 = { "User1", "User2" };
            String[] users2 = { "User2", "User1" };
            MessageHistory mh1 = new MessageHistory(users1);
            MessageHistory mh2 = new MessageHistory(users2);
            Assert.assertTrue(mh1.equals(mh2));
        }

        @Test(timeout = 1000)
        public void testNullMessageHistory() {
            MessageHistory mh = new MessageHistory();
            Assert.assertNull(mh.getUsernames());
            Assert.assertNotNull(mh.getMessageHistory());
        }

        @Test(timeout = 1000)
        public void testLargeMessageHistory() {
            String[] users = { "User1", "User2" };
            MessageHistory mh = new MessageHistory(users);
            for (int i = 0; i < 1000; i++) {
                mh.addMessage(new Message("Message " + i, "User1"));
            }
            Assert.assertEquals(1000, mh.getMessageHistory().size());
        }

        @Test(timeout = 1000)
        public void testMultipleUsersMessageHistory() {
            String[] users = { "User1", "User2", "User3" };
            MessageHistory mh = new MessageHistory(users);
            Assert.assertEquals(3, mh.getUsernames().length);
        }

        @Test(timeout = 1000)
        public void testMessageHistoryWithNullMessage() {
            String[] users = { "User1", "User2" };
            MessageHistory mh = new MessageHistory(users);
            mh.addMessage(null);
            Assert.assertEquals(1, mh.getMessageHistory().size());
        }

        @Test(timeout = 1000)
        public void testDeleteNonExistentMessage() {
            String[] users = { "User1", "User2" };
            MessageHistory mh = new MessageHistory(users);
            Message m1 = new Message("Hello", "User1");
            mh.deleteMessage(m1);
            Assert.assertEquals(0, mh.getMessageHistory().size());
        }

        @Test(timeout = 1000)
        public void testMessageHistoryConstructorWithMessage() {
            Message firstMessage = new Message("Hello", "User1");
            MessageHistory mh = new MessageHistory(firstMessage, "User2");
            Assert.assertEquals("User1", mh.getSender());
            Assert.assertEquals("User2", mh.getRecipient());
            Assert.assertEquals(1, mh.getMessageHistory().size());
        }
    }
}
