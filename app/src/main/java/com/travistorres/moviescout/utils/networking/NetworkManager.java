/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.networking;

import com.travistorres.moviescout.utils.networking.exceptions.HttpConnectionTimeoutException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpPageNotFoundException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpUnauthorizedException;
import com.travistorres.moviescout.utils.networking.exceptions.NetworkingException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * NetworkManager
 *
 * Handles networking over HTTP protocol.  Will acquire all request in some given String format.
 *
 * @author Travis Anthony Torres
 * @version February 12, 2017
 */

public class NetworkManager {
    public final static String TAG = "NetworkManager";

    public final static int HTTP_READ_TIMEOUT = 3000;
    public final static int HTTP_CONNECT_TIMEOUT = 3000;
    public final static String HTTP_GET_METHOD = "GET";

    private final static String GENERIC_FAILURE_MESSAGE = "Network Access Failed:  ";

    /**
     * Opens a READ-ONLY connection to the server and imposes restrictions on the time that the
     * request is allowed to take.
     *
     * @param url The Url of the resource.
     *
     * @return The prepared connection
     *
     * @throws IOException When an issue occurs while connecting to the resource.
     */
    private static HttpURLConnection acquireConnection(URL url)
            throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(HTTP_READ_TIMEOUT);
        connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
        connection.setRequestMethod(HTTP_GET_METHOD);
        connection.connect();

        return connection;
    }

    /**
     * Checks the HTTP condition code to determine if the domain or api key may have been incorrect
     * or if a slow response from the server is being detected so that an appropriate message may
     * be displayed.  Other conditions are ambiguous and so will be handled the same way.
     *
     * @param connection The HTTP connection used.  It is assumed to be setup already.
     *
     * @throws IOException When an issue occurs reading the response code
     * @throws HttpPageNotFoundException When a 404 error occurs
     * @throws HttpPageNotFoundException When the resource rejected the connection
     * @throws HttpConnectionTimeoutException When a timeout occurs
     * @throws NetworkingException For some generic error which we don't particularly care about
     */
    private static void handleConnectionErrors(HttpURLConnection connection)
            throws IOException, HttpPageNotFoundException, HttpUnauthorizedException,
            HttpConnectionTimeoutException, NetworkingException {
        //  check that the connection passed, otherwise throws the desired exception
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            URL url = connection.getURL();
            switch (responseCode) {
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throw new HttpPageNotFoundException(url);
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    throw new HttpUnauthorizedException(url);
                case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                    throw new HttpConnectionTimeoutException(url);
                default:
                    throw new NetworkingException(GENERIC_FAILURE_MESSAGE, url, responseCode);
            }
        }
    }

    /**
     * Acquires the response from the specified resource URL as a String.
     *
     * @param url The resource to receive the request.
     *
     * @return String The network response.
     *
     * @throws IOException When an issue occurs reading the response code
     * @throws HttpPageNotFoundException When a 404 error occurs
     * @throws HttpPageNotFoundException When the resource rejected the connection
     * @throws HttpConnectionTimeoutException When a timeout occurs
     * @throws NetworkingException For some generic error which we don't particularly care about
     */
    public static String request(URL url)
            throws IOException, HttpPageNotFoundException, HttpUnauthorizedException,
            HttpConnectionTimeoutException, NetworkingException {
        String responseString = null;
        HttpURLConnection connection = null;

        try {
            //  Safely acquire connection and handle HTTP responses accordingly
            connection = acquireConnection(url);
            handleConnectionErrors(connection);

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
