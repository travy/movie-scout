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
import com.travistorres.moviescout.utils.moviedb.MovieDbRequester;
import com.travistorres.moviescout.utils.moviedb.listeners.MovieClickedListener;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

import java.util.ArrayList;

/**
 * Maps the results of the provided MovieList data set to the ActivityView in an efficient manner.
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListItemViewHolder> {
    private static final int REMAINING_ITEMS_REFRESH_LIMIT = 5;

    ArrayList<Movie> movieList;

    final MovieClickedListener clickHandler;
    final MovieDbRequester movieRequester;

    /**
     * Allows the MovieClickListener operation to be specified.
     *
     * @param onClickListener Listener object which defines the operation to be performed when a
     * movie has been selected.
     */
    public MovieListAdapter(MovieClickedListener onClickListener, MovieDbRequester requester) {
        clickHandler = onClickListener;
        movieRequester = requester;
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
        Context context = movieRequester.getContext();
        Movie currentMovie = movieList.get(position);
        currentMovie.loadPosterIntoImageView(context, holder.mPosterImageView);

        //  acquire more results when nearing the end of the list
        if (position == getItemCount() - REMAINING_ITEMS_REFRESH_LIMIT) {
            movieRequester.requestNext();
        }
    }

    /**
     * Retrieves the total number of elements in the list.
     *
     * @return Count of movies
     */
    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    /**
     * Retrieves an array of Movies that have been defined within the Adapter.
     *
     * @return Movie[] An array of previously loaded movies.
     */
    public Movie[] getMovies() {
        if (movieList == null) {
            return null;
        }

        int movieCount = movieList.size();

        return movieList.toArray(new Movie[movieCount]);
    }

    /**
     * Updates the movie data set.
     *
     * @param list
     */
    public void setMoviesList(Movie[] list) {
        //  allocates memory for an ArrayList
        if (movieList == null) {
            movieList = new ArrayList<>();
        }

        //  stores all contents within the list
        for (Movie movie : list) {
            movieList.add(movie);
        }

        notifyDataSetChanged();
    }

    /**
     * Clears out the data stored within the Adapter.
     *
     */
    public void empty() {
        movieList = null;
        notifyDataSetChanged();
    }
}
