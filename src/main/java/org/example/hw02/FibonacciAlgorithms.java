package org.example.hw02;

public class FibonacciAlgorithms {
    public static long fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
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
