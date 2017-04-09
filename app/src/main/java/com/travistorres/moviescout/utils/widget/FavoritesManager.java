/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.travistorres.moviescout.utils.db.MoviesDatabase;
import com.travistorres.moviescout.utils.db.tables.MoviesTable;
import com.travistorres.moviescout.utils.db.tables.ReviewsTable;
import com.travistorres.moviescout.utils.db.tables.TrailersTable;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * FavoritesManager
 *
 * Responsible for dealing with all operations that have to do with managing a users favorites.
 * All operations on favorites should be handled through this utility to allow for easy refactoring
 * in case the database scheme ever changes in a drastric way.
 *
 * @author Travis Anthony Torres
 * @version April 8, 2017
 */

public class FavoritesManager {
    private Context context;
    private SQLiteDatabase connection;

    private MoviesTable movieTable;
    private ReviewsTable reviewsTable;
    private TrailersTable trailersTable;

    /**
     * Constructs a new FavoritesManager interface which will share a common connection to ensure
     * valid data storage at all time using Database ACID properties.
     *
     * @param mContext
     */
    public FavoritesManager(Context mContext) {
        context = mContext;
        connection = new MoviesDatabase(context).getWritableDatabase();

        movieTable = new MoviesTable(context, connection);
        reviewsTable = new ReviewsTable(context, connection);
        trailersTable = new TrailersTable(context, connection);
    }

    /**
     * Checks if a given Movie has been added to the favorites.
     *
     * @param movie
     *
     * @return
     */
    public boolean isFavorite(Movie movie) {
        return movieTable.contains(movie);
    }

    /**
     * Inserts a Movie into the users favorites.
     *
     * @param movie
     */
    public void addFavorite(Movie movie) {
        if (!isFavorite(movie)) {
            movieTable.save(movie);
        }
    }

    /**
     * Removes a Move from the users favorites.
     *
     * @param movie
     */
    public void removeFavorite(Movie movie) {
        if (isFavorite(movie)) {
            movieTable.delete(movie);
        }
    }
}
