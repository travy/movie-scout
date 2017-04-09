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
import com.travistorres.moviescout.utils.moviedb.models.Review;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;

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
    public void addFavorite(Movie movie, Review[] reviews, Trailer[] trailers) {
        if (!isFavorite(movie)) {
            long id = movieTable.save(movie);
            saveReviewsWithMovieId(reviews, id);
            saveTrailersWithMovieId(trailers, id);
        }
    }

    /**
     * Saves a set of Trailer which is associated to a movie.
     *
     * @param trailers
     * @param movieId
     */
    public void saveTrailersWithMovieId(Trailer[] trailers, long movieId) {
        if (trailers != null) {
            for (int i = 0; i < trailers.length; ++i) {
                saveTrailerWithMovieId(trailers[i], movieId);
            }
        }
    }

    /**
     * Adds a new Trailer to the database which is associated to a specific Movie.
     *
     * @param trailer
     * @param movieId
     *
     * @return The id of the row in the database.
     */
    public long saveTrailerWithMovieId(Trailer trailer, long movieId) {
        trailer.movieId = Long.toString(movieId);
        return trailersTable.save(trailer);
    }

    /**
     * Saves a set of reviews into the database with the same movie item as its association.
     *
     * @param reviews
     * @param movieId
     */
    public void saveReviewsWithMovieId(Review[] reviews, long movieId) {
        if (reviews != null) {
            for (int i = 0; i < reviews.length; ++i) {
                saveReviewWithMovieId(reviews[i], movieId);
            }
        }
    }

    /**
     * Inserts a new review record into the database with a specified movie as its association.
     *
     * @param review
     * @param movieId
     *
     * @return The id of the review in the database.
     */
    public long saveReviewWithMovieId(Review review, long movieId) {
        review.movieId = Long.toString(movieId);
        return reviewsTable.save(review);
    }

    /**
     * Removes a Move from the users favorites.
     *
     * @param movie
     */
    public void removeFavorite(Movie movie) {
        if (isFavorite(movie)) {
            long movieId = movieTable.getId(movie);

            reviewsTable.deleteAssociatedToMovie(movieId);
            trailersTable.deleteAssociatedToMovie(movieId);
            movieTable.delete(movie);
        }
    }
}
