/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.networking.exceptions;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpPageNotFoundException
 *
 * Exception to be thrown whenever trying to access a network resource which is unknown or does not
 * exists.  Will log the URL that the application could not reach.
 *
 * @author Travis Anthony Torres
 * @version February 13, 2017
 */

public class HttpPageNotFoundException extends NetworkingException {
    private final static String DEFAULT_ERROR_MESSAGE = "The attempted web resource was:  ";
    /**
     * Records the URL that was attempted to be accessed.
     *
     * @param url the attempted url
     */
    public HttpPageNotFoundException(URL url) {
        super(DEFAULT_ERROR_MESSAGE, url, HttpURLConnection.HTTP_NOT_FOUND);
    }
}
