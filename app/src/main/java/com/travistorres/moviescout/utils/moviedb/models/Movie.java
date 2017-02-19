/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.travistorres.moviescout.utils.moviedb.MovieDbUrlManager;

import java.net.URL;

/**
 * Movie
 *
 * Contains the information regarding a Movie item.
 *
 * @author Travis Anthony Torres
 * @version February 15, 2017
 */

public class Movie implements Parcelable {
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

    private Context context;

    /**
     * TODO:  document
     *
     */
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {

            return null;
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Constructs a new Movie object.
     *
     * @param mContext
     */
    public Movie(Context mContext) {
        context = mContext;
    }

    /**
     * Retrieves the URL to acquire the poster for the Movie.
     *
     * @return Movie poster url
     */
    public URL getPosterUrl() {
        MovieDbUrlManager urlManager = new MovieDbUrlManager(context);
        URL posterUrl = urlManager.getMoviePosterUrl(posterPath);

        return posterUrl;
    }

    /**
     * Retrieves a String representation of the URL used to acquire the poster image for the given
     * Movie.
     *
     * @return movie poster url string
     */
    public String getPosterPathUrlString() {
        URL posterUrl = getPosterUrl();

        return posterUrl.toString();
    }

    /**
     * Stores the poster for the movie into a given ImageView resource.
     *
     * @param imageView The ImageView that the poster should be stored into.
     */
    public void loadPosterIntoImageView(ImageView imageView) {
        Picasso.with(context).load(getPosterPathUrlString()).into(imageView);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
