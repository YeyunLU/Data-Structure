package problems;

import datastructures.IntTree;
//import misc.exceptions.NotYetImplementedException;

// IntelliJ will complain that this is an unused import, but you should use IntTreeNode variables
// in your solution, and then this error should go away.
import datastructures.IntTree.IntTreeNode;

/**
 * Parts b.vi, through b.x should go here.
 *
 * (Implement depthSum, numberNodes, removeLeaves, tighten, and trim as described by the spec.
 * See the spec on the website for picture examples and more explanation!)
 *
 * Also note: you may want to use private helper methods to help you solve these problems.
 * YOU MUST MAKE THE PRIVATE HELPER METHODS STATIC, or else your code will not compile.
 * This happens for reasons that aren't the focus of this assignment and are mostly skimmed over in 142
 * and 143.  If you want to know more you can ask on Piazza or at office hours.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not construct new IntTreeNode objects (though you may have as many IntTreeNode variables as you like).
 * - do not call any IntTree methods
 * - do not construct any external data structures like arrays, queues, lists, etc.
 * - do not mutate the .data field of any nodes (except for numberNodes),
 */

public class IntTreeProblems {

    static int sum = 0;
    static int idx = 0;

    public static int depthSum(IntTree tree) {
        sum = 0;
        IntTreeNode root = tree.overallRoot;
        if (root==null) {return 0; }
        travelTree(root, 1);
        return sum;
    }

    public static void travelTree(IntTreeNode root, int depth){
        if (root==null){
            return; }
        else {
            sum+=(depth*(root.data)); }
        travelTree(root.left, (depth+1));
        travelTree(root.right, (depth+1));
        return;
    }

    public static void removeLeaves(IntTree tree) {
        IntTreeNode root = tree.overallRoot;
        if (root==null || (root.left==null && root.right==null)) {
            tree.overallRoot = null;
            return;
        }
        travel(root);
        return;
    }

    public static boolean travel(IntTreeNode root){
        if (root==null) {return true; }
        if (root.left==null && root.right==null)
        {   return true; }
        if (travel(root.left))
        {
            root.left = null;
        }
        if (travel(root.right))
        {
            root.right = null;
        }
        return false;

    }

    public static int numberNodes(IntTree tree) {
        idx = 0;
        IntTreeNode root = tree.overallRoot;
        if (root==null) {return idx; }
        preOrder(root);
        return idx;
    }

    public static void preOrder(IntTreeNode root){
        if (root==null) {return; }
        idx+=1;
        root.data = idx;
        preOrder(root.left);
        preOrder(root.right);
        return;
    }

    public static void tighten(IntTree tree) {
        IntTreeNode root = tree.overallRoot;
        if (root==null || (root.left==null && root.right==null)) {return; }
        tree.overallRoot = cutOneChild(root);

        return;
    }

    public static IntTreeNode cutOneChild(IntTreeNode root){
        if (root==null || (root.left==null && root.right==null)) {return root; }
        IntTreeNode left = cutOneChild(root.left);
        IntTreeNode right = cutOneChild(root.right);
        if (left!=null&&right==null){
            return left;
        }
        else if (left==null&&right!=null){
            return right;
        }
        else {
            root.left = left;
            root.right = right;
            return root;
        }

    }

    public static void trim(IntTree tree, int min, int max) {
        tree.overallRoot = trimTree(tree.overallRoot, min, max);
        return;
    }

    public static IntTreeNode trimTree(IntTreeNode root, int min, int max){
        if (root==null) {return null; }
        IntTreeNode left = trimTree(root.left, min, max);
        IntTreeNode right = trimTree(root.right, min, max);
        if (root.data<min){
            root = right;
        }
        else if (root.data>max){
            root = left;
        }
        else {
            root.left = left;
            root.right = right;
        }
        return root;
    }
}
