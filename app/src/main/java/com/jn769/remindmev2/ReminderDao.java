package com.jn769.remindmev2;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Query("DELETE FROM reminders")
    void deleteAll();

    @Query("SELECT * from reminders")
    LiveData<List<Reminder>> getAllReminders();

    @Query("SELECT * FROM reminders WHERE id = :id")
    Reminder getReminder(int id);

}
