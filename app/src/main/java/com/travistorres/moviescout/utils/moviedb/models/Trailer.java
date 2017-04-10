/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.moviedb.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.travistorres.moviescout.utils.networking.UrlManager;

import java.net.URL;

/**
 * Trailer
 *
 * Describes a trailer object that exists on the Movie DB servers in a simplified manner.
 *
 * @author Travis Anthony Torres
 * @version April 3, 2017
 */

public class Trailer implements Parcelable{
    public String id;
    public String iso_639_1;    // this is the name of the field on movieDb, could not resolve using Java conventions
    public String iso_3166_1;
    public String key;
    public String name;
    public String site;
    public String size;
    public String type;
    public String movieId;

    /**
     * Constructs a new Trailer instance.
     *
     */
    public Trailer() {
        //  intentionally left blank
    }

    /**
     * Builds a Trailer from a parcel.
     *
     * @param in
     */
    protected Trailer(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
        type = in.readString();
        movieId = in.readString();
    }

    /**
     * Instructs how to package the Trailer into a Parcel.
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);
        dest.writeString(type);
        dest.writeString(movieId);
    }

    /**
     * Describes the contents of the Parcel.
     *
     * @return constant zero
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Informs the process of how to recreate a Trailer from a Parcel.
     *
     */
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    /**
     * Retrieves the youtube URL that will allow for viewing of the trailer.
     *
     * @param context
     *
     * @return URL where the trailer can be watched.
     */
    public URL getVideoUrl(Context context) {
        UrlManager urlManager = new UrlManager(context);
        URL videoUrl = urlManager.getVideoTrailerUrl(this);

        return videoUrl;
    }
}
