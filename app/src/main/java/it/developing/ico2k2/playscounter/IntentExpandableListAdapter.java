package it.developing.ico2k2.playscounter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class IntentExpandableListAdapter extends BaseExpandableListAdapter
{
    private enum Childs
    {
        ACTION,
        CATEGORIES,
        CLIP_DATA,
        COMPONENT,
        DATA,
        FLAGS,
        PACKAGE,
        SCHEME,
        TYPE,
        EXTRAS,
    }

    private Context context;
    private List<Intent> intents;
    private LayoutInflater inflater;

    public IntentExpandableListAdapter(Context context)
    {
        this.context = context;
        intents = new ArrayList<>();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Intent intent)
    {
        intents.add(intent);
    }

    public void addAll(Collection<? extends Intent> collection)
    {
        intents.addAll(collection);
    }

    public void remove(Intent intent)
    {
        intents.remove(intent);
    }

    public void clear()
    {
        intents.clear();
    }

    @Override
    public int getGroupCount() {
        return intents.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Childs.values().length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return intents.get(getGroupCount() - groupPosition - 1);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        String n = context.getString(R.string.null_);
        StringBuilder s = new StringBuilder();
        Intent intent = (Intent)getGroup(groupPosition);
        switch(Childs.values()[childPosition])
        {
            case ACTION:
            {
                s.append(intent.getAction());
                break;
            }
            case CATEGORIES:
            {
                Set<String> set = intent.getCategories();
                s.append(String.format(context.getString(R.string.categories_count),
                        set == null ? 0 : set.size()));
                if(intent.getCategories() != null)
                {
                    s.append("\n");
                    for(String c : intent.getCategories())
                    {
                        s.append("\n");
                        s.append(c);
                    }
                }
                break;
            }
            case CLIP_DATA:
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    s.append((intent.getClipData() == null ? n : intent.getClipData().toString()));
                else
                    s.append(context.getString(R.string.not_supported));
                break;
            }
            case COMPONENT:
            {
                s.append(intent.getComponent() == null ? n : intent.getComponent().toString());
                break;
            }
            case DATA:
            {
                s.append(intent.getDataString());
                break;
            }
            case FLAGS:
            {
                s.append(intent.getFlags());
                s.append(" (0x");
                s.append(Integer.toHexString(intent.getFlags()));
                s.append(")");
                break;
            }
            case PACKAGE:
            {
                s.append(intent.getPackage());
                break;
            }
            case SCHEME:
            {
                s.append(intent.getScheme());
                break;
            }
            case TYPE:
            {
                s.append(intent.getType());
                break;
            }
            case EXTRAS:
            {
                Object o;
                Bundle extras = intent.getExtras();
                s.append(String.format(context.getString(R.string.extras_count),
                        extras == null ? 0 : extras.size()));
                if(extras != null)
                {
                    for(String key : extras.keySet())
                    {
                        o = extras.get(key);
                        s.append("\n\n");
                        s.append(String.format(context.getString(R.string.extras_key),key));
                        s.append("\n\n");
                        s.append(String.format(context.getString(R.string.extras_type),o == null ? null : o.getClass()));
                        s.append("\n\n");
                        s.append(String.format(context.getString(R.string.extras_content),o == null ? null : o.toString()));
                    }
                }
                break;
            }
        }
        return s.toString();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition,boolean isExpanded,View convertView,ViewGroup parent)
    {
        TextView text;
        if (convertView == null)
        {
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
        }
        text = convertView.findViewById(android.R.id.text1);
        text.setText(((Intent)getGroup(groupPosition)).getAction());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView text;
        if (convertView == null)
        {
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
        }
        text = convertView.findViewById(android.R.id.text1);
        text.setText((String)getChild(groupPosition,childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
