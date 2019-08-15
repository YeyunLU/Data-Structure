package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {

    private double lambda=1;
    private int size = 0;


    // You MUST use this field to store the contents of your dictionary.
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary(){
        this.chains = makeArrayOfChains(5);
    }

    public ChainedHashDictionary(double lambda) {
        this.lambda = lambda;
        this.chains = makeArrayOfChains(8);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    public int getKeyHash(K key){
        int keyHash;
        if (key==null){
            keyHash = 0;
        }
        else {
            keyHash = key.hashCode();
        }
        if (keyHash>0) {
            keyHash %= this.chains.length;
        }
        else {
            keyHash = (Math.abs(keyHash)%this.chains.length);
        }
        return keyHash;
    }
    @Override
    public V get(K key) {
        int keyHash = getKeyHash(key);
        if (keyHash>0) {
            keyHash %= this.chains.length;
        }
        else {
            keyHash = (Math.abs(keyHash)%this.chains.length);
        }
        if (this.chains[keyHash]==null){
            throw new NoSuchKeyException();
        }
        return this.chains[keyHash].get(key);
    }

    @Override
    public void put(K key, V value) {

        int keyHash;

        // resize
        if (this.size/this.chains.length>=lambda){

            IDictionary<K, V>[] newChains = makeArrayOfChains(this.chains.length*2);
            for (int i = 0; i<this.chains.length; i++){

                if (this.chains[i]==null){
                    continue;
                }

                for (KVPair<K, V> p:this.chains[i]){

                    K k = p.getKey();
                    V v = p.getValue();

                    if (k==null){
                        keyHash = 0;
                    }
                    else {
                        keyHash = k.hashCode();
                    }
                    if (keyHash>0) {
                        keyHash %= newChains.length;
                    }
                    else {
                        keyHash = (Math.abs(keyHash)%newChains.length);
                    }
                    if (newChains[keyHash]==null){
                        newChains[keyHash] = new ArrayDictionary<>();
                    }
                    newChains[keyHash].put(k, v);
                }
            }
            this.chains = newChains;
        }

        // add new key value pair
        keyHash = getKeyHash(key);
        if (this.chains[keyHash]==null){
            this.chains[keyHash] = new ArrayDictionary<>();
        }
        if (!chains[keyHash].containsKey(key)){
            this.size++;
        }
        this.chains[keyHash].put(key, value);
    }

    @Override
    public V remove(K key) {

        V value;
        int keyHash = getKeyHash(key);

        if (this.chains[keyHash]==null || !this.chains[keyHash].containsKey(key)){
            throw new NoSuchKeyException();
        }
        else {
            this.size--;
            return this.chains[keyHash].remove(key);
        }

    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public boolean containsKey(K key) {

        int keyHash = getKeyHash(key);

        if (this.chains[keyHash]==null){
            return false;
        }
        else {
            return this.chains[keyHash].containsKey(key);
        }

    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {

        private Iterator<KVPair<K, V>> iter;
        private IDictionary<K, V>[] chains;
        private int cur;


        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.cur = 0;
            this.chains = chains;
            if (this.chains[cur]!=null){
                this.iter = this.chains[cur].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            while (cur < this.chains.length) {
                if (iter != null && iter.hasNext()) {
                    return true;
                } else {
                    cur++;
                    if (cur == this.chains.length) {
                        return false;
                    }
                    if (this.chains[cur] != null) {
                        // switch to the beginning of an new array
                        iter = this.chains[cur].iterator();
                    } else {
                        iter = null;
                    }
                }
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                return iter.next();
            }
        }
    }
}
