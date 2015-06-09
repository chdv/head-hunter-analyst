package com.dch.app.analyst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Используется для отладки для минимизации обращения к hh.ru, а также для автотестов.
 * Скачанные данные с hh.ru предварительно сохранены в файлы
 * Created by Дмитрий on 09.06.2015.
 */
public class FileJobParser extends HHParallelJobParser {

    Logger logger = LoggerFactory.getLogger(FileJobParser.class);

    private static final String URL1 = "page1.txt";
    private static final String URL2 = "page2.txt";

    private static final String[] URLS = new String[]{URL1, URL2};

    public List<JobEntity> readJobs(String keyWord) throws JobParserException {
        List<JobEntity> result = null;
        try {
            result = parseUrls(Arrays.asList(URLS));
        } catch(Exception e) {
            throw new JobParserException(e);
        }
        return result;
    }

    protected List<JobEntity> parseUrls(List<String> urlList) throws Exception {
        List<JobEntity> result = AnalystFactory.createList();
        TimeWatch watch = new TimeWatch();
        for(String url : urlList) {
            result.addAll(
                    parsePage(
                            this.getClass().getResourceAsStream("/" + url)));
        }
        watch.printMicro();
        return result;
    }

}
