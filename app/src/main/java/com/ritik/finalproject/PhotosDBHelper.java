package com.ritik.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class PhotosDBHelper extends SQLiteOpenHelper {

    //The database is initialized and given a name as well as table headings.
    public static final String DATABASE_NAME = "Images.db";
    public static final String TABLE_NAME = "Images_Table";
    public static final String COL_1 = "IMAGES_ID";
    public static final String COL_2 = "IMAGES";


    public SQLiteDatabase db;



    public PhotosDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }
    //The database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( IMAGES_ID INTEGER PRIMARY KEY, IMAGES BLOB)");
    }
    //If the database has already been created then the new database table just created is dropped so that the previous data is not overridden.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String IMAGES_ID, byte[] IMAGES) {
        //This method places the user values passed form another class into the database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, IMAGES_ID);
        contentValues.put(COL_2, IMAGES);


        long result = db.insert(TABLE_NAME, null, contentValues);

//If the data has been not successfully placed into the database then -1 is returned so we return false where the method was called from.
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Bitmap getImage(int IMAGES_ID) {
        //The byte array data from a particular record is returned to the user.
        db = getReadableDatabase();
        Bitmap bitmap = null;
        Cursor cursor = db.rawQuery("select * from Images_Table where IMAGES_ID=? ", new String[] {String.valueOf(IMAGES_ID)});
        if (cursor.moveToNext())
        {
            //The byte array data that has been saved in the database table is converted back to bitmap data below.
            byte[] image = cursor.getBlob(1);
            bitmap = BitmapFactory.decodeByteArray(image ,0, image.length);
        }
        return bitmap; //bitmap data returned.
    }

    public boolean updateData(String IMAGES_ID, byte[] IMAGES) {
        //This method replaces the already existing byte array data with another on a particular record.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, IMAGES_ID);
        contentValues.put(COL_2, IMAGES);
        db.update(TABLE_NAME, contentValues, "IMAGES_ID = ?", new String[]{IMAGES_ID});
        return true;
    }


}

