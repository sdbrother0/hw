package org.example;

import org.example.hw02.FibonacciAlgorithms;

public class Main {
    public static void main(String[] args) {
        System.out.printf("fibonacciRecursive: %d%n", FibonacciAlgorithms.fibonacciRecursive(10));
        System.out.printf("fibonacciIterative: %d%n", FibonacciAlgorithms.fibonacciIterative(10));
    }
}