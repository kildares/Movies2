package com.example.kilda.movies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kilda.movies.moviesDB.MoviesDbContract;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MovieListAdapter.MovieListAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private TextView errorMsg;
    private ProgressBar mLoadingBar;
    private MovieListAdapter movieListAdapter;

    private static final int ID_MOVIES_LOADER = 71;

    private static final String[] MOVIES_PROJECTION = {
            MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesDbContract.MoviesEntry.COLUMN_TITLE,
            MoviesDbContract.MoviesEntry.COLUMN_FAVORITE
    };

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

        getSupportLoaderManager().initLoader(ID_MOVIES_LOADER,null, (android.support.v4.app.LoaderManager.LoaderCallbacks<Object>) this);

    }

    public ProgressBar getLoadingBar() {
        return mLoadingBar;
    }

    public void showLoadingBar(){
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

    public void setMovieData(Movies[] movies)
    {
        movieListAdapter.setMovieData(movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem menuTopRated = menu.findItem(R.id.item_top_rated);
        menuTopRated.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                TmdbApi.SetTopRated(true);
                return true;
            }
        });

        MenuItem menuPopularity = menu.findItem(R.id.item_popularity);
        menuPopularity.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                TmdbApi.SetTopRated(false);
                return true;
            }
        });

        final MenuItem menuFavorite = menu.findItem(R.id.item_favorites);
        menuFavorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                queryFavorites();
                return true;
            }
        });
        return true;
    }

    /*
    * Uses the content resolver to query for the favorite movies, then displays in the screen
    */
    public void queryFavorites()
    {
        Uri favoriteUri = MoviesDbContract.MoviesEntry.buildMovieFavoriteUri();
        Cursor cursor = getContentResolver().query(favoriteUri,MOVIES_PROJECTION, " ? = true", new String[]{MoviesDbContract.MoviesEntry.COLUMN_FAVORITE}, null);
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

                return new CursorLoader(this,movieQueryUri, MOVIES_PROJECTION, selection,null, sortOrder);
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
