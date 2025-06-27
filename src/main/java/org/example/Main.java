package org.example;


import com.google.common.base.Stopwatch;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        for (int my = 0; my < 100; my++) {
            for (int test = 0; test < 2; test++) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                List list;
                if (test == 1) {
                    list = new ArrayList();
                } else {
                    list = new CustomList();
                }
                for (int i = 0; i < 10_000_000; i++) {
                    list.add(i);
                }
                stopwatch.stop();
                Duration duration = stopwatch.elapsed();
                System.out.println(String.format("%s Duration Millis %s: %s", my, list.getClass(), duration.toMillis()));
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
            System.out.println("---------------------------------------------------");
        }
    }

}