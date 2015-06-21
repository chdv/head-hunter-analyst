package com.dch.app.analyst;

import com.dch.app.analyst.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public final class AnalystFactory {

    private static Logger logger = LoggerFactory.getLogger(AnalystFactory.class);

    private static volatile ThreadPool mainThreadPool = null;

    private AnalystFactory() {}

    private static ThreadPool createThreadPool() {
        int poolSize = Runtime.getRuntime().availableProcessors() * AnalystConfiguration.getThreadsCountPerCore();
        logger.debug("create ThreadPool on " + poolSize + " threads");
        return new FixedThreadPool(poolSize);
    }

    private static ThreadPool getMainThreadPool() throws AnalystException {
        if (mainThreadPool == null) {
            synchronized (AnalystFactory.class) {
                if (mainThreadPool == null) {
                    mainThreadPool = createThreadPool();
                }
            }
        }
        return mainThreadPool;
    }

    public static <T> List<T> createList() {
        return new ArrayList<T>();
    }

    public static <T> List<T> createList(Collection c) {
        return new ArrayList<T>(c);
    }

    public static ForkJoinRunner createForkJoinRunner() {
        return new ForkJoinRunnerImpl(getMainThreadPool());
    }

    public static void shutDown() {
        if(mainThreadPool!=null) {
            mainThreadPool.stop();
        }
        if(executorService!=null) {
            executorService.shutdown();
        }
    }

    private static volatile ExecutorService executorService = null;

    private static ExecutorService createExecutorService() {
        int poolSize = Runtime.getRuntime().availableProcessors() * AnalystConfiguration.getThreadsCountPerCore();
        logger.debug("create ExecutorService on " + poolSize + " threads");
        return Executors.newFixedThreadPool(poolSize);
    }

    private static ExecutorService getMainExecutorService() throws AnalystException {
        if (executorService == null) {
            synchronized (AnalystFactory.class) {
                if (executorService == null) {
                    executorService = createExecutorService();
                }
            }
        }
        return executorService;
    }

    public static ForkJoinRunner createCommonForkJoinRunner() {
        return new ForkJoinRunnerBaseImpl(getMainExecutorService());
    }

}
