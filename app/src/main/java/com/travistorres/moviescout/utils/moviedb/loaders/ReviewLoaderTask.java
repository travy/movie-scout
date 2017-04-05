/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.travistorres.moviescout.utils.moviedb.models.Review;

/**
 * ReviewLoaderTask
 *
 * Responsible for managing threads utilized for Loading of Movie Reviews in the background.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public class ReviewLoaderTask extends AsyncTaskLoader<Review[]> {
    /**
     * Constructs the Loader instance.
     *
     * @param context
     */
    public ReviewLoaderTask(Context context) {
        super(context);
    }

    /**
     * Instructs how to request the reviews for a specified Movie object.
     *
     * @return List of Reviews that were loaded from the network resource.
     */
    @Override
    public Review[] loadInBackground() {
        return new Review[0];
    }
}
