package com.dch.app.analyst;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public class HtmlJobFormatter implements JobFormatter {

    private static final String outputBegin = AnalystConfiguration.getOutputBegin();

    private static final String outputEnd = AnalystConfiguration.getOutputEnd();

    private static final String delimeter = AnalystConfiguration.getDelimeter();

    public void formatJobs(List<JobEntity> jobs, OutputStream stream) throws IOException {
        BufferedWriter writer =
                new BufferedWriter(
                        new OutputStreamWriter(
                                stream,
                                AnalystConfiguration.getEncoding()));
        try {
            writer.write(String.format(outputBegin, jobs.size()));
            for (JobEntity entity : jobs) {
                writer.write(entity.getJobInfo());
                writer.write(delimeter);
            }
            writer.write(outputEnd);
        } finally {
            writer.close();
        }
    }
}
