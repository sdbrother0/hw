package org.example.hw05;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Deadlock {

    private Thread thread2;

    public void test1() {
        Object lock1 = new Object();
        Object lock2 = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1: locked lock1");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock2) {
                    System.out.println("Thread 1: locked lock2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2: locked lock2");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock1) {
                    System.out.println("Thread 2: locked lock1");
                }
            }
        });

        thread1.start();
        thread2.start();
    }

    public static synchronized void test2() {
        System.out.println("Locked: " + Thread.currentThread().getName());
        try {
            Thread thread = new Thread(Deadlock::test2);
            thread.start();
            thread.join();
        } catch (Exception exception) {
            //
        }
    }

    public void test3() {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            lock1.lock();
            System.out.println("Thread 1: locked lock1");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lock2.lock();
            System.out.println("Thread 1: locked lock2");
        });

        Thread thread2 = new Thread(() -> {
            lock2.lock();
            System.out.println("Thread 2: locked lock2");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lock1.lock();
            System.out.println("Thread 2: locked lock1");
        });

        thread1.start();
        thread2.start();
    }

    public void test4() {
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1 waiting for Thread 2...");
            try {
                thread2.join();
            } catch (InterruptedException ignored) {
                //...
            }
        });

        thread2 = new Thread(() -> {
            System.out.println("Thread 2 waiting for Thread 1...");
            try {
                thread1.join();
            } catch (InterruptedException ignored) {

            }
        });

        thread1.start();
        thread2.start();
    }
}
