package it.developing.ico2k2.playscounter;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ForegroundNotificationBuilder
{
	private static final int NOTIFICATION_ID = 0xAA;
	private static Notification notification;

	public static Notification getNotification(Context context) {

		if(notification == null) {

			notification = new NotificationCompat.Builder(context,Utils.CHANNEL_ID_MAIN)
					.setContentTitle(context.getString(R.string.notification_foreground))
					.setContentText(context.getString(R.string.notification_foreground_desc))
					.setContentIntent(PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0))
					.setSmallIcon(R.drawable.ic_not_foreground)
					.setCategory(NotificationCompat.CATEGORY_SERVICE)
					.build();
		}

		return notification;
	}

	public static int getNotificationId() {
		return NOTIFICATION_ID;
	}
}
