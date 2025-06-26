package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class CustomListTest {

    private static Stream<Arguments> impls() {
        return Stream.of(
                Arguments.of(new ArrayList<Integer>()),
                Arguments.of(new CustomList<Integer>()));
    }

    private List<Integer> customList;

    @BeforeEach
    void setUp() {
        customList = new CustomList<>();
    }

    @Test
    void size() {
        Assertions.assertEquals(customList.size(), 0);
        customList.add(1);
        customList.add(2);
        customList.add(2);
        Assertions.assertEquals(customList.size(), 3);
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(customList.isEmpty());
        customList.add(1);
        Assertions.assertFalse(customList.isEmpty());
    }

    @Test
    void contains() {
        Assertions.assertFalse(customList.contains(1));
        customList.add(1);
        customList.add(2);
        customList.add(4);
        Assertions.assertTrue(customList.contains(1));
    }

    @Test
    void iterator() {
    }

    @Test
    void toArray() {
    }

    @Test
    void toArrayWithArg() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        //list.toArray(new Integer[]{1,2,3,4,5,6,7,8,9,120}); //[ 1, 2, 3, 4, 5, 6, 7, null, 9, 120 ]

    }

    @Test
    void testToArray() {
    }

    @Test
    void add() {

    }

    @ParameterizedTest
    @MethodSource("impls")
    void addAfterIndex(List list) {
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
            Assertions.assertEquals(list.get(i), i);
        }
    }

    @Test
    void remove() {
    }

    @Test
    void containsAll() {
    }

    @Test
    void addAll() {
    }

    @Test
    void testAddAll() {
    }

    @Test
    void removeAll() {
    }

    @DisplayName("Keep only intersection")
    @Test
    void testRetainAll() {
        List<Integer> list1 = new CustomList<>();
        list1.add(1);
        list1.add(2);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        List<Integer> list2 = new CustomList<>();
        list2.add(2);
        list2.add(3);

        Assertions.assertTrue(list1.retainAll(list2));
        Assertions.assertEquals(list1.get(0), 2);
        Assertions.assertEquals(list1.get(1), 2);
        Assertions.assertEquals(list1.get(2), 3);
        Assertions.assertEquals(list1.size(), 3);
    }

    @Test
    void clear() {
    }

    @Test
    void get() {
    }

    @Test
    void set() {
    }

    @Test
    void testAdd() {
    }

    @Test
    void testRemove() {
    }

    @Test
    void indexOf() {
    }

    @Test
    void lastIndexOf() {
    }

    @Test
    void listIterator() {
    }

    @Test
    void testListIterator() {
    }

    @Test
    void subList() {
    }
}