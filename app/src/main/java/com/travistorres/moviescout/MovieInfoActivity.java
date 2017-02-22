/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.travistorres.moviescout.utils.moviedb.exceptions.NoContextException;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * MovieInfoActivity
 *
 * Responsible for displaying information regarding a specific Movie object.
 *
 * @author Travis Anthony Torres
 * @version February 19, 2017
 */

public class MovieInfoActivity extends AppCompatActivity {
    /*
     *  Meta data.
     *
     */
    private final static String RELEASE_LABEL = "Released:  ";
    private final static String POPULARITY_LABEL = "Popularity:  ";
    private final static String LANGUAGE_LABEL = "Language:  ";
    private final static String VOTE_AVERAGE_LABEL = "Vote Average:  ";
    private final static String MISSING_MOVIE_MODEL_LOG_STRING = "Activity triggered without a selected movie being specified";
    private final static String MISSING_MOVIE_MODEL_TOAST = "Unable to acquire Movie data";

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mMovieReleaseDate;
    private TextView mMovieOverview;
    private TextView mMovieLanguage;
    private TextView mMoviePopularity;
    private TextView mMovieVoteAverage;

    /**
     * Loads information regarding the selected video and displays it's meta-data on the screen
     * in a clean user readable format.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        //  load page views
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        mMovieOverview = (TextView) findViewById(R.id.movie_overview);
        mMovieLanguage = (TextView) findViewById(R.id.movie_language);
        mMoviePopularity = (TextView) findViewById(R.id.movie_popularity);
        mMovieVoteAverage = (TextView) findViewById(R.id.movie_vote_average);

        //  obtain the selected video and display it's information
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.SELECTED_MOVIE_EXTRA)) {
            Movie movie = (Movie) intent.getParcelableExtra(MainActivity.SELECTED_MOVIE_EXTRA);
            movie.setContext(this);

            //  display information regarding the video
            mMovieTitle.setText(movie.originalTitle);
            mMovieReleaseDate.setText(RELEASE_LABEL + movie.getCleanDateFormat());
            mMovieVoteAverage.setText(VOTE_AVERAGE_LABEL + movie.voteAverage);
            mMoviePopularity.setText(POPULARITY_LABEL + movie.popularity);
            mMovieLanguage.setText(LANGUAGE_LABEL + movie.originalLanguage);
            mMovieOverview.setText(movie.overview);
            retrievePoster(movie);
        } else {
            //  display an error message when a movie is not defined within the intent.  Should never occur.
            Log.d(getClass().toString(), MISSING_MOVIE_MODEL_LOG_STRING);
            Toast.makeText(this, MISSING_MOVIE_MODEL_TOAST, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Safely loads the poster resource into it's respective ImageView field.
     *
     * @param movie
     */
    private void retrievePoster(Movie movie) {
        try {
            movie.loadPosterIntoImageView(mMoviePoster);
        } catch (NoContextException e) {
            e.printStackTrace();
        }
    }
}
