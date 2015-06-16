package com.dch.app.analyst;

import com.dch.app.analyst.parser.SiteInfo;
import com.dch.app.analyst.parser.SiteParser;
import com.dch.app.analyst.util.FileUtils;
import com.dch.app.analyst.util.ForkJoinRunner;
import com.dch.app.analyst.writer.HtmlJobsWriter;
import com.dch.app.analyst.writer.JobsWriter;
import com.dch.app.analyst.writer.Renamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Дмитрий on 08.06.2015.
 */
public class AnalystMain {

    private static Logger logger = LoggerFactory.getLogger(AnalystMain.class);

    private static final String OUT_DIR = "out";

    private static final String ARCHIVE_DIR = OUT_DIR + File.separator + "archive";

    private AnalystMain() {
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
        if(dirFile.exists()) {
            FileUtils.deleteRecursive(dirFile);
        }
        dirFile.mkdirs();
        File archiveDir = new File(ARCHIVE_DIR);
        archiveDir.mkdirs();

        ForkJoinRunner runner = AnalystFactory.createForkJoinRunner();
        for(SiteInfo site : AnalystConfiguration.getSites()) {
            runner.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        main.processSite(site, dir);
                    } catch (IOException e) {
                        throw new AnalystException(e);
                    }
                }
            });
        }
        runner.awaitJobsDone();
        logger.debug("end work");
        AnalystFactory.getMainThreadPool().stop();

        FileUtils.createZipArchive(dirFile, archiveDir);
    }

    private void processSite(SiteInfo site, final String dir) throws IOException {
        ForkJoinRunner runner = AnalystFactory.createForkJoinRunner();

        for(String world : AnalystConfiguration.getKeyWorlds()) {
            runner.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        processKeyWorld(site,  dir, world);
                    } catch (IOException e) {
                        throw new AnalystException(e);
                    }
                }
            });
        }
        runner.awaitJobsDone();
    }

    private void processKeyWorld(SiteInfo site, String dir, String keyWord) throws IOException {
        String f = dir  + File.separator + site.getName() + "-" + keyWord;
        File newFile = new File(f);
        // ренеймер добавляет в конец названия файла количество найденных вакансий
        Renamer renamer = new Renamer(f);
        JobsWriter jobsWriter = new HtmlJobsWriter(site, keyWord, newFile, renamer);
        jobsWriter.writeTop();
        SiteParser parser = site.createJobParser();
        parser.setJobsWriter(jobsWriter);
        parser.readJobs(keyWord);
        jobsWriter.writeBottom();
    }


}
