/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.services;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import com.travistorres.moviescout.notifications.NotificationsUtils;
import com.travistorres.moviescout.utils.widget.FavoritesManager;

/**
 * FavoritesUpdatingTask
 *
 * Will request that all of the users favorites be updated by checking them against the latest
 * information from the server.
 *
 * @author Travis Anthony Torres
 * @version April 24, 2017
 */

class FavoritesUpdatingTask extends AsyncTask {
    private JobService jobService;
    private JobParameters jobParameters;
    private boolean didUpdateOccur;

    /**
     * Registers both the service and the parameters with the task.
     *
     * @param jobService
     * @param jobParameters
     */
    public FavoritesUpdatingTask(JobService jobService, JobParameters jobParameters) {
        this.jobService = jobService;
        this.jobParameters = jobParameters;
    }

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
        FavoritesManager favoritesManager = new FavoritesManager(jobService);
        didUpdateOccur = favoritesManager.updateMovies();

        return null;
    }

    /**
     * Specifies what to do when the job has finished updating all of the users favorites.
     *
     * @param o
     */
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        //  notify the system that the job is finished and display a notification if an update occurred
        jobService.jobFinished(jobParameters, false);
        if (didUpdateOccur) {
            NotificationsUtils.notifyUserThatFavoritesHaveBeenUpdated(jobService);
        }
    }
}
