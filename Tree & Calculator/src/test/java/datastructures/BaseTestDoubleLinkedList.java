package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;

/**
 * This file provides some helper methods for DoubleLinkedList tests.
 *
 * Changes in this file will be ignored during grading.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // This annotation makes JUnit run tests in alphabetical order
public class BaseTestDoubleLinkedList extends BaseTest {
    /**
     * This method creates a simple list containing three elements to help minimize
     * redundancy later in our tests.
     */
    protected IList<String> makeBasicList() {
        IList<String> list = new DoubleLinkedList<>();

        list.add("a");
        list.add("b");
        list.add("c");

        return list;
    }

    /**
     * This function checks whether a list is a valid doubly-linked list and
     * contains exactly the same elements as the "expected" array.
     *
     * See the tests provided for example usage, and see the other two helper methods
     * for more details on how they work.
     * @see TestDoubleLinkedList
     */
    protected <T> void assertListValidAndMatches(T[] expected, IList<T> actual) {
        assertListValid(expected, actual);
        assertListMatches(expected, actual);
    }

    /**
     * This function checks whether the node pointers inside the given list form a
     * valid doubly-linked list, and checks that the data in each node corresponds
     * to the values in the "expected" array.
     */
    private <T> void assertListValid(T[] expected, IList<T> actual) {
        // Extract the front and back fields from the list
        Object front = getField(actual, "front");
        Object back = getField(actual, "back");

        // Handle edge cases: empty and single-element lists
        if (expected.length == 0) {
            assertEquals("Actual list has more elements than expected array.", null, front);
            assertEquals("Actual list has more elements than expected array.", null, back);
            return;
        } else if (expected.length == 1) {
            assertEquals("List nodes do not form a valid linked list.", null, getPrev(front));
            assertEquals("List nodes do not form a valid linked list.", null, getNext(back));
            assertEquals("List node contains incorrect data.", expected[0], getData(front));
            assertEquals("Actual list has more elements than expected array.", front, back);
            return;
        }

        // Handle regular cases

        // Check front node (front.prev, front.next.prev, front.data)
        assertEquals("List nodes do not form a valid linked list.", null, getPrev(front));
        assertEquals("List nodes do not form a valid linked list.", front, getPrev(getNext(front)));
        assertEquals("List node contains incorrect data.", expected[0], getData(front));

        // Check whether middle nodes are correct
        int size = 1;
        Object curr = getNext(front);
        while (curr != back) {
            // Make sure that we haven't reached the end of the expected values yet
            assertTrue("Actual list has more elements than expected array.",size < expected.length);

            // Check that:
            //   curr.prev.next points to curr
            //   curr.next.prev points to curr
            Object prev = getPrev(curr);
            Object next = getNext(curr);
            assertEquals("List nodes do not form a valid linked list.", curr, getNext(prev));
            assertEquals("List nodes do not form a valid linked list.", curr, getPrev(next));
            assertEquals("List node contains incorrect data.", expected[size], getData(curr));

            size++;
            curr = next;
        }

        // Make sure that we haven't reached the end of the expected values yet
        assertTrue("Actual list has more elements than expected array.", size < expected.length);
        // Check back node (back.next, back.prev.next, back.data)
        assertEquals("List nodes do not form a valid linked list.", null, getNext(curr));
        assertEquals("List nodes do not form a valid linked list.", curr, getNext(getPrev(curr)));
        assertEquals("List node contains incorrect data.", expected[size], getData(curr));
        size++;

        // Make sure that we've reached the end of the expected values
        assertEquals("Actual list has fewer elements than expected array.", expected.length, size);
    }

    /**
     * This function checks whether a list contains exactly the same elements as
     * the "expected" array using the get method.
     */
    private <T> void assertListMatches(T[] expected, IList<T> actual) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected.length == 0, actual.isEmpty());

        for (int i = 0; i < expected.length; i++) {
            try {
                assertEquals("Item at index " + i + " does not match", expected[i], actual.get(i));
            } catch (Exception ex) {
                String errorMessage = String.format(
                        "Got %s when getting item at index %d (expected '%s')",
                        ex.getClass().getSimpleName(),
                        i,
                        expected[i]);
                throw new AssertionError(errorMessage, ex);
            }
        }
    }

    /**
     * A helper method to extract the prev field of a node.
     */
    private Object getPrev(Object node) {
        if (node == null) {
            throw new NullPointerException();
        }
        return getField(node, "prev");
    }

    /**
     * A helper method to extract the next field of a node.
     */
    private Object getNext(Object node) {
        if (node == null) {
            throw new NullPointerException();
        }
        return getField(node, "next");
    }

    /**
     * A helper method to extract the data field of a node.
     */
    private Object getData(Object node) {
        if (node == null) {
            throw new NullPointerException();
        }
        return getField(node, "data");
    }
}
