package com.MJ.Lingo;

import android.database.Cursor;
import android.provider.Settings;

import com.MJ.Lingo.models.Dictionary;
import com.MJ.Lingo.models.User;
import com.MJ.Lingo.models.UserCloudHandler;
import com.MJ.Lingo.models.UserHandler;

/**
 * UnityController consists of methods called from Unity
 * Returning a String[] value in getWordsUnity and getUserUnity is easier to handle
 * when passing values to C# in Unity
 */

public class UnityController {

    //Requests a set of words for in game use
    public String[] getWordsUnity(){
        Dictionary dic = new Dictionary(MainActivity.ctx.getApplicationContext());
        Cursor rs = dic.selectWord();
        String[] unity = new String[12];
        int x = 0;
        rs.moveToFirst();
        unity[0] = rs.getString(rs.getColumnIndex(Dictionary.COLUMN_ICE));
        unity[1] = rs.getString(rs.getColumnIndex(Dictionary.COLUMN_ENG));
        unity[2] = "f";
        while(rs.moveToNext()){
            x = x+3;
            unity[x] = rs.getString(rs.getColumnIndex(Dictionary.COLUMN_ICE));
            unity[x+1] = rs.getString(rs.getColumnIndex(Dictionary.COLUMN_ENG));
            unity[x+2] = "f";
        }

        return unity;
    }

    //Creates user or updates username if one exists beforehand
    public static void addUserUnity(String userName){
        UserHandler us = new UserHandler(MainActivity.ctx.getApplicationContext());
        String deviceId = Settings.Secure.getString(MainActivity.ctx.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        User previousUser = us.findLocalUser(deviceId);
        User user = new User(deviceId, userName, 0);
        if(previousUser != null){
            user.setScore(previousUser.getScore());
            us.updateUser(user);
        } else{
            us.createUser(user);
        }

    }

    //Requests a list of top users from local DB which were loaded previously from cloud
    public String[] getUserUnity(){
        UserHandler us = new UserHandler(MainActivity.ctx.getApplicationContext());
        Cursor rs = us.selectUser();
        String[] unity = new String[30];

        if(rs.moveToFirst()){
            int x = 0;
            unity[x] = rs.getString(rs.getColumnIndex(UserHandler.COLUMN_USER));
            unity[x+1] = Integer.toString(rs.getInt(rs.getColumnIndex(UserHandler.COLUMN_SCORE)));
            String deviceId = Settings.Secure.getString(MainActivity.ctx.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            if(rs.getString(rs.getColumnIndex(UserHandler.COLUMN_ID)).equals(deviceId)) {unity[x+2] = "t";}
            else {unity[x+2] = "f";}
            while(rs.moveToNext() && x < unity.length-3){
                x = x+3;
                unity[x] = rs.getString(rs.getColumnIndex(UserHandler.COLUMN_USER));
                unity[x+1] = Integer.toString(rs.getInt(rs.getColumnIndex(UserHandler.COLUMN_SCORE)));
                if(rs.getString(rs.getColumnIndex(UserHandler.COLUMN_ID)).equals(deviceId)) {unity[x+2] = "t";}
                else {unity[x+2] = "f";}
            }
        }
        for(int i = 0; i < unity.length; i++){
            System.out.println(unity[i]);
        }

        return unity;
    }

    //Updates user score if user gets personal best in game
    public void updateScoreUnity(int score){
        UserHandler us = new UserHandler(MainActivity.ctx.getApplicationContext());
        UserCloudHandler cloudHandler = new UserCloudHandler();
        String deviceId = Settings.Secure.getString(MainActivity.ctx.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        User user = us.findLocalUser(deviceId);

        if(user != null){
            if(score > user.getScore()){
                user.setScore(score);
                us.updateUser(user);
                cloudHandler.updateUser(user, MainActivity.ctx.getApplicationContext());
            }
        }

    }


}
