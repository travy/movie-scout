/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.configs;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Configurations
 *
 * Packages the contents of a given XML parser into a Hash Table for easy access to it's contents.
 * Each configuration can be acquired by it's tag name DOM sequence.  For instance since the API V3
 * configuration lies under the configurations > movieDbApi > apiKeyV3 tag's, it can be acquired
 * by specifying configurations.movieDbApi.apiKeyV3 as the key.  Each config section will be
 * delimited using the "." character.
 *
 * @author Travis Anthony Torres
 * @version February 12, 2017
 */

class Configurations extends Hashtable<String, String> {
    public static final int DEFAULT_CONTENT_SIZE = 10;

    /*
     *  Various values that are utilized for storing the contents of the configuration file into
     *  a Collection.
     *
     */
    private static final String RESOURCE_DELIMITER  = ".";
    private static final String INVALID_PARSER_SUPPLIED_MESSAGE = "XmlResourceParser provided was not set to the start of the document";
    private static final int RESOURCE_NAME_OFFSET_POSITION = 1;
    private static final int RESOURCE_NAME_START_POSITION = 0;

    /**
     * Stores the contents of the configurations file into a Collection for accessing throughout
     * the application.
     *
     * @param parser An XML parser which is set to read from the configuration file.
     *
     * @throws XmlPullParserException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public Configurations(XmlResourceParser parser)
            throws XmlPullParserException, IllegalArgumentException, IOException {
        this(parser, DEFAULT_CONTENT_SIZE);
    }

    /**
     * Stores the contents of the configurations file into a Collection for accessing throughout
     * the application.
     *
     * @param parser An XML parser which is set to read from the configuration file.
     * @param contentSize The number of items that should be reserved for use within the
     *                    Collection.
     *
     * @throws XmlPullParserException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public Configurations(XmlResourceParser parser, int contentSize)
            throws XmlPullParserException, IllegalArgumentException, IOException {
        super(contentSize);

        buildConfigurations(parser);
    }

    /**
     * Iterates through the XML document and maps each configuration to a specific key value pair
     * in the Collection.
     *
     * @param parser An XML parser which is set to read from the configuration file.
     *
     * @throws XmlPullParserException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    private void buildConfigurations(XmlResourceParser parser)
            throws XmlPullParserException, IllegalArgumentException, IOException {
        //  The parser must be set to the start of the document
        if (parser.getEventType() != XmlResourceParser.START_DOCUMENT) {
            throw new IllegalArgumentException(INVALID_PARSER_SUPPLIED_MESSAGE);
        }

        String resourceName = "";
        while (parser.next() != XmlResourceParser.END_DOCUMENT) {
            int domEvent = parser.getEventType();
            switch (domEvent) {
                case XmlResourceParser.START_TAG:
                    //  identify the name of the configuration
                    resourceName += RESOURCE_DELIMITER + parser.getName();
                    break;
                case XmlResourceParser.END_TAG:
                    //  omit the previous resource from the resource name string
                    int separatorIndex = resourceName.lastIndexOf(RESOURCE_DELIMITER);
                    resourceName = resourceName.substring(RESOURCE_NAME_START_POSITION, separatorIndex);
                    break;
                case XmlResourceParser.TEXT:
                    //  add the configuration
                    put(resourceName.substring(RESOURCE_NAME_OFFSET_POSITION), parser.getText());
                    break;
            }
        }
    }
}
