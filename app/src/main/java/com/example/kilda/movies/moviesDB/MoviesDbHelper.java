package com.example.kilda.movies.moviesDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kilda on 3/13/2018.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 2;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + MoviesDbContract.MoviesEntry.TABLE_NAME + " (" +
                        MoviesDbContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesDbContract.MoviesEntry.COLUMN_FAVORITE + " INTEGER NOT NULL, "   +
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MoviesDbContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL, " +
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_AVERAGE + " TEXT NOT NULL, " +
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_IMAGE + " TEXT NOT NULL, " +
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_REVIEW + " TEXT, " +
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_TRAILER + " TEXT, " +
                        "UNIQUE (" + MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT IGNORE);"
        ;

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesDbContract.MoviesEntry.TABLE_NAME );
    }
}
