package it.developing.ico2k2.playscounter;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.List;

import it.developing.ico2k2.playscounter.database.Database;
import it.developing.ico2k2.playscounter.database.DatabaseClient;
import it.developing.ico2k2.playscounter.database.Song;
import it.developing.ico2k2.playscounter.database.SongDao;

import static it.developing.ico2k2.playscounter.ForegroundNotificationBuilder.ACTION_STOP;
import static it.developing.ico2k2.playscounter.ForegroundNotificationBuilder.EXTRA_ACTION;

public class IntentListener extends Service
{
    public static final String ACTION_PLAYSTATE_CHANGED = "com.android.music.playstatechanged";

    public static final String EXTRA_TITLE = "track";
    public static final String EXTRA_ARTIST = "artist";
    public static final String EXTRA_PLAYING = "playing";
    public static final String EXTRA_LENGTH = "trackLength";

    private static final String[] FILTERS =
    {
        "com.adam.aslfms.notify.playstatechanged",
        "com.amazon.mp3.metachanged",
        "com.amazon.mp3.playstatechanged",
        "com.andrew.apollo.metachanged",
        "com.android.music.metachanged",
        "com.android.music.playbackcomplete",
        ACTION_PLAYSTATE_CHANGED,
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
    };

    private Receiver receiver;
    private String lastSongId;
    private Database database;
    private OnUpdateListener listener;
    private final IBinder binder = new LocalBinder();

    public interface OnUpdateListener
    {
        public void onUpdate(String currentSongId);
    }
    protected void sendUpdate()
    {
        if(listener != null)
            listener.onUpdate(lastSongId);
    }

    protected void setOnUpdateListener(@Nullable OnUpdateListener listener)
    {
        this.listener = listener;
    }

    public class LocalBinder extends Binder {
        IntentListener getService() {
            return IntentListener.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        manageNotification(true);
        database = DatabaseClient.getInstance(this,DatabaseClient.DATABASE_SONGS);
        receiver = new Receiver();
        lastSongId = null;
        IntentFilter filter = new IntentFilter();
        for(String action : FILTERS)
            filter.addAction(action);
        registerReceiver(receiver,filter);
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent != null)
        {
            if(intent.getExtras() != null)
                receiver.onReceive(this,intent);
        }
        return super.onStartCommand(intent,flags,startId);
    }

    class Receiver extends BroadcastReceiver
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(IntentListener.this);
        boolean waitingForLength = false;
        @Override
        public void onReceive(Context context,Intent intent)
        {
            Bundle extras = intent.getExtras();
            Log.d(getClass().getSimpleName(),extras.getString(EXTRA_TITLE,"null") + ", " +
                    extras.getString(EXTRA_ARTIST,"null"));
            if(extras.containsKey(EXTRA_ACTION))
            {
                switch(extras.getByte(EXTRA_ACTION))
                {
                    case ACTION_STOP:
                    {
                        BootListener.stopServices(context);
                        break;
                    }
                }
            }
            else
            {
                String title = extras.getString(EXTRA_TITLE,null);
                String artist = extras.getString(EXTRA_ARTIST,null);
                //if(title != null && artist != null && extras.getBoolean(EXTRA_PLAYING,false))
                if(title != null && artist != null)
                    updateDatabase(title,artist);
            }
        }

        private void updateDatabase(String title,String artist)
        {
            SongDao dao = database.dao();
            String songId = Song.generateId(title,artist);
            if(!songId.equals(prefs.getString(getString(R.string.key_song_last),""))){
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        List<Song> results = dao.findById(songId);
                        Song song;
                        if(results.size() > 0)
                        {
                            song = results.get(0);
                            song.updateLastPlayDate();
                        }
                        else
                            song = new Song(title,artist,0,new Song.Date());
                        lastSongId = song.getId();
                        sendUpdate();
                        song.setPlaysCount(song.getPlaysCount() + 1);
                        dao.insertAll(song);
                        Log.d(getClass().getSimpleName(),"Saved new play for song " + song.toString());
                    }
                }).start();
                prefs.edit().putString(getString(R.string.key_song_last),songId).apply();
            }
            else Log.d(getClass().getSimpleName(),"Song already playing!");

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
