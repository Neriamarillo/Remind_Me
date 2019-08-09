package com.jn769.remindmev2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAlarm(Alarm alarm);

    @Delete
    void deleteAlarm(Alarm alarm);

    @Query("SELECT * FROM alarms")
    List<Alarm> getAlarms();

}
