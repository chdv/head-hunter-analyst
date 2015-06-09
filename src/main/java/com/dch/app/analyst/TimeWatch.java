package com.dch.app.analyst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

/** Класс для оценки времени исполнения блока
 * Created by Дмитрий on 08.06.2015.
 */
public class TimeWatch {

    Logger logger = LoggerFactory.getLogger(TimeWatch.class);

    private static final TimeUnit CURRENT_TIME_UNIT = TimeUnit.NANOSECONDS;

    long startTime = 0;
    long endTime = 0;

    TimeWatch() {
        startTime = getCurrentTime();
    }

    public void stop() {
        endTime = getCurrentTime();
    }

    private long getCurrentTime() {
        return System.nanoTime();
    }

    public void print() {
        print(CURRENT_TIME_UNIT);
    }
    public void print(TimeUnit unit) {
        if(endTime == 0) {
            stop();
        }
        if(unit != CURRENT_TIME_UNIT) {
            logger.debug("{}", unit.convert(endTime - startTime, CURRENT_TIME_UNIT));
        } else {
            logger.debug("{}", endTime - startTime);
        }
    }

    public void printMicro() {
        print(TimeUnit.MICROSECONDS);
    }

}
