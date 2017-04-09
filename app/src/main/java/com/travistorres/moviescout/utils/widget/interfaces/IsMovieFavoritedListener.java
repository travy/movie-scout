package com.travistorres.moviescout.utils.widget.interfaces;

import com.travistorres.moviescout.utils.moviedb.models.Movie;

public interface IsMovieFavoritedListener {
    void onDeterminedIsMovieFavorited(Movie movie, boolean isFavorite);
}
