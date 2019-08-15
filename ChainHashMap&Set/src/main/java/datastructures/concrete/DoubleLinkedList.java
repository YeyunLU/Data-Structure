package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        Node<T> node = new Node<T>(item);
        if (this.size == 0)
        {
            this.front = node;
            this.back = node;
        }
        else {
            this.back.next = node;
            node.prev = this.back;
            this.back = node;

        }
        this.size++;
        return;
    }

    @Override
    public T remove() {

        if (this.size == 0) {
            throw new EmptyContainerException();
        }
        T item = this.back.data;
        if (this.size == 1){
            this.front = null;
            this.back = null;
            this.size = 0;
        }
        else {
            this.back = this.back.prev;
            this.back.next=null;
            this.size--;
        }
        return item;
    }

    @Override
    public T get(int index) {
        if (index>=this.size || index < 0){
            throw new IndexOutOfBoundsException();
        }
        Node<T> cur;
        if (index>(this.size/2)){
            cur = this.back;
            for (int i=0; i<this.size-1-index; i++){
                cur = cur.prev;
            }
        }
        else {
            cur = this.front;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
        }
        return cur.data;
    }

    public void set(int index, T item) {
        if (index>=this.size || index < 0){
            throw new IndexOutOfBoundsException();
        }
        Node<T> cur;
        if (index <= (this.size/2)) {
            cur = this.front;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
        }
        else {
            cur = this.back;
            for (int i=0; i<this.size -1 -index; i++){
                cur = cur.prev;
            }
        }
        cur.data = item;
    }

    @Override
    public void insert(int index, T item) {
        if (index>=this.size+1 || index < 0){
            throw new IndexOutOfBoundsException();
        }
        Node<T> node = new Node(item);
        if (this.size<=1){
            if (index==1){
                this.back.next =node;
                node.prev=this.back;
                this.back=node;
            }
            else {
                if (this.front!=null) {
                    node.next = this.front;
                    this.back.prev=node; }
                else {this.back=node; }
                this.front = node;
            }
            this.size++;
            return;
        }
        if (index==0){
            node.next = this.front;
            this.front.prev = node;
            this.front = node;
            this.size++;
            return;
        }
        else if (index==this.size){
            this.back.next = node;
            node.prev = this.back;
            this.back = node;
            this.size++;
            return;
        }
        Node<T> cur;
        if (index>(this.size/2)) {
            cur = this.back;
            for (int i=0; i<this.size-1-index; i++){
                cur = cur.prev;
            }
        }
        else {
            cur=this.front;
            for (int i=0; i<index; i++){
                cur = cur.next;
            }
        }
        cur.prev.next = node;
        node.next = cur;
        node.prev = cur.prev;
        cur.prev = node;
        this.size++;
    }

    @Override
    public T delete(int index) {
        if (index>=this.size || index < 0){
            throw new IndexOutOfBoundsException();
        }
        if (this.size <= 1){
            T ret = this.front.data;
            this.front = null;
            this.back = null;
            this.size = 0;
            return ret;
        }else {
            if (index == 0) {
                Node<T> second = this.front.next;
                T ret = this.front.data;
                this.front = second;
                this.front.prev = null;
                this.size--;
                return ret;
            }
            else if (index==this.size-1){
                T ret = this.back.data;
                this.back.prev.next = null;
                this.back = this.back.prev;
                this.size--;
                return ret;
            }
            Node<T> cur;
            if (index>(this.size/2)) {
                cur = this.back;
                for (int i=0; i<this.size-1-index; i++){
                    cur = cur.prev;
                }
            }
            else {
                cur=this.front;
                for (int i=0; i<index; i++){
                    cur = cur.next;
                }
            }
            T ret = cur.data;
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
            this.size--;
            return ret;
        }
    }

    @Override
    public int indexOf(T item) {
        int idx = 0;
        Node<T> cur = this.front;
        while (cur!=null){
            if (Objects.equals(cur.data, item)) {return idx; }
            cur = cur.next;
            idx++;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> cur = this.front;
        while (cur!=null){
            if (Objects.equals(cur.data, other)) {return true; }
            cur = cur.next;
        }

        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return this.current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (this.current == null){
                throw new NoSuchElementException();
            }
            T data = this.current.data;
            this.current = this.current.next;

            return data;

        }
    }
}


