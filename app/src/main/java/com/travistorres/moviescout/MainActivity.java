/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
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

import com.travistorres.moviescout.utils.UpdateFavoritesServiceUtils;
import com.travistorres.moviescout.utils.moviedb.MovieDbRequester;
import com.travistorres.moviescout.utils.moviedb.MovieSortType;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieClickedListener;
import com.travistorres.moviescout.utils.moviedb.adapters.MovieListAdapter;
import com.travistorres.moviescout.utils.moviedb.interfaces.MovieDbNetworkingErrorHandler;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.networking.broadcast_receivers.NetworkConnectionBroadcastReceiver;
import com.travistorres.moviescout.utils.networking.interfaces.NetworkConnectivityInterface;

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
        implements MovieClickedListener, MovieDbNetworkingErrorHandler, SharedPreferences.OnSharedPreferenceChangeListener, NetworkConnectivityInterface {
    private boolean areMenuItemsVisible;
    private BroadcastReceiver networkBroadcastReceiver;
    private GridLayoutManager mMovieLayoutManager;
    private IntentFilter networkListeningIntent;
    private Menu mMenuBar;
    private MovieDbRequester mMovieRequester;
    private MovieListAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mMovieListView;
    private String movieDbApiThreeKey;
    private TextView mNetworkingErrorTextView;
    private TextView mPageNotFoundTextView;
    private TextView mUnauthorizedTextView;

    /**
     * Specifies what to do when the os loses a connection to the network.
     *
     */
    @Override
    public void onNoNetworkConnectivity() {
        setMenuVisibility(false);
        sortMovies(MovieSortType.FAVORITES);

        String lostConnectionMessage = getString(R.string.lost_network_connection_message);
        Toast.makeText(this, lostConnectionMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Specifies what to do when a network connection is received from the os.
     *
     */
    @Override
    public void onHasNetworkConnectivity() {
        setMenuVisibility(true);
    }

    /**
     * Will either show or hide the popularity and rating sort options depending on the value of
     * `visibilityState`.
     *
     * @param visibilityState
     */
    private void setMenuVisibility(boolean visibilityState) {
        areMenuItemsVisible = visibilityState;
        if (mMenuBar != null) {
            MenuItem popularity = mMenuBar.findItem(R.id.popularity_sort_button);
            popularity.setVisible(visibilityState);
            MenuItem rating = mMenuBar.findItem(R.id.rating_sort_button);
            rating.setVisible(visibilityState);
        }
    }

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

        areMenuItemsVisible = true;

        //  obtain all error message objects
        mPageNotFoundTextView = (TextView) findViewById(R.id.page_not_found_error);
        mNetworkingErrorTextView = (TextView) findViewById(R.id.network_connection_failed_error);
        mUnauthorizedTextView = (TextView) findViewById(R.id.api_key_unauthorized_error);

        //  acquire the loading indicator
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator_pb);

        //  create an intent filter for watching network activity
        networkListeningIntent = new IntentFilter();
        networkListeningIntent.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkBroadcastReceiver = new NetworkConnectionBroadcastReceiver(this);

        //  sets the api keys
        movieDbApiThreeKey = getString(R.string.movie_scout_version_three_api_key);

        //  determine if the screen needs to be constructed or if a previous state exists
        mMovieRequester = new MovieDbRequester(this, this, this);
        mMovieRequester.setVersionThreeApiKey(movieDbApiThreeKey);
        String mainActivityStateExtra = getString(R.string.main_activity_state_bundle);
        if (savedInstanceState != null && savedInstanceState.containsKey(mainActivityStateExtra)) {
            //  load the previously loaded movies and display the results
            MainActivityParcelable parcelable = savedInstanceState.getParcelable(mainActivityStateExtra);
            int currentPage = parcelable.currentPage;
            int totalPages = parcelable.totalPages;
            MovieSortType sortType = parcelable.sortType;
            Movie[] movieList = parcelable.movieList;

            mMovieRequester.setTotalPages(totalPages);
            mMovieRequester.setCurrentPage(currentPage);
            mMovieRequester.setSortType(sortType);
            setupMovieView(movieList);
            mMovieRequester.requestNext();
        } else {
            //  create a movie request object and display the interface
            setupMovieView();
            mMovieRequester.reset();
            mMovieRequester.requestNext();
        }

        //  schedule the favorites update job
        updateNotificationStatusSettings();
    }

    /**
     * Listens for network connectivity events that are broadcast from the os.
     *
     */
    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(networkBroadcastReceiver, networkListeningIntent);
    }

    /**
     * Stop listening for all broadcast events.
     *
     */
    @Override
    public void onPause() {
        super.onPause();

        unregisterReceiver(networkBroadcastReceiver);
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
        parcelable.totalPages = mMovieRequester.getTotalPages();

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
     * Allows the menu to be displayed on the activity bar.
     *
     * @param menu
     *
     * @return true since the menu should always be shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        //  determines if the menu items should be displayed or not
        mMenuBar = menu;
        setMenuVisibility(areMenuItemsVisible);

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
        mMovieRequester = new MovieDbRequester(this, this, this);
        mMovieRequester.setVersionThreeApiKey(movieDbApiThreeKey);
        mMovieRequester.setSortType(sortType);
        setupMovieView();
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
        if (s == getString(R.string.favorite_movies_notification_state_key)) {
            updateNotificationStatusSettings();
        }

        if (s == getString(R.string.favorite_movies_update_interval_key)) {
            updateNotificationsIntervalTime();
        }
    }

    /**
     * Specifies whether or not the service to update favorites should be scheduled.
     *
     */
    public void updateNotificationStatusSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //  determines if the notifications setting is on
        Resources resources = getResources();
        String showNotificationsKey = getString(R.string.favorite_movies_notification_state_key);
        boolean showNotificationsDefaultValue = resources.getBoolean(R.bool.favorite_movies_update_notification_setting_default_value);
        boolean notificationsTurnedOn = sharedPreferences.getBoolean(showNotificationsKey, showNotificationsDefaultValue);

        //  acquires the interval for when the service should run
        String notificationsIntervalKey = getString(R.string.favorite_movies_update_interval_key);
        String notificationsIntervalDefaultValue = getString(R.string.favorite_movies_update_interval_default_value);
        int notificationsIntervalValue = Integer.parseInt(sharedPreferences.getString(notificationsIntervalKey, notificationsIntervalDefaultValue));

        //  schedules and unscheduled the service as necessary
        if (notificationsTurnedOn) {
            UpdateFavoritesServiceUtils.scheduleUpdateFavorites(this, notificationsIntervalValue);
        } else {
            UpdateFavoritesServiceUtils.unscheduledUpdateFavorites(this);
        }
    }

    /**
     * Reschedules the service to run at a specified interval.
     *
     */
    public void updateNotificationsIntervalTime() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //  acquires the interval for when the service should run
        String notificationsIntervalKey = getString(R.string.favorite_movies_update_interval_key);
        String notificationsIntervalDefaultValue = getString(R.string.favorite_movies_update_interval_default_value);
        int notificationsIntervalValue = Integer.parseInt(sharedPreferences.getString(notificationsIntervalKey, notificationsIntervalDefaultValue));

        //  schedules and unscheduled the service as necessary
        UpdateFavoritesServiceUtils.rescheduleUpdateFavorites(this, notificationsIntervalValue);
    }
}
