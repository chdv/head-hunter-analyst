package com.dch.app.analyst;

import org.apache.commons.configuration.XMLConfiguration;

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
