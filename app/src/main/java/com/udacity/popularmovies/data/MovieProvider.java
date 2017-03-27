/*
 * Copyright (C) 2015 The Android Open Source Project
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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.udacity.popularmovies.data.MovieContract.MovieEntry;


public class MovieProvider extends ContentProvider {


    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {


        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);


        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return 0;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;


        switch (sUriMatcher.match(uri)) {


            case CODE_MOVIE_WITH_ID: {


                String movieId = uri.getLastPathSegment();


                String[] selectionArguments = new String[]{movieId};

                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        MovieEntry.TABLE_NAME,

                        projection,

                        MovieEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }


            case CODE_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;



        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE_WITH_ID:
                String movieId = uri.getLastPathSegment();


                String[] selectionArguments = new String[]{movieId};
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieEntry.TABLE_NAME,
                        MovieEntry._ID + " = ? ",
                        selectionArguments);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in the app.");
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (buildUriMatcher().match(uri))
        {
            case CODE_MOVIE_WITH_ID:


                    long _id=db.insert(MovieEntry.TABLE_NAME,null,values);
                    if (_id > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        return uri;
                    }


        }

        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in the app");
    }


    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}