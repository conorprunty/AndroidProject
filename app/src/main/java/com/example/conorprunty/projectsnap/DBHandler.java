package com.example.conorprunty.projectsnap;

/**
 * Created by conorprunty on 10/12/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//http://mobilesiri.com/android-sqlite-database-tutorial-using-android-studio/
public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ratingsInfo";
    // Contacts table name
    private static final String TABLE_RATINGS = "ratings";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "rating";
    private static final String KEY_TOTAL = "total";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //It is called first time when database is created. We usually create tables and the initialize here.
        String CREATE_RATINGS_TABLE = "CREATE TABLE " + TABLE_RATINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_TOTAL + " TEXT" + ")";
        db.execSQL(CREATE_RATINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //Run when database is upgraded / changed, like drop tables, add tables etc.
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new rating
    public void addRating(Rating rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, rating.getId());
        values.put(KEY_NAME, rating.getRating());
        values.put(KEY_TOTAL, rating.getTotal());
        // Inserting Row
        db.insert(TABLE_RATINGS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one rating
    public Rating getRating(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RATINGS, new String[]{KEY_ID,
                        KEY_NAME, KEY_TOTAL}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Rating contact = new Rating(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        return contact;
    }


}
