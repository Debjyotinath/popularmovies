package com.udacity.popularmovies.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.popularmovies.BuildConfig;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.adapter.RecyclerViewAdapter;
import com.udacity.popularmovies.controller.Controller;
import com.udacity.popularmovies.model.PopularMoviePOJO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)RecyclerView recyclerView;

    RecyclerViewAdapter viewAdapter;
    ArrayList<PopularMoviePOJO> moviePOJOs;

    public static final String API_KEY= BuildConfig.API_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        if(savedInstanceState!=null && savedInstanceState.containsKey(getString(R.string.key_movie_list)))
            moviePOJOs=savedInstanceState.getParcelableArrayList(getString(R.string.key_movie_list));
        if(moviePOJOs==null)
            moviePOJOs=new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this,calculateNoOfColumns(getBaseContext())));
        recyclerView.setHasFixedSize(true);
        viewAdapter=new RecyclerViewAdapter(MovieListActivity.this,moviePOJOs);
        recyclerView.setAdapter(viewAdapter);
        if(moviePOJOs.isEmpty())
            getMovies(false);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.key_movie_list),moviePOJOs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sorting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sort_by_popular:
                getMovies(false);
                break;
            case R.id.sort_by_rating:
                getMovies(true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMovies(boolean topRated)
    {
        Controller controller=Controller.getInstance();
        controller.getMovies(API_KEY,topRated,new MovieResponse());
    }

    class MovieResponse implements Callback<ResponseBody>
    {

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            JSONObject jsonObject=Controller.getJsonResponseFromRaw(response);
            if(jsonObject!=null)
            {
                JSONArray jsonArray=jsonObject.optJSONArray("results");
                if(jsonArray!=null)
                {
                    Gson gson=new Gson();
                    moviePOJOs=gson.fromJson(jsonArray.toString(),new TypeToken<List<PopularMoviePOJO>>(){}.getType());
                    viewAdapter.setMovieList(moviePOJOs);
                    viewAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
