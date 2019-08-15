package misc;

import org.junit.Assert;

public class BaseTest {
    protected static final int SECOND = 1000;

    protected static <T> void assertEquals(T expected, T actual) {
        Assert.assertEquals(expected, actual);
    }

    protected static <T> void assertEquals(String message, T expected, T actual) {
        Assert.assertEquals(message, expected, actual);
    }
}
