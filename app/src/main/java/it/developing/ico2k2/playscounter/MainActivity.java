package it.developing.ico2k2.playscounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.preference.PreferenceManager;

import java.util.Comparator;
import java.util.List;

import it.developing.ico2k2.playscounter.database.Database;
import it.developing.ico2k2.playscounter.database.DatabaseClient;
import it.developing.ico2k2.playscounter.database.Song;
import it.developing.ico2k2.playscounter.database.SongDao;

import static it.developing.ico2k2.playscounter.Utils.DATABASE_SONGS;

public class MainActivity extends Activity
{
    private SimpleListAdapter adapter;
    private Database database;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        list = new ListView(this);
        list.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(list);
        adapter = new SimpleListAdapter(getLayoutInflater(),
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                android.R.id.text2);
        list.setAdapter(adapter);



        database = DatabaseClient.getInstance(this,DATABASE_SONGS);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            if(!NotificationListener.isNotificationAccessEnabled && !prefs.getBoolean(getString(R.string.key_permission_message_show),false))
                showSettingsPage(prefs);
        }
    }

    class SongItem extends SimpleListAdapter.DataHolder
    {
        private final int playsCount;

        public SongItem(String title,String artist,int playsCount)
        {
            super(title,artist);
            this.playsCount = playsCount;
        }

        public SongItem(Song song)
        {
            this(song.getTitle(),song.getArtist(),song.getPlaysCount());
        }

        public SongItem(String title,String artist)
        {
            this(title,artist,0);
        }

        @Override
        public String getTitle()
        {
            return String.format(getString(R.string.song_desc),super.getTitle(),super.getSubtitle());
        }

        @Override
        public String getSubtitle()
        {
            return String.format(getString(playsCount > 1 ? R.string.song_desc_sub_p : R.string.song_desc_sub),playsCount);
        }

        public int getPlaysCount()
        {
            return playsCount;
        }
    }

    private void refresh()
    {
        new Thread(new Runnable(){
            @Override
            public void run(){
                adapter.clear();
                SongDao dao = database.dao();
                List<Song> list = dao.getAll();
                for(Song song : list)
                    adapter.add(new SongItem(song));
                if(adapter.isEmpty())
                    adapter.add("Empty list","there is no items");
                else
                    adapter.sort(new Comparator<Integer>(){
                        @Override public int compare(Integer i1,Integer i2)
                        {
                            return ((SongItem)adapter.getRealItem(i2)).getPlaysCount() - ((SongItem)adapter.getRealItem(i1)).getPlaysCount();
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

    private void clear()
    {
        new Thread(new Runnable(){
            @Override
            public void run(){
                adapter.clear();
                database.dao().deleteAll();
                MainActivity.this.list.post(new Runnable(){
                    @Override
                    public void run(){
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        BootListener.startServices(this);
        refresh();
    }

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
        dialog.setNeutralButton(R.string.ok,new DialogInterface.OnClickListener(){
            @Override public void onClick(DialogInterface dialogInterface,int i)
            {

            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && NotificationListener.isNotificationAccessEnabled)
        {
            int i;
            for(i = 0; i < menu.size(); i++)
            {
                if(menu.getItem(i).getItemId() == R.id.menu_permission)
                {
                    menu.removeItem(i);
                }
            }
        }
        return true;
    }

    @Override
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
                refresh();
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
