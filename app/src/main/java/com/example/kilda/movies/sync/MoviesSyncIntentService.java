package com.example.kilda.movies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.kilda.movies.utilities.TmdbApi;

/**
 * Created by kilda on 3/16/2018.
 */

public class MoviesSyncIntentService  extends IntentService{

    public MoviesSyncIntentService() {

        super("MoviesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Bundle bundle = intent.getExtras();
        String type = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            type = bundle.getString(MoviesJobService.KEY_JOB);

            switch(type) {
                case TmdbApi.GET_TRAILER_STR: {
                    MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask(this);

                    String movieId = bundle.getString(MoviesJobService.KEY_MOVIE_ID);
                    moviesAsyncTask.execute(new String[]{TmdbApi.GET_TRAILER_STR,movieId});
                    break;
                }
                case TmdbApi.GET_REVIEW_STR: {
                    MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask(this);
                    String movieId = bundle.getString(MoviesJobService.KEY_MOVIE_ID);
                    moviesAsyncTask.execute(new String[]{TmdbApi.GET_REVIEW_STR,movieId});
                    break;
                }
                default: {
                    FetchMoviesTask.syncMovies(this);
                }
            }
        }
        else
            FetchMoviesTask.syncMovies(this);


    }
}
