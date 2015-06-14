package com.dch.app.analyst.writer;

import com.dch.app.analyst.parser.JobEntity;

import java.io.IOException;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public interface JobsWriter {

    void writeTop() throws IOException;

    void writeJob(JobEntity entity) throws IOException;

    void writeBottom() throws IOException;

    int getCount();

}
