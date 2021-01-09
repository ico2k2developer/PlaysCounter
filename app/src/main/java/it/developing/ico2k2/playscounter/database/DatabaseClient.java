package it.developing.ico2k2.playscounter.database;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseClient
{
    private static final Map<String,DatabaseClient> clients = new HashMap<>();
    private final Database database;

    private DatabaseClient(Context context,String name) {

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        database = Room.databaseBuilder(context, Database.class, name).build();
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
