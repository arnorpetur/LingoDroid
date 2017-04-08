package com.MJ.Lingo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.List;

public class UserHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Eggplant.db";
    public static final String TABLE_USER = "USER";
    public static final String COLUMN_ID = "USERID";
    public static final String COLUMN_USER = "USERNAME";
    public static final String COLUMN_SCORE = "SCORE";
    private UserCloudHandler userCloudHandler = new UserCloudHandler();

    public UserHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates both tables in database when DB is upgraded
    @Override
    public void onCreate(SQLiteDatabase db){
        System.out.println("UserHandler onCreate");
        String createUserTable = "CREATE TABLE "+TABLE_USER+" (" +
                COLUMN_ID + " TEXT NOT NULL UNIQUE, " +
                COLUMN_USER + " TEXT NOT NULL, " +
                COLUMN_SCORE + " INT NOT NULL); ";
        String createDictionaryTable = "CREATE TABLE " + Dictionary.TABLE_DICTIONARY + " (" +
                Dictionary.COLUMN_DIFF + " INT NOT NULL, " +
                Dictionary.COLUMN_ICE + " TEXT NOT NULL UNIQUE, " +
                Dictionary.COLUMN_ENG + " TEXT NOT NULL);";

        db.execSQL(createUserTable);
        db.execSQL(createDictionaryTable);
    }
    //Drops both tables when DB is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        System.out.println("UserHandler onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS "+Dictionary.TABLE_DICTIONARY);
        onCreate(db);
    }


    //Creates an user profile in local DB
    public boolean createUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String createId = user.getId();
        String createName = user.getName();
        int createScore = user.getScore();

        values.put(COLUMN_ID, createId);
        values.put(COLUMN_USER, createName);
        values.put(COLUMN_SCORE, createScore);
        try{
            long success = db.insertOrThrow(TABLE_USER, null, values);
            if(success > 0){
                userCloudHandler.insertUser(user);
                return true;
            }
        }
        catch(SQLiteConstraintException e){
            e.printStackTrace();
        }
        return false;
    }

    //Flushes table USER and inserts profiles from cloud
    public void updateTable(List<User> users){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        for(User u : users){
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, u.getId());
            values.put(COLUMN_USER, u.getName());
            values.put(COLUMN_SCORE, u.getScore());
            db.insert(TABLE_USER, null, values);
        }
    }

    //Finds local user if one exists beforehand
    public User findLocalUser(String deviceId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_USER+" WHERE "+COLUMN_ID+" = '"+deviceId+"'";
        Cursor rs = db.rawQuery(query, null);

        if(rs.moveToFirst()){
            User user = new User(
                    rs.getString(rs.getColumnIndex(COLUMN_ID)),
                    rs.getString(rs.getColumnIndex(COLUMN_USER)),
                    rs.getInt(rs.getColumnIndex(COLUMN_SCORE))
            );
            return user;
        }
        return null;
    }

    //Select users from table USER
    public Cursor selectUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM USER", null);
        return res;
    }


    //Updates score from a given user profile and a new username
    public boolean updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String id = user.getId();
        String userName = user.getName();
        int newScore = user.getScore();

        values.put(COLUMN_ID, id);
        values.put(COLUMN_USER, userName);
        values.put(COLUMN_SCORE, newScore);

        db.update(TABLE_USER, values, COLUMN_ID+" = ?", new String[] {id});
        return true;
    }

}
