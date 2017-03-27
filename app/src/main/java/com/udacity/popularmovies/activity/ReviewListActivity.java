package com.udacity.popularmovies.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.adapter.ReviewsRecyclerViewAdapter;
import com.udacity.popularmovies.controller.Controller;
import com.udacity.popularmovies.model.ReviewPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewListActivity extends AppCompatActivity {

    @BindView(R.id.review_list)RecyclerView reviewList;
    @BindView(R.id.background_image)ImageView backgroundImage;
    ReviewsRecyclerViewAdapter reviewListAdapter;
    String moviewTitle,backgroundImagePath;
    ArrayList<ReviewPOJO> reviewPOJOArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            reviewPOJOArrayList=bundle.getParcelableArrayList(getString(R.string.key_movie_reviews));
            moviewTitle=bundle.getString(getString(R.string.key_movie_title));
            backgroundImagePath=bundle.getString(getString(R.string.key_movie_background_image_path));
        }
        getSupportActionBar().setTitle(moviewTitle);
        if(reviewPOJOArrayList==null || reviewPOJOArrayList.isEmpty())
        {
            Toast.makeText(this, getString(R.string.no_review), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else
        {
            reviewListAdapter=new ReviewsRecyclerViewAdapter(this,reviewPOJOArrayList);
            reviewList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

            reviewList.setAdapter(reviewListAdapter);
        }
        Glide.with(this)
                .load(Controller.IMAGE_URL+backgroundImagePath)
                .into(backgroundImage);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
