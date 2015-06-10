package com.dch.app.analyst;

import com.dch.app.analyst.format.JobFormatter;
import com.dch.app.analyst.parser.FileJobParser;
import com.dch.app.analyst.parser.JobEntity;
import com.dch.app.analyst.parser.JobParser;
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

    public AnalystTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( AnalystTest.class );
    }

    public void testFromLocalFile() throws IOException {
        String keyWorld = AnalystConfiguration.getKeyWorlds().get(0);
        List<JobEntity> jobs = parser.readJobs(keyWorld);
        logger.debug("job size = {}", jobs.size());
        assertTrue(jobs.size() == 841 );
        String resultFileName = keyWorld + ".html";
        File out = new File(resultFileName);
        formatter.formatJobs(keyWorld, jobs, new FileOutputStream(resultFileName));
        out.delete();
    }
}
