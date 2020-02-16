package com.truevisionsa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.truevisionsa.ModelItems.Config;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "config_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Config.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


    public void insertUser(Config config) {
        // get writable database as we want to write data
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Config.COLUMN_DEFAULT_SCHEMA , config.getDefaultSchema());
        values.put(Config.COLUMN_SERVER_IP , config.getServerIp());
        values.put(Config.COLUMN_SERVER_PASSWORD , config.getServerPassword());
        values.put(Config.COLUMN_SERVER_PORT , config.getServerPort());
        values.put(Config.COLUMN_SERVER_USERNAME , config.getServerUserName());
        values.put(Config.COLUMN_WEB_IP , config.getWebIp());
        values.put(Config.COLUMN_WEB_PORT , config.getWebPort());
        // insert row
        db.insert(Config.TABLE_NAME, null, values);

        // close db connection
        db.close();


        // return newly inserted row id
    }


    public List<Config> getUser() {
        // get readable database as we are not inserting anything
        List<Config> configList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Config.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Config config = new Config();
                config.setDefaultSchema(cursor.getString(cursor.getColumnIndex(Config.COLUMN_DEFAULT_SCHEMA)));
                config.setServerIp(cursor.getString(cursor.getColumnIndex(Config.COLUMN_SERVER_IP)));
                config.setServerPassword(cursor.getString(cursor.getColumnIndex(Config.COLUMN_SERVER_PASSWORD)));
                config.setServerPort(cursor.getString(cursor.getColumnIndex(Config.COLUMN_SERVER_PORT)));
                config.setServerUserName(cursor.getString(cursor.getColumnIndex(Config.COLUMN_SERVER_USERNAME)));
                config.setWebIp(cursor.getString(cursor.getColumnIndex(Config.COLUMN_WEB_IP)));
                config.setWebPort(cursor.getString(cursor.getColumnIndex(Config.COLUMN_WEB_PORT)));
                configList.add(config);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return configList;
    }


    public int updateUser(Config config) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Config.COLUMN_DEFAULT_SCHEMA , config.getDefaultSchema());
        values.put(Config.COLUMN_SERVER_IP , config.getServerIp());
        values.put(Config.COLUMN_SERVER_PASSWORD , config.getServerPassword());
        values.put(Config.COLUMN_SERVER_PORT , config.getServerPort());
        values.put(Config.COLUMN_SERVER_USERNAME , config.getServerUserName());

        // updating row
        return db.update(Config.TABLE_NAME, values, Config.COLUMN_SERVER_IP + " = ?",
                new String[]{String.valueOf(config.getServerIp())});
    }


    public void deleteUser(Config config) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Config.TABLE_NAME, Config.COLUMN_SERVER_IP + " = ?",
                new String[]{String.valueOf(config.getServerIp())});
        db.close();
    }

    public void deleteAll() {
        String selectQuery = "DELETE FROM " + Config.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
    }


    public void deleteAndInsertUser(Config config) {

        deleteAll();

        insertUser(config);

    }
}
