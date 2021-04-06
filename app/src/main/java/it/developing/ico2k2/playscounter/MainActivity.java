package it.developing.ico2k2.playscounter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.developing.ico2k2.playscounter.database.Database;
import it.developing.ico2k2.playscounter.database.DatabaseClient;
import it.developing.ico2k2.playscounter.database.Song;
import it.developing.ico2k2.playscounter.database.SongDao;

import static it.developing.ico2k2.playscounter.database.DatabaseClient.DATABASE_SONGS;

public class MainActivity extends BaseActivity
{
    private SimpleListAdapter adapter;
    private Database database;
    private ListView list;
    private Menu menu;
    IntentListener service = null;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ((IntentListener.LocalBinder)iBinder).getService().setOnUpdateListener(new IntentListener.OnUpdateListener() {
                @Override
                public void onUpdate(String currentSongId) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            update(currentSongId);
                        }
                    });
                }
            });
            // now you have the instance of service.
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(service != null)
            service.setOnUpdateListener(null);
        unbindService(serviceConnection);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        list = new ListView(this);
        list.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(list);
        //list.setSelector(R.color.colorPrimaryDark);
        adapter = new SimpleListAdapter(MainActivity.this,
                android.R.layout.simple_list_item_activated_2,
                android.R.id.text1,
                android.R.id.text2);
        adapter.setMultiLineSubtitle(true);
        list.setAdapter(adapter);

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener(){
            private ArrayList<Integer> ids;
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,int position,long id,boolean checked)
            {
                if(list.getCheckedItemCount() > 1)
                    mode.setTitle(String.format(Locale.getDefault(),getString(R.string.items_selected),list.getCheckedItemCount()));
                else
                    mode.setTitle(R.string.item_selected);
                if(checked)
                    ids.add((Integer)(int)id);
                else
                    ids.remove((Integer)(int)id);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode,Menu menu){
                ids = new ArrayList<>();
                getMenuInflater().inflate(R.menu.menu_action_main,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode,Menu menu)
            {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode,MenuItem item){
                boolean result = true;
                switch(item.getItemId())
                {
                    case R.id.menu_delete:
                    {
                        databaseUpdate(UpdateType.DELETE, ids.toArray());
                        mode.finish();
                        break;
                    }
                    default:
                    {
                        result = false;
                    }
                }
                return result;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode){
                Log.d(MainActivity.class.getSimpleName(),"List: onDestroyActionMode");

            }
        });

        database = DatabaseClient.getInstance(this,DATABASE_SONGS);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if(!NotificationListener.isPermissionGranted(this) &&
                    !prefs.getBoolean(getString(R.string.key_permission_message_show),false))
                showSettingsPage(prefs);
        }

        startService(new Intent(this, IntentListener.class));
        bindService(new Intent(this,IntentListener.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    class SongItem extends SimpleListAdapter.DataHolder
    {
        private final int playsCount;
        private final String date;

        public SongItem(String title,String artist,int playsCount,@Nullable Date date)
        {
            super(title,artist);
            this.playsCount = playsCount;
            this.date = date == null ? null : String.format(getString(R.string.date_time),
                    android.text.format.DateFormat.getDateFormat(MainActivity.this)
                            .format(date),
                    android.text.format.DateFormat.getTimeFormat(MainActivity.this)
                            .format(date));
        }

        public SongItem(String title,String artist,int playsCount,@Nullable Song.Date date)
        {
            this(title,artist,playsCount,date == null ? null : date.toDate());
        }

        public SongItem(String title,String artist,int playsCount)
        {
            this(title,artist,playsCount,(Date)null);
        }

        public SongItem(Song song)
        {
            this(song.getTitle(),song.getArtist(),song.getPlaysCount(),song.getLastPlay());
        }

        public SongItem(String title,String artist)
        {
            this(title,artist,0);
        }

        public String generateId()
        {
            return Song.generateId(getTitle(),getArtist());
        }

        @Override
        public String getItemTitle()
        {
            return String.format(getString(R.string.song_desc),getTitle(),getArtist());
        }

        @Override
        public String getItemSubtitle()
        {
            return String.format(
                    getString(playsCount > 1 ? R.string.song_desc_sub_p : R.string.song_desc_sub),
                    playsCount,date);
        }

        public String getTitle()
        {
            return super.getItemTitle();
        }

        public String getArtist()
        {
            return super.getItemSubtitle();
        }

        public int getPlaysCount()
        {
            return playsCount;
        }
    }

    private enum UpdateType
    {
        REFRESH,
        DELETE,
        DELETEALL,
    }

    private void update()
    {
        if(service != null)
            service.askUpdate();
        else
            databaseUpdate(UpdateType.REFRESH);
    }

    private void update(@Nullable String currentSongId)
    {
        databaseUpdate(UpdateType.REFRESH,currentSongId);
    }

    private void resetTitleSubtitle()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                getActionBar().setTitle(R.string.app_name);
                getActionBar().setSubtitle(null);
            }
        });
    }

    private void setTitleSubtitle(CharSequence title,CharSequence subtitle)
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                getActionBar().setTitle(title);
                getActionBar().setSubtitle(subtitle);
            }
        });
    }

    private void databaseUpdate(UpdateType type,Object ... arguments)
    {
        synchronized(adapter)
        {
            new Thread(new Runnable(){
                @Override
                public void run(){
                    SongDao dao = database.dao();
                    switch(type)
                    {
                        case REFRESH:
                        {
                            adapter.clear();
                            List<Song> list = dao.getAll();
                            if(arguments.length > 0)
                            {
                                if(arguments[0] == null)
                                {
                                    for(Song song : list)
                                    {
                                        adapter.add(new SongItem(song));
                                    }
                                    resetTitleSubtitle();
                                }
                                else
                                {
                                    String title = null;
                                    for(Song song : list)
                                    {
                                        adapter.add(new SongItem(song));
                                        if(song.getId().equals((String)arguments[0]))
                                            title = song.getTitle();
                                    }
                                    if(title == null)
                                        resetTitleSubtitle();
                                    else
                                        setTitleSubtitle(title,getString(R.string.currently_playing));
                                }
                            }
                            else
                            {
                                for(Song song : list)
                                {
                                    adapter.add(new SongItem(song));
                                }
                            }
                            break;
                        }
                        case DELETEALL:
                        {
                            adapter.clear();
                            database.dao().deleteAll();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit()
                                    .remove(getString(R.string.key_song_last)).apply();
                            break;
                        }
                        case DELETE:
                        {
                            SongItem item;
                            for(Object o : arguments)
                            {
                                if(o instanceof Integer)
                                {
                                    Log.d(MainActivity.class.getSimpleName(),
                                            "Looking for item " + ((Integer)o));
                                    item = (SongItem)adapter.getItemById((Integer)o);
                                    Log.d(MainActivity.class.getSimpleName(),
                                            "Found item " + item.getTitle());
                                    adapter.remove(item);
                                    dao.delete(item.generateId());
                                }
                                else
                                    break;
                            }
                            break;
                        }
                    }
                    if(!adapter.isEmpty())
                        adapter.sort(new Comparator<Integer>(){
                            @Override public int compare(Integer i1,Integer i2)
                            {
                                Log.d(MainActivity.class.getSimpleName(),
                                        "Confronting item " + i1 + " with item " + i2);
                                Log.d(MainActivity.class.getSimpleName(),
                                        "Confronting item " +
                                        adapter.getRealItem(i1).getItemTitle() + " with item " +
                                        adapter.getRealItem(i2).getItemTitle());
                                return ((SongItem)adapter.getItem(i2)).getPlaysCount() - ((SongItem)adapter.getItem(i1)).getPlaysCount();
                            }
                        });
                    MainActivity.this.list.post(new Runnable(){
                        @Override
                        public void run(){
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
            }).start();
        }
    }

    private void clear()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        {
            dialog.setMessage(R.string.db_clear_message);
            dialog.setNegativeButton(R.string.ok,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){
                    databaseUpdate(UpdateType.DELETEALL);
                }
            });
            dialog.setPositiveButton(R.string.cancel,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){

                }
            });
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        BootListener.startServices(this);
        update();
        checkMenu();
    }

    @RequiresApi(19)
    private void showSettingsPage(SharedPreferences prefs)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.permission_notification_message);
        dialog.setPositiveButton(R.string.permission_notification_show_settings,new DialogInterface.OnClickListener(){
            @Override public void onClick(DialogInterface dialogInterface,int i)
            {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                else
                    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        });
        dialog.setNegativeButton(R.string.show_never_again,new DialogInterface.OnClickListener(){
            @Override public void onClick(DialogInterface dialogInterface,int i)
            {
                prefs.edit().putBoolean(getString(R.string.key_permission_message_show),true).apply();
            }
        });
        dialog.setNeutralButton(R.string.not_now,new DialogInterface.OnClickListener(){
            @Override public void onClick(DialogInterface dialogInterface,int i)
            {

            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @TargetApi(19)
    private void checkMenu()
    {
        if(menu != null)
        {
            boolean connected = NotificationListener.isPermissionGranted(this);
            if(menu.findItem(R.id.menu_permission) != null)
            {
                if(connected)
                {
                    Log.d(getClass().getSimpleName(),"Found menu item, removing & restart service");
                    menu.removeItem(R.id.menu_permission);
                    BootListener.restartNotificationListener(this);
                }
            }
            else
            {
                if(!connected)
                    menu.add(Menu.NONE,R.id.menu_permission,90,R.string.permission_notification_menu);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            this.menu = menu;
            checkMenu();
        }
        return true;
    }

    @Override
    @TargetApi(19)
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean result = true;

        switch(id)
        {
            case R.id.menu_update:
            {
                update();
                break;
            }
            case R.id.menu_clear:
            {
                clear();
                break;
            }
            case R.id.menu_permission:
            {
                showSettingsPage(PreferenceManager.getDefaultSharedPreferences(this));
                break;
            }
            default:
            {
                result = super.onOptionsItemSelected(item);
            }
        }
        return result;
    }
}
