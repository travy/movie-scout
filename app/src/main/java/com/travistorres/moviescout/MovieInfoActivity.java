/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * TODO:  document
 *
 * @author Travis Anthony Torres
 * @version February 19, 2017
 */

public class MovieInfoActivity extends AppCompatActivity {
    /**
     * TODO:  document
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.SELECTED_MOVIE_EXTRA)) {
            Toast.makeText(this, "Movie Selected", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("Movie Info", "Activity triggered without a selected movie being specified");
            //  TODO:  display an error message
        }
    }
}
