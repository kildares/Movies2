package com.example.kilda.movies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by kilda on 3/16/2018.
 */

public class MoviesSyncIntentService  extends IntentService{

    public MoviesSyncIntentService() {
        super("MoviesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FetchMoviesTask.sy(this);
    }
}
