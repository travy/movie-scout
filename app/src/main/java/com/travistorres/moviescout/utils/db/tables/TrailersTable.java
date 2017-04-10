/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travistorres.moviescout.utils.moviedb.models.Movie;
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

    /**
     * Constructs a Table model.
     *
     * @param mContext
     */
    public TrailersTable(Context mContext) {
        super(mContext);

        setTableName(NAME);
    }

    /**
     * Constructs a Table model.
     *
     * @param mContext
     * @param database
     */
    public TrailersTable(Context mContext, SQLiteDatabase database) {
        super(mContext, database);

        setTableName(NAME);
    }

    /**
     * Constructs a Table model
     *
     * @param mContext
     * @param readOnly
     */
    public TrailersTable(Context mContext, boolean readOnly) {
        super(mContext, readOnly);

        setTableName(NAME);
    }

    /**
     * Specifies how to convert the model to the database.
     *
     * @param data
     *
     * @return content values for storing into the database.
     */
    @Override
    protected ContentValues getContentValues(Object data) {
        Trailer trailer = (Trailer) data;

        ContentValues cv = new ContentValues();
        cv.put(Cols.KEY, trailer.key);
        cv.put(Cols.ISO_639_1, trailer.iso_639_1);
        cv.put(Cols.ISO_3166_1, trailer.iso_3166_1);
        cv.put(Cols.MOVIE_ID, trailer.movieId);
        cv.put(Cols.NAME, trailer.name);
        cv.put(Cols.SITE, trailer.site);
        cv.put(Cols.SIZE, trailer.size);
        cv.put(Cols.TYPE, trailer.type);

        return cv;
    }

    /**
     * Determines if the database has a trailer.
     *
     * @param data
     *
     * @return `true` if the trailer is in the database and false otherwise
     */
    @Override
    public boolean contains(Object data) {
        Trailer trailer = (Trailer) data;
        String whereClause = Cols.KEY + " = ?";
        String[] whereArgs = new String[] {
            trailer.key
        };

        return doesExistsInDatabase(whereClause, whereArgs);
    }

    /**
     * Removes a specific field from the database.
     *
     * @param data
     *
     * @return Number of rows deleted.
     */
    @Override
    public int delete(Object data) {
        Trailer trailer = (Trailer) data;
        String whereClause = Cols.KEY + " = ?";
        String[] whereArgs = new String[] {
            trailer.key
        };

        return deleteFromDatabase(whereClause, whereArgs);
    }

    /**
     * Deletes all trailers associated to a specified movie.
     *
     * @param movieId
     *
     * @return Number of rows deleted
     */
    public int deleteAssociatedToMovie(long movieId) {
        String whereClause = Cols.MOVIE_ID + " = ?";
        String[] whereArgs = new String[] {
            Long.toString(movieId)
        };

        return deleteFromDatabase(whereClause, whereArgs);
    }

    /**
     * Specifies the tables columns.
     *
     */
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

    /**
     * Creates a new table.
     *
     * @param db
     */
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
