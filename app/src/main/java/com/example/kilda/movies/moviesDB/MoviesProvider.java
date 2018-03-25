package com.example.kilda.movies.moviesDB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kilda on 3/13/2018.
 */

public class MoviesProvider extends ContentProvider{

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_FAVORITE = 200;
    public static final int CODE_MOVIE_FAVORITE_FILTER = 201;
    public static final int CODE_MOVIE_REVIEW = 202;
    public static final int CODE_MOVIE_TRAILER = 203;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesDbContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesDbContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MoviesDbContract.PATH_MOVIE + "/" + MoviesDbContract.PATH_MOVIE_FAVORITE, CODE_MOVIE_FAVORITE);
        //URI to update favorites
        matcher.addURI( authority,   MoviesDbContract.PATH_MOVIE +
                                        "/" +
                                        MoviesDbContract.PATH_MOVIE_FAVORITE +
                                        "/" +
                                        MoviesDbContract.PATH_MOVIE_ID,
                        MoviesProvider.CODE_MOVIE_FAVORITE_FILTER);
        //URI to update movie reviews
        matcher.addURI(authority,   MoviesDbContract.PATH_MOVIE +
                                        "/" +
                                        MoviesDbContract.PATH_MOVIE_REVIEW +
                                        "/" +
                                        MoviesDbContract.PATH_MOVIE_ID,
                        MoviesProvider.CODE_MOVIE_REVIEW);
        //URI to update movie trailer
        matcher.addURI(authority,   MoviesDbContract.PATH_MOVIE +
                        "/" +
                        MoviesDbContract.PATH_MOVIE_TRAILER+
                        "/" +
                        MoviesDbContract.PATH_MOVIE_ID,
                MoviesProvider.CODE_MOVIE_TRAILER);

        matcher.addURI(authority,MoviesDbContract.PATH_MOVIE+ "/" + MoviesDbContract.PATH_MOVIE_TRAILER + "/#", CODE_MOVIE_TRAILER);
        matcher.addURI(authority,MoviesDbContract.PATH_MOVIE+ "/" + MoviesDbContract.PATH_MOVIE_REVIEW + "/#", CODE_MOVIE_REVIEW);

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
        int matchResult = sUriMatcher.match(uri);
        switch(matchResult){
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
                String[] selectionArguments = new String[]{"1"};

                cursor = mOpenHelper.getReadableDatabase().query(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        projection,
                        MoviesDbContract.MoviesEntry.COLUMN_FAVORITE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                        );
                break;
            }

            case CODE_MOVIE_TRAILER:{

                cursor = mOpenHelper.getReadableDatabase().query(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        projection,
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            }
            case CODE_MOVIE_REVIEW:{

                cursor = mOpenHelper.getReadableDatabase().query(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        projection,
                        MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            }

            default: throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }

        try{
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

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

    /**
     * content provider function to delete database movie data. Actually supporting removal of non favorite movies
     * @param uri parameter to decide which type of deletion will use.
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int numRowsDeleted;

        if(selection == null )
            selection = "1";

        int match = sUriMatcher.match(uri);

        switch(match)
        {
            case CODE_MOVIE:{
                SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
                numRowsDeleted = sqLiteDatabase.delete(
                        MoviesDbContract.MoviesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                try{
                    if(numRowsDeleted > 0)
                        getContext().getContentResolver().notifyChange(uri, null);
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
                return numRowsDeleted;
            }

            default: throw new UnsupportedOperationException("Invalid Uri");
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int result;
        switch(match)
        {
            case CODE_MOVIE_FAVORITE_FILTER:{

                result = sqLiteDatabase.update(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        contentValues,
                        s,
                        strings);

                Log.d("MOVIES","UPDATED MOVIE FAVORITE");
                break;
            }

            case CODE_MOVIE_REVIEW:
            {

                result = sqLiteDatabase.update(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        contentValues,
                        s,
                        strings);

                Log.d("MOVIES","UPDATED MOVIE FAVORITE");
                break;
            }

            case CODE_MOVIE_TRAILER:
            {
                result = sqLiteDatabase.update(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        contentValues,
                        s,
                        strings);

                Log.d("MOVIES","UPDATED MOVIE FAVORITE");
                break;
            }

            default: throw new UnsupportedOperationException("Not valid Uri");
        }

        try{
            if(result > 0)
                getContext().getContentResolver().notifyChange(uri, null);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        switch(match){
            case CODE_MOVIE: {
                sqLiteDatabase.beginTransaction();
                int rows = 0;

                try{
                    for (ContentValues value : values)
                    {
                        String title = value.getAsString(MoviesDbContract.MoviesEntry.COLUMN_TITLE);
                        if(title.isEmpty())
                            throw new UnsupportedOperationException("Movie Title cannot be empty");

                        long _id = sqLiteDatabase.insert(MoviesDbContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rows++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                }
                finally {
                    sqLiteDatabase.endTransaction();
                }

                if(rows > 0)
                    getContext().getContentResolver().notifyChange(uri, null);

                return rows;
            }

            default:{
                return super.bulkInsert(uri,values);
            }
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
