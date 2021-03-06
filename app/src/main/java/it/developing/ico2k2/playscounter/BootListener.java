package it.developing.ico2k2.playscounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class BootListener extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context,Intent intent)
    {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            startServices(context);
    }

    public static void startServices(Context context)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            startNotificationListener(context);
        ContextCompat.startForegroundService(context,new Intent(context,IntentListener.class));
    }

    public static void stopServices(Context context)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            stopNotificationListener(context);
        context.stopService(new Intent(context,IntentListener.class));
    }

    public static void restartNotificationListener(Context context)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            stopNotificationListener(context);
            startNotificationListener(context);
        }
    }

    private static void startNotificationListener(Context context)
    {
        ContextCompat.startForegroundService(context,new Intent(context,NotificationListener.class));
    }

    private static void stopNotificationListener(Context context)
    {
        context.stopService(new Intent(context,NotificationListener.class));
    }

}
