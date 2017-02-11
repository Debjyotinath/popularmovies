package com.udacity.popularmovies.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.popularmovies.controller.services.MoviesService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by debjyotinath on 10/02/17.
 */

public class Controller {
    private static final Controller ourInstance = new Controller();
    private static final String SERVER_URL="http://api.themoviedb.org";
    public static final String TOPRATED_URL="3/movie/top_rated";
    public static final String MOST_POPULAR_URL="3/movie/popular";
    public static final String IMAGE_URL="http://image.tmdb.org/t/p/w780/";


    private static final Retrofit retrofit=buildRetrofit();
    public static Controller getInstance() {
        return ourInstance;
    }

    private Controller() {
    }

    public static JSONObject getJsonResponseFromRaw(Response<ResponseBody> response)
    {

        try {
            return new JSONObject(getStringResponseFromRaw(response));
        }catch(Exception ex)
        {
            return null;
        }
    }

    public static String getStringResponseFromRaw(Response<ResponseBody> response)
    {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public void getMovies(String key,boolean topRated, Callback<ResponseBody> bodyCallback)
    {
        MoviesService service = retrofit.create(MoviesService.class);
        Call<ResponseBody> responseCall=null;
        HashMap<String,String> parameterHashMap=new HashMap<>();
        parameterHashMap.put("api_key",key);
        if(topRated)
            responseCall=service.getTopRatedMovies(parameterHashMap);
        else
            responseCall=service.getPopularMovies(parameterHashMap);
        responseCall.enqueue(bodyCallback);
    }
    public static Retrofit buildRetrofit()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }
}
