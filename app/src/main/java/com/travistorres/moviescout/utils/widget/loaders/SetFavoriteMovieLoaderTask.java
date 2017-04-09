/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.widget.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.widget.FavoritesManager;

/**
 * SetFavoriteMovieLoaderTask
 *
 * Adds a movie to the users Favorites.
 *
 * @author Travis Anthony Torres
 * @version April, 9, 2017
 */

public class SetFavoriteMovieLoaderTask extends AsyncTaskLoader<Boolean[]> {
    private Bundle movieBundle;

    /**
     * Sets the context for adding the Movie to favorites.
     *
     * @param context
     * @param bundle
     */
    public SetFavoriteMovieLoaderTask(Context context, Bundle bundle) {
        super(context);

        movieBundle = bundle;
    }

    /**
     * Forces the loader to start.
     *
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    /**
     * Adds the favorites in the background.
     *
     * @return null if the movie could not be resolved and either `true` or `false` depending on
     * if the movie could be added to favorites.
     */
    @Override
    public Boolean[] loadInBackground() {
        Boolean[] results = null;
        Context context = getContext();
        String selectedMovieKey = context.getString(R.string.selected_movie_extra_key);
        if (movieBundle.containsKey(selectedMovieKey)) {
            Movie movie = movieBundle.getParcelable(selectedMovieKey);
            FavoritesManager favoritesManager = new FavoritesManager(context);
            boolean isFavorite = favoritesManager.isFavorite(movie);
            if (!isFavorite) {
                favoritesManager.addFavorite(movie, null, null);
            }

            results = new Boolean[] {
                !isFavorite
            };
        }

        return results;
    }
}
