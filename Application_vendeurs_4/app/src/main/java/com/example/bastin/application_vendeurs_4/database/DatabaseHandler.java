package com.example.bastin.application_vendeurs_4.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bastin.application_vendeurs_4.entity.LogBook;

/**
 * Created by bastin on 12-11-16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String LOGBOOK_TABLE_CREATE =
            "CREATE TABLE " + LogBook.LOGBOOK_TABLE_NAME + " (" +
                    LogBook.LOGBOOK_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LogBook.LOGBOOK_ACTION + " TEXT, " +
                    LogBook.LOGBOOK_DATE + " TEXT);";

    public static final String LOGBOOK_TABLE_DROP = "DROP TABLE IF EXISTS " + LogBook.LOGBOOK_TABLE_NAME + ";";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOGBOOK_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LOGBOOK_TABLE_DROP);
        onCreate(db);
    }
}
