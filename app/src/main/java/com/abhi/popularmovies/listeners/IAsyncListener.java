package com.abhi.popularmovies.listeners;

import com.abhi.popularmovies.adapter.Movie;

import java.util.ArrayList;

/**
 * Interface for AsyncTask to communicxate with the fragment.
 *
 * Created by Abhishek on 12/7/2015.
 */
public interface IAsyncListener {

    /**
     * Called when AsyncTask starts.
     */
    public void onAsyncStart();

    /**
     * Called on AsyncStart complete.
     *
     * @param object
     */
    public void onAsyncStop(Object object);
}
