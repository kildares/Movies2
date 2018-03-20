package com.example.kilda.movies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.kilda.movies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kilda on 2/8/2018.
 */

public class TmdbApi {

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final String LANGUAGE = "en-US";
    private static final String PAGE = "1";
    private static final String REGION = "Brazil";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String IMG_SIZE_STANDARD = "w185/";
    private static final String API_KEY_URL = "api_key";
    private static final String LANGUAGE_URL = "language";
    private static final String PAGE_URL = "page";
    private static final String REGION_URL = "region";
    private static final String POPULAR_URL = "popular";
    private static final String TOP_RATED_URL = "top_rated";

    public static final int TOP_RATED = 55;
    public static final int FAVORITES = 56;
    public static final int POPULAR = 57;


    public static URL buildMovieQueryURL(int queryType)
    {
        URL url = null;
        switch(queryType){
            case TOP_RATED:{
                url = buildTopRatedRequestURL();
            }
            case FAVORITES:{
                Log.d("MOVIE","Favorite option chosen. Querying default TOP_RATED");
                url = buildTopRatedRequestURL();
            }
            case POPULAR:{
                url = buildPopularRequestURL();
            }
            default:{
                Log.e("MOVIE","Invalid movie query option. Querying default TOP_RATED");
                url = buildTopRatedRequestURL();
            }
        }
        return url;
    }

    public static URL buildTopRatedRequestURL()
    {
        Uri builtUri = Uri.parse(BASE_URL + TOP_RATED_URL).buildUpon().appendQueryParameter(API_KEY_URL, API_KEY).
                appendQueryParameter(LANGUAGE_URL,LANGUAGE).
                appendQueryParameter(PAGE,PAGE_URL).
                appendQueryParameter(REGION_URL,REGION)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildPopularRequestURL()
    {
        Uri builtUri = Uri.parse(BASE_URL + POPULAR_URL).buildUpon().appendQueryParameter(API_KEY_URL, API_KEY).
                appendQueryParameter(LANGUAGE_URL,LANGUAGE).
                appendQueryParameter(PAGE,PAGE_URL).
                appendQueryParameter(REGION_URL,REGION)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildRatePostURL(String id)
    {
        Uri builtUri = Uri.parse(BASE_URL + id + "/rating").buildUpon().appendQueryParameter(API_KEY_URL,API_KEY).build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildAuthRequest()
    {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(API_KEY_URL,API_KEY).build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

    public static String getImageUrl(String movieImg) {
        return "http://image.tmdb.org/t/p/" + IMG_SIZE_STANDARD + movieImg;
    }
}
