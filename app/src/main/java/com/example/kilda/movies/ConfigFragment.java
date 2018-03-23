package com.example.kilda.movies;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.kilda.movies.moviesDB.MoviesDbContract;
import com.example.kilda.movies.moviesPreferences.MoviesPreferences;
import com.example.kilda.movies.sync.MoviesSyncUtils;

/**
 * Created by kilda on 3/20/2018.
 */

public class ConfigFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();

        // For list preferences, look up the correct display value in
        // the preference's 'entries' list (since they have separate labels/values).
        ListPreference listPreference = (ListPreference) preference;
        int prefIndex = listPreference.findIndexOfValue(stringValue);
        if (prefIndex >= 0)
        {
            preference.setSummary(listPreference.getEntries()[prefIndex]);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // register the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Activity activity = getActivity();
        if (key.equals(getString(R.string.pref_type_key))) {

            String chosenOption = sharedPreferences.getString(key,getString(R.string.val_movie_config_top));

            MoviesPreferences.updateMovieListType(getActivity(),chosenOption);
            //If the user selects the favorite movies only, there is no need to sync again. will display
            if(!chosenOption.equals(getString(R.string.pref_type_favorites))){
                MoviesSyncUtils.startImmediateSync(getActivity());
            }
            else{

            }

            PreferenceScreen preferenceScreen = getPreferenceScreen();
            int count = preferenceScreen.getPreferenceCount();
            for (int i = 0; i < count; i++)
            {
                Preference p = preferenceScreen.getPreference(i);
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }

        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++)
        {
            Preference p = prefScreen.getPreference(i);
            String value = sharedPreferences.getString(p.getKey(), "");
            setPreferenceSummary(p, value);
        }
    }
}
