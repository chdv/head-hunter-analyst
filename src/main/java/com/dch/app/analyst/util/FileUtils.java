package com.dch.app.analyst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by ִלטענטי on 10.06.2015.
 */
public final class FileUtils {

    private FileUtils() {}

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static void createZipArchive(File dirFrom, File dirTo) throws IOException {
        String zipFileName = dirFrom.getName().substring(0, 10);
        for(File f : dirTo.listFiles()) {
            if(f.getName().startsWith(zipFileName)) {
                logger.debug("today archive file already exists");
                return;
            }
        }
        File zipFile = new File(dirTo.getPath() + File.separator + zipFileName + ".zip");
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile));
        for(File f : dirFrom.listFiles()) {
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

    public static boolean deleteRecursive(File path) throws FileNotFoundException {
        if (!path.exists()) throw new FileNotFoundException(path.getAbsolutePath());
        boolean ret = true;
        if (path.isDirectory()){
            for (File f : path.listFiles()){
                ret = ret && FileUtils.deleteRecursive(f);
            }
        }
        return ret && path.delete();
    }
}
