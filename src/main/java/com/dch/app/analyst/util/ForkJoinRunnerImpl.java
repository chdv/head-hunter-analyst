package com.dch.app.analyst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Моя реализация утилиты для работы с потоками.
 * В устоявшемся режиме показывает производительность в полтора раза лучшую нежели стандартная реализация
 * Created by Дмитрий on 12.06.2015.
 */
public class ForkJoinRunnerImpl implements ForkJoinRunner {

    private Logger logger = LoggerFactory.getLogger(ForkJoinRunnerImpl.class);

    private volatile Set<RunnerTask> tasksSet = new ConcurrentHashSet<RunnerTask>();

    private ReentrantLock mainLock = new ReentrantLock();

    private Condition whaitDoneLock = mainLock.newCondition();

    private ThreadPool pool = null;

    public ForkJoinRunnerImpl(ThreadPool pool) {
        this.pool = pool;
    }

    public void execute(Runnable runnable) {
        RunnerTask task = new RunnerTask();
        tasksSet.add(task);
        pool.execute(() -> {
            runnable.run();
            tasksSet.remove(task);
            if(tasksSet.isEmpty()) {
                mainLock.lock();
                try {
                    whaitDoneLock.signalAll();
                } finally {
                    mainLock.unlock();
                }
            }
        });
    }

    public void awaitJobsDone() {
        mainLock.lock();
        try {
            while(!tasksSet.isEmpty()) {
                try {
                    whaitDoneLock.await();
                } catch (InterruptedException ignore) {
                }
            }
        } finally {
            mainLock.unlock();
        }
    }

    private class RunnerTask {

        private long time = 0;

        RunnerTask() {
            time = System.nanoTime();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RunnerTask that = (RunnerTask) o;

            return time == that.time;
        }

        @Override
        public int hashCode() {
            return (int) (time ^ (time >>> 32));
        }

        @Override
        public String toString() {
            return String.valueOf(time);
        }
    }

}
