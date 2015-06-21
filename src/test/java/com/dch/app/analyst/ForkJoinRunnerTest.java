package com.dch.app.analyst;

import com.dch.app.analyst.util.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Created by ִלטענטי on 12.06.2015.
 */
public class ForkJoinRunnerTest extends TestCase {

    Logger logger = LoggerFactory.getLogger(ForkJoinRunnerTest.class);

    public ForkJoinRunnerTest(String testName) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( ForkJoinRunnerTest.class );
    }

    public void testRunner() throws Exception {
        ForkJoinRunner runner = new ForkJoinRunnerImpl(new FixedThreadPool(20));
        Set<Integer> numberSet = new ConcurrentHashSet<Integer>();
        for(int i = 0; i<100; i++) {
            final int finalI = i;
            runner.execute(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            /* NOP */
                        }
                    }).start();
                    try {
                        Thread.sleep(10 * finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    numberSet.add(finalI);
                }
            });
        }
        runner.awaitJobsDone();
        assertTrue(numberSet.size() == 100);
        for(int i = 0; i<100; i++) {
            assertTrue(numberSet.remove(i));
        }
    }

    public void testCompareExecutors() {
        ForkJoinRunner runner = new ForkJoinRunnerImpl(new FixedThreadPool(20));
        ForkJoinRunner service = new ForkJoinRunnerBaseImpl(Executors.newFixedThreadPool(20));
        Runnable testRunnable = new Runnable() {
            @Override
            public void run() {
                long k = 1;
                for(int i = 1; i<100; i++ ){
                    k = k*i;
                }
            }
        };

        for(int i = 1; i<50; i++ ) {
            logger.debug("run new test");

            TimeWatch watch = new TimeWatch("my");
            for(int k = 1; k<5_000; k++ ){
                runner.execute(testRunnable);
            }
            runner.awaitJobsDone();
            watch.printMicro();

            watch = new TimeWatch("common");
            for(int k = 1; k<5_000; k++ ){
                service.execute(testRunnable);
            }
            service.awaitJobsDone();
            watch.printMicro();
        }

    }

}