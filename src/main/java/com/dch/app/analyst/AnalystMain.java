package com.dch.app.analyst;

import com.dch.app.analyst.format.JobFormatter;
import com.dch.app.analyst.parser.JobEntity;
import com.dch.app.analyst.parser.JobParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Дмитрий on 08.06.2015.
 */
public class AnalystMain {

    private static Logger logger = LoggerFactory.getLogger(AnalystMain.class);

    private JobFormatter formatter = AnalystFactory.createJobFormatter();

    private JobParser parser = AnalystFactory.createJobParser();

    private static String OUT_DIR = "out";

    private static String ARCHIVE_DIR = OUT_DIR + File.separator + "archive";

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
     * и если указано в конфигурации, открывает сохраненные файла броузером.
     *
     * @throws IOException
     */
    private static void savePagesToLocalFiles() throws IOException {
        AnalystMain main = new AnalystMain();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String dir = OUT_DIR + File.separator + format.format(new Date());
        File dirFile = new File(dir);
        dirFile.mkdirs();
        for(String world : AnalystConfiguration.getKeyWorlds()) {
            File newFile = new File(dir  + File.separator + world + ".html");
            main.readSite(world, newFile);
            if(AnalystConfiguration.isOpenInBrowser()) {
                Desktop.getDesktop().browse(newFile.toURI());
            }
        }
        createZipArchive(dirFile);
    }

    private static void createZipArchive(File dirFile) throws IOException {
        File dir = new File(ARCHIVE_DIR);
        String zipFileName = dirFile.getName().substring(0, 10);
        dir.mkdirs();
        for(File f : new File(ARCHIVE_DIR).listFiles()) {
            if(f.getName().startsWith(zipFileName)) {
                logger.debug("today archive file already exists");
                return;
            }
        }
        File zipFile = new File(dir.getPath() + File.separator + zipFileName + ".zip");
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile));
        for(File f : dirFile.listFiles()) {
            ZipEntry entry = new ZipEntry(f.getName());
            FileInputStream fis = new FileInputStream(f);
            zout.putNextEntry(entry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zout.write(bytes, 0, length);
            }
            zout.closeEntry();
            fis.close();
        }
        zout.close();
        logger.debug("make archive {}", zipFile.getAbsolutePath());
    }
}
