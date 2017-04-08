package com.MJ.Lingo.models;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/*
* Interface when accessing DB webservice
*/

public interface UserCloudInterface {
    @GET("users/{isbn}")
    Call<List<User>> all(@Path("isbn") String isbn);

    @POST("users/insert")
    Call<User> create(@Body User user);

    @POST("users/delete")
    Call<ResponseBody> delete(@Body User user);

    @POST("users/update")
    Call<ResponseBody> update(@Body User user);
}
