/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.networking.interfaces;

/**
 * NetworkConnectivityInterface
 *
 * This interface provides a way to specify the actions which should occur whenever a network state
 * has changed.
 *
 * @author Travis Anthony Torres
 * @version April 22, 2017
 */
public interface NetworkConnectivityInterface {
    /**
     * Signals that there is no network connectivity.
     *
     */
    void onNoNetworkConnectivity();

    /**
     * Signals that a network connection has been found.
     *
     */
    void onHasNetworkConnectivity();
}
