package it.developing.ico2k2.playscounter.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Song
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String id;

    @ColumnInfo(name = "plays_count")
    private int playsCount;

    private static final String separator = ";";

    public static String generateId(String title,String artist)
    {
        return title + separator + artist;
    }

    public Song(String id,int playsCount)
    {
        this.id = id;
        this.playsCount = playsCount;
    }

    @Ignore
    public Song(String title,String artist,int playsCount)
    {
        this(generateId(title,artist),playsCount);
    }

    @Ignore
    public Song(String title,String artist)
    {
        this(title,artist,0);
    }

    @Ignore
    public Song(String id)
    {
        this(id,0);
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return id.substring(0,id.indexOf(separator));
    }

    public String getArtist()
    {
        return id.substring(id.indexOf(separator) + 1);
    }

    public int getPlaysCount()
    {
        return playsCount;
    }

    public void setPlaysCount(int count)
    {
        playsCount = count;
    }

    @Override
    public String toString()
    {
        return id.replace(separator,", ") + " played " + playsCount + " times";
    }
}