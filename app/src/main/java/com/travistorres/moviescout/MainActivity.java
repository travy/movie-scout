/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.travistorres.moviescout.utils.moviedb.MovieDbParser;
import com.travistorres.moviescout.utils.moviedb.MovieDbUrlManager;
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
 * TODO:  document the class
 *
 * @author Travis Anthony Torres
 * @version February 12, 2017
 */

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMovieListView;
    private LinearLayoutManager mMovieLayoutManager;
    private MovieListAdapter mMovieAdapter;

    private TextView mPageNotFoundTextView;
    private TextView mNetworkingErrorTextView;
    private TextView mUnauthorizedTextView;

    /**
     * TODO:  document method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPageNotFoundTextView = (TextView) findViewById(R.id.page_not_found_error);
        mNetworkingErrorTextView = (TextView) findViewById(R.id.network_connection_failed_error);
        mUnauthorizedTextView = (TextView) findViewById(R.id.api_key_unauthorized_error);

        //  configures adapter objects
        mMovieAdapter = new MovieListAdapter();
        mMovieLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //  configure the recycler view
        mMovieListView = (RecyclerView) findViewById(R.id.movie_list_rv);
        mMovieListView.setAdapter(mMovieAdapter);
        mMovieListView.setLayoutManager(mMovieLayoutManager);

        requestMovies();
    }

    private void requestMovies() {
        MovieDbUrlManager urlManager = new MovieDbUrlManager(this);
        URL popularMoviesUrl = urlManager.getPopularMoviesUrl();

        new NetworkingTask().execute(popularMoviesUrl);
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

    private class NetworkingTask extends AsyncTask <URL, Void, Movie[]> {
        @Override
        protected Movie[] doInBackground(URL... urls) {
            if (urls.length <= 0) {
                return null;
            }

            URL url = urls[0];

            Movie[] movieList = null;
            try {
                String json = NetworkManager.request(url);
                movieList = MovieDbParser.retrieveMovieList(json, getApplicationContext());
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

        @Override
        protected void onPostExecute(Movie[] list) {
            if (list != null) {
                mMovieAdapter.setMoviesList(list);
            }
        }
    }
}
