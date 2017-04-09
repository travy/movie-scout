/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.travistorres.moviescout.utils.db.MoviesDatabase;
import com.travistorres.moviescout.utils.db.tables.MoviesTable;
import com.travistorres.moviescout.utils.moviedb.adapters.ReviewListAdapter;
import com.travistorres.moviescout.utils.moviedb.adapters.TrailerListAdapter;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.interfaces.TrailerClickedListener;
import com.travistorres.moviescout.utils.moviedb.loaders.ReviewLoaderTask;
import com.travistorres.moviescout.utils.moviedb.loaders.TrailerLoaderTask;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.moviedb.models.Review;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;
import com.travistorres.moviescout.utils.widget.buttons.FavoriteButton;
import com.travistorres.moviescout.utils.widget.interfaces.OnFavoriteButtonClicked;

import java.net.URL;

/**
 * MovieInfoActivity
 *
 * Responsible for displaying information regarding a specific Movie object.
 *
 * @author Travis Anthony Torres
 * @version v1.2.0 (March 26, 2017)
 */

public class MovieInfoActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Object[]>, MovieDbNetworkingErrorHandler, TrailerClickedListener, OnFavoriteButtonClicked {
    private final String LOG_TAG = getClass().getSimpleName();

    //  used for separating labels from their data
    private final static String LABEL_SEPERATOR = ":  ";

    private Movie selectedMovie;

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mMovieReleaseDate;
    private TextView mMovieOverview;
    private TextView mMovieLanguage;
    private TextView mMoviePopularity;
    private TextView mMovieVoteAverage;
    private ImageView mBackdropImage;
    private RecyclerView mTrailerListRecyclerView;
    private RecyclerView mReviewListRecyclerView;
    private Button mFavoriteMovieButton;

    private SQLiteDatabase mDatabase;

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
        mTrailerListRecyclerView = (RecyclerView) findViewById(R.id.movie_trailers_list);
        mReviewListRecyclerView = (RecyclerView) findViewById(R.id.movie_review_list);
        mFavoriteMovieButton = (Button) findViewById(R.id.favorite_movie_button);

        //  retrieves the key for identifying the selected movie
        String selectedMovieExtraKey = getString(R.string.selected_movie_extra_key);

        //  obtain the selected video and display it's information
        Intent intent = getIntent();
        if (intent.hasExtra(selectedMovieExtraKey)) {
            selectedMovie = intent.getParcelableExtra(selectedMovieExtraKey);

            //  Sets up the behavior of the favorites button
            MoviesTable moviesTable = new MoviesTable(getApplicationContext(), false);
            mDatabase = moviesTable.getDatabase();
            boolean isFavorite = moviesTable.isFavorite(selectedMovie);
            FavoriteButton favState = new FavoriteButton(mFavoriteMovieButton, isFavorite, this);

            //  show the title in the app bar
            CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            layout.setExpandedTitleColor(getResources().getColor(android.R.color.white, getResources().newTheme()));
            layout.setTitle(selectedMovie.title);

            //  get the label strings from the resource files
            String releaseDateLabel = getString(R.string.movie_release_date_label);
            String voteAverageLabel = getString(R.string.movie_vote_average_label);
            String popularityLabel = getString(R.string.movie_popularity_rating_label);
            String languageLabel = getString(R.string.movie_language_label);

            //  display information regarding the video
            mMovieTitle.setText(selectedMovie.originalTitle);
            mMovieReleaseDate.setText(releaseDateLabel + LABEL_SEPERATOR + selectedMovie.getCleanDateFormat());
            mMovieVoteAverage.setText(voteAverageLabel + LABEL_SEPERATOR + selectedMovie.voteAverage);
            mMoviePopularity.setText(popularityLabel + LABEL_SEPERATOR + selectedMovie.popularity);
            mMovieLanguage.setText(languageLabel + LABEL_SEPERATOR + selectedMovie.originalLanguage);
            mMovieOverview.setText(selectedMovie.overview);
            retrieveBackdrop(selectedMovie);
            retrievePoster(selectedMovie);

            //  load the selected movies trailers
            setupTrailerRecyclerView();
            loadMovieTrailers(selectedMovieExtraKey, selectedMovie);
            setupReviewRecyclerView();
            loadMovieReviews(selectedMovieExtraKey, selectedMovie);
        } else {
            //  display an error message when a movie is not defined within the intent.  Should never occur.
            Log.e(LOG_TAG, getString(R.string.movie_info_activity_missing_movie_message));
            String missingMovieMessage = getString(R.string.missing_movie_model_error_message);
            Toast.makeText(this, missingMovieMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Specifies the operations to perform as the Activity is shutting down.
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        destroyMovieTrailerLoader();
        destroyReviewLoader();
    }

    /**
     * Sets up the recycler view for displaying Reviews.
     *
     */
    private void setupReviewRecyclerView() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.Adapter adapter = new ReviewListAdapter();
        mReviewListRecyclerView.setAdapter(adapter);
        mReviewListRecyclerView.setLayoutManager(linearLayout);
    }

