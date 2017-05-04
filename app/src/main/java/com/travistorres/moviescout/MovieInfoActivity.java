/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.travistorres.moviescout.utils.DateConversionUtility;
import com.travistorres.moviescout.utils.widget.loaders.IsFavoriteMovieLoaderTask;
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
import com.travistorres.moviescout.utils.widget.interfaces.IsMovieFavoritedListener;
import com.travistorres.moviescout.utils.widget.interfaces.OnFavoriteButtonClicked;
import com.travistorres.moviescout.utils.widget.loaders.RemoveFavoriteMovieLoaderTask;
import com.travistorres.moviescout.utils.widget.loaders.SetFavoriteMovieLoaderTask;

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
        implements LoaderManager.LoaderCallbacks<Object[]>, MovieDbNetworkingErrorHandler, TrailerClickedListener, OnFavoriteButtonClicked, IsMovieFavoritedListener {
    private final String LOG_TAG = getClass().getSimpleName();

    private ImageButton mFavoriteMovieButton;
    private ImageView mBackdropImage;
    private ImageView mMoviePoster;
    private Movie selectedMovie;
    private ProgressBar mProgressBar;
    private RecyclerView mReviewListRecyclerView;
    private RecyclerView mTrailerListRecyclerView;
    private String movieDbApiThreeKey;
    private TextView mMovieLanguage;
    private TextView mMovieOverview;
    private TextView mMoviePopularity;
    private TextView mMovieReleaseDate;
    private TextView mMovieTitle;
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

        //  use a custom app bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupApiPreferences();

        //  load page views
        mBackdropImage = (ImageView) findViewById(R.id.movie_backdrop_image_view);
        mFavoriteMovieButton = (ImageButton) findViewById(R.id.favorite_movie_button);
        mFavoriteMovieButton.setVisibility(View.INVISIBLE);
        mMovieLanguage = (TextView) findViewById(R.id.movie_language);
        mMovieOverview = (TextView) findViewById(R.id.movie_overview);
        mMoviePopularity = (TextView) findViewById(R.id.movie_popularity);
        mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMovieVoteAverage = (TextView) findViewById(R.id.movie_vote_average);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator_pb);
        mReviewListRecyclerView = (RecyclerView) findViewById(R.id.movie_review_list);
        mTrailerListRecyclerView = (RecyclerView) findViewById(R.id.movie_trailers_list);

        //  retrieves the key for identifying the selected movie
        String selectedMovieExtraKey = getString(R.string.selected_movie_extra_key);

        //  obtain the selected video and display it's information
        Intent intent = getIntent();
        if (intent.hasExtra(selectedMovieExtraKey)) {
            selectedMovie = intent.getParcelableExtra(selectedMovieExtraKey);
            determineIfMovieIsFavorited(selectedMovie);

            //  show the title in the app bar
            CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            layout.setExpandedTitleColor(getResources().getColor(android.R.color.white, getResources().newTheme()));
            layout.setTitle(selectedMovie.title);

            //  display information regarding the video
            mMovieLanguage.setText(selectedMovie.originalLanguage);
            mMovieOverview.setText(selectedMovie.overview);
            mMoviePopularity.setText(Double.toString(selectedMovie.popularity));
            mMovieReleaseDate.setText(DateConversionUtility.convertNumericalDateToFullDate(this, selectedMovie.releaseDate));
            mMovieTitle.setText(selectedMovie.originalTitle);
            mMovieVoteAverage.setText(Double.toString(selectedMovie.voteAverage));
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
     * Triggers the process of determining if the selected Movie is one of the users favorites.
     *
     * @param movie
     */
    private void determineIfMovieIsFavorited(Movie movie) {
        String selectedMovieKey = getString(R.string.selected_movie_extra_key);
        configureLoaderWithSelectedMovieBundle(selectedMovieKey, movie, R.integer.is_movie_favorited_loader_manager_id);
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
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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
        configureLoaderWithSelectedMovieBundle(movieBundleExtraKey, movie, R.integer.movie_review_requester_loader_manager_id);
    }

    /**
     * Request the trailers from the server
     *
     * @param movieBundleExtraKey
     * @param movie
     */
    private void loadMovieTrailers(String movieBundleExtraKey, Movie movie) {
        configureLoaderWithSelectedMovieBundle(movieBundleExtraKey, movie, R.integer.movie_trailer_requester_loader_manager_id);
    }

    /**
     * Loads a specified LoaderManager into its own execution thread.
     *
     * @param movieBundleExtraKey
     * @param movie
     * @param loaderManagerResourceId
     */
    private void configureLoaderWithSelectedMovieBundle(String movieBundleExtraKey, Movie movie, int loaderManagerResourceId) {
        Bundle movieBundle = new Bundle();
        movieBundle.putParcelable(movieBundleExtraKey, movie);

        configureLoader(movieBundle, loaderManagerResourceId);
    }

    /**
     * Starts a particular Loader Manager and passes in a bundle for it to utilize.
     *
     * @param bundle
     * @param loaderManagerResourceId
     */
    private void configureLoader(Bundle bundle, int loaderManagerResourceId) {
        Resources resources = getResources();
        int loaderKey = resources.getInteger(loaderManagerResourceId);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(loaderKey, bundle, this);
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
        mMoviePoster.setContentDescription(movie.title);
    }

    /**
     * Safely loads the poster resource into it's respective ImageView field.
     *
     * @param movie
     */
    private void retrievePoster(Movie movie) {
        movie.loadPosterIntoImageView(this, mMoviePoster);
        mMoviePoster.setContentDescription(movie.title);
    }

    /**
     * Loads the api key from the settings.
     *
     */
    private void setupApiPreferences() {
        movieDbApiThreeKey = getString(R.string.movie_scout_version_three_api_key);
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
        Loader loader = null;
        if (id == resources.getInteger(R.integer.movie_trailer_requester_loader_manager_id)) {
            loader = new TrailerLoaderTask(this, args, this, movieDbApiThreeKey);
        } else if (id == resources.getInteger(R.integer.movie_review_requester_loader_manager_id)) {
            loader = new ReviewLoaderTask(this, args, this, movieDbApiThreeKey);
        } else if (id == resources.getInteger(R.integer.is_movie_favorited_loader_manager_id)) {
            loader = new IsFavoriteMovieLoaderTask(this, args);
        } else if (id == resources.getInteger(R.integer.set_movie_favorite_loader_manager_id)) {
            loader = new SetFavoriteMovieLoaderTask(this, args);
        } else if (id == resources.getInteger(R.integer.remove_movie_favorite_loader_manager_id)) {
            loader = new RemoveFavoriteMovieLoaderTask(this, args);
        }

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
        } else if (loader instanceof ReviewLoaderTask) {
            finishLoadingReviews((Review[]) array);
        } else if (loader instanceof IsFavoriteMovieLoaderTask) {
            Boolean[] favorites = (Boolean[]) array;
            for (Boolean favorite : favorites) {
                onDeterminedIsMovieFavorited(selectedMovie, favorite);
            }
        } else if (loader instanceof SetFavoriteMovieLoaderTask) {
            Boolean[] favorites = (Boolean[]) array;
            for (Boolean favorite : favorites) {
                if (favorite) {
                    String prefix = getString(R.string.add_movie_to_favorites_prefix_message);
                    String postfix = getString(R.string.add_movie_to_favorites_postfix_message);
                    Toast.makeText(this, prefix + selectedMovie.title + postfix, Toast.LENGTH_SHORT).show();
                }
            }
        } else if (loader instanceof RemoveFavoriteMovieLoaderTask) {
            Boolean[] favorites = (Boolean[]) array;
            for (Boolean favorite : favorites) {
                if (favorite) {
                    String removedMessage = getString(R.string.removed_movie_from_favorites_message);
                    Toast.makeText(this, removedMessage, Toast.LENGTH_SHORT).show();
                }
            }
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
        if (reviews != null) {
            ReviewListAdapter adapter = (ReviewListAdapter) mReviewListRecyclerView.getAdapter();
            adapter.setReviews(reviews);
        } else {
            String noReviewsMessage = getString(R.string.no_reviews_found_for_movie_message);
            Toast.makeText(this, noReviewsMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Specifies the operation to perform when the Loader has reset.
     *
     * @param loader
     */
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

    /**
     * Displays the progress bar.
     *
     */
    @Override
    public void beforeNetworkRequest() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the progress bar.
     *
     */
    @Override
    public void afterNetworkRequest() {
        mProgressBar.setVisibility(View.INVISIBLE);
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
    public void onFavorited(ImageButton buttonView) {
        Bundle selectedMovieBundle = new Bundle();

        //  stores the selected movie into the bundle
        String selectedMovieKey = getString(R.string.selected_movie_extra_key);
        selectedMovieBundle.putParcelable(selectedMovieKey, selectedMovie);

        //  adds all reviews into the bundle
        String moviesReviewsKey = getString(R.string.selected_movies_reviews_extra);
        Review[] reviews = getReviews();
        selectedMovieBundle.putParcelableArray(moviesReviewsKey, reviews);

        //  adds all trailers to into the bundle
        String moviesTrailerKey = getString(R.string.selected_movies_trailers_extra);
        Trailer[] trailers = getTrailers();
        selectedMovieBundle.putParcelableArray(moviesTrailerKey, trailers);

        configureLoader(selectedMovieBundle, R.integer.set_movie_favorite_loader_manager_id);
    }

    /**
     * Retrieves all trailers that have been loaded.
     *
     * @return trailers for the selected movies
     */
    private Trailer[] getTrailers() {
        TrailerListAdapter adapter = (TrailerListAdapter) mTrailerListRecyclerView.getAdapter();
        return adapter.getTrailers();
    }

    /**
     * Retrieves all reviews for the selected movie.
     *
     * @return reviews for the selected movie
     */
    private Review[] getReviews() {
        ReviewListAdapter adapter = (ReviewListAdapter) mReviewListRecyclerView.getAdapter();
        return adapter.getReviews();
    }

    /**
     * Removes the selected Movie and its associated fields from the favorites database.
     *
     * @param buttonView
     */
    @Override
    public void onUnfavorited(ImageButton buttonView) {
        String selectedMovieKey = getString(R.string.selected_movie_extra_key);
        configureLoaderWithSelectedMovieBundle(selectedMovieKey, selectedMovie, R.integer.remove_movie_favorite_loader_manager_id);
    }

    /**
     * Displays the favorite button once it has been determined if the movie has been favorited
     * or not.
     *
     * @param movie
     * @param isFavorite
     */
    @Override
    public void onDeterminedIsMovieFavorited(Movie movie, boolean isFavorite) {
        FavoriteButton favs = new FavoriteButton(mFavoriteMovieButton, isFavorite, this, this);
        mFavoriteMovieButton.setVisibility(View.VISIBLE);
    }
}
