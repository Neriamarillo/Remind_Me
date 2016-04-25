package com.jn769.remindme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * @author Jorge Nieves
 * @version 1.0
 */
class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "reminderDatabase";

    private static final String TABLE_REMINDERS = "My_Reminders";


    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_DESC = "description";
    private Reminder reminder;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TITLE + " TEXT, " + KEY_TIME
                + " TEXT, " + KEY_DATE + " TEXT, " + KEY_DESC + " TEXT " + ")";
        db.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        onCreate(db);
    }

    void addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, reminder.getTitle());
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_DESC, reminder.getDescription());

        db.insert(TABLE_REMINDERS, null, values);
        db.close();
    }

    Reminder getReminder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[]{KEY_ID, KEY_TITLE, KEY_TIME, KEY_DATE, KEY_DESC}, KEY_ID + " =?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            reminder = new Reminder(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4));
        }
        db.close();
        return reminder;
    }

    public ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> reminderArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setID(Integer.parseInt(cursor.getString(0)));
                reminder.setTitle(cursor.getString(1));
                reminder.setTime(cursor.getString(2));
                reminder.setDate(cursor.getString(3));
                reminder.setDescription(cursor.getString(4));
                reminderArrayList.add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reminderArrayList;
    }

    public int updateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, reminder.getTitle());
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_DESC, reminder.getDescription());

        return db.update(TABLE_REMINDERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(reminder.getID())});
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, KEY_ID + " = ?",
                new String[]{String.valueOf(reminder.getID())});
        db.close();
    }


}
