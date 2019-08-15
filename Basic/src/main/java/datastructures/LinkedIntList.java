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
 * This class represents a Linked List of int data.  Each piece of data is represented as an
 * ListNode object that stores some int data (.data), as well as the ListNode object that
 * comes after in the list order (.next).
 *
 * Note that this data structure is not quite useful in its current state. There are a couple
 * of useful methods (and it's actually more useful than IntTree), but it could use a lot more
 * to meet the functionality of the official Java LinkedList.
 *
 * Don't worry though - we'll implement a more speed-efficient and more useful LinkedList
 * very soon! Stay tuned.
 */

public class LinkedIntList {

    public ListNode front;

    LinkedIntList() { }

    LinkedIntList(int[] array) {
        // this is inefficient
        // for (int n : array) {
        //    this.add(n);
        // }
        if (array.length != 0) {
            front = new ListNode(array[0]);
            ListNode temp = front;
            for (int i = 1; i < array.length; i++) {
                temp.next = new ListNode(array[i]);
                temp = temp.next;
            }
        }
    }

    // for testing

    // Returns a comma-separated String representation of this list.
    public String toString() {
        if (front == null) {
            return "[]";
        } else {
            Set<ListNode> seen = new HashSet<>();
            String result = "[" + front.data;
            ListNode current = front.next;
            while (current != null) {
                if (!seen.contains(current)) {
                    result += ", " + current.data;
                    seen.add(current);
                    current = current.next;
                } else {
                    return result + ", " + current.data + " CYCLE DETECTED HERE ...";
                }
            }
            result += "]";
            return result;
        }
    }

    int get(int index) {
        ListNode current = front;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    int size() {
        ListNode current = front;
        int count = 0;
        Set<ListNode> seen = new HashSet<>();
        while (current != null) {
            assert !seen.contains(current) : "cycle detected when calling size()";
            seen.add(current);
            current = current.next;
            count++;
        }
        return count;
    }

    boolean isEmpty() {
        return size() == 0;
    }


    public static class ListNode {
        public final int data;
        public ListNode next;

        ListNode(int data) {
            this.data = data;
            this.next = null;
        }

        ListNode(int data, ListNode next) {
            this.data = data;
            this.next = next;
        }
    }
}
