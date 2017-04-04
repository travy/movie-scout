/**
 * Copyright (C) 2017 Travis Anthony Torres
 */
package com.travistorres.moviescout.utils.moviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.interfaces.TrailerClickedListener;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;

import java.util.ArrayList;

/**
 * TrailerListAdapter
 *
 * The TrailerList will inflate a set of TrailerItemViewHolders with information obtained from
 * Trailer objects.  The Trailers will be provided as a set of information at runtime and new
 * information can be passed in on the fly.
 *
 * @author Travis Anthony Torres
 * @version April 3, 2017
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerItemViewHolder> {
    ArrayList<Trailer> trailerList;

    final TrailerClickedListener clickListener;

    /**
     * Specifies the TrailerClickedListener that will determine the operation to perform when
     * a Trailer is clicked.
     *
     * @param trailerClickListener
     */
    public TrailerListAdapter(TrailerClickedListener trailerClickListener) {
        clickListener = trailerClickListener;
    }

    /**
     * Instantiates a new TrailerItemViewHolder for use by a supplied Trailer object.
     *
     * @param parent
     * @param viewType
     *
     * @return TrailerItemViewHolder
     */
    @Override
    public TrailerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View trailerView = layoutInflater.inflate(R.layout.movie_trailer_item, parent, false);

        return new TrailerItemViewHolder(trailerView, this);
    }

    /**
     * Displays the contents of a Trailer object onto the specified TrailerItemViewHolder.
     *
     * @param trailerView
     * @param position
     */
    @Override
    public void onBindViewHolder(TrailerItemViewHolder trailerView, int position) {
        Trailer trailer = trailerList.get(position);
        trailerView.mTitleTextView.setText(trailer.name);
        trailerView.mTypeTextView.setText(trailer.type);
        trailerView.mVideoLanguage.setText(trailer.iso_639_1);
    }

    /**
     * Specifies the number of items in the list.
     *
     * @return Number of trailers loaded in the adapter
     */
    @Override
    public int getItemCount() {
        return trailerList == null ? 0 : trailerList.size();
    }

    /**
     * Supplies a set of Trailers to be inflated onto a view.
     *
     * @param trailers
     */
    public void setTrailers(Trailer[] trailers) {
        if (trailerList == null) {
            trailerList = new ArrayList<>(trailers.length);
        }

        for (Trailer trailer : trailers) {
            trailerList.add(trailer);
        }

        notifyDataSetChanged();
    }
}
