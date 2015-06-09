package com.dch.app.analyst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Created by ִלטענטי on 08.06.2015.
 */
public class AnalystMain {

    Logger logger = LoggerFactory.getLogger(AnalystMain.class);

    private JobFormatter formatter = AnalystFactory.createJobFormatter();

    private JobParser parser = AnalystFactory.createJobParser();

    public void readSite(String keyWord) throws IOException {
        List<JobEntity> jobs = parser.readJobs(keyWord);
        formatter.formatJobs(jobs, new FileOutputStream("result-" + keyWord + ".html"));
    }

    public static void main(String args[]) throws IOException {
        AnalystMain main = new AnalystMain();
        main.readSite(AnalystConfiguration.getKeyWorld());
    }
}
