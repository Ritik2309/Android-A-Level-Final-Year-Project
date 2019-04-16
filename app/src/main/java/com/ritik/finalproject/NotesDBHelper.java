package com.ritik.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NotesDBHelper extends SQLiteOpenHelper {
//The database is initialized and given a name as well as table headings.
    public static final String DATABASE_NAME = "Notes.db";
    public static final String TABLE_NAME = "Notes_table";
    public static final String COL_1 = "NOTE_ID";
    public static final String COL_2 = "SUBJECT";
    public static final String COL_3 = "NOTE";


    public NotesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }
//The database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (NOTE_ID INTEGER PRIMARY KEY, SUBJECT TEXT, NOTE TEXT )");

    }
//If the database has already been created then the new database table just created is dropped so that the previous data is not overridden.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String NOTE_ID, String subject, String note) {
        //This method places the user values passed form another class into the database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, NOTE_ID);
        contentValues.put(COL_2, subject);
        contentValues.put(COL_3, note);
        long result = db.insert(TABLE_NAME, null, contentValues);
//If the data has been not successfully placed into the database then -1 is returned so we return false where the method was called from.
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData()
    {
        //The cursor class is an interface that provides the read/write access to the result.
        SQLiteDatabase db = this.getWritableDatabase();
        //All the data is selected from the table in the database and returned to where the method was called.
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;
    }

    public boolean updateData(String NOTE_ID, String subject, String note)
    {
        //If an entry exists then teh primary key can be used to identify it and replace it with new data.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, NOTE_ID);
        contentValues.put(COL_2, subject);
        contentValues.put(COL_3, note);
        db.update(TABLE_NAME, contentValues, "NOTE_ID = ?", new String[]  {NOTE_ID});
        return true;
    }

    public Integer deleteData(String NOTE_ID)
    {
        //This method deletes a particular row depending on the user input of the Note ID.
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"NOTE_ID = ?", new String[] {NOTE_ID});

    }
}
