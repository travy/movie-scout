/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.interfaces;

/**
 * MovieDbNetworkingErrorHandler
 *
 * Specifies an interface of operations that will be requested during the MovieDbRequester's
 * lifecycle.
 *
 * @author Travis Anthony Torres
 * @version February 21, 2017
 */

public interface MovieDbNetworkingErrorHandler {
    void onPageNotFound();
    void onUnauthorizedAccess();
    void onGeneralNetworkingError();
    void beforeNetworkRequest();
    void afterNetworkRequest();
}
