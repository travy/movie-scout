/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.builders;

import android.content.Context;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * MovieBuilder
 *
 * Constructs movies from JSON objects that are provided from the movie db server.
 *
 * @author Travis Anthony Torres
 * @version April 30, 2017
 */

public class MovieBuilder {
    /**
     * Total results from the requests.
     *
     * @param context
     * @param json
     *
     * @return Number of movies on the server
     */
    public static int totalResults(Context context, String json) {
        return getNumberFromResponse(context, json, R.string.movie_parser_json_total_results);
    }

    /**
     * Total pages in the response.
     *
     * @param context
     * @param json
     *
     * @return Extracts the total number of pages from the json request
     */
    public static int totalPages(Context context, String json) {
        return getNumberFromResponse(context, json, R.string.movie_parser_json_total_pages);
    }

    /**
     * Acquires an integral value from the response based on some resource string.
     *
     * @param context
     * @param json
     * @param resource
     *
     * @return Integral value
     */
    private static int getNumberFromResponse(Context context, String json, int resource) {
        int numericValue = -1;

        try {
            JSONObject jsonObject = new JSONObject(json);
            numericValue = jsonObject.getInt(context.getString(resource));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return numericValue;
    }

    /**
     * Constructs an array of movies based on a given json object.
     *
     * @param context
     * @param jsonObject
     *
     * @return array of movies
     */
    public static Movie[] createMovies(Context context, String jsonObject) {
        Movie[] movies = null;

        try {
            String resultsKey = context.getString(R.string.movie_parser_json_results);
            JSONObject parser = new JSONObject(jsonObject);
            JSONArray resultArray = parser.optJSONArray(resultsKey);
            int listSize = resultArray.length();

            //  map the JSON results to the movie array
            movies = new Movie[listSize];
            for (int i = 0; i < listSize; ++i) {
                JSONObject movieJson = (JSONObject) resultArray.get(i);
                movies[i] = createMovie(context, movieJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    /**
     * Will construct a Movie object based off of the information provided by the json object.
     *
     * @param context
     * @param json
     *
     * @return movie object acquired from json object
     *
     * @throws JSONException
     */
    public static Movie createMovie(Context context, JSONObject json) throws JSONException {
        String prefix = context.getString(R.string.movie_parser_json_poster_path_prefix);
        String noCharacter = context.getString(R.string.movie_parser_json_poster_path_prefix_off);

        Movie movie = new Movie();
        movie.posterPath = json.getString(context.getString(R.string.movie_parser_json_poster_path)).replace(prefix, noCharacter);
        movie.isAdultFilm = json.getBoolean(context.getString(R.string.movie_parser_json_adult));
        movie.overview = json.getString(context.getString(R.string.movie_parser_json_overview));
        movie.releaseDate = json.getString(context.getString(R.string.movie_parser_json_release_date));
        movie.genreIds = new int[1];
        movie.id = json.getInt(context.getString(R.string.movie_parser_json_id));
        movie.originalTitle = json.getString(context.getString(R.string.movie_parser_json_original_title));
        movie.originalLanguage = json.getString(context.getString(R.string.movie_parser_json_original_language));
        movie.title = json.getString(context.getString(R.string.movie_parser_json_title));
        movie.backdropPath = json.getString(context.getString(R.string.movie_parser_json_backdrop_path)).replace(prefix, noCharacter);
        movie.popularity = json.getDouble(context.getString(R.string.movie_parser_json_popularity));
        movie.voteCount = json.getInt(context.getString(R.string.movie_parser_json_vote_count));
        movie.hasVideo = json.getBoolean(context.getString(R.string.movie_parser_json_video));
        movie.voteAverage = json.getDouble(context.getString(R.string.movie_parser_json_vote_average));

        return movie;
    }
}
