package com.abhi.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.net.URI;

/**
 * Provider class to create ContentProvider for movies database.
 *
 * Created by Abhishek on 1/12/2016.
 */

@ContentProvider(authority = MoviesProvider.AUTHORITY, database = MovieDatabase.class)
public final class MoviesProvider {

    public static final String AUTHORITY = "com.abhi.popularmovies.data.provider";

    @TableEndpoint(table = MovieDatabase.MOVIES) public static class Movies{

        @ContentUri(path = MovieDatabase.MOVIES,
        type = "vnd.android.cursor.dir/movie",
        defaultSort = MovieColumns._ID + " DESC")
        public static final Uri MOVIES_URI = Uri.parse("content://"+ AUTHORITY +"/movies");

    }
}
