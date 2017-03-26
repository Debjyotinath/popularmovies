package com.udacity.popularmovies.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.popularmovies.BuildConfig;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.adapter.TrailerRecyclerViewAdapter;
import com.udacity.popularmovies.controller.Controller;
import com.udacity.popularmovies.data.MovieContract;
import com.udacity.popularmovies.model.LanguageEnum;
import com.udacity.popularmovies.model.PopularMoviePOJO;
import com.udacity.popularmovies.model.TrailerVideoPOJO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @BindView(R.id.fab)FloatingActionButton favMovie;
    @BindView(R.id.trailer_horizontal_scroll_recycler_view)RecyclerView trailerContainer;
    @BindView(R.id.tariler_list_progress_bar)ProgressBar trailerListLoading;
    TrailerRecyclerViewAdapter trailerRecyclerViewAdapter;
    Uri CONTENT_URI= null;

    PopularMoviePOJO popularMoviePOJO;
    ArrayList<TrailerVideoPOJO> trailerVideoPOJOArrayList;

    Controller controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle=getIntent()!=null?getIntent().getExtras():null;
        trailerVideoPOJOArrayList=new ArrayList<>();
        if(bundle!=null)
        {
            popularMoviePOJO=bundle.getParcelable(getString(R.string.key_movie_detail));
        }
        else if(savedInstanceState!=null && savedInstanceState.containsKey(getString(R.string.key_movie_detail)))
        {
            popularMoviePOJO=savedInstanceState.getParcelable(getString(R.string.key_movie_detail));
            trailerVideoPOJOArrayList=savedInstanceState.getParcelableArrayList(getString(R.string.key_movie_trailers));
        }
        CONTENT_URI=MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(popularMoviePOJO.getId()))
                .build();

        Cursor cursor=null;
        ContentResolver contentResolver=this.getContentResolver();
        if(popularMoviePOJO!=null)

            //Check if the a record with the same id exists
            cursor=contentResolver.query(CONTENT_URI,null,null,null,null);
        if(cursor!=null && cursor.moveToNext())
        {
            setMovieFavImage(true);
        }
        else
        {
            setMovieFavImage(false);
        }
        favMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favButtonClickHandler();
            }
        });
        setValues();
        trailerRecyclerViewAdapter=new TrailerRecyclerViewAdapter(this,trailerVideoPOJOArrayList);
        trailerContainer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        trailerContainer.setAdapter(trailerRecyclerViewAdapter);

        controller=Controller.getInstance();
        if(trailerVideoPOJOArrayList==null || trailerVideoPOJOArrayList.isEmpty()) {
            trailerListLoading.setVisibility(View.VISIBLE);
            controller.getMovieTrailerList(BuildConfig.API_KEY, String.valueOf(popularMoviePOJO.getId()), new VideoListResponseListener());
        }
        else
        {
            trailerListLoading.setVisibility(View.GONE);
        }

    }

    private void setMovieFavImage(boolean isFav)
    {
        if(isFav)
            favMovie.setImageResource(R.drawable.ic_favorite);
        else
            favMovie.setImageResource(R.drawable.ic_favorite_border);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.key_movie_detail),popularMoviePOJO);
        outState.putParcelableArrayList(getString(R.string.key_movie_trailers),trailerVideoPOJOArrayList);
    }

    private void setValues()
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat(getString(R.string.dateformat_yyyy_MM_dd));
        SimpleDateFormat formatTo=new SimpleDateFormat(getString(R.string.dateformat_dd_MMMM_yyyy));
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

    private void favButtonClickHandler()
    {
        Cursor cursor=null;
        ContentValues contentValues=new ContentValues();
        ContentResolver contentResolver=this.getContentResolver();
        if(popularMoviePOJO!=null)

        //Check if the a record with the same id exists
            cursor=contentResolver.query(CONTENT_URI,null,null,null,null);
        if(cursor!=null && cursor.moveToNext())
        {
            //delete the entry
            int id=contentResolver.delete(CONTENT_URI,null,null);
            if(id!=-1)
            {
                setMovieFavImage(false);
            }
        }
        else {
            //else insert the record
            contentValues.put(MovieContract.MovieEntry._ID,popularMoviePOJO.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,popularMoviePOJO.getTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH,popularMoviePOJO.getPoster_path());
            contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT,popularMoviePOJO.isAdult()?"1":"0");
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,popularMoviePOJO.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,popularMoviePOJO.getRelease_date());
            String genreIdString="";
            for(int genreId:popularMoviePOJO.getGenre_ids())
            {
                genreIdString=genreIdString+genreId+",";
            }
            genreIdString=genreIdString.substring(0,genreIdString.lastIndexOf(","));
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENRE_IDS,genreIdString);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE,popularMoviePOJO.getTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE,popularMoviePOJO.getOriginal_language());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,popularMoviePOJO.getBackdrop_path());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY,popularMoviePOJO.getPopularity());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VIDEO,popularMoviePOJO.isVideo()?"1":"0");
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,popularMoviePOJO.getVote_average());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT,popularMoviePOJO.getVote_count());
            Uri uri=contentResolver.insert(CONTENT_URI,contentValues);
            if(uri!=null)
            {
                setMovieFavImage(true);
            }
        }
    }

    private class VideoListResponseListener implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            JSONObject jsonObject=Controller.getJsonResponseFromRaw(response);
            if(jsonObject!=null)
            {
                JSONArray jsonArray=jsonObject.optJSONArray("results");
                if(jsonArray!=null)
                {
                    Gson gson=new Gson();
                    trailerVideoPOJOArrayList=new ArrayList<>();
                    ArrayList<TrailerVideoPOJO> trailerVideoPOJOArrayListNew=gson.fromJson(jsonArray.toString(),new TypeToken<List<TrailerVideoPOJO>>(){}.getType());
                    for(TrailerVideoPOJO trailerVideoPOJO:trailerVideoPOJOArrayListNew)
                    {
                        if(trailerVideoPOJO.getType().equals("Trailer"))
                        trailerVideoPOJOArrayList.add(trailerVideoPOJO);
                    }
                    trailerRecyclerViewAdapter.setTrailerList(trailerVideoPOJOArrayList);
                    trailerRecyclerViewAdapter.notifyDataSetChanged();
                    trailerListLoading.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }
}
