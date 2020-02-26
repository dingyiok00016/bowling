package com.cloudysea.ui;

import android.util.Log;

import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.net.OkHttpUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author roof 2019/10/24.
 * @email lyj@yhcs.com
 * @detail
 */
public class NetPresenter {
    private NetView mNetView;
    public NetPresenter(NetView netView){
        mNetView = netView;
    }



    public void getGameInfoSingleLine(final String id){
        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",id);
            OkHttpUtils.getInstance().postAsync(OkHttpUtils.GET_BOWLER_GAME, jsonObject1.toString(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.body() != null){
                        try{
                            ResponseBody body = response.body();
                            String string = body.string();
                            Log.d("Okhttp",string);
                            // 获取到basicInfo
                            BowlerGameSingLine gameSingLine = new Gson().fromJson(string,BowlerGameSingLine.class);
                            if(mNetView != null){
                                mNetView.getBowlerGame(gameSingLine);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getGameInfo(final String id,JSONObject jsonObject1){
        try {
            if(jsonObject1 == null){
                jsonObject1 = new JSONObject();
                jsonObject1.put("id",id);

            }
            OkHttpUtils.getInstance().postAsync(OkHttpUtils.GET_PLAYING_GAMES, jsonObject1.toString(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.body() != null){
                        try{
                            ResponseBody body = response.body();
                            String string = body.string();
                            Log.d("Okhttp",string);
                            // 获取到basicInfo
                            PlayingGame gameBasicInfo = new Gson().fromJson(string,PlayingGame.class);
                            if(mNetView != null){
                                mNetView.getBasicGameInfo(gameBasicInfo);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public  void getBowlerGameForTurn(String id,int turnNumber){
        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("gameId",id);
            jsonObject1.put("turnNumber",turnNumber + 1);
            OkHttpUtils.getInstance().postAsync(OkHttpUtils.GET_BOWLER_GAMESFORTURN, jsonObject1.toString(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.body() != null){
                        try{
                            ResponseBody body = response.body();
                            String string = body.string();
                            Log.d("Okhttp",string);
                            BowlerGameForTurn gameBasicInfo = new Gson().fromJson(string,BowlerGameForTurn.class);
                            mNetView.getBowlerGameForTurn(gameBasicInfo);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void getBowlerBasicInfo(final String id){
        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id",id);
            OkHttpUtils.getInstance().postAsync(OkHttpUtils.GET_BASIC_INFO, jsonObject1.toString(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.body() != null){
                        try{
                            ResponseBody body = response.body();
                            String string = body.string();
                            Log.d("Okhttp",string);
                            GameBasicInfo gameBasicInfo = new Gson().fromJson(string,GameBasicInfo.class);
                            mNetView.getBowlerBasicInfo(gameBasicInfo,id);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
