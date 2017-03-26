/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * SettingsFragment
 *
 * Responsible for displaying all Configurable settings that the application has.  Upon modifying
 * each of the settings, the Fragment will then alert any listeners of changes that have been
 * made.
 *
 * @author Travis Anthony Torres
 * @version v1.2.0 (March 26, 2017)
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    /**
     * Specifies the file that contains information about the settings that users are allowed
     * to modify within the app.  Most notably this includes instructions on the api key.
     *
     * @param savedInstanceState
     * @param rootKey
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.application_settings);
    }
}
