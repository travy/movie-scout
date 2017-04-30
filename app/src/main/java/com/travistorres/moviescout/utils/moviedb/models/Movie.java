/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.travistorres.moviescout.utils.networking.UrlManager;

import java.net.URL;

/**
 * Movie
 *
 * Contains the information regarding a Movie item.
 *
 * @author Travis Anthony Torres
 * @version v1.1.1 (February 15, 2017)
 */

public class Movie implements Parcelable {
    /*
     *  Date formatting constants.
     *
     */
    private final static String DATE_FORMAT_DELIMITER = "-";
    private final static int DATE_FORMAT_YEAR_INDEX = 0;
    private final static int DATE_FORMAT_MONTH_INDEX = 1;
    private final static int DATE_FORMAT_DAY_INDEX = 2;

    /*
     * Attributes
     *
     */
    public boolean hasVideo;
    public boolean isAdultFilm;
    public double popularity;
    public double voteAverage;
    public int dbId;
    public int id;
    public int voteCount;
    public int[] genreIds;
    public String backdropPath;
    public String originalLanguage;
    public String originalTitle;
    public String overview;
    public String posterPath;
    public String releaseDate;
    public String title;

    /**
     * Constructs a movie object.
     *
     */
    public Movie() {
        //  intentionally left blank
    }

    /**
     * Constructs a new Movie instance after retrieval from some data stream.
     *
     */
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        /**
         * Specifies how to construct a new Movie object using a provided Parcel instance.
         *
         * @param in Parcel containing the compressed data object.
         *
         * @return Newly constructed Movie object.
         */
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        /**
         * Allocates memory for multiple Movie instances.
         *
         * @param size Number of instances to reserve.
         *
         * @return Movie Array
         */
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Converts the date into the format MMM dd, yyyy.
     *
     * @return  Formatted date string
     */
    public String getCleanDateFormat() {
        String[] dateBreakdown = releaseDate.split(DATE_FORMAT_DELIMITER);
        String formattedDate = null;

        //  TODO-  Move into a Utility class
        //  Evaluate the month to day form
        switch (dateBreakdown[DATE_FORMAT_MONTH_INDEX]) {
            case "01":
                formattedDate = "Jan ";
                break;
            case "02":
                formattedDate = "Feb ";
                break;
            case "03":
                formattedDate = "Mar ";
                break;
            case "04":
                formattedDate = "Apr ";
                break;
            case "05":
                formattedDate = "May ";
                break;
            case "06":
                formattedDate = "Jun ";
                break;
            case "07":
                formattedDate = "Jul ";
                break;
            case "08":
                formattedDate = "Aug ";
                break;
            case "09":
                formattedDate = "Sep ";
                break;
            case "10":
                formattedDate = "Oct ";
                break;
            case "11":
                formattedDate = "Nov ";
                break;
            case "12":
                formattedDate = "Dec ";
        }

        return formattedDate + dateBreakdown[DATE_FORMAT_DAY_INDEX] + ", " + dateBreakdown[DATE_FORMAT_YEAR_INDEX];
    }

    /**
     * Stores the backdrop image into a given ImageView.
     *
     * @param context
     * @param imageView
     */
    public void loadBackdropIntoImageView(Context context, ImageView imageView) {
        UrlManager urlManager = new UrlManager(context);
        URL backdropUrl = urlManager.getMovieBackdropUrl(backdropPath);

        loadImageFromUrlIntoImageView(context, backdropUrl, imageView);
    }

    /**
     * Stores the poster for the movie into a given ImageView resource.
     *
     * @param context
     * @param imageView
     */
    public void loadPosterIntoImageView(Context context, ImageView imageView) {
        UrlManager urlManager = new UrlManager(context);
        URL posterUrl = urlManager.getMoviePosterUrl(posterPath);

        loadImageFromUrlIntoImageView(context, posterUrl, imageView);
    }

    /**
     * Displays the image that is provided from a URL resource into a given ImageView.
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void loadImageFromUrlIntoImageView(Context context, URL imageUrl, ImageView imageView) {
        String imageUrlString = imageUrl.toString();
        Picasso.with(context).load(imageUrlString).into(imageView);
    }

    /**
     * Maps the contents of the streamed parcel onto the Movie object.
     *
     * @param parcel
     */
    private Movie(Parcel parcel) {
        hasVideo = convertByteToBoolean(parcel.readByte());
        isAdultFilm = convertByteToBoolean(parcel.readByte());
        popularity = parcel.readDouble();
        voteAverage = parcel.readDouble();
        dbId = parcel.readInt();
        id = parcel.readInt();
        voteCount = parcel.readInt();
        genreIds = parcel.createIntArray();
        backdropPath = parcel.readString();
        originalLanguage = parcel.readString();
        originalTitle = parcel.readString();
        overview = parcel.readString();
        posterPath = parcel.readString();
        releaseDate = parcel.readString();
        title = parcel.readString();
    }

    /**
     * Describes the type of data stored within the Parcelable.  Since we only work with Movie
     * instances, we can just return 0
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Packages the Movie instance into an Array format that can be stored within a Parcel for
     * transmission over some data stream.
     *
     * @param dest Package to contain the array
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(convertBooleanToByte(hasVideo));
        dest.writeByte(convertBooleanToByte(isAdultFilm));
        dest.writeDouble(popularity);
        dest.writeDouble(voteAverage);
        dest.writeInt(dbId);
        dest.writeInt(id);
        dest.writeInt(voteCount);
        dest.writeIntArray(genreIds);
        dest.writeString(backdropPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(title);
    }

    /**
     * Will convert a given boolean expression to a byte representation.
     *
     * @param bool The bool to convert
     *
     * @return byte value of 1 for true and 0 for false
     */
    private static byte convertBooleanToByte(boolean bool) {
        return (byte) (bool ? 1 : 0);
    }

    /**
     * Coverts a given byte value to a boolean representation.
     *
     * @param value byte value to convert
     *
     * @return false for 0 and true otherwise
     */
    private static boolean convertByteToBoolean(byte value) {
        return value != 0;
    }

    /**
     * Compares the instance with another Object.
     *
     * @param obj
     *
     * @return true if both identify the same movie id or if the base Object.equals returns true.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Movie) {
            Movie other = (Movie) obj;


            return other.backdropPath.equals(backdropPath) &&
                    other.hasVideo == hasVideo &&
                    other.id == id &&
                    other.isAdultFilm == isAdultFilm &&
                    other.originalLanguage.equals(originalLanguage) &&
                    other.originalTitle.equals(originalTitle) &&
                    other.overview.equals(overview) &&
                    other.popularity == popularity &&
                    other.posterPath.equals(posterPath) &&
                    other.releaseDate.equals(releaseDate) &&
                    other.title.equals(title) &&
                    other.voteAverage == voteAverage &&
                    other.voteCount == voteCount;
        }

        return super.equals(obj);
    }
}
