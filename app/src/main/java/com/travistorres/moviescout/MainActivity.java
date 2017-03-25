/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
 * @version February 12, 2017
 */

public class MainActivity extends AppCompatActivity
        implements MovieClickedListener, MovieDbNetworkingErrorHandler {
    //  TODO-  (1) Construct an Activity named SettingsActivity have the layout file simply read "Are you ready to ROCK!!!"

    //  TODO-  (2) Create a menu item named Settings
    //  TODO-  (3) When the settings menu item is clicked, create an intent which will load the SettingsActivity
    //  TODO-  (4) Run the emulator to ensure that the SettingsActivity loads properly and displays the text "Are you ready to Rock!!!"

    //  TODO-  (5) Create a Fragment named SettingsFragment which will only display the text "Hello, World" in it
    //  TODO-  (6) Update the settings_activity.xml file to load the SettingsFragment
    //  TODO-  (7) Run the emulator to ensure that the SettingsFragment is properly loaded and displays the text "Hello, World" when the settings menu item clicked

    //  TODO-  (8) Create an XML file named settings.xml
    //  TODO-  (9) Add preference options for the MovieDB API's v3 and v4 key to be entered
    //  TODO-  (10) Load the XML file into the SettingsFragment
    //  TODO-  (11) Get the settings to show as EditText fields in the SettingsFragment
    //  TODO-  (12) Move all hard-coded data to a strings.xml resource object

    //  TODO-  (13) Configure the app to view the contents of the Settings.xml file and display an error message if the keys do not work
    //  TODO-  (14) Deprecate everything in the package com.travistorres.moviescout.utils.configs
    //  TODO-  (15) Refactor the app so that settings are only acquired by the Settings.xml file
    //  TODO-  (16) Remove the configurations.xml resource file

    //  TODO-  (17) Update the MainActivity so that it will automatically update whenever the Settings are changed

    //  TODO-  (18) Create a menu for the MovieInfoActivity
    //  TODO-  (19) Add a menu item named settings
    //  TODO-  (20) When the user clicks the settings menu item an intent should be fired which loads the SettingsActivity
    //  TODO-  (21) The MovieInfoActivity should automatically update whenever the Settings are changed

    //  TODO-  (22) Add an Up Arrow to the SettingsFragment so that the user can press it to return to the previous page

    //  TODO-  (23) Document all classes and ensure Code is up to date with Udacity standards

    /*
     *  Specifies the key used for accessing the selected movie in a requested Activity.
     *
     */
    public final static String SELECTED_MOVIE_EXTRA = "selectedMovie";

    /*
     *  Defines the number of columns in the grid layout.
     *
     */
    private final static int GRIDLAYOUT_COLUMN_COUNT = 3;

    private RecyclerView mMovieListView;
    private GridLayoutManager mMovieLayoutManager;
    private MovieListAdapter mMovieAdapter;

    private TextView mPageNotFoundTextView;
    private TextView mNetworkingErrorTextView;
    private TextView mUnauthorizedTextView;

    private ProgressBar mLoadingIndicator;

    private MovieDbRequester mMovieRequester;

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
        mMovieRequester = new MovieDbRequester(this, this, this);
        mMovieLayoutManager = new GridLayoutManager(this, GRIDLAYOUT_COLUMN_COUNT);
        mMovieAdapter = mMovieRequester.getAdapter();

        //  configures the recycler view
        mMovieListView = (RecyclerView) findViewById(R.id.movie_list_rv);
        mMovieListView.setAdapter(mMovieAdapter);
        mMovieListView.setLayoutManager(mMovieLayoutManager);

        //  starts up the requester sequence
        mMovieRequester.requestNext();
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
                mMovieRequester.setSortType(MovieSortType.MOST_POPULAR);
                break;
            case R.id.rating_sort_button:
                mMovieRequester.setSortType(MovieSortType.HIGHEST_RATED);
                break;
        }

        //  resets the results
        mMovieRequester.reset();
        mMovieRequester.requestNext();

        return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(context, movieInfoPage);
        intent.putExtra(SELECTED_MOVIE_EXTRA, clickedMovie);

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
}
