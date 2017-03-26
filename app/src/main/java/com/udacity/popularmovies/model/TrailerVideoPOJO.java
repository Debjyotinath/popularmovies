package com.udacity.popularmovies.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by debjyotinath on 27/03/17.
 */

public class TrailerVideoPOJO implements Parcelable{
    private String id;
    private String key;
    private String title;
    private String name;
    private String size;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putString("key",key);
        bundle.putString("title",title);
        bundle.putString("name",name);
        bundle.putString("size",size);
        bundle.putString("type",type);
        dest.writeBundle(bundle);
    }

    public TrailerVideoPOJO(Parcel in)
    {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in)
    {
        Bundle bundle=in.readBundle();
        if(bundle!=null)
        {
            id=bundle.getString("id");
            key=bundle.getString("key");
            title=bundle.getString("title");
            name=bundle.getString("name");
            size=bundle.getString("size");
            type=bundle.getString("type");
        }
    }

    public static final Creator<TrailerVideoPOJO> CREATOR=new Creator<TrailerVideoPOJO>() {
        @Override
        public TrailerVideoPOJO createFromParcel(Parcel source) {
            return new TrailerVideoPOJO(source);
        }

        @Override
        public TrailerVideoPOJO[] newArray(int size) {
            return new TrailerVideoPOJO[0];
        }
    };
}
