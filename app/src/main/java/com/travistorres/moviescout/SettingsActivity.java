/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * SettingsActivity
 *
 * Responsible for displaying a page where the user will be allowed to modify configurable fields
 * within the app.  Most notably, this is where the user will be allowed to enter in an api key
 * for connecting to the moviedb api resource.
 *
 * @author Travis Anthony torres
 * @version 1.2.0 (March 26, 2017)
 */

public class SettingsActivity extends AppCompatActivity {
    /**
     * Determines what to do when the Activity is first loaded into memory.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //  display the back button on the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Informs the Android runtime to backtrack to the previous Activity that was loaded when the
     * back button is pressed.
     *
     * @param item The selected item.
     *
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();

        if (selectedItem == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
