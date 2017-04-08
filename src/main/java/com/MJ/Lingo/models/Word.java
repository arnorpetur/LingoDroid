package com.MJ.Lingo.models;

import com.google.gson.annotations.SerializedName;

public class Word {

    @SerializedName("icelandic")
    private String icelandic;

    @SerializedName("english")
    private String english;

    @SerializedName("difficulty")
    private int difficulty;

    /*
    * Contains basic runtime information for each word in game dictionary
    */

    public Word(String icelandic, String english, int difficulty){
        this.icelandic = icelandic;
        this.english = english;
        this.difficulty = difficulty;
    }

    public String getIcelandic(){
        return this.icelandic;
    }

    public void setIcelandic(String newIcelandic){
        this.icelandic = newIcelandic;
    }

    public String getEnglish(){
        return this.english;
    }

    public void setEnglish(String newEnglish){
        this.english = newEnglish;
    }

    public int getDifficulty(){
        return this.difficulty;
    }

    public void setDifficulty(int newDifficulty){
        this.difficulty = newDifficulty;
    }

}
