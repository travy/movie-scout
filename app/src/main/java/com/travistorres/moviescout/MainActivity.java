/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.travistorres.moviescout.utils.networking.MovieDbUrlBuilder;

import java.net.URL;

/**
 * TODO:  document the class
 */

public class MainActivity extends AppCompatActivity {
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
        URL ratingMovieUrl = MovieDbUrlBuilder.getRatingsMoviesUrl();
        TextView ratingView = (TextView) findViewById(R.id.popular_movie_url);
        ratingView.setText(ratingMovieUrl.toString());
        URL popularMovieUrl = MovieDbUrlBuilder.getPopularMoviesUrl();
        TextView popularView = (TextView) findViewById(R.id.rating_movie_url);
        popularView.setText(popularMovieUrl.toString());
    }
}
