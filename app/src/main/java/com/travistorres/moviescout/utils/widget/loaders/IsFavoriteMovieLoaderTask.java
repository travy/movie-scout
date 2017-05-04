/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.widget.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.widget.FavoritesManager;

/**
 * IsFavoriteMovieLoaderTask
 *
 * Used for detecting if a specified movie is favorited within the database.
 *
 * @author Travis Anthony Torres
 * @version April 9, 2017
 */

public class IsFavoriteMovieLoaderTask extends AsyncTaskLoader<Boolean[]> {
    private Bundle selectedMovieBundle;

    /**
     * Instantiates a new FavoritesLoader.
     *
     * @param context
     * @param movieBundle
     */
    public IsFavoriteMovieLoaderTask(Context context, Bundle movieBundle) {
        super(context);

        selectedMovieBundle = movieBundle;
    }

    /**
     * Forces the loader to run.
     *
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    /**
     * Determines if the movie is a favorite.
     *
     * @return null if a valid bundle was not provided or a boolean value of `true` or `false` to
     * determine if the movie is a favorite.
     */
    @Override
    public Boolean[] loadInBackground() {
        Boolean[] isFavorite = null;
        Context context = getContext();
        String selectedMovieKey = context.getString(R.string.selected_movie_extra_key);
        if (selectedMovieBundle.containsKey(selectedMovieKey)) {
            Movie movie = selectedMovieBundle.getParcelable(selectedMovieKey);
            FavoritesManager favoritesManager = new FavoritesManager(context);
            isFavorite = new Boolean[] {
                favoritesManager.isFavorite(movie)
            };
        }

        return isFavorite;
    }
}
