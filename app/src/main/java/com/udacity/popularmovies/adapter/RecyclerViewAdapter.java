package com.udacity.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.activity.MovieDetailsActivity;
import com.udacity.popularmovies.controller.Controller;
import com.udacity.popularmovies.model.PopularMoviePOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by debjyotinath on 10/02/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderItem> {

    LayoutInflater layoutInflater;
    ArrayList<PopularMoviePOJO> movieList;
    Activity activity;

    public RecyclerViewAdapter(Activity activity, ArrayList<PopularMoviePOJO> movieList) {
        layoutInflater=LayoutInflater.from(activity);
        this.activity = activity;
        this.movieList=movieList;
    }

    public void setMovieList(ArrayList<PopularMoviePOJO> movieList) {
        this.movieList = movieList;
    }

    @Override
    public RecyclerViewAdapter.ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.list_item,parent,false);
        ViewHolderItem viewHolderItem=new ViewHolderItem(view);

        return viewHolderItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolderItem holder, int position) {

        final PopularMoviePOJO popularMoviePOJO=movieList.get(position);
        if(popularMoviePOJO!=null)
        {
            Glide.with(activity)
                    .load(Controller.IMAGE_URL+popularMoviePOJO.getPoster_path())
                    .into(holder.imgPopularMovieThumbnail);
        }
        holder.materialRippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity,MovieDetailsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable(activity.getString(R.string.key_movie_detail),popularMoviePOJO);
                intent.putExtras(bundle);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(activity, (ImageView)holder.imgPopularMovieThumbnail, activity.getString(R.string.transition_name));
                activity.startActivity(intent,options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    class ViewHolderItem extends RecyclerView.ViewHolder
    {
        @BindView(R.id.movie_images)
        ImageView imgPopularMovieThumbnail;
        @BindView(R.id.parent_container)RelativeLayout rlParentContainer;
        @BindView(R.id.material_click)MaterialRippleLayout  materialRippleLayout;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
