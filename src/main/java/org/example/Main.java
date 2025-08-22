package org.example;

import org.example.hw05.Bank;
import org.example.hw05.ListTest;
import org.example.hw05.Perf05;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 1. Multithreaded performance
        Perf05 perf05 = new Perf05();
        perf05.testSumWithParallelThreads(1);
        perf05.testSumWithParallelThreads(10);
        perf05.testSumWithParallelThreads(100);
        perf05.testSumWithParallelThreads(1000);

        perf05.testSumWithParallelStream(1);
        perf05.testSumWithParallelStream(10);
        perf05.testSumWithParallelStream(100);
        perf05.testSumWithParallelStream(1000);

        // 2. How heavy is the platform thread
        perf05.testVirtualThreads();
        perf05.testPlatformThreads();

        // 3. Banking simulator
        for (int i = 0; i < 100; i++) {
            Bank.threadUnsafeTest(Bank.SafeType.UNSAFE);
        }
        for (int i = 0; i < 100; i++) {
            Bank.threadUnsafeTest(Bank.SafeType.SYNCHRONIZED);
        }
        for (int i = 0; i < 100; i++) {
            Bank.threadUnsafeTest(Bank.SafeType.REENTRANT_LOCK);
        }

        // 4. Multithreaded CustomList
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            new ListTest().test(0);
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Unsafe duration millis: " + duration);

        start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            new ListTest().test(1);
        }
        duration = System.currentTimeMillis() - start;
        System.out.println("Unsafe duration millis: " + duration);

//        for (int i = 0; i < 100; i++) {
//            new ListTest().test(2);
//        }

        /*
        Deadlock deadlock = new Deadlock();
        deadlock.test1();
        deadlock.test2();
        deadlock.test3();
        deadlock.test4();
        */
    }
}