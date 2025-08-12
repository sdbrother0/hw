package org.example.hw04;

import com.google.common.base.Stopwatch;

import java.util.HashMap;
import java.util.Map;

public class PerfTest4 {

    public static void init() {
        //HashMap test
        Stopwatch stopwatch = Stopwatch.createStarted();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 1_000_000; i++) {
            map.put(String.valueOf(i), i);
        }
        stopwatch.stop();
        System.out.println("Put HashMap Elapsed: " + stopwatch.elapsed());
        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1_000_000; i++) {
            map.get(String.valueOf(i));
        }
        stopwatch.stop();
        System.out.println("Get HashMap Elapsed: " + stopwatch.elapsed());
        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1_000_000; i++) {
            map.remove(String.valueOf(i));
        }
        stopwatch.stop();
        System.out.println("Remove HashMap Elapsed: " + stopwatch.elapsed());

        //Custom test
        stopwatch = Stopwatch.createStarted();
        map = new CustomHashMap<>();
        for (int i = 0; i < 1_000_000; i++) {
            map.put(String.valueOf(i), i);
        }
        stopwatch.stop();
        System.out.println("Put HashMap Elapsed: " + stopwatch.elapsed());
        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1_000_000; i++) {
            map.get(String.valueOf(i));
        }
        stopwatch.stop();
        System.out.println("Get HashMap Elapsed: " + stopwatch.elapsed());
        stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1_000_000; i++) {
            map.remove(String.valueOf(i));
        }
        stopwatch.stop();
        System.out.println("Remove HashMap Elapsed: " + stopwatch.elapsed());
    }
}
