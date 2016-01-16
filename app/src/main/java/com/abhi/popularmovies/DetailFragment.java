package com.abhi.popularmovies;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.popularmovies.adapter.Movie;
import com.abhi.popularmovies.adapter.Trailer;
import com.abhi.popularmovies.adapter.TrailerAdapter;
import com.abhi.popularmovies.data.MovieColumns;
import com.abhi.popularmovies.data.MoviesProvider;
import com.abhi.popularmovies.listeners.IAsyncListener;
import com.abhi.popularmovies.webservices.GetMovieDataAsync;
import com.abhi.popularmovies.webservices.GetMoviesAsync;
import com.abhi.popularmovies.webservices.MovieData;
import com.abhi.popularmovies.webservices.Utils;
import com.squareup.picasso.Picasso;

import net.simonvt.schematic.annotation.ContentProvider;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Fragment to display the Movie detail.
 */
public class DetailFragment extends Fragment implements IAsyncListener {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.year)
    TextView year;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.description)
    TextView description;
    @Bind(R.id.votes) TextView votes;
    @Bind(R.id.poster_image)
    ImageView image;
    @Bind(R.id.trailer_list)
    RecyclerView trailerView;

    @OnClick(R.id.make_fav_btn)
    public void markFavorite() {
        addMovieToDb();
    }

    @OnClick(R.id.share) public void shareVideo(){
        if(mList.size()>0) {
            String url = "http://www.youtube.com/watch?v" + mList.get(0).getTrailerKey();
            String shareMsg =  getString(R.string.share_trailer_format)+" "+mMovie.getTitle()+" "+url;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMsg);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
        }else{
            Toast.makeText(getContext(),getString(R.string.no_trailers),Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.reviews)
    public void startReviewActivity() {
        Intent reviewIntent = new Intent(getContext(), ReviewActivity.class);
        reviewIntent.putExtra(ReviewFragment.MOVIE_ID, String.valueOf(mMovie.getMovieId()));
        reviewIntent.putExtra(ReviewFragment.MOVIE_NAME, String.valueOf(mMovie.getOrigTitle()));
        startActivity(reviewIntent);
    }

    public static final String MOVIE_OBJECT = "movie";
    private ArrayList<Trailer> mList;
    private ListView mListView;
    private final String MOVIES_KEY = "movies";
    private Movie mMovie;
    private GetMovieDataAsync mAsync;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(MOVIE_OBJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        trailerView.setLayoutManager(mLayoutManager);
        trailerView.setHasFixedSize(true);
        setMovieData();

        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_KEY)) {
            mList = new ArrayList<Trailer>();
            if (Utils.isNetworkConnected(getContext())) {
                mAsync = new GetMovieDataAsync(this, MovieData.VIDEOS);
                mAsync.execute(mMovie.getMovieId());
            } else {
                Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            mList = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            onAsyncStop(mList);
        }
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setMovieData() {

        if (mMovie != null) {
            title.setText(mMovie.getOrigTitle());
            year.setText(getYear(mMovie.getReleaseDate()));
            rating.setText(String.format(getString(R.string.rating_format), mMovie.getVoteAverage()));
            description.setText(mMovie.getOverview());
            votes.setText(String.valueOf(mMovie.getVoteCount()));
            Picasso.with(getContext()).load(Utils.IMAGE_PATH + mMovie.getPosterPath()).placeholder(R.drawable.ic_trailer).error(R.drawable.ic_no_image).into(image);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_KEY, mList);
        super.onSaveInstanceState(outState);
    }

    /**
     * Get the year from date string.
     *
     * @param date
     * @return
     */
    private String getYear(String date) {
        String year = "N/A";
        if (date != null && !date.equals("")) {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            Date movieDate = null;
            try {
                movieDate = parser.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(movieDate);
            year = "" + cal.get(Calendar.YEAR);
        }
        return year;
    }

    /**
     * Add favorite movie to database.
     */
    private void addMovieToDb() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieColumns.ADULT,mMovie.getAdult());
        contentValues.put(MovieColumns.BACKDROP_PATH,mMovie.getBackdropPath());
        contentValues.put(MovieColumns.GENRE,Utils.arrayToString(mMovie.getGenreIds()));
        contentValues.put(MovieColumns.MOV_ID,mMovie.getMovieId());
        contentValues.put(MovieColumns.ORIGINAL_LANGUAGE,mMovie.getOrigLanguage());
        contentValues.put(MovieColumns.ORIGINAL_TITLE,mMovie.getOrigTitle());
        contentValues.put(MovieColumns.OVERVIEW,mMovie.getOverview());
        contentValues.put(MovieColumns.RELEASE_DATE,mMovie.getReleaseDate());
        contentValues.put(MovieColumns.POSTER_PATH,mMovie.getPosterPath());
        contentValues.put(MovieColumns.POPULARITY,mMovie.getPopularity());
        contentValues.put(MovieColumns.TITLE,mMovie.getTitle());
        contentValues.put(MovieColumns.VIDEO, mMovie.getVideo());
        contentValues.put(MovieColumns.VOTE_AVERAGE, mMovie.getVoteAverage());
        contentValues.put(MovieColumns.VOTE_COUNT, mMovie.getVoteCount());

        try {
            getActivity().getContentResolver().insert(MoviesProvider.Movies.MOVIES_URI, contentValues);
            Toast.makeText(getContext(),getString(R.string.movie_added_as_favorite),Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            if(ex instanceof SQLiteConstraintException){
                Toast.makeText(getContext(), getString(R.string.movie_already_added_as_favorite), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), getString(R.string.movie_added_as_favorite_problem), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAsyncStart() {

    }

    @Override
    public void onAsyncStop(Object object) {

        if (object instanceof ArrayList) {
            mList = (ArrayList<Trailer>) object;
            if(mList!=null && mList.size()>0) {
                TrailerAdapter adapter = new TrailerAdapter(getContext(), mList);
                trailerView.setAdapter(adapter);
            }else{
                Toast.makeText(getContext(),getString(R.string.no_trailers),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