    /**
     * Sets up the RecyclerView so that it will display the contents of the Adapter.
     *
     */
    private void setupTrailerRecyclerView() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.Adapter adapter = new TrailerListAdapter(this);
        mTrailerListRecyclerView.setAdapter(adapter);
        mTrailerListRecyclerView.setLayoutManager(linearLayout);
    }

    /**
     * Request that reviews be loaded for the movie.
     *
     * @param movieBundleExtraKey
     * @param movie
     */
    private void loadMovieReviews(String movieBundleExtraKey, Movie movie) {
        configureLoader(movieBundleExtraKey, movie, R.integer.movie_review_requester_loader_manager_id);
    }

    /**
     * Request the trailers from the server
     *
     * @param movieBundleExtraKey
     * @param movie
     */
    private void loadMovieTrailers(String movieBundleExtraKey, Movie movie) {
        configureLoader(movieBundleExtraKey, movie, R.integer.movie_trailer_requester_loader_manager_id);
    }

    /**
     * Loads a specified LoaderManager into its own execution thread.
     *
     * @param movieBundleExtraKey
     * @param movie
     * @param loaderManagerResourceId
     */
    private void configureLoader(String movieBundleExtraKey, Movie movie, int loaderManagerResourceId) {
        Bundle movieBundle = new Bundle();
        movieBundle.putParcelable(movieBundleExtraKey, movie);

        Resources resources = getResources();
        int loaderKey = resources.getInteger(loaderManagerResourceId);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader = loaderManager.getLoader(loaderKey);
        loaderManager.restartLoader(loaderKey, movieBundle, this);
    }

    /**
     * Destroys any threads that are being used to load Movie Trailers.
     *
     */
    private void destroyMovieTrailerLoader() {
        destroyLoader(R.integer.movie_trailer_requester_loader_manager_id);
    }

    /**
     * Destroys any threads used for loading of reviews.
     *
     */
    private void destroyReviewLoader() {
        destroyLoader(R.integer.movie_review_requester_loader_manager_id);
    }

    /**
     * Instructs to destroy a specified LoaderManager instance.
     *
     * @param loaderManagerResourceId
     */
    private void destroyLoader(int loaderManagerResourceId) {
        Resources resources = getResources();
        int loaderManagerId = resources.getInteger(loaderManagerResourceId);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.destroyLoader(loaderManagerId);
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
    public Loader onCreateLoader(int id, final Bundle args) {
        Resources resources = getResources();
        Loader loader = (id == resources.getInteger(R.integer.movie_trailer_requester_loader_manager_id)) ?
                new TrailerLoaderTask(this, args, this, movieDbApiThreeKey) :
                new ReviewLoaderTask(this, args, this, movieDbApiThreeKey);

        return loader;
    }

    /**
     * Will either display the Trailer in the list or show a Toash which reads that No Trailers
     * could be found.
     *
     * @param loader
     * @param array
     */
    @Override
    public void onLoadFinished(Loader loader, Object[] array) {
        afterNetworkRequest();

        if (loader instanceof TrailerLoaderTask) {
            finishLoadingTrailers((Trailer[]) array);
        } else {
            finishLoadingReviews((Review[]) array);
        }
    }

    /**
     * Specifies the operation to be performed after trailers have been loaded.
     *
     * @param trailers
     */
    private void finishLoadingTrailers(Trailer[] trailers) {
        if (trailers != null) {
            TrailerListAdapter adapter = (TrailerListAdapter) mTrailerListRecyclerView.getAdapter();
            adapter.setTrailers(trailers);
        } else {
            Toast.makeText(this, getString(R.string.no_movie_trailer_found), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Specifies the operation to be performed after reviews have been loaded.
     *
     * @param reviews
     */
    private void finishLoadingReviews(Review[] reviews) {
        //  TODO-  display the reviews
        Log.d(getClass().getSimpleName(), "Reviews loaded but feature not implemented!");
        if (reviews != null) {
            ReviewListAdapter adapter = (ReviewListAdapter) mReviewListRecyclerView.getAdapter();
            adapter.setReviews(reviews);
        } else {
            Toast.makeText(this, "No Reviews were found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Object[]> loader) {
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

    /**
     * Creates an Intent which will show the trailer in Youtube.
     *
     * @param trailer
     */
    @Override
    public void onClick(Trailer trailer) {
        URL trailerUrl = trailer.getVideoUrl(this);
        String trailerUrlString = trailerUrl.toString();
        Uri trailerUri = Uri.parse(trailerUrlString);

        Intent playTrailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        startActivity(playTrailerIntent);
    }

    /**
     * Will add the Movie and it's associated Reviews and Trailers to the favorites database where
     * it will be able to be accessed offline at any time.
     *
     * @param buttonView
     */
    @Override
    public void onFavorited(Button buttonView) {
        MoviesTable moviesTable = new MoviesTable(getApplicationContext(), mDatabase);
        boolean isFavorite = moviesTable.isFavorite(selectedMovie);
        if (!isFavorite) {
            moviesTable.save(selectedMovie);
            Toast.makeText(this, "Added Favorite", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Removes the selected Movie and its associated fields from the favorites database.
     *
     * @param buttonView
     */
    @Override
    public void onUnfavorited(Button buttonView) {
        MoviesTable moviesTable = new MoviesTable(getApplicationContext(), mDatabase);
        boolean isFavorite = moviesTable.isFavorite(selectedMovie);
        if (isFavorite) {
            moviesTable.deleteMovie(selectedMovie);
            Toast.makeText(this, "Removed Favorite", Toast.LENGTH_SHORT).show();
        }
    }
}
