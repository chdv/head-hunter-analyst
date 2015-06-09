package com.dch.app.analyst;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public class AnalystTest extends TestCase {

    Logger logger = LoggerFactory.getLogger(AnalystTest.class);

    private JobFormatter formatter = AnalystFactory.createJobFormatter();

    private JobParser parser = new FileJobParser();

    final String resultFileName = "result-test.html";

    public AnalystTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( AnalystTest.class );
    }

    public void testAnalyst() throws IOException {
        List<JobEntity> jobs = parser.readJobs("java");
        logger.debug("job size = {}", jobs.size());
        assertTrue(jobs.size() == 841 );
        File out = new File(resultFileName);
        formatter.formatJobs(jobs, new FileOutputStream(resultFileName));
        out.delete();
    }
}
