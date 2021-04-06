package it.developing.ico2k2.playscounter.database;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;
import androidx.room.RoomDatabase;

import it.developing.ico2k2.playscounter.R;

@androidx.room.Database(entities = {Song.class}, version = 1)
public abstract class Database extends RoomDatabase
{
    public abstract SongDao dao();
}
