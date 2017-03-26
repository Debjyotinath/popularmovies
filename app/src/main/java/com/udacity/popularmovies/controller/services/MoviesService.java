package com.udacity.popularmovies.controller.services;

import com.udacity.popularmovies.controller.Controller;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by debjyotinath on 10/02/17.
 */

public interface MoviesService {

    @GET(Controller.TOPRATED_URL)
    Call<ResponseBody> getTopRatedMovies(@QueryMap HashMap<String,String> map);

    @GET(Controller.MOST_POPULAR_URL)
    Call<ResponseBody> getPopularMovies(@QueryMap HashMap<String,String> map);

    @GET(Controller.VIDEO_LIST_URL)
    Call<ResponseBody> getVideoList(@Path("id")String movieId,@QueryMap HashMap<String,String> map);

    @GET(Controller.REVIEW_LIST_URL)
    Call<ResponseBody> getReviewList(@Path("id")String movieId,@QueryMap HashMap<String,String> map);
}
