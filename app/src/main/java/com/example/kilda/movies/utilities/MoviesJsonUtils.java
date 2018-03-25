package com.example.kilda.movies.utilities;

import android.content.ContentValues;

import com.example.kilda.movies.moviesDB.MoviesDbContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kilda on 2/13/2018.
 */

public class MoviesJsonUtils {

    private static final String results = "results";
    private static final String RELEASE_DATE = "release_date";
    private static final String NAME = "title";
    private static final String SYNOPSIS = "overview";
    private static final String IMAGE_PATH = "poster_path";
    private static final String ID = "id";
    private static final String AVG = "vote_average";

    private static final String REVIEW_CONTENT = "content";
    private static final String TRAILER_KEY = "key";


    public static ContentValues[] parseJSonToMovies(String jsonStr)
    {

        try {
            JSONObject json = new JSONObject(jsonStr);

            JSONArray jsonArray = json.getJSONArray(MoviesJsonUtils.results);

            ContentValues[] moviesContentValues = new ContentValues[jsonArray.length()];


            for(int i=0 ; i < jsonArray.length() ; i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString(MoviesJsonUtils.NAME);
                String year = jsonObject.getString(MoviesJsonUtils.RELEASE_DATE);
                String synopsis = jsonObject.getString(MoviesJsonUtils.SYNOPSIS);
                String image = jsonObject.getString(MoviesJsonUtils.IMAGE_PATH);
                String id = jsonObject.getString(MoviesJsonUtils.ID);
                String avg = jsonObject.getString(MoviesJsonUtils.AVG);

                ContentValues movieContentValue = new ContentValues();
                movieContentValue.put(MoviesDbContract.MoviesEntry.COLUMN_TITLE,name);
                movieContentValue.put(MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID,id);
                movieContentValue.put(MoviesDbContract.MoviesEntry.COLUMN_FAVORITE, 0);
                movieContentValue.put(MoviesDbContract.MoviesEntry.COLUMN_MOVIE_AVERAGE, avg);
                movieContentValue.put(MoviesDbContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS, synopsis);
                movieContentValue.put(MoviesDbContract.MoviesEntry.COLUMN_MOVIE_IMAGE, image);
                movieContentValue.put(MoviesDbContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, year);

                moviesContentValues[i] = movieContentValue;
            }
            return moviesContentValues;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ContentValues parseJSonToMovieReview(String jsonData) {
        try {
            JSONObject json = new JSONObject(jsonData);

            JSONArray jsonArray = json.getJSONArray(MoviesJsonUtils.results);
            if (jsonArray == null || jsonArray.length() == 0)
                return null;

            ContentValues contentValues = new ContentValues();
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String review = jsonObject.getString(MoviesJsonUtils.REVIEW_CONTENT);

            contentValues.put(MoviesDbContract.MoviesEntry.COLUMN_MOVIE_REVIEW, review);
            return contentValues;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ContentValues parseJSonToMovieTrailer(String jsonData)
    {
        try {
            JSONObject json = new JSONObject(jsonData);

            JSONArray jsonArray = json.getJSONArray(MoviesJsonUtils.results);
            if (jsonArray == null || jsonArray.length() == 0)
                return null;

            ContentValues contentValues = new ContentValues();
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String trailer = jsonObject.getString(MoviesJsonUtils.TRAILER_KEY);
            contentValues.put(MoviesDbContract.MoviesEntry.COLUMN_MOVIE_TRAILER, trailer);
            return contentValues;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
