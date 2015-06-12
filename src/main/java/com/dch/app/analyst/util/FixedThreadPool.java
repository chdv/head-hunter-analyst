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
    private PoolWorker[] workers;

    private volatile boolean run = true;

    public FixedThreadPool(int size) {
        this.size = size;
        initWorkers();
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

    private void initWorkers() {
        workers = new PoolWorker[size];
        for (int i = 0; i < size; i++) {
            workers[i] = new PoolWorker();
            workers[i].start();
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            Runnable runnable;
            while (run) {
                try {
                    runnable = runQueue.take();
                    if(runnable!=stopRunnable) {
                        runnable.run();
                    }
                } catch (InterruptedException ignored) {
                    /*NOP*/
                } catch (RuntimeException e) {
                    logger.error(e.getMessage(), e);
                } finally {
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
        } catch (InterruptedException e) {
        }
    }
}
