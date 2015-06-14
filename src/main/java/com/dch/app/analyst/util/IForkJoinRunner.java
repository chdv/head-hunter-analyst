package com.dch.app.analyst.util;

import java.util.concurrent.Executor;

/**
 * Created by ִלטענטי on 14.06.2015.
 */
public interface IForkJoinRunner extends Executor {

    void awaitJobsDone();
}
