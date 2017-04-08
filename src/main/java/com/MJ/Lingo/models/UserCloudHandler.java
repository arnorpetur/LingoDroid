package com.MJ.Lingo.models;

import android.content.Context;
import android.provider.Settings;

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

public class UserCloudHandler {

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

    private final UserCloudInterface users = retrofit.create(UserCloudInterface.class);
    private List<User> userList = new ArrayList<>();

    //Sends out an http POST request through UserCloudInterface to insert new user to cloud
    public void insertUser(User user){

        Call<User> call = users.create(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> _, Response<User> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Villumelding: - " + t.getMessage());
            }
        });
    }

    //Sends out an http POST request through UserCloudInterface to update user information on cloud
    public void updateUser(User user, final Context context){

        Call<ResponseBody> call = users.update(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> _, Response<ResponseBody> response) {
                System.out.println(response.body());
                getTopUsers(context);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Villumelding: - " + t.getMessage());
            }
        });
    }

    //Sends out an http POST request through UserCloudInterface to delete user information on cloud
    public void deleteUser(User user){

        Call<ResponseBody> call = users.delete(user);
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

    //Sends out an http GET request through UserCloudInterface to get the 10 users with the highest score
    public void getTopUsers(Context context){
        String deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final UserHandler localUsers = new UserHandler(context);
        Call<List<User>> call = users.all(deviceId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> _, Response<List<User>> response) {
                for(User u : response.body()){
                    userList.add(u);
                    System.out.println(u.getName()+ " - " + u.getScore());
                }
                localUsers.updateTable(userList);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Villumelding: - " + t.getMessage());
            }
        });
    }

}
