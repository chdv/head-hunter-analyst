package com.dch.app.analyst.util;

import java.util.concurrent.Executor;

/**
 * Created by ������� on 12.06.2015.
 */
public interface ThreadPool extends Executor {

    void stop();

}
