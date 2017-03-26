package com.udacity.popularmovies.activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.udacity.popularmovies.data.MovieContract.MovieEntry;
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

public class MovieListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.recycler_view)RecyclerView recyclerView;

    RecyclerViewAdapter viewAdapter;
    ArrayList<PopularMoviePOJO> moviePOJOs;

    public static final String API_KEY= BuildConfig.API_KEY;
    private static final int MOVIE_LOADER = 44;
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
            case R.id.sort_by_fav:
                getFavMovies();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMovies(boolean topRated)
    {
        getSupportLoaderManager().destroyLoader(MOVIE_LOADER);
        Controller controller=Controller.getInstance();
        controller.getMovies(API_KEY,topRated,new MovieResponse());
    }

    private void getFavMovies()
    {
/*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case MOVIE_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri movieQueryUri = MovieEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = MovieEntry._ID + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
//                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this,
                        movieQueryUri,
                        null,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        moviePOJOs=new ArrayList<>();
        while(data.moveToNext())
        {
            Long id=data.getLong(data.getColumnIndex(MovieEntry._ID));
            String title=data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_TITLE));
            String imagePath=data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_IMAGE_PATH));
            PopularMoviePOJO moviePOJO=new PopularMoviePOJO();
            moviePOJO.setId(id);
            moviePOJO.setTitle(title);
            moviePOJO.setPoster_path(imagePath);
            moviePOJO.setAdult(data.getInt(data.getColumnIndex(MovieEntry.COLUMN_ADULT))==1?true:false);
            moviePOJO.setVideo(data.getInt(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_VIDEO))==1?true:false);
            moviePOJO.setBackdrop_path(data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_BACKDROP_PATH)));
            moviePOJO.setOriginal_language(data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE)));
            moviePOJO.setOriginal_title(data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE)));
            moviePOJO.setOverview(data.getString(data.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)));
            moviePOJO.setPopularity(data.getDouble(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_POPULARITY)));
            moviePOJO.setRelease_date(data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
            moviePOJO.setVote_average(data.getDouble(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)));
            moviePOJO.setVote_count(data.getInt(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_VOTE_COUNT)));
            String[] s=data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_GENRE_IDS)).split(",");
            int[] result = new int[s.length];
            for (int i = 0; i < s.length; i++) {
                result[i] = Integer.parseInt(s[i]);
            }
            moviePOJO.setGenre_ids(result);
            moviePOJOs.add(moviePOJO);

        }
        viewAdapter.setMovieList(moviePOJOs);
        viewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
