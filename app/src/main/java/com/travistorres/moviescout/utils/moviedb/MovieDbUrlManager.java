/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb;

import android.content.res.Resources;
import android.net.Uri;

import com.travistorres.moviescout.utils.configs.ConfigurationsReader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * MovieDbUrlManager
 *
 * Provides URL's that are capable of safely retrieving content from the Movie DB API.  Using the
 * URI objects generated from this class will ensure that URL's are always well formed.
 *
 * @author Travis Anthony Torres
 * @version February 11, 2017
 */

public class MovieDbUrlManager {
    /*
     * URI construction strings that will be utilized throughout the class for constructing valid
     * request to the movie db api.
     *
     */
    private final static String URL_SCHEME = "HTTP";
    private final static String URL_DOMAIN = "api.themoviedb.org";
    private final static String API_V3_IDENTIFIER = "3";
    private final static String MOVIE_REQUEST_ACTION = "movie";
    private final static String POPULAR_MOVIE_SORT_ACTION = "popular";
    private final static String HIGH_RATING_MOVIE_SORT_ACTION = "rating";
    private final static String API_KEY_QUERY_NAME = "api_key";

    /*
     * Specifies values used for determining a users preference on how movies should be sorted
     * when requested using the API.
     *
     */
    public final static int SORT_BY_POPULARITY = 0x00;
    public final static int SORT_BY_RATING = 0x01;

    private Resources resources;

    /**
     * Allocates memory for the builder and provides a system resource manager.
     *
     * @param resources System resources
     */
    public MovieDbUrlManager(Resources resources) {
        setResources(resources);
    }

    /**
     * Specify the resources object.
     *
     * @param res The resources
     */
    public void setResources(Resources res) {
        resources = res;
    }

    /**
     * Retrieves the URL for acquiring a list of movies that is sorted by popularity.
     *
     * @return URL the URL of the request or null on error
     */
    public URL getPopularMoviesUrl() {
        return getSortedMoveListUrl(SORT_BY_POPULARITY);
    }

    /**
     * Retrieves the URL for acquiring a list of movies that is sorted by highest rating.
     *
     * @return URL The URL of the request or null on error
     */
    public URL getRatingsMoviesUrl() {
        return getSortedMoveListUrl(SORT_BY_RATING);
    }

    /**
     * Retrieves the URL for requesting a list of movies.  The sort order of the movies will be
     * based off of the value passed to movieSortType which can be set to either of the following:
     * SORT_BY_POPULARITY, or SORT_BY_RATING.
     *
     * @param movieSortType The sort order for the movies
     *
     * @return URL The URL of the movie or NULL on failure
     */
    private URL getSortedMoveListUrl(int movieSortType) {
        //  specify if movies should be sorted by popularity or by rating
        String sortAction = (movieSortType == SORT_BY_POPULARITY) ?
                POPULAR_MOVIE_SORT_ACTION : HIGH_RATING_MOVIE_SORT_ACTION;

        String apiKeyV3 = ConfigurationsReader.getApiKey(resources);

        //  construct the movie db connection uri
        Uri uri = new Uri.Builder()
                .scheme(URL_SCHEME)
                .authority(URL_DOMAIN)
                .appendPath(API_V3_IDENTIFIER)
                .appendPath(MOVIE_REQUEST_ACTION)
                .appendPath(sortAction)
                .appendQueryParameter(API_KEY_QUERY_NAME, apiKeyV3)
                .build();

        //  safely convert the uri into a url
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
