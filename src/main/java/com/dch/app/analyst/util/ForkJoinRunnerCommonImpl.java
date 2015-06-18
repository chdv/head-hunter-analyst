package com.dch.app.analyst.util;

import com.dch.app.analyst.AnalystException;
import com.dch.app.analyst.AnalystFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Реализация на базе стандартного фреймворка для работы с многопоточностью
 * Created by Дмитрий on 12.06.2015.
 */
public class ForkJoinRunnerCommonImpl implements ForkJoinRunner {

    private Logger logger = LoggerFactory.getLogger(ForkJoinRunnerCommonImpl.class);

    private ExecutorService pool = null;

    private List<Future<?>> futureList = AnalystFactory.createList();

    public ForkJoinRunnerCommonImpl(ExecutorService pool) {
        this.pool = pool;
    }

    public void execute(Runnable runnable) {
        futureList.add(pool.submit(runnable));
    }

    public void awaitJobsDone() {
        for(Future<?> future : futureList) {
            try {
                future.get();
            } catch (InterruptedException ignored) {
                /*NOP*/
            } catch (ExecutionException e) {
                throw new AnalystException(e);
            }
        }
    }

}
