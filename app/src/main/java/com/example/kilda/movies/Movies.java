package com.example.kilda.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kilda.movies.moviesDB.MoviesDbContract;

/**
 * Created by kilda on 2/12/2018.
 */

public class Movies implements Parcelable{
    private String image;
    private String name;
    private String year;
    private String synopsis;
    private String movieId;
    private String average;
    private String favorite;

    public Movies(String movieId, String name, String year, String image, String synopsis,String average, String favorite)
    {
        this.movieId = movieId;
        this.name=name;
        this.year=year;
        this.image=image;
        this.synopsis=synopsis;
        this.average=average;
        this.favorite=favorite;
    }

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




    private Movies(Parcel parcel) {

        this.image = parcel.readString();
        this.name = parcel.readString();
        this.year = parcel.readString();
        this.synopsis = parcel.readString();
        this.movieId = parcel.readString();
        this.average = parcel.readString();
        this.favorite=parcel.readString();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>(){

        @Override
        public Movies createFromParcel(Parcel parcel) {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int i) {
            return new Movies[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.image);
        parcel.writeString(this.name);
        parcel.writeString(this.year);
        parcel.writeString(this.synopsis);
        parcel.writeString(this.movieId);
        parcel.writeString(this.average);
        parcel.writeString(this.favorite);
    }

    public String getFavorite() {
        return favorite;
    }

    public boolean isFavorite()
    {
        return getFavorite().equals("1");
    }

    public void
    setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
