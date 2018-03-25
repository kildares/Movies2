package com.example.kilda.movies.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;


import com.example.kilda.movies.utilities.TmdbApi;
import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by kilda on 3/13/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MoviesJobService extends com.firebase.jobdispatcher.JobService implements OnMoviesJobFinishedListener
{
    public static final String KEY_JOB = "key_job";
    public static final String KEY_MOVIE_ID = "key_movie_id";
    private AsyncTask<String, Void, Void> mAsyncTask;

    public MoviesJobService(Context context){
        this.mAsyncTask = new MoviesAsyncTask(context);
    }


    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Bundle extras = jobParameters.getExtras();
        String job = extras.getString(KEY_JOB);

        MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask(getApplicationContext());
        moviesAsyncTask.setJobParameters(jobParameters);
        moviesAsyncTask.setOnMoviesJobFinishedListener(this);
        mAsyncTask = moviesAsyncTask;

        try{
            switch(job){
                case TmdbApi.GET_REVIEW_STR:{
                    String movieId = extras.getString(KEY_MOVIE_ID);
                    mAsyncTask.execute(new String[]{job,movieId});
                    break;
                }
                default:{
                    throw new UnsupportedOperationException("Unsupported fetch type");
                }
            }
        }catch(NullPointerException e){
            throw new UnsupportedOperationException("Unsupported key type");
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mAsyncTask != null)
            mAsyncTask.cancel(true);
        return true;
    }

    @Override
    public void onMoviesJobFinished(JobParameters jobParameters)
    {
        jobFinished(jobParameters,false);
    }
}
