/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.tables;

/**
 * TrailersTable
 *
 * Describes each of the Movie trailers that have been associated with a Movie entry in the
 * Movies database table.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public final class TrailersTable {
    public static final String NAME = "trailers";

    public static final class Cols {
        public static final String UUID = "uuid";
        public static final String MOVIE_UUID = "movie_uuid";
        public static final String ISO_639_1 = "iso_639_1";
        public static final String ISO_3166_1 = "iso_3166_1";
        public static final String KEY = "key";
        public static final String NAME = "name";
        public static final String SITE = "site";
        public static final String SIZE = "size";
        public static final String TYPE = "type";
    }
}
