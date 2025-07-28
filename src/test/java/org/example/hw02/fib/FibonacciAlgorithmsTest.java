package org.example.hw02.fib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FibonacciAlgorithmsTest {

    @ParameterizedTest(name = "test: \"{0}\", expected: \"{1}\"")
    @CsvSource({
            "0, 0",
            "1, 1",
            "2, 1",
            "3, 2",
            "4, 3",
            "5, 5",
            "6, 8",
            "7, 13",
            "8, 21",
            "9, 34",
            "10, 55",
            "11, 89",
            "12, 144",
            "13, 233",
            "14, 377",
            "15, 610",
            "16, 987",
            "17, 1597",
            "18, 2584",
            "19, 4181",
            "20, 6765",
            "21, 10946",
            "22, 17711",
            "23, 28657",
            "24, 46368",
            "25, 75025",
            "26, 121393",
            "27, 196418",
            "28, 317811",
            "29, 514229",
            "30, 832040",
            "31, 1346269",
            "32, 2178309",
            "33, 3524578",
            "34, 5702887",
            "35, 9227465"
    })
    void fibonacciRecursive(int n, long expected) {
        System.out.println("----------------------------------------------------------");
        Runtime runtime = Runtime.getRuntime();
        long freeMem = runtime.freeMemory();
        long currentTime = System.currentTimeMillis();
        Assertions.assertEquals(expected, FibonacciAlgorithms.fibonacciRecursive(n));
        System.out.printf("FibonacciRecursive n: %s, duration: %sms memory: %s, %n", n, System.currentTimeMillis() - currentTime, freeMem - runtime.freeMemory());

        FibonacciAlgorithms fibonacciAlgorithms = new FibonacciAlgorithms();
        freeMem = runtime.freeMemory();
        currentTime = System.currentTimeMillis();
        Assertions.assertEquals(expected, fibonacciAlgorithms.fibonacciMemorized(n));

        System.out.printf("FibonacciMemorized n: %s, duration: %sms memory: %s, %n", n, System.currentTimeMillis() - currentTime, freeMem - runtime.freeMemory());
        freeMem = runtime.freeMemory();
        currentTime = System.currentTimeMillis();
        Assertions.assertEquals(expected, FibonacciAlgorithms.fibonacciIterative(n));
        System.out.printf("FibonacciIterative n: %s, duration: %sms memory: %s, %n", n, System.currentTimeMillis() - currentTime, freeMem - runtime.freeMemory());
    }
}