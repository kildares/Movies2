package com.example.kilda.movies.moviesPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.example.kilda.movies.R;

/**
 * Created by kilda on 3/21/2018.
 */

public class MoviesPreferences {

    public static void updateMovieListType(FragmentActivity activity, String chosenConfiguration) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(activity.getString(R.string.key_movie_config),chosenConfiguration);
        editor.apply();
    }

    public static boolean isPopularMovies(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String option = sp.getString(context.getString(R.string.key_movie_config),context.getString(R.string.pref_type_popular));
        return option == context.getString(R.string.pref_type_popular);
    }

    public static boolean isTopRatedMovies(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String option = sp.getString(context.getString(R.string.key_movie_config),context.getString(R.string.pref_type_popular));
        return option == context.getString(R.string.pref_type_top_rated);
    }

    public static boolean isFavoriteMovies(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String option = sp.getString(context.getString(R.string.key_movie_config),context.getString(R.string.pref_type_popular));
        return option == context.getString(R.string.pref_type_favorites);
    }
}
