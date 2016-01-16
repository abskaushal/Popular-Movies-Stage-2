package com.abhi.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ReviewActivity extends AppCompatActivity {

    private final String REVIEW_FRAG_TAG = "review_frag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        if(savedInstanceState == null){
            ReviewFragment fragment = new ReviewFragment();
            Bundle arg = new Bundle();
            arg.putString(ReviewFragment.MOVIE_ID,getIntent().getStringExtra(ReviewFragment.MOVIE_ID));
            fragment.setArguments(arg);

            getSupportFragmentManager().beginTransaction().add(R.id.review_frame,fragment,REVIEW_FRAG_TAG).commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.format(getString(R.string.review_format),getIntent().getStringExtra(ReviewFragment.MOVIE_NAME)));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
