package misc;

import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;


public class Sorter {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "largest".
     *
     * If the input list contains fewer than 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     * @throws IllegalArgumentException  if input is null
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        // Implementation notes:
        //
        // - This static method is a _generic method_. A generic method is similar to
        //   the generic methods we covered in class, except that the generic parameter
        //   is used only within this method.
        //
        //   You can implement a generic method in basically the same way you implement
        //   generic classes: just use the 'T' generic type as if it were a regular type.
        //
        // - You should implement this method by using your ArrayHeap for the sake of
        //   efficiency.
        IList<T> result = new DoubleLinkedList<>();
        IPriorityQueue<T> heap = new ArrayHeap<>();
        if (k<0||input==null){
            throw new IllegalArgumentException();
        }
        int idx =0;
        int resultsize;
        if (k>input.size()){
            resultsize = input.size();
        }else {
            resultsize = k;
        }
        if (k==0||input.size()==0){
            return result;
        }
        for (T item:input){
            if (idx<resultsize){
                heap.add(item);
                idx++;
            }
            else {
                if (item.compareTo(heap.peekMin())>0){
                    heap.removeMin();
                    heap.add(item);
                }
            }
        }
        while (heap.size()>0){
            result.add(heap.removeMin());
        }
        return result;
    }
}
