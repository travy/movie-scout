/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.content_providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.db.MoviesDatabase;
import com.travistorres.moviescout.utils.db.tables.BaseTable;
import com.travistorres.moviescout.utils.db.tables.MoviesTable;
import com.travistorres.moviescout.utils.db.tables.ReviewsTable;
import com.travistorres.moviescout.utils.db.tables.TrailersTable;

/**
 * FavoriteMovieContentProvider
 *
 * Provides access to the database via a Content Provider.
 *
 * @author Travis Anthony Torres
 * @version May 2, 2017
 */

public class FavoriteMovieContentProvider extends ContentProvider {
    private MoviesDatabase movieDatabase;

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final int REVIEWS = 200;
    public static final int REVIEW_WITH_ID = 201;
    public static final int TRAILERS = 300;
    public static final int TRAILER_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Construct a UriMatcher which will map a requested uri to a resource id.
     *
     * @return uriMatcher
     */
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //  content://com.travistorres.moviescout/movies/
        uriMatcher.addURI(MoviesDatabase.AUTHORITY, MoviesTable.CONTENT_PATH, MOVIES);
        //  content://com.travistorres.moviescout/movies/#
        uriMatcher.addURI(MoviesDatabase.AUTHORITY, MoviesTable.CONTENT_PATH + "/#", MOVIE_WITH_ID);

        //  content://com.travistorres.moviescout/reviews/
        uriMatcher.addURI(MoviesDatabase.AUTHORITY, ReviewsTable.CONTENT_PATH, REVIEWS);
        //  content://com.travistorres.moviescout/reviews/#
        uriMatcher.addURI(MoviesDatabase.AUTHORITY, ReviewsTable.CONTENT_PATH + "/#", REVIEW_WITH_ID);

        //  content://com.travistorres.moviescout/trailers/
        uriMatcher.addURI(MoviesDatabase.AUTHORITY, TrailersTable.CONTENT_PATH, TRAILERS);
        //  content://com.travistorres.moviescout/trailers/#
        uriMatcher.addURI(MoviesDatabase.AUTHORITY, TrailersTable.CONTENT_PATH + "/#", TRAILER_WITH_ID);

        return uriMatcher;
    }

    /**
     * Acquires a database instance for creating connections.
     *
     * @return true if the instance was created
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDatabase = new MoviesDatabase(context);

        return true;
    }

    /**
     * Acquires a connection to a writable database.
     *
     * @return writable database
     */
    private SQLiteDatabase getWritableDatabase() {
        return movieDatabase.getWritableDatabase();
    }

    /**
     * Acquires a connection to a readable database.
     *
     * @return read only database
     */
    private SQLiteDatabase getReadableDatabase() {
        return movieDatabase.getReadableDatabase();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     * Will insert an entity into the database based off of the provided uri and content values.
     *
     * @param uri
     * @param values
     *
     * @return Uri with the id of the inserted entry
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();
        Context context = getContext();

        Uri returnUri;
        int uriCode = sUriMatcher.match(uri);
        switch (uriCode) {
            case MOVIES:
                MoviesTable movies = new MoviesTable(context, writableDatabase);
                returnUri = acquireInsertUri(movies, values, MoviesTable.MOVIE_CONTENT_URI, uri);
                break;
            case REVIEWS:
                ReviewsTable reviews = new ReviewsTable(context, writableDatabase);
                returnUri = acquireInsertUri(reviews, values, ReviewsTable.REVIEW_CONTENT_URI, uri);
                break;
            case TRAILERS:
                TrailersTable trailers = new TrailersTable(context, writableDatabase);
                returnUri = acquireInsertUri(trailers, values, TrailersTable.TRAILER_CONTENT_URI, uri);
                break;
            default:
                throw new UnsupportedOperationException(context.getString(R.string.content_provider_unknown_uri_message) + uri);
        }

        context.getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    /**
     * Allows for inserting into a specified table.
     *
     * @param table
     * @param contentValues
     * @param contentUri
     * @param uri
     *
     * @return The id of the inserted table row
     */
    private Uri acquireInsertUri(BaseTable table, ContentValues contentValues, Uri contentUri, Uri uri) {
        Uri returnedUri;
        long id = table.save(null, contentValues);
        if (id > 0) {
            returnedUri = ContentUris.withAppendedId(contentUri, id);
        } else {
            throw new SQLException(getContext().getString(R.string.content_provider_failed_insert_message) + uri);
        }

        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
