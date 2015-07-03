package com.dch.app.analyst.parser;

import com.dch.app.analyst.AnalystConfiguration;
import com.dch.app.analyst.writer.JobsWriter;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
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

    private boolean redirect = false;

    public CommonSiteParser(SiteInfo info) {
        siteInfo = info;
    }

    public void setJobsWriter(JobsWriter jobsWriter) {
        this.jobsWriter = jobsWriter;
    }

    private RedirectStrategy noRedirectStrategy = new DefaultRedirectStrategy() {
        @Override
        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
            boolean red = super.isRedirected(request, response, context);

            if(red) {
                redirect = true;
            }
            return false;
        }
    };

    public void readJobs(String keyWord) throws SiteParserException {
        try {
            HttpClientBuilder builder = HttpClientBuilder.create();
            builder.setRedirectStrategy(noRedirectStrategy);
            CloseableHttpClient httpclient = builder.build();

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
        CloseableHttpResponse response = httpclient.execute(httpGet);

        if(redirect) {
            return false;
        }

        logger.debug("load data from {}", url);

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
            for(String end : siteInfo.getJobInfoBlockEnd()) {
                int iend = currentLine.indexOf(end);
                if(iend > 0) {
                    currentLine = currentLine.substring(
                            0,
                            iend);
                    findJobs = true;
                    break;
                }
            }
            if(findJobs)
                jobsWriter.writeJob(new JobEntity(currentLine));
        }
        return findJobs;
    }

}
