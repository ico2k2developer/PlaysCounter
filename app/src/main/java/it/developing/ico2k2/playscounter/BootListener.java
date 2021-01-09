package it.developing.ico2k2.playscounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class BootListener extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context,Intent intent)
    {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                context.startService(new Intent(context,NotificationListener.class));
            context.startService(new Intent(context,IntentListener.class));
            Toast.makeText(context,"Launching services...",Toast.LENGTH_SHORT).show();
        }
    }
}
