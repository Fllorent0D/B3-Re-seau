package com.example.bastin.application_vendeurs_4.entity;

/**
 * Created by bastin on 12-11-16.
 */

public class LogBook {

    public static final String LOGBOOK_KEY = "id";
    public static final String LOGBOOK_ACTION = "action";
    public static final String LOGBOOK_DATE = "date";

    public static final String LOGBOOK_TABLE_NAME = "LogBook";

    protected long id;
    private String action;
    private String date;

    public LogBook() {}

    public LogBook(long id, String action, String date) {
        this.id = id;
        this.action = action;
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
