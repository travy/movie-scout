/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.travistorres.moviescout.utils.moviedb.listeners.MovieClickedListener;
import com.travistorres.moviescout.utils.moviedb.listeners.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.adapters.MovieListAdapter;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.networking.NetworkManager;
import com.travistorres.moviescout.utils.networking.exceptions.HttpConnectionTimeoutException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpPageNotFoundException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpUnauthorizedException;
import com.travistorres.moviescout.utils.networking.exceptions.NetworkingException;

import java.io.IOException;
import java.net.URL;

/**
 * MovieDbRequester
 *
 * This request management class will allow the total querying of all movies that are listed under
 * Movie DB API.
 *
 * @author Travis Anthony Torres
 * @version February 21, 2017
 */

public class MovieDbRequester {
    /*
     *  Error message to display when no network could be reached.
     *
     */
    public final static String NO_NETWORK_ERROR_MESSAGE = "Unable to access Network Resource";

    private MovieDbNetworkingErrorHandler errorHandler;
    private Context context;
    private MovieListAdapter movieAdapter;
    private int currentPage;
    private int totalPages;
    private int totalMovies;
    private MovieSortType sortType;
    private String versionThreeApiKey;
    private String versionFourApiKey;

    /**
     * Constructs a new Request object that will queried.
     *
     * @param appContext
     * @param networkHandler
     * @param clickListener
     */
    public MovieDbRequester(Context appContext, MovieDbNetworkingErrorHandler networkHandler, MovieClickedListener clickListener) {
        context = appContext;
        errorHandler = networkHandler;
        movieAdapter = new MovieListAdapter(clickListener, this);
        sortType = MovieSortType.MOST_POPULAR;

        reset();
    }

    /**
     * Retrieves the MovieListAdapter object.
     *
     * @return
     */
    public MovieListAdapter getAdapter() {
        return movieAdapter;
    }

    /**
     * Specifies the api keys to use for accessing resources from the Movie DB.
     *
     * @param versionThreeKey
     * @param versionFourKey
     */
    public void setApiKeys(String versionThreeKey, String versionFourKey) {
        setVersionThreeApiKey(versionThreeKey);
        setVersionFourApiKey(versionFourKey);
    }

    /**
     * Specifies the key to use for accessing Version 3 API features.
     *
     * @param versionThreeKey
     */
    public void setVersionThreeApiKey(String versionThreeKey) {
        versionThreeApiKey = versionThreeKey;
    }

    /**
     * Specifies the key to use for accessing Version 4 API features.
     *
     * @param versionFourKey
     */
    public void setVersionFourApiKey(String versionFourKey) {
        versionFourApiKey = versionFourKey;
    }

    /**
     * Clears out the contents of the adapter and resets all flag values.
     *
     */
    public void reset() {
        currentPage = 1;
        totalPages = 1;
        totalMovies = 0;
        movieAdapter.empty();
    }

    /**
     * Specifies the order in which movies should be sorted.
     *
     * @param type The movie sort type.
     */
    public void setSortType(MovieSortType type) {
        sortType = type;
    }

    /**
     * Displays the next set of results onto the movie list.
     *
     */
    public void requestNext() {
        if (hasNextPage()) {
            URL url = getCurrentRequestUrl();
            new NetworkingTask().execute(url);
            ++currentPage;
        }
    }

    /**
     * Acquires the required URL for the current operation.
     *
     * @return The URL to acquire the next set of results.
     */
    public URL getCurrentRequestUrl() {
        MovieDbUrlManager urlManager = new MovieDbUrlManager(context);
        URL url = urlManager.getSortedMoveListUrl(sortType, currentPage, versionThreeApiKey);

        return url;
    }

    /**
     * Determines if there is another page that can be queried.
     *
     * @return true if there are more pages or total pages has not been set, and false otherwise.
     */
    public boolean hasNextPage() {
        return currentPage < totalPages || totalPages == 1;
    }

    /**
     * NetworkingTask
     *
     * A utility class which will run a sub-process that queries a list of movies over the network.
     * The process runs separately from the MainActivity to keep the app responsive during times of
     * low/non-existent network connectivity.
     *
     */

    private class NetworkingTask extends AsyncTask<URL, Void, Movie[]> {
        /**
         * Allow users to see the loading indicator wheel.
         *
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            errorHandler.beforeNetworkRequest();
        }

        /**
         * Request a list of movies from the network resource.
         *
         * @param urls List of urls for acquiring the movies.  We only need one.
         *
         * @return List of movies that were queried.
         */
        @Override
        protected Movie[] doInBackground(URL... urls) {
            if (urls.length <= 0) {
                return null;
            }

            URL url = urls[0];
            Movie[] movieList = null;

            try {
                String json = NetworkManager.request(url);
                if (json != null) {
                    movieList = MovieDbParser.retrieveMovieList(json, context);
                    totalMovies = MovieDbParser.acquireTotalResults(json);
                    totalPages = MovieDbParser.acquireTotalPages(json);
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

        /**
         * Stores the list of movies in the adapter so they can be efficiently rendered onto the
         * app.
         *
         * @param list acquired movies.
         */
        @Override
        protected void onPostExecute(Movie[] list) {
            errorHandler.afterNetworkRequest();

            if (list != null) {
                movieAdapter.setMoviesList(list);
            } else {
                Toast.makeText(context, NO_NETWORK_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
