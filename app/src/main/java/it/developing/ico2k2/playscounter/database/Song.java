package it.developing.ico2k2.playscounter.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.getInstance;

@Entity
public class Song
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String id;

    @ColumnInfo(name = "plays_count")
    private int playsCount;

    @ColumnInfo(name = "last_play_day")
    private byte day;

    @ColumnInfo(name = "last_play_month")
    private byte month;

    @ColumnInfo(name = "last_play_year")
    private short year;

    @ColumnInfo(name = "last_play_hour")
    private byte hour;

    @ColumnInfo(name = "last_play_minute")
    private byte minute;

    private static final String separator = ";";

    public static String generateId(String title,String artist)
    {
        return title + separator + artist;
    }

    public Song(String id,int playsCount,byte day,byte month,short year,byte hour,byte minute)
    {
        this.id = id;
        this.playsCount = playsCount;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public Song(String id,int playsCount,Date lastPlay)
    {
        this.id = id;
        this.playsCount = playsCount;
        setDate(lastPlay);
    }

    @Ignore
    public Song(String id,int playsCount)
    {
        this(id,playsCount,null);
    }

    @Ignore
    public Song(String title,String artist,int playsCount)
    {
        this(generateId(title,artist),playsCount);
    }

    @Ignore
    public Song(String title,String artist,int playsCount,Date lastPlay)
    {
        this(generateId(title,artist),playsCount,lastPlay);
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

    public byte getDay(){
        return day;
    }

    public byte getMonth(){
        return month;
    }

    public short getYear(){
        return year;
    }

    public byte getHour(){
        return hour;
    }

    public byte getMinute(){
        return minute;
    }

    public Date getLastPlay(){
        Date date = new Date();
        date.setDate(day);
        date.setMonth(month);
        date.setYear(year);
        date.setHours(hour);
        date.setMinutes(minute);
        return date;
    }

    public void updateLastPlayDate()
    {
        setDate(Calendar.getInstance().getTime());
    }

    private void setDate(@Nullable Date date)
    {
        if(date != null)
        {
            day = (byte)date.getDate();
            month = (byte)date.getMonth();
            year = (short)date.getYear();
            hour = (byte)date.getHours();
            minute = (byte)date.getMinutes();
        }
    }

    @Override
    public String toString()
    {
        return id.replace(separator,", ") + " played " + playsCount + " times";
    }
}