package com.abhi.popularmovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abhi.popularmovies.R;
import com.abhi.popularmovies.webservices.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Class to create list of trailer
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {


    private Context mContext;
    private ArrayList<Trailer> mList;

    public TrailerAdapter(Context context, ArrayList<Trailer> objects) {
        mContext = context;
        mList = objects;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.trailer_name)
        TextView name;
        @Bind(R.id.row)
        RelativeLayout row;
        @Bind(R.id.trailer_pic)
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(String.format(mContext.getString(R.string.trailer_format), (position + 1)));
        Picasso.with(mContext).load(Utils.TRAILER_THUMB + mList.get(position).getTrailerKey() + "/mqdefault.jpg").placeholder(R.drawable.ic_trailer).error(R.drawable.ic_trailer).into(holder.image);
        holder.row.setTag(mList.get(position).getTrailerKey());
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchYoutubeVideo((String) view.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * Open the trailer in youtube app. If app not installed then
     * open it in the browser
     *
     * @param id
     */
    private void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Utils.YOUTUBE_URL + id));
            mContext.startActivity(intent);
        }
    }
}
