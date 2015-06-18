package com.dch.app.analyst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Класс для оценки времени исполнения блока
 * Created by Дмитрий on 08.06.2015.
 */
public class TimeWatch {

    Logger logger = LoggerFactory.getLogger(TimeWatch.class);

    private static final TimeUnit CURRENT_TIME_UNIT = TimeUnit.NANOSECONDS;

    long startTime = 0;
    long endTime = 0;

    private String name;

    public TimeWatch(String name) {
        startTime = getCurrentTime();
        this.name = name;
    }

    public TimeWatch(){
        this("");
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
            logger.debug("{} {}", name, unit.convert(endTime - startTime, CURRENT_TIME_UNIT));
        } else {
            logger.debug("{} {}", name, endTime - startTime);
        }
    }

    public void printMicro() {
        print(TimeUnit.MICROSECONDS);
    }

}
