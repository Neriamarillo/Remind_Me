package com.jn769.remindmev2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms")
class Alarm {

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alarmId")
    private long alarmId;

    @ColumnInfo(name = "alarmTime")
    private long alarmTime;


    public Alarm(@NonNull String title, String description, long alarmId, long alarmTime) {
        this.title = title;
        this.description = description;
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }
}
