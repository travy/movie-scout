/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.configs;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Hashtable;

/**
 * TODO:  document
 */

class Configurations extends Hashtable<String, String> {
    public static  final int DEFAULT_CONTENT_SIZE = 10;

    private static final String RESOURCE_DELIMITER  = ".";
    private static final String INVALID_PARSER_SUPPLIED_MESSAGE = "XmlResourceParser provided was not set to the start of the document";
    private static final int RESOURCE_NAME_OFFSET_POSITION = 1;
    private static final int RESOURCE_NAME_START_POSITION = 0;

    public Configurations(XmlResourceParser parser)
            throws XmlPullParserException, IllegalArgumentException, IOException {
        this(parser, DEFAULT_CONTENT_SIZE);
    }

    public Configurations(XmlResourceParser parser, int contentSize)
            throws XmlPullParserException, IllegalArgumentException, IOException {
        super(contentSize);

        buildConfigurations(parser);
    }

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
