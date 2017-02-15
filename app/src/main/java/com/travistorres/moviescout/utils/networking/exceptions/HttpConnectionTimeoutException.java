/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.networking.exceptions;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpConnectionTimeoutException
 *
 * The appropriate Exception to be thrown whenever the client request or response from the server
 * takes to long to process.
 *
 * @author Travis Anthony Torres
 * @version February 13, 2017
 */

public class HttpConnectionTimeoutException extends NetworkingException {
    private final static String DEFAULT_MESSAGE = "Took to long to get responese from resource:  ";

    /**
     * Allows the url to be displayed in the Log event.
     *
     * @param url The url that timed out
     */
    public HttpConnectionTimeoutException(URL url) {
        super(DEFAULT_MESSAGE, url, HttpURLConnection.HTTP_CLIENT_TIMEOUT);
    }
}
