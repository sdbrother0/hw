package org.example;

import org.example.hw02.arr.ArrayOperations;
import org.example.hw02.fib.FibonacciAlgorithms;
import org.example.hw02.lomb.examples.LombokExamples;
import org.example.hw02.tst.utils.CustomTestRunner;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Part 1
        LombokExamples.init();

        //Part 2
        System.out.printf("fibonacciRecursive: %d%n", FibonacciAlgorithms.fibonacciRecursive(8));
        System.out.printf("fibonacciIterative: %d%n", FibonacciAlgorithms.fibonacciIterative(8));
        FibonacciAlgorithms fibMem = new FibonacciAlgorithms();
        System.out.printf("fibonacciMemorized: %d%n", fibMem.fibonacciMemorized(8));

        //Part 3
        ArrayOperations.run();

        //Part 4
        CustomTestRunner.run("org.example.hw02.tst.tests");
    }

}