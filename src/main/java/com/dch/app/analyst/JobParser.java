package com.dch.app.analyst;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public interface JobParser {

    List<JobEntity> readJobs(String keyWord) throws JobParserException;

}
