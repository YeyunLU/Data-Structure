package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDoubleLinkedList extends BaseTest {
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
            assertTrue("Actual list has more elements than expected array.", size < expected.length);

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

    /**
     * Note: We use 1 second as the default timeout for many of our tests.
     *
     * One second is typically extremely generous: most of your tests should
     * finish in milliseconds. If one of your tests is timing out, you're almost
     * certainly doing something wrong.
     */

    @Test(timeout=SECOND)
    public void basicTestAddAndGet() {
        IList<String> list = makeBasicList();
        this.assertListValidAndMatches(new String[] {"a", "b", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void basicTestAddIncrementsSize() {
        IList<String> list = makeBasicList();
        int initSize = list.size();
        list.add("d");

        assertEquals(initSize + 1, list.size());
    }

    @Test(timeout=SECOND)
    public void basicTestRemoveDecrementsSize() {
        IList<String> list = makeBasicList();
        int initSize = list.size();
        list.remove();

        assertEquals(initSize - 1, list.size());
    }

    @Test(timeout=SECOND)
    public void basicTestSet() {
        IList<String> list = makeBasicList();
        int initSize = list.size();
        list.set(1, "d");

        assertEquals("d", list.get(1));
        assertEquals(initSize, list.size());
    }

    @Test(timeout=SECOND)
    public void basicTestContains() {
        IList<String> list = makeBasicList();

        assertTrue(list.contains("a"));
        assertTrue(list.contains("b"));
        assertTrue(list.contains("c"));
        assertFalse(list.contains("d"));
    }

    @Test(timeout=SECOND)
    public void basicTestIndexOf() {
        IList<String> list = makeBasicList();

        assertEquals(0, list.indexOf("a"));
        assertEquals(1, list.indexOf("b"));
        assertEquals(2, list.indexOf("c"));
        assertEquals(-1, list.indexOf("d"));
    }

    @Test(timeout=SECOND)
    public void basicTestInsert() {
        IList<String> list = this.makeBasicList();
        list.insert(0, "x");
        this.assertListValidAndMatches(new String[] {"x", "a", "b", "c"}, list);

        list.insert(2, "y");
        this.assertListValidAndMatches(new String[] {"x", "a", "y", "b", "c"}, list);

        list.insert(5, "z");
        this.assertListValidAndMatches(new String[] {"x", "a", "y", "b", "c", "z"}, list);
    }

    @Test(timeout=2 * SECOND)
    public void testAddAndGetMany() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 1000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
        for (int i = 0; i < cap; i++) {
            int value = list.get(i);
            assertEquals(i* 2, value);
        }
        assertEquals(cap, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testAddIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
    }

    @Test(timeout=SECOND)
    public void testRemoveMultiple() {
        IList<String> list = this.makeBasicList();
        assertEquals("c", list.remove());
        this.assertListValidAndMatches(new String[] {"a", "b"}, list);

        assertEquals("b", list.remove());
        this.assertListValidAndMatches(new String[] {"a"}, list);

        assertEquals("a", list.remove());
        this.assertListValidAndMatches(new String[] {}, list);
    }

    @Test(timeout=SECOND)
    public void testRemoveMany() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 1000;

        for (int i = 0; i < cap; i++) {
            list.add(i);
        }

        assertEquals(cap, list.size());

        for (int i = cap - 1; i >= 0; i--) {
            int value = list.remove();
            assertEquals(i, value);
        }

        assertEquals(0, list.size());
    }

    @Test(timeout=SECOND)
    public void testAlternatingAddAndRemove() {
        int iterators = 1000;

        IList<String> list = new DoubleLinkedList<>();

        for (int i = 0; i < iterators; i++) {
            String entry = "" + i;
            list.add(entry);
            assertEquals(1, list.size());

            String out = list.remove();
            assertEquals(entry, out);
            assertEquals(0, list.size());
        }
    }

    @Test(timeout=5 * SECOND)
    public void testRemoveFromEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }

        for (int i = 0; i < 10000; i++) {
            list.add(-1);
            list.remove();
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveOnEmptyListThrowsException() {
        IList<String> list = this.makeBasicList();
        list.remove();
        list.remove();
        list.remove();
        try {
            list.remove();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testGetOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();
        try {
            list.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        // This should be ok
        list.get(2);

        try {
            // Now we're out of bounds
            list.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        try {
            list.get(1000);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testSet() {
        IList<String> list = this.makeBasicList();

        list.set(0, "AAA");
        assertListValidAndMatches(new String[] {"AAA", "b", "c"}, list);

        list.set(1, "BBB");
        assertListValidAndMatches(new String[] {"AAA", "BBB", "c"}, list);

        list.set(2, "CCC");
        assertListValidAndMatches(new String[] {"AAA", "BBB", "CCC"}, list);
    }

    @Test(timeout=SECOND)
    public void testSetSingleElement() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");

        list.set(0, "bar");
        assertListValidAndMatches(new String[] {"bar"}, list);

        list.set(0, "baz");
        assertListValidAndMatches(new String[] {"baz"}, list);
    }

    @Test(timeout=SECOND)
    public void testSetOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        try {
            list.set(-1, "AAA");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // This is ok: do nothing
        }

        try {
            list.set(3, "AAA");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // This is ok: do nothing
        }
    }

    @Test(timeout=5 * SECOND)
    public void testSetMany() {
        IList<String> list = new DoubleLinkedList<>();
        int cap = 10000;

        for (int i = 0; i < cap; i++) {
            list.add("foo" + i);
        }

        for (int i = 0; i < cap; i++) {
            list.set(i, "bar" + i);
        }

        for (int i = 0; i < cap; i++) {
            assertEquals("bar" + i, list.get(i));
        }

        for (int i = cap - 1; i >= 0; i--) {
            list.set(i, "qux" + i);
        }

        for (int i = cap - 1; i >= 0; i--) {
            assertEquals("qux" + i, list.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testInsertEmptyAndSingleElement() {
        // Lists 1 and 2: insert into empty
        IList<String> list1 = new DoubleLinkedList<>();
        IList<String> list2 = new DoubleLinkedList<>();
        list1.insert(0, "a");
        list2.insert(0, "a");

        // No point in checking both lists
        this.assertListValidAndMatches(new String[] {"a"}, list1);

        // List 1: insert at front
        list1.insert(0, "b");
        this.assertListValidAndMatches(new String[] {"b", "a"}, list1);

        // List 2: insert at end
        list2.insert(1, "b");
        this.assertListValidAndMatches(new String[] {"a", "b"}, list2);
    }

    @Test(timeout=SECOND)
    public void testInsertOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        try {
            list.insert(-1, "a");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        try {
            list.insert(4, "a");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=15 * SECOND)
    public void testInsertAtEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.insert(list.size(), i * 2);
        }
        assertEquals(cap, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testInsertNearEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(-1);
        list.add(-2);

        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.insert(list.size() - 2, i * 2);
        }
        assertEquals(cap + 2, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testInsertAtFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.insert(0, i * 2);
        }
        assertEquals(cap, list.size());
    }

    @Test(timeout=SECOND)
    public void testIndexOfAndContainsCorrectlyCompareItems() {
        // Two different String objects, but with equal values
        String item1 = "abcdefghijklmnopqrstuvwxyz";
        String item2 = item1 + "";

        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");
        list.add(item1);

        assertEquals(1, list.indexOf(item2));
        assertTrue(list.contains(item2));
    }

    @Test(timeout=5 * SECOND)
    public void testIndexOfAndContainsMany() {
        int cap = 1000;
        int stringLength = 100;
        String validChars = "abcdefghijklmnopqrstuvwxyz0123456789";

        // By setting the seed to some arbitrary but constant number, we guarantee
        // this random number generator will produce the exact same sequence of numbers
        // every time we run this test. This helps us keep our tests deterministic, which
        // can help with debugging.
        Random rand = new Random();
        rand.setSeed(12345);

        IList<String> list = new DoubleLinkedList<>();
        IList<String> refList = new DoubleLinkedList<>();

        for (int i = 0; i < cap; i++) {
            String entry = "";
            for (int j = 0; j < stringLength; j++) {
                int charIndex = rand.nextInt(validChars.length());
                entry += validChars.charAt(charIndex);
            }

            list.add(entry);
            if (i % 100 == 0) {
                refList.add(entry);
            }
        }

        for (int i = 0; i < refList.size(); i++) {
            String entry = refList.get(i);
            assertEquals(i * 100, list.indexOf(entry));
            assertTrue(list.contains(entry));
        }
    }

    @Test(timeout=SECOND)
    public void testNullElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        assertEquals(-1, list.indexOf(null));
        assertFalse(list.contains(null));

        list.insert(2, null);
        assertListValidAndMatches(new Integer[]{1, 2, null, 3, 4}, list);

        assertEquals(2, list.indexOf(null));
        assertTrue(list.contains(null));
        assertTrue(list.contains(4));
    }

    @Test(timeout=SECOND)
    public void testIteratorBasic() {
        IList<String> list = this.makeBasicList();
        Iterator<String> iter = list.iterator();

        // Get first element
        for (int i = 0; i < 5; i++) {
            assertTrue(iter.hasNext());
        }
        assertEquals("a", iter.next());

        // Get second
        for (int i = 0; i < 5; i++) {
            assertTrue(iter.hasNext());
        }
        assertEquals("b", iter.next());

        // Get third
        for (int i = 0; i < 5; i++) {
            assertTrue(iter.hasNext());
        }
        assertEquals("c", iter.next());

        for (int i = 0; i < 5; i++) {
            assertFalse(iter.hasNext());
        }

        try {
            iter.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // This is ok: do nothing
        }

        // Check that the list is unchanged
        this.assertListValidAndMatches(new String[]{"a", "b", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testIteratorOnEmptyList() {
        IList<String> list = new DoubleLinkedList<>();

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < 5; j++) {
                assertFalse(iter.hasNext());
            }
            try {
                iter.next();
                fail("Expected NoSuchElementException");
            } catch (NoSuchElementException ex) {
                // This is ok: do nothing
            }
        }

        assertListValidAndMatches(new String[] {}, list);
    }

    @Test(timeout=SECOND)
    public void testIteratorSingleElement() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < 5; j++) {
                assertTrue(iter.hasNext());
            }
            assertEquals("foo", iter.next());
            for (int j = 0; j < 5; j++) {
                assertFalse(iter.hasNext());
            }
            try {
                iter.next();
                fail("Expected NoSuchElementException");
            } catch (NoSuchElementException ex) {
                // This is ok: do nothing
            }
        }

        assertListValidAndMatches(new String[] {"foo"}, list);
    }

    @Test(timeout=SECOND)
    public void testIteratorMany() {
        IList<String> list = this.makeBasicList();
        String[] expected = {"a", "b", "c"};

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < expected.length; j++) {
                for (int k = 0; k < 5; k++) {
                    assertTrue(iter.hasNext());
                }
                assertEquals(expected[j], iter.next());
            }

            for (int j = 0; j < 5; j++) {
                assertFalse(iter.hasNext());
            }
        }

        assertListValidAndMatches(expected, list);
        list.insert(2, "z");
        assertListValidAndMatches(new String[] {"a", "b", "z", "c"}, list);
    }

    @Test(timeout=15 * SECOND)
    public void testIteratorIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
        int count = 0;
        for (int num : list) {
            assertEquals(count, num);
            count += 2;
        }
    }

    @SuppressWarnings("unchecked")
    @Test(timeout=SECOND)
    public void testCustomObjectValues() {
        IList<Wrapper<Integer>> list = new DoubleLinkedList<>();
        list.add(new Wrapper<>(1));
        list.add(new Wrapper<>(2));
        list.add(new Wrapper<>(3));
        list.add(new Wrapper<>(4));

        assertListValidAndMatches(
                new Wrapper[] {new Wrapper<>(1), new Wrapper<>(2), new Wrapper<>(3), new Wrapper<>(4)},
                list);

        assertEquals(2, list.indexOf(new Wrapper<>(3)));
        list.delete(2);

        assertListValidAndMatches(
                new Wrapper[] {new Wrapper<>(1), new Wrapper<>(2), new Wrapper<>(4)},
                list);

        list.insert(0, new Wrapper<>(4));
        assertEquals(0, list.indexOf(new Wrapper<>(4)));
    }
}
