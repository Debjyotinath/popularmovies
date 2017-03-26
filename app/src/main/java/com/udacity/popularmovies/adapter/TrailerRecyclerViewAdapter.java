package com.udacity.popularmovies.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.TrailerVideoPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by debjyotinath on 10/02/17.
 */

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.ViewHolderItem> {

    LayoutInflater layoutInflater;
    ArrayList<TrailerVideoPOJO> movieList;
    Activity activity;

    public TrailerRecyclerViewAdapter(Activity activity, ArrayList<TrailerVideoPOJO> movieList) {
        layoutInflater=LayoutInflater.from(activity);
        this.activity = activity;
        this.movieList=movieList;
    }

    public void setTrailerList(ArrayList<TrailerVideoPOJO> movieList) {
        this.movieList = movieList;
    }

    @Override
    public TrailerRecyclerViewAdapter.ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.trailer_list_item,parent,false);
        ViewHolderItem viewHolderItem=new ViewHolderItem(view);

        return viewHolderItem;
    }

    @Override
    public void onBindViewHolder(final TrailerRecyclerViewAdapter.ViewHolderItem holder, int position) {

        final TrailerVideoPOJO trailerVideoPOJO=movieList.get(position);
        if(trailerVideoPOJO.getType().equals("Trailer")) {
            if (trailerVideoPOJO != null) {
                Glide.with(activity)
                        .load(activity.getString(R.string.youtube_image_url, trailerVideoPOJO.getKey()))
                        .into(holder.imgTrailerVideoThumbnail);
            }
            holder.materialRippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+trailerVideoPOJO.getKey())));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    class ViewHolderItem extends RecyclerView.ViewHolder
    {
        @BindView(R.id.trailer_images)
        ImageView imgTrailerVideoThumbnail;
        @BindView(R.id.parent_container)RelativeLayout rlParentContainer;
        @BindView(R.id.material_click)MaterialRippleLayout  materialRippleLayout;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
