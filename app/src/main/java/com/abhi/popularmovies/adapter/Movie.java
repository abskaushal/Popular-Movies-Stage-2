package com.abhi.popularmovies.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie class to holds detail related to movie
 */
public class Movie implements Parcelable {

    private int adult;
    private String backdropPath;
    private int[] genreIds;
    private int movieId;
    private String origLanguage;
    private String origTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private Double popularity;
    private String title;
    private int video;
    private Double voteAverage;
    private int voteCount;




    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOrigLanguage() {
        return origLanguage;
    }

    public void setOrigLanguage(String origLanguage) {
        this.origLanguage = origLanguage;
    }

    public String getOrigTitle() {
        return origTitle;
    }

    public void setOrigTitle(String origTitle) {
        this.origTitle = origTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Movie() {

    }

    private Movie(Parcel parcel) {
        adult = parcel.readInt();
        backdropPath = parcel.readString();
        genreIds = parcel.createIntArray();
        movieId = parcel.readInt();
        origLanguage = parcel.readString();
        origTitle = parcel.readString();
        overview = parcel.readString();
        releaseDate = parcel.readString();
        posterPath = parcel.readString();
        popularity = parcel.readDouble();
        title = parcel.readString();
        video = parcel.readInt();
        voteAverage = parcel.readDouble();
        voteCount = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(adult);
        parcel.writeString(backdropPath);
        parcel.writeIntArray(genreIds);
        parcel.writeInt(movieId);
        parcel.writeString(origLanguage);
        parcel.writeString(origTitle);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeDouble(popularity);
        parcel.writeString(title);
        parcel.writeInt(video);
        parcel.writeDouble(voteAverage);
        parcel.writeInt(voteCount);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
