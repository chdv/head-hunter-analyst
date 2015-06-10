package com.dch.app.analyst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ִלטענטי on 08.06.2015.
 */
public class AnalystMain {

    Logger logger = LoggerFactory.getLogger(AnalystMain.class);

    private JobFormatter formatter = AnalystFactory.createJobFormatter();

    private JobParser parser = AnalystFactory.createJobParser();

    public void readSite(String keyWord, File file) throws IOException {
        List<JobEntity> jobs = parser.readJobs(keyWord);
        formatter.formatJobs(keyWord, jobs, new FileOutputStream(file));
        logger.debug("save results to \"{}\"", file.getAbsolutePath());
    }

    public static void main(String args[]) throws IOException {
        AnalystMain main = new AnalystMain();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String dir = "out" + File.separator + format.format(new Date());
        new File(dir).mkdirs();
        for(String world : AnalystConfiguration.getKeyWorlds()) {
            File newFile = new File(dir  + File.separator + world + ".html");
            main.readSite(world, newFile);
            Desktop.getDesktop().browse(newFile.toURI());
        }
    }
}
