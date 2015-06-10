package com.dch.app.analyst.parser;

import com.dch.app.analyst.AnalystConfiguration;
import com.dch.app.analyst.AnalystFactory;
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
 * Created by ƒмитрий on 09.06.2015.
 */
public class HHJobParser implements JobParser {

    Logger logger = LoggerFactory.getLogger(HHJobParser.class);

    protected String jobBlockStart = AnalystConfiguration.getBlockStart();
    protected String jobBlockEnd = AnalystConfiguration.getBlockEnd();

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
        processEnding();

        httpclient.close();
        return result;
    }

    protected void processEnding() throws Exception { }

    protected List<JobEntity> parsePage(InputStream input) throws Exception {
        List<JobEntity> result = AnalystFactory.createList();
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(input, AnalystConfiguration.getHHEncoding()));
        try {
            String line = null;
            while((line = reader.readLine())!=null) {
                if(line.contains(jobBlockStart)) {
                    result.add(parseLine(line));
                }
            }
        } finally {
            reader.close();
        }
        return result;
    }

    protected JobEntity parseLine(String line) {
//        Ћогика примитивна€, но в потенциале могла быть и посложней
        String newLine =
                line.substring(
                        line.indexOf(jobBlockStart),
                        line.indexOf(jobBlockEnd));

        return new JobEntity(newLine);
    }
}
