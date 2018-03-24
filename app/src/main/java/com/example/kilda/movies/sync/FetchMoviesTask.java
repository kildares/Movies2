package com.example.kilda.movies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.example.kilda.movies.MainActivity;
import com.example.kilda.movies.Movies;
import com.example.kilda.movies.R;
import com.example.kilda.movies.moviesDB.MoviesDbContract;
import com.example.kilda.movies.moviesPreferences.MoviesPreferences;
import com.example.kilda.movies.utilities.MoviesJsonUtils;
import com.example.kilda.movies.utilities.NetworkUtils;
import com.example.kilda.movies.utilities.TmdbApi;

import java.io.IOException;
import java.net.URL;

/**
 * Created by kilda on 2/20/2018.
 */

public class FetchMoviesTask
{

    synchronized public static void syncMovies(Context context)
    {
        if(!NetworkUtils.isNetworkConnected(context)){
            Log.e("Movies",context.getString(R.string.log_connectivity_error));
            return;
        }

        int queryType = MoviesPreferences.isFavoriteMovies(context) ?   TmdbApi.FAVORITES :
                MoviesPreferences.isPopularMovies(context)  ?   TmdbApi.POPULAR   :
                        TmdbApi.TOP_RATED;

        //In the case of favorite movies, there is no need to go online
        switch(queryType){
            case TmdbApi.FAVORITES:{
                queryFavorites(context);
                break;
            }
            default :{
                queryAPI(context, queryType);
            }
        }
    }

    private static void queryFavorites(Context context) {

        ContentResolver moviesContentResolver = context.getContentResolver();

        int deletedRows = moviesContentResolver.delete(MoviesDbContract.MoviesEntry.CONTENT_URI,
                "favorite != 1",
                null);

        Log.d("queryFavorites","returned " + Integer.toString(deletedRows));
    }

    private static void queryAPI(Context context, int queryType)
    {
        try{
            URL moviesRequestUrl = TmdbApi.buildMovieQueryURL(queryType);
            String movieData = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
            ContentValues[] movies = MoviesJsonUtils.parseJSonToMovies(movieData);

            if(movies != null && movies.length > 0){
                ContentResolver moviesContentResolver = context.getContentResolver();

                int deletedRows = moviesContentResolver.delete(MoviesDbContract.MoviesEntry.CONTENT_URI,
                        "favorite != 1",
                        null);

                Log.d("Movies","Number of deleted rows: " + Integer.toString(deletedRows));

                MainActivity.testHelper(context.getContentResolver());

                moviesContentResolver.bulkInsert(MoviesDbContract.MoviesEntry.CONTENT_URI, movies);
            }

        }catch (IOException e){
            e.printStackTrace();
            Log.e("Movie",context.getString(R.string.log_movie_api_error));
        }

    }

}