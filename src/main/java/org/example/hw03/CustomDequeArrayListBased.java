package org.example.hw03;

import lombok.Getter;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomDequeArrayListBased<E> implements Deque<E> {

    private static final double MAG_FACTOR = 1.5;
    private static final int INIT_CAPACITY = 10;
    private int elementsCount = 0;
    private E[] elements = initElements(INIT_CAPACITY);
    private int head;
    private int tail;

    @Override
    public void addFirst(E e) {
        if (size() == elements.length) {
            resizeElements();
        }
        head = (head + elements.length - 1) % elements.length;
        elements[head] = e;
        elementsCount++;
    }

    @Override
    public void addLast(E e) {
        if (size() == elements.length) {
            resizeElements();
        }
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        elementsCount++;
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
        E element = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        elementsCount--;
        return element;
    }

    @Override
    public E removeLast() {
        tail = (tail + elements.length - 1) % elements.length;
        E element = elements[tail];
        elements[tail] = null;
        elementsCount--;
        return element;
    }

    @Override
    public E pollFirst() {
        return removeFirst();
    }

    @Override
    public E pollLast() {
        return removeLast();
    }

    @Override
    public E getFirst() {
        return elements[head];
    }

    @Override
    public E getLast() {
        return elements[(tail + elements.length - 1) % elements.length];
    }

    @Override
    public E peekFirst() {
        return getFirst();
    }

    @Override
    public E peekLast() {
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        CustomIterator it = (CustomIterator) iterator();
        while (it.hasNext()) {
            if (it.equals(o)) {
                removeByIndex(it.getCurrentIndex());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        int lastIndex = -1;
        CustomIterator it = (CustomIterator) iterator();
        while (it.hasNext()) {
            if (it.equals(o)) {
                lastIndex = it.getCurrentIndex();
            }
        }
        if (lastIndex > 0) {
            removeByIndex(lastIndex);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        elementsCount++;
        return true;
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return pop();
    }

    @Override
    public E poll() {
        return pop();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return getFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E element : c) {
            addLast(element);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object element : c) {
            remove(element);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        boolean found;
        CustomIterator it = (CustomIterator) iterator();
        while (it.hasNext()) {
            found = false;
            for (Object o : c) {
                if (c.equals(o)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = true;
                removeByIndex(it.getCurrentIndex());
            }
        }
        return result;
    }

    @Override
    public void clear() {
        elementsCount = 0;
        head = 0;
        tail = 0;
        for (int i = 0; i < size(); i++) {
            elements[(head + i) % elements.length] = null;
        }
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object object : c) {
            if (!contains(object)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size(); i++) {
            if (o.equals(elements[(head + i) % elements.length])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return elementsCount;
    }

    @Override
    public boolean isEmpty() {
        return elementsCount == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new CustomIterator();
    }

    @Override
    public Object[] toArray() {
        E[] result = initElements(size());
        for (int i = 0; i < size(); i++) {
            result[i] = elements[(head + i) % elements.length];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < elementsCount) {
            return (T[]) toArray();
        }
        if (a.length > elementsCount) {
            System.arraycopy(elements, 0, a, 0, elementsCount);
            a[elementsCount] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new CustomDescendIterator();
    }

    private void removeByIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == head) {
            removeFirst();
        } else if (index == (tail + elements.length - 1) % elements.length) {
            removeLast();
        } else {
            if (index - head < tail - index) {
                for (int i = index; i != head; i = (i + elements.length - 1) % elements.length) {
                    elements[i] = elements[(i + elements.length - 1) % elements.length];
                }
                elements[head] = null;
                head = (head + 1) % elements.length;
            } else {
                for (int i = index; i != tail; i = (i + 1) % elements.length) {
                    elements[i] = elements[(i + 1) % elements.length];
                }
                elements[(tail + elements.length - 1) % elements.length] = null;
                tail = (tail + elements.length - 1) % elements.length;
            }
            elementsCount--;
        }
    }

    private E[] initElements(int length) {
        return (E[]) new Object[length];
    }

    private void resizeElements() {
        if (elements.length == elementsCount) {
            int newLength = (int) (elementsCount * MAG_FACTOR);
            if (newLength == Integer.MAX_VALUE) {
                throw new OutOfMemoryError();
            }
            E[] newElements = initElements(newLength);
            for (int i = 0; i < size(); i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }
            elements = newElements;
            head = 0;
            tail = size();
        }
    }

    private class CustomIterator implements Iterator<E> {
        @Getter
        private int currentIndex = head;
        private int idx = elementsCount;

        @Override
        public boolean hasNext() {
            return idx > 0;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = elements[currentIndex];
            currentIndex = (currentIndex + 1) % elements.length;
            idx--;
            return element;
        }
    }

    private class CustomDescendIterator implements Iterator<E> {
        private int currentIndex = (tail + elements.length - 1) % elements.length;
        private int idx = elementsCount;

        @Override
        public boolean hasNext() {
            return idx > 0;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = elements[currentIndex];
            currentIndex = (currentIndex + elements.length - 1) % elements.length;
            idx--;
            return element;
        }
    }

}
