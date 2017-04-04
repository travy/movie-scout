/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.loaders;

import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.builders.TrailerBuilder;
import com.travistorres.moviescout.utils.moviedb.listeners.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;

/**
 * TrailerLoaderTask
 *
 * Will perform a networking operation to acquire all Trailers that pertain to a specific movie
 * object.
 *
 * @author Travis Anthony Torres
 * @version April 3, 2017
 */

public class TrailerLoaderTask extends AsyncTaskLoader<Trailer[]> {
    private Bundle selectedMovieBundle;
    private AppCompatActivity parentActivity;
    private MovieDbNetworkingErrorHandler errorHandler;
    private String versionThreeApiKey;

    /**
     * Creates a new Loader.  All information for the activity that is running the loader and the
     * tasks that should be completed on error should be passed here.
     *
     * @param activity
     * @param movieBundle
     * @param networkingErrorHandler
     */
    public TrailerLoaderTask(AppCompatActivity activity, Bundle movieBundle, MovieDbNetworkingErrorHandler networkingErrorHandler, String apiKey) {
        super(activity);

        selectedMovieBundle = movieBundle;
        parentActivity = activity;
        errorHandler = networkingErrorHandler;
        versionThreeApiKey = apiKey;
    }

    /**
     * Specifies what to do before the networking task is triggered.
     *
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (selectedMovieBundle == null) {
            return;
        }

        errorHandler.beforeNetworkRequest();
        forceLoad();
    }

    /**
     * Acquires a list of Trailers from the network resource.
     *
     * @return List of trailers or null if there aren't any.
     */
    @Override
    public Trailer[] loadInBackground() {
        Trailer[] trailers = null;

        if (selectedMovieBundle.containsKey(parentActivity.getString(R.string.selected_movie_extra_key))) {
            Movie selectedMovie = selectedMovieBundle.getParcelable(parentActivity.getString(R.string.selected_movie_extra_key));
            trailers = TrailerBuilder.createTrailersArray(getContext(), selectedMovie, errorHandler, versionThreeApiKey);
        }

        return trailers;
    }
}
