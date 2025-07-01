package org.example;


import com.google.common.base.Stopwatch;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //Bulk Addition Test: Add 1,000,000 elements and measure:
        for (int my = 0; my < 5; my++) {
            for (int test = 0; test < 3; test++) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                List list;
                if (test == 1) {
                    list = new ArrayList();
                } else if (test == 2){
                    list = new LinkedList<>();
                } else {
                    list = new CustomList();
                }
                for (int i = 0; i < 1_000_000; i++) {
                    list.add(i);
                }
                stopwatch.stop();
                Duration duration = stopwatch.elapsed();
                System.out.println(String.format("%s Duration Millis add %s: %s", my, list.getClass(), duration.toMillis()));
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
            System.out.println("---------------------------------------------------");
        }

        //Remove test
        for (int my = 0; my < 5; my++) {
            for (int test = 0; test < 3; test++) {

                List list;
                if (test == 1) {
                    list = new ArrayList();
                } else if (test == 2){
                    list = new LinkedList<>();
                } else {
                    list = new CustomList();
                }
                for (int i = 0; i < 10_000; i++) {
                    list.add(i);
                }
                Stopwatch stopwatch = Stopwatch.createStarted();
                for (int i = 0; i < 10_000; i++) {
                    list.remove(0);
                }
                stopwatch.stop();
                Duration duration = stopwatch.elapsed();
                System.out.println(String.format("%s Duration Millis remove 0 element %s: %s", my, list.getClass(), duration.toMillis()));
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
            System.out.println("---------------------------------------------------");
        }
    }

}