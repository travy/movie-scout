/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

/**
 * SettingsFragment
 *
 * Responsible for displaying all Configurable settings that the application has.  Upon modifying
 * each of the settings, the Fragment will then alert any listeners of changes that have been
 * made.
 *
 * @author Travis Anthony Torres
 * @version v1.2.0 (March 27, 2017)
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {
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

        //  listen for changes on preferences
        validatePreferences();
    }

    /**
     * Listens for changes made on the api key preference values.
     *
     */
    private void validatePreferences() {
        listenForChangesOnPreference(R.string.movie_db_v3_settings_key);
        listenForChangesOnPreference(R.string.movie_db_v4_settings_key);
    }

    /**
     * Sets a listener for changes on a particular preference value.
     *
     * @param preferenceKeyIdentifier
     */
    private void listenForChangesOnPreference(int preferenceKeyIdentifier) {
        String preferenceKey = getString(preferenceKeyIdentifier);
        Preference preference = findPreference(preferenceKey);
        preference.setOnPreferenceChangeListener(this);
    }

    /**
     * Prevents the API key from being an empty string.
     *
     * @param preference
     * @param newValue
     *
     * @return `false` if the api key is invalid and `true` otherwise.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //  creates a toast to display if the api key is not specified
        String missingApiErrorMessage = getString(R.string.missing_api_keys);
        Toast errorMessage = Toast.makeText(getContext(), missingApiErrorMessage, Toast.LENGTH_SHORT);

        //  convert field results
        boolean isValid = true;
        String preferenceKey = preference.getKey();
        String apiThreeKey = getString(R.string.movie_db_v3_settings_key);
        String apiFourKey = getString(R.string.movie_db_v4_settings_key);
        String newApiKeyValue = (String) newValue;

        //  validates the api key values
        if (preferenceKey.equals(apiThreeKey) ||
                preferenceKey.equals(apiFourKey)) {
            //  displays an error message if the object is left blank
            isValid = newApiKeyValue.trim().length() >= 1;
            if (!isValid) {
                errorMessage.show();
            }
        }

        return isValid;
    }
}
