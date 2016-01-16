package com.abhi.popularmovies;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.abhi.popularmovies.adapter.Movie;
import com.abhi.popularmovies.adapter.Review;
import com.abhi.popularmovies.adapter.ReviewAdapter;
import com.abhi.popularmovies.listeners.IAsyncListener;
import com.abhi.popularmovies.webservices.GetMovieDataAsync;
import com.abhi.popularmovies.webservices.GetMoviesAsync;
import com.abhi.popularmovies.webservices.MovieData;
import com.abhi.popularmovies.webservices.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * ReviewFragment conatins the list of all the reviews
 */
public class ReviewFragment extends Fragment implements IAsyncListener{

    @Bind(R.id.review_list) RecyclerView reviewList;

    public static final String MOVIE_ID = "id";
    public static final String MOVIE_NAME = "name";
    private final String REVIEW_KEY = "reviews";
    private String mMovieId;
    private GetMovieDataAsync mAsync;
    private ArrayList<Review> mList;
    private ProgressDialog mProgress;



    public ReviewFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           mMovieId = getArguments().getString(MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this,view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        reviewList.setHasFixedSize(true);
        reviewList.setLayoutManager(gridLayoutManager);
        if (savedInstanceState == null || !savedInstanceState.containsKey(REVIEW_KEY)) {
            mList = new ArrayList<Review>();
            if (Utils.isNetworkConnected(getContext())) {
                mAsync = new GetMovieDataAsync(this, MovieData.REVIEWS);
                mAsync.execute(mMovieId);
            } else {
                Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            mList = savedInstanceState.getParcelableArrayList(REVIEW_KEY);
            onAsyncStop(mList);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(REVIEW_KEY,mList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAsyncStart() {
        if(mProgress==null){
            mProgress = ProgressDialog.show(getContext(), "Loading", "Please wait...");
        }
    }

    @Override
    public void onAsyncStop(Object object) {
        if(mProgress!=null){
            mProgress.cancel();
        }
        if(object instanceof ArrayList){
            mList = (ArrayList<Review>) object;
            if(mList.size()>0) {
                ReviewAdapter adapter = new ReviewAdapter(getContext(), mList);
                reviewList.setAdapter(adapter);
            }else{
                Toast.makeText(getContext(),getString(R.string.no_review),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
