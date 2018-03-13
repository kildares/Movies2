package com.example.kilda.movies.moviesDB;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kilda on 3/12/2018.
 */

public class MoviesDbContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.moviesDB";

    public static final Uri BASE_CONTENT_URI = Uri.parse("moviesDB://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_MOVIE_FAVORITE = "movie/favorite";

    public static final class MoviesEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_FAVORITE = "favorite";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static Uri buildMovieFavoriteUri() {
            return CONTENT_URI.buildUpon()
                    .appendPath("favorite")
                    .build();
        }


        public static String getSqlSelectForFavorite() {
            return MoviesEntry.COLUMN_FAVORITE + "= true";
        }

    }
}
