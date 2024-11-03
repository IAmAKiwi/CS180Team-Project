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
        public void MessageHistoryTest() {
            // Tests the creation of a MessageHistory
            ArrayList<Message> messageHistory = new ArrayList<Message>();
            String[] users = { "User1", "User2" };
            Message m1 = new Message("Hello World!", "User1");
            Message m2 = new Message("Goodbye World!", "User2");
            messageHistory.add(m1);
            messageHistory.add(m2);
            MessageHistory mh = new MessageHistory(users);
            mh.setMessageHistory(messageHistory);
            // Tests accessing MessageHistory messages.
            Assert.assertEquals("User1: Hello World!", mh.getMessageHistory().get(0).toString());
            Assert.assertEquals("User2", mh.getMessageHistory().get(1).getSender());
            Assert.assertEquals("Goodbye World!", mh.getMessageHistory().get(1).getMessage());
            Assert.assertEquals("User1 User2", mh.toString());

        }
    }
}