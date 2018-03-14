package com.example.kilda.movies.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.example.kilda.movies.MainActivity;
import com.example.kilda.movies.Movies;
import com.example.kilda.movies.utilities.MoviesJsonUtils;
import com.example.kilda.movies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by kilda on 2/20/2018.
 */

public class FetchMoviesTask extends AsyncTask<URL, Void,Movies[]>
{
    Context CurrentContext;

    public FetchMoviesTask(Context context){
        this.CurrentContext = context;
    }

    @Override
    protected Movies[] doInBackground(URL... params) {

        if(params.length==0)
            return null;

        URL reqUrl = params[0];
        Movies[] Movies = null;
        try {
            String jsonResponse;
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(reqUrl);
            Movies = MoviesJsonUtils.parseJSonToMovies(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Movies;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        ((MainActivity)this.CurrentContext).getLoadingBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Movies[] movies) {
        ((MainActivity)this.CurrentContext).getLoadingBar().setVisibility(View.INVISIBLE);
        if (movies != null) {
            ((MainActivity)this.CurrentContext).showMovieDataView();
            ((MainActivity)this.CurrentContext).setMovieData(movies);
        } else {
            ((MainActivity)this.CurrentContext).showErrorMessage();
        }
    }
}