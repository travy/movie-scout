/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.configs;

import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * TODO:  document
 */

class Configurations implements Map<String, String> {
    public final static int DEFAULT_CONTENT_SIZE = 10;

    private final static String RESOURCE_DELIMITER  = ".";
    private final static String INVALID_PARSER_SUPPLIED_MESSAGE = "XmlResourceParser provided was not set to the start of the document";

    private Hashtable<String, String> contents;

    public Configurations(XmlResourceParser parser)
            throws XmlPullParserException, IllegalArgumentException, IOException {
        contents = new Hashtable<String, String>(DEFAULT_CONTENT_SIZE);

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
                    //  omit the previous resource from the resource name
                    int separatorIndex = resourceName.lastIndexOf(RESOURCE_DELIMITER);
                    resourceName = resourceName.substring(0, separatorIndex);
                    break;
                case XmlResourceParser.TEXT:
                    //  add the configuration
                    put(resourceName.substring(1), parser.getText());
                    break;
            }
        }
    }

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return contents.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return contents.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return contents.get(key);
    }

    @Override
    public String put(String key, String value) {
        return contents.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return contents.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        contents.putAll(m);
    }

    @Override
    public void clear() {
        contents.clear();
    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return contents.keySet();
    }

    @NonNull
    @Override
    public Collection<String> values() {
        return contents.values();
    }

    @NonNull
    @Override
    public Set<Entry<String, String>> entrySet() {
        return contents.entrySet();
    }
}
