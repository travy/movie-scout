/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.travistorres.moviescout.utils.moviedb.MovieDbRequester;
import com.travistorres.moviescout.utils.moviedb.MovieSortType;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieClickedListener;
import com.travistorres.moviescout.utils.moviedb.adapters.MovieListAdapter;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * MainActivity
 *
 * The main activity is the first view area that the user will be brought to when the first open
 * the app.  Here they will be shown a list of Movies that can be sorted either by popularity or
 * highest rating.
 *
 * Each movie shown can then be pressed to reveal information regarding the selected title.
 *
 * @author Travis Anthony Torres
 * @version v1.2.0 (March 28, 2017)
 */

public class MainActivity extends AppCompatActivity
        implements MovieClickedListener, MovieDbNetworkingErrorHandler, SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView mMovieListView;
    private GridLayoutManager mMovieLayoutManager;
    private MovieListAdapter mMovieAdapter;

    private TextView mPageNotFoundTextView;
    private TextView mNetworkingErrorTextView;
    private TextView mUnauthorizedTextView;

    private ProgressBar mLoadingIndicator;

    private MovieDbRequester mMovieRequester;

    private String movieDbApiThreeKey;
    private String movieDbApiFourKey;

    /**
     * Responsible for loading all resource objects and triggering the events that will allow a
     * list of movies to be displayed on the screen.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  obtain all error message objects
        mPageNotFoundTextView = (TextView) findViewById(R.id.page_not_found_error);
        mNetworkingErrorTextView = (TextView) findViewById(R.id.network_connection_failed_error);
        mUnauthorizedTextView = (TextView) findViewById(R.id.api_key_unauthorized_error);

        //  acquire the loading indicator
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator_pb);

        //  determine if the screen needs to be constructed or if a previous state exists
        String mainActivityStateExtra = getString(R.string.main_activity_state_bundle);
        if (savedInstanceState != null && savedInstanceState.containsKey(mainActivityStateExtra)) {
            //  load the previously loaded movies and display the results
            MainActivityParcelable parcelable = savedInstanceState.getParcelable(mainActivityStateExtra);
            int currentPage = parcelable.currentPage;
            MovieSortType sortType = parcelable.sortType;
            Movie[] movieList = parcelable.movieList;

            mMovieRequester = new MovieDbRequester(this, this, this);
            mMovieRequester.setCurrentPage(currentPage);
            mMovieRequester.setSortType(sortType);

            setupMovieView(movieList);
            updateMovieApiKey(false, true);
        } else {
            //  create a movie request object and display the interface
            mMovieRequester = new MovieDbRequester(this, this, this);
            setupMovieView();
            updateMovieApiKey();
        }
    }

    /**
     * Configures the ListView to display movie results.
     *
     * @param movieList Contains an array of Movies that were previously loaded in the GridLayout.
     *                  This field should be left as null if there are no movies that have been
     *                  previously loaded.
     */
    private void setupMovieView(@Nullable  Movie[] movieList) {
        //  sets up the requester object
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        int deviceOrientation = configuration.orientation;
        int gridLayoutColumnCount = deviceOrientation == Configuration.ORIENTATION_PORTRAIT ?
                resources.getInteger(R.integer.movie_grid_layout_manager_portrait_column_count) :
                resources.getInteger(R.integer.movie_grid_layout_manager_landscape_column_count);

        //  sets the number of columns in the grid layout
        mMovieLayoutManager = new GridLayoutManager(this, gridLayoutColumnCount);
        mMovieAdapter = mMovieRequester.getAdapter();
        if (movieList != null) {
            mMovieAdapter.setMoviesList(movieList);
        }

        //  configures the recycler view
        mMovieListView = (RecyclerView) findViewById(R.id.movie_list_rv);
        mMovieListView.setAdapter(mMovieAdapter);
        mMovieListView.setLayoutManager(mMovieLayoutManager);
    }

    /**
     * Configures the ListView to display the movie Results.  The list with default to being empty.
     *
     */
    private void setupMovieView() {
        setupMovieView(null);
    }

    /**
     * Clears any cached results from previous network requests and then performs a new requests
     * with any new values passed for the API keys.
     *
     * @param shouldReset Specifies if the movie requester instance should be reset or not.
     * @param shouldRequest Specifies if a new page should be requested.  This is generally useful
     *                      if the current page is 1.
     */
    private void updateMovieApiKey(boolean shouldReset, boolean shouldRequest) {
        //  hides the unauthorized message
        mUnauthorizedTextView.setVisibility(TextView.INVISIBLE);

        //  updates the api keys based on the settings
        setupApiPreferences();

        //  resets the counter fields in the request object if requested
        if (shouldReset) {
            mMovieRequester.reset();
        }

        //  specifies the api keys in the requester
        mMovieRequester.setApiKeys(movieDbApiThreeKey, movieDbApiFourKey);

        //  request the next page if necessary
        if (shouldRequest) {
            mMovieRequester.requestNext();
        }
    }

    /**
     * Updates the movie api key and forces both the requester to reset and a new page be requested
     * from the server.
     *
     */
    private void updateMovieApiKey() {
        updateMovieApiKey(true, true);
    }

    /**
     * Save the movie results.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String mainActivityStateExtra = getString(R.string.main_activity_state_bundle);
        MainActivityParcelable parcelable = new MainActivityParcelable();
        parcelable.sortType = mMovieRequester.getSortType();
        parcelable.currentPage = mMovieRequester.getCurrentPage();
        parcelable.movieList = mMovieAdapter.getMovies();

        outState.putParcelable(mainActivityStateExtra, parcelable);
    }

    /**
     * Cleans up system resources when the Activity is being destroyed.
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //  stop listening to changes in preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Acquires the settings for the application and stores them so that they are easily
     * accessible.  Will also redirect the user to the Settings page if they have not yet provided
     * an API access key.
     *
     */
    private void setupApiPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //  setup the version 3 api key
        String versionThreeApiSettingsKey = getString(R.string.movie_db_v3_settings_key);
        String versionThreeApiSettingsDefault = getString(R.string.movie_db_v3_settings_default);
        movieDbApiThreeKey = sharedPreferences.getString(versionThreeApiSettingsKey, versionThreeApiSettingsDefault);

        //  setup the version four api key
        String versionFourApiSettingsKey = getString(R.string.movie_db_v4_settings_key);
        String versionFourApiSettingsDefault = getString(R.string.movie_db_v4_settings_default);
        movieDbApiFourKey = sharedPreferences.getString(versionFourApiSettingsKey, versionFourApiSettingsDefault);

        //  direct the user to the settings page if the api keys have not been specified
        if (!wereMovieDbApiKeysSet()) {
            String missingKeyMessage = getString(R.string.missing_api_keys);
            Toast.makeText(this, missingKeyMessage, Toast.LENGTH_SHORT).show();
            loadSettingsPage();
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Determines if both the Movie DB API v3 and v4 access keys have been provided.
     *
     * @return boolean `true` if both keys have a proper value defined as by the
     * `wasMovieDbApiKeySpecified` method call.
     */
    private boolean wereMovieDbApiKeysSet() {
        return wasMovieDbApiKeySpecified(movieDbApiThreeKey) &&
                wasMovieDbApiKeySpecified(movieDbApiFourKey);
    }

    /**
     * Determines if a specified API access key was provided.  A key is determined to be valid, if
     * it is not null and is composed of at least one character (excluding white space).
     *
     * @param apiKey The API key to check the validity of.
     *
     * @return boolean `true` if the key is neither null or an empty string and `false` otherwise.
     */
    private boolean wasMovieDbApiKeySpecified(String apiKey) {
        return apiKey != null && apiKey.trim().length() > 1;
    }

    /**
     * Allows the menu to be displayed on the activity bar.
     *
     * @param menu
     *
     * @return true since the menu should always be shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    /**
     * Determines the action to be performed when a button is pressed.
     *
     * @param item The item that was pressed by the user.
     *
     * @return some response
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  determine how to sort the system
        switch (item.getItemId()) {
            case R.id.popularity_sort_button:
                sortMovies(MovieSortType.MOST_POPULAR);
                break;
            case R.id.rating_sort_button:
                sortMovies(MovieSortType.HIGHEST_RATED);
                break;
            case R.id.favorite_movies_menu_item:
                sortMovies(MovieSortType.FAVORITES);
                break;
            case R.id.settings_menu_button:
                loadSettingsPage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sorts movies and updates the list.
     *
     * @param sortType
     */
    private void sortMovies(MovieSortType sortType) {
        mMovieRequester.setSortType(sortType);
        mMovieRequester.reset();
        mMovieRequester.requestNext();
    }

    /**
     * Request that the Settings page be displayed.
     *
     */
    private void loadSettingsPage() {
        Context context = this;
        Class settingsClass = SettingsActivity.class;
        Intent displaySettingsIntent = new Intent(context, settingsClass);
        startActivity(displaySettingsIntent);
    }

    /**
     * Opens a new page where users will be able to see information regarding the Movie title that
     * they selected from the list.
     *
     * @param clickedMovie The selected movie
     */
    @Override
    public void onClick(Movie clickedMovie) {
        Context context = this;
        Class movieInfoPage = MovieInfoActivity.class;
        String selectedMovieExtraKey = getString(R.string.selected_movie_extra_key);
        Intent intent = new Intent(context, movieInfoPage);
        intent.putExtra(selectedMovieExtraKey, clickedMovie);

        startActivity(intent);
    }

    /**
     * Changes the visibility of the views modified by the networking thread.
     *
     * @param view The view to be modified
     * @param visibility The level of visibility
     */
    private void networkErrorHandlerUiViewVisibility(final View view, final int visibility) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(visibility);
            }
        });
    }

    /**
     * Displays the page not found error whenever a network resources returns a 404 response.
     *
     */
    @Override
    public void onPageNotFound() {
        networkErrorHandlerUiViewVisibility(mPageNotFoundTextView, View.VISIBLE);
    }

    /**
     * Displays an access denied message whenever the MovieDBAPI rejects access to a request.
     *
     */
    @Override
    public void onUnauthorizedAccess() {
        networkErrorHandlerUiViewVisibility(mUnauthorizedTextView, View.VISIBLE);
    }

    /**
     * Displays a message when a generic networking error occurs.
     *
     */
    @Override
    public void onGeneralNetworkingError() {
        networkErrorHandlerUiViewVisibility(mNetworkingErrorTextView, View.VISIBLE);
    }

    /**
     * Displays the Progress bar whenever a network resource is requested.
     *
     */
    @Override
    public void beforeNetworkRequest() {
        networkErrorHandlerUiViewVisibility(mLoadingIndicator, View.VISIBLE);
    }

    /**
     * Hides the Progress bar after acquiring the system resource.
     *
     */
    @Override
    public void afterNetworkRequest() {
        networkErrorHandlerUiViewVisibility(mLoadingIndicator, View.INVISIBLE);
    }

    /**
     * Takes the changes made and updates the state of the application based on them.
     *
     * @param sharedPreferences shared preference provider
     * @param s the key of the setting that was changed
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s == getString(R.string.movie_db_v3_settings_key) ||
                s == getString(R.string.movie_db_v4_settings_key)) {
            updateMovieApiKey();
        }
    }
}
