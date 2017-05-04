/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.travistorres.moviescout.R;

/**
 * ReviewListItemViewHolder
 *
 * Displays the contents of a Review object on the user interface.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

class ReviewListItemViewHolder extends RecyclerView.ViewHolder {
    public TextView mAuthorTextView;
    public TextView mContentTextView;

    /**
     * Acquires the View items that make up the interface.
     *
     * @param view
     */
    public ReviewListItemViewHolder(View view) {
        super(view);

        mAuthorTextView = (TextView) view.findViewById(R.id.review_author);
        mContentTextView = (TextView) view.findViewById(R.id.review_content);
    }
}
