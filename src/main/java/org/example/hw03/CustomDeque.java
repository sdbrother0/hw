package org.example.hw03;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomDeque<E> implements Deque<E> {

    private final List<E> list = new CustomLinkedList<>();

    @Override
    public void addFirst(E e) {
        if (list.isEmpty()) {
            list.add(e);
        } else {
            list.add(0, e);
        }
    }

    @Override
    public void addLast(E e) {
        if (list.isEmpty()) {
            list.add(e);
        } else {
            list.add(list.size(), e);
        }
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        E first = list.get(0);
        list.remove(0);
        return first;
    }

    @Override
    public E removeLast() {
        E last = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        return last;
    }

    @Override
    public E pollFirst() {
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public E pollLast() {
        if (!list.isEmpty()) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    @Override
    public E getFirst() {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return list.get(0);
    }

    @Override
    public E getLast() {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return list.get(list.size() - 1);
    }

    @Override
    public E peekFirst() {
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public E peekLast() {
        if (!list.isEmpty()) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(E e) {
        return list.add(e);
    }

    @Override
    public boolean offer(E e) {
        list.add(list.size() - 1, e);
        return true;
    }

    @Override
    public E remove() {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return pop();
    }

    @Override
    public E poll() {
        if (!list.isEmpty()) {
            return pop();
        }
        return null;
    }

    @Override
    public E element() {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return peek();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void push(E e) {
        list.add(0, e);
    }

    @Override
    public E pop() {
        E pop = list.get(0);
        list.remove(0);
        return pop;
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return list.iterator();
    }
}
