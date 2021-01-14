package it.developing.ico2k2.playscounter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import java.util.Date;

import it.developing.ico2k2.playscounter.database.Song;

public class SongListAdapter extends SimpleListAdapter
{
    public static class SongHolder extends SimpleListAdapter.DataHolder
    {
        private final String duration;

        public SongHolder(@Nullable String title,@Nullable String artist,@Nullable String duration)
        {
            super(title,artist);
            this.duration = duration;
        }

        public SongHolder(@Nullable String title,@Nullable String duration)
        {
            this(title,null,duration);
        }

        public SongHolder(@Nullable String title)
        {
            this(title,null);
        }

        public SongHolder()
        {
            this(null);
        }

        public String getTitle()
        {
            return super.getItemTitle();
        }

        public String getArtist()
        {
            return super.getItemSubtitle();
        }

        public String getDuration()
        {
            return duration;
        }
    }

    private final int duration;

    public SongListAdapter(int capacity,LayoutInflater inflater,@LayoutRes int layout,
                           @IdRes int title,@IdRes int subtitle,@IdRes int duration)
    {
        super(capacity,inflater,layout,title,subtitle);
        this.duration = duration;
    }

    public SongListAdapter(LayoutInflater inflater,@LayoutRes int layout,
                           @IdRes int title,@IdRes int subtitle,@IdRes int duration)
    {
        this(0,inflater,layout,title,subtitle,duration);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        View view = super.getView(position,convertView,parent);
        DataHolder h = getItem(position);
        if(h instanceof SongHolder)
        {
            SongHolder holder = (SongHolder)h;
            TextView txt = (TextView)view.findViewById(duration);
            if(txt != null)
            {
                if(holder.getDuration() != null)
                {
                    txt.setText(holder.getDuration());
                    txt.setVisibility(View.VISIBLE);
                }
                else
                    txt.setVisibility(View.GONE);
            }
        }
        return view;
    }
}
