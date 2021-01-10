package it.developing.ico2k2.playscounter;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import static it.developing.ico2k2.playscounter.Utils.CHANNEL_ID_MAIN;

public class PlaysCounter extends Application
{
	@Override
	public void onCreate()
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			NotificationChannel channel;
			channel = new NotificationChannel(CHANNEL_ID_MAIN,getString(R.string.main_notification_channel),NotificationManager.IMPORTANCE_LOW);
			notificationManager.createNotificationChannel(channel);
		}
	}
}
