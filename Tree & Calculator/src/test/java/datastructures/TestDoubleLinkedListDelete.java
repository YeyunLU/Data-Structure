package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified. You should give your tests
 * with a timeout of 1 second.
 *
 * This test extends the BaseTestDoubleLinkedList class. This means that
 * you can use the helper methods defined within BaseTestDoubleLinkedList.
 * @see BaseTestDoubleLinkedList
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDoubleLinkedListDelete extends BaseTestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }

    @Test(timeout=SECOND)
    public void testDeleteMiddleElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.delete(1);
        this.assertListValidAndMatches(new Integer[] {1, 3}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteIndexOfAndDeleteMiddle() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.delete(1);
        assertEquals(-1, list.indexOf(2));
    }

    @Test(timeout=SECOND)
    public void testDeleteUpdatesSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.delete(1);
        assertEquals(2, list.size());
    }

    @Test(timeout=SECOND)
    public void testDeleteFrontElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.delete(0);
        this.assertListValidAndMatches(new Integer[] {2, 3}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteBackElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.delete(2);
        this.assertListValidAndMatches(new Integer[] {1, 2}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteDuplicates() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(1);
        list.add(1);
        list.delete(0);
        this.assertListValidAndMatches(new Integer[] {2, 3, 1, 1}, list);
        list.delete(2);
        this.assertListValidAndMatches(new Integer[] {2, 3, 1}, list);
        list.delete(2);
        this.assertListValidAndMatches(new Integer[] {2, 3}, list);
        assertEquals(-1, list.indexOf(1));
    }

    @Test(timeout=SECOND)
    public void testDeleteSingleElementList() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.delete(0);
        assertEquals(0, list.size());
    }

    @Test(timeout=SECOND)
    public void testDeleteOutOfBoundsThrowsException() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        try {
            list.delete(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testDeleteAtFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 500000;
        for (int i = 0; i < cap; i++) {
            list.insert(list.size(), i * 2);
        }
        for (int j = 0; j < cap; j++) {
            list.delete(0);
        }
        assertEquals(0, list.size());
    }

    @Test(timeout=SECOND)
    public void testDeleteNearFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 500000;
        for (int i = 0; i < cap; i++) {
            list.insert(0, i * 2);
        }
        for (int i = 0; i < cap-1; i++) {
            list.delete(1);
        }
        assertEquals(1, list.size());
    }

    @Test(timeout=SECOND)
    public void testDeleteAtEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 500000;
        for (int i = 0; i < cap; i++) {
            list.insert(list.size(), i * 2);
        }
        for (int j = 0; j < cap; j++) {
            list.delete(list.size()-1);
        }
        assertEquals(0, list.size());
    }

    @Test(timeout=SECOND)
    public void testDeleteNearEndIsEfficient(){
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 500000;
        for (int i = 0; i < cap; i++) {
            list.insert(list.size(), i * 2);
        }
        for (int j = 0; j < cap-5; j++) {
            list.delete(list.size()-6);
        }
        assertEquals(5, list.size());
    }
    
    // Above are some examples of provided assert methods from JUnit,
    // but in these tests you will also want to use a custom assert
    // we have provided you in BaseTestDoubleLinkedList called
    // assertListValidAndMatches. It will check many properties of
    // your DoubleLinkedList so you will want to use it frequently.
    // For usage examples, you can refer to TestDoubleLinkedList,
    // and refer to BaseTestDoubleLinkedList for the method comment.
}
