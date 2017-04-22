/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.networking.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.travistorres.moviescout.utils.networking.interfaces.NetworkConnectivityInterface;

/**
 * NetworkConnectionBroadcastReceiver
 *
 * This BroadcastReceiver will listen to events that transpire on the ConnectivityManager.  If it
 * detects that a connection to a given network has been lost, or found, then it will fire off
 * a request for the appropriate NetworkConnectivityInterface event.
 *
 * When a network has been detected, the registered connectivity interface will fire the
 * onHasNetworkConnectivity event so that the appropriate actions can be performed.  On the other
 * hand, if the network has been lost then the onNoNetworkConnectivity action will occur.
 *
 * This BroadcastReceiver is an excellent tool to use to hide Activity elements depending on the
 * state of a network connection.
 *
 * @author Travis Anthony Torres
 * @version April 22, 2017
 */

public class NetworkConnectionBroadcastReceiver extends BroadcastReceiver {
    private NetworkConnectivityInterface connectivityCallbacks;

    /**
     * Registers the actions that should be performed when a given connection event occurs.
     *
     * @param connectivityInterface
     */
    public NetworkConnectionBroadcastReceiver(NetworkConnectivityInterface connectivityInterface) {
        connectivityCallbacks = connectivityInterface;
    }

    /**
     * Receives a ConnectivityManager event from the os and fires off either the
     * onNoNetworkConnectivity or onHasNetworkConnectivity actions depending on the connection
     * signal that was received.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String networkAction = intent.getAction();
        if (networkAction.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (intent.hasExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
                connectivityCallbacks.onNoNetworkConnectivity();
            } else {
                connectivityCallbacks.onHasNetworkConnectivity();
            }
        }
    }
}
