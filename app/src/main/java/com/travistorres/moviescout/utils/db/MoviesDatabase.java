/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.travistorres.moviescout.utils.db.tables.MoviesTable;
import com.travistorres.moviescout.utils.db.tables.ReviewsTable;
import com.travistorres.moviescout.utils.db.tables.TrailersTable;

/**
 * MoviesDatabase
 *
 * Informs the application how to construct and upgrade various versions of the applications
 * database.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public class MoviesDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "moviescout.db";

    public static final String AUTHORITY = "com.travistorres.moviescout";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Constructs a new database model.
     *
     * @param context
     */
    public MoviesDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Specifies how to build the applications database.
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        MoviesTable.createTable(sqLiteDatabase);
        ReviewsTable.createTable(sqLiteDatabase);
        TrailersTable.createTable(sqLiteDatabase);
    }

    /**
     * Specifies how to update the database from the previous version.
     *
     * @param sqLiteDatabase
     * @param previous The previous version code
     * @param latest The newest version code
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int previous, int latest) {
        //  not implemented as this is an initial release
    }
}
