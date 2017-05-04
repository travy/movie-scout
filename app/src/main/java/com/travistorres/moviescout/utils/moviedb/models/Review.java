/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Review
 *
 * Describes the contents of a Movie Review.
 *
 * @author Travis Anthony Torres
 * @version April 5, 2017
 */

public class Review implements Parcelable {
    public String author;
    public String content;
    public String id;
    public String movieId;
    public String url;

    /**
     * Allows an instance to be created without loading in a parcel.
     *
     */
    public Review() {
        //  intentionally left blank
    }

    /**
     * Constructs a new Review from a Parcel.
     *
     * @param in
     */
    protected Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
        movieId = in.readString();
    }

    /**
     * Instructs how to build a new Review after restoring from a Parcel.
     *
     */
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    /**
     * Describes the type of data in the parcel.
     *
     * @return constant 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Instructs how to write into the parcel.
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(movieId);
    }
}
