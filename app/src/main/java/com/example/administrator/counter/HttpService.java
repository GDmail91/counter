package com.example.administrator.counter;

import com.example.administrator.counter.http.ResponseData;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by YS on 2015-12-05.
 */
public interface HttpService {
    @POST("/user/join")
    Call<LoginData> joinUser(@Body String id, @Body String password);

    @Headers("Content-Type : application/json")
    @POST("/clicky/user/login")
    Call<Object> loginUser(@Body RequestBody requestBody);

    @POST("/btn")
    Call<ResponseData> regBtn(@Body String message);


    @POST("/clicky/user/login")
    Call<JSONObject> testSend();

    @POST("/user/login")
    Call<JSONObject> testSend2();

    @POST("clicky/user/login")
    Call<JSONObject> testSend3();

}
