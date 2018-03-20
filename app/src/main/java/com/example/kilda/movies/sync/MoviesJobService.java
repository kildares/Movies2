package com.example.kilda.movies.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by kilda on 3/13/2018.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MoviesJobService extends com.firebase.jobdispatcher.JobService
{
    Context CurrentContext;
    private AsyncTask<Void, Void, Void> mAsyncTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask();
        moviesAsyncTask.setJobParameters(jobParameters);
        mAsyncTask = moviesAsyncTask;
        mAsyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mAsyncTask != null)
            mAsyncTask.cancel(true);
        return true;
    }

    private class MoviesAsyncTask extends AsyncTask<Void, Void, Void>
    {
        JobParameters jobParameters;

        public void setJobParameters(JobParameters jobParameters){
            this.jobParameters = jobParameters;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Context context = getApplicationContext();

            FetchMoviesTask.syncMovies(context);

            jobFinished(jobParameters,false);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            jobFinished(jobParameters, false);
        }
    }
}
