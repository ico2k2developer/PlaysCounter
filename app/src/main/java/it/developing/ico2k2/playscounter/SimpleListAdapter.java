package it.developing.ico2k2.playscounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimpleListAdapter extends BaseAdapter
{
    public static class DataHolder
    {
        private final String title;
        private final String subtitle;

        public DataHolder(String title,String subtitle)
        {
            this.title = title;
            this.subtitle = subtitle;
        }

        public DataHolder(String title)
        {
            this(title,null);
        }
    }

    private final ArrayList<DataHolder> data;
    private final LayoutInflater inflater;
    private final int layout;
    private final int title;
    private final int subtitle;
    private boolean showSubtitle;

    public SimpleListAdapter(int capacity,LayoutInflater inflater,@LayoutRes int layout,@IdRes int title,@IdRes int subtitle)
    {
        this.inflater = inflater;
        this.layout = layout;
        this.title = title;
        this.subtitle = subtitle;
        showSubtitle = true;
        data = capacity > 0 ? new ArrayList<>(capacity) : new ArrayList<>();
    }

    public SimpleListAdapter(int capacity,LayoutInflater inflater,@LayoutRes int layout,@IdRes int title)
    {
        this(capacity,inflater,layout,title,0);
        showSubtitle = false;
    }

    public SimpleListAdapter(LayoutInflater inflater,@LayoutRes int layout,@IdRes int title,@IdRes int subtitle)
    {
        this(0,inflater,layout,title,subtitle);
    }

    public SimpleListAdapter(LayoutInflater inflater,@LayoutRes int layout,@IdRes int title)
    {
        this(0,inflater,layout,title);
    }

    public void setShowSubtitle(boolean show)
    {
        showSubtitle = show;
    }

    public boolean getShowSubtitle()
    {
        return showSubtitle;
    }

    public void add(String title,String subtitle)
    {
        data.add(new DataHolder(title,subtitle));
    }

    public void add(String title)
    {
        data.add(new DataHolder(title));
    }

    public void add(DataHolder item)
    {
        data.add(item);
    }

    public void addAll(Collection<? extends DataHolder> items)
    {
        data.addAll(items);
    }

    public void addAll(DataHolder ... items )
    {
        data.addAll(Arrays.asList(items));
    }

    public void remove(int index)
    {
        data.remove(index);
    }

    public void remove(DataHolder item)
    {
        data.remove(item);
    }

    public void clear()
    {
        data.clear();
    }

    public void ensureCapacity(int capacity)
    {
        data.ensureCapacity(capacity);
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public DataHolder getItem(int position){
        return data.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = inflater.inflate(layout,parent,false);
        }
        DataHolder data = getItem(position);
        ((TextView)convertView.findViewById(title)).setText(data.title);
        if(data.subtitle != null && showSubtitle)
            ((TextView)convertView.findViewById(subtitle)).setText(data.subtitle);
        return convertView;
    }
}
