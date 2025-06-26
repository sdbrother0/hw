package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CustomListTest {

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

    @Test
    void addAfterIndex() {
        customList.add(0);
        customList.add(1);
        customList.add(2);
        customList.add(3);
        customList.add(4);
        customList.add(5);
        //skip 6
        customList.add(7);
        customList.add(8);
        customList.add(9);
        customList.add(10);
        customList.add(11);
        //add 6
        customList.add(5, 6);

        for (int i = 0; i < customList.size(); i++) {
            Assertions.assertEquals(customList.get(i), i);
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