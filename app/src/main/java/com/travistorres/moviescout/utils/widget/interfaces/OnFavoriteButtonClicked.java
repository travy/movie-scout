/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.widget.interfaces;

import android.widget.Button;
import android.widget.ImageButton;

/**
 * OnFavoriteButtonClicked
 *
 * Provides an interface for operations that will be performed depending on if a Favorite button
 * has been pressed or not.
 *
 * @author Travis Anthony Torres
 * @version April 8, 2017
 */

public interface OnFavoriteButtonClicked {
    void onFavorited(ImageButton buttonView);
    void onUnfavorited(ImageButton buttonView);
}
