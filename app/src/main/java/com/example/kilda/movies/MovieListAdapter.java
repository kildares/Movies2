package com.example.kilda.movies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kilda.movies.entities.Movies;
import com.example.kilda.movies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

/**
 * Created by kilda on 2/12/2018.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>{

    private final MovieListAdapterOnClickHandler movieListAdapterOnClickHandler;
    private Cursor mCursor;

    private static final int TYPE_FAVORITE = 1;
    private static final int TYPE_NORMAL = 0;

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
    public int getItemViewType(int position) {

        if(mCursor == null || mCursor.getCount() <=0 || !mCursor.moveToFirst())
            return 0;
        if(!mCursor.move(position))
            return 0;

        String movieName = mCursor.getString(Movies.INDEX_MOVIE_TITLE);
        String favorite = mCursor.getString(Movies.INDEX_MOVIE_FAVORITE);
        int favorited = mCursor.getInt(Movies.INDEX_MOVIE_FAVORITE);
        Log.d("Movie",movieName);
        Log.d("Movie",favorite);
        return mCursor.getInt(Movies.INDEX_MOVIE_FAVORITE);
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem;
        int layoutType = getItemViewType(viewType);
        switch(layoutType){
            case TYPE_NORMAL:{
                layoutIdForListItem = R.layout.movie_list_item;
                break;
            }
            case TYPE_FAVORITE:{
                layoutIdForListItem = R.layout.movie_list_item_favorite;
                break;
            }
            default:{
                layoutIdForListItem = R.layout.movie_list_item_favorite;
            }
        }

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);
        return new MovieListViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int viewType = getItemViewType(position);
        Drawable background;
        Context context = holder.mView.getContext();
        switch(viewType){
            case TYPE_FAVORITE:{
                background = context.getResources().getDrawable(R.drawable.border_favorite,null );
                break;
            }
            case TYPE_NORMAL:{
                background = context.getResources().getDrawable(R.drawable.border,null );
                break;
            }
            default:{
                background = context.getResources().getDrawable(R.drawable.border,null );
            }
        }
        holder.mView.setBackground(background);

        String movieImg = mCursor.getString(Movies.INDEX_MOVIE_IMAGE);
        String movieImgUrl = TmdbApi.getImageUrl(movieImg);
        Picasso.with(holder.movieImg.getContext()).load(movieImgUrl).into(holder.movieImg);

        holder.movieName.setText( mCursor.getString(Movies.INDEX_MOVIE_TITLE));
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
        public final View mView;

        public MovieListViewHolder(View itemView) {
            super(itemView);
            this.movieName = itemView.findViewById(R.id.tv_movie_name);
            this.movieImg = itemView.findViewById(R.id.view_movie_img);
            this.mView = itemView.findViewById(R.id.recycler_item_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToFirst();
            mCursor.move(adapterPosition);
            Movies movie = new Movies(
                    mCursor.getString(Movies.INDEX_MOVIE_ID),
                    mCursor.getString(Movies.INDEX_MOVIE_TITLE),
                    mCursor.getString(Movies.INDEX_MOVIE_RELEASE_DATE),
                    mCursor.getString(Movies.INDEX_MOVIE_IMAGE),
                    mCursor.getString(Movies.INDEX_MOVIE_SYNOPSIS),
                    mCursor.getString(Movies.INDEX_MOVIE_AVERAGE),
                    mCursor.getString(Movies.INDEX_MOVIE_FAVORITE))
                    ;
            movieListAdapterOnClickHandler.onClick(movie);
        }
    }

}
