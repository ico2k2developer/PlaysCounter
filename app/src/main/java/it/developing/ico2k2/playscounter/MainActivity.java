package it.developing.ico2k2.playscounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import it.developing.ico2k2.playscounter.database.Database;
import it.developing.ico2k2.playscounter.database.DatabaseClient;
import it.developing.ico2k2.playscounter.database.Song;
import it.developing.ico2k2.playscounter.database.SongDao;

/*
AudioManager.isMusicActive();


 */

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


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            startService(new Intent(this,NotificationListener.class));
        startService(new Intent(this,IntentListener.class));

        database = DatabaseClient.getInstance(this,"database");
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
                    adapter.add(String.format(getString(R.string.song_desc),song.getTitle(),song.getArtist()),Integer.toString(song.getPlaysCount()));
                if(adapter.isEmpty())
                    adapter.add("Empty list","there is no items");
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
                database.dao().deleteAll();

            }
        }).start();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
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
            default:
            {
                result = super.onOptionsItemSelected(item);
            }
        }
        return result;
    }
}
