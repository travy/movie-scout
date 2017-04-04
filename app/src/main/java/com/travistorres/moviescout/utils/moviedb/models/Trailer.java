/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.models;

/**
 * Trailer
 *
 * Describes a trailer object that exists on the Movie DB servers in a simplified manner.
 *
 * @author Travis Anthony Torres
 * @version April 3, 2017
 */

public class Trailer {
    public String id;
    public String iso_639_1;    // this is the name of the field on movieDb, could not resolve using Java conventions
    public String iso_3166_1;
    public String key;
    public String name;
    public String site;
    public String size;
    public String type;

    @Override
    public String toString() {
        return name;
    }
}
