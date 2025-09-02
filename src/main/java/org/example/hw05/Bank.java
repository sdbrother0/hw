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
    private Integer[] accountsObjectSync;
    private Lock[] accountsLock;
    private Random random = new Random();

    public Bank(int numberOfAccounts, long minBalance, long maxBalance) {
        accountsObjectSync = new Integer[numberOfAccounts];
        accountsLock = new ReentrantLock[numberOfAccounts];
        for (int i = 0; i < numberOfAccounts; i++) {
            Integer accNum = i;
            accountsObjectSync[i] = accNum;
            long rndValue = random.nextLong((maxBalance - minBalance) + 1) + minBalance;
            accounts.put(accNum, rndValue);
        }
    }

    public Object getAccountObjectSync(int accountNumber) {
        return accountsObjectSync[accountNumber];
    }

    public Lock getAccountLock(int accountNumber) {
        return accountsLock[accountNumber];
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

    public void transferUnsafe() {
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

    public void transferSynchronized() {
        int from = pickRandomAccountId();
        int to = pickRandomAccountId();
        synchronized (getAccountObjectSync(from)) {
            synchronized (getAccountObjectSync(to)) {
                long value = getAccountBalance(from);
                long x = value == 0 ? 0 : random.nextLong(value);
                // withdraw
                long balFrom = getAccountBalance(from) - x;
                setAccountBalance(from, balFrom);
                // deposit
                long balTo = getAccountBalance(to) + x;
                setAccountBalance(to, balTo);
            }
        }
    }

    public void transferReentrantLock() {
        int from = pickRandomAccountId();
        int to = pickRandomAccountId();
        Lock accFromLock = getAccountLock(from);
        Lock accToLock = getAccountLock(to);
        try {
            accFromLock.lock();
            accToLock.lock();
            long value = getAccountBalance(from);
            long x = value == 0 ? 0 : random.nextLong(value);
            // withdraw
            long balFrom = getAccountBalance(from) - x;
            setAccountBalance(from, balFrom);
            // deposit
            long balTo = getAccountBalance(to) + x;
            setAccountBalance(to, balTo);
        } finally {
            accFromLock.unlock();
            accToLock.unlock();
        }
    }

    public static void threadUnsafeTest(SafeType safeType) throws InterruptedException {
        Bank bank = new Bank(200, 0L, 1_000L);
        BigInteger initialTotal = bank.getSumOfAllAccounts();
        System.out.println("Initial total: " + initialTotal);
        Runnable runnable = () -> {
            switch (safeType) {
                case UNSAFE -> bank.transferUnsafe();
                case SYNCHRONIZED -> bank.transferSynchronized();
                case REENTRANT_LOCK -> bank.transferReentrantLock();
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
