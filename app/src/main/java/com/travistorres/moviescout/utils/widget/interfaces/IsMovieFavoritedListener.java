/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.widget.interfaces;

import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * IsMovieFavoritedListener
 *
 * Used to determine when a movie is or is not a favorite within the SQLite database.
 *
 * @author Travis Anthony Torres
 * @version April 9, 2017
 */

public interface IsMovieFavoritedListener {
    void onDeterminedIsMovieFavorited(Movie movie, boolean isFavorite);
}
