package com.dch.app.analyst;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public final class AnalystConfiguration {

    private static Configuration config = null;

    private AnalystConfiguration() {};

    private static Configuration getConf() throws AnalystException {
        if(config == null) {
            try {
                config = new XMLConfiguration("analyst-config.xml");
            } catch (ConfigurationException e) {
                throw new AnalystException(e);
            }
        }
        return config;
    }

    public static String getHHEncoding() {
        return getConf().getString("encoding");
    }

    public static List<String> getUrls() {
        return (List<String>)(Object)getConf().getList("reader.urls.url");
    }

    public static String getBlockStart() {
        return getConf().getString("reader.page.block-start");
    }

    public static String getBlockEnd() {
        return getConf().getString("reader.page.block-end");
    }

    public static String getDelimeter() {
        return getConf().getString("writer.delimeter");
    }

    public static String getOutputBegin() {
        return getConf().getString("writer.output-begin");
    }

    public static String getOutputEnd() {
        return getConf().getString("writer.output-end");
    }

    public static String getKeyWorld() {
        return getConf().getString("key-world");
    }

    public static String getConfEncoding() {
        return "UTF-8";
    }
}
