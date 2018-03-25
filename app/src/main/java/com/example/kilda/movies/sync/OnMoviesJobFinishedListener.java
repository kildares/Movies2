package com.example.kilda.movies.sync;

import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by kilda on 3/24/2018.
 */

/**
 * Interface to end the job
 */
interface OnMoviesJobFinishedListener {

    void onJobFinished(JobParameters jobParameters);

}
