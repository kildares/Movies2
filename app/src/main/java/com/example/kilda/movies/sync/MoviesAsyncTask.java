package com.example.kilda.movies.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.example.kilda.movies.utilities.TmdbApi;
import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by kilda on 3/24/2018.
 */
public class MoviesAsyncTask extends AsyncTask<String, Void, Void>
{

    private OnMoviesJobFinishedListener mListener;

    JobParameters jobParameters;
    Context mContext;

    public MoviesAsyncTask(Context context){
        this.mContext = context;
    }

    public void setJobParameters(JobParameters jobParameters){
        this.jobParameters = jobParameters;
    }

    @Override
    protected Void doInBackground(String... fetchData) {

        if(fetchData == null)
            return null;

        int type = Integer.parseInt(fetchData[0]);

        switch(type){
            case TmdbApi.GET_MOVIES_INT:{
                Context context = this.mContext;
                FetchMoviesTask.syncMovies(context);
                onJobFinishedEvent();
                break;
            }
            case TmdbApi.GET_REVIEW_INT:{
                String trailerId = fetchData[1];
                if(trailerId == null)
                    return null;

                FetchMoviesTask.fetchReview(mContext,trailerId);
                return null;
            }
            case TmdbApi.GET_TRAILER_INT:{
                String reviewID = fetchData[1];
                if(reviewID == null)
                    return null;
                FetchMoviesTask.fetchTrailer(mContext, reviewID);
                break;
            }
            default:{
                Context context = this.mContext;
                FetchMoviesTask.syncMovies(context);
                onJobFinishedEvent();
            }
        }
        return null;
    }


    public void setOnMoviesJobFinishedListener(OnMoviesJobFinishedListener listener){
        mListener = listener;
    }

    public void onJobFinishedEvent()
    {
        mListener.onMoviesJobFinished(jobParameters);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        onJobFinishedEvent();
    }

}

