package org.example.hw02.fib;

import java.util.HashMap;
import java.util.Map;

public class FibonacciAlgorithms {

    private Map<Integer, Long> cache = new HashMap<>();

    public static long fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }

    public long fibonacciMemorized(int n) {
        if (n <= 1) {
            return n;
        }
        if (!cache.containsKey(n - 1 )) {
            cache.put(n - 1, fibonacciMemorized(n - 1));
        }
        if (!cache.containsKey(n - 2 )) {
            cache.put(n - 2, fibonacciMemorized(n - 2));
        }
        return cache.get(n - 1) + cache.get(n - 2);
    }

    public static long fibonacciIterative(int n) {
        long x = 0;
        long y = 1;
        if (n == 0) {
            return x;
        }
        long sum;
        for (int i = 2; i <= n; i++) {
            sum = x + y;
            x = y;
            y = sum;
        }
        return y;
    }
}
