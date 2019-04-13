package com.jn769.remindmev2;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
