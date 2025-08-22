package org.example.hw05;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {

    public enum SafeType {
        UNSAFE,
        SYNCHRONIZED,
        REENTRANT_LOCK
    }

    private Map<Integer, Long> accounts = new HashMap<>();
    private Random random = new Random();

    public Bank(int numberOfAccounts, long minBalance, long maxBalance) {
        for (int i = 0; i < numberOfAccounts; i++) {
            long rndValue = random.nextLong((maxBalance - minBalance) + 1) + minBalance;
            accounts.put(i, rndValue);
        }
    }

    public int pickRandomAccountId() {
        return random.nextInt(accounts.size());
    }

    public long getAccountBalance(int accountId) {
        return accounts.get(accountId);
    }

    public void setAccountBalance(int accountId, long newBalance) {
        accounts.put(accountId, newBalance);
    }

    public BigInteger getSumOfAllAccounts() {
        BigInteger sum = BigInteger.ZERO;
        for (Long value : accounts.values()) {
            sum = sum.add(BigInteger.valueOf(value));
        }
        return sum;
    }

    public void transfer() {
        int from = pickRandomAccountId();
        int to = pickRandomAccountId();
        long value = getAccountBalance(from);
        long x = value == 0 ? 0 : random.nextLong(value);
        // withdraw
        long balFrom = getAccountBalance(from) - x;
        setAccountBalance(from, balFrom);
        // deposit
        long balTo = getAccountBalance(to) + x;
        setAccountBalance(to, balTo);
    }

    public synchronized void transferSynchronized() {
        transfer();
    }
    public synchronized void transferReentrantLock(Lock lock) {
        try {
            lock.lock();
            transfer();
        } finally {
            lock.unlock();
        }
    }

    public static void threadUnsafeTest(SafeType safeType) throws InterruptedException {
        Bank bank = new Bank(200, 0L, 1_000L);
        BigInteger initialTotal = bank.getSumOfAllAccounts();
        System.out.println("Initial total: " + initialTotal);
        ReentrantLock lock = new ReentrantLock();
        Runnable runnable = () -> {
            switch (safeType) {
                case UNSAFE -> bank.transfer();
                case SYNCHRONIZED -> bank.transferSynchronized();
                case REENTRANT_LOCK -> bank.transferReentrantLock(lock);
            }
        };
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1_000; i++) {
            Thread thread = Thread.ofVirtual().start(runnable);
            threads.add(thread);
        }
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }
        System.out.println("Final total: " + bank.getSumOfAllAccounts());
        if (initialTotal.compareTo(bank.getSumOfAllAccounts()) == 0) {
            System.out.println("✅" + safeType.name());
        } else {
            System.out.println("❌❌❌❌❌❌❌❌❌❌" + safeType.name());
        }
    }

}
