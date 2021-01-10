package it.developing.ico2k2.playscounter;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import java.util.List;

import it.developing.ico2k2.playscounter.database.Database;
import it.developing.ico2k2.playscounter.database.DatabaseClient;
import it.developing.ico2k2.playscounter.database.Song;
import it.developing.ico2k2.playscounter.database.SongDao;

import static it.developing.ico2k2.playscounter.Utils.DATABASE_SONGS;
import static it.developing.ico2k2.playscounter.Utils.examineBundle;
import static it.developing.ico2k2.playscounter.Utils.examineIntent;

public class IntentListener extends Service
{

    public static final String ACTION_NOTIFICATION = "it.developing.ico2k2.playcounter.notificationchanged";

    public static final String EXTRA_TITLE = "track";
    public static final String EXTRA_ARTIST = "artist";
    public static final String EXTRA_PLAYING = "playing";

    private static final String[] FILTERS =
    {
        "com.adam.aslfms.notify.playstatechanged",
        "com.amazon.mp3.metachanged",
        "com.amazon.mp3.playstatechanged",
        "com.andrew.apollo.metachanged",
        "com.android.music.metachanged",
        "com.android.music.playbackcomplete",
        "com.android.music.playstatechanged",
        "com.android.music.queuechanged",
        "com.doubleTwist.androidPlayer.metachanged",
        "com.doubleTwist.androidPlayer.playstatechanged",
        "com.e8tracks.metachanged",
        "com.e8tracks.playstatechanged",
        "com.htc.music.metachanged",
        "com.htc.music.playbackcomplete",
        "com.htc.music.playstatechanged",
        "com.jetappfactory.jetaudio.metachanged",
        "com.jetappfactory.jetaudio.playstatechanged",
        "com.jetappfactory.jetaudioplus.metachanged",
        "com.jetappfactory.jetaudioplus.playstatechanged",
        "com.jrtstudio.AnotherMusicPlayer.metachanged",
        "com.jrtstudio.AnotherMusicPlayer.playstatechanged",
        "com.jrtstudio.music.metachanged",
        "com.jrtstudio.music.playstatechanged",
        "com.lge.music.metachanged",
        "com.lge.music.playstatechanged",
        "com.maxmpz.audioplayer.playstatechanged",
        "com.miui.player.metachanged",
        "com.miui.player.playbackcomplete",
        "com.miui.player.playstatechanged",
        "com.neowiz.android.bugs.metachanged",
        "com.nullsoft.winamp.metachanged",
        "com.nullsoft.winamp.playstatechanged",
        "com.pantech.app.music.metachanged",
        "com.rdio.android.metachanged",
        "com.rdio.android.playstatechanged",
        "com.real.IMP.metachanged",
        "com.rhapsody.metachanged",
        "com.rhapsody.playstatechanged",
        "com.samsung.MusicPlayer.metachanged",
        "com.samsung.music.metachanged",
        "com.samsung.sec.android.MusicPlayer.metachanged",
        "com.samsung.sec.android.MusicPlayer.playstatechanged",
        "com.samsung.sec.metachanged",
        "com.sec.android.app.music.metachanged",
        "com.sec.android.app.music.playstatechanged",
        "com.sonyericsson.music.TRACK_COMPLETED",
        "com.sonyericsson.music.metachanged",
        "com.sonyericsson.music.playbackcomplete",
        "com.sonyericsson.music.playbackcontrol.ACTION_PAUSED",
        "com.sonyericsson.music.playbackcontrol.ACTION_PLAYBACK_PLAY",
        "com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED",
        "com.sonyericsson.music.playstatechanged",
        "com.soundcloud.android.metachanged",
        "com.soundcloud.android.playback.playcurrent",
        "com.spotify.music.metadatachanged",
        "com.spotify.music.playbackstatechanged",
        "com.tbig.playerpro.metachanged",
        "com.tbig.playerpro.playstatechanged",
        "com.tbig.playerprotrial.metachanged",
        "com.tbig.playerprotrial.playstatechanged",
        "fm.last.android.metachanged",
        "net.jjc1138.android.scrobbler.action.MUSIC_STATUS",
        "com.android.music.metachanged",
        "com.android.music.playstatechanged",
        "com.android.music.playbackcomplete",
        "com.andrew.apolloMod.metachanged",
        "com.ting.mp3.playinfo_changed",
        "com.ting.minibar.play",
        "com.htc.music.metachanged",
        "com.htc.music.playstatechanged",
        "com.htc.music.playbackcomplete",
        "fm.last.android.metachanged",
        "fm.last.android.playbackpaused",
        "fm.last.android.playbackcomplete",
        "com.spotify.mobile.android.metadatachanged",
        "com.spotify.mobile.android.playbackstatechanged",
        "com.spotify.mobile.android.queuechanged",
        "com.rdio.android.playstatechanged",
        "com.rdio.android.metachanged",
        "com.mixzing.music.metachanged",
        "com.mixzing.music.playstatechanged",
        "com.mixzing.music.playbackcomplete",
        "com.tbig.playerprotrial.metachanged",
        "com.tbig.playerprotrial.playstatechanged",
        "com.tbig.playerprotrial.playbackcomplete",
        "com.tbig.playerpro.metachanged",
        "com.tbig.playerpro.playstatechanged",
        "com.tbig.playerpro.playbackcomplete",
        "org.abrantix.rockon.rockonnggl.metachanged",
        "org.abrantix.rockon.rockonnggl.playstatechanged",
        "org.abrantix.rockon.rockonnggl.playbackcomplete",
        "com.jrtstudio.music.metachanged",
        "com.jrtstudio.music.playstatechanged",
        "com.jrtstudio.music.playbackcomplete",
        "com.nullsoft.winamp.metachanged",
        "com.nullsoft.winamp.playbackcomplete",
        "com.nullsoft.winamp.playstatechanged",
        "com.real.IMP.metachanged",
        "com.real.IMP.playbackcomplete",
        "com.real.IMP.playstatechanged",
        "com.real.RealPlayer.metachanged",
        "com.real.RealPlayer.playstatechanged",
        "com.real.RealPlayer.playbackcomplete",
        "com.sonyericsson.music.playbackcontrol.ACTION_PLAYBACK_PLAY",
        "com.sonyericsson.music.playbackcontrol.ACTION_PLAYBACK_PAUSE",
        "com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED",
        "com.sonyericsson.music.playbackcontrol.ACTION_TRACK_PAUSED",
        "com.sonyericsson.music.playbackcontrol.ACTION_STARTED",
        "com.sonyericsson.music.playbackcontrol.ACTION_PAUSED",
        "com.sonyericsson.music.TRACK_COMPLETED",
        "com.sonyericsson.music.metachanged",
        "com.sonyericsson.music.playbackcomplete",
        "com.sonyericsson.music.playstatechanged",
        "com.samsung.sec.android.MusicPlayer.metachanged",
        "com.samsung.sec.android.MusicPlayer.playbackcomplete",
        "com.samsung.sec.android.MusicPlayer.playstatechanged",
        "com.samsung.music.metachanged",
        "com.samsung.music.playbackcomplete",
        "com.samsung.music.playstatechanged",
        "com.samsung.sec.metachanged",
        "com.samsung.sec.playbackcomplete",
        "com.samsung.sec.playstatechanged",
        "com.samsung.sec.android.metachanged",
        "com.samsung.sec.android.playbackcomplete",
        "com.samsung.sec.android.playstatechanged",
        "com.samsung.MusicPlayer.metachanged",
        "com.samsung.MusicPlayer.playbackcomplete",
        "com.samsung.MusicPlayer.playstatechanged",
        "com.sec.android.app.music.metachanged",
        "com.sec.android.app.music.playbackcomplete",
        "com.sec.android.app.music.playstatechanged",
        "com.amazon.mp3.playstatechanged",
        "com.rhapsody.playstatechanged",
        "com.adam.aslfms.notify.metachanged",
        "com.adam.aslfms.notify.playstatechanged",
        "com.adam.aslfms.notify.playbackcomplete",
        "net.jjc1138.android.scrobbler.action.MUSIC_STATUS",
        "org.iii.romulus.meridian.metachanged",
        "org.iii.romulus.meridian.playstatechanged",
        "org.iii.romulus.meridian.playbackcomplete",
        "com.doubleTwist.androidPlayer.metachanged",
        "com.doubleTwist.androidPlayer.playstatechanged",
        "com.doubleTwist.androidPlayer.playbackcomplete",
        "com.miui.player.metachanged",
        "com.miui.player.playstatechanged",
        "com.miui.player.playbackcomplete",
        ACTION_NOTIFICATION,
    };

