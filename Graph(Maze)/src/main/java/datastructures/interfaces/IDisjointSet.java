package datastructures.interfaces;

/**
 * Represents a collection of non-overlapping (disjoint) sets.
 * The client may create new sets, or merge existing ones.
 */
public interface IDisjointSet<T> {
    /**
     * Creates a new set containing just the given item.
     * The item is internally assigned an integer id (a 'representative').
     *
     * @throws IllegalArgumentException  if the item is already a part of this disjoint set somewhere
     */
    public void makeSet(T item);

    /**
     * Returns the integer id (the 'representative') associated with the given item.
     *
     * @throws IllegalArgumentException  if the item is not contained inside this disjoint set
     */
    public int findSet(T item);

    /**
     * Finds the two sets associated with the given items, and combines the two sets together.
     * Does nothing if both items are already in the same set.
     *
     * @throws IllegalArgumentException  if either item1 or item2 is not contained inside this disjoint set
     */
    public void union(T item1, T item2);

}
