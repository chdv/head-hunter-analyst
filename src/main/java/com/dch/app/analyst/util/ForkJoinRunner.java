package com.dch.app.analyst.util;

import java.util.concurrent.Executor;

/**
 * Базовый интерфейс для сервиса исполнения задач и последующего ожидания их выполнения
 * Created by Дмитрий on 14.06.2015.
 */
public interface ForkJoinRunner extends Executor {

    void awaitJobsDone();
}
