package org.example;

import com.google.common.base.Stopwatch;
import org.example.hw06.CustomExecutorService;
import org.example.hw06.CustomWebServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Test 1: Performance comparison
        testPerformanceComparison();
        // Test 2: Concurrent task execution
        testConcurrentExecution();
        // Test 3: Shutdown behavior
        testShutdownBehavior();

        CustomWebServer customWebServer = new CustomWebServer(8080, 100, true);
        customWebServer.start();
        //customWebServer.stop();
        Thread.sleep(60_000); // Run for 1 minute
    }

    public static void testPerformanceComparison() throws InterruptedException {
        /*
        Create two executors: one with virtual threads, one with platform threads
        Submit 10,000 tasks that sleep for 10ms each
        Measure and compare execution time and memory usage
        Test with different pool sizes: 10, 50, 100, 500
         */
        for (boolean useVirtualThreads : new boolean[]{true, false}) {
            for (int poolSize : new int[]{10, 50, 100, 500}) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                ExecutorService executorService = new CustomExecutorService(poolSize, useVirtualThreads);
                for (int i = 0; i < 10_000; i++) {
                    executorService.submit(() -> {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                executorService.shutdown();
                long usedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
                executorService.awaitTermination(30, TimeUnit.SECONDS);
                stopwatch.stop();
                System.out.printf("Pool size: %s; Elapsed ms: %s; UseVirtualThreads: %s; mem MB: %s\n", poolSize, stopwatch.elapsed().toMillis(), useVirtualThreads, usedMem);
            }
        }
    }

    public static void testConcurrentExecution() throws InterruptedException {
        /*
        Submit 1000 tasks that increment a shared AtomicInteger
        Verify all tasks complete and counter reaches 1000
        Test with both virtual and platform thread versions
         */
        for (boolean useVirtualThreads : new boolean[]{true, false}) {
            ExecutorService executorService = new CustomExecutorService(10, useVirtualThreads);
            AtomicInteger atomicInteger = new AtomicInteger(0);
            for (int i = 0; i < 1000; i++) {
                executorService.submit(() -> {
                    atomicInteger.incrementAndGet();
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
            System.out.printf("Count: %s; UseVirtualThreads %s\n", atomicInteger.get(), useVirtualThreads);
        }
    }

    public static void testShutdownBehavior() throws InterruptedException {
        for (boolean useVirtualThreads : new boolean[]{true, false}) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            ExecutorService executorService = new CustomExecutorService(100, useVirtualThreads);
            for (int i = 0; i < 100; i++) {
                executorService.submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
            stopwatch.stop();
            System.out.printf("Elapsed ms: %s; UseVirtualThreads %s\n", stopwatch.elapsed().toMillis(), useVirtualThreads);
        }
    }
}