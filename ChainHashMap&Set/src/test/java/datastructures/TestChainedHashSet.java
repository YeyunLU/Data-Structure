package datastructures;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import misc.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestChainedHashSet extends BaseTest {
    protected ISet<String> makeBasicSet() {
        ISet<String> set = new ChainedHashSet<>();
        set.add("keyA");
        set.add("keyB");
        set.add("keyC");
        return set;
    }

    protected <T> void assertSetMatches(T[] expectedItems, ISet<T> actual) {
        assertEquals(expectedItems.length, actual.size());
        assertEquals(expectedItems.length == 0, actual.isEmpty());

        for (int i = 0; i < expectedItems.length; i++) {
            T key = expectedItems[i];
            assertTrue(actual.contains(key));
        }
    }

    @Test(timeout=SECOND)
    public void testAddAndRemoveBasic() {
        ISet<String> set = this.makeBasicSet();
        this.assertSetMatches(new String[] {"keyA", "keyB", "keyC"}, set);
    }

    @Test(timeout=SECOND)
    public void testPutAndGetSameKeyRepeated() {
        ISet<Integer> set = new ChainedHashSet<>();

        // First insertion
        set.add(3);
        this.assertSetMatches(new Integer[] {3}, set);

        // Second insertion
        set.add(3);
        this.assertSetMatches(new Integer[] {3}, set);

        // Third insertion
        set.add(3);
        this.assertSetMatches(new Integer[] {3}, set);
    }

    @Test(timeout=SECOND)
    public void testPutAndGetSameKeyRepeatedMany() {
        ISet<String> set = new ChainedHashSet<>();
        set.add("a");
        set.add("b");
        set.add("a");
        set.add("a");
        set.add("c");
        set.add("a");
        set.add("c");

        this.assertSetMatches(new String[] {"a", "b", "c"}, set);
    }

    @Test(timeout=SECOND)
    public void testContains() {
        ISet<String> set = new ChainedHashSet<>();

        assertFalse(set.contains("foo"));

        set.add("foo");
        set.add("bar");

        assertFalse(set.contains("qux"));
    }

    @Test(timeout=10 * SECOND)
    public void testAddAndCheckMany() {
        ISet<Integer> set = new ChainedHashSet<>();
        int cap = 10000;

        for (int i = 0; i < cap; i++) {
            set.add(i);
        }

        for (int i = cap - 1; i >= 0; i--) {
            assertTrue(set.contains(i));
            if (i != 0) {
                assertFalse(set.contains(-i));
            }
        }

        assertEquals(cap, set.size());
        assertFalse(set.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testRemoveBasic() {
        ISet<String> set = this.makeBasicSet();

        set.remove("keyB");
        this.assertSetMatches(new String[] {"keyA", "keyC"}, set);

        set.remove("keyA");
        this.assertSetMatches(new String[] {"keyC"}, set);

        set.remove("keyC");
        this.assertSetMatches(new String[] {}, set);
    }

    @Test(timeout=SECOND)
    public void testRemoveDuplicate() {
        ISet<String> set = new ChainedHashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("a");
        set.add("d");
        set.add("b");

        this.assertSetMatches(new String[] {"a", "b", "c", "d"}, set);

        set.remove("a");
        this.assertSetMatches(new String[] {"b", "c", "d"}, set);

        set.remove("b");
        this.assertSetMatches(new String[] {"c", "d"}, set);
    }

    @Test(timeout=SECOND)
    public void testRemoveErrorHandling() {
        ISet<Integer> list = new ChainedHashSet<>();
        list.add(3);

        try {
            list.remove(4);
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // Do nothing: this is ok
        }

        list.remove(3);

        try {
            list.remove(3);
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testContainsWithRemovesBasic() {
        ISet<String> set = new ChainedHashSet<>();

        set.add("a");
        set.add("b");
        set.add("c");
        set.add("a");
        set.remove("c");
        set.add("c");
        set.add("d");
        set.add("a");
        set.remove("c");

        assertTrue(set.contains("a"));
        assertTrue(set.contains("b"));
        assertFalse(set.contains("c"));
        assertTrue(set.contains("d"));
        assertFalse(set.contains("e"));
    }

    @Test(timeout=SECOND)
    public void testLargeKeys() {
        // Force keys to be two separate objects
        String key1 = "abcdefghijklmnopqrstuvwxyz";
        String key2 = key1 + "";

        ISet<String> set = new ChainedHashSet<>();
        set.add(key1);

        assertTrue(set.contains(key1));
        assertTrue(set.contains(key2));

        set.remove(key2);

        assertFalse(set.contains(key1));
        assertFalse(set.contains(key2));
    }

    @Test(timeout=SECOND)
    public void testNullKey() {
        ISet<String> set = this.makeBasicSet();

        set.add(null);

        assertEquals(4, set.size());
        assertTrue(set.contains(null));

        set.remove(null);
        assertFalse(set.contains(null));
    }

    @Test(timeout=SECOND)
    public void testContainsMany() {
        ISet<String> set = this.makeBasicSet();
        int cap = 100000;

        for (int i = 0; i < cap; i++) {
            set.add("keyC");
        }

        for (int i = 0; i < cap; i++) {
            assertTrue(set.contains("keyC"));
        }
    }

    @Test(timeout=SECOND)
    public void testIterator() {
        ISet<String> set = new ChainedHashSet<>();
        ISet<String> copy = new ChainedHashSet<>();
        for (int i = 0; i < 1000; i++) {
            set.add("" + i);
            copy.add("" + i);
        }

        for (String key : set) {
            copy.remove(key);
        }

        assertTrue(copy.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testIteratorUnusualKeys() {
        ISet<String> map = new ChainedHashSet<>();

        map.add(null);
        map.add("");

        boolean metNullKey = false;
        boolean metEmptyKey = false;
        int numItems = 0;
        for (String key : map) {
            if (key == null) {
                metNullKey = true;
            } else if (key.equals("")) {
                metEmptyKey = true;
            }

            numItems += 1;
        }

        assertEquals(2, numItems);
        assertTrue(metNullKey);
        assertTrue(metEmptyKey);
    }

    @Test(timeout=SECOND)
    public void testIteratorEndsCorrectly() {
        ISet<String> set = this.makeBasicSet();

        Iterator<String> iter = set.iterator();

        for (int i = 0; i < set.size(); i++) {
            for (int j = 0; j < 1000; j++) {
                assertTrue(iter.hasNext());
            }
            iter.next();
        }

        for (int j = 0; j < 1000; j++) {
            assertFalse(iter.hasNext());
        }

        try {
            iter.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // This is ok; deliberately empty
        }
    }

    @Test(timeout=SECOND)
    public void testIteratorsAreIndependent() {
        ISet<String> set = makeBasicSet();
        Iterator<String> iter1 = set.iterator();
        Iterator<String> iter2 = set.iterator();

        for (int i = 0; i < set.size(); i++) {
            assertEquals(iter1.hasNext(), iter2.hasNext());
            assertEquals(iter1.next(), iter2.next());
        }

        assertFalse(iter1.hasNext());
        assertFalse(iter2.hasNext());

        set.add("nextKey1");
        set.add("nextKey2");

        assertEquals(5, set.size());
        Iterator<String> iter3 = set.iterator();
        Iterator<String> iter4 = set.iterator();

        for (int i = 0; i < set.size(); i++) {
            assertEquals(iter3.hasNext(), iter4.hasNext());
            assertEquals(iter3.next(), iter4.next());
        }

        assertFalse(iter3.hasNext());
        assertFalse(iter4.hasNext());
    }

    @Test(timeout=SECOND)
    public void testManyObjectsWithSameHashCode() {
        ISet<Wrapper<String>> set = new ChainedHashSet<>();
        for (int i = 0; i < 1000; i++) {
            set.add(new Wrapper<>("" + i, 0));
        }

        assertEquals(1000, set.size());

        for (int i = 999; i >= 0; i--) {
            String key = "" + i;
            assertFalse(set.contains(new Wrapper<>(key + "a", 0)));
        }

        Wrapper<String> key1 = new Wrapper<>("abc", 0);
        Wrapper<String> key2 = new Wrapper<>("cde", 0);

        set.add(key1);
        set.add(key2);

        assertEquals(1002, set.size());
    }

    @Test(timeout=SECOND)
    public void testNegativeHashCode() {
        ISet<Wrapper<String>> set = new ChainedHashSet<>();

        Wrapper<String> key1 = new Wrapper<>("foo", -1);
        Wrapper<String> key2 = new Wrapper<>("bar", -100000);
        Wrapper<String> key3 = new Wrapper<>("baz", 1);
        Wrapper<String> key4 = new Wrapper<>("qux", -4);

        set.add(key1);
        set.add(key2);
        set.add(key3);

        assertTrue(set.contains(key1));
        assertTrue(set.contains(key2));
        assertTrue(set.contains(key3));
        assertFalse(set.contains(key4));

        set.remove(key1);
        assertFalse(set.contains(key1));
    }

    @Test(timeout=10*SECOND)
    public void stressTest() {
        int limit = 1000000;
        ISet<Integer> set = new ChainedHashSet<>();

        for (int i = 0; i < limit; i++) {
            set.add(i);
            assertTrue(set.contains(i));
        }

        for (int i = 0; i < limit; i++) {
            assertFalse(set.contains(-1));
        }

        for (int i = 0; i < limit; i++) {
            set.add(i);
        }

        for (int i = 0; i < limit; i++) {
            set.remove(i);
            assertFalse(set.contains(i));
        }
    }
}
