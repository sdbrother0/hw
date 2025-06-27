package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

class CustomListTest {

    private static Stream<Arguments> impls() {
        return Stream.of(Arguments.of(new ArrayList<Integer>()), Arguments.of(new CustomList<Integer>()));
    }

    @ParameterizedTest
    @MethodSource("impls")
    void size(List<Integer> list) {
        Assertions.assertEquals(0, list.size());
        list.add(1);
        list.add(2);
        list.add(2);
        Assertions.assertEquals(3, list.size());
        list.remove(2);
        Assertions.assertEquals(2, list.size());
    }

    @ParameterizedTest
    @MethodSource("impls")
    void isEmpty(List<Integer> list) {
        Assertions.assertTrue(list.isEmpty());
        list.add(1);
        Assertions.assertFalse(list.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("impls")
    void contains(List<Integer> list) {
        Assertions.assertFalse(list.contains(1));
        list.add(1);
        list.add(2);
        list.add(4);
        Assertions.assertTrue(list.contains(1));
    }

    @ParameterizedTest
    @MethodSource("impls")
    void iterator(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> integerIterator = list.iterator();
        Assertions.assertEquals(1, integerIterator.next());
        Assertions.assertEquals(2, integerIterator.next());
        Assertions.assertEquals(3, integerIterator.next());
    }

    @ParameterizedTest
    @MethodSource("impls")
    void iteratorList(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        ListIterator<Integer> listIterator = list.listIterator();
        //go forward
        Assertions.assertEquals(1, listIterator.next());
        Assertions.assertEquals(2, listIterator.next());
        Assertions.assertEquals(3, listIterator.next());
        //go backward
        Assertions.assertEquals(3, listIterator.previous());
        Assertions.assertEquals(2, listIterator.previous());
    }

    @ParameterizedTest
    @MethodSource("impls")
    void toArray(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        Assertions.assertArrayEquals(new Integer[]{1, 2, 3}, list.toArray());
    }

    @ParameterizedTest
    @MethodSource("impls")
    void toArrayWithArg(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        Assertions.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, null, 9, 120}, list.toArray(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 120}));
    }

    @ParameterizedTest
    @MethodSource("impls")
    void add(List<Integer> list) {
        Assertions.assertTrue(list.add(1));
        Assertions.assertTrue(list.add(2));
    }

    @ParameterizedTest
    @MethodSource("impls")
    void addAfterIndex(List<Integer> list) {
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        //skip 6
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);
        list.add(11);
        //add 6
        list.add(6, 6);
        for (int i = 0; i < list.size(); i++) {
            Assertions.assertEquals(i, list.get(i));
        }
    }

    @ParameterizedTest
    @MethodSource("impls")
    void remove(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(1, list.get(0));
        Assertions.assertEquals(3, list.get(1));
    }

    @ParameterizedTest
    @MethodSource("impls")
    void containsAll(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        Assertions.assertTrue(list.containsAll(List.of(1, 2, 3)));
    }

    @ParameterizedTest
    @MethodSource("impls")
    void addAll(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.addAll(list);
        Assertions.assertEquals(4, list.size());
    }

    @ParameterizedTest
    @MethodSource("impls")
    void testAddAll(List<Integer> list) {
    }

    @ParameterizedTest
    @MethodSource("impls")
    void removeAll(List<Integer> list) {
    }

    @ParameterizedTest
    @MethodSource("impls")
    void testRetainAll(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(4);

        List<Integer> list1 = new ArrayList<>();
        list1.add(2);
        list1.add(3);

        Assertions.assertTrue(list.retainAll(list1));
        Assertions.assertEquals(2, list.get(0));
        Assertions.assertEquals(2, list.get(1));
        Assertions.assertEquals(3, list.get(2));

        Assertions.assertEquals(3, list.size());
    }

    @ParameterizedTest
    @MethodSource("impls")
    void clear(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.add(2);
        list.clear();
        Assertions.assertEquals(0, list.size());
    }

    @ParameterizedTest
    @MethodSource("impls")
    void get(List<Integer> list) {
        list.add(1);
        list.add(2);
        Assertions.assertEquals(2, list.get(1));
    }

    @ParameterizedTest
    @MethodSource("impls")
    void set(List<Integer> list) {
        list.add(1);
        list.add(2);
        list.set(1, 3);
        Assertions.assertEquals(3, list.get(1));
    }
}