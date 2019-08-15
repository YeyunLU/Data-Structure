package datastructures;

import misc.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import problems.IntTreeProblems;
import datastructures.IntTree.IntTreeNode;
import java.util.HashMap;
import java.util.Map;

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
public class TestIntTreeProblems extends BaseTest {

    /**
     * Note: We use 1 second as the default timeout for many of our tests.
     *
     * One second is typically extremely generous: most of your tests should
     * finish in milliseconds. If one of your tests is timing out, you're almost
     * certainly doing something wrong.
     */

    @Test(timeout=SECOND)
    public void testDepthSumEmptyTree() {
        IntTree tree = new IntTree(new Integer[0]);
        assertEquals(0, IntTreeProblems.depthSum(tree));
    }

    @Test(timeout=SECOND)
    public void testDepthSum1Element() {
        IntTree tree = new IntTree(new Integer[]{12});
        assertEquals(12, IntTreeProblems.depthSum(tree));

    }

    @Test(timeout=SECOND)
    public void testDepthSumHeight3() {
        IntTree tree = new IntTree(new Integer[]{9, 7, 6, 3, 2, null, 4, null, null,
                5, null, null, null, null, 2});
        assertEquals(90, IntTreeProblems.depthSum(tree));
    }

    @Test(timeout=SECOND)
    public void testDepthSumHeight2() {
        IntTree tree = new IntTree(new Integer[]{3, 5, 2, 1, null, 4, 6, });
        assertEquals(50, IntTreeProblems.depthSum(tree));
    }

    @Test(timeout=SECOND)
    public void testDepthSumNegatives() {
        IntTree tree = new IntTree(new Integer[]{19, 47, 63, 23, -2, null, 94, null,
                null, 55, null, null, null, null, 28});
        assertEquals(916, IntTreeProblems.depthSum(tree));
    }

    @Test//(timeout=SECOND)
    public void testDepthSumAllRightSide() {
        IntTree tree = new IntTree(new Integer[]{2, null, 1, null, null, 7, 6, null,
                null, null, null, 4, null, null, 9, null, null, null, null, null,
                null, null, null, 3, 5, null, null,  null, null,  8});
        assertEquals(175, IntTreeProblems.depthSum(tree));
    }

