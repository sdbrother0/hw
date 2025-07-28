package org.example;

import org.example.hw02.arr.ArrayOperations;
import org.example.hw02.fib.FibonacciAlgorithms;
import org.example.hw02.lomb.examples.LombokExamples;
import org.example.hw02.tst.utils.CustomTestRunner;

public class Main {
    public static void main(String[] args) {
        //Part 1
        LombokExamples.run();

        //Part 2
        FibonacciAlgorithms.run();
        //fibonacciRecursive(50), duration: 29 999 ms - so long time

        //Part 3
        ArrayOperations.run();

        //Part 4
        CustomTestRunner.run("org.example.hw02.tst.tests");
    }

}