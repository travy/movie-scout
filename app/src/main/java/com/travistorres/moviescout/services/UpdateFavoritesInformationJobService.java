/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.services;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * UpdateFavoritesInformationJobService
 *
 * Constructs a schedulable job which will query the server for updates on all of the users
 * Favorite movies.
 *
 * @author Travis Anthony Torres
 * @version April 23, 2017
 */

public class UpdateFavoritesInformationJobService extends JobService {
    private AsyncTask mFavoritesUpdateTask;

    /**
     * Starts a separate thread which will query the server for all of the latest information on
     * all of the users Favorites.  If a change is detected on the users Favorites, then the
     * database will be updated with the latest information.
     *
     * @param jobParameters
     *
     * @return Constant value of true
     */
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFavoritesUpdateTask = new AsyncTask() {
            /**
             * Requests the latest information in the users favorites and if any data has changed
             * will update the record in the database.
             *
             * @param params
             *
             * @return
             */
            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            /**
             * Specifies what to do when the job has finished updating all of the users favorites.
             * @param o
             */
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                jobFinished(jobParameters, false);
            }
        };

        mFavoritesUpdateTask.execute();

        return true;
    }

    /**
     * Specify the operations to perform when the Job needs to be cancelled.
     *
     * @param jobParameters
     *
     * @return Constant value of true
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFavoritesUpdateTask != null) {
            mFavoritesUpdateTask.cancel(true);
        }

        return true;
    }
}
