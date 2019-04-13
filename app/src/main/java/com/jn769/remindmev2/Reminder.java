package com.jn769.remindmev2;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

/**
 * @author Jorge Nieves
 * @version 1.0
 */
@Entity(tableName = "reminders")
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "time")
    @Nullable
    private String time;

    @ColumnInfo(name = "date")
    @TypeConverters(DateConverter.class)
    @Nullable
    private Date date;

    @ColumnInfo(name = "description")
    @Nullable
    private String description;

    public Reminder() {
    }

    @Ignore
    public Reminder(int id, String title, String time, Date date, String description) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.date = date;
        this.description = description;
    }

    public Reminder(String title, String time, Date date, String description) {
        this.title = title;
        this.time = time;
        this.date = date;
        this.description = description;
    }

    public Reminder(Reminder reminder) {
        this.id = reminder.getId();
        this.title = reminder.getTitle();
        this.time = reminder.getTime();
        this.date = reminder.getDate();
        this.description = reminder.getDescription();
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String _title) {
        this.title = _title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String _time) {
        this.time = _time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date _date) {
        this.date = _date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String _description) {
        this.description = _description;
    }

}
