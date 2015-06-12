package com.dch.app.analyst.parser;

import com.dch.app.analyst.AnalystConfiguration;
import com.dch.app.analyst.AnalystFactory;
import com.dch.app.analyst.util.ForkJoinRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by ������� on 09.06.2015.
 */
public class HHJobParser implements JobParser {

    Logger logger = LoggerFactory.getLogger(HHJobParser.class);

    protected final String jobBlockStart = AnalystConfiguration.getBlockStart();
    protected final String jobBlockEnd = AnalystConfiguration.getBlockEnd();

    public List<JobEntity> readJobs(String keyWord) throws JobParserException {
        List<String> urls = AnalystFactory.createList();
        List<JobEntity> result = null;
        try {
            for(String s : AnalystConfiguration.getUrls()) {
                urls.add(String.format(s, URLEncoder.encode(keyWord, AnalystConfiguration.getConfEncoding())));
            }
            result = parseUrls(urls);
        } catch(Exception e) {
            throw new JobParserException(e);
        }
        return result;
    }

    protected List<JobEntity> parseUrls(List<String> urlList) throws Exception {
        List<JobEntity> result = AnalystFactory.createList();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        for(String url : urlList) {
            HttpGet httpGet = new HttpGet(url);
            logger.debug("go to {}", url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            result.addAll(
                    parsePage(
                            response.getEntity().getContent()));
        }
        httpclient.close();
        return result;
    }

    protected List<JobEntity> parsePage(InputStream input) throws Exception {
        List<JobEntity> result = AnalystFactory.createList();
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(input, AnalystConfiguration.getHHEncoding()));
        try {
            ForkJoinRunner runner = AnalystFactory.createForkJoinRunner();
            String line = null;
            while((line = reader.readLine())!=null) {
                final String finalLine = line;
                runner.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(finalLine.contains(jobBlockStart)) {
                            result.add(parseLine(finalLine));
                        }
                    }
                });
            }
            runner.await();
        } finally {
            reader.close();
        }
        return result;
    }

    protected JobEntity parseLine(String line) {
//        ������ �����������, �� � ���������� ����� ���� � ���������
        String newLine =
                line.substring(
                        line.indexOf(jobBlockStart),
                        line.indexOf(jobBlockEnd));

        return new JobEntity(newLine);
    }
}
