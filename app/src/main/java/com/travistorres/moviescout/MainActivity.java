/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.travistorres.moviescout.utils.moviedb.MovieDbParser;
import com.travistorres.moviescout.utils.moviedb.MovieDbUrlManager;
import com.travistorres.moviescout.utils.moviedb.adapters.MovieClickedListener;
import com.travistorres.moviescout.utils.moviedb.adapters.MovieListAdapter;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.networking.exceptions.HttpConnectionTimeoutException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpPageNotFoundException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpUnauthorizedException;
import com.travistorres.moviescout.utils.networking.exceptions.NetworkingException;
import com.travistorres.moviescout.utils.networking.NetworkManager;

import java.io.IOException;
import java.net.URL;

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
        implements MovieClickedListener {
    /*
     *  Specifies the key used for accessing the selected movie in a requested Activity.
     *
     */
    public final static String SELECTED_MOVIE_EXTRA = "selectedMovie";

    /*
     *  Defines the number of columns in the grid layout.
     *
     */
    private final static int GRIDLAYOUT_COLUMN_COUNT = 2;

    /*
     *  Error message to display when no network could be reached.
     *
     */
    public final static String NO_NETWORK_ERROR_MESSAGE = "Unable to access Network Resource";

    /*
     * Specifies what the sort order should default to.
     */
    private final static int DEFAULT_SORT_ORDER = MovieDbUrlManager.SORT_BY_POPULARITY;

    private RecyclerView mMovieListView;
    private GridLayoutManager mMovieLayoutManager;
    private MovieListAdapter mMovieAdapter;

    private TextView mPageNotFoundTextView;
    private TextView mNetworkingErrorTextView;
    private TextView mUnauthorizedTextView;

    private ProgressBar mLoadingIndicator;

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

        //  obtain all error mesage objects
        mPageNotFoundTextView = (TextView) findViewById(R.id.page_not_found_error);
        mNetworkingErrorTextView = (TextView) findViewById(R.id.network_connection_failed_error);
        mUnauthorizedTextView = (TextView) findViewById(R.id.api_key_unauthorized_error);

        //  configures adapter objects
        mMovieAdapter = new MovieListAdapter(this);
        mMovieLayoutManager = new GridLayoutManager(this, GRIDLAYOUT_COLUMN_COUNT);

        //  configure the recycler view
        mMovieListView = (RecyclerView) findViewById(R.id.movie_list_rv);
        mMovieListView.setAdapter(mMovieAdapter);
        mMovieListView.setLayoutManager(mMovieLayoutManager);

        //  acquire the loading indicator
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator_pb);

        requestMovies(DEFAULT_SORT_ORDER);
    }

    /**
     * Will startup a sub-process for acquiring a list of movies and displaying them on the app.
     *
     */
    private void requestMovies(int sortOrder) {
        MovieDbUrlManager urlManager = new MovieDbUrlManager(this);
        URL popularMoviesUrl = urlManager.getSortedMoveListUrl(sortOrder);

        new NetworkingTask().execute(popularMoviesUrl);
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
        switch (item.getItemId()) {
            case R.id.popularity_sort_button:
                requestMovies(MovieDbUrlManager.SORT_BY_POPULARITY);
                break;
            case R.id.rating_sort_button:
                requestMovies(MovieDbUrlManager.SORT_BY_RATING);
                break;
        }

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
     * Display the page not found error message.
     *
     */
    public void showPageNotFoundError() {
        mPageNotFoundTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * Displays the error message for when the api key was invalid.
     *
     */
    public void showUnauthorizedError() {
        mUnauthorizedTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * Displays the networking error message.
     *
     */
    public void showNetworkingError() {
        mNetworkingErrorTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * NetworkingTask
     *
     * A utility class which will run a sub-process that queries a list of movies over the network.
     * The process runs separately from the MainActivity to keep the app responsive during times of
     * low/non-existent network connectivity.
     *
     */

    private class NetworkingTask extends AsyncTask <URL, Void, Movie[]> {
        /**
         * Allow users to see the loading indicator wheel.
         *
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        /**
         * Request a list of movies from the network resource.
         *
         * @param urls List of urls for acquiring the movies.  We only need one.
         *
         * @return List of movies that were queried.
         */
        @Override
        protected Movie[] doInBackground(URL... urls) {
            if (urls.length <= 0) {
                return null;
            }

            URL url = urls[0];

            Movie[] movieList = null;
            try {
                String json = NetworkManager.request(url);
                if (json != null) {
                    movieList = MovieDbParser.retrieveMovieList(json, getApplicationContext());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (HttpConnectionTimeoutException e) {
                e.printStackTrace();
            } catch (HttpPageNotFoundException e) {
                e.printStackTrace();
                showPageNotFoundError();
            } catch (HttpUnauthorizedException e) {
                e.printStackTrace();
                showUnauthorizedError();
            } catch (NetworkingException e) {
                e.printStackTrace();
                showNetworkingError();
            }

            return movieList;
        }

        /**
         * Stores the list of movies in the adapter so they can be efficiently rendered onto the
         * app.
         *
         * @param list acquired movies.
         */
        @Override
        protected void onPostExecute(Movie[] list) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (list != null) {
                mMovieAdapter.setMoviesList(list);
            } else {
                Toast.makeText(MainActivity.this, NO_NETWORK_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
