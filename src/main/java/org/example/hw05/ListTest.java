package org.example.hw05;

import org.example.hw01.CustomList;

import java.util.List;
import java.util.Map;

public class ListTest {

    private final Map<Integer, List<Integer>> typeMap = Map.of(
        0, new CustomList<>(),
        1, new SynchronizedList<>(new CustomList<>()),
        2, new ReentrantReadWriteLockList<>(new CustomList<>())
    );

    public void test(int type) throws InterruptedException {
        List<Integer> list = typeMap.get(type);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                list.add(i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                list.add(i);
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(typeMap.get(type).getClass().getName() + " " + list.size() + " " + (list.size() == 2_000_000 ? "✅" : "❌"));
    }
}
