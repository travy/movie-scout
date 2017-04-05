/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.builders;

import android.content.Context;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.MovieDbUrlManager;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.moviedb.models.Review;
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
 * ReviewBuilder
 *
 * Will construct a Review object(s) based on provided configurations.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public class ReviewBuilder {
    /**
     * Creates an array of Reviews that are associated with a specified Movie instance.
     *
     * @param context
     * @param movie
     * @param apiKey
     *
     * @return Array of Reviews for the provided Movie.
     */
    public static final Review[] createReviewsForMovie(Context context, MovieDbNetworkingErrorHandler errorHandler, Movie movie, String apiKey) {
        Review[] reviews = null;
        MovieDbUrlManager urlManager = new MovieDbUrlManager(context);
        URL reviewsUrl = urlManager.getMovieReviewsUrl(movie, apiKey);

        try {
            String jsonResults = NetworkManager.request(reviewsUrl);
            JSONObject jsonObject = new JSONObject(jsonResults);

            reviews = createReviewsArrayFromJson(context, jsonObject);
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

        return reviews;
    }

    /**
     * Will construct an array of Reviews based on the data provided by some given JsonObject that
     * was acquired from MovieDbApi.org.
     *
     * @param context
     * @param jsonObject
     *
     * @return Array of Reviews constructed from the provided JSON object.
     */
    public static final Review[] createReviewsArrayFromJson(Context context, JSONObject jsonObject)
            throws JSONException {

        JSONArray jsonArray = jsonObject.getJSONArray(context.getString(R.string.movie_review_json_results));
        Review[] reviews = constructReviews(context, jsonArray);

        return reviews;
    }

    /**
     * Constructs an Array of Reviews that were acquired from a Network resource with JSON.
     *
     * @param context
     * @param reviewArray
     *
     * @return Array of Reviews
     *
     * @throws JSONException
     */
    private static Review[] constructReviews(Context context, JSONArray reviewArray)
            throws JSONException {
        Review[] reviews = null;
        int reviewCount = reviewArray.length();
        if (reviewCount > 0) {
            reviews = new Review[reviewCount];
            for (int i = 0; i < reviewCount; ++i) {
                JSONObject meta = reviewArray.getJSONObject(i);
                reviews[i] = constructReview(context, meta);
            }
        }

        return reviews;
    }

    /**
     * Constructs a new Review object based off of the data stored within the JSON object generated
     * by the server.
     *
     * @param context
     * @param reviewData
     *
     * @return Review object
     *
     * @throws JSONException
     */
    private static Review constructReview(Context context, JSONObject reviewData)
            throws JSONException {
        Review review = new Review();
        review.url = reviewData.getString(context.getString(R.string.movie_review_json_url));
        review.movieId = null;
        review.id = reviewData.getString(context.getString(R.string.movie_review_json_id));
        review.content = reviewData.getString(context.getString(R.string.movie_review_json_content));
        review.author = reviewData.getString(context.getString(R.string.movie_review_json_author));

        return review;
    }
}
