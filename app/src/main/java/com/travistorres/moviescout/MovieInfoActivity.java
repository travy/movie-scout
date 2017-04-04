/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.travistorres.moviescout.utils.moviedb.listeners.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.loaders.TrailerLoaderTask;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;

/**
 * MovieInfoActivity
 *
 * Responsible for displaying information regarding a specific Movie object.
 *
 * @author Travis Anthony Torres
 * @version v1.2.0 (March 26, 2017)
 */

public class MovieInfoActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Trailer[]>, MovieDbNetworkingErrorHandler {
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

    private String movieDbApiThreeKey;

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

        //  use a custom app bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupApiPreferences();

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

            //  show the title in the app bar
            CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            layout.setExpandedTitleColor(getResources().getColor(android.R.color.white, getResources().newTheme()));
            layout.setTitle(movie.title);

            //  get the label strings from the resource files
            String releaseDateLabel = getString(R.string.movie_release_date_label);
            String voteAverageLabel = getString(R.string.movie_vote_average_label);
            String popularityLabel = getString(R.string.movie_popularity_rating_label);
            String languageLabel = getString(R.string.movie_language_label);

            //  display information regarding the video
            mMovieTitle.setText(movie.originalTitle);
            mMovieReleaseDate.setText(releaseDateLabel + LABEL_SEPERATOR + movie.getCleanDateFormat());
            mMovieVoteAverage.setText(voteAverageLabel + LABEL_SEPERATOR + movie.voteAverage);
            mMoviePopularity.setText(popularityLabel + LABEL_SEPERATOR + movie.popularity);
            mMovieLanguage.setText(languageLabel + LABEL_SEPERATOR + movie.originalLanguage);
            mMovieOverview.setText(movie.overview);
            retrieveBackdrop(movie);
            retrievePoster(movie);

            //  load the selected movies trailers
            loadMovieTrailers(selectedMovieExtraKey, movie);
        } else {
            //  display an error message when a movie is not defined within the intent.  Should never occur.
            Log.d(LOG_TAG, MISSING_MOVIE_MODEL_LOG_STRING);
            String missingMovieMessage = getString(R.string.missing_movie_model_error_message);
            Toast.makeText(this, missingMovieMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Request the trailers from the server
     *
     * @param movieBundleExtraKey
     * @param movie
     */
    private void loadMovieTrailers(String movieBundleExtraKey, Movie movie) {
        Bundle movieBundle = new Bundle();
        movieBundle.putParcelable(movieBundleExtraKey, movie);

        Resources resources = getResources();
        int loaderKey = resources.getInteger(R.integer.movie_trailer_requester_loader_manager_id);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Trailer[]> loader = loaderManager.getLoader(loaderKey);
        loaderManager.restartLoader(loaderKey, movieBundle, this);
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

    /**
     * Loads the api key from the settings.
     *
     */
    private void setupApiPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //  setup the version 3 api key
        String versionThreeApiSettingsKey = getString(R.string.movie_db_v3_settings_key);
        String versionThreeApiSettingsDefault = getString(R.string.movie_db_v3_settings_default);
        movieDbApiThreeKey = sharedPreferences.getString(versionThreeApiSettingsKey, versionThreeApiSettingsDefault);
    }

    /**
     * Creates a task for constricting of a loader.
     *
     * @param id
     * @param args
     *
     * @return The Loader for acquiring trailers
     */
    @Override
    public Loader<Trailer[]> onCreateLoader(int id, final Bundle args) {
        return new TrailerLoaderTask(this, args, this, movieDbApiThreeKey);
    }

    /**
     * Will either display the Trailer in the list or show a Toash which reads that No Trailers
     * could be found.
     *
     * @param loader
     * @param trailers
     */
    @Override
    public void onLoadFinished(Loader<Trailer[]> loader, Trailer[] trailers) {
        afterNetworkRequest();

        if (trailers != null) {
            for (Trailer trailer : trailers) {
                Log.d(LOG_TAG, trailer.toString());
            }
        } else {
            Toast.makeText(this, "No trailers were found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Trailer[]> loader) {
        //  intentionally left blank
    }

    @Override
    public void onPageNotFound() {

    }

    @Override
    public void onUnauthorizedAccess() {

    }

    @Override
    public void onGeneralNetworkingError() {

    }

    @Override
    public void beforeNetworkRequest() {

    }

    @Override
    public void afterNetworkRequest() {

    }
}
