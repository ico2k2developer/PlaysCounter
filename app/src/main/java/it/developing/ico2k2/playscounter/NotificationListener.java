package it.developing.ico2k2.playscounter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.developing.ico2k2.playscounter.IntentListener.EXTRA_ARTIST;
import static it.developing.ico2k2.playscounter.IntentListener.EXTRA_TITLE;

@RequiresApi(Build.VERSION_CODES.KITKAT)
public class NotificationListener extends NotificationListenerService
{
    private static final String PACKAGE_SAMSUNG = "com.sec.android.app.music";

    private static final String[] FILTER =
    {
        PACKAGE_SAMSUNG,
        "com.spotify.music",
        "com.google.android.apps.youtube.music",
        "deezer.android.app",
        "com.soundcloud.android",
        "com.melodis.midomiMusicIdentifier",
    };

    private List<String> packages;
    private NotificationCompat notification;

    public static boolean isNotificationAccessEnabled = false;

    @Override
    public void onListenerConnected() {
        isNotificationAccessEnabled = true;
    }

    @Override
    public void onListenerDisconnected() {
        isNotificationAccessEnabled = false;
        stopSelf();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        manageNotification(true);
        packages = new ArrayList<>(Arrays.asList(FILTER));
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        if(packages.contains(sbn.getPackageName()))
        {
            Log.d(getClass().getSimpleName(),Utils.examine(sbn.getNotification().extras));
            Intent i = new Intent(this,IntentListener.Receiver.class);
            i.putExtra(EXTRA_TITLE,sbn.getNotification().extras.get("android.title").toString());
            i.putExtra(EXTRA_ARTIST,sbn.getNotification().extras.get("android.text").toString());
            sendBroadcast(i);
        }
    }

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

    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
