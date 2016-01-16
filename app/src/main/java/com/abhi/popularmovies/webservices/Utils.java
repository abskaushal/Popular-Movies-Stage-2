package com.abhi.popularmovies.webservices;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.abhi.popularmovies.R;

/**
 * Utils class to hold utility methods to be used across application.
 * <p/>
 * Created by Abhishek on 12/8/2015.
 */
public class Utils {
    public static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
    public static final String IMAGE_PATH_DETAIL = "http://image.tmdb.org/t/p/w342";
    public static final String TRAILER_THUMB = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    /**
     * Get the Internet Network status ie connected or not
     *
     * @param context
     * @return boolean
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cManager.getActiveNetworkInfo() != null;
    }

    /**
     * Get sort order of the movies from the shared preference.
     *
     * @param context
     * @return
     */
    public static String getSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_desc));
    }

    /**
     * Convert array to string for genre ids to be stored in database.
     *
     * @param arr
     * @return
     */
    public static String arrayToString(int arr[]) {
        String string = "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            buffer.append(arr[i] + ",");
        }
        string = buffer.toString();
        return string;
    }

    /**
     * Convert the string to array of genre ids when fetched from the database.
     *
     * @param string
     * @return
     */
    public static int[] stringToArray(String string) {
        int arr[] = null;
        String strArr[] = string.split(",");
        arr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            arr[i] = Integer.parseInt(strArr[i]);
        }
        return arr;
    }
}
