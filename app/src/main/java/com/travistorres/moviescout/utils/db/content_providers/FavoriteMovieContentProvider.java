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

    /**
     * Will execute a `SELECT` query on a given table.
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     *
     * @return Cursor object from the `SELECT` operation.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase readableDatabase = getReadableDatabase();

        BaseTable table;
        Cursor cursor;
        Context context = getContext();
        int uriCode = sUriMatcher.match(uri);
        switch (uriCode) {
            case MOVIES:
                table = new MoviesTable(context, readableDatabase);
                cursor = table.query(projection, selection, selectionArgs, sortOrder);
                break;
            case MOVIE_WITH_ID:
                table = new MoviesTable(context, readableDatabase);
                cursor = getFieldFromTable(uri, table, projection, sortOrder);
                break;
            case REVIEWS:
                table = new ReviewsTable(context, readableDatabase);
                cursor = table.query(projection, selection, selectionArgs, sortOrder);
                break;
            case REVIEW_WITH_ID:
                table = new ReviewsTable(context, readableDatabase);
                cursor = getFieldFromTable(uri, table, projection, sortOrder);
                break;
            case TRAILERS:
                table = new TrailersTable(context, readableDatabase);
                cursor = table.query(projection, selection, selectionArgs, sortOrder);
                break;
            case TRAILER_WITH_ID:
                table = new TrailersTable(context, readableDatabase);
                cursor = getFieldFromTable(uri, table, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(context.getString(R.string.content_provider_unknown_uri_message));
        }

        cursor.setNotificationUri(context.getContentResolver(), uri);

        return cursor;
    }

    /**
     * Requests a `SELECT` operation on a particular row in the table.
     *
     * @param uri
     * @param table
     * @param projection
     * @param sortOrder
     *
     * @return
     */
    private Cursor getFieldFromTable(Uri uri, BaseTable table, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);
        String mSelection = "_id=?";
        String[] mSelectionArgs = new String[]{id};

        return table.query(projection, mSelection, mSelectionArgs, sortOrder);
    }

    /**
     * Constant null as not needed.
     *
     * @param uri
     *
     * @return value null or some mime type
     */
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

    /**
     * Removes a selection of rows from the specified table.
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     *
     * @return Number of rows that were deleted.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();

        BaseTable table;
        Context context = getContext();
        int uriCode = sUriMatcher.match(uri);
        switch (uriCode) {
            case MOVIES:
            case MOVIE_WITH_ID:
                table = new MoviesTable(context, writableDatabase);
                break;
            case TRAILERS:
            case TRAILER_WITH_ID:
                table = new TrailersTable(context, writableDatabase);
                break;
            case REVIEWS:
            case REVIEW_WITH_ID:
                table = new ReviewsTable(context, writableDatabase);
                break;
            default:
                throw new UnsupportedOperationException(context.getString(R.string.content_provider_unknown_uri_message));
        }

        int numDeletedRows = table.deleteFromDatabase(selection, selectionArgs);
        context.getContentResolver().notifyChange(uri, null);

        return numDeletedRows;
    }

    /**
     * Updates fields in the database.
     *
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     *
     * @return The number of updated fields.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();

        BaseTable table;
        Context context = getContext();
        int uriCode = sUriMatcher.match(uri);
        switch (uriCode) {
            case MOVIES:
            case MOVIE_WITH_ID:
                table = new MoviesTable(context, writableDatabase);
                break;
            case TRAILERS:
            case TRAILER_WITH_ID:
                table = new TrailersTable(context, writableDatabase);
                break;
            case REVIEWS:
            case REVIEW_WITH_ID:
                table = new ReviewsTable(context, writableDatabase);
                break;
            default:
                throw new UnsupportedOperationException(context.getString(R.string.content_provider_unknown_uri_message));
        }

        int numUpdated = table.update(values, selection, selectionArgs);
        context.getContentResolver().notifyChange(uri, null);

        return numUpdated;
    }
}
