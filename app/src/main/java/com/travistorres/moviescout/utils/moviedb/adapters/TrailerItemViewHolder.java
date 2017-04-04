/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.travistorres.moviescout.R;

/**
 * TrailerItemViewHolder
 *
 * Creates a package level interface for determining how to interact with a trailer.
 *
 * @author Travis Anthony Torres
 * @version April 3, 2017
 */

class TrailerItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTitleTextView;
    public final TextView mTypeTextView;
    public final TextView mVideoLanguage;

    /**
     * Constructs a new view for the Trailer instance and specifies how to map data to each
     * data field.
     *
     * @param view
     */
    public TrailerItemViewHolder(View view) {
        super(view);

        //  acquire a reference to each of the views
        mTitleTextView = (TextView) view.findViewById(R.id.video_title);
        mTypeTextView = (TextView) view.findViewById(R.id.video_type);
        mVideoLanguage = (TextView) view.findViewById(R.id.video_language);
    }
}
