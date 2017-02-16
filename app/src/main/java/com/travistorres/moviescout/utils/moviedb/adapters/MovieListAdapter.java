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
import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * TODO:  document
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListItemViewHolder> {
    private int currentPageNumber;
    private int totalPages;
    private int totalResults;

    private Movie[] movieList;

    @Override
    public MovieListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);

        return new MovieListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListItemViewHolder holder, int position) {
        //  TODO:  request the poster and display that instead of the title
        Movie currentMovie = movieList[position];

        holder.mMovieListItemView.setText(currentMovie.title);
    }

    @Override
    public int getItemCount() {
        //  TODO:  return totalResults after testing with a single page to acquire all results from the database
        return movieList == null ? 0 : movieList.length;
    }

    public void setMoviesList(Movie[] list) {
        movieList = list;
    }
}
