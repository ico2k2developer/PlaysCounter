package it.developing.ico2k2.playscounter;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Set;

public class Utils
{
    public static final String CHANNEL_ID_MAIN = "main";

    public static final String DATABASE_SONGS = "songs";

    public static String examineBundle(Bundle bundle)
    {
        StringBuilder s = new StringBuilder();
        if(bundle != null)
        {
            s.append(bundle.size());
            s.append(" elements:");
            Object o;
            for(String key : bundle.keySet())
            {
                s.append("\n[");
                o = bundle.get(key);
                s.append(o == null ? "null" : o.getClass().getSimpleName());
                s.append("] ");
                s.append(key);
                s.append(": ");
                s.append(examine(o));
            }
        }
        else
            s.append("null");
        return s.toString();
    }

    @RequiresApi(19)
    public static String examineAction(Notification.Action action)
    {
        StringBuilder s = new StringBuilder();
        if(action != null)
        {
            s.append("Title: ");
            s.append(action.title);
            s.append("\nIcon: ");
            s.append(action.icon);
            s.append("\nPendingIntent: ");
            s.append(examine(action.actionIntent));
        }
        else
            s.append("null");
        return s.toString();
    }

    public static String examineIntent(Intent intent)
    {
        StringBuilder s = new StringBuilder();
        if(intent != null)
        {
            s.append("Action: ");
            s.append(examine(intent.getAction()));
            s.append("\n");
            Set<String> set = intent.getCategories();
            s.append(set == null ? 0 : set.size());
            s.append(" categories: ");
            if(set != null)
            {
                for(String c : intent.getCategories())
                {
                    s.append(c);
                    s.append(", ");
                }
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                s.append("\nClip data: ");
                s.append(intent.getClipData());
            }
            s.append("\nComponent: ");
            s.append(intent.getComponent());
            s.append("\nData: ");
            s.append(examine(intent.getDataString()));
            s.append("\nFlags: ");
            s.append(examine(intent.getFlags()));
            s.append(" (0x");
            s.append(Integer.toHexString(intent.getFlags()));
            s.append(")\nPackage: ");
            s.append(examine(intent.getPackage()));
            s.append("\nScheme: ");
            s.append(examine(intent.getScheme()));
            s.append("\nType: ");
            s.append(examine(intent.getType()));
            s.append("\nExtras:\n");
            s.append(examineBundle(intent.getExtras()));
        }
        else
            s.append("null");
        return s.toString();
    }

    @RequiresApi(17)
    public static String examinePendingIntent(PendingIntent intent)
    {
        StringBuilder s = new StringBuilder();
        if(intent != null)
        {
            s.append("Creator package: ");
            s.append(intent.getCreatorPackage());
            s.append("\nIntentSender: ");
            s.append(intent.getIntentSender().toString());
            s.append("\nCreator UserHandle: ");
            s.append(intent.getCreatorUserHandle().toString());
        }
        else
            s.append("null");
        return s.toString();
    }

    enum Type
    {
        NONE,
        Intent,
        Bundle,
        Action,
        PendingIntent,
    }

    @TargetApi(19)
    public static String examine(Object value)
    {
        StringBuilder s = new StringBuilder();
        if(value != null)
        {
            if(value.getClass().isArray())
            {
                s.append("array: ");
                if(value instanceof Object[])
                {
                    s.append("[");
                    for(Object o : (Object[])value)
                    {
                        s.append(examine(o));
                        s.append(";");
                    }
                    s.append("]");
                }
                else if(value instanceof int[])
                {
                    s.append("[");
                    for(int o : (int[])value)
                    {
                        s.append(o);
                        s.append(";");
                    }
                    s.append("]");
                }
                else if(value instanceof char[])
                {
                    s.append("[");
                    for(char o : (char[])value)
                    {
                        s.append(o);
                        s.append(";");
                    }
                    s.append("]");
                }
                else if(value instanceof long[])
                {
                    s.append("[");
                    for(long o : (long[])value)
                    {
                        s.append(o);
                        s.append(";");
                    }
                    s.append("]");
                }
                else if(value instanceof double[])
                {
                    s.append("[");
                    for(double o : (double[])value)
                    {
                        s.append(o);
                        s.append(";");
                    }
                    s.append("]");
                }
                else if(value instanceof short[])
                {
                    s.append("[");
                    for(short o : (short[])value)
                    {
                        s.append(o);
                        s.append(";");
                    }
                    s.append("]");
                }
                else if(value instanceof float[])
                {
                    s.append("[");
                    for(float o : (float[])value)
                    {
                        s.append(o);
                        s.append(";");
                    }
                    s.append("]");
                }
                else if(value instanceof byte[])
                {
                    s.append("[");
                    for(byte o : (byte[])value)
                    {
                        s.append(o);
                        s.append(";");
                    }
                    s.append("]");
                }
                else
                    s.append("unknown array");
            }
            else
            {
                Type type;
                try
                {
                    type = Type.valueOf(value.getClass().getSimpleName());
                }
                catch(Exception e)
                {
                    type = Type.NONE;
                }
                switch(type)
                {
                    case Intent:
                    {
                        s.append(examineIntent((Intent)value));
                        break;
                    }
                    case Bundle:
                    {
                        s.append(examineBundle((Bundle)value));
                        break;
                    }
                    case Action:
                    {
                        s.append(examineAction((Notification.Action)value));
                        break;
                    }
                    case PendingIntent:
                    {
                        s.append(examinePendingIntent((PendingIntent)value));
                        break;
                    }
                    default:
                    {
                        s.append(value.toString());
                    }
                }
            }
        }
        else
            s.append("null");
        return s.toString();
    }
}
