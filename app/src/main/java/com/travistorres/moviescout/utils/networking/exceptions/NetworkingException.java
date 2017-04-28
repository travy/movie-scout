/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.networking.exceptions;

import android.util.Log;

import java.net.URL;

/**
 * NetworkingException
 *
 * Base class for all Networking Exceptions that can be thrown by the application.  Will ensure
 * that the message is always recorded in the error log.
 *
 * @author Travis Anthony Torres
 * @version February 13, 2017
 */

public class NetworkingException extends Exception {
    private final static String LOG_MESSAGE = "Movie Scout Networking Exception was thrown:  [HTTP ";

    /**
     * Will log that an exception was thrown due to the applications Networking features failing.
     *
     * @param message An appropriate message for the error
     */
    //  TODO- look into providing a context so that the default message can be read from configs.
    public NetworkingException(String message, URL url, int httpResponseCode) {
        super(message + url.toString());

        Log.e(getClass().toString(), LOG_MESSAGE + httpResponseCode + "] " + message + url.toString());
    }
}
