package com.dch.app.analyst.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ִלטענטי on 21.06.2015.
 */
public class SingleThreadExecutorBaseImpl implements  SingleThreadExecutor {

    private ExecutorService executorService;

    public SingleThreadExecutorBaseImpl() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void stop() {
        executorService.shutdown();
    }

    @Override
    public void execute(Runnable command) {
        executorService.execute(command);
    }
}
