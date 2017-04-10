/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * MoviesTable
 *
 * Specifies all of the Movies that a user has saved as their favorites.  Movie entries that have
 * been stored within the table will be accessible even if the user does not have a valid api key
 * or if they don't have network access.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public final class MoviesTable extends BaseTable {
    public static final String NAME = "movies";

    /**
     * Specifies the tables columns.
     *
     */
    public static final class Cols {
        public static final String POSTER_PATH = "poster_path";
        public static final String IS_ADULT_FILM = "is_adult_film";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String MOVIE_ID = "movie_id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String TITLE = "title";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String POPULARITY = "popularity";
        public static final String VOTE_COUNT = "vote_count";
        public static final String HAS_VIDEO = "has_video";
        public static final String VOTE_AVERAGE = "vote_average";
    }

    /**
     * Creates the database table.
     *
     * @param db
     */
    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NAME + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cols.POSTER_PATH + ", " +
                Cols.IS_ADULT_FILM + ", " +
                Cols.OVERVIEW + ", " +
                Cols.RELEASE_DATE + ", " +
                Cols.MOVIE_ID + ", " +
                Cols.ORIGINAL_TITLE + ", " +
                Cols.TITLE + ", " +
                Cols.BACKDROP_PATH + ", " +
                Cols.POPULARITY + ", " +
                Cols.VOTE_COUNT + ", " +
                Cols.HAS_VIDEO + ", " +
                Cols.VOTE_AVERAGE +
                ")"
        );
    }

    /**
     * Constructs a Table model.
     *
     * @param mContext
     */
    public MoviesTable(Context mContext) {
        super(mContext);

        setTableName(NAME);
    }

    /**
     * Constructs a Table model.
     *
     * @param mContext
     * @param database
     */
    public MoviesTable(Context mContext, SQLiteDatabase database) {
        super(mContext, database);

        setTableName(NAME);
    }

    /**
     * Constructs a Table model.
     *
     * @param mContext
     * @param readOnly
     */
    public MoviesTable(Context mContext, boolean readOnly) {
        super(mContext, readOnly);

        setTableName(NAME);
    }

    /**
     * Maps a movie model to the fields in the database.
     *
     * @param object
     *
     * @return mapping content values.
     */
    @Override
    protected ContentValues getContentValues(Object object) {
        Movie movie = (Movie) object;

        ContentValues cv = new ContentValues();
        cv.put(Cols.POSTER_PATH, movie.posterPath);
        cv.put(Cols.IS_ADULT_FILM, movie.isAdultFilm);
        cv.put(Cols.OVERVIEW, movie.overview);
        cv.put(Cols.RELEASE_DATE, movie.releaseDate);
        cv.put(Cols.MOVIE_ID, Integer.toString(movie.id));
        cv.put(Cols.ORIGINAL_TITLE, movie.originalTitle);
        cv.put(Cols.TITLE, movie.title);
        cv.put(Cols.BACKDROP_PATH, movie.backdropPath);
        cv.put(Cols.POPULARITY, movie.popularity);
        cv.put(Cols.VOTE_COUNT, movie.voteCount);
        cv.put(Cols.HAS_VIDEO, movie.hasVideo);
        cv.put(Cols.VOTE_AVERAGE, movie.voteAverage);

        return cv;
    }

    /**
     * Determines if a movie exist in the database.
     *
     * @param data
     *
     * @return `true` if the movie exist in the database and `false` otherwise.
     */
    @Override
    public boolean contains(Object data) {
        Movie movie = (Movie) data;
        String whereClause = Cols.MOVIE_ID + " = ?";
        String[] whereArgs = new String[] {
                Integer.toString(movie.id)
        };

        return doesExistsInDatabase(whereClause, whereArgs);
    }

    /**
     * Removes a movie from the database.
     *
     * @param data
     *
     * @return Number of rows removed
     */
    @Override
    public int delete(Object data) {
        Movie movie = (Movie) data;
        String whereClause = Cols.MOVIE_ID + " = ?";
        String[] whereArgs = new String[] {
            Integer.toString(movie.id)
        };

        return deleteFromDatabase(whereClause, whereArgs);
    }

    /**
     * Retrieves the id of the record for the data field, if one exists.
     *
     * @param data
     *
     * @return Id of the record if one exists otherwise -1.
     */
    public long getId(Object data) {
        Movie movie = (Movie) data;
        String whereClause = Cols.MOVIE_ID + " = ?";
        String[] whereArgs = new String[] {
            Integer.toString(movie.id)
        };

        long movieId = -1;
        Cursor cursor = connection.query(NAME, null, whereClause, whereArgs, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            movieId = cursor.getLong(cursor.getColumnIndex("_id"));
        }
        cursor.close();

        return movieId;
    }
}