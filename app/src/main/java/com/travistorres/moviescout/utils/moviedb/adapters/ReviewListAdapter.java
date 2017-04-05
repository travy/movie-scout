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
import com.travistorres.moviescout.utils.moviedb.models.Review;

import java.util.ArrayList;

/**
 * ReviewListAdapter
 *
 * Converts the contents of a Loaded Review object into a form that can be displayed onto the
 * user interface for the user to see and interact with.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListItemViewHolder> {
    ArrayList<Review> reviewList;

    /**
     * Creates a new view for displaying Reviews.
     *
     * @param parent
     * @param viewType
     *
     * @return Review interface
     */
    @Override
    public ReviewListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View layout = layoutInflater.inflate(R.layout.movie_review_item, parent, false);

        return new ReviewListItemViewHolder(layout);
    }

    /**
     * Binds the contents of the Review onto the user interface.
     *
     * @param reviewItemViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ReviewListItemViewHolder reviewItemViewHolder, int position) {
        Review review = reviewList.get(position);

        reviewItemViewHolder.mAuthorTextView.setText(review.author);
        reviewItemViewHolder.mContentTextView.setText(review.content);
    }

    /**
     * Retrieves the number of items contained within the List
     *
     * @return number of Reviews
     */
    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    /**
     * Stores the reviews in the List so they can be displayed.
     *
     * @param reviews
     */
    public void setReviews(Review[] reviews) {
        if (reviewList == null) {
            reviewList = new ArrayList<>(reviews.length);
        }

        for (int i = 0; i < reviews.length; ++i) {
            Review review = reviews[i];
            reviewList.add(review);
        }

        notifyDataSetChanged();
    }
}
