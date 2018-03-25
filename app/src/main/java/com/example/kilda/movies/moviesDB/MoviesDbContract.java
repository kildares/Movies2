package com.example.kilda.movies.moviesDB;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by kilda on 3/12/2018.
 */

public class MoviesDbContract {

    public static final String CONTENT_AUTHORITY = "com.example.kilda.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_MOVIE_FAVORITE = "favorite";
    public static final String PATH_MOVIE_TRAILER = "trailer";
    public static final String PATH_MOVIE_REVIEW = "review";
    public static final String PATH_MOVIE_ID = "#";

    public static final class MoviesEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_FAVORITE = "favorite";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_SYNOPSIS = "synopsys";

        public static final String COLUMN_MOVIE_AVERAGE = "average";

        public static final String COLUMN_MOVIE_RELEASE_DATE = "date";

        public static final String COLUMN_MOVIE_IMAGE = "image";

        public static final String COLUMN_MOVIE_REVIEW = "review";

        public static final String COLUMN_MOVIE_TRAILER = "trailer";


        public static Uri buildQueryTrailerUri(String id)
        {
            Uri uri = BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_MOVIE).appendPath(PATH_MOVIE_TRAILER).appendPath(id)
                    .build();
            return uri;
        }
        public static Uri buildQueryReviewUri(String id)
        {
            Uri uri = BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_MOVIE).appendPath(PATH_MOVIE_REVIEW).appendPath(id)
                    .build();
            return uri;
        }


        public static Uri buildMovieFavoriteIdUri(String id)
        {
            Uri uri = CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_FAVORITE).appendPath(id).build();
            Log.d("Favorite Id URI",uri.toString());
            return uri;
        }

        public static Uri buildMovieTrailerUri(String id)
        {
            Uri uri = CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_TRAILER).appendPath(id).build();
            Log.d("Review Id URI",uri.toString());
            return uri;
        }

        public static Uri buildMovieReviewUri(String id)
        {
            Uri uri = CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_REVIEW).appendPath(id).build();
            Log.d("Review Id URI",uri.toString());
            return uri;
        }

        public static String getSqlForIdUpdate()
        {
            return COLUMN_MOVIE_ID + "= ?";
        }

    }
}
