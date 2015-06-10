package com.dch.app.analyst.format;

import com.dch.app.analyst.parser.JobEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public interface JobFormatter {

    void formatJobs(String title, List<JobEntity> list, OutputStream stream) throws IOException;
}
