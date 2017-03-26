/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.travistorres.moviescout.utils.moviedb.listeners.MovieClickedListener;
import com.travistorres.moviescout.utils.moviedb.adapters.MovieListAdapter;
import com.travistorres.moviescout.utils.moviedb.listeners.MovieDbNetworkingErrorHandler;
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
 * @version v1.2.0 (March 26, 2017)
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

        //  sets up the requester object
        Resources resources = getResources();
        int gridLayoutColumnCount = resources.getInteger(R.integer.movie_grid_layout_manager_column_count);
        mMovieRequester = new MovieDbRequester(this, this, this);
        mMovieLayoutManager = new GridLayoutManager(this, gridLayoutColumnCount);
        mMovieAdapter = mMovieRequester.getAdapter();

        //  configures the recycler view
        mMovieListView = (RecyclerView) findViewById(R.id.movie_list_rv);
        mMovieListView.setAdapter(mMovieAdapter);
        mMovieListView.setLayoutManager(mMovieLayoutManager);
    }

    /**
     * Re-affirms the state of the Activity when it has been unpaused.
     */
    @Override
    protected void onResume() {
        super.onResume();

        //  Checks the applications preferences
        setupPreferences();

        //  Starts the movie loads
        mUnauthorizedTextView.setVisibility(TextView.INVISIBLE);
        mMovieRequester.setApiKeys(movieDbApiThreeKey, movieDbApiFourKey);
        mMovieRequester.requestNext();
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
    private void setupPreferences() {
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
     * Displays the page not found error whenever a network resources returns a 404 response.
     *
     */
    @Override
    public void onPageNotFound() {
        mPageNotFoundTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * Displays an access denied message whenever the MovieDBAPI rejects access to a request.
     *
     */
    @Override
    public void onUnauthorizedAccess() {
        mUnauthorizedTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * Displays a message when a generic networking error occurs.
     *
     */
    @Override
    public void onGeneralNetworkingError() {
        mNetworkingErrorTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * Displays the Progress bar whenever a network resource is requested.
     *
     */
    @Override
    public void beforeNetworkRequest() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the Progress bar after acquiring the system resource.
     *
     */
    @Override
    public void afterNetworkRequest() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    /**
     * Takes the changes made and updates the state of the application based on them.
     *
     * @param sharedPreferences shared preference provider
     * @param s the key of the setting that was changed
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s == getString(R.string.movie_db_v3_settings_key)) {
            String defaultValue = getString(R.string.movie_db_v3_settings_default);
            movieDbApiThreeKey = sharedPreferences.getString(s, defaultValue);
        } else if (s == getString(R.string.movie_db_v4_settings_key)) {
            String defaultValue = getString(R.string.movie_db_v4_settings_default);
            movieDbApiFourKey = sharedPreferences.getString(s, defaultValue);
        }
    }
}
