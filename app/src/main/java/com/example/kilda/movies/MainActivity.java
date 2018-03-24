package com.example.kilda.movies;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.Build;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kilda.movies.moviesDB.MoviesDbContract;
import com.example.kilda.movies.moviesPreferences.MoviesPreferences;
import com.example.kilda.movies.sync.MoviesSyncUtils;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MovieListAdapter.MovieListAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private TextView errorMsg;
    private ProgressBar mLoadingBar;
    private MovieListAdapter movieListAdapter;

    private static final int ID_MOVIES_LOADER = 71;


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


    @TargetApi(Build.VERSION_CODES.O)
    public static void testHelper(ContentResolver contentResolver)
    {
        Uri uri = MoviesDbContract.MoviesEntry.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,Movies.MOVIES_PROJECTION, null, null);

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                Log.d("teste",cursor.getString(Movies.INDEX_MOVIE_TITLE));
                Log.d("teste",cursor.getString(Movies.INDEX_MOVIE_FAVORITE));
                cursor.moveToNext();
            }
        }
        else{
            Log.e("teste","cursor is invalid");
        }


    }


    public void showLoadingBar(){
        errorMsg.setVisibility(errorMsg.INVISIBLE);
        mLoadingBar.setVisibility(mLoadingBar.VISIBLE);
    }

    public void showMovieDataView()
    {
        errorMsg.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(item.getIntent());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch(loaderId){
            case ID_MOVIES_LOADER:{
                Uri movieQueryUri = MoviesDbContract.MoviesEntry.CONTENT_URI;

                String selection = MoviesPreferences.isFavoriteMovies(this) ? " favorite = 1 " :
                                                                                            null;

                return new CursorLoader(this,
                        movieQueryUri,
                        Movies.MOVIES_PROJECTION,
                        selection,
                        null,
                        null);
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
