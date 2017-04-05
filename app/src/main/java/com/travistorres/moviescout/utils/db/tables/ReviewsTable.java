/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * ReviewsTable
 *
 * Describes the contents of the Reviews database table.  Each entry in the table will have an
 * association with a given Movie in the Movies table.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public final class ReviewsTable {
    public static final String NAME = "reviews";

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
