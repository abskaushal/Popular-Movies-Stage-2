package com.abhi.popularmovies.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhishek on 1/10/2016.
 */
public class Trailer implements Parcelable{

    private String trailerKey;
    private String trailerId;

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public  Trailer(){

    }

    private Trailer(Parcel parcel){
        trailerKey = parcel.readString();
        trailerId = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trailerKey);
        parcel.writeString(trailerId);
    }

    public static  final  Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>(){

        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };
}
