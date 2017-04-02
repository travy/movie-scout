/**
 * Copyright (C) Travis Anthony Torres
 */

package com.travistorres.moviescout;

import android.os.Parcel;
import android.os.Parcelable;

import com.travistorres.moviescout.utils.moviedb.MovieSortType;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * MainActivityParcelable
 *
 * A Parcelable package of all instance fields in the MainActivity that need to be saved in order
 * to rebuild the state of the Activity after it is destroyed.
 *
 * @author Travis Anthony Torres
 * @version April 1, 2017
 */

public class MainActivityParcelable implements Parcelable {
    private static final int CURRENT_PAGE_INDEX = 0;
    private static final int SORT_TYPE_INDEX = 1;
    private static final int MOVIE_LIST_INDEX = 2;

    public int currentPage;
    public MovieSortType sortType;
    public Movie[] movieList;

    /**
     * Instructs Android how to unpack the Parcelable.
     *
     */
    public static final Creator<MainActivityParcelable> CREATOR = new Creator<MainActivityParcelable>() {
        @Override
        public MainActivityParcelable createFromParcel(Parcel in) {
            return new MainActivityParcelable(in);
        }

        @Override
        public MainActivityParcelable[] newArray(int size) {
            return new MainActivityParcelable[size];
        }
    };

    /**
     * Constructs a new Parcelable object for the MainActivity.
     *
     */
    public MainActivityParcelable() {
        //  Intentionally left blank
    }

    /**
     * Stores the value of the parcel back into the objects scope to restore its state.
     *
     * @param in
     */
    protected MainActivityParcelable(Parcel in) {
        Object[] stream = in.readArray(getClass().getClassLoader());

        currentPage = (int) stream[CURRENT_PAGE_INDEX];
        sortType = (MovieSortType) stream[SORT_TYPE_INDEX];
        //  needs to iterate over the list based on how Java Grammars work with interfaces read http://stackoverflow.com/questions/8745893/i-dont-get-why-this-classcastexception-occurs
        Object[] blah = (Object[]) stream[MOVIE_LIST_INDEX];
        movieList = new Movie[blah.length];
        for (int i = 0; i < blah.length; i++) {
            Movie m = (Movie) blah[i];
            movieList[i] = (Movie) blah[i];
        }
    }

    /**
     * Describes the type of data stored within the zero.
     *
     * @return zero value
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the contents of the class into a parcel.
     *
     * @param parcel
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Object[] stream = new Object[3];
        stream[CURRENT_PAGE_INDEX] = currentPage;
        stream[SORT_TYPE_INDEX] = sortType;
        stream[MOVIE_LIST_INDEX] = movieList;

        parcel.writeArray(stream);
    }
}
