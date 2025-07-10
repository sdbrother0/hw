package org.example.hw03;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class CustomLinkedList<T> implements List<T> {

    private Node head;
    private Node tail;

    private int elementsCount = 0;

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
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getData().equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator();
    }

    @Override
    public Object[] toArray() {
        T[] array = (T[]) new Object[elementsCount];
        if (elementsCount == 0) {
            return array;
        }
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            Node node = it.next();
            array[it.getIndex()] = node.getData();
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < elementsCount) {
            return (T1[]) toArray();
        }
        if (a.length > elementsCount) {
            System.arraycopy(toArray(), 0, a, 0, elementsCount);
            a[elementsCount] = null;
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        if (head == null) {
            head = addNodeAfter(null, t);
            tail = head;
            return true;
        } else {
            tail = addNodeAfter(tail, t);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getData().equals(o)) {
                removeNode(node);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Object[] array = this.toArray();
        for (int i = 0; i < array.length; i++) {
            add((T) array[i]);
        }
        return array.length > 0;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            Node node = it.next();
            if (it.getIndex() == index - 1) {
                Node nodeAfter = node;
                for (T t : c) {
                    nodeAfter = addNodeAfter(nodeAfter, t);
                }
                break;
            }
        }
        return !c.isEmpty();
    }

    @Override
    public void clear() {
        tail = null;
        head = null;
        elementsCount = 0;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            Node node = it.next();
            if (it.getIndex() == index) {
                return node.getData();
            }
        }
        return null;
    }

    @Override
    public T set(int index, T element) {
        if (elementsCount == 0 || index < 0 || index >= elementsCount) {
            throw new IndexOutOfBoundsException();
        }
        T prev = null;
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            Node node = it.next();
            if (it.getIndex() == index) {
                prev = node.getData();
                node.setData(element);
                return prev;
            }
        }
        return prev;
    }

    @Override
    public void add(int index, T element) {
        addAll(index, List.of(element));
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int removed = 0;
        for (Object o : c) {
            remove(o);
            removed++;
        }
        return removed > 0;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        boolean found;
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            found = false;
            Node node = it.next();
            for (Object o : c) {
                if (node.getData().equals(o)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = true;
                removeNode(node);
            }
        }
        return result;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            Node node = it.next();
            if (it.getIndex() == index) {
                T prev = node.getData();
                removeNode(node);
                return prev;
            }
        }
        return null;
    }

    @Override
    public int indexOf(Object o) {
        IteratorNode it = new IteratorNode();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getData().equals(o)) {
                return it.getIndex();
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        IteratorNode it = new IteratorNode();
        int lastIndex = -1;
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getData().equals(o)) {
                lastIndex = it.getIndex();
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
        return null; //TODO
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size() - 1 || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        IteratorNode it = new IteratorNode();
        List<T> subList = new CustomLinkedList<>();
        while (it.hasNext()) {
            Node node = it.next();
            if (it.getIndex() >= fromIndex  && it.getIndex() <= toIndex) {
                subList.add(node.getData());
            }
        }
        return subList;
    }

    @EqualsAndHashCode
    @Getter
    @Setter
    private class Node {
        public Node(T data) {
            this.data = data;
        }

        private T data;
        private Node next;
        private Node prev;
    }

    private class CustomIterator<T> implements Iterator<T> {
        protected IteratorNode iteratorNode = new IteratorNode();

        @Override
        public boolean hasNext() {
            return iteratorNode.hasNext();
        }

        @Override
        public T next() {
            return (T) iteratorNode.next().getData();
        }
    }

    private class CustomListIterator extends CustomIterator<T> implements ListIterator<T> {

        @Override
        public boolean hasPrevious() {
            return iteratorNode.hasPrevious();
        }

        @Override
        public T previous() {
            return (T) iteratorNode.previous().getData();
        }

        @Override
        public int nextIndex() {
            return iteratorNode.getIndex() + 1;
        }

        @Override
        public int previousIndex() {
            return iteratorNode.getIndex() - 1;
        }

        @Override
        public void remove() {
            removeNode(iteratorNode.getNode());
        }

        @Override
        public void set(T t) {
            Node node = iteratorNode.getNode();
            node.setData(t);
        }

        @Override
        public void add(T t) {
            addNodeAfter(iteratorNode.getNode(), t);
        }
    }

    private class IteratorNode {
        @Getter
        private Node node = null;

        @Getter
        private int index = 0;

        public boolean hasNext() {
            if (node == null && head != null) {
                return true;
            }
            return node != null && node.getNext() != null;
        }

        public boolean hasPrevious() {
            return node.getPrev() != null;
        }

        public Node next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (node == null) {
                node = head;
                return node;
            }
            this.node = this.node.getNext();
            index++;
            return node;
        }

        public Node previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            Node prev = node;
            this.node = this.node.getPrev();
            index--;
            return prev;
        }
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        Node prev = node.getPrev();
        Node next = node.getNext();
        if (node.equals(head) && next != null) {
            head = next;
        } else {
            if (prev != null) {
                prev.setNext(next);
            }
            if (next != null) {
                next.setPrev(prev);
            }
        }
        elementsCount--;
    }

    private Node addNodeAfter(Node node, T data) {
        if (node == null) {
            node = new Node(data);
            elementsCount++;
            return node;
        }
        Node newNode = new Node(data);
        newNode.setPrev(node);
        Node nodeNext = node.getNext();
        node.setNext(newNode);
        newNode.setNext(nodeNext);
        elementsCount++;
        return newNode;
    }

}
