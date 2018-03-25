package com.example.kilda.movies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.kilda.movies.R;
import com.example.kilda.movies.moviesDB.MoviesDbContract;
import com.example.kilda.movies.moviesPreferences.MoviesPreferences;
import com.example.kilda.movies.utilities.MoviesJsonUtils;
import com.example.kilda.movies.utilities.NetworkUtils;
import com.example.kilda.movies.utilities.TmdbApi;

import java.io.IOException;
import java.net.URL;

/**
 *
 */
public class FetchMoviesTask
{

    synchronized public static void fetchTrailer(Context context,String movieId){

        if(!NetworkUtils.isNetworkConnected(context)){
            Log.e("Movies",context.getString(R.string.log_connectivity_error));
            return;
        }

        URL trailerRequest = TmdbApi.buildTrailerRequestURL(movieId);
        String movieData = null;
        try {
            movieData = NetworkUtils.getResponseFromHttpUrl(trailerRequest);
            ContentValues contentValues = MoviesJsonUtils.parseJSonToMovieTrailer(movieData);
            updateMovieData(context, TmdbApi.GET_TRAILER_INT, contentValues, movieId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public static void fetchReview(Context context,String movieId){

        if(!NetworkUtils.isNetworkConnected(context)){
            Log.e("Movies",context.getString(R.string.log_connectivity_error));
            return;
        }

        URL trailerRequest = TmdbApi.buildReviewRequestURL(movieId);
        String movieData = null;
        try {
            movieData = NetworkUtils.getResponseFromHttpUrl(trailerRequest);
            ContentValues contentValues = MoviesJsonUtils.parseJSonToMovieReview(movieData);
            updateMovieData(context, TmdbApi.GET_REVIEW_INT, contentValues, movieId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function effectively updates the data received
     * @param context
     * @param movieData
     */
    public static void updateMovieData(Context context, int updateType, ContentValues movieData, String movieId)
    {
        switch(updateType) {
            case TmdbApi.GET_TRAILER_INT: {
                int result = context.getContentResolver().update(
                        MoviesDbContract.MoviesEntry.buildMovieTrailerUri(movieId),
                        movieData,
                        MoviesDbContract.MoviesEntry.getSqlForIdUpdate(),
                        new String[]{movieId});
                Log.d("Movies", "update movie traailers: " + Integer.toString(result));
                break;
            }
            case TmdbApi.GET_REVIEW_INT:
            {
                int result = context.getContentResolver().update(
                        MoviesDbContract.MoviesEntry.buildMovieTrailerUri(movieId),
                        movieData,
                        MoviesDbContract.MoviesEntry.getSqlForIdUpdate(),
                        new String[]{movieId});
                Log.d("Movies", "update movie reviews: " + Integer.toString(result));
                break;
            }

            default:
            {
                throw new UnsupportedOperationException("INVALID UPDATE OPTION");
            }
        }
    }

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

                //Log.d("Movies","Number of deleted rows: " + Integer.toString(deletedRows));

                //MainActivity.testHelper(context.getContentResolver());

                moviesContentResolver.bulkInsert(MoviesDbContract.MoviesEntry.CONTENT_URI, movies);
            }

        }catch (IOException e){
            e.printStackTrace();
            Log.e("Movie",context.getString(R.string.log_movie_api_error));
        }

    }
}