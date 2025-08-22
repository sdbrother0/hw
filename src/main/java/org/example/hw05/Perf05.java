package org.example.hw05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Perf05 {

    private short[] array = new short[100_000_000];

    public Perf05() {
        // 100_000_000 not enough to see effect in my system. Use 1_000_000_000
        array = new short[100_000_000];
        //Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            //array[i] = (short) (random.nextInt((Short.MAX_VALUE - Short.MIN_VALUE) + 1) + Short.MIN_VALUE);
            array[i] = 1;
        }
    }

    public long sumWithParallelStream(int threadsCount) {
        IntStream intStream = IntStream.range(0, array.length);
        /* We can use custom ForkJoinPool for Java Stream API or just set System.property:
         * System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(threadsCount));
         * and return:
         * return intStream.parallel().map(index -> array[index]).sum();
         */
        try (ForkJoinPool customThreadPool = new ForkJoinPool(threadsCount)) {
            return customThreadPool.submit(() -> intStream
                .parallel()
                .map(index -> array[index])
                .sum()
            ).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long sumWithParallelThreads(int threadsCount) {
        int partSize = array.length / threadsCount;
        int partCount = array.length % threadsCount;
        int from = 0;
        List<Future<Long>> futureList = new ArrayList<>();
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadsCount)) {
            for (int i = 0; i < threadsCount; i++) {
                int to = from + partSize + (i < partCount ? 1 : 0) - 1;
                final int finalFrom = from;
                futureList.add(executorService.submit(() -> {
                    long sum = 0;
                    for (int partIndex = finalFrom; partIndex <= to; partIndex++) {
                        sum += array[partIndex];
                    }
                    return sum;
                }));
                from = to + 1;
            }
        }
        long result = 0;
        for (Future<Long> future : futureList) {
            try {
                result += future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public void testSumWithParallelStream(int threadsCount) {
        long start = System.nanoTime();
        long sum = sumWithParallelStream(threadsCount);
        long duration = System.nanoTime() - start;
        System.out.printf("Thread count: %s, method: umWithParallelStream, Result: %s, Time: %s, nanoseconds", String.format("%04d", threadsCount), sum, String.format("%,d", duration));
        System.out.println();
    }

    public void testSumWithParallelThreads(int threadsCount) {
        long start = System.nanoTime();
        long sum = sumWithParallelThreads(threadsCount);
        long duration = System.nanoTime() - start;
        System.out.printf("Thread count: %s, method: sumWithParallelThreads, Result: %s, Time: %s, nanoseconds", String.format("%04d", threadsCount), sum, String.format("%,d", duration));
        System.out.println();
    }

    public void testVirtualThreads() throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryStart = runtime.totalMemory() - runtime.freeMemory();
        long start = System.nanoTime();
        Runnable runnable = () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 8_000; i++) {
            threads.add(Thread.ofVirtual().start(runnable));
        }
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }
        long duration = System.nanoTime() - start;
        long usedMemoryFinish = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("VirtualThreads Time: %s, UsedMemory: %s",
            String.format("%,d nanoseconds", duration),
            String.format("%,d bytes", usedMemoryFinish - usedMemoryStart));
        System.out.println();
    }

    public void testPlatformThreads() throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryStart = runtime.totalMemory() - runtime.freeMemory();
        long start = System.nanoTime();
        Runnable runnable = () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 8_000; i++) {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }
        for (int i = 0; i < 8_000; i++) {
            threads.get(i).join();
        }
        long duration = System.nanoTime() - start;
        long usedMemoryFinish = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("PlatformThreads Time: %s, UsedMemory: %s",
            String.format("%,d nanoseconds", duration),
            String.format("%,d bytes", usedMemoryFinish - usedMemoryStart));
        System.out.println();
    }
}
