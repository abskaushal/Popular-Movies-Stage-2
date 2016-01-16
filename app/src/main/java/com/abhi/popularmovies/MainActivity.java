package com.abhi.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.abhi.popularmovies.adapter.Movie;
import com.abhi.popularmovies.webservices.Utils;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnFragmentInteractionListener {

    private static final String DETAILFRAGMENT_TAG = "DETAIL";
    private boolean mTwoPain;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortOrder = Utils.getSortOrder(this);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.detail_frame) != null) {
            mTwoPain = true;
        } else {
            mTwoPain = false;
            getSupportActionBar().setElevation(0f);
        }
        MoviesFragment fragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.movie_frag_tag));
        Bundle bundle = new Bundle();
        bundle.putBoolean("twopain", mTwoPain);
        if (fragment == null) {
            fragment = new MoviesFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.movies_frame, fragment, getString(R.string.movie_frag_tag)).commit();
        } else {
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (mTwoPain) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.MOVIE_OBJECT, movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_frame, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(DetailFragment.MOVIE_OBJECT, movie);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortOrder = Utils.getSortOrder(this);
        if (sortOrder != null && !sortOrder.equals(mSortOrder)) {
            MoviesFragment fragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.movie_frag_tag));
            if (fragment != null) {
                fragment.changeOrder(sortOrder);
                mSortOrder = sortOrder;
            }

            if (mTwoPain) {
                DetailFragment detailFrag = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
                if (detailFrag != null) {
                    getSupportFragmentManager().beginTransaction().remove(detailFrag).commit();
                }
            }
        }
    }
}
