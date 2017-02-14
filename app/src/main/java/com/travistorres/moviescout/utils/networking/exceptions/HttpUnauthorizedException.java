/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.networking.exceptions;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpUnauthorizedException
 *
 * Represents a situation in which a resource rejected access due to invalid authorization levels.
 *
 * Logs the requested URL.
 *
 * @author Travis Anthony Torres
 * @version February 13, 2017
 */

public class HttpUnauthorizedException extends NetworkingException {
    private static final String DEFAULT_MESSAGE = "Access Denied by the following resource:  ";

    /**
     * Logs the restricted url
     *
     * @param url The url that was restricted
     */
    public HttpUnauthorizedException(URL url) {
        super(DEFAULT_MESSAGE, url, HttpURLConnection.HTTP_UNAUTHORIZED);
    }
}
