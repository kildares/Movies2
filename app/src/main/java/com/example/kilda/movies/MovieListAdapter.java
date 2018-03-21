package com.example.kilda.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kilda.movies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

/**
 * Created by kilda on 2/12/2018.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>{

    private final MovieListAdapterOnClickHandler movieListAdapterOnClickHandler;
    private Cursor mCursor;

    public MovieListAdapter(MovieListAdapterOnClickHandler clickHandler)
    {
        movieListAdapterOnClickHandler = clickHandler;
    }

    public void updateCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public interface MovieListAdapterOnClickHandler
    {
        void onClick(Movies movie);
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String movieImg = mCursor.getString(MainActivity.INDEX_MOVIE_IMAGE);
        String movieImgUrl = TmdbApi.getImageUrl(movieImg);
        Picasso.with(holder.movieImg.getContext()).load(movieImgUrl).into(holder.movieImg);

        holder.movieName.setText( mCursor.getString(MainActivity.INDEX_MOVIE_TITLE));
    }

    @Override
    public int getItemCount()
    {
        if(mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView movieName;
        public final ImageView movieImg;

        public MovieListViewHolder(View itemView) {
            super(itemView);
            this.movieName = itemView.findViewById(R.id.tv_movie_name);
            this.movieImg = itemView.findViewById(R.id.view_movie_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.move(adapterPosition);
            Movies movie = new Movies(
                    mCursor.getString(MainActivity.INDEX_MOVIE_ID),
                    mCursor.getString(MainActivity.INDEX_MOVIE_TITLE),
                    mCursor.getString(MainActivity.INDEX_MOVIE_RELEASE_DATE),
                    mCursor.getString(MainActivity.INDEX_MOVIE_IMAGE),
                    mCursor.getString(MainActivity.INDEX_MOVIE_SYNOPSIS),
                    mCursor.getString(MainActivity.INDEX_MOVIE_AVERAGE))
                    ;
            movieListAdapterOnClickHandler.onClick(movie);
        }
    }

}
