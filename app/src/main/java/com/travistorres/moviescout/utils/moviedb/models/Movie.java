/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.models;

/**
 * Contains the information regarding a Movie item.
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

public class Movie {
    public String posterPath;
    public boolean isAdultFilm;
    public String overview;
    public String releaseDate;  //  TODO:  should be a Date object
    public int[] genreIds;
    public int id;
    public String originalTitle;
    public String originalLanguage;
    public String title;
    public String backdropPath;
    public double popularity;
    public int voteCount;
    public boolean hasVideo;
    public double voteAverage;
}
