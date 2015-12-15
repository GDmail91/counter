package com.example.administrator.counter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
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

    /**
     * 회원가입 프로세스
     * @param jobj JSON 파싱된 사용자 ID, PW
     * @param callback result 콜백
     */
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

    /**
     * 로그인 프로세스
     * @param jobj JSON 파싱된 사용자 ID, PW
     * @param callback result 콜백
     */
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

                        List<String> test = response.headers().values("set-cookie");
                        ArrayList<String> testS = new ArrayList<String>();
                        for (String sid : test) {
                            testS.add(sid.split(";\\s*")[0]);
                        }
                        Log.d(TAG, "cookie: " + testS.get(0));

                        ApplicationClass.app.setToken(testS.get(0));
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

    /**
     * 버튼 등록
     * @param jobj JSON 파싱된 보낼 정보
     * @param callback result 콜백
     */
    public void regBtn(String jobj, final MyCallback callback) {
        // 보내는 데이터
        Log.d(TAG, jobj);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj);
        cookie = ApplicationClass.app.getToken();
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

    /**
     * 버튼 기능 등록
     * @param jobj JSON 파싱된 기능정보
     * @param callback result 콜백
     */
    public void regFunc(String jobj, final MyCallback callback) {
        // 보내는 데이터
        Log.d(TAG, jobj);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj);
        cookie = ApplicationClass.app.getToken();
        Log.d(TAG, cookie);
        final Call<LinkedTreeMap> responseData = httpService.regFunc(cookie, requestBody);

        Log.d(TAG, "등록하는 데이터: "+jobj);
        responseData.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        Log.d(TAG, response.body().toString());
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();
                        JSONObject data = null;
                        if (temp.get("data") != null)
                            data = new JSONObject(temp.get("data").toString());

                        result = new JSONObject("{\"status\":" + status + ",\"message\":\"" + messages + "\",\"data\":\"" + data + "\"}");
                        Log.d(TAG, "성공");
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

    /**
     * 버튼 기능 초기화(카운트, 체크)
     * @param func 초기화할 기능
     * @param jobj JSON 파싱된 기능정보
     * @param callback result 콜백
     */
    public void resetFunc(String func, String jobj, final MyCallback callback) {
        // 보내는 데이터
        Log.d(TAG, jobj);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj);
        cookie = ApplicationClass.app.getToken();
        Log.d(TAG, cookie);
        final Call<LinkedTreeMap> responseData = httpService.resetFunc(cookie, func, requestBody);

        Log.d(TAG, "등록하는 데이터: "+jobj);
        responseData.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        Log.d(TAG, response.body().toString());
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();
                        JSONObject data = null;
                        if (temp.get("data") != null)
                            data = new JSONObject(temp.get("data").toString());

                        result = new JSONObject("{\"status\":" + status + ",\"message\":\"" + messages + "\",\"data\":\"" + data + "\"}");
                        Log.d(TAG, "성공");
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

    /**
     * 버튼 기능 정보 추가(타이머)
     * @param func 추가할 기능
     * @param jobj JSON 파싱된 기능정보
     * @param callback result 콜백
     */
    public void putFunc(String func, String jobj, final MyCallback callback) {
        // 보내는 데이터
        Log.d(TAG, jobj);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj);
        cookie = ApplicationClass.app.getToken();
        Log.d(TAG, cookie);
        final Call<LinkedTreeMap> responseData = httpService.putFunc(cookie, func, requestBody);

        Log.d(TAG, "등록하는 데이터: "+jobj);
        responseData.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        Log.d(TAG, response.body().toString());
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();
                        JSONObject data = null;
                        if (temp.get("data") != null)
                            data = new JSONObject(temp.get("data").toString());

                        result = new JSONObject("{\"status\":" + status + ",\"message\":\"" + messages + "\",\"data\":\"" + data + "\"}");
                        Log.d(TAG, "성공");
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

    /**
     * 버튼 정보 얻어옴
     * @param mac_addr 얻어올 버튼의 MAC Address
     * @param callback result 콜백
     */
    public void getBtn(String mac_addr, final Context context, final ReceiverCallback callback) {
        // 보내는 데이터
        cookie = ApplicationClass.app.getToken();
        Log.d(TAG, "쿠키: "+cookie);
        Log.d(TAG, "MAC_ADDR: "+mac_addr);
        final Call<LinkedTreeMap> responseData = httpService.getBtn(cookie, mac_addr);

        responseData.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        Log.d(TAG, response.body().toString());
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();
                        JSONObject data = new JSONObject();
                        if (temp.get("data") != null)
                            data = new JSONObject(new Gson().toJson(temp.get("data")));

                        result = new JSONObject("{\"status\":"+status+",\"message\":\""+messages+"\",\"data\":"+data.toString()+"}");

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

                callback.httpProcessing(context, result);
            }

            @Override
            public void onFailure(Throwable t) {

                // TODO 실패
                Log.d(TAG, t.toString());
                Log.d(TAG, "아예실패");
            }
        });

    }

    /**
     * 등록된 버튼 목록 얻어옴
     * @param callback result 콜백
     */
    public void getBtnList(final MyCallback callback) {
        // 보내는 데이터
        cookie = ApplicationClass.app.getToken();
        Log.d(TAG, "쿠키: "+cookie);
        final Call<LinkedTreeMap> responseData = httpService.getBtnList(cookie);

        responseData.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        Log.d(TAG, response.body().toString());
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();
                        JSONArray data = new JSONArray();
                        if (temp.get("data") != null)
                            data = new JSONArray(new Gson().toJson(temp.get("data")));

                        Log.d(TAG, "gson test: "+data.toString());

                        result = new JSONObject("{\"status\":"+status+",\"message\":\""+messages+"\",\"data\":"+data.toString()+"}");

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

    public void testSend(final MyCallback callback) {
        Log.d(TAG, "testSend()");

        JSONObject jobj =null;
        try {
             jobj = new JSONObject("{\"fid\" : \"2\"," +
                    " \"mac_addr\": \"TESTMACADDR3\"," +
                    " \"title\" : \"개밥주기\"" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jobj.toString());
        cookie = ApplicationClass.app.getToken();

        Log.d(TAG, jobj.toString());
        Call<LinkedTreeMap> repos = httpService.testSend(cookie, requestBody);
        repos.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response, Retrofit retrofit) {
                String test = response.message();
                if (response.isSuccess()) {
                    try {
                        // 받은 데이터
                        Log.d(TAG, response.body().toString());
                        LinkedTreeMap temp = response.body();
                        String status = temp.get("status").toString();
                        String messages = temp.get("message").toString();
                        String data = "";
                        if (temp.get("data") != null)
                            data = temp.get("data").toString();

                        result = new JSONObject("{\"status\":" + status + ",\"message\":\"" + messages + "\",\"data\":\"" + data + "\"}");
                        Log.d(TAG, "성공");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, test);
                callback.httpProcessing(result);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "실패");
            }
        });

    }

}
