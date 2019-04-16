package com.ritik.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;



public class QuizDBHelper extends SQLiteOpenHelper {
//The database is initialized and given a name as well as table headings.

    public static final String DATABASE_NAME = "Questions.db";
    public static final String TABLE_NAME = "Questions_table";
    public static final String COL_1 = "QUESTION_NUM";
    public static final String COL_2 = "QUESTION";
    public static final String COL_3 = "OPTION1";
    public static final String COL_4 = "OPTION2";
    public static final String COL_5 = "OPTION3";
    public static final String COL_6 = "CORRECT_ANS_NUM";


    public SQLiteDatabase db;


    public QuizDBHelper(Context context) {
        //The Database gets created
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //The table gets created.
        db.execSQL("create table " + TABLE_NAME + " ( QUESTION_NUM INTEGER PRIMARY KEY, QUESTION TEXT, OPTION1 TEXT, OPTION2 TEXT, OPTION3 TEXT, CORRECT_ANS_NUM INTEGER, IMAGE BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //The table is dropped if it already exists as to not override the previous table and its data.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String question_num, String question, String option1, String option2, String option3, String correct_Ans) {
        //This method take all the data from the user and places it inside the table.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); //ContentValues is used to store a set of values that the ContentResolver can process.
        contentValues.put(COL_1, question_num);
        contentValues.put(COL_2, question);
        contentValues.put(COL_3, option1);
        contentValues.put(COL_4, option2);
        contentValues.put(COL_5, option3);
        contentValues.put(COL_6, correct_Ans);

        long result = db.insert(TABLE_NAME, null, contentValues); //this inserts the data stored in content values into the table.

        //Here the long 'result' is returned and if it does not return -1 then the data was stored correctly and a boolean true value is returned to
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        //The cursor class is an interface that provides the random read/write access to the result.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;
    }


    public boolean updateData(String QuestionNumber, String Question, String option1, String option2, String option3, String correct_Ans) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); //ContentValues is used to store a set of values that the ContentResolver can process.
        contentValues.put(COL_1, QuestionNumber);
        contentValues.put(COL_2, Question);
        contentValues.put(COL_3, option1);
        contentValues.put(COL_4, option2);
        contentValues.put(COL_5, option3);
        contentValues.put(COL_6, correct_Ans);
        db.update(TABLE_NAME, contentValues, "QUESTION_NUM = ?", new String[]{QuestionNumber});
        return true;
    }

    public ArrayList<Quiz> getAllQuestions() {
        ArrayList<Quiz> questionList = new ArrayList<>(); //sets up an arraylist.
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuizDBHelper.TABLE_NAME, null); //The Cursor class provides methods for moving the cursor through the data structure, and methods to get the data from the fields in each row.
        {
            if (c.moveToFirst()) {
                do {
                    //sets all the different questions and answers to the Quiz class in their getter and setter methods for the Quiz.
                    Quiz question = new Quiz();
                    question.setQuestionNumber(c.getString(c.getColumnIndex(QuizDBHelper.COL_1)));
                    question.setQuestion(c.getString(c.getColumnIndex(QuizDBHelper.COL_2)));
                    question.setOption1(c.getString(c.getColumnIndex(QuizDBHelper.COL_3)));
                    question.setOption2(c.getString(c.getColumnIndex(QuizDBHelper.COL_4)));
                    question.setOption3(c.getString(c.getColumnIndex(QuizDBHelper.COL_5)));
                    question.setCorrect_Ans_Num(c.getInt(c.getColumnIndex(QuizDBHelper.COL_6)));
                    questionList.add(question);
                } while (c.moveToNext());
            }

            c.close();
            return questionList;
        }

    }


    public Integer deleteData(String QuestionNumber) {
        //deletes certain rows of data from the table.
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "QUESTION_NUM = ?", new String[]{QuestionNumber});
    }



}

