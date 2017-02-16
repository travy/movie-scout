/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.travistorres.moviescout.R;

/**
 * TODO:  document
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

class MovieListItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mMovieListItemView;

    public MovieListItemViewHolder(View view) {
        super(view);

        mMovieListItemView = (TextView) view.findViewById(R.id.movie_list_item_name);
    }
}
