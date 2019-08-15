package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Objects;
/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;
    private int size;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(1);
        pairs[0] = null;
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        V value;
        for (int i=0; i<size; i++){
            if (Objects.equals(this.pairs[i].key, key)){
                value = this.pairs[i].value;
                return value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        for (int i=0; i<size; i++){
            if (Objects.equals(this.pairs[i].key, key)){
                this.pairs[i].value=value;
                return;
            }
        }
        Pair<K, V> pair = new Pair<>(key, value);
        if (this.size==0){
            this.pairs[0]=pair;
        }
        else {
            if (this.size==this.pairs.length){
                int newsize = this.size*2;
                Pair<K, V>[] newarray = this.makeArrayOfPairs(newsize);
                System.arraycopy(this.pairs, 0, newarray, 0, this.size);
                this.pairs = newarray;
            }
            this.pairs[this.size]=pair;
        }
        this.size++;
        return;
    }

    @Override
    public V remove(K key) {
        V value;
        for (int i=0; i<size; i++){
            if (Objects.equals(this.pairs[i].key, key)){
                value=this.pairs[i].value;
                this.pairs[i]=this.pairs[this.size-1];
                this.pairs[this.size-1]=null;
                this.size--;
                return value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public boolean containsKey(K key) {
        for (int i=0; i<this.size; i++){
            if (Objects.equals(this.pairs[i].key, key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
