package com.abhi.popularmovies.webservices;

import android.net.Uri;
import android.os.AsyncTask;

import com.abhi.popularmovies.BuildConfig;
import com.abhi.popularmovies.adapter.Movie;
import com.abhi.popularmovies.adapter.Review;
import com.abhi.popularmovies.adapter.Trailer;
import com.abhi.popularmovies.listeners.IAsyncListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Abhishek on 1/9/2016.
 */
public class GetMovieDataAsync extends AsyncTask {

    private IAsyncListener mListener;
    private MovieData mData;
    private String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final String APIKEY_PARAM = "api_key";
    private ArrayList mList;

    public GetMovieDataAsync(IAsyncListener listener, MovieData data) {
        if (listener == null) {
            throw new IllegalArgumentException("IAsyncListener cannot be null");
        }
        mListener = listener;
        mData = data;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onAsyncStart();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String movieId = objects[0].toString();
        BASE_URL = BASE_URL + movieId + "/" + mData.value() + "?";
        HttpURLConnection connection;
        BufferedReader bufferedReader;
        try {
            Uri buildURI = Uri.parse(BASE_URL).buildUpon().
                    appendQueryParameter(APIKEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();

            URL url = new URL(buildURI.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            StringBuffer buffer = new StringBuffer();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }

            parseData(buffer.toString());
        } catch (IOException e) {

        }
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mListener.onAsyncStop(mList);
    }

    private void parseData(String data) {

        final String ID = "id";
        final String CONTENT = "content";
        final String AUTHOR = "author";
        final String KEY = "key";
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            if (mData == MovieData.VIDEOS) {
                mList = new ArrayList<Trailer>(jsonArray.length());
                Trailer trailer;
                for (int i = 0; i < jsonArray.length(); i++) {
                    trailer = new Trailer();
                    JSONObject object = jsonArray.getJSONObject(i);
                    trailer.setTrailerId(object.getString(ID));
                    trailer.setTrailerKey(object.getString(KEY));
                    mList.add(trailer);
                }
            } else {
                mList = new ArrayList<Review>(jsonArray.length());
                Review review;
                for (int i = 0; i < jsonArray.length(); i++) {
                    review = new Review();
                    JSONObject object = jsonArray.getJSONObject(i);
                    review.setReviewId(object.getString(ID));
                    review.setContent(object.getString(CONTENT));
                    review.setAuthor(object.getString(AUTHOR));
                    mList.add(review);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
