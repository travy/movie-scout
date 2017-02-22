/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.listeners;

import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * MovieClickedListener
 *
 * This interface provides an interface for triggering the proper response to a click event.
 *
 * @author Travis Anthony Torres
 * @version February 19, 2017
 */

public interface MovieClickedListener {
    void onClick(Movie clickedMovie);
}
