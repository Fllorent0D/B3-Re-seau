package com.example.bastin.application_vendeurs_4.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.bastin.application_vendeurs_4.database.DatabaseHandler;

/**
 * Created by bastin on 12-11-16.
 */

public class DAOBase {
    protected final static int VERSION = 1;

    protected final static String NAME ="database.db";

    protected SQLiteDatabase db = null;
    protected DatabaseHandler handler = null;

    public DAOBase(Context context) {
        this.handler = new DatabaseHandler(context, NAME, null, VERSION);
    }

    public SQLiteDatabase open() {
        db = handler.getWritableDatabase();
        return db;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
