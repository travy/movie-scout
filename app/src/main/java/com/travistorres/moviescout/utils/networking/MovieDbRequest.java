/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.networking;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * TODO:  document
 *
 * @author Travis Anthony Torres
 * @version February 12, 2017
 */

public class MovieDbRequest {
    public final String TAG = getClass().toString();

    public final static int HTTP_READ_TIMEOUT = 3000;
    public final static int HTTP_CONNECT_TIMEOUT = 3000;
    public final static String HTTP_GET_METHOD = "GET";

    /**
     * TODO:  document
     * TODO:  test
     *
     * @param url
     * @return
     */
    public String request(URL url) {
        String responseString = null;
        HttpURLConnection connection = null;

        try {
            //  Acquire the connection and set restrictions on the connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(HTTP_READ_TIMEOUT);
            connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
            connection.setRequestMethod(HTTP_GET_METHOD);
            connection.connect();

            //  Check that an HTTP 200 was acquired
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                //  TODO:  should handle event that HTTP 200 is not evaluated
                switch (responseCode) {
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Log.d(TAG, "Could not locate the web page");
                        break;
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        Log.d(TAG, "API Key was not authorized by server");
                        break;
                    case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                        Log.d(TAG, "Connection Timed out");
                        break;
                    default:
                        Log.d(TAG, "Networking Error");
                        break;
                }
            }

            //  Reads the response into an iterator
            InputStream response = connection.getInputStream();
            Scanner responseReader = new Scanner(response);
            responseReader.useDelimiter("\\A");

            //  Acquires the response from the server
            boolean hasInput = responseReader.hasNext();
            responseString = hasInput ? responseReader.next() : null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //  release the connection if defined
            if (connection != null) {
                connection.disconnect();
            }
        }

        return responseString;
    }
}
