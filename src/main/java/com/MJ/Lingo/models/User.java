package com.MJ.Lingo.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private String id;

    @SerializedName("userName")
    private String userName;

    @SerializedName("score")
    private int score;

    /*
    * Contains basic runtime user information
    */

    public User(String id, String user, int score){
        this.id = id;
        this.userName = user;
        this.score = score;
    }

    public User(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String newId){
        this.id = newId;
    }

    public String getName(){
        return this.userName;
    }

    public void setName(String newName){
        this.userName = newName;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int newScore){
        this.score = newScore;
    }

}
