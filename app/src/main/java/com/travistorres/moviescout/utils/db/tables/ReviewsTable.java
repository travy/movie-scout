/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.travistorres.moviescout.utils.moviedb.models.Review;

/**
 * ReviewsTable
 *
 * Describes the contents of the Reviews database table.  Each entry in the table will have an
 * association with a given Movie in the Movies table.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public final class ReviewsTable extends BaseTable {
    public static final String NAME = "reviews";

    public ReviewsTable(Context mContext) {
        super(mContext);

        setTableName(NAME);
    }

    public ReviewsTable(Context mContext, SQLiteDatabase database) {
        super(mContext, database);

        setTableName(NAME);
    }

    public ReviewsTable(Context mContext, boolean readOnly) {
        super(mContext, readOnly);

        setTableName(NAME);
    }

    @Override
    protected ContentValues getContentValues(Object data) {
        Review review = (Review) data;

        ContentValues cv = new ContentValues();
        cv.put(Cols.MOVIE_ID, review.movieId);
        cv.put(Cols.ID, review.id);
        cv.put(Cols.AUTHOR, review.author);
        cv.put(Cols.CONTENT, review.content);
        cv.put(Cols.REVIEW_URL, review.url);;

        return cv;
    }

    public static final class Cols {
        public static final String MOVIE_ID = "movie_id";   //  identifiers id of movie in apps database
        public static final String ID = "id";
        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String REVIEW_URL = "review_url";   //  change name from moviedb as to not be confused with the class name
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cols.MOVIE_ID + ", " +
                Cols.ID + ", " +
                Cols.AUTHOR + ", " +
                Cols.CONTENT + ", " +
                Cols.REVIEW_URL +
                ")"
        );
    }
}
