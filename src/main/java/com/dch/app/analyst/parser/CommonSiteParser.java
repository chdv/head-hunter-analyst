package com.dch.app.analyst.parser;

import com.dch.app.analyst.AnalystConfiguration;
import com.dch.app.analyst.format.JobsWriter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public class CommonSiteParser implements SiteParser {

    Logger logger = LoggerFactory.getLogger(CommonSiteParser.class);

    private SiteInfo siteInfo;

    private JobsWriter jobsWriter;

    public CommonSiteParser(SiteInfo info) {
        siteInfo = info;
    }

    public void setJobsWriter(JobsWriter jobsWriter) {
        this.jobsWriter = jobsWriter;
    }

    public void readJobs(String keyWord) throws SiteParserException {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            int pageNum = siteInfo.getPageFrom();
            boolean findJobs = true;
            while(findJobs) {
                String url =
                        String.format(
                                siteInfo.getUrl(),
                                URLEncoder.encode(keyWord, AnalystConfiguration.getConfEncoding()),
                                Integer.valueOf(pageNum));

                findJobs = parseUrl(url, httpclient);
                pageNum++;
            }

            httpclient.close();
        } catch(Exception e) {
            throw new SiteParserException(e);
        }
    }

    protected boolean parseUrl(String url, CloseableHttpClient httpclient) throws Exception {
        boolean findJobs = false;
        HttpGet httpGet = new HttpGet(url);
        logger.debug("load data from {}", url);
        CloseableHttpResponse response = httpclient.execute(httpGet);

        Reader reader = new InputStreamReader(
                response.getEntity().getContent(),
                siteInfo.getEncoding());
        try {
            findJobs = parsePage(reader);
        } finally {
            reader.close();
        }
        response.close();

        return findJobs;
    }

    private boolean parsePage(Reader reader) throws IOException {
        boolean findJobs = false;
        Scanner s = new Scanner(reader).useDelimiter(siteInfo.getJobInfoBlockStart());
        if(s.hasNext()) s.next();
        while(s.hasNext() ) {
            String currentLine = s.next();
            currentLine = currentLine.substring(
                    0,
                    currentLine.indexOf(siteInfo.getJobInfoBlockEnd()));
            jobsWriter.writeJob(new JobEntity(currentLine));
            findJobs = true;
        }
        return findJobs;
    }

}
