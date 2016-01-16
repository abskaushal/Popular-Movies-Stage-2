package com.abhi.popularmovies.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhi.popularmovies.MoviesFragment;
import com.abhi.popularmovies.R;
import com.abhi.popularmovies.webservices.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * MovieAdapter create the gridview for movies
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private MoviesFragment mFragment;
    private List<Movie> mList;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView title;
        @Bind(R.id.movie_image) ImageView image;
        @Bind(R.id.cardview) CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }



    public MovieAdapter(MoviesFragment fragment, List<Movie> list) {
        mFragment = fragment;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.movie_grid, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.cardView.setTag(position);
        Picasso.with(mFragment.getContext()).load(Utils.IMAGE_PATH + mList.get(position).getPosterPath()).placeholder(R.drawable.ic_trailer).error(R.drawable.ic_trailer).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                mFragment.onGridViewItemSelected(mList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}