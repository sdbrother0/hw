package org.example;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class CustomList<T> implements List<T> {

    private static final double MAG_FACTOR = 1.5;
    private static final int INIT_CAPACITY = 10;
    private int elementsCount = 0;
    private T[] elements = initElements(INIT_CAPACITY);

    private boolean isManualCopy = true;

    public CustomList() {
    }


    public CustomList(T[] elements) {
        this.elements = elements;
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
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator();
    }

    @Override
    public Object[] toArray() {
        T[] toArray = initElements(elementsCount);
        arraycopy(elements, 0, toArray, 0, elementsCount);
        return toArray;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < elementsCount) {
            return (T1[]) toArray();
        }
        if (a.length > elementsCount) {
            arraycopy(elements, 0, (T[]) a, 0, elementsCount);
            a[elementsCount] = null;
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        resizeElements();
        elements[elementsCount] = t;
        elementsCount++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < elementsCount; i++) {
            if (elements[i].equals(o)) {
                removeByIndex(i);
                elementsCount--;
                elements[elementsCount] = null;
                return true;
            }
        }
        return false;
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
    public boolean addAll(Collection<? extends T> c) {
        for (Object object : c) {
            this.add((T) object);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        T[] newElements = initElements(elementsCount + c.size());
        arraycopy(elements, 0, newElements, 0, index);
        arraycopy((T[]) c.toArray(), 0, newElements, index, c.size());
        arraycopy(elements, index, newElements, index + c.size(), elementsCount - index);
        elements = newElements;
        elementsCount = elementsCount + c.size();
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (Object o : c) {
            int index = indexOf(o);
            if (index >= 0) {
                removeByIndex(index);
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        boolean found;
        for (int i = 0; i < elementsCount; i++) {
            T element = elements[i];
            found = false;
            for (Object object : c) {
                if (element.equals(object)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = true;
                removeByIndex(i);
                elementsCount--;
            }
        }
        return result;
    }

    @Override
    public void clear() {
        elements = initElements(INIT_CAPACITY);
        elementsCount = 0;
    }

    @Override
    public T get(int index) {
        return elements[index];
    }

    @Override
    public T set(int index, T element) {
        elements[index] = element;
        return element;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        T[] newElements = initElements(elementsCount + 1);
        arraycopy(elements, 0, newElements, 0, index);
        arraycopy(elements, index, newElements, index + 1, elementsCount - index);
        newElements[index] = element;
        elements = newElements;
        elementsCount++;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= elementsCount) {
            throw new IndexOutOfBoundsException();
        }
        T element = elements[elementsCount];
        removeByIndex(index);
        elements[elementsCount] = null;
        elementsCount--;
        return element;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < elementsCount; i++) {
            if (elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;
        for (int i = 0; i < elementsCount; i++) {
            if (elements[i].equals(o)) {
                lastIndex = i;
            }
        }
        return lastIndex;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new CustomListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new CustomListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        T[] newElements = initElements(toIndex - fromIndex);
        arraycopy(elements, fromIndex, newElements, fromIndex, toIndex - fromIndex);
        return new CustomList<>(newElements);
    }

    private void arraycopy(T[] src, int srcPos, T[] dest, int destPos, int length) {
        if (isManualCopy) {
            for (int i = 0; i < length; i++) {
                dest[destPos + i] = src[srcPos + i];
            }
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
        }
    }

    private void resizeElements() {
        if (elements.length == elementsCount) {
            int newLength = (int) (elementsCount * MAG_FACTOR);
            if (newLength == Integer.MAX_VALUE) { //(int) (Integer.MAX_VALUE * MAG_FACTOR) == Integer.MAX_VALUE
                throw new OutOfMemoryError();
            }
            T[] newElements = initElements(newLength);
            arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
    }

    private T[] initElements(int length) {
        return (T[]) new Object[length];
    }

    private void removeByIndex(int index) {
        arraycopy(elements, index + 1, elements, index, elementsCount - index - 1);
    }


    private class CustomIterator implements Iterator<T> {
        protected int index = -1;

        @Override
        public boolean hasNext() {
            return index < elementsCount;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            index++;
            T element = elements[index];
            return element;
        }
    }

    private class CustomListIterator extends CustomIterator implements ListIterator<T> {

        public CustomListIterator() {
        }

        public CustomListIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            T element = elements[index];
            index--;
            return element;
        }

        @Override
        public int nextIndex() {
            if (hasNext()) {
                return index + 1;
            }
            return -1;
        }

        @Override
        public int previousIndex() {
            if (hasPrevious()) {
                return index - 1;
            }
            return -1;
        }

        @Override
        public void remove() {
            removeByIndex(index);
        }

        @Override
        public void set(T t) {
            elements[index] = t;
        }

        @Override
        public void add(T t) {
            addLast(t);
        }
    }

}
