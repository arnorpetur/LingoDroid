package com.MJ.Lingo.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordCloudHandler {

    //Free accounts on heroku can take time, important to give LingoDingo time to connect
    //with DB webservice
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://calm-shelf-61149.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    private final DictionaryInterface dictionary = retrofit.create(DictionaryInterface.class);
    private List<Word> words = new ArrayList<>();

    //Sends out an http POST request through WordCloudInterface to insert a new word to cloud
    public void insertWord(Word word){

        Call<Word> call = dictionary.create(word);
        call.enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> _, Response<Word> response) {
                Word insertWord = response.body();
                System.out.println(insertWord.getIcelandic() +
                        " = " + insertWord.getEnglish() +
                        ", stig = " + insertWord.getDifficulty());
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Villumelding: - " + t.getMessage());
            }
        });
    }

    //Sends out an http POST request through WordCloudInterface to delete word on cloud
    public void deleteWord(Word word){

        Call<ResponseBody> call = dictionary.delete(word);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> _, Response<ResponseBody> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Villumelding: - " + t.getMessage());
            }
        });
    }

    //Sends out an http GET request through UserCloudInterface to get all words from cloud DB
    public void getWords(Context context){
        final Dictionary localDictionary = new Dictionary(context);
        Call<List<Word>> call = dictionary.all();
        call.enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> _, Response<List<Word>> response) {
                for(Word w : response.body()){
                    words.add(w);
                }
                localDictionary.updateTable(words);
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Villumelding: - " + t.getMessage());
            }
        });
    }

}
