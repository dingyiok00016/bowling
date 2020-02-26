package com.cloudysea.net;

import android.util.Log;

import com.cloudysea.BowlingApplication;
import com.cloudysea.bean.GameBasicInfo;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author roof 2019/10/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class OkHttpUtils {
    private static OkHttpUtils sInstance;

    public static final String GET_PLAYING_GAMES = "GetPlayingGames";
    public static final String GET_BOWLER_GAMESFORTURN = "GetBowlerGamesForTurn";
    public static final String GET_BOWLER_GAME = "GetBowlerGame";
    public static final String GET_BASIC_INFO = "GetGameBasicInfo";
    public static final String QUERY_SUBCRIBE_STATE = "QuerySubscribeState";
    private  OkHttpClient client;

    public class HttpLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            Log.d("OkHttpClient", message);//okHttp的详细日志会打印出来
        }
    }

    private OkHttpUtils(){
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());//创建拦截对象
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这一句一定要记得写，否则没有数据输出
        client = new OkHttpClient.Builder().addInterceptor(logInterceptor).build();
    }

    public static OkHttpUtils getInstance(){
        if(sInstance == null){
            sInstance = new OkHttpUtils();
        }
        return sInstance;
    }






    public  void postAsync(final String url,String jsonString,Callback callback) throws Exception
    {
        if(jsonString != null){
            Log.d("OkHttpClient", jsonString.toString());
        }
        String newUrl = BowlingApplication.HUB_URL + "Api/OnlineGame/" + url;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        Request request = new Request.Builder()//创建Request 对象。
                .url(newUrl)
                .post(requestBody)//传递请求体   //与get的区别在这里
                .build();
        client.newCall(request).enqueue(callback);
    }
}
