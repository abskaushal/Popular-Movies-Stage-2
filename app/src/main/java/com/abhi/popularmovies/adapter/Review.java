package com.abhi.popularmovies.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhishek on 1/11/2016.
 */
public class Review implements Parcelable{

    private String content;
    private String author;
    private String reviewId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public Review(){

    }
    private Review(Parcel parcel){
        content = parcel.readString();
        reviewId = parcel.readString();
        author = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
        parcel.writeString(reviewId);
        parcel.writeString(author);
    }

    public  static Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>(){

        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };
}
