package datastructures.interfaces;

import misc.exceptions.NoSuchKeyException;

/**
 * Represents a data structure that contains a bunch of key-value mappings. Each key must be unique.
 */
public interface IDictionary<K, V> {
    /**
     * Returns the value corresponding to the given key.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    public V get(K key);

    /**
     * Returns the value corresponding to the given key, if the key exists in the map.
     *
     * If the key does *not* contain the given key, returns the default value.
     *
     * Note: This method does not modify the map in any way. The interface also
     *       provides a default implementation, but you may optionally override
     *       it with a more efficient version.
     */
    public default V getOrDefault(K key, V defaultValue) {
        if (this.containsKey(key)) {
            return this.get(key);
        } else {
            return defaultValue;
        }
    }

    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replace its value with the given one.
     */
    public void put(K key, V value);

    /**
     * Remove the key-value pair corresponding to the given key from the dictionary.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    public V remove(K key);

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     */
    public boolean containsKey(K key);

    /**
     * Returns the number of key-value pairs stored in this dictionary.
     */
    public int size();

    /**
     * Returns 'true' if this dictionary is empty and 'false' otherwise.
     */
    public default boolean isEmpty() {
        return this.size() == 0;
    }
}
