package com.dch.app.analyst.util;

/**
 * Created by ������� on 21.06.2015.
 */
public class SingleThreadExecutorImpl extends FixedThreadPool implements  SingleThreadExecutor {

    public SingleThreadExecutorImpl() {
        super(1);
    }
}
