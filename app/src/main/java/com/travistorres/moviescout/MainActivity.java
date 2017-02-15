/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.travistorres.moviescout.utils.networking.exceptions.HttpConnectionTimeoutException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpPageNotFoundException;
import com.travistorres.moviescout.utils.networking.exceptions.HttpUnauthorizedException;
import com.travistorres.moviescout.utils.networking.exceptions.NetworkingException;
import com.travistorres.moviescout.utils.networking.moviedb.MovieDbUrlManager;
import com.travistorres.moviescout.utils.networking.NetworkManager;

import java.io.IOException;
import java.net.URL;

/**
 * TODO:  document the class
 */

public class MainActivity extends AppCompatActivity {
    private TextView mRatingUrl;

    /**
     * TODO:  document method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  TODO:  remove code below, used only for testing networking api
        mRatingUrl = (TextView) findViewById(R.id.popular_movie_url);

        requestMovies();
    }

    private void requestMovies() {
        Resources resources = getResources();
        MovieDbUrlManager urlManager = new MovieDbUrlManager(resources);
        URL popularMoviesUrl = urlManager.getPopularMoviesUrl();

        new NetworkingTask().execute(popularMoviesUrl);
    }

    private class NetworkingTask extends AsyncTask <URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            if (urls.length <= 0) {
                return null;
            }

            URL url = urls[0];

            String response = null;
            try {
                response = NetworkManager.request(url);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (HttpConnectionTimeoutException e) {
                e.printStackTrace();
            } catch (HttpPageNotFoundException e) {
                e.printStackTrace();
            } catch (HttpUnauthorizedException e) {
                e.printStackTrace();
            } catch (NetworkingException e) {
                e.printStackTrace();
            }
            Log.d("Copy me", url.toString());
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            mRatingUrl.setText(s);
        }
    }
}
