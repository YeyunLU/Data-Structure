package datastructures.concrete.dictionaries;

import java.util.Objects;

/**
 * Represents a key-value pair from a dictionary.
 *
 * This data structure is deliberately immutable (read-only): you cannot
 * change the key or value within this pair.
 */
public class KVPair<K, V> {
    // Implementation note: you may NOT modify this class.

    private K key;
    private V value;

    /**
     * Constructs a new KVPair instance using the given key and value.
     */
    public KVPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key.
     */
    public K getKey() {
        return this.key;
    }

    /**
     * Returns the value.
     */
    public V getValue() {
        return this.value;
    }

    /**
     * Returns 'true' if the given object is of type KVPair and if
     * its key and value are both equivalent to this one's.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        KVPair<?, ?> that = (KVPair<?, ?>) o;

        if (!Objects.equals(key, that.key)) { return false; }
        return Objects.equals(value, that.value);
    }

    /**
     * Returns a hashcode based on both the key and the value's .hashCode() methods.
     *
     * This means it's possible to use a KVPair object as the key of a hash map.
     * That said, it would be very unusual to want to do this.
     *
     * This method is provided more for completeness, not because we expect you
     * will ever end up actually needing to use it.
     */
    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of this KVPair.
     */
    @Override
    public String toString() {
        return this.key + "=" + this.value;
    }
}
