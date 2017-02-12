/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.configs;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.travistorres.moviescout.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * TODO:  document
 */

public final class ConfigurationsReader {
    private static Configurations configs = null;

    public final static String API_V3_KEY = "configurations.movieDbApi.apiKeyV3";
    public final static String API_V4_KEY = "configurations.movieDbApi.apiKeyV4";

    public static String getApiKey(Resources resource) {
        return getConfig(resource, API_V3_KEY);
    }

    public static String getApiV4Key(Resources resource) {
        return getConfig(resource, API_V4_KEY);
    }

    public static String getConfig(Resources resources, String configName) {
        //  acquire the configs
        if (configs == null) {
            configs = getConfigurationsList(resources);
        }

        return configs.get(configName);
    }

    private static Configurations getConfigurationsList(Resources resources) {
        XmlResourceParser parser = resources.getXml(R.xml.configurations);
        Configurations configurations = null;

        try {
            configurations = new Configurations(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configurations;
    }
}
