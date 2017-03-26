/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb;

import android.net.Uri;

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
    //  TODO- Move all String contants in this file to the Strings.xml file
    //  TODO- Convert to a singleton

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
    private final static String PAGE_QUERY_NAME = "page";

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

    /**
     * Retrieves the URL for requesting a list of movies.
     *
     * @param sortType The sort order for the movies
     *
     * @return URL The URL of the movie or NULL on failure
     */
    public URL getSortedMoveListUrl(MovieSortType sortType, int pageNumber, String versionThreeApiKey) {
        //  specify if movies should be sorted by popularity or by rating
        String sortAction = (sortType == MovieSortType.MOST_POPULAR) ?
                POPULAR_MOVIE_SORT_ACTION : HIGH_RATING_MOVIE_SORT_ACTION;

        //  construct the movie db connection uri
        Uri uri = new Uri.Builder()
                .scheme(URL_SCHEME)
                .authority(URL_DOMAIN)
                .appendPath(API_V3_IDENTIFIER)
                .appendPath(MOVIE_REQUEST_ACTION)
                .appendPath(sortAction)
                .appendQueryParameter(API_KEY_QUERY_NAME, versionThreeApiKey)
                .appendQueryParameter(PAGE_QUERY_NAME, Integer.toString(pageNumber))
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
