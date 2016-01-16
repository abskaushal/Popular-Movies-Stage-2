package com.abhi.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.DataType.Type;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * MovieColumns contains all the columns related to movie table
 *
 * Created by Abhishek on 1/12/2016.
 */
public interface MovieColumns {
    @DataType(Type.INTEGER)  @PrimaryKey @AutoIncrement String _ID = "id";
    @DataType(Type.INTEGER) String ADULT = "adult";
    @DataType(Type.TEXT) String BACKDROP_PATH = "backdrop_path";
    @DataType(Type.TEXT) String GENRE = "genre_ids";
    @DataType(Type.INTEGER) @Unique String MOV_ID = "mov_id";
    @DataType(Type.TEXT) String ORIGINAL_LANGUAGE = "original_language";
    @DataType(Type.TEXT) String ORIGINAL_TITLE = "original_title";
    @DataType(Type.TEXT) String OVERVIEW = "overview";
    @DataType(Type.TEXT) String RELEASE_DATE = "release_date";
    @DataType(Type.TEXT) String POSTER_PATH = "poster_path";
    @DataType(Type.REAL) String POPULARITY = "popularity";
    @DataType(Type.TEXT) String TITLE = "title";
    @DataType(Type.INTEGER) String VIDEO = "video";
    @DataType(Type.REAL) String VOTE_AVERAGE = "vote_average";
    @DataType(Type.INTEGER) String VOTE_COUNT = "vote_count";
}
