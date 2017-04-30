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
    public int totalPages;
    public Movie[] movieList;
    public MovieSortType sortType;

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
        ClassLoader classLoader = getClass().getClassLoader();

        currentPage = in.readInt();
        totalPages = in.readInt();
        movieList = convertArrayToMovies(in.readArray(classLoader));
        sortType = MovieSortType.valueOf(in.readString());
    }

    public static Movie[] convertArrayToMovies(Object[] genericArray) {
        int arrayLength = genericArray.length;
        Movie[] movies = new Movie[arrayLength];
        for (int i = 0; i < arrayLength; ++i) {
            movies[i] = (Movie) genericArray[i];
        }

        return movies;
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
        parcel.writeInt(currentPage);
        parcel.writeInt(totalPages);
        parcel.writeArray(movieList);
        parcel.writeString(sortType.name());
    }
}
