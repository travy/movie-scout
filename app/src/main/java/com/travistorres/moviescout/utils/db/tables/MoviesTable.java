/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.tables;

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

public final class MoviesTable {
    public static final String NAME = "movies";

    public static final class Cols {
        public static final String UUID = "uuid";
        public static final String POSTER_PATH = "poster_path";
        public static final String IS_ADULT_FILM = "is_adult_film";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String GENRE_IDS = "genre_ids";
        public static final String MOVIE_ID = "movie_id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String TITLE = "title";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String POPULARITY = "popularity";
        public static final String VOTE_COUNT = "vote_count";
        public static final String HAS_VIDEO = "has_video";
        public static final String VOTE_AVERAGE = "vote_average";
    }
}
