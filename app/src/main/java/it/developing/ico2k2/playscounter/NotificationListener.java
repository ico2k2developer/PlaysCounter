package it.developing.ico2k2.playscounter;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.developing.ico2k2.playscounter.IntentListener.ACTION_PLAYSTATE_CHANGED;
import static it.developing.ico2k2.playscounter.IntentListener.EXTRA_ARTIST;
import static it.developing.ico2k2.playscounter.IntentListener.EXTRA_PLAYING;
import static it.developing.ico2k2.playscounter.IntentListener.EXTRA_TITLE;

@RequiresApi(Build.VERSION_CODES.KITKAT)
public class NotificationListener extends NotificationListenerService
{
    private static final String PACKAGE_SAMSUNG = "com.sec.android.app.music";
    private static final String PACKAGE_SPOTIFY = "com.spotify.music";

    private static final String[] FILTER =
    {
        PACKAGE_SAMSUNG,
        PACKAGE_SPOTIFY,
        "com.google.android.apps.youtube.music",
        "deezer.android.app",
        "com.soundcloud.android",
        "com.melodis.midomiMusicIdentifier",
    };

    private List<String> packages;

    public static boolean isPermissionGranted(Context context)
    {
        ComponentName cn = new ComponentName(context,NotificationListener.class);
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        boolean result = flat != null && flat.contains(cn.flattenToString());
        Log.d(NotificationListener.class.getSimpleName(),"Permission granted: " + result);
        return result;
    }

    @Override
    public void onListenerConnected() {
        Log.d(getClass().getSimpleName(),"Listener connected");
    }

    @Override
    public void onListenerDisconnected() {
        Log.d(getClass().getSimpleName(),"Listener disconnected");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        manageNotification(true);
        packages = new ArrayList<>(Arrays.asList(FILTER));
        Log.d(getClass().getSimpleName(),"Listener created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getClass().getSimpleName(),"Listener started");
        return super.onStartCommand(intent,flags,startId);
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        if(packages.contains(sbn.getPackageName()))
        {
            Log.d(getClass().getSimpleName(),Utils.examine(sbn.getNotification().extras));
            Log.d(getClass().getSimpleName(),Utils.examine(sbn.getNotification().actions));
            Log.d(getClass().getSimpleName(),getSongTitle(sbn) + ", " + getSongArtist(sbn));
            Intent i = new Intent(ACTION_PLAYSTATE_CHANGED);
            i.putExtra(EXTRA_TITLE,getSongTitle(sbn));
            i.putExtra(EXTRA_ARTIST,getSongArtist(sbn));
            //i.putExtra(EXTRA_PLAYING,getPlaying(sbn.getPackageName(),sbn.getNotification()));
            sendBroadcast(i);
        }
    }

    private String getSongTitle(StatusBarNotification notification)
    {
        return notification.getNotification().extras.get("android.title").toString();
    }
    private String getSongArtist(StatusBarNotification notification)
    {
        return notification.getNotification().extras.get("android.text").toString();
    }

    /*private static final int PLAY_SAMSUNG = 2131230952;
    private static final int PLAY_SPOTIFY = 2131231605;

    private static boolean getPlaying(String packageName,Notification notification)
    {
        boolean result = true;
        switch(packageName)
        {
            case PACKAGE_SAMSUNG:
            {
                result = notification.actions[1].icon == PLAY_SAMSUNG;
                break;
            }
            case PACKAGE_SPOTIFY:
            {
                result = notification.actions[2].icon == PLAY_SPOTIFY;
                break;
            }
        }
        return result;
    }*/

    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void manageNotification(boolean show)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if(show)
                startForeground(ForegroundNotificationBuilder.getNotificationId(),ForegroundNotificationBuilder.getNotification(this));
            else
                stopForeground(false);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();


        manageNotification(false);
        Log.d(getClass().getSimpleName(),"Listener destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(getClass().getSimpleName(),"Listener bound");
        return super.onBind(intent);
    }
}
