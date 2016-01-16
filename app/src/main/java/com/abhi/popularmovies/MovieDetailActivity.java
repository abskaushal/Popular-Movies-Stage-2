package com.abhi.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.abhi.popularmovies.adapter.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String DETAILFRAG_TAG = "detail_frag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if(savedInstanceState == null){
            DetailFragment fragment = new DetailFragment();
            Bundle arg = new Bundle();
            arg.putParcelable(DetailFragment.MOVIE_OBJECT,getIntent().getParcelableExtra(DetailFragment.MOVIE_OBJECT));
            fragment.setArguments(arg);

            getSupportFragmentManager().beginTransaction().add(R.id.detail_frame,fragment,DETAILFRAG_TAG).commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
