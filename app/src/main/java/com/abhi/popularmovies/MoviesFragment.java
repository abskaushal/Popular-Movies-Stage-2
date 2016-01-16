package com.abhi.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abhi.popularmovies.adapter.Movie;
import com.abhi.popularmovies.adapter.MovieAdapter;
import com.abhi.popularmovies.listeners.IAsyncListener;
import com.abhi.popularmovies.webservices.GetMoviesAsync;
import com.abhi.popularmovies.webservices.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Fragment displays the grid of movies.
 */
public class MoviesFragment extends Fragment implements IAsyncListener {


    @Bind(R.id.movie_list)
    RecyclerView recyclerView;
    @Bind(R.id.no_movies_available)
    RelativeLayout noMovieRelative;
    private OnFragmentInteractionListener mListener;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> mList;
    private GetMoviesAsync mAsync;
    private final String MOVIES_KEY = "movies";
    private ProgressDialog mProgress;
    private boolean mTwoPain = false;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mTwoPain = getArguments().getBoolean("twopain");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, rootView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.columns));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_KEY)) {
            mList = new ArrayList<Movie>();
            if (Utils.isNetworkConnected(getContext()) || Utils.getSortOrder(getContext()).equalsIgnoreCase(getString(R.string.pref_sort_favorite))) {
                mAsync = new GetMoviesAsync(getContext(), this);
                mAsync.execute(Utils.getSortOrder(getContext()));
            } else {
                Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
                noMovieRelative.setVisibility(View.VISIBLE);
            }
        } else {
            mList = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            onAsyncStop(mList);
        }


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_KEY, mList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAsyncStart() {
        if (mProgress == null) {
            mProgress = ProgressDialog.show(getContext(), "Loading", "Please wait...");
        }
    }

    @Override
    public void onAsyncStop(Object object) {
        if (mProgress != null) {
            mProgress.cancel();
        }
        if (object instanceof ArrayList) {
            mList = (ArrayList<Movie>) object;
        }
        mAdapter = new MovieAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
        if (mList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noMovieRelative.setVisibility(View.GONE);
            if (mTwoPain) {
                onGridViewItemSelected(mList.get(0));
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            noMovieRelative.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), getString(R.string.no_movies), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onMovieSelected(Movie movie);
    }

    public void changeOrder(String order) {
        if (Utils.isNetworkConnected(getContext()) || order.equalsIgnoreCase(getString(R.string.pref_sort_favorite))) {
            mAsync = new GetMoviesAsync(getContext(), this);
            mAsync.execute(order);
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            noMovieRelative.setVisibility(View.VISIBLE);
        }
    }

    public void onGridViewItemSelected(Movie movie) {
        mListener.onMovieSelected(movie);
    }
}
