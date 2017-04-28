/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.db.MoviesDatabase;
import com.travistorres.moviescout.utils.db.tables.MoviesTable;
import com.travistorres.moviescout.utils.db.tables.ReviewsTable;
import com.travistorres.moviescout.utils.db.tables.TrailersTable;
import com.travistorres.moviescout.utils.moviedb.builders.MovieDbParser;
import com.travistorres.moviescout.utils.moviedb.models.Movie;
import com.travistorres.moviescout.utils.moviedb.models.Review;
import com.travistorres.moviescout.utils.moviedb.models.Trailer;
import com.travistorres.moviescout.utils.networking.NetworkManager;
import com.travistorres.moviescout.utils.networking.UrlManager;
import com.travistorres.moviescout.utils.networking.exceptions.NetworkingException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

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
    private MoviesTable movieTable;
    private ReviewsTable reviewsTable;
    private SQLiteDatabase connection;
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

    /**
     * Acquires all of the users favorites.
     *
     * @return array of movies or null.
     */
    public Movie[] getFavorites() {
        return movieTable.getAll();
    }

    /**
     * Will request updated information on all of the users favorite movies.
     *
     * @return true if there was at least one movie that was updated and false otherwise.
     */
    public boolean updateMovies() {
        boolean didUpdateOccur = false;
        Movie[] favorites = getFavorites();
        for (Movie favorite : favorites) {
            Movie updatedMovie = getLatestMovieInfo(favorite);
            if (updatedMovie != null && !favorite.equals(updatedMovie)) {
                //  Should update the field in the database
                updateMovie(favorite.dbId, updatedMovie);
                didUpdateOccur = true;
            }
        }

        return didUpdateOccur;
    }

    /**
     * Request the latest movie from the server.
     *
     * @param movie
     *
     * @return Latest copy of the movie
     */
    private Movie getLatestMovieInfo(Movie movie) {
        Movie latestCopy = null;

        try {
            String apiKey = context.getString(R.string.movie_scout_version_three_api_key);
            UrlManager urlManager = new UrlManager(context);
            URL movieUrl = urlManager.getMovieInformation(movie, apiKey);
            String jsonResponse = NetworkManager.request(movieUrl);
            if (jsonResponse != null) {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                latestCopy = MovieDbParser.mapJsonToMovie(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkingException e) {
            e.printStackTrace();
        }

        return latestCopy;
    }

    /**
     * Will update an individual movie within the database.
     *
     * @param movie
     */
    public void updateMovie(int id, Movie movie) {
        movieTable.update(id, movie);
    }
}
