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

import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * MovieInfoActivity
 *
 * Responsible for displaying information regarding a specific Movie object.
 *
 * @author Travis Anthony Torres
 * @version v1.2.0 (March 26, 2017)
 */

public class MovieInfoActivity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();
    private final static String MISSING_MOVIE_MODEL_LOG_STRING = "Activity triggered without a selected movie being specified";

    //  used for separating labels from their data
    private final static String LABEL_SEPERATOR = ":  ";

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mMovieReleaseDate;
    private TextView mMovieOverview;
    private TextView mMovieLanguage;
    private TextView mMoviePopularity;
    private TextView mMovieVoteAverage;
    private ImageView mBackdropImage;

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
        mBackdropImage = (ImageView) findViewById(R.id.movie_backdrop_image_view);

        //  retrieves the key for identifying the selected movie
        String selectedMovieExtraKey = getString(R.string.selected_movie_extra_key);

        //  obtain the selected video and display it's information
        Intent intent = getIntent();
        if (intent.hasExtra(selectedMovieExtraKey)) {
            Movie movie = (Movie) intent.getParcelableExtra(selectedMovieExtraKey);

            //  get the label strings from the resource files
            String releaseDateLabel = getString(R.string.movie_release_date_label);
            String voteAverageLabel = getString(R.string.movie_vote_average_label);
            String popularityLabel = getString(R.string.movie_popularity_rating_label);
            String languageLabel = getString(R.string.movie_language_label);

            //  display information regarding the video
            retrieveBackdrop(movie);
            mMovieTitle.setText(movie.originalTitle);
            mMovieReleaseDate.setText(releaseDateLabel + LABEL_SEPERATOR + movie.getCleanDateFormat());
            mMovieVoteAverage.setText(voteAverageLabel + LABEL_SEPERATOR + movie.voteAverage);
            mMoviePopularity.setText(popularityLabel + LABEL_SEPERATOR + movie.popularity);
            mMovieLanguage.setText(languageLabel + LABEL_SEPERATOR + movie.originalLanguage);
            mMovieOverview.setText(movie.overview);
            retrievePoster(movie);
        } else {
            //  display an error message when a movie is not defined within the intent.  Should never occur.
            Log.d(LOG_TAG, MISSING_MOVIE_MODEL_LOG_STRING);
            String missingMovieMessage = getString(R.string.missing_movie_model_error_message);
            Toast.makeText(this, missingMovieMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays the backdrop of the movie in the app bar.
     *
     * @param movie
     */
    private void retrieveBackdrop(Movie movie) {
        movie.loadBackdropIntoImageView(this, mBackdropImage);
    }

    /**
     * Safely loads the poster resource into it's respective ImageView field.
     *
     * @param movie
     */
    private void retrievePoster(Movie movie) {
        movie.loadPosterIntoImageView(this, mMoviePoster);
    }
}
