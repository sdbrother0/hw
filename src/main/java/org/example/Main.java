package org.example;

import org.example.hw03.CustomLinkedList;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Main {
    public static void main(String[] args) {
        /*
        HW03:
        Implement your own LinkedList + Implement your own queue & stack
        add your new linkedlist to the performance test from the hw01.
        compare performance of your different custom List implementations
        Implement your deque implementation based on your own linkedlist
        Implement your own dequeue implementation based on dynamic array
         */

        CustomLinkedList list = new CustomLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);


        ListIterator<Integer> listIterator = list.listIterator();
        Integer next = listIterator.next();
        next = listIterator.next();
        Integer prev =  listIterator.previous();


        list.addAll(list);

        //list.addAll(1, List.of(9,10, 11, 11));

        //list.retainAll(List.of(2,3,4));

        //System.out.println(list.get(1));
        //System.out.println(list);

        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        //System.out.println(list.get(2));


    }
}