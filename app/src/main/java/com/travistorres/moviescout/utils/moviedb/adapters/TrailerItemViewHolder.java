/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;

/**
 * TrailerItemViewHolder
 *
 * Creates a package level interface for determining how to interact with a trailer.
 *
 * @author Travis Anthony Torres
 * @version April 3, 2017
 */

class TrailerItemViewHolder extends RecyclerView.ViewHolder
        implements OnClickListener {
    private final TrailerListAdapter adapter;

    public final TextView mTitleTextView;
    public final TextView mTypeTextView;
    public final TextView mVideoLanguage;

    /**
     * Constructs a new view for the Trailer instance and specifies how to map data to each
     * data field.
     *
     * @param view
     */
    public TrailerItemViewHolder(View view, TrailerListAdapter trailerAdapter) {
        super(view);

        adapter = trailerAdapter;

        //  acquire a reference to each of the views
        mTitleTextView = (TextView) view.findViewById(R.id.video_title);
        mTypeTextView = (TextView) view.findViewById(R.id.video_type);
        mVideoLanguage = (TextView) view.findViewById(R.id.video_language);

        view.setOnClickListener(this);
    }

    /**
     * Trigger the click event on the Info Activity.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        Trailer trailer = adapter.trailerList.get(position);
        adapter.clickListener.onClick(trailer);
    }
}
