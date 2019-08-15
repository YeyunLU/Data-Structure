package datastructures.interfaces;

import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;

/**
 * Represents a queue where the elements are ordered such that the
 * front element always has the least value, as defined by the
 * element's 'compareTo' method.
 *
 * Does not support duplicate elements (based on the 'equals' method).
 */
public interface IPriorityQueue<T extends Comparable<T>> {
    /**
     * Removes and return the smallest element in the queue.
     *
     * If two elements within the queue have equal values
     * according to the 'compareTo' method, this method may break
     * the tie arbitrarily and return either one.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    T removeMin();

    /**
     * Returns, but does not remove, the least-valued element in the queue.
     *
     * This method must break ties in the same way the removeMin
     * method breaks ties.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    T peekMin();

    /**
     * Adds the given item into the queue.
     *
     * @throws IllegalArgumentException  if the item is null
     * @throws InvalidElementException  if item is already in the queue
     */
    void add(T item);

    /**
     * Returns 'true' if this queue contains the given element, and 'false' otherwise.
     *
     * @throws IllegalArgumentException  if the item is null
     */
    boolean contains(T item);

    /**
     * Removes the given item from the queue, if present.
     *
     * @throws IllegalArgumentException  if the item is null
     * @throws InvalidElementException  if the item is not in the queue
     */
    void remove(T item);

    /**
     * Replaces an item in the queue with a new item.
     *
     * @throws InvalidElementException  if the old item is not in the queue or
     *                                  if new item is already in queue
     */
    void replace(T oldItem, T newItem);

    /**
     * Returns the number of elements contained within this queue.
     */
    int size();

    /**
     * Returns 'true' if this queue is empty, and 'false' otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }
}