    @Test(timeout=SECOND)
    public void testDepthSumFull() {
        IntTree tree = new IntTree(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                12, 13, 14, 15});
        assertEquals(445, IntTreeProblems.depthSum(tree));
    }

    @Test(timeout=SECOND)
    public void testRemoveLeavesEmpty() {
        IntTree actual = new IntTree();
        IntTreeProblems.removeLeaves(actual);
        IntTree expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testRemoveLeaves1Element() {
        IntTree actual = new IntTree(new Integer[]{12});
        IntTreeProblems.removeLeaves(actual);
        IntTree expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testRemoveLeavesOnce() {
        IntTree actual = new IntTree(new Integer[]{7, 3, 9, 1, 4, 6, 8, null, null,
                null, null, null, null, null, 0});
        IntTreeProblems.removeLeaves(actual);
        IntTree expected = new IntTree(new Integer[]{7, 3, 9, null, null, null, 8});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testRemoveLeavesRepeatedly1() {
        IntTree actual = new IntTree(new Integer[]{7, 3, 9, 1, 4, 6, 8, null, null,
                null, null, null, null, null, 0});
        IntTreeProblems.removeLeaves(actual);
        IntTree expected = new IntTree(new Integer[]{7, 3, 9, null, null, null, 8});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{7, null, 9});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{7});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testRemoveLeavesRepeatedlySymmetrical() {
        IntTree actual1 = new IntTree(new Integer[]{6, 5, 4, null, null, null, 1,
                null, null, null, null, null, null, 3, 2});
        IntTreeProblems.removeLeaves(actual1);
        IntTree expected1 = new IntTree(new Integer[]{6, null, 4, null, null, null, 1});
        assertTreesMatch(expected1, actual1);
        IntTreeProblems.removeLeaves(actual1);
        expected1 = new IntTree(new Integer[]{6, null, 4});
        assertTreesMatch(expected1, actual1);
        IntTreeProblems.removeLeaves(actual1);
        expected1 = new IntTree(new Integer[]{6});
        assertTreesMatch(expected1, actual1);

        // testing the same structure tree as above but mirrored horizontally (some different values though)
        IntTree actual2 = new IntTree(new Integer[]{6, 5, 2, 4,  null, null, null, 1, 3});
        IntTreeProblems.removeLeaves(actual2);
        IntTree expected2 = new IntTree(new Integer[]{6, 5, null, 4});
        assertTreesMatch(expected2, actual2);
        IntTreeProblems.removeLeaves(actual2);
        expected2 = new IntTree(new Integer[]{6, 5});
        assertTreesMatch(expected2, actual2);
        IntTreeProblems.removeLeaves(actual2);
        expected2 = new IntTree(new Integer[]{6});
        assertTreesMatch(expected2, actual2);
    }

    @Test(timeout=SECOND)
    public void testRemoveLeavesLinkedListLeft() {
        IntTree actual = new IntTree(new Integer[]{4, 2, null, 3, null, null, null, 1});
        IntTreeProblems.removeLeaves(actual);
        IntTree expected = new IntTree(new Integer[]{4, 2, null, 3});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{4, 2});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{4});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testRemoveLeavesLinkedListRight() {
        IntTree actual = new IntTree(new Integer[]{2, null, 3, null, null, null, 4,
                null, null, null, null, null, null, null, 1});
        IntTreeProblems.removeLeaves(actual);
        IntTree expected = new IntTree(new Integer[]{2, null, 3, null, null, null, 4});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{2, null, 3});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{2});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testRemoveLeavesLinkedListZigZag() {
        IntTree actual = new IntTree(new Integer[]{6, 5, null, null, 4,  null, null,
                null, null, 3, null, null, null, null, null, /**/ null, null, null,
                null, null, 2, null, null, /**/ null, null, null, null, null, null,
                null, null, /**/  null, null, null, null, null, null, null, null,
                null, null, 1});
        IntTreeProblems.removeLeaves(actual);
        IntTree expected = new IntTree(new Integer[]{6, 5, null, null, 4,  null,
                null, null, null, 3, null, null, null, null, null, /**/ null, null,
                null, null, null, 2});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{6, 5, null, null, 4,  null, null, null, null, 3});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{6, 5, null, null, 4});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{6, 5});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{6});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testRemoveLeavesFull() {
        IntTree actual = new IntTree(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                12, 13, 14, 15});
        IntTreeProblems.removeLeaves(actual);
        IntTree expected = new IntTree(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{1, 2, 3});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree(new Integer[]{1});
        assertTreesMatch(expected, actual);
        IntTreeProblems.removeLeaves(actual);
        expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesEmpty() {
        IntTree actual = new IntTree();
        int result = IntTreeProblems.numberNodes(actual);
        IntTree expected = new IntTree();
        assertTreesMatch(expected, actual);
        assertEquals(result, 0);
    }

    @Test(timeout=SECOND)
    public void testNumberNodes1Element() {
        IntTree actual = new IntTree(new Integer[]{12});
        int result = IntTreeProblems.numberNodes(actual);
        IntTree expected = new IntTree(new Integer[]{1});
        assertTreesMatch(expected, actual);
        assertEquals(result, 1);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesGeneral() {
        IntTree actual = new IntTree(new Integer[]{7, 3, 9, 9, 2, null, 0});
        int result = IntTreeProblems.numberNodes(actual);
        IntTree expected = new IntTree(new Integer[]{1, 2, 5, 3, 4, null, 6});
        assertTreesMatch(expected, actual);
        assertEquals(result, 6);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesInputIsAnswer() {
        IntTree actual = new IntTree(new Integer[]{1, 2, 5, 3, 4, null, 6});
        int result = IntTreeProblems.numberNodes(actual);
        IntTree expected = new IntTree(new Integer[]{1, 2, 5, 3, 4, null, 6});
        assertTreesMatch(expected, actual);
        assertEquals(6, result);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesSameStructureDifferentValues() {
        IntTree expected = new IntTree(new Integer[]{1, 2, 5, 3, 4, null, 6});

        IntTree actual1 = new IntTree(new Integer[]{6, 5, 2, 4, 3, null, 1});
        int result1 = IntTreeProblems.numberNodes(actual1);
        assertTreesMatch(expected, actual1);
        assertEquals(6, result1);

        IntTree actual2 = new IntTree(new Integer[]{0, 9, 3, 2, 9, null, 7});
        int result2 = IntTreeProblems.numberNodes(actual2);
        assertTreesMatch(expected, actual2);
        assertEquals(6, result2);

        IntTree actual3 = new IntTree(new Integer[]{0, 0, 0, 0, 0, null, 0});
        int result3 = IntTreeProblems.numberNodes(actual3);
        assertTreesMatch(expected, actual3);
        assertEquals(6, result3);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesNegatives() {
        IntTree actual = new IntTree(new Integer[]{-6, -3, -12, -15, -3, null, null,
                -12, null, null, -6});
        int result = IntTreeProblems.numberNodes(actual);
        IntTree expected = new IntTree(new Integer[]{1, 2, 7, 3, 5, null, null, 4,
                null, null, 6});
        assertTreesMatch(expected, actual);
        assertEquals(7, result);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesFull() {
        IntTree actual1 = new IntTree(new Integer[]{1, 2, 2, 3, 3, 3, 3});
        int result1 = IntTreeProblems.numberNodes(actual1);
        IntTree expected1 = new IntTree(new Integer[]{1, 2, 5, 3, 4, 6, 7});
        assertTreesMatch(expected1, actual1);
        assertEquals(7, result1);

        IntTree actual2 = new IntTree(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        int result2 = IntTreeProblems.numberNodes(actual2);
        IntTree expected2 = new IntTree(new Integer[]{1, 2, 5, 3, 4, 6, 7});
        assertTreesMatch(expected2, actual2);
        assertEquals(7, result2);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesLinkedListLeft() {
        IntTree actual = new IntTree(new Integer[]{4, 2, null, 3, null, null, null, 1});
        int result = IntTreeProblems.numberNodes(actual);
        IntTree expected = new IntTree(new Integer[]{1, 2, null, 3, null, null, null, 4});
        assertTreesMatch(expected, actual);
        assertEquals(4, result);

    }

    @Test(timeout=SECOND)
    public void testNumberNodesLinkedListRight() {
        IntTree actual = new IntTree(new Integer[]{2, null, 3, null, null, null, 4,
                null, null, null, null, null, null, null, 1});
        int result = IntTreeProblems.numberNodes(actual);
        IntTree expected = new IntTree(new Integer[]{1, null, 2, null, null, null, 3,
                null, null, null, null, null, null, null, 4});
        assertTreesMatch(expected, actual);
        assertEquals(4, result);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesSymmetrical() {
        IntTree actual1 = new IntTree(new Integer[]{6, 5, 4, null, null, null, 1, null,
                null, null, null, null, null, 3, 2});
        int result1 = IntTreeProblems.numberNodes(actual1);
        IntTree expected1 = new IntTree(new Integer[]{1, 2, 3, null, null, null, 4, null,
                null, null, null, null, null, 5, 6});
        assertTreesMatch(expected1, actual1);
        assertEquals(6, result1);

        // testing the same structure tree as above but mirrored horizontally (some different values though)
        IntTree actual2 = new IntTree(new Integer[]{6, 5, 2, 4,  null, null, null, 1, 3});
        int result2 = IntTreeProblems.numberNodes(actual2);
        IntTree expected2 = new IntTree(new Integer[]{1, 2, 6, 3,  null, null, null, 4, 5});
        assertTreesMatch(expected2, actual2);
        assertEquals(6, result2);
    }

    @Test(timeout=SECOND)
    public void testNumberNodesLinkedListZigZag() {
        IntTree actual = new IntTree(new Integer[]{6, 5, null, null, 4,  null, null,
                null, null, 3, null, null, null, null, null, /**/ null, null, null,
                null, null, 2, null, null, /**/ null, null, null, null, null, null,
                null, null, /**/  null, null, null, null, null, null, null, null, null,
                null, 1});
        int result = IntTreeProblems.numberNodes(actual);
        IntTree expected = new IntTree(new Integer[]{1, 2, null, null, 3,  null, null,
                null, null, 4, null, null, null, null, null, /**/ null, null, null,
                null, null, 5, null, null, /**/ null, null, null, null, null, null,
                null, null, /**/  null, null, null, null, null, null, null, null,
                null, null, 6});
        assertTreesMatch(expected, actual);
        assertEquals(6, result);
    }

    @Test(timeout=SECOND)
    public void testTightenEmpty() {
        IntTree actual = new IntTree();
        IntTreeProblems.tighten(actual);
        IntTree expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTighten1Element() {
        IntTree actual = new IntTree(new Integer[]{12});
        IntTreeProblems.tighten(actual);
        IntTree expected = new IntTree(new Integer[]{12});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTightenResultFull() {
        IntTree actual = new IntTree(new Integer[]{2, 8, 9, 7, null,  6, null, 4, 1,
                null, null, null, 0, null, null, /* */ null, null, null, 3, null,
                null, null, null, null, null, 4, 5});
        IntTreeProblems.tighten(actual);
        IntTree expected = new IntTree(new Integer[]{2, 7, 0, 4, 3, 4, 5});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTightenLinkedListLeft() {
        IntTree actual = new IntTree(new Integer[]{1, 2, null, 3, null, null, null, 4});
        IntTreeProblems.tighten(actual);
        IntTree expected = new IntTree(new Integer[]{4});
        assertTreesMatch(expected, actual);

    }

    @Test(timeout=SECOND)
    public void testTightenLinkedListRight() {
        IntTree actual = new IntTree(new Integer[]{1, null, 2, null, null, null, 3,
                null, null, null, null, null, null, null, 4});
        IntTreeProblems.tighten(actual);
        IntTree expected = new IntTree(new Integer[]{4});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTightenSymmetrical() {
        IntTree actual1 = new IntTree(new Integer[]{1, 2, 3, null, null, null, 4,
                null, null, null, null, null, null, 5, 6});
        IntTreeProblems.tighten(actual1);
        IntTree expected1 = new IntTree(new Integer[]{1, 2, 4, null, null, 5, 6});
        assertTreesMatch(expected1, actual1);

        // testing the same structure tree as above but mirrored horizontally (some different values though)
        IntTree actual2 = new IntTree(new Integer[]{1, 2, 3, 4,  null, null, null, 5, 6});
        IntTreeProblems.tighten(actual2);
        IntTree expected2 = new IntTree(new Integer[]{1, 4, 3, 5, 6});
        assertTreesMatch(expected2, actual2);
    }

    @Test(timeout=SECOND)
    public void testTightenLinkedListZigZag() {
        IntTree actual = new IntTree(new Integer[]{1, 2, null, null, 3,  null, null,
                null, null, 4, null, null, null, null, null, /**/ null, null, null,
                null, null, 5, null, null, /**/ null, null, null, null, null, null,
                null, null, /**/  null, null, null, null, null, null, null, null,
                null, null, 6});
        IntTreeProblems.tighten(actual);
        IntTree expected = new IntTree(new Integer[]{6});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTightenInputFull() {
        IntTree actual = new IntTree(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                12, 13, 14, 15});
        IntTreeProblems.tighten(actual);
        IntTree expected = new IntTree(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                12, 13, 14, 15});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTrimEmpty() {
        IntTree actual = new IntTree();
        IntTreeProblems.trim(actual, 40, 1300);
        IntTree expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTrimRemoveGeneral() {
        IntTree actual = new IntTree(new Integer[]{50, 38, 90, 14, 42, 54, null,
                8, 20, null, null, null, 72, null, null, /*newrow*/ null, null,
                null, 26, null, null, null, null, null, null, 61, 83});
        IntTreeProblems.trim(actual, 25, 75);
        IntTree expected = new IntTree(new Integer[]{50, 38, 54, 26, 42, null, 72,
                null, null, null, null, null, null, 61});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTrimMinBiggerThanRoot() {
        IntTree actual = new IntTree(new Integer[]{50, 38, 90, 14, 42, 54, null,
                8, 20, null, null, null, 72, null, null, /*newrow*/ null, null,
                null, 26, null, null, null, null, null, null, 61, 83});
        IntTreeProblems.trim(actual, 52, 65);
        IntTree expected = new IntTree(new Integer[]{54, null, 61});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTrimMaxSmallerThanRoot() {
        IntTree actual = new IntTree(new Integer[]{50, 38, 90, 14, 42, 54, null,
                8, 20, null, null, null, 72, null, null, /*newrow*/ null, null,
                null, 26, null, null, null, null, null, null, 61, 83});
        IntTreeProblems.trim(actual, 18, 42);
        IntTree expected = new IntTree(new Integer[]{38, 20, 42, null, 26});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTrimMaxLessThanValues() {
        IntTree actual = new IntTree(new Integer[]{50, 38, 90, 14, 42, 54, null,
                8, 20, null, null, null, 72, null, null, /*newrow*/ null, null,
                null, 26, null, null, null, null, null, null, 61, 83});
        IntTreeProblems.trim(actual, -90, -50);
        IntTree expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTrimMinGreaterThanValues() {
        IntTree actual = new IntTree(new Integer[]{50, 38, 90, 14, 42, 54, null,
                8, 20, null, null, null, 72, null, null, /*newrow*/ null, null,
                null, 26, null, null, null, null, null, null, 61, 83});
        IntTreeProblems.trim(actual, 120, 500);
        IntTree expected = new IntTree();
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTrimMinEqualsMax() {
        IntTree actual = new IntTree(new Integer[]{50, 38, 90, 14, 42, 54, null,
                8, 20, null, null, null, 72, null, null, /*newrow*/ null, null,
                null, 26, null, null, null, null, null, null, 61, 83});
        IntTreeProblems.trim(actual, 50, 50);
        IntTree expected = new IntTree(new Integer[]{50});
        assertTreesMatch(expected, actual);
    }

    @Test(timeout=SECOND)
    public void testTrimLargeRange() {
        IntTree actual = new IntTree(new Integer[]{50, 38, 90, 14, 42, 54, 1100,
                null, null, null, null, null, null, 99, 1500, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, 1250});
        IntTreeProblems.trim(actual, 40, 1300);
        IntTree expected = new IntTree(new Integer[]{50, 42, 90, null, null,
                54, 1100, null, null, null, null, null, null, 99, 1250});
        assertTreesMatch(expected, actual);
    }

    protected static void assertTreesMatch(IntTree expected, IntTree actual) {
        assertNoCycles(expected, actual);
        String newLine = System.lineSeparator();
        String errorMessage = String.format("%s Expected tree: %s %s %s Actual tree: %s %s %s",
                newLine, newLine, expected.toStringSideways(),
                newLine, newLine, actual.toStringSideways(), newLine);
        assertTreesMatch(expected.overallRoot, actual.overallRoot, errorMessage);
    }

    private static void assertNoCycles(IntTree expected, IntTree actual) {
        Map<IntTreeNode, Integer> seenLevels = new HashMap<>();
        assertNoCycles(actual.overallRoot, seenLevels, 1, expected, actual);
    }

    private static void assertNoCycles(IntTreeNode root, Map<IntTreeNode, Integer> seenLevels,
                                       int level, IntTree expected, IntTree actual) {
        if (root != null) {
            if (seenLevels.containsKey(root)) {
                String newLine = System.lineSeparator();
                String errorMessage = String.format("Found cycle - encountered the node with .data (%d) " +
                        "twice and stopped.", root.data);
                errorMessage += newLine + String.format("First found node on level %d " +
                        "and again on level %d (1-based indexing)", seenLevels.get(root), level);
                errorMessage += newLine + newLine + "Actual (going through cycle once):";
                errorMessage += newLine + actual.toStringSideways(level + 2);
                errorMessage += newLine + "Expected:";
                errorMessage += newLine + expected.toStringSideways();

                fail(errorMessage);
            } else {
                seenLevels.put(root, level);
                assertNoCycles(root.left, seenLevels, level + 1, expected, actual);
                assertNoCycles(root.right, seenLevels, level + 1, expected, actual);
            }
        }

    }

    private static void assertTreesMatch(IntTreeNode expected, IntTreeNode actual, String errorMessage) {
        if (expected == null && actual != null) {
            fail(errorMessage + "First error found: expected was null but actual was not null");
        } else if (expected != null && actual == null) {
            fail(errorMessage + "First error found: expected was null but actual was not null");
        } else if ((expected != null && actual != null)) {
            if (expected.data != actual.data) {
                fail(errorMessage + "First error found: .data not equal");
            }
            assertTreesMatch(expected.left, actual.left, errorMessage);
            assertTreesMatch(expected.right, actual.right, errorMessage);
        }
    }

}
