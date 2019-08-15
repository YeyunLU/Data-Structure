package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class ChainedHashSet<T> implements ISet<T> {
    private IDictionary<T, Boolean> map;
    private int size;

    public ChainedHashSet() {
        // No need to change this method
        this.map = new ChainedHashDictionary<>();
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (this.map.containsKey(item)){
            return;
        }
        else {
            this.map.put(item, true);
            this.size++;
        }
    }

    @Override
    public void remove(T item) {
        if (!map.containsKey(item)){
            throw new NoSuchElementException();
        }
        else {
            this.map.remove(item);
            this.size--;
        }
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;

        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            // No need to change this method.
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public T next() {
            return iter.next().getKey();
        }
    }
}
