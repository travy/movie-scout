/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.builders;

import android.content.Context;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.networking.UrlManager;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;
import com.travistorres.moviescout.utils.networking.NetworkManager;
import com.travistorres.moviescout.utils.networking.exceptions.HttpConnectionTimeoutException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpPageNotFoundException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpUnauthorizedException;
import com.travistorres.moviescout.utils.networking.exceptions.NetworkingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * TrailerBuilder
 *
 * Will construct Trailer objects that are associated with a specific Movie entry.
 *
 * @author Travis Anthony Torres
 * @version April 3, 2017
 */

public class TrailerBuilder {
    /**
     * Creates an array of Trailers that are located on the movie servers for a particular Movie
     * instance.
     *
     * @param context
     * @param movie
     * @param errorHandler
     *
     * @return An array of trailers acquired from the movie server or null if no trailers are
     * associated with the Movie.
     */
    public static Trailer[] createTrailersArray(Context context, Movie movie, MovieDbNetworkingErrorHandler errorHandler, String versionThreeApiKey) {
        UrlManager urlManager = new UrlManager(context);
        URL trailerRequestUrl = urlManager.getTrailersUrl(movie, versionThreeApiKey);
        Trailer[] trailers = null;

        try {
            String json = NetworkManager.request(trailerRequestUrl);
            if (json != null) {
                JSONObject jsonData = new JSONObject(json);
                JSONArray resultsArray = jsonData.getJSONArray(context.getString(R.string.movie_trailer_json_results));
                trailers = constructTrailers(context, resultsArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

        return trailers;
    }

    /**
     * Will construct an array of Trailers using a JSONArray that is acquired from a Network
     * request.
     *
     * @param trailerData Json array which can be used to construct Trailers.
     *
     * @return Array of Trailers or null if no trailers could be constructed.
     *
     * @throws JSONException
     */
    private static Trailer[] constructTrailers(Context context, JSONArray trailerData)
            throws JSONException {
        Trailer[] trailers = null;
        int trailerCount = trailerData.length();
        if (trailerCount > 0) {
            trailers = new Trailer[trailerCount];
            for (int i = 0; i < trailerCount; ++i) {
                JSONObject meta = trailerData.getJSONObject(i);
                trailers[i] = constructTrailer(context, meta);
            }
        }

        return trailers;
    }

    /**
     * Builds a single Trailer object from a JSON representation.
     *
     * @param trailerData
     *
     * @return Trailer object
     *
     * @throws JSONException
     */
    private static Trailer constructTrailer(Context context, JSONObject trailerData)
            throws JSONException {
        Trailer trailer = new Trailer();
        trailer.id = trailerData.getString(context.getString(R.string.movie_trailer_json_id));
        trailer.iso_639_1 = trailerData.getString(context.getString(R.string.movie_trailer_json_iso_639_1));
        trailer.iso_3166_1 = trailerData.getString(context.getString(R.string.movie_trailer_json_iso_3166_1));
        trailer.key = trailerData.getString(context.getString(R.string.movie_trailer_json_key));
        trailer.name = trailerData.getString(context.getString(R.string.movie_trailer_json_name));
        trailer.site = trailerData.getString(context.getString(R.string.movie_trailer_json_site));
        trailer.size = trailerData.getString(context.getString(R.string.movie_trailer_json_size));
        trailer.type = trailerData.getString(context.getString(R.string.movie_trailer_json_type));

        return trailer;
    }
}
