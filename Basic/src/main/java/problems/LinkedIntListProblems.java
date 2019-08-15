package problems;

import datastructures.LinkedIntList;
//import misc.exceptions.NotYetImplementedException;

// IntelliJ will complain that this is an unused import, but you should use ListNode variables
// in your solution, and then this error should go away.

import datastructures.LinkedIntList.ListNode;

/**
 * Parts b.iii, b.iv, and b.v should go here.
 *
 * (Implement reverse3, firstLast, and shift as described by the spec
 *  See the spec on the website for picture examples and more explanation!)
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not construct new ListNode objects (though you may have as many ListNode variables as you like).
 * - do not call any LinkedIntList methods
 * - do not construct any external data structures like arrays, queues, lists, etc.
 * - do not mutate the .data field of any nodes, change the list only by modifying links between nodes.
 * - Your solution should run in linear time with respect to the number of elements of the linked list.
 */

public class LinkedIntListProblems {

    // Reverses the 3 elements in the LinkedIntList (assume there are only 3 elements).
    public static void reverse3(LinkedIntList list) {

        ListNode pre = null;
        while (list.front.next!=null){
            ListNode tmp = list.front.next;
            list.front.next = pre;
            pre = list.front;
            list.front = tmp;
        }
        list.front.next=pre;
    }

    public static void shift(LinkedIntList list) {
        if (list.front==null) {return; }
        ListNode odd = list.front;
        ListNode even = list.front.next;
        ListNode connect = even;

        while (1==1)
        {
            if (odd==null||even==null||even.next==null)
            {
                odd.next=connect;
                break;
            }
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;

        }

    }

    public static void firstLast(LinkedIntList list) {
        ListNode tail = list.front;
        list.front = list.front.next;
        ListNode cur = list.front;
        while (cur.next!=null){ cur=cur.next; }
        cur.next = tail;
        cur.next.next = null;

    }
}
