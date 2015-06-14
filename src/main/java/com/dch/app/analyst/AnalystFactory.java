package com.dch.app.analyst;

import com.dch.app.analyst.util.FixedThreadPool;
import com.dch.app.analyst.util.ForkJoinRunner;
import com.dch.app.analyst.util.ForkJoinRunnerImpl;
import com.dch.app.analyst.util.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static ThreadPool getMainThreadPool() throws AnalystException {
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

}
