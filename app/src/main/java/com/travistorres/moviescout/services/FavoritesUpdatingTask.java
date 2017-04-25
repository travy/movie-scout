/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.services;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import com.travistorres.moviescout.notifications.NotificationsUtils;

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

        jobService.jobFinished(jobParameters, false);
        NotificationsUtils.notifyUserThatFavoritesHaveBeenUpdated(jobService);
    }
}
