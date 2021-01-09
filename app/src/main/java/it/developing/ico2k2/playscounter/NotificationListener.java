package it.developing.ico2k2.playscounter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.developing.ico2k2.playscounter.database.Database;
import it.developing.ico2k2.playscounter.database.DatabaseClient;
import it.developing.ico2k2.playscounter.database.Song;
import it.developing.ico2k2.playscounter.database.SongDao;

import static it.developing.ico2k2.playscounter.IntentListener.ACTION_NOTIFICATION;
import static it.developing.ico2k2.playscounter.IntentListener.EXTRA_ARTIST;
import static it.developing.ico2k2.playscounter.IntentListener.EXTRA_TITLE;
import static it.developing.ico2k2.playscounter.Utils.examineBundle;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class NotificationListener extends NotificationListenerService
{
    private static final String[] FILTER =
    {
        "com.sec.android.app.music",
        "com.spotify.music",
        "com.google.android.apps.youtube.music",
        "deezer.android.app",
        "com.soundcloud.android",
        "com.melodis.midomiMusicIdentifier",
    };

    private List<String> packages;
    private Database database;

    @Override
    public void onCreate()
    {
        super.onCreate();
        packages = new ArrayList<>(Arrays.asList(FILTER));
        database = DatabaseClient.getInstance(this,"database");
        Log.d(getClass().getName(),"Created");
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        if(packages.contains(sbn.getPackageName()))
        {
            Intent i = new Intent(ACTION_NOTIFICATION);
            i.putExtra(EXTRA_TITLE,sbn.getNotification().extras.getString("android.title"));
            i.putExtra(EXTRA_ARTIST,sbn.getNotification().extras.getString("android.text"));
            sendBroadcast(i);
        }
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
