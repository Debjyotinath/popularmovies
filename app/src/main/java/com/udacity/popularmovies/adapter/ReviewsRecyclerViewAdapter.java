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
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.ReviewPOJO;
import com.udacity.popularmovies.model.TrailerVideoPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by debjyotinath on 10/02/17.
 */

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolderItem> {

    LayoutInflater layoutInflater;
    ArrayList<ReviewPOJO> movieList;
    Activity activity;

    public ReviewsRecyclerViewAdapter(Activity activity, ArrayList<ReviewPOJO> movieList) {
        layoutInflater=LayoutInflater.from(activity);
        this.activity = activity;
        this.movieList=movieList;
    }

    public void setTrailerList(ArrayList<ReviewPOJO> movieList) {
        this.movieList = movieList;
    }

    @Override
    public ReviewsRecyclerViewAdapter.ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.review_item_layout,parent,false);
        ViewHolderItem viewHolderItem=new ViewHolderItem(view);

        return viewHolderItem;
    }

    @Override
    public void onBindViewHolder(final ReviewsRecyclerViewAdapter.ViewHolderItem holder, int position) {

        final ReviewPOJO reviewPOJO=movieList.get(position);
        holder.txtReviewAuthor.setText("- "+reviewPOJO.getAuthor());
        holder.txtReviewContent.setText("\" "+reviewPOJO.getContent()+" \"");

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    class ViewHolderItem extends RecyclerView.ViewHolder
    {
        @BindView(R.id.review_author)
        TextView txtReviewAuthor;
        @BindView(R.id.review_content)TextView txtReviewContent;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
