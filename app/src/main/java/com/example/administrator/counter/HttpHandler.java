package com.example.administrator.counter;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by YS on 2015-12-05.
 */
public class HttpHandler {
    private static final String TAG = "HttpClient";

    private final String baseUrl = "http://nayak.mooo.com:30003/clicky";
    private Retrofit client = new Retrofit.Builder()
                                        .baseUrl(baseUrl)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                        .build();
    private HttpService httpService = client.create(HttpService.class);
    private JSONObject result;

    public JSONObject joinUser(String id, String password) {
        Call<LoginData> responseData = httpService.joinUser(id, password);

        responseData.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Response<LoginData> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    //result = response.body();
                } else {
                    // TODO error
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        return result;
    }

    public void loginUser(String jobj, final MyCallback callback) {
        //JSONObject parseResult = null;
        // 보내는 데이터
        Log.d(TAG, jobj);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj);
        final Call<Object> responseData = httpService.loginUser(requestBody);

        responseData.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        result = new JSONObject(response.body().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 받은 데이터 출력
                    Log.d(TAG, response.body().toString());
                    Log.d(TAG, result.toString());

                } else {
                    String test = response.message();
                    Log.d(TAG, test);

                    // TODO error
                }

                callback.loginProcessing(result);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, t.toString());
                Log.d(TAG, "아예실패");
            }
        });

    }

    public void testSend() {
        Log.d(TAG, "testSend()");
        Log.d(TAG, client.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "test");
        Call<JSONObject> repos = httpService.testSend ();
        repos.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Response<JSONObject> response, Retrofit retrofit) {
                String test = response.message();
                if (response.isSuccess()) {

                    Log.d(TAG, "성공");
                }
                Log.d(TAG, test);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "실패");
            }
        });
    }

    public void testSend2() {
        Log.d(TAG, "testSend2()");
        Log.d(TAG, client.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "test");
        Call<JSONObject> repos = httpService.testSend2 ();
        repos.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Response<JSONObject> response, Retrofit retrofit) {
                String test = response.message();
                if (response.isSuccess()) {

                    Log.d(TAG, "성공");
                }
                Log.d(TAG, test);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "실패");
            }
        });
    }

    public void testSend3() {
        Log.d(TAG, "testSend3()");
        Log.d(TAG, client.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "test");
        Call<JSONObject> repos = httpService.testSend3 ();
        repos.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Response<JSONObject> response, Retrofit retrofit) {
                String test = response.message();
                if (response.isSuccess()) {

                    Log.d(TAG, "성공");
                }
                Log.d(TAG, test);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "실패");
            }
        });
    }
}
