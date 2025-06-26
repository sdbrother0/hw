package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Main {
    public static void main(String[] args) {

        List<Integer> list1 = new CustomList<>();
        list1.add(0);
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        //skip 6
        list1.add(7);
        list1.add(8);
        list1.add(9);
        list1.add(10);
        list1.add(11);
        //add 6
        list1.add(6, 6);




        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(6);
        list.add(7);

        ListIterator<Integer> iterator = list.listIterator();


        list.addAll(1, List.of(-1,-3));
        Integer a = iterator.next();
        a = iterator.next();
        a = iterator.previous();
        //Integer y = iterator.next();

        //list.toArray(new Integer[]{1,2,3,4,5,6,7,8,9,120}); //[ 1, 2, 3, 4, 5, 6, 7, null, 9, 120 ]
        //list.toArray(new Integer[]{1,2,3,4,5,6,7,8,9,120}); //[ 1, 2, 3, 4, 5, 6, 7, null, 9, 120 ]
        Integer[] x = list.toArray(new Integer[]{4, 20, 17});

        Iterator<Integer> stringIterator = list.iterator();
        while (stringIterator.hasNext()) {
            System.out.println(stringIterator.next());
        }
    }

}