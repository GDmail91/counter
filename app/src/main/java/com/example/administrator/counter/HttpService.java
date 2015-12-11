package com.example.administrator.counter;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

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

    @GET("/clicky/btn")
    Call<LinkedTreeMap> getBtnList(@Header("cookie") String cookie );

    @POST("/clicky/btn")
    Call<LinkedTreeMap> regBtn(@Header("cookie") String cookie, @Body RequestBody requestBody);

    @Headers("Content-Type : application/json")
    @POST("/clicky/btn/func")
    Call<LinkedTreeMap> regFunc(@Header("cookie") String cookie, @Body RequestBody requestBody);

    @GET("/clicky/btn/func/{mac}")
    Call<LinkedTreeMap> getBtn(@Header("cookie") String cookie, @Path("mac") String mac_addr );



    @Headers("Content-Type : application/json")
    @POST("/clicky/btn/func")
    Call<LinkedTreeMap> testSend(@Header("cookie") String cookie, @Body RequestBody requestBody);

}
