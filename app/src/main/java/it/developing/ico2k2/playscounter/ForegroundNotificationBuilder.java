package it.developing.ico2k2.playscounter;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ForegroundNotificationBuilder
{
	private static final byte NOTIFICATION_ID = (byte)0x11;

	public static final String EXTRA_ACTION = "action";

	public static final byte ACTION_STOP = 0x11;

	private static Notification notification;

	public static Notification getNotification(Context context) {

		if(notification == null) {

			notification = new NotificationCompat.Builder(context,Utils.CHANNEL_ID_MAIN)
					.setContentTitle(context.getString(R.string.notification_foreground))
					.setContentText(context.getString(R.string.notification_foreground_desc))
					.setContentIntent(PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0))
					.setSmallIcon(R.drawable.ic_not_foreground)
					.setCategory(NotificationCompat.CATEGORY_SERVICE)
					.addAction(0,context.getString(R.string.notification_foreground_act_close),
							PendingIntent.getService(context,0,new Intent(context,IntentListener.class).putExtra(
									EXTRA_ACTION,ACTION_STOP),0))
					.build();
		}

		return notification;
	}

	public static byte getNotificationId() {
		return NOTIFICATION_ID;
	}
}
