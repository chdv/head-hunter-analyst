package com.dch.app.analyst;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public interface JobFormatter {

    void formatJobs(List<JobEntity> list, OutputStream stream) throws IOException;
}
