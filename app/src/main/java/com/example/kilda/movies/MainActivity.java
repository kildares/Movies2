package com.example.kilda.movies;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kilda.movies.moviesDB.MoviesDbContract;
import com.example.kilda.movies.sync.MoviesSyncUtils;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MovieListAdapter.MovieListAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private TextView errorMsg;
    private ProgressBar mLoadingBar;
    private MovieListAdapter movieListAdapter;

    private static final int ID_MOVIES_LOADER = 71;

    public static final String[] MOVIES_PROJECTION = {
            MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesDbContract.MoviesEntry.COLUMN_TITLE,
            MoviesDbContract.MoviesEntry.COLUMN_FAVORITE,
            MoviesDbContract.MoviesEntry.COLUMN_MOVIE_AVERAGE,
            MoviesDbContract.MoviesEntry.COLUMN_MOVIE_IMAGE,
            MoviesDbContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
            MoviesDbContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_FAVORITE = 2;
    public static final int INDEX_MOVIE_AVERAGE = 3;
    public static final int INDEX_MOVIE_IMAGE = 4;
    public static final int INDEX_MOVIE_RELEASE_DATE = 5;
    public static final int INDEX_MOVIE_SYNOPSIS = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);
        errorMsg = findViewById(R.id.tv_error_loading);
        mLoadingBar = findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,4);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        movieListAdapter = new MovieListAdapter(this);

        mRecyclerView.setAdapter(movieListAdapter);

        showLoadingBar();

        getSupportLoaderManager().initLoader(ID_MOVIES_LOADER,null, this);

        MoviesSyncUtils.initialize(this);
    }

    public ProgressBar getLoadingBar() {
        return mLoadingBar;
    }

    public void showLoadingBar(){
        errorMsg.setVisibility(errorMsg.INVISIBLE);
        mLoadingBar.setVisibility(mLoadingBar.VISIBLE);
    }

    public void showMovieDataView()
    {
        errorMsg.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movies movie) {
        Intent intent = new Intent(MainActivity.this,MovieDetail.class);
        intent.putExtra("movie",movie);

        startActivity(intent);
    }

    public void showErrorMessage()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorMsg.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem menuTopRated = menu.findItem(R.id.item_config_menu);
        menuTopRated.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent = new Intent(MainActivity.this,ConfigActivity.class);
                startActivity(intent);
                return true;
            }
        });

        return true;
    }

    public void setMoviesPreferences(int researchType)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.key_movie_config),researchType);
        editor.apply();
    }

    /*
    * Uses the content resolver to query for the favorite movies, then displays in the screen
    */
    public void queryFavorites()
    {
        Uri favoriteUri = MoviesDbContract.MoviesEntry.buildMovieFavoriteUri();
        Cursor cursor = getContentResolver().query(favoriteUri, MOVIES_PROJECTION, " ? = 1", new String[]{MoviesDbContract.MoviesEntry.COLUMN_FAVORITE}, null);
        movieListAdapter.updateCursor(cursor);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(item.getIntent());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        Cursor cursor = null;
        switch(loaderId){
            case ID_MOVIES_LOADER:{
                Uri movieQueryUri = MoviesDbContract.MoviesEntry.CONTENT_URI;

                //Ordering by favorites
                String sortOrder =MoviesDbContract.MoviesEntry.COLUMN_FAVORITE + " ASC";

                String selection = MoviesDbContract.MoviesEntry.getSqlSelectForFavorite();

                return new CursorLoader(this,
                        movieQueryUri,
                        MOVIES_PROJECTION,
                        selection,
                        null,
                        sortOrder);
            }

            default:
                throw new UnsupportedOperationException("Invalid Loader Id");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        movieListAdapter.updateCursor(cursor);
        if(cursor.getCount() > 0)
            showMovieDataView();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        movieListAdapter.updateCursor(null);
    }
}
