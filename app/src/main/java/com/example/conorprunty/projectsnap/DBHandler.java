package com.example.conorprunty.projectsnap;

/**
 * Created by conorprunty on 10/12/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


//http://mobilesiri.com/android-sqlite-database-tutorial-using-android-studio/
public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "boardInfo";
    // Contacts table name
    private static final String TABLE_BOARDS = "boards";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "rating";
    private static final String KEY_COLOUR = "total";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //It is called first time when database is created. We usually create tables and the initialize here.
        String CREATE_BOARDS_TABLE = "CREATE TABLE " + TABLE_BOARDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_COLOUR + " TEXT" + ")";
        db.execSQL(CREATE_BOARDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //Run when database is upgraded / changed, like drop tables, add tables etc.
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARDS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new board
    public void addBoard(Rating rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, rating.getRating()); // Board Name
        values.put(KEY_COLOUR, rating.getTotal()); // Board Colour
        // Inserting Row
        db.insert(TABLE_BOARDS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one board
    public Rating getBoard(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOARDS, new String[] { KEY_ID,
                        KEY_NAME, KEY_COLOUR }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Rating contact = new Rating(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        return contact;
    }

    // Getting All Boards
    public List<Rating> getAllBoards() {
        List<Rating> boardList = new ArrayList<Rating>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BOARDS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Rating rating = new Rating();
                rating.setId(Integer.parseInt(cursor.getString(0)));
                rating.setRating(cursor.getString(1));
                rating.setTotal(cursor.getString(2));
                // Adding board to list
                boardList.add(rating);
            } while (cursor.moveToNext());
        }
        // return board list
        return boardList;
    }

    // Getting boards count
    public int getBoardsCount() {
        String countQuery = "SELECT * FROM " + TABLE_BOARDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }





}