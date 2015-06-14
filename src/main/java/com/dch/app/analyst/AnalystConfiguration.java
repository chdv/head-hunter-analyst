package com.dch.app.analyst;

import com.dch.app.analyst.parser.SiteInfo;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.List;

/**
 * Created by ִלטענטי on 09.06.2015.
 */
public final class AnalystConfiguration {

    private static volatile Configuration config = null;

    public static Configuration getConf() throws AnalystException {
        if (config == null) {
            synchronized (AnalystConfiguration.class) {
                if (config == null) {
                    try {
                        config = new XMLConfiguration("config/analyst-config.xml");
                    } catch (ConfigurationException e) {
                        throw new AnalystException(e);
                    }
                }
            }
        }
        return config;
    }

    private AnalystConfiguration() { }

//    public static String getSiteEncoding() {
//        return getConf().getString("encoding");
//    }

    public static SiteInfo[] getSites() {
        int count = getConf().getList("sites.site.name").size();
        SiteInfo[] result = new SiteInfo[count];
        for(int i = 0; i<count; i++) {
            result[i] = SiteInfo.loadFromConf(getConf(), i);
        }
        return result;
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

    public static List<String>  getKeyWorlds() {
        return (List<String>)(Object)getConf().getList("key-worlds.key-world");
    }

    public static String getConfEncoding() {
        return "UTF-8";
    }

    public static Boolean isOpenInBrowser() {
        return getConf().getBoolean("open-in-browser");
    }

    public static int getThreadsCountPerCore() {
        return getConf().getInt("threads-count-per-core");
    }
}
