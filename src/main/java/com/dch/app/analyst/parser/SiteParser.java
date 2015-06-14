package com.dch.app.analyst.parser;

import com.dch.app.analyst.writer.JobsWriter;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public interface SiteParser {

    void readJobs(String keyWord) throws SiteParserException;

    void setJobsWriter(JobsWriter jobsWriter);

}
