package it.developing.ico2k2.playscounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            ContextCompat.startForegroundService(context,new Intent(context,NotificationListener.class));
        ContextCompat.startForegroundService(context,new Intent(context,IntentListener.class));
    }
}
