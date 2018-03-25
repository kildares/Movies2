package com.example.kilda.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kilda.movies.entities.Movies;
import com.example.kilda.movies.moviesDB.MoviesDbContract;
import com.example.kilda.movies.sync.MoviesSyncUtils;
import com.example.kilda.movies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;


public class MovieDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageView MovieImg;
    private TextView tvName;
    private TextView tvYear;
    private TextView tvSynopsis;
    private TextView tvAverage;
    private Button btTrailer;
    private Button btReview;

    private Movies detailedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_movie_detail);

        this.MovieImg = findViewById(R.id.iv_detail_poster);
        this.tvName = findViewById(R.id.tv_detail_name);
        this.tvYear = findViewById(R.id.tv_detail_year);
        this.tvSynopsis = findViewById(R.id.tv_detail_synopsis);
        this.tvAverage = findViewById(R.id.tv_vote_avg);
        this.btTrailer = findViewById(R.id.bt_trailer);
        this.btReview = findViewById(R.id.bt_review);


        btTrailer.setOnClickListener(this);
        btReview.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bdl = intent.getExtras();
        try{
            Movies mv = bdl.getParcelable("movie");
            this.tvName.setText(mv.getName());
            this.tvYear.setText(formatTvYear(mv.getYear()));
            this.tvSynopsis.setText(mv.getSynopsis());
            this.tvAverage.setText(mv.getAverage());

            this.detailedMovie = mv;

            Picasso.with(MovieDetail.this).load(TmdbApi.getImageUrl(this.detailedMovie.getImage())).into(this.MovieImg);
        }catch(NullPointerException e){
            e.printStackTrace();
            Intent intent1 = new Intent(MovieDetail.this,MainActivity.class);
            startActivity(intent1);
        }

    }

    private String formatTvYear(String dateStr)
    {
        String year = dateStr.substring(0,4);
        String month = dateStr.substring(5,7);
        String day = dateStr.substring(8,10);
        return month+"/"+day+"/"+year;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);

        final MenuItem menuFavorite = menu.findItem(R.id.action_favorite);

        String title = detailedMovie.isFavorite() ? getString(R.string.unfavorite) : getString(R.string.favorite);
        menuFavorite.setTitle(title);

        menuFavorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                boolean isFavorite = detailedMovie.isFavorite();
                String isFav = isFavorite ? "0" : "1";

                ContentValues contentValues = new ContentValues();
                contentValues.put(MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID, detailedMovie.getMovieId());
                contentValues.put(MoviesDbContract.MoviesEntry.COLUMN_FAVORITE,isFav);

                getContentResolver().update(MoviesDbContract.MoviesEntry.buildMovieFavoriteIdUri(detailedMovie.getMovieId()),
                        contentValues,
                        MoviesDbContract.MoviesEntry.getSqlForIdUpdate(),
                        new String[]{detailedMovie.getMovieId()}
                        );

                MoviesSyncUtils.startImmediateSync(MovieDetail.this);

                detailedMovie.setFavorite(isFav);

                String title = detailedMovie.isFavorite() ? getString(R.string.unfavorite) : getString(R.string.favorite);
                menuFavorite.setTitle(title);

                return true;
            }
        });

        return true;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == btReview.getId()){
            loadReview();
        }
        else if(view.getId() == btTrailer.getId()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                loadTrailer();
            }
        }
    }

    public void loadReview()
    {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadTrailer()
    {
        String movieTrailer = this.detailedMovie.getTrailer();
        String errorMsg = null;
        if(movieTrailer == null || movieTrailer.isEmpty())
        {
            Cursor cursor = getContentResolver().query(
                    MoviesDbContract.MoviesEntry.buildQueryTrailerUri(this.detailedMovie.getMovieId()),
                    new String[]{MoviesDbContract.MoviesEntry.COLUMN_MOVIE_TRAILER},
                    null,
                    new String[]{this.detailedMovie.getMovieId()},
                    null);

            if(cursor == null || cursor.getCount() == 0)
                errorMsg = getString(R.string.error_trailer_unavailable);
            else
            {
                cursor.moveToFirst();

                Log.d("Cursor",DatabaseUtils.dumpCursorToString(cursor));
                movieTrailer = cursor.getString(0);
            }
        }

        if(errorMsg != null)
            Toast.makeText(this,errorMsg,Toast.LENGTH_LONG).show();
        else
        {
            Uri uri = Uri.parse("https://youtube.com/watch?v=" + movieTrailer);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
            else{
                errorMsg = getString(R.string.error_no_app_to_resolve);
                Toast.makeText(this,errorMsg,Toast.LENGTH_LONG).show();
            }
        }
    }
}
