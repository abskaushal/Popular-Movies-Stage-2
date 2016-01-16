package com.abhi.popularmovies.webservices;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.abhi.popularmovies.BuildConfig;
import com.abhi.popularmovies.R;
import com.abhi.popularmovies.adapter.Movie;
import com.abhi.popularmovies.data.MovieColumns;
import com.abhi.popularmovies.data.MoviesProvider;
import com.abhi.popularmovies.listeners.IAsyncListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to retrieve the movies list from the themoviedb.org
 *
 * Created by Abhishek on 12/7/2015.
 */
public class GetMoviesAsync extends AsyncTask {

    private IAsyncListener mListener;
    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private final String SORT_PARAM = "sort_by";
    private final String APIKEY_PARAM = "api_key";
    private Context mContext;
    private ArrayList<Movie> mList;

    public GetMoviesAsync(Context context, IAsyncListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("IAsyncListener cannot be null");
        }
        mContext =  context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onAsyncStart();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String sortOrder = objects[0].toString();
        if(sortOrder.equals(mContext.getString(R.string.pref_sort_favorite))){
            getDataFromLocal();
        }else{
            getDataFromServer(sortOrder);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mListener.onAsyncStop(mList);
    }

    /**
     * Get the movies from the moviesdb server
     * @param sortOrder
     */
    private void getDataFromServer(String sortOrder){
        HttpURLConnection connection;
        BufferedReader reader;
        try {
            Uri buildURI = Uri.parse(BASE_URL).buildUpon().
                    appendQueryParameter(SORT_PARAM, "popularity."+sortOrder).
                    appendQueryParameter(APIKEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();


            URL url = new URL(buildURI.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();


            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return ;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return ;
            }
            parseJson(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get the movies from local ContentProvider
     */
    private void getDataFromLocal(){
        Cursor cursor = mContext.getContentResolver().query(MoviesProvider.Movies.MOVIES_URI, null, null, null, null);
        mList = new ArrayList<Movie>(cursor.getCount());
        Movie movie;
        if(cursor.moveToFirst()){
            do{
                movie = new Movie();

                movie.setAdult(cursor.getInt(cursor.getColumnIndex(MovieColumns.ADULT)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieColumns.BACKDROP_PATH)));
                movie.setMovieId(cursor.getInt(cursor.getColumnIndex(MovieColumns.MOV_ID)));
                movie.setOrigLanguage(cursor.getString(cursor.getColumnIndex(MovieColumns.ORIGINAL_LANGUAGE)));
                movie.setOrigTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.ORIGINAL_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieColumns.OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASE_DATE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER_PATH)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieColumns.POPULARITY)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.TITLE)));
                movie.setVideo(cursor.getInt(cursor.getColumnIndex(MovieColumns.VIDEO)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieColumns.VOTE_AVERAGE)));
                movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieColumns.VOTE_COUNT)));
                movie.setGenreIds(Utils.stringToArray(cursor.getString(cursor.getColumnIndex(MovieColumns.GENRE))));

                mList.add(movie);
            }while (cursor.moveToNext());
        }
    }



    /**
     * Parse the json data to ArrayList of {@link Movie}
     *
     * @param jsonString
     */
    private void parseJson(String jsonString) {
        final String RESULT = "results";
        final String ADULT = "adult";
        final String BACKDROP_PATH = "backdrop_path";
        final String GENRE = "genre_ids";
        final String ID = "id";
        final String ORIGINAL_LANGUAGE = "original_language";
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String POPULARITY = "popularity";
        final String TITLE = "title";
        final String VIDEO = "video";
        final String VOTE_AVERAGE = "vote_average";
        final String VOTE_COUNT = "vote_count";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(RESULT);

            mList = new ArrayList<Movie>(jsonArray.length());
            Movie movie;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieJson = jsonArray.getJSONObject(i);
                movie = new Movie();
                movie.setAdult(getIntFromBoolean(movieJson.getBoolean(ADULT)));
                movie.setBackdropPath(movieJson.getString(BACKDROP_PATH));
                movie.setMovieId(movieJson.getInt(ID));
                movie.setOrigLanguage(movieJson.getString(ORIGINAL_LANGUAGE));
                movie.setOrigTitle(movieJson.getString(ORIGINAL_TITLE));
                movie.setOverview(movieJson.getString(OVERVIEW));
                movie.setReleaseDate(movieJson.getString(RELEASE_DATE));
                movie.setPosterPath(movieJson.getString(POSTER_PATH));
                movie.setPopularity(movieJson.getDouble(POPULARITY));
                movie.setTitle(movieJson.getString(TITLE));
                movie.setVideo(getIntFromBoolean(movieJson.getBoolean(VIDEO)));
                movie.setVoteAverage(movieJson.getDouble(VOTE_AVERAGE));
                movie.setVoteCount(movieJson.getInt(VOTE_COUNT));

                JSONArray ids = movieJson.getJSONArray(GENRE);
                int array[] = new int[ids.length()];
                for(int k=0; k<ids.length();k++){
                    int val = ids.getInt(k);
                    array[k] = val;
                }
                movie.setGenreIds(array);

                mList.add(movie);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getIntFromBoolean(boolean value){
        if(value){
            return 1;
        }else{
            return 0;
        }
    }
}
