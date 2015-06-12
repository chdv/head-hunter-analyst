package com.dch.app.analyst;

import com.dch.app.analyst.format.JobFormatter;
import com.dch.app.analyst.parser.JobEntity;
import com.dch.app.analyst.parser.JobParser;
import com.dch.app.analyst.util.FileUtils;
import com.dch.app.analyst.util.ForkJoinRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Дмитрий on 08.06.2015.
 */
public class AnalystMain {

    private static Logger logger = LoggerFactory.getLogger(AnalystMain.class);

    private JobFormatter formatter = AnalystFactory.createJobFormatter();

    private JobParser parser = AnalystFactory.createJobParser();

    private static final String OUT_DIR = "out";

    private static final String ARCHIVE_DIR = OUT_DIR + File.separator + "archive";

    public void readSite(String keyWord, File file) throws IOException {
        List<JobEntity> jobs = parser.readJobs(keyWord);
        formatter.formatJobs(keyWord, jobs, new FileOutputStream(file));
        logger.debug("save results to \"{}\"", file.getAbsolutePath());
    }

    public static void main(String args[]) throws IOException {
        savePagesToLocalFiles();
    }

    /**
     * Сохраняет содержимое выборки по ключевым словам с сайта hh.ru в локальные файлы, создает архив,
     * и если указано в конфигурации, открывает сохраненные файлы броузером.
     *
     * @throws IOException
     */
    private static void savePagesToLocalFiles() throws IOException {
        AnalystMain main = new AnalystMain();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String dir = OUT_DIR + File.separator + format.format(new Date());
        File dirFile = new File(dir);
        dirFile.mkdirs();
        main.processKeyWorlds(dir);
        File dirTo = new File(ARCHIVE_DIR);
        dirTo.mkdirs();
        FileUtils.createZipArchive(dirFile, dirTo);
    }

    private void processKeyWorlds(final String dir) throws IOException {
        ForkJoinRunner runner = AnalystFactory.createForkJoinRunner();
        for(String world : AnalystConfiguration.getKeyWorlds()) {
            runner.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        processKeyWorld(dir, world);
                    } catch (IOException e) {
                        throw new AnalystException(e);
                    }
                }
            });
        }
        runner.await();
        logger.debug("end work");
        AnalystFactory.getMainThreadPool().stop();
    }

    /*private void processKeyWorlds(final String dir) throws IOException {
        for(String world : AnalystConfiguration.getKeyWorlds()) {
            processKeyWorld(dir, world);
        }
    }*/

    private void processKeyWorld(String dir, String world) throws IOException {
        File newFile = new File(dir  + File.separator + world + ".html");
        readSite(world, newFile);
        if(AnalystConfiguration.isOpenInBrowser()) {
            Desktop.getDesktop().browse(newFile.toURI());
        }
    }

}
