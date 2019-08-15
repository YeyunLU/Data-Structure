package datastructures;

import java.util.HashSet;
import java.util.Set;

/**
 * WARNING: DO NOT MODIFY THIS FILE - we will be using this class as provided below when
 * we score your assignment (run the provided tests).
 *
 * If you modify it, you risk breaking our stuff in a not-fun way.
 */



/**
 * This class represents a binary tree that can store int data.  Specifically,
 * Each piece of data is represented as an IntTreeNode that can be connected to
 * its two potential 'children' IntTreeNodes, the .left and .right field of any
 * IntTreeNode.
 *
 * Note that this data structure is actually not that useful in its current state.
 * There are not that many useful methods.
 *
 * We will learn about more useful tree data structures later on in the course, and
 * understanding programming ideas with binary trees now will be helpful for
 * that future occasion.
 */

public class IntTree {

    public IntTreeNode overallRoot;

    IntTree() {
        overallRoot = null;
    }

    IntTree(Integer[] input) {
        overallRoot = insertLevelOrder(input, 0);
    }

    IntTreeNode insertLevelOrder(Integer[] arr, int i) {
        if (i < arr.length && arr[i] != null) {
            IntTreeNode root = new IntTreeNode(arr[i]);
            root.left = insertLevelOrder(arr, 2 * i + 1);
            root.right = insertLevelOrder(arr, 2 * i + 2);
            return root;
        }
        return null;
    }

    public String toString() {
        return toString(overallRoot, new HashSet<>());
    }

    private String toString(IntTreeNode root, Set<IntTreeNode> seen) {
        if (root == null) {
            return "empty";
        } else {
            if (seen.contains(root)) {
                return "!!cycle!!";
            }
            seen.add(root);
            if (root.left == null && root.right == null) {
                return "" + root.data;
            } else {
                return "(" + root.data + ", " +  toString(root.left, seen) + ", " + toString(root.right, seen) + ")";
            }
        }
    }

    int size() {
        return size(overallRoot, new HashSet<>());
    }

    private int size(IntTreeNode root, Set<IntTreeNode> seen) {
        if (root == null) {
            return 0;
        } else {
            assert !seen.contains(root) : "cycle detected when calling size()";
            seen.add(root);
            return 1 + size(root.left, seen) + size(root.right, seen);
        }
    }

    public String toStringSideways() {
        return toStringSideways(-1);
    }

    public String toStringSideways(int limit) {
        return toStringSideways(overallRoot, 0, limit, new HashSet<>());
    }

    // post: prints in reversed preorder the tree with given root, indenting
    //       each line to the given level
    private String toStringSideways(IntTreeNode root, int level, int limit, Set<IntTreeNode> seen) {
        String result = "";
        if ((limit == -1 || level < limit) && root != null) {
            if (seen.contains(root)) {
                return "!!cycle!!";
            }
            seen.add(root);
            result += toStringSideways(root.right, level + 1, limit, seen);
            for (int i = 0; i < level; i++) {
                result += "    ";
            }
            result += root.data + System.lineSeparator();
            result += toStringSideways(root.left, level + 1, limit, seen);
        }
        return result;
    }

    public static class IntTreeNode {
        public int data;
        public IntTreeNode left;
        public IntTreeNode right;

        IntTreeNode(int data) {
            this.data = data;
        }

        IntTreeNode(int data, IntTreeNode left, IntTreeNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }
}
