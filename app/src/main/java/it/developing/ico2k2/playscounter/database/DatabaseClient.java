package it.developing.ico2k2.playscounter.database;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;
import androidx.room.Room;
import java.util.HashMap;
import java.util.Map;

import it.developing.ico2k2.playscounter.R;

public class DatabaseClient
{
    public static final String DATABASE_SONGS = "songs";

    private static final Map<String,DatabaseClient> clients = new HashMap<>();
    private final Database database;

    private DatabaseClient(Context context,String name)
    {
        database = Room.databaseBuilder(context, Database.class, name)
                .fallbackToDestructiveMigrationFrom()
                .build();
    }

    public static synchronized Database getInstance(Context context,String name)
    {
        DatabaseClient result;
        if (!clients.containsKey(name)) {
            clients.put(name,result = new DatabaseClient(context,name));
        }
        else
            result = clients.get(name);
        return result.database;
    }
}
