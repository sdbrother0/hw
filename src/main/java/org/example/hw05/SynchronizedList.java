package org.example.hw05;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class SynchronizedList<E> implements List<E> {
    private final List<E> list;
    private final Object lock = new Object();

    public SynchronizedList(List<E> list) {
        this.list = Objects.requireNonNull(list);
    }

    @Override
    public int size() {
        synchronized (lock) {
            return list.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (lock) {
            return list.isEmpty();
        }
    }

    @Override
    public boolean contains(Object o) {
        synchronized (lock) {
            return list.contains(o);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        synchronized (lock) {
            return list.toArray();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        synchronized (lock) {
            return list.toArray(a);
        }
    }

    @Override
    public boolean add(E e) {
        synchronized (lock) {
            return list.add(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (lock) {
            return list.remove(o);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        synchronized (lock) {
            return list.containsAll(c);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        synchronized (lock) {
            return list.addAll(c);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        synchronized (lock) {
            return list.addAll(index, c);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        synchronized (lock) {
            return list.removeAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        synchronized (lock) {
            return list.retainAll(c);
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            list.clear();
        }
    }

    @Override
    public E get(int index) {
        synchronized (lock) {
            return list.get(index);
        }
    }

    @Override
    public E set(int index, E element) {
        synchronized (lock) {
            return list.set(index, element);
        }
    }

    @Override
    public void add(int index, E element) {
        synchronized (lock) {
            list.add(index, element);
        }
    }

    @Override
    public E remove(int index) {
        synchronized (lock) {
            return list.remove(index);
        }
    }

    @Override
    public int indexOf(Object o) {
        synchronized (lock) {
            return list.indexOf(o);
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        synchronized (lock) {
            return list.lastIndexOf(o);
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        synchronized (lock) {
            return new SynchronizedList<>(list.subList(fromIndex, toIndex));
        }
    }
}