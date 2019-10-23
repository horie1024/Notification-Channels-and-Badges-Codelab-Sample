package com.horie1024.notificationchannelcodelab

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import java.util.*

internal class NotificationHelper(context: Context) : ContextWrapper(context) {

    companion object {
        const val FOLLOWERS_CHANNEL = "follower"
        const val DIRECT_MESSAGE_CHANNEL = "direct_message"
    }

    private val mNotificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * Registers notification channels, which can be used later by individual notifications.
     */
    init {

        // Create the channel object with the unique ID FOLLOWERS_CHANNEL
        val followersChannel = NotificationChannel(
            FOLLOWERS_CHANNEL,
            getString(R.string.notification_channel_followers),
            NotificationManager.IMPORTANCE_DEFAULT
        )

        // Configure the channel's initial settings
        followersChannel.lightColor = Color.GREEN
        followersChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 500, 200, 500)

        // Submit the notification channel object to the notification manager
        mNotificationManager.createNotificationChannel(followersChannel)

        val dmChannel = NotificationChannel(
            DIRECT_MESSAGE_CHANNEL,
            getString(R.string.notification_channel_direct_message),
            NotificationManager.IMPORTANCE_HIGH
        )
        dmChannel.lightColor = Color.BLUE
        mNotificationManager.createNotificationChannel(dmChannel)
    }

    /**
     * Get a follow/un-follow notification
     *
     * Provide the builder rather than the notification it's self as useful for making
     * notification changes.

     * @param title the title of the notification
     * *
     * @param body  the body text for the notification
     * *
     * @return A Notification.Builder configured with the selected channel and details
     */
    fun getNotificationFollower(title: String, body: String): Notification.Builder {
        return Notification.Builder(applicationContext, FOLLOWERS_CHANNEL)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

    /**
     * Get a direct message notification
     *
     * Provide the builder rather than the notification it's self as useful for making
     * notification changes.

     * @param title Title for notification.
     * *
     * @param body  Message for notification.
     * *
     * @return A Notification.Builder configured with the selected channel and details
     */
    fun getNotificationDM(title: String, body: String): Notification.Builder {
        return Notification.Builder(applicationContext, DIRECT_MESSAGE_CHANNEL)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

    /**
     * Create a PendingIntent for opening up the MainActivity when the notification is pressed

     * @return A PendingIntent that opens the MainActivity
     */
    private // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your application to the Home screen.
    // Adds the back stack for the Intent (but not the Intent itself)
    // Adds the Intent that starts the Activity to the top of the stack
    val pendingIntent: PendingIntent
        get() {
            val openMainIntent = Intent(this, MainActivity::class.java)
            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addParentStack(MainActivity::class.java)
            stackBuilder.addNextIntent(openMainIntent)
            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT)
        }

    /**
     * Send a notification.
     *
     * @param id           The ID of the notification
     * *
     * @param notification The notification object
     */
    fun notify(id: Int, notification: Notification.Builder) {
        mNotificationManager.notify(id, notification.build())
    }

    /**
     * Get the small icon for this app

     * @return The small icon resource id
     */
    private val smallIcon: Int
        get() = android.R.drawable.stat_notify_chat


    /**
     * Get a random name string from resources to add personalization to the notification

     * @return A random name
     */
    val randomName: String
        get() {
            val names = applicationContext.resources.getStringArray(R.array.names_array)
            return names[Random().nextInt(names.size)]
        }
}