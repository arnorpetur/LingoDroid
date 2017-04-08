package com.MJ.Lingo.models;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/*
* Interface when accessing DB webservice
*/

public interface DictionaryInterface {
    @GET("words")
    Call<List<Word>> all();

    @POST("words/insert")
    Call<Word> create(@Body Word word);

    @POST("words/delete")
    Call<ResponseBody> delete(@Body Word word);
}
