package com.udacity.popularmovies.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.controller.Controller;
import com.udacity.popularmovies.model.LanguageEnum;
import com.udacity.popularmovies.model.PopularMoviePOJO;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.movie_backdrop_image)ImageView imgBackDrop;
    @BindView(R.id.movie_images)ImageView imgMovieThumbnail;
    @BindView(R.id.background_image)ImageView backgroundImage;
    @BindView(R.id.movie_title)TextView txtMoviewTitle;
    @BindView(R.id.release_date)TextView txtReleaseDate;
    @BindView(R.id.ratings)TextView txtRatings;
    @BindView(R.id.voter_count)TextView txtVoterCount;
    @BindView(R.id.overview_txt)TextView txtOverview;
    @BindView(R.id.language_txt)TextView txtLanguage;


    PopularMoviePOJO popularMoviePOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            popularMoviePOJO=bundle.getParcelable(getString(R.string.key_movie_detail));
        }
        else if(savedInstanceState!=null && savedInstanceState.containsKey(getString(R.string.key_movie_detail)))
        {
            popularMoviePOJO=savedInstanceState.getParcelable(getString(R.string.key_movie_detail));
        }
        setValues();
    }

    private void setValues()
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTo=new SimpleDateFormat("dd-MM-yyyy");
        if(popularMoviePOJO!=null)
        {
            Glide.with(this)
                    .load(Controller.IMAGE_URL+popularMoviePOJO.getPoster_path())
                    .into(imgMovieThumbnail);
            Glide.with(this)
                    .load(Controller.IMAGE_URL+popularMoviePOJO.getPoster_path())
                    .into(backgroundImage);
            Glide.with(this)
                    .load(Controller.IMAGE_URL+popularMoviePOJO.getBackdrop_path())
                    .into(imgBackDrop);
            getSupportActionBar().setTitle(popularMoviePOJO.getTitle());
            txtMoviewTitle.setText(popularMoviePOJO.getOriginal_title());
            try {
                Date date = dateFormat.parse(popularMoviePOJO.getRelease_date());
                txtReleaseDate.setText(formatTo.format(date));
            }catch (Exception e)
            {
                txtReleaseDate.setText(popularMoviePOJO.getRelease_date());
            }

            txtRatings.setText(popularMoviePOJO.getVote_average()+"");
            txtVoterCount.setText(popularMoviePOJO.getVote_count()+"");
            txtOverview.setText(popularMoviePOJO.getOverview());
            txtLanguage.setText(" "+LanguageEnum.findFromCode(popularMoviePOJO.getOriginal_language()).getName());

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
