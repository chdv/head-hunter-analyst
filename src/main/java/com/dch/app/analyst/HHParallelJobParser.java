package com.dch.app.analyst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public class HHParallelJobParser extends HHJobParser {

    Logger logger = LoggerFactory.getLogger(HHParallelJobParser.class);

    private ExecutorService executor = AnalystFactory.createExecutorService();

    protected List<JobEntity> parsePage(InputStream input) throws IOException, InterruptedException {
        Queue result = new ConcurrentLinkedQueue();
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(input, AnalystConfiguration.getEncoding()));
        try {
            String line = null;
            while((line = reader.readLine())!=null) {
                if(line.contains(jobBlockStart)) {
                    executor.submit(new ParseRunnable(result, line));
                }
            }
        } finally {
            reader.close();
        }
        return AnalystFactory.createList(result);
    }

    @Override
    protected void processEnding() throws Exception {
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    private class ParseRunnable implements Runnable {

        private Collection result;
        private String line;

        ParseRunnable(Collection r, String l) {
            line = l;
            result = r;
        }

        public void run() {
            result.add( parseLine(line));
        }
    }
}
