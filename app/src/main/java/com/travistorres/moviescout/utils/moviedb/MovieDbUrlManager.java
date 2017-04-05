/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb;

import android.content.Context;
import android.net.Uri;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * MovieDbUrlManager
 *
 * Provides URL's that are capable of safely retrieving content from the Movie DB API.  Using the
 * URI objects generated from this class will ensure that URL's are always well formed.
 *
 * @author Travis Anthony Torres
 * @version v1.2.0 (March 26, 2017)
 */

public class MovieDbUrlManager {
    /*
     *  The context is needed in order to read from the systems Configurations.
     *
     */
    private Context context;

    /**
     * Configures a new URL Manager by providing a context which will provide system
     * configurations.
     *
     * @param mContext
     */
    public MovieDbUrlManager(Context mContext) {
        context = mContext;
    }

    /**
     * Retrieves the URL for requesting a list of movies.
     *
     * @param sortType The sort order for the movies
     *
     * @return URL The URL of the movie or NULL on failure
     */
    public URL getSortedMoveListUrl(MovieSortType sortType, int pageNumber, String versionThreeApiKey) {
        //  read request parameters from the strings resource
        String httpScheme = context.getString(R.string.movie_db_api_uri_scheme);
        String domainName = context.getString(R.string.movie_db_api_uri_domain);
        String apiVersion = context.getString(R.string.movie_db_api_v3_identifier);
        String movieRequest = context.getString(R.string.movie_db_api_movie_request_action);
        String apiKeyQuery = context.getString(R.string.movie_db_api_key_query_key);
        String pageQuery = context.getString(R.string.movie_db_api_page_query_key);

        //  specify if movies should be sorted by popularity or by rating
        String sortAction = (sortType == MovieSortType.MOST_POPULAR) ?
                context.getString(R.string.movie_db_api_popular_movie_sort_action) :
                context.getString(R.string.movie_db_api_top_rating_sort_action);

        //  construct the movie db connection uri
        Uri uri = new Uri.Builder()
                .scheme(httpScheme)
                .authority(domainName)
                .appendPath(apiVersion)
                .appendPath(movieRequest)
                .appendPath(sortAction)
                .appendQueryParameter(apiKeyQuery, versionThreeApiKey)
                .appendQueryParameter(pageQuery, Integer.toString(pageNumber))
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
        String defaultImageSize = context.getString(R.string.tmdb_image_size_default);

        return getMoviePosterUrl(resourceName, defaultImageSize);
    }

    /**
     * Constructs a URL that will acquire the Backdrop image for the Movie.
     *
     * @param resourceName
     *
     * @return A properly formatted URL for acquiring the movies backdrop
     */
    public URL getMovieBackdropUrl(String resourceName) {
        String defaultImageSize = context.getString(R.string.tmdb_image_size_1280);

        return getMoviePosterUrl(resourceName, defaultImageSize);
    }

    /**
     * Constructs a URL for obtaining a list of trailers for a movie.
     *
     * @param movie
     * @param versionThreeApiKey
     *
     * @return URL to request trailers for a specific movie
     */
    public URL getTrailersUrl(Movie movie, String versionThreeApiKey) {
        //  read request parameters from the strings resource
        String httpScheme = context.getString(R.string.movie_db_api_uri_scheme);
        String domainName = context.getString(R.string.movie_db_api_uri_domain);
        String apiVersion = context.getString(R.string.movie_db_api_v3_identifier);
        String movieRequest = context.getString(R.string.movie_db_api_movie_request_action);
        String movieId = Integer.toString(movie.id);
        String trailerRequest = context.getString(R.string.movie_db_api_trailer_action);
        String apiKeyQuery = context.getString(R.string.movie_db_api_key_query_key);

        //  construct the movie db connection uri
        Uri uri = new Uri.Builder()
                .scheme(httpScheme)
                .authority(domainName)
                .appendPath(apiVersion)
                .appendPath(movieRequest)
                .appendPath(movieId)
                .appendPath(trailerRequest)
                .appendQueryParameter(apiKeyQuery, versionThreeApiKey)
                .build();

        return getUrl(uri);
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
        //  request configurations for forming request on the tmdb database
        String httpScheme = context.getString(R.string.tmdb_uri_scheme);
        String domainName = context.getString(R.string.tmdb_uri_domain);
        String pathOne = context.getString(R.string.tmdb_uri_image_host_path_one);
        String pathTwo = context.getString(R.string.tmdb_uri_image_host_path_two);

        //  construct the uri to make requests on
        Uri uri = new Uri.Builder()
                .scheme(httpScheme)
                .authority(domainName)
                .appendPath(pathOne)
                .appendPath(pathTwo)
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

    /**
     * Constructs a URL where the specified Trailer can be viewed.
     *
     * @param trailer
     *
     * @return URL where the trailer can be viewed
     */
    public URL getVideoTrailerUrl(Trailer trailer) {
        String httpScheme = context.getString(R.string.youtube_scheme);
        String domainName = context.getString(R.string.youtube_domain);
        String videoParam = context.getString(R.string.youtube_video_param);
        String videoQuery = context.getString(R.string.youtube_video_identifier_query);
        String videoIdentifier = trailer.key;

        //  construct the uri to make requests on
        Uri uri = new Uri.Builder()
                .scheme(httpScheme)
                .authority(domainName)
                .appendPath(videoParam)
                .appendQueryParameter(videoQuery, videoIdentifier)
                .build();

        return getUrl(uri);
    }

    /**
     * Retrieves the URL for acquiring the movies Reviews.
     *
     * @param movie
     * @param versionThreeApiKey
     *
     * @return URL used to request reviews
     */
    public URL getMovieReviewsUrl(Movie movie, String versionThreeApiKey) {
        //  read request parameters from the strings resource
        String httpScheme = context.getString(R.string.movie_db_api_uri_scheme);
        String domainName = context.getString(R.string.movie_db_api_uri_domain);
        String apiVersion = context.getString(R.string.movie_db_api_v3_identifier);
        String movieRequest = context.getString(R.string.movie_db_api_movie_request_action);
        String movieId = Integer.toString(movie.id);
        String reviewRequest = context.getString(R.string.movie_db_api_reviews_action);
        String apiKeyQuery = context.getString(R.string.movie_db_api_key_query_key);

        Uri uri = new Uri.Builder()
                .scheme(httpScheme)
                .authority(domainName)
                .appendPath(apiVersion)
                .appendPath(movieRequest)
                .appendPath(movieId)
                .appendPath(reviewRequest)
                .appendQueryParameter(apiKeyQuery, versionThreeApiKey)
                .build();

        return getUrl(uri);
    }
}
