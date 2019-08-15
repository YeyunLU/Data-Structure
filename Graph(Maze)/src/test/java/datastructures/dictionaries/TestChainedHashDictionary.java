package datastructures.dictionaries;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestChainedHashDictionary extends BaseTestDictionary {
    protected <K, V> IDictionary<K, V> newDictionary() {
        return new ChainedHashDictionary<>();
    }

    @Test(timeout=SECOND)
    public void testContainsMaintainsStateForIterator() {
        IDictionary<String, String> map = this.makeBasicDictionary();

        char mid = 'N';
        for (char i = 'D'; i < mid; i++) {
            map.put("key" + i, "val" + i);
        }

        for (char i = mid; i < 'Z'; i++) {
            assertFalse(map.containsKey("key" + i));
        }

        boolean[] contained = new boolean[mid - 'A'];

        int baseSize = 3;
        for (KVPair<String, String> pair : map) {
            char key = pair.getKey().charAt(baseSize);
            char val = pair.getValue().charAt(baseSize);
            assertEquals(key, val);

            int letter = key - 'A';
            if (letter > contained.length) {
                Assert.fail("Invalid letter found: " + key);
            }

            if (contained[letter]) {
                Assert.fail("Duplicate letter found: " + key);
            }

            contained[letter] = true;
        }

        for (boolean found : contained) {
            assertTrue(found);
        }
    }

    @Test(timeout=SECOND)
    public void testBigHashCodesAndNull() {
        IDictionary<Wrapper<String>, Integer> map = this.newDictionary();
        int start = 10000;
        int values = 500;
        for (int i = start; i < start + values; i++) {
            map.put(new Wrapper<>("" + i, i), i);
        }

        for (int i = start; i < start + values; i++) {
            assertEquals(i, map.get(new Wrapper<>("" + i, i)));

            assertFalse(map.containsKey(new Wrapper<>("no", i)));
            assertFalse(map.containsKey(new Wrapper<>(null, i)));
        }

        for (int i = start; i < start + values; i++) {
            map.put(new Wrapper<>("" + i, i), i + values);
        }

        for (int i = start; i < start + values; i++) {
            assertEquals(i + values, map.get(new Wrapper<>("" + i, i)));
        }
        assertFalse(map.containsKey(null));
    }

    @Test(timeout=SECOND)
    public void testManyObjectsWithSameHashCode() {
        IDictionary<Wrapper<String>, Integer> map = this.newDictionary();
        for (int i = 0; i < 1000; i++) {
            map.put(new Wrapper<>("" + i, 0), i);
        }

        assertEquals(1000, map.size());

        for (int i = 999; i >= 0; i--) {
            String key = "" + i;
            assertEquals(i, map.get(new Wrapper<>(key, 0)));

            assertFalse(map.containsKey(new Wrapper<>(key + "a", 0)));
        }

        Wrapper<String> key1 = new Wrapper<>("abc", 0);
        Wrapper<String> key2 = new Wrapper<>("cde", 0);

        map.put(key1, -1);
        map.put(key2, -2);

        assertEquals(1002, map.size());
        assertEquals(-1, map.get(key1));
        assertEquals(-2, map.get(key2));
    }

    @Test(timeout=SECOND)
    public void testNegativeHashCode() {
        IDictionary<Wrapper<String>, String> dict = this.newDictionary();

        Wrapper<String> key1 = new Wrapper<>("foo", -1);
        Wrapper<String> key2 = new Wrapper<>("bar", -100000);
        Wrapper<String> key3 = new Wrapper<>("baz", 1);
        Wrapper<String> key4 = new Wrapper<>("qux", -4);

        dict.put(key1, "val1");
        dict.put(key2, "val2");
        dict.put(key3, "val3");

        assertTrue(dict.containsKey(key1));
        assertTrue(dict.containsKey(key2));
        assertTrue(dict.containsKey(key3));
        assertFalse(dict.containsKey(key4));

        assertEquals("val1", dict.get(key1));
        assertEquals("val2", dict.get(key2));
        assertEquals("val3", dict.get(key3));

        dict.remove(key1);
        assertFalse(dict.containsKey(key1));
    }

    @Test(timeout=10*SECOND)
    public void testStress() {
        int limit = 1000000;
        IDictionary<Integer, Integer> dict = this.newDictionary();

        for (int i = 0; i < limit; i++) {
            dict.put(i, i);
            assertEquals(i, dict.get(i));
        }

        for (int i = 0; i < limit; i++) {
            assertFalse(dict.containsKey(-1));
        }

        for (int i = 0; i < limit; i++) {
            dict.put(i, -i);
        }

        for (int i = 0; i < limit; i++) {
            assertEquals(-i, dict.get(i));
            dict.remove(i);
        }
    }

    @Test(timeout=SECOND)
    public void testPrivateFieldExample() {
        IDictionary<String, String> map = this.makeBasicDictionary();
        @SuppressWarnings("unchecked")
        IDictionary<String, String>[] chains = getField(map, "chains", IDictionary[].class);
        assertNotNull(chains);
    }
}
