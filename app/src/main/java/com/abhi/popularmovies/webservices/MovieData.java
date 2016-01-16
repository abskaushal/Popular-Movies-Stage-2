package com.abhi.popularmovies.webservices;

import com.abhi.popularmovies.R;

/**
 * Created by Abhishek on 1/9/2016.
 */
public enum MovieData {
    VIDEOS("videos"),
    REVIEWS("reviews");

    private String mValue;


    MovieData(String value) {
        mValue = value;
    }

    public String value(){
        return  mValue;
    }
}
