/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.travistorres.moviescout.R;
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
 * @version v1.2.0 (March 27, 2017)
 */

public class MovieDbRequester
        implements LoaderManager.LoaderCallbacks<Movie[]> {
    /*
     *  Error message to display when no network could be reached.
     *
     */
    public final static String NO_NETWORK_ERROR_MESSAGE = "Unable to access Network Resource";
    public final String MOVIE_REQUEST_URL_EXTRA;

    private MovieDbNetworkingErrorHandler errorHandler;
    private FragmentActivity parentActivity;
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
     * @param parent
     * @param networkHandler
     * @param clickListener
     */
    public MovieDbRequester(FragmentActivity parent, MovieDbNetworkingErrorHandler networkHandler, MovieClickedListener clickListener) {
        parentActivity = parent;
        errorHandler = networkHandler;
        movieAdapter = new MovieListAdapter(clickListener, this);
        sortType = MovieSortType.MOST_POPULAR;

        //  specifies the extra to use for acquiring the resource url
        MOVIE_REQUEST_URL_EXTRA = parentActivity.getString(R.string.movie_request_url_extra);

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
            Bundle requestUrlBundle = new Bundle();
            requestUrlBundle.putString(MOVIE_REQUEST_URL_EXTRA, url.toString());

            //  retrieves the loader that will lookup the movies
            Resources resources = parentActivity.getResources();
            int loaderKey = resources.getInteger(R.integer.movie_db_requester_loader_manager_id);
            LoaderManager loaderManager = parentActivity.getSupportLoaderManager();
            Loader<Movie[]> movieLoader = loaderManager.getLoader(loaderKey);
            loaderManager.restartLoader(loaderKey, requestUrlBundle, this);

            //  update the page index
            ++currentPage;
        }
    }

    /**
     * Acquires the required URL for the current operation.
     *
     * @return The URL to acquire the next set of results.
     */
    public URL getCurrentRequestUrl() {
        MovieDbUrlManager urlManager = new MovieDbUrlManager(parentActivity);
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
     * Creates a thread that will issue a network requests for all of the Movies.
     *
     * @param id
     * @param args
     *
     * @return LoaderManager responsible for requesting movie resources from the server
     */
    @Override
    public Loader<Movie[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Movie[]>(parentActivity) {
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
                if (args.containsKey(MOVIE_REQUEST_URL_EXTRA)) {
                    urlString = args.getString(MOVIE_REQUEST_URL_EXTRA);
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
                        movieList = MovieDbParser.retrieveMovieList(json, parentActivity);
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
        };
    }

    /**
     * Specifies what to do with the movie resources that were obtained from the server.
     *
     * @param loader
     * @param list
     */
    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] list) {
        errorHandler.afterNetworkRequest();

        //  add all of the movies to the list
        if (list != null) {
            movieAdapter.setMoviesList(list);
        } else {
            Toast.makeText(parentActivity, NO_NETWORK_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Specifies what to do when the loader is reset.
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {
        //  Intentionally left blank
    }
}
