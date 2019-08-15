package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * AVL Dictionary implementation of the Dictionary ADT.
 *
 * Does not allow null keys.
 */
public class AVLDictionary<K extends Comparable<K>, V> implements IDictionary<K, V> {
    private AVLNode<K, V> overallRoot;
    private int size;

    public AVLDictionary() {
        this.size = 0;
    }

    /**
     * Returns the value corresponding to a given key.
     *
     * @throws NoSuchKeyException if the key is not found.
     * @throws IllegalArgumentException if the given key is null.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        AVLNode<K, V> node = get(key, this.overallRoot);
        if (node == null) {
            throw new NoSuchKeyException();
        }
        return node.value;
    }

    /**
     * Returns the node with the given key or null if no such node is found.
     */
    private AVLNode<K, V> get(K key, AVLNode<K, V> root) {
        if (root == null) {
            return null;
        } else if (key.compareTo(root.key) < 0) {
            return get(key, root.left);
        } else if (key.compareTo(root.key) > 0) {
            return get(key, root.right);
        }
        return root;
    }

    /**
     * Returns the value corresponding to the given key or defaultValue if the key is not found.
     */
    @Override
    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value == null ? defaultValue : value;
    }

    /**
     * Adds the given key-value pair into the dictionary. Overwrites value if key already exists.
     *
     * @throws IllegalArgumentException if the given key is null.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        this.overallRoot = put(key, value, this.overallRoot);
    }

    /**
     * Adds the given key-value pair into the dictionary.  Overwrites value if key already exists.
     */
    private AVLNode<K, V> put(K key, V value, AVLNode<K, V> root) {
        if (root == null) {
            this.size++;
            return new AVLNode<>(key, value);
        }

        if (key.compareTo(root.key) < 0) {
            root.left = put(key, value, root.left);
        } else if (key.compareTo(root.key) > 0) {
            root.right = put(key, value, root.right);
        } else {
            root.value = value;
            return root;
        }
        updateHeight(root);
        return balanceTree(root);
    }

    /**
     * Maintains AVL balance invariant. Returns the balanced subtree.
     */
    private AVLNode<K, V> balanceTree(AVLNode<K, V> root) {
        int heightDiff = getHeightDiff(root);
        if (heightDiff > 1) {  // left-heavy, do right rotation
            if (getHeightDiff(root.left) < 0) {  // kink case, do left-right rotation
                root.left = rotateLeft(root.left);
            }
            root = rotateRight(root);
        } else if (heightDiff < -1) {  // right-heavy, do left rotation
            if (getHeightDiff(root.right) > 0) {  // kink case, do right-left rotation
                root.right = rotateRight(root.right);
            }
            root = rotateLeft(root);
        }
        return root;
    }

    /**
     * Returns the difference in heights of the left and right subtrees of the given node.
     */
    private int getHeightDiff(AVLNode<K, V> node) {
        return getHeight(node.left) - getHeight(node.right);
    }

    /**
     * Sets the given node's height to the maximum of its subtrees' heights plus 1.
     */
    private void updateHeight(AVLNode<K, V> node) {
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    /**
     * Returns the height of the given node's subtree. Note: Height of an empty tree is -1
     * and the height of a tree with a single node is 0.
     */
    private int getHeight(AVLNode<K, V> node) {
        return node == null ? -1 : node.height;
    }

    /**
     * Performs a right rotation on the given subtree. Returns the rotated subtree.
     */
    private AVLNode<K, V> rotateRight(AVLNode<K, V> root) {
        AVLNode<K, V> leftChild = root.left;
        root.left = leftChild.right;
        leftChild.right = root;
        updateHeight(leftChild);
        updateHeight(root);
        return leftChild;
    }

    /**
     * Performs a left rotation on the given subtree. Returns the rotated subtree.
     */
    private AVLNode<K, V> rotateLeft(AVLNode<K, V> root) {
        AVLNode<K, V> rightChild = root.right;
        root.right = rightChild.left;
        rightChild.left = root;
        updateHeight(rightChild);
        updateHeight(root);
        return rightChild;
    }

    /**
     * Remove has been left unimplemented.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     */
    @Override
    public boolean containsKey(K key) {
        return get(key, this.overallRoot) != null;
    }

    /**
     * Returns the number of key-value pairs stored in this dictionary.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns 'true' if this dictionary is empty and 'false' otherwise.
     */
    @Override
    public boolean isEmpty() {
        return overallRoot == null;
    }

    /**
     * Returns an iterator that, when used, will yield all key-value pairs contained within
     * this dictionary.
     */
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new AVLIterator<>(this.overallRoot);
    }

    /**
     * AVLNode class. Nodes store a key and a value and have at most two children. Each node
     * keeps track of its own height in the AVL Tree. This is used to balance the tree.
     */
    private static class AVLNode<K, V> {
        public AVLNode<K, V> left;
        public AVLNode<K, V> right;
        public K key;
        public V value;
        public int height;

        public AVLNode(K key, V value) {
            left = null;
            right = null;
            this.key = key;
            this.value = value;
            this.height = 0;
        }
    }

    /**
     * AVLIterator Class. Creates an iterator over the key-value pairs stored in the AVL Dictionary.
     */
    private static class AVLIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Stack<KVPair<K, V>> stack;

        public AVLIterator(AVLNode<K, V> overallRoot) {
            this.stack = new Stack<>();
            reverseOrderFill(overallRoot);
        }

        private void reverseOrderFill(AVLNode<K, V> root) {
            if (root == null) {
                return;
            }
            reverseOrderFill(root.right);
            this.stack.push(new KVPair<>(root.key, root.value));
            reverseOrderFill(root.left);
        }

        @Override
        public boolean hasNext() {
            return this.stack.size() > 0;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return this.stack.pop();
        }
    }
}
