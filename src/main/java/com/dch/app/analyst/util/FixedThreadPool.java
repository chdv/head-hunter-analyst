package com.dch.app.analyst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Изначально в программе использовался класс java.util.concurrent.ThreadPoolExecutor, но из-за моей ошибки в коде
 * класса ForkJoinRunner программа вела себя довольно удивительно и непредсказуемо. Поэтому я реализовал ThreadPool сам,
 * а потом уже нашел ошибку. Возвращать ThreadPoolExecutor не стал, просто моя реализация вроде работаект корректно,
 * а реализация Дага Ли по сравнению с моей довольно перегружена не нужным в настоящий момент функционалом.
 *
 * Created by Дмитрий on 12.06.2015.
 */
public class FixedThreadPool implements ThreadPool {

    private Logger logger = LoggerFactory.getLogger(FixedThreadPool.class);

    private int size;
    private volatile BlockingQueue<Runnable> runQueue = new LinkedBlockingQueue<Runnable>();

    private Thread[] threads;

    private volatile boolean run = true;

    public FixedThreadPool(int size) {
        this.size = size;
        initThreads();
    }

    public void execute(Runnable run) {
        try {
            runQueue.put(run);
        } catch (InterruptedException ignored) {
            /*NOP*/
        }
    }

    public int getQueueSize() {
        return runQueue.size();
    }

    private void initThreads() {
        threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(new PoolWorker());
            threads[i].start();
        }
    }

    private class PoolWorker implements Runnable {
        public void run() {
            while (run) {
                try {
                    Runnable executionRunnable = runQueue.take();
                    if(executionRunnable!=stopRunnable) {
                        executionRunnable.run();
                    }
                } catch (InterruptedException ignored) {
                    /*NOP*/
                } catch (RuntimeException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private Runnable stopRunnable = new Runnable() {
        @Override
        public void run() {
            /* NOP */
        }
    };

    public boolean isRun() {
        return run;
    }

    public void stop() {
        run = false;
        try {
            for (int i = 0; i < size; i++) {
                runQueue.put(stopRunnable);
            }
        } catch (InterruptedException ignored) {
            /* NOP */
        }
    }
}
