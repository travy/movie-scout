/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.exceptions;

/**
 * NoContextException
 *
 * Exception to be thrown whenever an attempt is made to access a Context which has not been
 * defined.
 *
 * @author Travis Anthony Torres
 * @version February 19, 2017
 */

public class NoContextException extends NullPointerException {
    /**
     * Logs the message to display when No Context was defined.
     *
     * @param message
     */
    public NoContextException(String message) {
        super(message);
    }
}
