/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.travistorres.moviescout.utils.moviedb.models.Trailer;

/**
 * TrailersTable
 *
 * Describes each of the Movie trailers that have been associated with a Movie entry in the
 * Movies database table.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public final class TrailersTable extends BaseTable {
    public static final String NAME = "trailers";

    public TrailersTable(Context mContext) {
        super(mContext);

        setTableName(NAME);
    }

    public TrailersTable(Context mContext, SQLiteDatabase database) {
        super(mContext, database);

        setTableName(NAME);
    }

    public TrailersTable(Context mContext, boolean readOnly) {
        super(mContext, readOnly);

        setTableName(NAME);
    }

    @Override
    protected ContentValues getContentValues(Object data) {
        Trailer trailer = (Trailer) data;

        ContentValues cv = new ContentValues();
        cv.put(Cols.KEY, trailer.key);
        cv.put(Cols.ISO_639_1, trailer.iso_639_1);
        cv.put(Cols.ISO_3166_1, trailer.iso_3166_1);
        //cv.put(Cols.MOVIE_ID, trailer.movie_id);
        cv.put(Cols.NAME, trailer.name);
        cv.put(Cols.SITE, trailer.site);
        cv.put(Cols.SIZE, trailer.size);
        cv.put(Cols.TYPE, trailer.type);

        return cv;
    }

    public static final class Cols {
        public static final String MOVIE_ID = "movie_id";   //  id within the movie-scout database
        public static final String ISO_639_1 = "iso_639_1";
        public static final String ISO_3166_1 = "iso_3166_1";
        public static final String KEY = "key";
        public static final String NAME = "name";
        public static final String SITE = "site";
        public static final String SIZE = "size";
        public static final String TYPE = "type";
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cols.MOVIE_ID + ", " +
                Cols.ISO_639_1 + ", " +
                Cols.ISO_3166_1 + ", " +
                Cols.KEY + ", " +
                Cols.NAME + ", " +
                Cols.SITE + ", " +
                Cols.SIZE + ", " +
                Cols.TYPE + "" +
                ")"
        );
    }
}
