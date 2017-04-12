/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.widget.FavoritesManager;

/**
 * FavoriteMovieLoaderTask
 *
 * Loads all of the users Favorite movies.
 *
 * @author Travis Anthony Torres
 * @version April 10, 2017
 */

public class FavoriteMovieLoaderTask extends AsyncTaskLoader<Movie[]> {
    /**
     * Configures the context of the loader.
     *
     * @param context
     */
    public FavoriteMovieLoaderTask(Context context) {
        super(context);
    }

    /**
     * Specifies what to perform when the loader starts running.
     *
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    /**
     * Acquires all of the users favorites.
     *
     * @return array of all of the users favorites or null if none have been set.
     */
    @Override
    public Movie[] loadInBackground() {
        Context context = getContext();
        FavoritesManager favoritesManager = new FavoritesManager(context);
        return favoritesManager.getFavorites();
    }
}
