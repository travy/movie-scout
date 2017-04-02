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

        currentPage = (int) stream[0];
        sortType = (MovieSortType) stream[1];

        //  TODO- figure out why I can't just cast the movie list to an Movie[] without the compiler getting confused
        Object[] blah = (Object[]) stream[2];
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
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Object[] stream = new Object[3];
        stream[0] = currentPage;
        stream[1] = sortType;
        stream[2] = movieList;

        parcel.writeArray(stream);
    }
}