    private Receiver receiver;
    private Database database;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        manageNotification(true);
        database = DatabaseClient.getInstance(this,DATABASE_SONGS);
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        for(String action : FILTERS)
            filter.addAction(action);
        registerReceiver(receiver,filter);
    }

    class Receiver extends BroadcastReceiver
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(IntentListener.this);

        @Override
        public void onReceive(Context context,Intent intent){
            Log.d(getClass().getSimpleName(),Utils.examine(intent));
            SongDao dao = database.dao();
            String title = intent.getExtras().getString(EXTRA_TITLE,null);
            String artist = intent.getExtras().getString(EXTRA_ARTIST,null);
            boolean playing = intent.getExtras().getBoolean(EXTRA_PLAYING,false);
            if(title != null && artist != null && playing)
            {
                String songId = Song.generateId(title,artist);
                if(!songId.equals(prefs.getString(getString(R.string.key_song_last),"")))
                {
                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            List<Song> results = dao.findById(Song.generateId(title,artist));
                            Song song;
                            if(results.size() > 0)
                                song = results.get(0);
                            else
                                song = new Song(title,artist);
                            song.setPlaysCount(song.getPlaysCount() + 1);
                            dao.insertAll(song);
                        }
                    }).start();
                    prefs.edit().putString(getString(R.string.key_song_last),songId).apply();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void manageNotification(boolean show)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if(show)
                startForeground(ForegroundNotificationBuilder.getNotificationId(),ForegroundNotificationBuilder.getNotification(this));
            else
                stopForeground(true);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);

        manageNotification(false);
    }
}
