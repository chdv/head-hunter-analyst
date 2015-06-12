package com.dch.app.analyst;

import com.dch.app.analyst.util.ConcurrentHashSet;
import com.dch.app.analyst.util.ForkJoinRunner;
import com.dch.app.analyst.util.FixedThreadPool;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

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
        ForkJoinRunner runner = new ForkJoinRunner(new FixedThreadPool(20));
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
        runner.await();
        assertTrue(numberSet.size() == 100);
        for(int i = 0; i<100; i++) {
            assertTrue(numberSet.remove(i));
        }
    }
}