package it.developing.ico2k2.playscounter.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao
{
    @Query("SELECT * FROM song")
    List<Song> getAll();

    @Query("SELECT * FROM song WHERE id IN (:ids)")
    List<Song> loadAllById(String[] ids);

    @Query("SELECT * FROM song WHERE plays_count IN (:playsCounts)")
    List<Song> loadAllByPlaysCount(int[] playsCounts);

    @Query("SELECT * FROM song WHERE last_play_day IN (:days)")
    List<Song> loadAllByDay(byte[] days);

    @Query("SELECT * FROM song WHERE last_play_month IN (:months)")
    List<Song> loadAllByMonth(byte[] months);

    @Query("SELECT * FROM song WHERE last_play_year IN (:year)")
    List<Song> loadAllByYear(short[] year);

    @Query("SELECT * FROM song WHERE last_play_hour IN (:hours)")
    List<Song> loadAllByHour(byte[] hours);

    @Query("SELECT * FROM song WHERE last_play_minute IN (:minutes)")
    List<Song> loadAllByMinute(byte[] minutes);

    @Query("SELECT * FROM song WHERE id LIKE :id")
    List<Song> findById(String id);

    @Query("SELECT * FROM song WHERE plays_count LIKE :playsCount")
    List<Song> findByPlaysCount(int playsCount);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Song... songs);

    @Delete
    void delete(Song user);

    @Query("DELETE FROM song WHERE id LIKE :id")
    void delete(String id);

    @Query("DELETE FROM song")
    void deleteAll();
}
