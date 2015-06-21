package com.dch.app.analyst.writer;

import com.dch.app.analyst.AnalystConfiguration;
import com.dch.app.analyst.parser.JobEntity;
import com.dch.app.analyst.parser.SiteInfo;
import com.dch.app.analyst.util.SingleThreadExecutor;
import com.dch.app.analyst.util.SingleThreadExecutorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Асинхронная реализация. Очередность записи в файл обеспечивает очередь в FixedThreadPool(1) - т.к. там один поток,
 * нет конкуренции при записи
 * Created by Дмитрий on 09.06.2015.
 */
public class HtmlJobsWriter implements JobsWriter {

    private Logger logger = LoggerFactory.getLogger(HtmlJobsWriter.class);

    private static final String outputBegin = AnalystConfiguration.getOutputBegin();

    private static final String outputEnd = AnalystConfiguration.getOutputEnd();

    private static final String delimeter = AnalystConfiguration.getDelimeter();

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private SiteInfo site;

    private File outFile;

    private Writer writer;

    private SingleThreadExecutor pool = new SingleThreadExecutorImpl();

    private String keyWorld;

    private AtomicInteger count = new AtomicInteger();

    private Renamer renamer;

    public HtmlJobsWriter(SiteInfo s, String key, File out, Renamer r) throws IOException {
        site = s;
        outFile = out;
        keyWorld = key;
        renamer = r;
        writer =
                new OutputStreamWriter(
                        new FileOutputStream(outFile),
                        s.getEncoding());
    }

    public void writeTop() throws IOException {
        writer.write(
                String.format(
                        outputBegin,
                        keyWorld,
                        site.getName(),
                        format.format(new Date())));
    }

    public void writeJob(JobEntity entity) throws IOException {
        count.incrementAndGet();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    writer.write(entity.getJobInfo());
                    writer.write(delimeter);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    public int getCount() {
        return count.get();
    }

    public void writeBottom() throws IOException {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    writer.write(String.format(outputEnd, getCount()));
                    writer.close();
                    renamer.rename(getCount());
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        pool.stop();
    }

}
