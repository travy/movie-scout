/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb;

import android.content.Context;

import com.travistorres.moviescout.utils.moviedb.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * MovieDbParser
 *
 * Static class which will parse out Movie data from a given JSON string.  The JSON String is
 * expected to be in the format provided by the MovieDB API.
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

public class MovieDbParser {
    private final static String RESULTS_KEY = "results";
    private final static String POSTER_PATH_KEY = "poster_path";
    private final static String ADULT_KEY = "adult";
    private final static String OVERVIEW_KEY = "overview";
    private final static String RELEASE_DATE_KEY = "release_date";
    private final static String GENRE_IDS_KEY = "genre_ids";
    private final static String ID_KEY = "id";
    private final static String ORIGINAL_TITLE_KEY = "original_title";
    private final static String ORIGINAL_LANGUAGE_KEY = "original_language";
    private final static String TITLE_KEY = "title";
    private final static String BACKDROP_PATH_KEY = "backdrop_path";
    private final static String POPULARITY_KEY = "popularity";
    private final static String VOTE_COUNT_KEY = "vote_count";
    private final static String VIDEO_KEY = "video";
    private final static String VOTE_AVERAGE_KEY = "vote_average";

    /**
     * Retrieves the total number of movies stored within the Movie API database.
     *
     * @param json
     *
     * @return Total number of movies
     */
    public static int acquireTotalResults(String json) {
        int totalResults = -1;

        try {
            JSONObject jsonObject = new JSONObject(json);
            totalResults = jsonObject.getInt("total_results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return totalResults;
    }

    /**
     * Acquires the total number of pages that can be accessed on the resource.
     *
     * @param json
     *
     * @return Total number of pages that contain valid data
     */
    public static int acquireTotalPages(String json) {
        int totalPages = -1;

        try {
            JSONObject jsonObject = new JSONObject(json);
            totalPages = jsonObject.getInt("total_pages");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return totalPages;
    }

    /**
     * Parse the given json string to acquire an array of Movie objects.
     *
     * @param json Response string from the service.
     * @param context The applications context
     *
     * @return List of Movies
     */
    public static Movie[] retrieveMovieList(String json, Context context) {
        Movie[] movieList = null;

        try {
            JSONObject parser = new JSONObject(json);
            JSONArray resultArray = parser.optJSONArray(RESULTS_KEY);
            int listSize = resultArray.length();

            //  map the JSON results to the movie array
            movieList = new Movie[listSize];
            for (int i = 0; i < listSize; ++i) {
                JSONObject movieJson = (JSONObject) resultArray.get(i);
                movieList[i] = mapJsonToMovie(movieJson, context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieList;
    }

    /**
     * Given a specific Movie Json object, will map each field to the appropriate data member on
     * a Movie object.
     *
     * @param json Data to be mapped
     * @param context The application context
     *
     * @return Movie object
     *
     * @throws JSONException When a referenced key was not found
     */
    private static Movie mapJsonToMovie(JSONObject json, Context context)
            throws JSONException {
        Movie movie = new Movie(context);

        movie.posterPath = json.getString(POSTER_PATH_KEY).replace("/", "");
        movie.isAdultFilm = json.getBoolean(ADULT_KEY);
        movie.overview = json.getString(OVERVIEW_KEY);
        movie.releaseDate = json.getString(RELEASE_DATE_KEY);
        movie.genreIds = new int[1];
        movie.id = json.getInt(ID_KEY);
        movie.originalTitle = json.getString(ORIGINAL_TITLE_KEY);
        movie.originalLanguage = json.getString(ORIGINAL_LANGUAGE_KEY);
        movie.title = json.getString(TITLE_KEY);
        movie.backdropPath = json.getString(BACKDROP_PATH_KEY);
        movie.popularity = json.getDouble(POPULARITY_KEY);
        movie.voteCount = json.getInt(VOTE_COUNT_KEY);
        movie.hasVideo = json.getBoolean(VIDEO_KEY);
        movie.voteAverage = json.getDouble(VOTE_AVERAGE_KEY);

        return movie;
    }
}
