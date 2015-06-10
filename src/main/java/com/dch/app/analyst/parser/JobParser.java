package com.dch.app.analyst.parser;

import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public interface JobParser {

    List<JobEntity> readJobs(String keyWord) throws JobParserException;

}
