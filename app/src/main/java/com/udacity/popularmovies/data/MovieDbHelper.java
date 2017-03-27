/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.popularmovies.data.MovieContract.MovieEntry;


public class MovieDbHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "movie.db";


    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

 String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +


                MovieEntry._ID               + " INTEGER PRIMARY KEY , " +
                MovieEntry.COLUMN_MOVIE_TITLE       + " TEXT NOT NULL, "                 +

                MovieEntry.COLUMN_MOVIE_IMAGE_PATH + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL,"                  +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_GENRE_IDS + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_POPULARITY + " REAL NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_VIDEO + " INTEGER NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " INTEGER NOT NULL,"                  +


                /*
                 * To ensure this table can only contain one weather entry per date, we declare
                 * the _id column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                 * SQLite that if we have a movie entry for a certain _id and we attempt to
                 * insert another movie entry with that _id, we replace the old movie entry.
                 */
                " UNIQUE (" + MovieEntry._ID + ") ON CONFLICT REPLACE);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}