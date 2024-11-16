import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * A framework to run public test cases for Message
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 *         lab sec 12
 *
 * @version Nov 2, 2024
 */

@RunWith(Enclosed.class)
public class MessageTestCases {

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
        public void testBasicMessage() {
            Message m1 = new Message("Hello World!", "User1");
            Message m2 = new Message("Goodbye World!", "User2");
            Assert.assertEquals("User1: Hello World!", m1.toString());
            Assert.assertEquals("User2", m2.getSender());
            Assert.assertEquals("Goodbye World!", m2.getMessage());
        }

        @Test(timeout = 1000)
        public void testEmptyMessage() {
            Message m = new Message("", "User1");
            Assert.assertEquals("", m.getMessage());
            Assert.assertEquals("User1: ", m.toString());
        }

        @Test(timeout = 1000)
        public void testNullMessage() {
            Message m = new Message();
            Assert.assertNull(m.getMessage());
            Assert.assertNull(m.getSender());
        }

        @Test(timeout = 1000)
        public void testLongMessage() {
            StringBuilder longMsg = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMsg.append("a");
            }
            Message m = new Message(longMsg.toString(), "User1");
            Assert.assertEquals(1000, m.getMessage().length());
        }

        @Test(timeout = 1000)
        public void testSpecialCharacters() {
            Message m = new Message("Hello\n\t\r!@#$%^&*()", "User1");
            Assert.assertEquals("Hello\n\t\r!@#$%^&*()", m.getMessage());
        }

        @Test(timeout = 1000)
        public void testUnicodeCharacters() {
            Message m = new Message("Hello 世界!", "User1");
            Assert.assertEquals("Hello 世界!", m.getMessage());
        }

        @Test(timeout = 1000)
        public void testMessageWithOnlySpaces() {
            Message m = new Message("   ", "User1");
            Assert.assertEquals("   ", m.getMessage());
            Assert.assertEquals("User1:    ", m.toString());
        }

        @Test(timeout = 1000)
        public void testMessageWithMultipleLines() {
            Message m = new Message("Line1\nLine2\nLine3", "User1");
            Assert.assertEquals("Line1\nLine2\nLine3", m.getMessage());
        }

        @Test(timeout = 1000)
        public void testMessageWithEmptySender() {
            Message m = new Message("Hello", "");
            Assert.assertEquals("", m.getSender());
            Assert.assertEquals(": Hello", m.toString());
        }

        @Test(timeout = 1000)
        public void testMessageWithNullSender() {
            Message m = new Message("Hello", null);
            Assert.assertNull(m.getSender());
        }
    }
}