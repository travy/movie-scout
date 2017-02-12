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
 * ConfigurationsReader
 *
 * Performs static methods for accessing the contents of the Configuration file.
 *
 * @author Travis Anthony Torres
 * @version February 12, 2017
 */

public final class ConfigurationsReader {
    private static Configurations configs = null;

    /*
     *  Configuration access keys
     */
    public final static String API_V3_KEY = "configurations.movieDbApi.apiKeyV3";
    public final static String API_V4_KEY = "configurations.movieDbApi.apiKeyV4";

    /**
     * Retrieves the API V3 Key for use when requesting content from Movie DB API.
     *
     * @param resources Activity resource object.
     *
     * @return The v3 api key.
     */
    public static String getApiKey(Resources resources) {
        return getConfig(resources, API_V3_KEY);
    }

    /**
     * Retrieves the API Key for accessing the Movie DB API v4 features (if utilized).
     *
     * @param resources Activity resource object.
     *
     * @return The v4 api key.
     */
    public static String getApiV4Key(Resources resources) {
        return getConfig(resources, API_V4_KEY);
    }

    /**
     * Acquires the value specified for a specific configuration.
     *
     * @param resources Activity resource object.
     * @param configName The configuration key for the desired setting.
     *
     * @return The value of the configuration.
     */
    public static String getConfig(Resources resources, String configName) {
        //  acquire the configs, only one should exist in memory to ensure consistency
        if (configs == null) {
            configs = getConfigurationsList(resources);
        }

        return configs.get(configName);
    }

    /**
     * Acquires a collection containing all the specified configurations for the project.
     *
     * @param resources Activity resource object.
     *
     * @return Collection object containing all of the required configurations for the project.
     */
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
