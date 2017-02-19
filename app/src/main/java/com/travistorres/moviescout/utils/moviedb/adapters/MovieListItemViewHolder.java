/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.travistorres.moviescout.R;

/**
 * MovieListItemViewHolder
 *
 * A wrapper for the Movie item as it is stored within a RecyclerView.
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

class MovieListItemViewHolder extends RecyclerView.ViewHolder {
    public final ImageView mPosterImageView;

    /**
     * Used for acquiring the image view from the layout resource.
     *
     * @param view The view for the movie item.
     */
    public MovieListItemViewHolder(View view) {
        super(view);

        mPosterImageView = (ImageView) view.findViewById(R.id.movie_poster);
    }
}
