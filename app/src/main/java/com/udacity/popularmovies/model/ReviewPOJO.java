package com.udacity.popularmovies.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by debjyotinath on 27/03/17.
 */

public class ReviewPOJO implements Parcelable{

    String id;
    String author;
    String content;
    String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putString("author",author);
        bundle.putString("content",content);
        bundle.putString("url",url);
        dest.writeBundle(bundle);
    }
    private void readFromParcel(Parcel in)
    {
        Bundle bundle=in.readBundle();
        if(bundle!=null)
        {
            id=bundle.getString("id");
            author=bundle.getString("author");
            content=bundle.getString("content");
            url=bundle.getString("url");
        }
    }
    public ReviewPOJO(Parcel in)
    {
        readFromParcel(in);
    }

    public static final Creator<ReviewPOJO> CREATOR=new Creator<ReviewPOJO>() {
        @Override
        public ReviewPOJO createFromParcel(Parcel source) {
            return new ReviewPOJO(source);
        }

        @Override
        public ReviewPOJO[] newArray(int size) {
            return new ReviewPOJO[0];
        }
    };
}
