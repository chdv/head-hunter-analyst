package com.dch.app.analyst.parser;

import com.dch.app.analyst.AnalystException;
import org.apache.commons.configuration.Configuration;

/**
 * Created by ִלטענטי on 13.06.2015.
 */
public class SiteInfo {

    private String name;
    private String url;
    private String parserClass;
    private String jobInfoBlockStart;
    private String jobInfoBlockEnd;
    private String encoding;
    private int pageFrom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParserClass() {
        return parserClass;
    }

    public void setParserClass(String parserClass) {
        this.parserClass = parserClass;
    }

    public String getJobInfoBlockStart() {
        return jobInfoBlockStart;
    }

    public void setJobInfoBlockStart(String jobInfoBlockStart) {
        this.jobInfoBlockStart = jobInfoBlockStart;
    }

    public String getJobInfoBlockEnd() {
        return jobInfoBlockEnd;
    }

    public void setJobInfoBlockEnd(String jobInfoBlockEnd) {
        this.jobInfoBlockEnd = jobInfoBlockEnd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(int pageFrom) {
        this.pageFrom = pageFrom;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public static SiteInfo loadFromConf(Configuration conf, int indx) {
        SiteInfo result = new SiteInfo();
        result.setName(conf.getString("sites.site(" + indx + ").name"));
        result.setUrl(conf.getString("sites.site(" + indx + ").url"));
        result.setEncoding(conf.getString("sites.site(" + indx + ").encoding"));
        result.setParserClass(conf.getString("sites.site(" + indx + ").parser-class"));
        result.setJobInfoBlockStart(conf.getString("sites.site(" + indx + ").job-info-block-start"));
        result.setJobInfoBlockEnd(conf.getString("sites.site(" + indx + ").job-info-block-end"));
        result.setPageFrom(conf.getInt("sites.site(" + indx + ").page-from"));
        return result;
    }

    public SiteParser createJobParser() throws AnalystException {
        try {
            return (SiteParser)Class.forName(getParserClass()).getConstructor(SiteInfo.class).newInstance(this);
        } catch (Exception e) {
            throw new AnalystException(e);
        }
    }
}
