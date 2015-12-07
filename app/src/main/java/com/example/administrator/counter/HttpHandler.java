package com.example.administrator.counter;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private static final String TAG = "HttpHandler";

    private final String baseUrl = "http://nayak.mooo.com/clicky";
    private Retrofit client = new Retrofit.Builder()
                                        .baseUrl(baseUrl)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                        .build();
    private HttpService httpService = client.create(HttpService.class);
    private JSONObject result;
    private static String cookie;

    public void joinUser(String jobj, final MyCallback callback) {
        Log.d(TAG, jobj);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj);
        final Call<LinkedTreeMap> responseData = httpService.joinUser(requestBody);

        responseData.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();

                        result = new JSONObject("{\"status\":"+status+",\"message\":\""+messages+"\"}");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 받은 데이터 출력
                    Log.d(TAG, response.body().toString());
                    Log.d(TAG, result.toString());

                } else {
                    // TODO 원래는 여기서 분기를 해줘도 되지만 각 액티비티에서 에러처리 실행(토스트를 띄우던가...)
                    String test = response.message();
                    Log.d(TAG, test);
                    try {
                        result = new JSONObject(response.message());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                callback.httpProcessing(result);
            }

            @Override
            public void onFailure(Throwable t) {

                // TODO 실패
                Log.d(TAG, t.toString());
                Log.d(TAG, "아예실패");
            }
        });
    }

    public void loginUser(String jobj, final MyCallback callback) {
        //JSONObject parseResult = null;
        // 보내는 데이터
        Log.d(TAG, jobj);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj);
        final Call<LinkedTreeMap> responseData = httpService.loginUser(requestBody);

        responseData.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();
                        //HttpCookie httpCookie = new HttpCookie();

                        List<String> test = response.headers().values("set-cookie");
                        ArrayList<String> testS = new ArrayList<String>();
                        for (String sid : test) {
                            testS.add(sid.split(";\\s*")[0]);
                        }
                        Log.d(TAG, "cookie: "+testS.get(0));

                        cookie = testS.get(0);
                        result = new JSONObject("{\"status\":"+status+",\"message\":\""+messages+"\"}");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 받은 데이터 출력
                    Log.d(TAG, result.toString());

                } else {
                    String test = response.message();
                    Log.d(TAG, test);

                    // TODO error
                }

                callback.httpProcessing(result);
            }

            @Override
            public void onFailure(Throwable t) {

                // TODO 실패
                Log.d(TAG, t.toString());
                Log.d(TAG, "아예실패");
            }
        });

    }

    public void regBtn(String jobj, final MyCallback callback) {
        // 보내는 데이터
        Log.d(TAG, jobj);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj);
        Log.d(TAG, cookie);
        final Call<LinkedTreeMap> responseData = httpService.regBtn(cookie, requestBody);

        responseData.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();

                        result = new JSONObject("{\"status\":"+status+",\"message\":\""+messages+"\"}");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 받은 데이터 출력
                    Log.d(TAG, result.toString());

                } else {
                    String test = response.message();
                    Log.d(TAG, test);

                    // TODO error
                }

                callback.httpProcessing(result);
            }

            @Override
            public void onFailure(Throwable t) {

                // TODO 실패
                Log.d(TAG, t.toString());
                Log.d(TAG, "아예실패");
            }
        });

    }

    public void testSend() {
        Log.d(TAG, "testSend()");
        Log.d(TAG, client.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "test");
        Call<LinkedTreeMap> repos = httpService.testSend ();
        repos.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
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
