package com.dch.app.analyst;

import com.dch.app.analyst.format.HtmlJobFormatter;
import com.dch.app.analyst.format.JobFormatter;
import com.dch.app.analyst.parser.HHJobParser;
import com.dch.app.analyst.parser.JobParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public final class AnalystFactory {

    private AnalystFactory() {}

    public static <T> List<T> createList() {
        return new ArrayList<T>();
    }

    public static <T> List<T> createList(Collection c) {
        return new ArrayList<T>(c);
    }

    public static JobFormatter createJobFormatter() {
        return new HtmlJobFormatter();
    }

    public static ExecutorService createExecutorService() {
        int poolSize = Runtime.getRuntime().availableProcessors() * 4;
        return Executors.newFixedThreadPool(poolSize);
    }

    public static JobParser createJobParser() {
        return new HHJobParser();
    }

}
