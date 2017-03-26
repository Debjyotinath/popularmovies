package com.udacity.popularmovies.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by debjyotinath on 10/02/17.
 */

public class PopularMoviePOJO implements Parcelable{

    long id;
    String poster_path;
    boolean adult;
    String overview;
    String release_date;
    int[] genre_ids;
    String original_title;
    String original_language;
    String title;
    String backdrop_path;

    double popularity;
    int vote_count;
    boolean video;
    double vote_average;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public PopularMoviePOJO(Parcel in) {
        readFromParcel(in);
    }
    public PopularMoviePOJO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in){
        Bundle bundle=in.readBundle();
        if(bundle!=null)
        {
            id=bundle.containsKey("id")?bundle.getLong("id",0):0L;
            poster_path=bundle.containsKey("poster_path")?bundle.getString("poster_path"):"";
            overview=bundle.containsKey("overview")?bundle.getString("overview"):"";
            release_date=bundle.containsKey("release_date")?bundle.getString("release_date"):"";
            original_title=bundle.containsKey("original_title")?bundle.getString("original_title"):"";
            original_language=bundle.containsKey("original_language")?bundle.getString("original_language"):"";
            title=bundle.containsKey("title")?bundle.getString("title"):"";
            backdrop_path=bundle.containsKey("backdrop_path")?bundle.getString("backdrop_path"):"";
            adult=bundle.containsKey("adult")?bundle.getBoolean("adult"):false;
            video=bundle.containsKey("video")?bundle.getBoolean("video"):false;
            genre_ids=bundle.containsKey("genre_ids")?bundle.getIntArray("genre_ids"):null;
            vote_count=bundle.containsKey("vote_count")?bundle.getInt("vote_count"):0;
            popularity=bundle.containsKey("popularity")?bundle.getDouble("popularity"):0.0;
            vote_average=bundle.containsKey("vote_average")?bundle.getDouble("vote_average"):0.0;

        }
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle=new Bundle();
        bundle.putLong("id",id);

        bundle.putString("poster_path",poster_path);
        bundle.putString("overview",overview);
        bundle.putString("release_date",release_date);
        bundle.putString("original_title",original_title);
        bundle.putString("original_language",original_language);
        bundle.putString("title",title);
        bundle.putString("backdrop_path",backdrop_path);

        bundle.putBoolean("adult",adult);
        bundle.putBoolean("video",video);

        bundle.putIntArray("genre_ids",genre_ids);
        bundle.putInt("vote_count",vote_count);

        bundle.putDouble("popularity",popularity);

        bundle.putDouble("vote_average",vote_average);

        parcel.writeBundle(bundle);
    }

    public static final Creator CREATOR=new Creator() {
        @Override
        public PopularMoviePOJO createFromParcel(Parcel parcel) {
            return new PopularMoviePOJO(parcel);
        }

        @Override
        public PopularMoviePOJO[] newArray(int size) {
            return new PopularMoviePOJO[size];
        }
    };
}
