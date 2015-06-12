package com.dch.app.analyst;

import com.dch.app.analyst.format.HtmlJobFormatter;
import com.dch.app.analyst.format.JobFormatter;
import com.dch.app.analyst.parser.HHJobParser;
import com.dch.app.analyst.parser.JobParser;
import com.dch.app.analyst.util.FixedThreadPool;
import com.dch.app.analyst.util.ForkJoinRunner;
import com.dch.app.analyst.util.ThreadPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public final class AnalystFactory {

    private static volatile ThreadPool mainThreadPool = null;

    private AnalystFactory() {}

    private static ThreadPool createThreadPool() {
        int poolSize = Runtime.getRuntime().availableProcessors() * 4;
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

    public static JobFormatter createJobFormatter() {
        return new HtmlJobFormatter();
    }

    public static JobParser createJobParser() {
        return new HHJobParser();
    }

    public static ForkJoinRunner createForkJoinRunner() {
        return new ForkJoinRunner(getMainThreadPool());
    }

}
