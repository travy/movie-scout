/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils;

import android.content.Context;
import android.content.res.Resources;

import com.travistorres.moviescout.R;

/**
 * DateConversionUtility
 *
 * Will convert the representation of a given date string into other representations.
 *
 * @author Travis Anthony Torres
 * @version April 30, 2017
 */

public class DateConversionUtility {
    //  used for hashing keys
    private static final String JANUARY_MONTH_CODE = "01";
    private static final String FEBRUARY_MONTH_CODE = "02";
    private static final String MARCH_MONTH_CODE = "03";
    private static final String APRIL_MONTH_CODE = "04";
    private static final String MAY_MONTH_CODE = "05";
    private static final String JUNE_MONTH_CODE = "06";
    private static final String JULY_MONTH_CODE = "07";
    private static final String AUGUST_MONTH_CODE = "08";
    private static final String SEPTEMBER_MONTH_CODE = "09";
    private static final String OCTOBER_MONTH_CODE = "10";
    private static final String NOVEMBER_MONTH_CODE = "11";
    private static final String DECEMBER_MONTH_CODE = "12";

    /**
     * Will convert a given date that is given in the format xx-xx-xxxx into the format MMM dd, yyyy
     * where the month is in its word representation.
     *
     * @param context
     * @param numericalDate
     *
     * @return Date with month written out.
     */
    public static String convertNumericalDateToFullDate(Context context, String numericalDate) {
        String[] tokens = tokenizeNumericalDate(context, numericalDate);
        String month = parseMonthNameFromNumericValue(context, tokens);
        String day = parseDayFromNumericalDate(context, tokens);
        String year = parseYearFromNumericalDate(context, tokens);

        return getDateString(context, month, day, year);
    }

    /**
     * Converts the given month code specified in the tokens into its string equivalent.
     *
     * @param context
     * @param tokens
     *
     * @return Converts numeric month representation to string representation.
     */
    private static String parseMonthNameFromNumericValue(Context context, String[] tokens) {
        String month = null;

        Resources resources = context.getResources();
        int monthIndex = resources.getInteger(R.integer.date_parse_month_index);
        switch (tokens[monthIndex]) {
            case JANUARY_MONTH_CODE:
                month = context.getString(R.string.date_parse_january_word);
                break;
            case FEBRUARY_MONTH_CODE:
                month = context.getString(R.string.date_parse_february_word);
                break;
            case MARCH_MONTH_CODE:
                month = context.getString(R.string.date_parse_march_word);
                break;
            case APRIL_MONTH_CODE:
                month = context.getString(R.string.date_parse_april_word);
                break;
            case MAY_MONTH_CODE:
                month = context.getString(R.string.date_parse_may_word);
                break;
            case JUNE_MONTH_CODE:
                month = context.getString(R.string.date_parse_june_word);
                break;
            case JULY_MONTH_CODE:
                month = context.getString(R.string.date_parse_july_word);
                break;
            case AUGUST_MONTH_CODE:
                month = context.getString(R.string.date_parse_august_word);
                break;
            case SEPTEMBER_MONTH_CODE:
                month = context.getString(R.string.date_parse_september_word);
                break;
            case OCTOBER_MONTH_CODE:
                month = context.getString(R.string.date_parse_october_word);
                break;
            case NOVEMBER_MONTH_CODE:
                month = context.getString(R.string.date_parse_november_word);
                break;
            case DECEMBER_MONTH_CODE:
                month = context.getString(R.string.date_parse_december_word);
        }

        return month;
    }

    /**
     * Breaks down a given string in the format xx-xx-xxxx into an array separated by the `-`
     * delimiter.
     *
     * @param context
     * @param numericalDate
     *
     * @return Delimited array
     */
    private static String[] tokenizeNumericalDate(Context context, String numericalDate) {
        String delimiter = context.getString(R.string.date_parse_token_delimiter);
        return numericalDate.split(delimiter);
    }

    /**
     * Acquires the day from the token array.
     *
     * @param context
     * @param tokens
     *
     * @return Day of the month
     */
    private static String parseDayFromNumericalDate(Context context, String[] tokens) {
        Resources resources = context.getResources();
        int dayIndex = resources.getInteger(R.integer.date_parse_day_index);
        return tokens[dayIndex];
    }

    /**
     * Pulls out the value for year from the tokens array.
     *
     * @param context
     * @param tokens
     *
     * @return year in tokens
     */
    private static String parseYearFromNumericalDate(Context context, String[] tokens) {
        Resources resources = context.getResources();
        int yearIndex = resources.getInteger(R.integer.date_parse_year_index);
        return tokens[yearIndex];
    }

    /**
     * Builds a string representation for a date based off of a concatenation of the provided
     * month day and year.
     *
     * @param context
     * @param month
     * @param day
     * @param year
     *
     * @return concatenation of month day and year
     */
    private static String getDateString(Context context, String month, String day, String year) {
        String monthDaySeparator = context.getString(R.string.date_parse_month_day_separator);
        String dayYearSeparator = context.getString(R.string.date_parse_day_year_separator);
        return month + monthDaySeparator + day + dayYearSeparator + year;
    }
}
