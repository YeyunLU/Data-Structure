package datastructures;

import misc.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import problems.MapProblems;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
public class TestMapProblems extends BaseTest {

    /**
     * Note: We use 1 second as the default timeout for many of our tests.
     *
     * One second is typically extremely generous: most of your tests should
     * finish in milliseconds. If one of your tests is timing out, you're almost
     * certainly doing something wrong.
     */

    @Test(timeout=SECOND)
    public void testIntersectGeneral1() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("Janet", 87);
        map1.put("Logan", 62);
        map1.put("Whitaker", 46);
        map1.put("Alyssa", 100);
        map1.put("Stefanie", 80);
        map1.put("Jeff", 88);
        map1.put("Kim", 52);
        map1.put("Sylvia", 95);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("Logan", 62);
        map2.put("Kim", 52);
        map2.put("Whitaker", 52);
        map2.put("Jeff", 88);
        map2.put("Stefanie", 80);
        map2.put("Brian", 60);
        map2.put("Lisa", 83);
        map2.put("Sylvia", 87);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("Logan", 62);
        expected.put("Stefanie", 80);
        expected.put("Jeff", 88);
        expected.put("Kim", 52);

        Map<String, Integer> actual = MapProblems.intersect(map1, map2);
        assertEquals(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testIntersectGeneral2() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);
        map1.put("c", 3);
        map1.put("d", 4);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("b", 2);
        map2.put("c", 5);
        map2.put("d", 4);
        map2.put("e", 4);
        map2.put("f", 1);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("b", 2);
        expected.put("d", 4);

        Map<String, Integer> actual = MapProblems.intersect(map1, map2);
        assertEquals(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testIntersectSameMaps() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);
        map1.put("c", 3);
        map1.put("d", 4);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("a", 1);
        map2.put("b", 2);
        map2.put("c", 3);
        map2.put("d", 4);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        expected.put("d", 4);

        Map<String, Integer> actual = MapProblems.intersect(map1, map2);
        assertEquals(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testIntersectMap2SupersetMap1() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);
        map1.put("c", 3);
        map1.put("d", 4);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("x", 0);
        map2.put("a", 1);
        map2.put("b", 2);
        map2.put("c", 3);
        map2.put("d", 4);
        map2.put("e", 5);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        expected.put("d", 4);

        Map<String, Integer> actual = MapProblems.intersect(map1, map2);
        assertEquals(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testIntersectMap1SupersetMap2() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);
        map1.put("c", 3);
        map1.put("d", 4);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("x", 0);
        map2.put("a", 1);
        map2.put("b", 2);
        map2.put("c", 3);
        map2.put("d", 4);
        map2.put("e", 5);

        Map<String, Integer> expected = new HashMap<>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        expected.put("d", 4);

        Map<String, Integer> actual = MapProblems.intersect(map2, map1);
        assertEquals(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testIntersectDisjointMaps() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);
        map1.put("c", 3);
        map1.put("d", 4);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("a", 5);
        map2.put("b", 6);
        map2.put("c", 7);
        map2.put("d", 8);

        Map<String, Integer> expected = new HashMap<>();

        Map<String, Integer> actual = MapProblems.intersect(map1, map2);
        assertEquals(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testIntersectEmptyMap2() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> expected = new HashMap<>();
        Map<String, Integer> actual = MapProblems.intersect(map1, map2);
        assertEquals(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testIntersectEmptyMap1() {
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("a", 1);
        Map<String, Integer> expected = new HashMap<>();
        Map<String, Integer> actual = MapProblems.intersect(map1, map2);
        assertEquals(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testContains3False() {
        List<String> input = Arrays.asList("to", "be", "or", "not", "to", "be", "hamlet");
        assertFalse(MapProblems.contains3(input));
    }

    @Test(timeout=SECOND)
    public void testContains3True() {
        List<String> input = Arrays.asList("to", "be", "or", "not", "to", "be", "to", "be", "hamlet");
        assertTrue(MapProblems.contains3(input));
    }

    @Test(timeout=SECOND)
    public void testContains3Empty() {
        List<String> input = new ArrayList<>();
        assertFalse(MapProblems.contains3(input));
    }

}
