package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private IDictionary<T, Integer> dict;
    private int size;

    // However, feel free to add more fields and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        this.pointers = new int[5];
        this.dict = new ChainedHashDictionary<>();
        this.size=0;
    }

    @Override
    public void makeSet(T item) {
        if (item==null||dict.containsKey(item)){
            throw new IllegalArgumentException();
        }
        dict.put(item, size);
        if (size==pointers.length){
            int newSize = size*2;
            int[] newArray = new int[newSize];
            System.arraycopy(pointers, 0, newArray, 0, size);
            pointers=newArray;
        }
        pointers[size]=-1;
        size++;
        return;
    }

    @Override
    public int findSet(T item) {
        if (!dict.containsKey(item)){
            throw new IllegalArgumentException();
        }
        int idx = dict.get(item);
        int parent = pointers[idx];
        if (parent<0){
            return idx;
        }
        while (true){
            int last = parent;
            parent = pointers[parent];
            if (parent<0){
               return last;
            }
        }
    }

    @Override
    public void union(T item1, T item2) {
        if (!dict.containsKey(item1)||!dict.containsKey(item2)){
            throw new IllegalArgumentException();
        }
        int rep1 = findSet(item1);
        int rep2 = findSet(item2);
        if (rep1==rep2) {return; }
        int rank1 = findRank(item1);
        int rank2 = findRank(item2);
        if (rank1<rank2) {
            pointers[rep1] = rep2;
        }
        else if (rank2<rank1){
            pointers[rep2] = rep1;
        }
        else {
            pointers[rep1] = rep2;
            pointers[rep2] = -(rank2+1);
        }

    }

    private int findRank(T item){
        int rank = 0;
        int idx = dict.get(item);
        int parent=pointers[idx];
        while (parent>=0){
           parent=pointers[parent];
           rank++;
        }
        return rank;
    }

}
