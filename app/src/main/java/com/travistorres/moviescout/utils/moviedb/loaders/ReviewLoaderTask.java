/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.loaders;

import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;

import com.travistorres.moviescout.utils.moviedb.interfaces.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.models.Review;

/**
 * ReviewLoaderTask
 *
 * Responsible for managing threads utilized for Loading of Movie Reviews in the background.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public class ReviewLoaderTask extends AsyncTaskLoader<Review[]> {
    private AppCompatActivity parent;
    private Bundle bundle;
    private MovieDbNetworkingErrorHandler errorHandler;
    private String apiKey;

    /**
     * Constructs the Loader instance.
     *
     * @param activity
     * @param movieBundle
     * @param networkingErrorHandler
     * @param movieDbApiKey
     */
    public ReviewLoaderTask(AppCompatActivity activity, Bundle movieBundle, MovieDbNetworkingErrorHandler networkingErrorHandler, String movieDbApiKey) {
        super(activity);

        parent = activity;
        bundle = movieBundle;
        errorHandler = networkingErrorHandler;
        apiKey = movieDbApiKey;
    }

    /**
     * Specify what to do before loading.
     *
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        errorHandler.beforeNetworkRequest();
        forceLoad();
    }

    /**
     * Instructs how to request the reviews for a specified Movie object.
     *
     * @return List of Reviews that were loaded from the network resource.
     */
    @Override
    public Review[] loadInBackground() {
        return new Review[0];
    }
}
