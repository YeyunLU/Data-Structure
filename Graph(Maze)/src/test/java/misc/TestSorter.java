package misc;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSorter extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
}
