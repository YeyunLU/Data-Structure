package datastructures.interfaces;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a data structure that contains a unique collection of items.
 */
public interface ISet<T> extends Iterable<T> {
    /**
     * Adds the given item to the set.
     *
     * If the item already exists in the set, this method does nothing.
     */
    public void add(T item);

    /**
     * Removes the given item from the set.
     *
     * @throws NoSuchElementException  if the set does not contain the given item
     */
    public void remove(T item);

    /**
     * Returns 'true' if the set contains this item and false otherwise.
     */
    public boolean contains(T item);

    /**
     * Returns the number of items contained within this set.
     */
    public int size();

    /**
     * Returns true if this set contains no items and false otherwise.
     */
    public default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns all items contained within this set.
     */
    public Iterator<T> iterator();
}
