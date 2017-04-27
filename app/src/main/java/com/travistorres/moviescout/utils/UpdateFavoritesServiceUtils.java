/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import com.travistorres.moviescout.R;
import com.travistorres.moviescout.services.UpdateFavoritesInformationJobService;

import java.util.concurrent.TimeUnit;

/**
 * UpdateFavoritesServiceUtils
 *
 * Sets up a service which will determine if any of the users Favorite Moves have been updated since
 * they were last viewed.
 *
 * @author Travis Anthony Torres
 * @version April 24, 2017
 */

public class UpdateFavoritesServiceUtils {
    private static final int REMINDER_INTERVAL_MINUTES = 2;//15;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    //  specifies if the app has been updated
    private static boolean sInitialized;

    /**
     * Runs a concurrent thread for determining if any of the users Favorites have been updated.
     *
     * @param context
     */
    synchronized public static void scheduleUpdateFavorites(@NonNull final Context context) {
        //  ignore if the service is already initialized
        if (sInitialized) {
            return;
        }

        //  configures the service for updating the favorites
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintFavoritesUpdateJob = dispatcher.newJobBuilder()
                .setService(UpdateFavoritesInformationJobService.class)
                .setTag(context.getString(R.string.favorites_update_job_task))
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS
                ))
                .setReplaceCurrent(true)
                .build();

        // schedule the service
        dispatcher.schedule(constraintFavoritesUpdateJob);
        sInitialized = true;
    }

    /**
     * Unscheduled the favorites update service.
     *
     * @param context
     */
    synchronized public static void unscheduledUpdateFavorites(@NonNull final Context context) {
        //  its fine if the service has not been scheduled
        if (!sInitialized) {
            return;
        }

        //  cancel the service
        String serviceTag = context.getString(R.string.favorites_update_job_task);
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        dispatcher.cancel(serviceTag);
        sInitialized = false;
    }
}
