/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb;

import android.content.Context;
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
    private final static String HIGH_RATING_MOVIE_SORT_ACTION = "top_rated";
    private final static String API_KEY_QUERY_NAME = "api_key";

    /**
     * Constants used for acquiring image resources.
     *
     */
    private final static String IMAGE_HOSTING_DOMAIN = "image.tmdb.org";
    private final static String IMAGE_HOSTING_PATH_ONE = "t";
    private final static String IMAGE_HOSTING_PATH_TWO = "p";

    /**
     * Image Size Constants
     *
     */
    private final static String W92_IMAGE_SIZE = "w92";
    private final static String W154_IMAGE_SIZE = "w154";
    private final static String W185_IMAGE_SIZE = "w185";
    private final static String W342_IMAGE_SIZE = "w342";
    private final static String W500_IMAGE_SIZE = "w500";
    private final static String W780_IMAGE_SIZE = "w780";
    private final static String ORIGINAL_IMAGE_SIZE = "original";

    /**
     * Default Image Size Constant.  By modifying this constant, you can easily modify the size
     * of the movie posters as they appear in the movie list.
     *
     */
    private final static String DEFAULT_IMAGE_SIZE = W342_IMAGE_SIZE;

    /*
     * Specifies values used for determining a users preference on how movies should be sorted
     * when requested using the API.
     *
     */
    public final static int SORT_BY_POPULARITY = 0x00;
    public final static int SORT_BY_RATING = 0x01;

    private Context context;

    /**
     * Allocates memory for the builder and provides a system resource manager.
     *
     * @param context System context
     */
    public MovieDbUrlManager(Context context) {
        setContext(context);
    }

    /**
     * Specify the resources object.
     *
     * @param mContext The context of the request
     */
    public void setContext(Context mContext) {
        context = mContext;
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
    public URL getSortedMoveListUrl(int movieSortType) {
        //  specify if movies should be sorted by popularity or by rating
        String sortAction = (movieSortType == SORT_BY_POPULARITY) ?
                POPULAR_MOVIE_SORT_ACTION : HIGH_RATING_MOVIE_SORT_ACTION;
        String apiKeyV3 = ConfigurationsReader.getApiKey(context);

        //  construct the movie db connection uri
        Uri uri = new Uri.Builder()
                .scheme(URL_SCHEME)
                .authority(URL_DOMAIN)
                .appendPath(API_V3_IDENTIFIER)
                .appendPath(MOVIE_REQUEST_ACTION)
                .appendPath(sortAction)
                .appendQueryParameter(API_KEY_QUERY_NAME, apiKeyV3)
                .build();

        return getUrl(uri);
    }

    /**
     * Retrieves the URL for acquiring a Movie Poster.
     *
     * @param resourceName The resource name of the image on the server.
     *
     * @return A properly formatted URL for acquiring the movie poster.
     */
    public URL getMoviePosterUrl(String resourceName) {
        return getMoviePosterUrl(resourceName, DEFAULT_IMAGE_SIZE);
    }

    /**
     * Retrieves the URL for acquiring a Movie Poster
     *
     * @param resourceName The resource name of the image on the server.
     * @param posterSize The size that the poster should appear as.
     *
     * @return A properly formatted URL for acquiring the movie poster.
     */
    public URL getMoviePosterUrl(String resourceName, String posterSize) {
        Uri uri = new Uri.Builder()
                .scheme(URL_SCHEME)
                .authority(IMAGE_HOSTING_DOMAIN)
                .appendPath(IMAGE_HOSTING_PATH_ONE)
                .appendPath(IMAGE_HOSTING_PATH_TWO)
                .appendPath(posterSize)
                .appendPath(resourceName)
                .build();

        return getUrl(uri);
    }

    /**
     * Converts a URI object into a valid URL.
     *
     * @param uri The URI to convert into a URL
     *
     * @return URL packaged uri
     */
    private static URL getUrl(Uri uri) {
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
