package com.example.android1.loginmedia;

/**
 * Created by Android1 on 8/11/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LoginMedia.db";
    public static final String PERSONS_TABLE_NAME = "RegistrationMember";
    public static final String PERSONS_COLUMN_ID = "id";
    public static final String PERSONS_COLUMN_NAME = "name";
    public static final String PERSONS_COLUMN_EMAIL = "email";
    public static final String PERSONS_COLUMN_PASSWORD = "password";
    public static final String PERSONS_COLUMN_PHONE = "phone";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table  " + PERSONS_TABLE_NAME
                        +
                        "(id integer primary key, name text,phone text,email text, password text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + PERSONS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String phone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSONS_COLUMN_NAME, name);
        contentValues.put(PERSONS_COLUMN_PHONE, phone);
        contentValues.put(PERSONS_COLUMN_EMAIL, email);
        contentValues.put(PERSONS_COLUMN_PASSWORD, password);
        db.insert(PERSONS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PERSONS_TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PERSONS_TABLE_NAME);
        return numRows;
    }

    public boolean updateData(Integer id, String name, String phone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSONS_COLUMN_NAME, name);
        contentValues.put(PERSONS_COLUMN_PHONE, phone);
        contentValues.put(PERSONS_COLUMN_EMAIL, email);
        contentValues.put(PERSONS_COLUMN_PASSWORD, password);
        db.update(PERSONS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteData(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSONS_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public Cursor getAllData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PERSONS_TABLE_NAME, null);
        return res;
    }
}