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
import com.travistorres.moviescout.utils.moviedb.exceptions.NoContextException;

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
    /*
     *  Date formatting constants.
     *
     */
    private final static String DATE_FORMAT_DELIMITER = "-";
    private final static int DATE_FORMAT_YEAR_INDEX = 0;
    private final static int DATE_FORMAT_MONTH_INDEX = 1;
    private final static int DATE_FORMAT_DAY_INDEX = 2;

    /*
     *  Error message to display when the developer forgot to set a context.
     *
     */
    private final static String CONTEXT_NOT_DEFINED_MESSAGE = "Please set the context before requesting a resource";

    /*
     *  Specifies the indexes each attribute will be assigned to when streamed.
     *
     */
    private final static int NUMBER_FIELDS_IN_STREAM = 14;  //  set to the number of attributes - context
    private final static int STREAM_POSTER_PATH_INDEX = 0;
    private final static int STREAM_IS_ADULT_FILM_INDEX = 1;
    private final static int STREAM_OVERVIEW_INDEX = 2;
    private final static int STREAM_RELEASE_DATE_INDEX = 3;
    private final static int STREAM_GENRE_IDS_INDEX = 4;
    private final static int STREAM_ID_INDEX = 5;
    private final static int STREAM_ORIGINAL_TITLE_INDEX = 6;
    private final static int STREAM_ORIGINAL_LANGUAGE_INDEX = 7;
    private final static int STREAM_TITLE_INDEX = 8;
    private final static int STREAM_BACKDROP_PATH_INDEX = 9;
    private final static int STREAM_POPULARITY_INDEX = 10;
    private final static int STREAM_VOTE_COUNT_INDEX = 11;
    private final static int STREAM_HAS_VIDEO_INDEX = 12;
    private final static int STREAM_VOTE_AVERAGE_INDEX = 13;

    /*
     * Attributes
     *
     */
    public String posterPath;
    public boolean isAdultFilm;
    public String overview;
    public String releaseDate;
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
     * Constructs a new Movie instance after retreival from some data stream.
     *
     */
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        /**
         * Specifies how to construct a new Movie object using a provided Parcel instance.
         *
         * @param in Parcel containing the compressed data object.
         *
         * @return Newly constructed Movie object without a Context set.
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
     * Maps the contents of the streamed parcel onto the Movie object.  This does not set the
     * Context field since it may have changed after the stream.  Remember to always call
     * setContext(...) after recovering a Movie from a Parcel.
     *
     * @param parcel
     */
    private Movie(Parcel parcel) {
        ClassLoader loader = Object.class.getClassLoader();
        Object[] stream = parcel.readArray(loader);

        //  maps each field in the stream to it's respective property
        posterPath = (String) stream[STREAM_POSTER_PATH_INDEX];
        isAdultFilm = (boolean) stream[STREAM_IS_ADULT_FILM_INDEX];
        overview = (String) stream[STREAM_OVERVIEW_INDEX];
        releaseDate = (String) stream[STREAM_RELEASE_DATE_INDEX];
        genreIds = (int[]) stream[STREAM_GENRE_IDS_INDEX];
        id = (int) stream[STREAM_ID_INDEX];
        originalTitle = (String) stream[STREAM_ORIGINAL_TITLE_INDEX];
        originalLanguage = (String) stream[STREAM_ORIGINAL_LANGUAGE_INDEX];
        title = (String) stream[STREAM_TITLE_INDEX];
        backdropPath = (String) stream[STREAM_BACKDROP_PATH_INDEX];
        popularity = (double) stream[STREAM_POPULARITY_INDEX];
        voteCount = (int) stream[STREAM_VOTE_COUNT_INDEX];
        hasVideo = (boolean) stream[STREAM_HAS_VIDEO_INDEX];
        voteAverage = (double) stream[STREAM_VOTE_AVERAGE_INDEX];
    }

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
     *
     * @throws NoContextException When the context has not been defined.
     */
    public URL getPosterUrl() {
        Context mContext = getContext();
        MovieDbUrlManager urlManager = new MovieDbUrlManager(mContext);
        URL posterUrl = urlManager.getMoviePosterUrl(posterPath);

        return posterUrl;
    }

    /**
     * Specifies the context that the Movie should work on.
     *
     * @param mContext
     */
    public void setContext(Context mContext) {
        context = mContext;
    }

    /**
     * Retrieves a String representation of the URL used to acquire the poster image for the given
     * Movie.
     *
     * @return movie poster url string or null on error.
     */
    public String getPosterPathUrlString() {
        String posterUrlString = null;
        try {
            URL posterUrl = getPosterUrl();
            posterUrlString = posterUrl.toString();
        } catch (NoContextException e) {
            e.printStackTrace();
        }

        return posterUrlString;
    }

    /**
     * Stores the poster for the movie into a given ImageView resource.
     *
     * @param imageView The ImageView that the poster should be stored into.
     *
     * @throws NoContextException
     */
    public void loadPosterIntoImageView(ImageView imageView) throws NoContextException {
        Context mContext = getContext();
        Picasso.with(mContext).load(getPosterPathUrlString()).into(imageView);
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
     * Retrieves the context for the Movie.
     *
     * This method is preferable over calling context directly since it will force the throwing of
     * a NoContextException if context is null.
     *
     * @return The defined context.
     *
     * @throws NoContextException When no context has been defined.
     */
    protected Context getContext() throws NoContextException {
        if (context == null) {
            throw new NoContextException(CONTEXT_NOT_DEFINED_MESSAGE);
        }

        return context;
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
        Object[] stream = new Object[NUMBER_FIELDS_IN_STREAM];
        stream[STREAM_POSTER_PATH_INDEX] = posterPath;
        stream[STREAM_IS_ADULT_FILM_INDEX] = isAdultFilm;
        stream[STREAM_OVERVIEW_INDEX] = overview;
        stream[STREAM_RELEASE_DATE_INDEX] = releaseDate;
        stream[STREAM_GENRE_IDS_INDEX] = genreIds;
        stream[STREAM_ID_INDEX] = id;
        stream[STREAM_ORIGINAL_TITLE_INDEX] = originalTitle;
        stream[STREAM_ORIGINAL_LANGUAGE_INDEX] = originalLanguage;
        stream[STREAM_TITLE_INDEX] = title;
        stream[STREAM_BACKDROP_PATH_INDEX] = backdropPath;
        stream[STREAM_POPULARITY_INDEX] = popularity;
        stream[STREAM_VOTE_COUNT_INDEX] = voteCount;
        stream[STREAM_HAS_VIDEO_INDEX] = hasVideo;
        stream[STREAM_VOTE_AVERAGE_INDEX] = voteAverage;

        dest.writeArray(stream);
    }
}
