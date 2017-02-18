package com.example.bastin.application_vendeurs_4.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.bastin.application_vendeurs_4.entity.LogBook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bastin on 13-11-16.
 */

public class DAOLogBook extends DAOBase {

    public DAOLogBook(Context context) {
        super(context);
    }

    public void log(String action) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();

        LogBook logBook = new LogBook();
        logBook.setAction(action);
        logBook.setDate(df.format(today));

        this.add(logBook);
    }

    public void add(LogBook logbook) {
        ContentValues values = new ContentValues();
        values.put(LogBook.LOGBOOK_ACTION, logbook.getAction());
        values.put(LogBook.LOGBOOK_DATE, logbook.getDate());
        db.insert(LogBook.LOGBOOK_TABLE_NAME, null, values);
    }

    public ArrayList<LogBook> getAll() {

        ArrayList<LogBook> list = new ArrayList();

        Cursor cursor = db.query(LogBook.LOGBOOK_TABLE_NAME, null, null, null, null, null, LogBook.LOGBOOK_KEY + " DESC");

        if(cursor.moveToFirst()) {
            while(cursor.moveToNext()) {
                LogBook logbook = new LogBook();
                logbook.setId(cursor.getLong(0));
                logbook.setAction(cursor.getString(1));
                logbook.setDate(cursor.getString(2));

                list.add(logbook);
            }

            cursor.close();
        }

        return list;
    }
}
