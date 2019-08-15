package datastructures;

import misc.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import problems.LinkedIntListProblems;
import datastructures.LinkedIntList.ListNode;

import java.util.Arrays;

import static org.junit.Assert.fail;

/**
 * WARNING: These tests are what your grade for Homework 1 is based on.
 * You don't need to change these tests, but it may be useful to add
 * extra printing / debugging information.
 *
 * If you do modify this file (the existing tests in particular),
 * take care to remember that you did so and revert it back.
 *
 * The reason for this is that if you make changes that change the
 * outcomes of the tests below, you might be tricking yourself into
 * thinking your code is more correct or less correct than it actually
 * is.
 *
 * So please be careful to revert things back when you run the tests
 * to check your work.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLinkedIntListProblems extends BaseTest {

    /**
     * This test will check if a LinkedIntList contains exactly the same elements as
     * the "expected" array. See the tests provided for example usage.
     */
    protected void assertListMatches(int[] expected, LinkedIntList actual) {
        String newLine = System.lineSeparator();
        String errorMessage = String.format("%s Expected list: %s %s %s Actual list: %s %s %s",
                newLine, newLine, Arrays.toString(expected),
                newLine, newLine, actual.toString(), newLine);

        if (errorMessage.contains("CYCLE")) {
            fail(errorMessage);
        }

        if (expected.length != actual.size() || ((expected.length == 0) != actual.isEmpty())) {
            fail(errorMessage);
        }

        for (int i = 0; i < expected.length; i++) {
            try {
                if (expected[i] != actual.get(i)) {
                    fail(errorMessage);
                }
            } catch (Exception ex) {
                 errorMessage += String.format(
                        "Unexpected exception - got %s when getting item at index %d (expected '%s')",
                        ex.getClass().getSimpleName(),
                        i,
                        expected[i]);
                fail(errorMessage);
            }
        }
    }

    /**
     * Note: We use 1 second as the default timeout for many of our tests.
     *
     * One second is typically extremely generous: most of your tests should
     * finish in milliseconds. If one of your tests is timing out, you're almost
     * certainly doing something wrong.
     */

    @Test(timeout=SECOND)
    public void testReverse3() {
        LinkedIntList list = new LinkedIntList(new int[]{3, 4, 5});
        ListNode first = list.front;
        ListNode second = list.front.next;
        ListNode third = list.front.next.next;
        LinkedIntListProblems.reverse3(list);
        assertEquals(third, list.front);
        assertEquals(second, list.front.next);
        assertEquals(first, list.front.next.next);
        assertEquals(null, list.front.next.next.next);
    }

    @Test(timeout=SECOND)
    public void testShiftEmpty() {
        LinkedIntList list = new LinkedIntList();
        LinkedIntListProblems.shift(list);
        assertListMatches(new int[0], list);
    }

    @Test(timeout=SECOND)
    public void testShift2Elements() {
        LinkedIntList list = new LinkedIntList(new int[]{0, 1});
        LinkedIntListProblems.shift(list);
        assertListMatches(new int[]{0, 1}, list);
    }

    @Test(timeout=SECOND)
    public void testShift3Elements() {
        LinkedIntList list = new LinkedIntList(new int[]{0, 1, 2});
        LinkedIntListProblems.shift(list);
        assertListMatches(new int[]{0, 2, 1}, list);
    }

    @Test(timeout=SECOND)
    public void testShiftConsecutiveDuplicates() {
        LinkedIntList list = new LinkedIntList(new int[]{3, 3, 3, 3, 4});
        LinkedIntListProblems.shift(list);
        assertListMatches(new int[]{3, 3, 4, 3, 3}, list);
    }

    @Test(timeout=SECOND)
    public void testShiftSorted() {
        LinkedIntList list = new LinkedIntList(new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        LinkedIntListProblems.shift(list);
        assertListMatches(new int[]{0, 2, 4, 6, 1, 3, 5, 7}, list);
    }

    @Test(timeout=SECOND)
    public void testShiftGeneralOddLength() {
        LinkedIntList list = new LinkedIntList(new int[]{4, 17, 29, 3, 8, 2, 28, 5, 7});
        LinkedIntListProblems.shift(list);
        assertListMatches(new int[]{4, 29, 8, 28, 7, 17, 3, 2, 5}, list);
    }

    @Test(timeout=SECOND)
    public void testFirstLastEmpty() {
        LinkedIntList list = new LinkedIntList();
        LinkedIntListProblems.shift(list);
        assertListMatches(new int[0], list);
    }

    @Test(timeout=SECOND)
    public void testFirstLast1Element() {
        LinkedIntList list = new LinkedIntList(new int[]{42});
        LinkedIntListProblems.shift(list);
        assertListMatches(new int[]{42}, list);
    }

    @Test(timeout=SECOND)
    public void testFirstLast2Elements() {
        LinkedIntList list = new LinkedIntList(new int[]{42, 99});
        LinkedIntListProblems.firstLast(list);
        assertListMatches(new int[]{99, 42}, list);
    }

    @Test(timeout=SECOND)
    public void testFirstLastGeneral() {
        LinkedIntList list = new LinkedIntList(new int[]{18, 4, 27, 9, 54, 5, 63});
        LinkedIntListProblems.firstLast(list);
        assertListMatches(new int[]{4, 27, 9, 54, 5, 63, 18}, list);
    }

    @Test(timeout=SECOND)
    public void testFirstLastGeneral2() {
        LinkedIntList list = new LinkedIntList(new int[]{18, 4, 27, 9, 54, 5, 63});
        LinkedIntListProblems.firstLast(list);
        assertListMatches(new int[]{4, 27, 9, 54, 5, 63, 18}, list);
    }

}
