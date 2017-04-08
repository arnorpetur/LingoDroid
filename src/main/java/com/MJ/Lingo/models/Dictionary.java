package com.MJ.Lingo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class Dictionary extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Eggplant.db";
    public static final String TABLE_DICTIONARY = "DICTIONARY";
    public static final String COLUMN_DIFF = "DIFFICULTY";
    public static final String COLUMN_ICE = "ICELANDIC";
    public static final String COLUMN_ENG = "ENGLISH";

    public Dictionary(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates both tables when DB is upgraded
    @Override
    public void onCreate(SQLiteDatabase db){
        System.out.println("Dictionary onCreate");
        String createDictionaryTable = "CREATE TABLE " + TABLE_DICTIONARY + " (" +
                COLUMN_DIFF + " INT NOT NULL, " +
                COLUMN_ICE + " TEXT NOT NULL UNIQUE, " +
                COLUMN_ENG + " TEXT NOT NULL); ";
        String createUserTable = "CREATE TABLE "+UserHandler.TABLE_USER+" (" +
                UserHandler.COLUMN_ID + " TEXT NOT NULL UNIQUE, " +
                UserHandler.COLUMN_USER + " TEXT NOT NULL, " +
                UserHandler.COLUMN_SCORE + " INT NOT NULL);";

        db.execSQL(createDictionaryTable);
        db.execSQL(createUserTable);
    }

    //Drops both tables when DB is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        System.out.println("Dictionary onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICTIONARY);
        db.execSQL("DROP TABLE IF EXISTS " + UserHandler.TABLE_USER);
        onCreate(db);
    }

    //Adds a word to dictionary
    public void addWord(Word word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int difficulty = word.getDifficulty();
        String icelandic = word.getIcelandic();
        String english = word.getEnglish();

        values.put(COLUMN_DIFF, difficulty);
        values.put(COLUMN_ICE, icelandic);
        values.put(COLUMN_ENG, english);
        db.insert(TABLE_DICTIONARY, null, values);
    }

    //Flushes table dictionary and inserts new words from cloud upon startup
    public void updateTable(List<Word> words){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DICTIONARY, null, null);
        for(Word w : words){
            addWord(w);
        }
    }

    //Selects a word randomly from dictionary with a certain difficulty
    public Cursor selectWord(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+ COLUMN_DIFF + ", " + COLUMN_ICE + ", " + COLUMN_ENG +
                " FROM " + TABLE_DICTIONARY + " ORDER BY RANDOM() LIMIT 4";
        Cursor res = db.rawQuery(query, null);
        return res;
    }

}
