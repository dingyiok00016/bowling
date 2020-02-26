package com.cloudysea.net;

import android.content.OperationApplicationException;
import android.text.TextUtils;
import android.util.Log;

import com.cloudysea.BowlingApplication;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.ui.NetPresenter;
import com.cloudysea.ui.NetView;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.LogcatFileManager;
import com.cloudysea.utils.PermissionUtils;
import com.google.gson.JsonArray;
import com.zsoft.SignalA.ConnectionState;
import com.zsoft.SignalA.Hubs.HubConnection;
import com.zsoft.SignalA.Hubs.HubInvokeCallback;
import com.zsoft.SignalA.Hubs.HubOnDataCallback;
import com.zsoft.SignalA.Hubs.IHubProxy;
import com.zsoft.SignalA.Transport.Longpolling.LongPollingTransport;
import com.zsoft.SignalA.Transport.StateBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.cloudysea.BowlingApplication.HUB_URL;

public class WebSocketClientService implements NetView {
    private static WebSocketClientService mService;
    private NetPresenter mPresenter;
    private List<String> mAllIds = new ArrayList<>();


    private WebSocketClientService() {
        mPresenter = new NetPresenter(this);
    }

    public static WebSocketClientService getInstance(){
        if(mService == null){
            mService = new WebSocketClientService();
        }
        return mService;
    }

    public void handleMsg(String str){
        if(hub == null){
            return;
        }
        Log.d("HubConnection","handleMsg" + str);
        if(conn != null && conn.getCurrentState().getState() == ConnectionState.Connected){
            if(str != null && str.equalsIgnoreCase(BowlingUtils.LAST_SUB_ID)){

            }else{
                if(!TextUtils.isEmpty(BowlingUtils.LAST_SUB_ID) && mAllIds.contains(BowlingUtils.LAST_SUB_ID)){
                    List<String> list = new ArrayList<>();
                    list.add(BowlingUtils.LAST_SUB_ID);
                    mAllIds.remove(BowlingUtils.LAST_SUB_ID);
                    hub.Invoke("Unsubscribe", list, new HubInvokeCallback() {
                        @Override
                        public void OnResult(boolean succeeded, String response) {
                            Log.d("HubConnection","Unsubscribe" + succeeded + response);
                            LogcatFileManager.getInstance().writeLog("HubConnection","Unsubscribe" + succeeded);

                        }

                        @Override
                        public void OnError(Exception ex) {

                        }
                    });
                }
            }
            if(TextUtils.isEmpty(str)){
                return;
            }
            List<String> lists = new ArrayList<>();
            lists.add(str);
            mAllIds.add(str);
            MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
            if(mainActivity != null){
                mainActivity.getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(hub == null){
                            return;
                        }
                        hub.Invoke("Subscribe", lists, new HubInvokeCallback() {
                            @Override
                            public void OnResult(boolean succeeded, String response) {
                                Log.d("HubConnection","Subscribe" + succeeded + response);
                                LogcatFileManager.getInstance().writeLog("HubConnection","Subscribe" + succeeded);
                            }

                            @Override
                            public void OnError(Exception ex) {

                            }
                        });
                    }
                },300);
            }
        }
    }

    public boolean isConn(){
        if(conn != null && conn.getCurrentState().getState() == ConnectionState.Connected){
            return true;
        }
        LogcatFileManager.getInstance().writeLog("WebSocketClientService","not connected");
        return false;
    }


    /**
     * hub代理 panderman 2013-10-25
     */
    private IHubProxy hub = null;
    /**
     * 开启推送服务 panderman 2013-10-25
     */
    public void beginConnect(){
        buildNewConn();
        try {
            hub=conn.CreateHubProxy("OnlineGameHub");
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        if(hub != null){
            hub.On("SendMessage", new HubOnDataCallback()
            {
                @Override
                public void OnReceived(JSONArray args) {
                    Log.d("HubConnection","OnReceived" + args.toString());
                    LogcatFileManager.getInstance().writeLog("HubConnection","OnReceived" + args.toString());
                    parseObject(args);
                }
            });
            conn.Start();
        }
        }

        private boolean mIsSubscirb;

        public boolean isSubscribStatus(){
            return mIsSubscirb;
        }

    private void parseObject(JSONArray args){
        if(args != null){
            try {
                String action = args.getString(0);
                // 远程分的刷新
                if(action.equalsIgnoreCase("BowlerGameInfoChanged")){
                    if(args.length() >=2){
                        String jsonObjectString = args.getString(1);
                        JSONObject jsonObject = new JSONObject(jsonObjectString);
                        String onlineGameId = jsonObject.getString("BowlerGameId");
                        // 重新拉取分数
                        mPresenter.getGameInfoSingleLine(onlineGameId);
                    }
                // 远程队列的刷新
                }else if(action.equalsIgnoreCase("OnlineGameInfoChanged")){
                    if(args.length() >=2){
                        String jsonObjectString = args.getString(1);
                        JSONObject jsonObject = new JSONObject(jsonObjectString);
                        String onlineGameId = jsonObject.getString("OnlineGameId");
                        // 重新拉取分数
                        mPresenter.getBowlerGameForTurn(onlineGameId,0);
                    }
                    // 订阅成功
                }else if(action.equalsIgnoreCase("SubscribeDone")){
                    if(args.length() >= 2){
                        String jsonObjectString = args.getString(1);
                        JSONObject jsonObject = new JSONObject(jsonObjectString);
                        String clientId = jsonObject.getString("ClientId");
                        mIsSubscirb = false;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public void onStop(){
        if(conn != null){
            conn.Stop();
            hub = null;
            conn = null;
            mAllIds.clear();
        }
    }

 //   private final static String HUB_URL="http://192.168.1.199:8081";

    public void buildNewConn(){
        conn =new HubConnection(HUB_URL, BowlingApplication.getContext(), new LongPollingTransport()) {
            @Override
            public void OnError(Exception exception) {
                Log.d("HubConnection", "message" + exception.getMessage());
                LogcatFileManager.getInstance().writeLog("HubConnection","OnError" + exception.getMessage());
                exception.printStackTrace();
            }

            @Override
            public void OnMessage(String message) {
                Log.d("HubConnection", "message" + message);
                LogcatFileManager.getInstance().writeLog("HubConnection","message" + message);
            }

            @Override
            public void OnStateChanged(StateBase oldState, StateBase newState) {
                Log.d("HubConnection", "StateChanged" + newState.getState().name());
                LogcatFileManager.getInstance().writeLog("HubConnection","StateChanged" + newState.getState().name());
            }

        };
    }

    /**
     * hub链接
     */
    private HubConnection conn;

    @Override
    public void getBasicGameInfo(PlayingGame gameBasicInfo) {

    }

    @Override
    public void getBowlerGameForTurn(BowlerGameForTurn bowlerGameForTurn) {
        BowlingManager.getInstance().currentGame.executeListeners(bowlerGameForTurn);
    }

    @Override
    public void getBowlerGame(BowlerGameSingLine singLine) {
        BowlingManager.getInstance().singleGame.executeListeners(singLine);
    }

    @Override
    public void getBowlerBasicInfo(GameBasicInfo gameBasicInfo, String onLineId) {

    }

}
