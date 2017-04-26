/**
 * Copyright (C) 2017 Travis Anthony Torres
 */

package com.travistorres.moviescout.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.travistorres.moviescout.MainActivity;
import com.travistorres.moviescout.R;
import com.travistorres.moviescout.utils.moviedb.models.Movie;

/**
 * NotificationUtils
 *
 * Responsible for displaying notifications that occur within the application to the user even
 * if they are not currently running the app.
 *
 * @author Travis Anthony Torres
 * @version April 24, 2017
 */

public class NotificationsUtils {
    /**
     * Acquires the task id for the notification pipeline.
     *
     * @param context
     *
     * @return The numeric identifier for the notification.
     */
    private static int getScheduledUpdatesTaskId(Context context) {
        Resources resources = context.getResources();
        int scheduledUpdatesTaskId = resources.getInteger(R.integer.favorite_movies_scheduled_updates_task_id);

        return scheduledUpdatesTaskId;
    }

    /**
     * Constructs a content.
     *
     * @param context
     *
     * @return
     */
    private static PendingIntent contentIntent(Context context) {
        int favoritesIntentId = getScheduledUpdatesTaskId(context);
        Intent startActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                favoritesIntentId,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    /**
     * Constructs a large icon for the notification window.
     *
     * @param context
     *
     * @return Large image icon
     */
    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.youtube_icon);
        return largeIcon;
    }

    /**
     * Informs the user that one or movies in their favorites have been updated.
     *
     * @param context
     */
    public static void notifyUserThatFavoritesHaveBeenUpdated(Context context) {
        //  builds a notification alerting the user that movies have been updated
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.youtube_icon)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.favorites_update_title_notification))
                .setContentText(context.getString(R.string.favorites_update_description_notification))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        //  checks the version of the os to see if priority can be changed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        //  displays the notification
        int favoritesTaskId = getScheduledUpdatesTaskId(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(favoritesTaskId, notificationBuilder.build());
    }
}
