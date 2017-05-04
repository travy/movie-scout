/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils.db.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travistorres.moviescout.utils.db.MoviesDatabase;

/**
 * BaseTable
 *
 * The `BaseTable` is an abstract representation of a SQL table contained within the application.
 * Using this as the parent of a given Table representation will grant the ability to save, load
 * and associate other records with extreme ease.
 *
 * @author Travis Anthony Torres
 * @version April 8, 2017
 */

public abstract class BaseTable {
    private String tableName;
    private Context context;
    protected SQLiteDatabase connection;

    /**
     * Provides a Base Table object which allows the context to be defined and utilizes a read-
     * only database connection.
     *
     * @param mContext
     */
    public BaseTable(Context mContext) {
        this(mContext, true);
    }

    /**
     * Constructs a new Table with and specifies the context and database connection.
     *
     * @param mContext
     * @param database
     */
    public BaseTable(Context mContext, SQLiteDatabase database) {
        context = mContext;
        connection = database;
    }

    /**
     * Constructs a new Table with a specified context and will construct a database connection
     * that is either readonly or writable based on the value of the `readOnly` parameter.
     *
     * @param mContext
     * @param readOnly
     */
    public BaseTable(Context mContext, boolean readOnly) {
        context = mContext;

        MoviesDatabase db = new MoviesDatabase(context);
        connection = (readOnly) ?
                db.getReadableDatabase() :
                db.getWritableDatabase();
    }

    /**
     * Acquires the database that the Table is using.
     *
     * @return Table database connection
     */
    public final SQLiteDatabase getDatabase() {
        return connection;
    }

    /**
     * Retrieves the name of the table.
     *
     * @return SQL table name
     */
    public final String getTableName() {
        return tableName;
    }

    /**
     * Specifies what the name of the table will be.
     *
     * @param sqlTableName
     */
    public final void setTableName(String sqlTableName) {
        tableName = sqlTableName;
    }

    /**
     * Uses a specified `ContentValues` object to save information into the Database.
     *
     * @param data
     * @param nullColumnStack
     *
     * @return Result of the table insert
     */
    public final long save(Object data, String nullColumnStack) {
        String tableName = getTableName();
        ContentValues values = getContentValues(data);

        return connection.insert(tableName, nullColumnStack, values);
    }

    /**
     * Saves a new entry into the database.
     *
     * @param nullColumnStack
     * @param contentValues
     *
     * @return id of the inserted row
     */
    public final long save(String nullColumnStack, ContentValues contentValues) {
        String tableName = getTableName();
        return connection.insert(tableName, nullColumnStack, contentValues);
    }

    /**
     * Updates a field in the database.
     *
     * @param contentValues
     * @param whereClause
     * @param whereArgs
     *
     * @return The number of fields updated
     */
    public final int update(ContentValues contentValues, String whereClause, String[] whereArgs) {
        String tableName = getTableName();
        return connection.update(tableName, contentValues, whereClause, whereArgs);
    }

    /**
     * Deletes a database field from the system.
     *
     * @param whereClause
     * @param whereArgs
     *
     * @return
     */
    public final int deleteFromDatabase(String whereClause, String[] whereArgs) {
        String tableName = getTableName();

        return connection.delete(tableName, whereClause, whereArgs);
    }

    /**
     * Determines if there are rows in the database with the specified data fields.
     *
     * @param whereClause
     * @param whereArgs
     *
     * @return `true` if fields exist and `false` otherwise.
     */
    protected final boolean doesExistsInDatabase(String whereClause, String[] whereArgs) {
        String tableName = getTableName();
        Cursor cursor = connection.query(tableName, null, whereClause, whereArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    /**
     * Uses a specified `ContentValues` object to save information into the Database.
     *
     * @param data
     *
     * @return Result of the table insert
     */
    public final long save(Object data) {
        return save(data, null);
    }

    /**
     * Executes a raw query operation on the table.
     *
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     *
     * @return
     */
    public final Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName();
        return connection.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    /**
     * Describes how to package the data being saved into the database table.
     *
     * @param data
     *
     * @return
     */
    public abstract ContentValues getContentValues(Object data);

    /**
     * Determines if a given object is contained within the table.
     *
     * @param data
     *
     * @return `true` if the object is contained in the database and `false` otherwise.
     */
    public abstract boolean contains(Object data);

    /**
     * Removes a given object from the database.
     *
     * @param data
     *
     * @return
     */
    public abstract int delete(Object data);
}
