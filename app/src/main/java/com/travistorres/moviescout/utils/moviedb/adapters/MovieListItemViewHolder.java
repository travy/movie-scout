/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * MovieListItemViewHolder
 *
 * A wrapper for the Movie item as it is stored within a RecyclerView.
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

class MovieListItemViewHolder extends RecyclerView.ViewHolder
        implements OnClickListener {
    private final MovieListAdapter container;

    public final ImageView mPosterImageView;

    /**
     * Used for acquiring the image view from the layout resource.
     *
     * @param view The view for the movie item.
     */
    public MovieListItemViewHolder(View view, MovieListAdapter adapter) {
        super(view);

        container = adapter;
        mPosterImageView = (ImageView) view.findViewById(R.id.movie_poster);

        //  allow the view to act as the listener
        view.setOnClickListener(this);
    }

    /**
     * Triggers the onClick event within the Adapter.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.d("clicked", "detected click");
        int position = getAdapterPosition();
        Movie clickedMovie = container.movieList[position];
        container.clickHandler.onClick(clickedMovie);
    }
}
