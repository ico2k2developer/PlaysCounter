package it.developing.ico2k2.playscounter.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;



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

    public static class Date
    {
        private final byte day;
        private final byte month;
        private final short year;
        private final byte hour;
        private final byte minute;

        public Date(byte day,byte month,short year,byte hour,byte minute)
        {
            this.day = day;
            this.month = month;
            this.year = year;
            this.hour = hour;
            this.minute = minute;
        }

        public Date()
        {
            java.util.Date date = Calendar.getInstance().getTime();
            day = (byte)date.getDay();
            month = (byte)date.getMonth();
            year = (short)date.getYear();
            hour = (byte)date.getHours();
            minute = (byte)date.getMinutes();
        }

        public java.util.Date toDate()
        {
            Calendar c = Calendar.getInstance();
            c.set(year,month,day,hour,minute);
            return c.getTime();
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

        @Override
        public String toString()
        {
            return day + "/" + (month + 1) + "/" + year + " " + hour + ":" + minute;
        }
    }

    @Ignore
    public Song(String title,String artist,int playsCount,byte day,byte month,short year,byte hour,byte minute)
    {
        this(generateId(title,artist),playsCount,day,month,year,hour,minute);
    }

    @Ignore
    public Song(String title,String artist,int playsCount,@NonNull Date lastPlay)
    {
        this(generateId(title,artist),playsCount,lastPlay);
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

    @Ignore
    public Song(String id,int playsCount,@NonNull Date lastPlay)
    {
        this(id, playsCount,lastPlay.getDay(),lastPlay.getMonth(),lastPlay.getYear(),
                lastPlay.getHour(),lastPlay.getMinute());
    }

    @Ignore
    public Song(String id,int playsCount)
    {
        this(id,playsCount,new Date());
    }

    @Ignore
    public Song(String title,String artist,int playsCount)
    {
        this(generateId(title,artist),playsCount);
    }

    @Ignore
    public Song(String id)
    {
        this(id,0);
    }

    @NonNull
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
        return new Date(day,month,year,hour,minute);
    }

    public void updateLastPlayDate()
    {
        setLastPlay(new Date());
    }

    private void setLastPlay(@NonNull Date lastPlay)
    {
        day = lastPlay.getDay();
        month = lastPlay.getMonth();
        year = lastPlay.getYear();
        hour = lastPlay.getHour();
        minute = lastPlay.getMinute();
    }

    @Override
    public String toString()
    {
        return id.replace(separator,", ") + " played " + playsCount +
                " times, last time was " + getLastPlay().toString();
    }
}