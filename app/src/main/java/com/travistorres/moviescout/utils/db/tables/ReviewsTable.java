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

    /**
     * Constructs a Review table model.
     *
     * @param mContext
     */
    public ReviewsTable(Context mContext) {
        super(mContext);

        setTableName(NAME);
    }

    /**
     * Constructs a Review table model.
     *
     * @param mContext
     * @param database
     */
    public ReviewsTable(Context mContext, SQLiteDatabase database) {
        super(mContext, database);

        setTableName(NAME);
    }

    /**
     * Constructs a Review table model.
     *
     * @param mContext
     * @param readOnly
     */
    public ReviewsTable(Context mContext, boolean readOnly) {
        super(mContext, readOnly);

        setTableName(NAME);
    }

    /**
     * Maps a Review model to the table field.
     *
     * @param data
     *
     * @return content values
     */
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

    /**
     * Determines if the database contains the specified review.
     *
     * @param data
     *
     * @return `true` if the database contains the review and false otherwise.
     */
    @Override
    public boolean contains(Object data) {
        Review review = (Review) data;
        String whereClause = Cols.ID + " = ?";
        String[] whereArgs = new String[] {
            review.id
        };

        return doesExistsInDatabase(whereClause, whereArgs);
    }

    /**
     * Deletes a review from the database.
     *
     * @param data
     *
     * @return Number of rows removed.
     */
    @Override
    public int delete(Object data) {
        Review review = (Review) data;
        String whereClause = Cols.ID + " = ?";
        String[] whereArgs = new String[] {
            review.id
        };

        return deleteFromDatabase(whereClause, whereArgs);
    }

    /**
     * Removes all reviews for a specified movie.
     *
     * @param movieId
     *
     * @return Number of Reviews deleted
     */
    public int deleteAssociatedToMovie(long movieId) {
        String whereClause = Cols.MOVIE_ID + " = ?";
        String[] whereArgs = new String[] {
            Long.toString(movieId)
        };

        return deleteFromDatabase(whereClause, whereArgs);
    }

    /**
     * Specifies all table columns.
     *
     */
    public static final class Cols {
        public static final String MOVIE_ID = "movie_id";   //  identifiers id of movie in apps database
        public static final String ID = "id";
        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String REVIEW_URL = "review_url";   //  change name from moviedb as to not be confused with the class name
    }

    /**
     * Creates the database table.
     *
     * @param db
     */
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
