package it.developing.ico2k2.playscounter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class SimpleListAdapter extends BaseAdapter
{
    public static class DataHolder
    {
        private final String title,subtitle;
        private int id;

        public DataHolder(@Nullable String title,@Nullable String subtitle)
        {
            this.title = title;
            this.subtitle = subtitle;
        }

        public DataHolder(@Nullable String title)
        {
            this(title,null);
        }

        public DataHolder()
        {
            this(null,null);
        }

        public String getItemTitle()
        {
            return title;
        }

        public String getItemSubtitle()
        {
            return subtitle;
        }

        @Override
        public boolean equals(Object o)
        {
            boolean result = false;
            if(o != null)
            {
                if(o instanceof DataHolder)
                    result = id == ((DataHolder)o).id;
            }
            return result;
        }
    }

    private final ArrayList<DataHolder> data;
    private final ArrayList<Integer> indexes;
    private final Context context;
    private final LayoutInflater inflater;
    private final int layout;
    private final int title;
    private final int subtitle;
    private final int icon;
    private boolean showSubtitle,multiLineSubtitle;

    public SimpleListAdapter(Activity activity,int capacity,@LayoutRes int layout,@IdRes int title,@IdRes int subtitle,@IdRes int icon)
    {
        this.context = activity;
        this.inflater = activity.getLayoutInflater();
        this.layout = layout;
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
        showSubtitle = subtitle != 0;
        multiLineSubtitle = false;
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

    public SimpleListAdapter(Activity activity,int capacity,@LayoutRes int layout,@IdRes int title,@IdRes int subtitle)
    {
        this(activity,capacity,layout,title,subtitle,0);
    }

    public SimpleListAdapter(Activity activity,@LayoutRes int layout,@IdRes int title,@IdRes int subtitle)
    {
        this(activity,0,layout,title,subtitle);
    }

    public SimpleListAdapter(Activity activity,@LayoutRes int layout,@IdRes int title)
    {
        this(activity,layout,title,0);
    }

    public void setShowSubtitle(boolean show)
    {
        showSubtitle = show;
    }

    public boolean getShowSubtitle()
    {
        return showSubtitle;
    }

    public void setMultiLineSubtitle(boolean multiLine)
    {
        multiLineSubtitle = multiLine;
    }

    public boolean getMultiLineSubtitle()
    {
        return multiLineSubtitle;
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
        item.id = data.size();
        data.add(item);
        indexes.add(item.id);
        printSizes();
    }

    public void addAll(Collection<? extends DataHolder> items)
    {
        /*data.addAll(items);
        for(DataHolder ignored: items)
            indexes.add(indexes.size());*/
        for(DataHolder d : items)
            add(d);
    }

    public void addAll(DataHolder ... items)
    {
        addAll(Arrays.asList(items));
    }

    public void remove(int index)
    {
        data.remove((int)indexes.get(index));
        indexes.remove(index);
        printSizes();
    }

    public void remove(DataHolder item)
    {
        int index = data.indexOf(item);
        indexes.remove((Integer)index);
        data.remove(index);
        printSizes();
    }

    private void printSizes()
    {
        Log.d(getClass().getSimpleName(),"Data array size is " + data.size() + ", indexes array size is " + indexes.size());
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
        printSizes();
        Log.d(getClass().getSimpleName(),Arrays.toString(indexes.toArray()));
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
        return getItem(position).id;
    }

    public int getItemPosition(int id)
    {
        DataHolder d = new DataHolder();
        d.id = id;
        return indexes.indexOf(data.indexOf(d));
    }

    public DataHolder getItemById(int id){
        DataHolder d = new DataHolder();
        d.id = id;
        return getItem(data.indexOf(d));
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        if(convertView == null)
            convertView = inflater.inflate(layout,parent,false);
        DataHolder data = getItem(position);
        TextView txt = (TextView)convertView.findViewById(title);
        if(txt != null)
            txt.setText(data.id + data.getItemTitle());
        if(getShowSubtitle())
        {
            txt = (TextView)convertView.findViewById(subtitle);
            if(txt != null)
            {
                if(data.getItemSubtitle() != null)
                {
                    txt.setText(data.getItemSubtitle());
                    txt.setVisibility(View.VISIBLE);
                    txt.setSingleLine(!getMultiLineSubtitle());
                }
                else
                    txt.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
}
