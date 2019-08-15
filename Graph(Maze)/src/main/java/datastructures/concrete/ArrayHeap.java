package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;


/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private IDictionary<T, Integer> dictionary;
    int size;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.dictionary = new ChainedHashDictionary<>();
        this.heap = makeArrayOfT(1);
        this.size= 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * upwards from a given index, if necessary.
     */
    private void percolateUp(int index) {
        int parent =  (index-1)/NUM_CHILDREN;
        if (parent<0) {return; }
        if (heap[index].compareTo(heap[parent])<0){
            swap(index, parent);
            percolateUp(parent);
        }
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * downwards from a given index, if necessary.
     */
    private void percolateDown(int index) {
        T min= heap[index];
        int minIdx = index;
        for (int i=1; i<=NUM_CHILDREN; i++){
            int tmp = NUM_CHILDREN*index+i;
            if (tmp>=size){
                break;
            }
            if (heap[tmp].compareTo(min)<0){
                minIdx = tmp;
                min = heap[minIdx];
            }
        }
        if (heap[minIdx].compareTo(heap[index])<0) {
            swap(index, minIdx);
            percolateDown(minIdx);
        }
        else {
            return;
        }
    }

    /**
     * A method stub that you may replace with a helper method for determining
     * which direction an index needs to percolate and percolating accordingly.
     */
    private void percolate(int index) {
        if (index==0){
            percolateDown(0);
        }
        else {
            int parent = (index-1)/NUM_CHILDREN;
            if (parent<0) {
                percolateDown(index);
            }
            else if (heap[index].compareTo(heap[parent])<0){
                percolateUp(index);
            }
            else {
                percolateDown(index);
            }
        }
    }

    /**
     * A method stub that you may replace with a helper method for swapping
     * the elements at two indices in the 'heap' array.
     */
    private void swap(int a, int b) {
        dictionary.put(heap[a], b);
        dictionary.put(heap[b], a);
        T tmp = heap[a];
        heap[a] = heap[b];
        heap[b] = tmp;

    }

    @Override
    public T removeMin() {
        if (this.size == 0){
            throw new EmptyContainerException();
        }
        T min = heap[0];
        if (size!=1){
            heap[0] = heap[size-1];
            size--;
            dictionary.remove(min);
            percolateDown(0);
        }
        else {
            size--;
            dictionary.remove(min);
        }
        return min;
    }

    @Override
    public T peekMin() {
        if (this.size == 0){
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void add(T item) {
        if (item==null){
            throw new IllegalArgumentException();
        }
        if (this.contains(item)){
            throw new InvalidElementException();
        }
        // resizing
        if (this.size==this.heap.length) {
            int newsize = this.size * 2;
            T[] newarray = this.makeArrayOfT(newsize);
            System.arraycopy(this.heap, 0, newarray, 0, this.size);
            this.heap = newarray;
        }
        heap[size] = item;
        dictionary.put(item, size);
        percolateUp(size);
        this.size++;

    }

    @Override
    public boolean contains(T item) {
        if (item == null){
            throw new IllegalArgumentException();
        }
        return dictionary.containsKey(item);
    }

    @Override
    public void remove(T item) {
        if (item==null){
            throw new IllegalArgumentException();
        }
        if (!this.contains(item)){
            throw new InvalidElementException();
        }
        int index=dictionary.get(item);
        heap[index]=heap[size-1];
        dictionary.remove(item);
        percolate(index);
        this.size--;
        return;
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (newItem == null || oldItem == null){
            throw new IllegalArgumentException();
        }
        if (!dictionary.containsKey(oldItem)){
            throw new InvalidElementException();
        }
        if (dictionary.containsKey(newItem)){
            throw new InvalidElementException();
        }
        int index = dictionary.remove(oldItem);
        heap[index] = newItem;
        dictionary.put(newItem, index);
        percolate(index);
    }

    @Override
    public int size() {
        return this.size;
    }
}
