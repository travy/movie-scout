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
 * RemoveFavoriteMovieLoaderTask
 *
 * Will remove a specified movie from the favorites.
 *
 * @author Travis Anthony Torres
 * @version April 9, 2017
 */

public class RemoveFavoriteMovieLoaderTask extends AsyncTaskLoader<Boolean[]> {
    private Bundle bundle;

    /**
     * Sets up the context for the movie to be removed from favorites.
     *
     * @param context
     * @param movie
     */
    public RemoveFavoriteMovieLoaderTask(Context context, Bundle movie) {
        super(context);

        bundle = movie;
    }

    /**
     * Forces the removal of the movie.
     *
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    /**
     * Request that the movie be removed from favorites.
     *
     * @return null or boolean value determining if the movie could be removed
     */
    @Override
    public Boolean[] loadInBackground() {
        Boolean[] wasRemoved = null;
        Context context = getContext();
        String movieKey = context.getString(R.string.selected_movie_extra_key);
        if (bundle.containsKey(movieKey)) {
            Movie movie = bundle.getParcelable(movieKey);
            FavoritesManager favoritesManager = new FavoritesManager(context);
            boolean isFavorite = favoritesManager.isFavorite(movie);
            if (isFavorite) {
                favoritesManager.removeFavorite(movie);
            }

            wasRemoved = new Boolean[] {
                isFavorite
            };
        }

        return wasRemoved;
    }
}
