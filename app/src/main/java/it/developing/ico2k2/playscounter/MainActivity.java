package it.developing.ico2k2.playscounter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

/*
AudioManager.isMusicActive();


 */

public class MainActivity extends Activity implements BroadcastReceiver
{
    public static final String[] filters =
    {
        "com.android.music.metachanged",
        "com.android.music.playstatechanged",
        "com.android.music.playbackcomplete",
        "com.android.music.queuechanged",

        "com.sonyericsson.music.metachanged",
        "com.sonyericsson.music.playbackcontrol.ACTION_PLAYBACK_PLAY",
        "com.sonyericsson.music.TRACK_COMPLETED",
        "com.sonyericsson.music.playbackcomplete",
        "com.sonyericsson.music.playstatechanged",
        "com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED",
        "com.sonyericsson.music.playbackcontrol.ACTION_PAUSED",

        "com.miui.player.metachanged",
        "com.htc.music.metachanged",
        "com.nullsoft.winamp.metachanged",
        "com.real.IMP.metachanged",
        "fm.last.android.metachanged",
        "com.sec.android.app.music.metachanged",
        "com.amazon.mp3.metachanged",
        "com.real.IMP.metachanged",
        "com.rdio.android.metachanged",
        "com.andrew.apollo.metachanged",
        "com.lge.music.metachanged",
        "com.pantech.app.music.metachanged",
        "com.neowiz.android.bugs.metachanged",
        "com.soundcloud.android.metachanged",
        "com.soundcloud.android.playback.playcurrent",
        "com.samsung.sec.android.MusicPlayer.metachanged",
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        setContentView(textView);
        textView.setSingleLine(false);
        IntentFilter filter = new IntentFilter()
        registerReceiver(this,filters);
    }

    @Override
    public void onReceive(Context context,Intent intent)
    {

    }
}
