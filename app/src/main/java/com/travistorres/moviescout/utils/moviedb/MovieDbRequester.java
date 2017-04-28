/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieClickedListener;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.adapters.MovieListAdapter;
import com.travistorres.moviescout.utils.moviedb.loaders.FavoriteMovieLoaderTask;
import com.travistorres.moviescout.utils.moviedb.loaders.MovieListLoader;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.networking.UrlManager;

import java.net.URL;

/**
 * MovieDbRequester
 *
 * This request management class will allow the total querying of all movies that are listed under
 * Movie DB API.
 *
 * @author Travis Anthony Torres
 * @version v1.2.0 (March 28, 2017)
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
            if (sortType == MovieSortType.FAVORITES) {
                Log.d(getClass().getSimpleName(), "Current Page:  " + Integer.toString(currentPage) + ":  Total Pages:  " + Integer.toString(totalPages));
                totalPages = 2; //  prevents infinite list of results
                currentPage = 3;
                loadLoaderManager(null, R.integer.favorite_movies_loader_manager_id);
                Log.d(getClass().getSimpleName(), "Current Page:  " + Integer.toString(currentPage) + ":  Total Pages:  " + Integer.toString(totalPages));

            } else {
                URL url = getCurrentRequestUrl();
                Bundle requestUrlBundle = new Bundle();
                requestUrlBundle.putString(MOVIE_REQUEST_URL_EXTRA, url.toString());

                //  run the restful request loader
                loadLoaderManager(requestUrlBundle, R.integer.movie_db_requester_loader_manager_id);

                //  update the page index
                ++currentPage;
            }
        }
    }

    /**
     * Executes a specified loader manager by its resource id.
     *
     * @param bundle
     * @param resourceId
     */
    private void loadLoaderManager(Bundle bundle, int resourceId) {
        Resources resources = parentActivity.getResources();
        int loaderKey = resources.getInteger(resourceId);
        LoaderManager loaderManager = parentActivity.getSupportLoaderManager();
        loaderManager.restartLoader(loaderKey, bundle, this);
    }

    /**
     * Acquires the required URL for the current operation.
     *
     * @return The URL to acquire the next set of results.
     */
    public URL getCurrentRequestUrl() {
        UrlManager urlManager = new UrlManager(parentActivity);
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
        Loader loader = null;
        Resources resources = parentActivity.getResources();
        if (id == resources.getInteger(R.integer.movie_db_requester_loader_manager_id)) {
            loader = new MovieListLoader(this, args, errorHandler);
        } else if (id == resources.getInteger(R.integer.favorite_movies_loader_manager_id)) {
            loader = new FavoriteMovieLoaderTask(parentActivity.getApplicationContext());
        }

        return loader;
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

        //if (loader instanceof MovieListLoader) {
            //  add all of the movies to the list
            if (list != null) {
                movieAdapter.setMoviesList(list);
            } else {
                Toast.makeText(parentActivity, NO_NETWORK_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        /*} else if (loader instanceof FavoriteMovieLoaderTask) {
            if (list != null) {
                for (int i = 0; i < list.length; ++i) {
                    Log.d(getClass().getSimpleName(), "Movie:  " + list[i].title);
                }
            }
        }*/
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

    public int getCurrentPage() {
        return currentPage;
    }

    public MovieSortType getSortType() {
        return sortType;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Retrieves the context which the Requester is acting on.
     *
     * @return calling context
     */
    public Context getContext() {
        return parentActivity;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalMovies(int totalMovies) {
        this.totalMovies = totalMovies;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
