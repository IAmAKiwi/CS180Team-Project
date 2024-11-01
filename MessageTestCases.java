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
        public void MessageTest() {
            Message m1 = new Message("Hello World!", "User1");
            Message m2 = new Message("Goodbye World!", "User2");
            Assert.assertEquals("User1: Hello World!", m1.toString());
            Assert.assertEquals("User2", m2.getSender());
            Assert.assertEquals("Goodbye World!", m2.getMessage());
        }
    }
}