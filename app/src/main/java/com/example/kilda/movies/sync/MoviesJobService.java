package com.example.kilda.movies.sync;

    import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.kilda.movies.utilities.TmdbApi;
import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by kilda on 3/13/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MoviesJobService extends com.firebase.jobdispatcher.JobService implements OnMoviesJobFinishedListener
{

    private AsyncTask<String, Void, String> mAsyncTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask(getApplicationContext());
        moviesAsyncTask.setJobParameters(jobParameters);
        moviesAsyncTask.setOnMoviesJobFinishedListener(this);
        mAsyncTask = moviesAsyncTask;

        mAsyncTask.execute(TmdbApi.GET_MOVIES_STR);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mAsyncTask != null)
            mAsyncTask.cancel(true);
        return true;
    }

    public void getTrailers(String movieId)
    {
        mAsyncTask = new MoviesAsyncTask(this);
        mAsyncTask.execute(new String[]{TmdbApi.GET_TRAILER_STR, movieId});
    }

    public void getReviews(String movieId)
    {
        mAsyncTask = new MoviesAsyncTask(this);
        mAsyncTask.execute(new String[]{TmdbApi.GET_REVIEW_STR, movieId});
    }


    @Override
    public void onMoviesJobFinished(JobParameters jobParameters)
    {
        jobFinished(jobParameters,false);
    }
}
