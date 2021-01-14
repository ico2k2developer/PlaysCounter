package it.developing.ico2k2.playscounter.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name = "length_hour")
    private byte lengthHour;

    @ColumnInfo(name = "length_minute")
    private byte lengthMinute;

    @ColumnInfo(name = "length_seconds")
    private byte lengthSeconds;

    private static final String separator = ";";

    public static String generateId(String title,String artist)
    {
        return title + separator + artist;
    }

    public static class Length
    {
        private final byte hours;
        private final byte minutes;
        private final byte seconds;

        public Length(byte hours,byte minutes,byte seconds)
        {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public Length()
        {
            this.hours = 0;
            this.minutes = 0;
            this.seconds = 0;
        }

        public byte getHours(){
            return hours;
        }

        public byte getMinutes(){
            return minutes;
        }

        public byte getSeconds(){
            return seconds;
        }

        @Override
        public String toString()
        {
            return hours + ":" + minutes + ":" + seconds;
        }
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
    public Song(String title,String artist,int playsCount,byte day,byte month,short year,byte hour,byte minute,byte lengthHour,byte lengthMinute,byte lengthSeconds)
    {
        this(generateId(title,artist),playsCount,day,month,year,hour,minute,
                lengthHour,lengthMinute,lengthSeconds);
    }

    @Ignore
    public Song(String title,String artist,int playsCount,@NonNull Date lastPlay,@NonNull Length length)
    {
        this(generateId(title,artist),playsCount,lastPlay,length);
    }

    public Song(String id,int playsCount,byte day,byte month,short year,byte hour,byte minute,byte lengthHour,byte lengthMinute,byte lengthSeconds)
    {
        this.id = id;
        this.playsCount = playsCount;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.lengthHour = lengthHour;
        this.lengthMinute = lengthMinute;
        this.lengthSeconds = lengthSeconds;
    }

    @Ignore
    public Song(String id,int playsCount,@NonNull Date lastPlay,@NonNull Length length)
    {
        this(id, playsCount,lastPlay.getDay(),lastPlay.getMonth(),lastPlay.getYear(),
                lastPlay.getHour(),lastPlay.getMinute(),length.getHours(),length.getMinutes(),
                length.getSeconds());
    }

    @Ignore
    public Song(String id,int playsCount)
    {
        this(id,playsCount,new Date(),new Length());
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

    public byte getLengthHour(){
        return lengthHour;
    }

    public byte getLengthMinute(){
        return lengthMinute;
    }

    public byte getLengthSeconds(){
        return lengthSeconds;
    }

    public Date getLastPlay(){
        return new Date(day,month,year,hour,minute);
    }

    public Length getLength(){
        return new Length(lengthHour,lengthMinute,lengthSeconds);
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

    private void setLength(@NonNull Length length)
    {
        lengthHour = length.getHours();
        lengthMinute = length.getMinutes();
        lengthSeconds = length.getSeconds();
    }

    @Override
    public String toString()
    {
        return id.replace(separator,", ") +
                getLength().toString() + " played " + playsCount +
                " times, last time was " + getLastPlay().toString();
    }
}