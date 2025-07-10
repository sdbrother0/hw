package org.example.hw03;

import com.google.common.base.Stopwatch;
import org.example.hw01.CustomList;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PerfTest {

    public static List<Integer> getImplByNum(int num) {
        return switch (num) {
            case 0 -> new ArrayList<>();
            case 1 -> new LinkedList<>();
            case 2 -> new CustomList<>();
            case 3 -> new CustomLinkedList<>();
            default -> null;
        };
    }

    public static void init() throws InterruptedException {
        //Bulk Addition Test: Add 1,000,000 elements and measure:
        for (int my = 0; my < 5; my++) {
            for (int test = 0; test < 4; test++) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                List<Integer> list = getImplByNum(test);
                for (int i = 0; i < 1_000_000; i++) {
                    list.add(i);
                }
                stopwatch.stop();
                Duration duration = stopwatch.elapsed();
                System.out.printf("%s Duration Millis add %s: %s%n", my, list.getClass(), duration.toMillis());
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
            System.out.println("---------------------------------------------------");
        }

        //Remove test
        for (int my = 0; my < 5; my++) {
            for (int test = 0; test < 4; test++) {
                List<Integer> list =  getImplByNum(test);
                for (int i = 0; i < 10_000; i++) {
                    list.add(i);
                }
                Stopwatch stopwatch = Stopwatch.createStarted();
                for (int i = 0; i < 10_000; i++) {
                    list.remove(0);
                }
                stopwatch.stop();
                Duration duration = stopwatch.elapsed();
                System.out.printf("%s Duration Millis remove 0 element %s: %s%n", my, list.getClass(), duration.toMillis());
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
            System.out.println("---------------------------------------------------");
        }

    }
}
