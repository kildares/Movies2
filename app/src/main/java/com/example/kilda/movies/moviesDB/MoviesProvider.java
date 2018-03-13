package com.example.kilda.movies.moviesDB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by kilda on 3/13/2018.
 */

public class MoviesProvider extends ContentProvider{

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_FAVORITE = 200;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesDbContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesDbContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MoviesDbContract.PATH_MOVIE +"/favorite", CODE_MOVIE_FAVORITE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch(sUriMatcher.match(uri)){
            case CODE_MOVIE:{

                cursor = mOpenHelper.getReadableDatabase().query(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                        );

                break;
            }

            case CODE_MOVIE_FAVORITE:
            {
                String favoriteString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{"1"};

                cursor = mOpenHelper.getReadableDatabase().query(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        projection,
                        MoviesDbContract.MoviesEntry.COLUMN_FAVORITE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                        );

            }

            default: throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
