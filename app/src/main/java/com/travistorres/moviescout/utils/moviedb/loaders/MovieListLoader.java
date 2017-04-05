/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.loaders;

import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.travistorres.moviescout.utils.moviedb.builders.MovieDbParser;
import com.travistorres.moviescout.utils.moviedb.MovieDbRequester;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.networking.NetworkManager;
import com.travistorres.moviescout.utils.networking.exceptions.HttpConnectionTimeoutException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpPageNotFoundException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpUnauthorizedException;
import com.travistorres.moviescout.utils.networking.exceptions.NetworkingException;

import java.io.IOException;
import java.net.URL;

/**
 * MovieListLoader
 *
 * The MovieListLoader handles all network operations responsible for loading movies in the
 * background so that it will not slow down the app.
 *
 * @author Travis Anthony Torres
 * @version April 2, 2017
 */

public class MovieListLoader extends AsyncTaskLoader<Movie[]> {
    private Bundle args;
    private MovieDbRequester requester;
    private MovieDbNetworkingErrorHandler errorHandler;

    /**
     * Provides a movie requester instance and a bundle for loading data from.
     *
     * @param movieRequester
     * @param bundle
     */
    public MovieListLoader(MovieDbRequester movieRequester, Bundle bundle, MovieDbNetworkingErrorHandler networkErrorHandler) {
        super(movieRequester.getContext());

        requester = movieRequester;
        args = bundle;
        errorHandler = networkErrorHandler;
    }

    /**
     * Displays the loading wheel so that users are made aware that a requests is occurring
     * in the background.
     *
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (args == null) {
            return;
        }

        //  displays the loading wheel
        errorHandler.beforeNetworkRequest();

        //  forces content to display
        forceLoad();
    }

    /**
     * Issues a network request for all movie information from MovieDbApi.com.
     *
     * @return List of movies obtained from the server.
     */
    @Override
    public Movie[] loadInBackground() {
        String urlString = null;
        Movie[] movieList = null;

        //  acquire the string value of the URL if one has been set
        if (args.containsKey(requester.MOVIE_REQUEST_URL_EXTRA)) {
            urlString = args.getString(requester.MOVIE_REQUEST_URL_EXTRA);
            if (urlString == null || TextUtils.isEmpty(urlString)) {
                return null;
            }
        } else {
            return null;
        }

        //  attempt to acquire the resource
        try {
            URL url = new URL(urlString);
            String json = NetworkManager.request(url);
            if (json != null) {
                movieList = MovieDbParser.retrieveMovieList(json);
                requester.setTotalMovies(MovieDbParser.acquireTotalResults(json));
                requester.setTotalPages(MovieDbParser.acquireTotalPages(json));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpConnectionTimeoutException e) {
            e.printStackTrace();
        } catch (HttpPageNotFoundException e) {
            e.printStackTrace();
            errorHandler.onPageNotFound();
        } catch (HttpUnauthorizedException e) {
            e.printStackTrace();
            errorHandler.onUnauthorizedAccess();
        } catch (NetworkingException e) {
            e.printStackTrace();
            errorHandler.onGeneralNetworkingError();
        }

        return movieList;
    }
}
