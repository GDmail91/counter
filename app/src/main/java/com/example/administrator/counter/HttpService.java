package com.example.administrator.counter;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by YS on 2015-12-05.
 */
public interface HttpService {
    @Headers("Content-Type : application/json")
    @POST("/clicky/user/join")
    Call<LinkedTreeMap> joinUser(@Body RequestBody requestBody);

    @Headers("Content-Type : application/json")
    @POST("/clicky/user/login")
    Call<LinkedTreeMap> loginUser(@Body RequestBody requestBody);

    @POST("/clicky/btn")
    Call<LinkedTreeMap> regBtn(@Header("set-cookie") String cookie, @Body RequestBody requestBody);

    @Headers("Content-Type : application/json")
    @POST("/clicky/btn/func")
    Call<LinkedTreeMap> regFunc(@Body RequestBody requestBody);


    @POST("/clicky/user/login")
    Call<LinkedTreeMap> testSend();

}
