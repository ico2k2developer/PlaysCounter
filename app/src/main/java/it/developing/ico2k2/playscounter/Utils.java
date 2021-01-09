package it.developing.ico2k2.playscounter;

import android.os.Bundle;

public class Utils
{
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
                s.append(o == null ? "null" : o.toString());
            }
        }
        else
            s.append("null");
        return s.toString();
    }
}
