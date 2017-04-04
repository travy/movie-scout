/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.interfaces;

import com.travistorres.moviescout.utils.moviedb.models.Trailer;

/**
 * TrailerClickedListener
 *
 * Declares an interface for a clickable object.
 *
 * @author Travis Anthony Torres
 * @version April 3, 2017
 */

public interface TrailerClickedListener {
    void onClick(Trailer trailer);
}
