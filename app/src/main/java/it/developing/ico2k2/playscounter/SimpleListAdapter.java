package it.developing.ico2k2.playscounter;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleListAdapter extends BaseAdapter
{
    public static class DataHolder
    {
        private final String title,subtitle;

        public DataHolder(String title,String subtitle)
        {
            this.title = title;
            this.subtitle = subtitle;
        }

        public DataHolder(String title)
        {
            this(title,null);
        }

        public String getTitle()
        {
            return title;
        }

        public String getSubtitle()
        {
            return subtitle;
        }
    }

    private final ArrayList<DataHolder> data;
    private final ArrayList<Integer> indexes;
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
        if(capacity > 0)
        {
            data = new ArrayList<>(capacity);
            indexes = new ArrayList<>(capacity);
        }
        else
        {
            data = new ArrayList<>();
            indexes = new ArrayList<>();
        }
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
        add(new DataHolder(title,subtitle));
    }

    public void add(String title)
    {
        add(new DataHolder(title));
    }

    public void add(DataHolder item)
    {
        data.add(item);
        indexes.add(indexes.size());
    }

    public void addAll(Collection<? extends DataHolder> items)
    {
        data.addAll(items);
        for(DataHolder ignored: items)
            indexes.add(indexes.size());
    }

    public void addAll(DataHolder ... items)
    {
        addAll(Arrays.asList(items));
    }

    public void remove(int index)
    {
        data.remove(indexes.get(index));
        indexes.remove(index);
    }

    public void remove(DataHolder item)
    {
        indexes.remove((Integer)data.indexOf(item));
        data.remove(item);
    }

    public void clear()
    {
        data.clear();
        indexes.clear();
    }

    public void ensureCapacity(int capacity)
    {
        data.ensureCapacity(capacity);
        indexes.ensureCapacity(capacity);
    }

    public void sort(Comparator<Integer> comparator)
    {
        Collections.sort(indexes,comparator);
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public DataHolder getItem(int position){
        return data.get(indexes.get(position));
    }

    public DataHolder getRealItem(int position){
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
        TextView txt = (TextView)convertView.findViewById(title);
        if(txt != null)
            txt.setText(data.getTitle());
        txt = (TextView)convertView.findViewById(subtitle);
        if(txt != null)
        {
            if(data.getSubtitle() != null && showSubtitle)
            {
                txt.setText(data.getSubtitle());
                txt.setVisibility(View.VISIBLE);
            }
            else
                txt.setVisibility(View.GONE);
        }
        return convertView;
    }
}
