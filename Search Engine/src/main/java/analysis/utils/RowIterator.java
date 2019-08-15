package analysis.utils;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RowIterator<T> implements Iterator<IList<T>> {
    private int columnLength;
    private int counter;
    private IList<Iterator<T>> columnIterators;

    public RowIterator(IList<IList<T>> columns) {
        this.columnLength = columns.get(0).size();
        this.counter = 0;
        this.columnIterators = new DoubleLinkedList<>();
        for (IList<T> list : columns) {
            if (list.size() != columnLength) {
                throw new IllegalArgumentException("Not all columns have the same length");
            }
            this.columnIterators.add(list.iterator());
        }
    }

    public boolean hasNext() {
        return this.counter < this.columnLength;
    }

    public IList<T> next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.counter += 1;

        IList<T> row = new DoubleLinkedList<>();
        for (Iterator<T> columnIter : this.columnIterators) {
            row.add(columnIter.next());
        }

        return row;
    }
}
