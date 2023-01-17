package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /**
     * Creates an empty linked list deque
     */
    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
    }

    private int getOne(int index) {
        return (index + 1) % items.length;
    }

    private int getBack(int index) {
        return (index + items.length - 1) % items.length;
    }

    private void resize(int capacity) {
        T[] resized = (T[]) new Object[capacity];

        int index = getOne(nextFirst);
        for (int i = 0; i < size; i++) {
            resized[i] = items[index];
            index = getOne(index);
        }

        nextFirst = capacity - 1;
        nextLast = size;
        items = resized;
    }


    /**
     * Adds an item of type T to the front of the deque.
     * You can assume that item is never null.
     */
    @Override
    public void addFirst(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (size == items.length) {
            resize(size * 2);
        }

        items[nextFirst] = item;
        nextFirst = getBack(nextFirst);
        size += 1;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * You can assume that item is never null.
     */
    @Override
    public void addLast(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (size == items.length) {
            resize(size * 2);
        }

        items[nextLast] = item;
        nextLast = getOne(nextLast);
        size += 1;
    }


    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    @Override
    public void printDeque() {
        int index = getOne(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[index] + " ");
            index = getOne(index);
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        int len = items.length;
        if (len >= 16 && size < len / 4) {
            resize(len / 4);
        }

        nextFirst = getOne(nextFirst);
        T item = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        return item;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        int len = items.length;
        if (len >= 16 && size < len / 4) {
            resize(len / 4);
        }

        nextLast = getBack(nextLast);
        T item = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        return item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     * Must not alter the deque!
     */
    @Override
    public T get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int i = (nextFirst + 1 + index) % items.length;
        return items[i];
    }

    /**
     * The Deque objects we’ll make are iterable (i.e. Iterable<T>)
     * so we must provide this method to return an iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int ptr;

        ArrayDequeIterator() {
            ptr = getOne(nextFirst);
        }
        public boolean hasNext() {
            return ptr != nextLast;
        }
        public T next() {
            T item =  items[ptr];
            ptr = getOne(ptr);
            return item;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (size != other.size()) {
            return false;
        }

        int index = getOne(nextFirst);
        for (int i = 0; i < size; i++) {
            if (!(items[index].equals(other.get(i)))) {
                return false;
            }
            index = getOne(index);
        }
        return true;
    }
}
