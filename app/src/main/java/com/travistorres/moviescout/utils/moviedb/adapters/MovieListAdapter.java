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
import com.travistorres.moviescout.utils.moviedb.exceptions.NoContextException;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * Maps the results of the provided MovieList data set to the ActivityView in an efficient manner.
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListItemViewHolder> {
    Movie[] movieList;

    final MovieClickedListener clickHandler;

    /**
     * Allows the MovieClickListener operation to be specified.
     *
     * @param onClickListener Listener object which defines the operation to be performed when a
     * movie has been selected.
     */
    public MovieListAdapter(MovieClickedListener onClickListener) {
        clickHandler = onClickListener;
    }

    /**
     * Constructs a new view for the movie item to be displayed on.
     *
     * @param parent
     * @param viewType
     *
     * @return The generated ViewHolder
     */
    @Override
    public MovieListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);

        return new MovieListItemViewHolder(view, this);
    }

    /**
     * Maps the title of the movie to the list.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MovieListItemViewHolder holder, int position) {
        Movie currentMovie = movieList[position];
        try {
            currentMovie.loadPosterIntoImageView(holder.mPosterImageView);
        } catch (NoContextException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the total number of elements in the list.
     *
     * @return Count of movies
     */
    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.length;
    }

    /**
     * Updates the movie data set.
     *
     * @param list
     */
    public void setMoviesList(Movie[] list) {
        movieList = list;
        notifyDataSetChanged();
    }
}
