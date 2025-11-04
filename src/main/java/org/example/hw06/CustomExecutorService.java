package org.example.hw06;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomExecutorService implements ExecutorService {

    private final BlockingQueue<Runnable> taskQueue;
    private List<Worker> workers;
    private List<Thread> virtualThreads;
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);
    private final boolean useVirtualThreads;
    private static final int POLL_TIMEOUT_MS = 100;

    public CustomExecutorService(int corePoolSize, boolean useVirtualThreads) {
        this.useVirtualThreads = useVirtualThreads;
        taskQueue = new LinkedBlockingQueue<>();
        if (useVirtualThreads) {
            virtualThreads = new CopyOnWriteArrayList<>();
        } else {
            workers = new ArrayList<>();
            for (int i = 0; i < corePoolSize; i++) {
                Worker worker = new Worker();
                worker.start();
                workers.add(worker);
            }
        }
    }

    @Override
    public void shutdown() {
        isShutdown.set(true);
    }

    @Override
    public List<Runnable> shutdownNow() {
        isShutdown.set(true);
        for (Thread worker : workers) {
            worker.interrupt();
        }
        for (Thread thread : virtualThreads) {
            thread.interrupt();
        }
        return new ArrayList<>(taskQueue);
    }

    @Override
    public boolean isShutdown() {
        return isShutdown.get();
    }

    @Override
    public boolean isTerminated() {
        if (useVirtualThreads) {
            return isShutdown.get() && virtualThreads.isEmpty();
        } else {
            return isShutdown.get() && workers.stream().noneMatch(Thread::isAlive);
        }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
        if (useVirtualThreads) {
            for (Thread thread : virtualThreads) {
                long timeLeft = deadline - System.currentTimeMillis();
                if (timeLeft <= 0) {
                    return false;
                }
                thread.join(timeLeft);
             }
        } else {
            for (Thread worker : workers) {
                long timeLeft = deadline - System.currentTimeMillis();
                if (timeLeft <= 0) {
                    return false;
                }
                worker.join(timeLeft);
            }
        }
        return isTerminated();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> future = new FutureTask<>(task);
        execute(future);
        return future;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        FutureTask<T> future = new FutureTask<>(task, result);
        execute(future);
        return future;
    }

    @Override
    public Future<?> submit(Runnable task) {
        FutureTask<?> future = new FutureTask<>(task, null);
        execute(future);
        return future;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        List<Future<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            futures.add(submit(task));
        }
        for (Future<T> future : futures) {
            try {
                future.get(); }
            catch (ExecutionException ignored) {
                //
            }
        }
        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return List.of();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public void execute(Runnable command) {
        if (isShutdown.get()) {
            throw new RejectedExecutionException("Executor is shut down");
        }
        if (useVirtualThreads) {
            Thread thread = Thread.startVirtualThread(() -> {
                try {
                    command.run();
                } finally {
                    virtualThreads.remove(Thread.currentThread());
                }
            });
            virtualThreads.add(thread);
        } else {
            taskQueue.add(command);
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            while (!isShutdown.get() || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.poll(POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        task.run();
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
