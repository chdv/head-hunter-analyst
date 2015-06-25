package com.dch.app.analyst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ִלטענטי on 12.06.2015.
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
            while (run && !Thread.currentThread().isInterrupted()) {
                try {
                    Runnable executionRunnable = runQueue.take();
                    if(executionRunnable!=stopRunnable) {
                        executionRunnable.run();
                    }
                } catch (InterruptedException e) {
                    run = false;
                } catch (RuntimeException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private Runnable stopRunnable = () -> {
        /* NOP */
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
