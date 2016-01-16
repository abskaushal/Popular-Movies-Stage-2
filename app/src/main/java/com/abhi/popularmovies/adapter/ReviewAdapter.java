package com.abhi.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.abhi.popularmovies.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ReviewAdapter class create the list of all the reviews related to movie.
 * <p/>
 * Created by Abhishek on 1/11/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Review> mList;

    public ReviewAdapter(Context context, ArrayList<Review> objects) {
        mContext = context;
        mList = objects;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.author)
        TextView authorTv;
        @Bind(R.id.review)
        TextView reviewTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.authorTv.setText(mList.get(position).getAuthor());
        holder.reviewTv.setText(mList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
