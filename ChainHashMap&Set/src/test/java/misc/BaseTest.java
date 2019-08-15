package misc;

import org.junit.Assert;

import java.lang.reflect.Field;
import java.util.Objects;

public class BaseTest {
    protected static final int SECOND = 1000;

    /**
     * A wrapper around JUnit's assertEquals method that requires that both parameters
     * be of the same type.
     */
    protected static <T> void assertEquals(T expected, T actual) {
        Assert.assertEquals(expected, actual);
    }

    /**
     * A wrapper around JUnit's assertEquals method that requires that both parameters
     * be of the same type.
     */
    protected static <T> void assertEquals(String message, T expected, T actual) {
        Assert.assertEquals(message, expected, actual);
    }

    /**
     * A helper method that can extract values from private fields of objects using a
     * technique called reflection. This variant additionally provides a return type
     * to cast the value to.
     *
     * You do not need to know how this method works, but you should be aware that you should
     * not submit test code that uses this method, since it requires that you know the name
     * of a field in order to extract it. (Our code may use different names than yours.)
     */
    protected static <T> T getField(Object obj, String fieldName, Class<T> expectedType) {
        return expectedType.cast(getField(obj, fieldName));
    }

    /**
     * A helper method that can extract values from private fields of objects using a
     * technique called reflection.
     *
     * You do not need to know how this method works, but you should be aware that you should
     * not submit test code that uses this method, since it requires that you know the name
     * of a field in order to extract it. (Our code may use different names than yours.)
     */
    protected static Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This is a wrapper class for arbitrary objects that additionally allows us to
     * define a custom hash code.
     *
     * If no hash code is provided, the object's existing hash code is used instead.
     *
     * It is up to the user to make sure that the hash codes assigned are valid.
     * (E.g., the user must ensure that two Wrapper objects with equal inner objects
     * also have equal hash codes).
     */
    protected static class Wrapper<T> {
        private T inner;
        private int hashCode;

        public Wrapper(T inner) {
            this(inner, inner == null ? 0 : inner.hashCode());
        }

        public Wrapper(T inner, int hashCode) {
            this.inner = inner;
            this.hashCode = hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }

            Wrapper<?> wrapper = (Wrapper<?>) o;

            return Objects.equals(inner, wrapper.inner);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }
}
