package com.cloudysea.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.PublicBean;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.ui.SplashActivity;
import com.cloudysea.bean.ChannelRequestBean;
import com.cloudysea.bean.GetScoreBean;
import com.cloudysea.controller.LocalExecutor;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.DeviceIdUtil;
import com.cloudysea.utils.LogcatFileManager;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.cloudysea.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbSession;

import static com.cloudysea.utils.SharedPreferencesUtils.CHANNEL_IP;

/**
 * @author roof 2019/9/14.
 * @email lyj@yhcs.com
 * @detail 客户端连接
 */
public class BowlingClient {
    private static BowlingClient sInstance;
    private static final String TAG = "BowlingClient";
    private Socket socket;
    private BufferedReader is;
    private  OutputStream os;
    private static   String CLIENT_HOST = "192.168.1.199";
    private static final int CLIENT_PORT = 8555;
    private android.os.Handler handler;
    private final static int MSG_CONNECT_OUT =  0x999;
    private final static int MSG_CONNECT_RETRY = 0x01;
    private final static int MSG_CONNECT_STATE = 0x02;
    private Toast mToast;
    public  boolean sCheckValid = true;

    private  BowlingClient(){
        handler = new android.os.Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_CONNECT_OUT:
                        ToastUtil.showText(BowlingApplication.getContext(),BowlingApplication.getContext().getResources().getString(R.string.check_network) + CLIENT_HOST);
                        break;
                    case MSG_CONNECT_RETRY:
                        if(socket == null){
                            Log.d(TAG, "connect retry...");
                            ToastUtil.showText(BowlingApplication.getContext(), R.string.server_is_reconnect);
                            connect();
                            handler.sendEmptyMessageDelayed(MSG_CONNECT_RETRY,5000);
                        }
                        break;
                    case MSG_CONNECT_STATE:
                        if(socket != null){
                            handleMsg("{}");
                            WebSocketClientService.getInstance().isConn();
                            handler.sendEmptyMessageDelayed(MSG_CONNECT_STATE,5000);
                        }
                        break;
                }
            }
        };
    }



    public static BowlingClient getInstance(){
        if(sInstance == null){
            sInstance = new BowlingClient();
        }
        return  sInstance;
    }



    private Runnable startConnect = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG, "client start to connect...");
                LogcatFileManager.getInstance().writeLog(TAG,"client start to connect...");
                //向本机的4700端口发出客户请求
                CLIENT_HOST = (String) SharedPreferencesUtils.getParam(CHANNEL_IP,"192.168.1.199");
                Log.d(TAG, "client start to connect...host:" + CLIENT_HOST);
                if(socket != null){
                    return;
                }
                socket = new Socket(CLIENT_HOST, CLIENT_PORT);
                //由系统标准输入设备构造BufferedReader对象
                BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
                //由Socket对象得到输出流，并构造PrintWriter对象
                os = socket.getOutputStream();
                Log.d(TAG, "client connectted...");
                LogcatFileManager.getInstance().writeLog(TAG,"client connectted...");
                // 连接切换，由无网切换到有网
                if(mIsloading){
                    mIsloading = false;
                    jumpToWorkActivity();
                }
                // 正常连接之后,开启连接检测
                handler.removeMessages(MSG_CONNECT_STATE);
                handler.sendEmptyMessageDelayed(MSG_CONNECT_STATE,5000);
            } catch (Exception e) {
                handler.sendEmptyMessage(MSG_CONNECT_OUT);
                e.printStackTrace();//出错，打印出错信息
                StackTraceElement[] trace = e.getStackTrace();
                for (StackTraceElement traceElement : trace)
                    Log.e(TAG, traceElement.toString());
                close();
                if(!mIsloading){
                    jumpToLoadingActivity();
                }
            }
        }
    };
    public boolean mIsloading = false;
    private boolean mWorkActivity = false;

    public void jumpToWorkActivity(){
        if(!sCheckValid){
            return;
        }
        Activity activity = BowlingUtils.getTopActivity();
        if(activity instanceof SplashActivity){
            mIsloading = false;
            mWorkActivity = true;
            Intent intent = new Intent(activity,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();
            Log.d(TAG,"jump to workActivity");
        }
    }


    public void jumpToLoadingActivity(){
        Activity activity = BowlingUtils.getTopActivity();
        if(activity instanceof MainActivity){
            Intent intent = new Intent(activity,SplashActivity.class);
            intent.putExtra(SplashActivity.EXTRA_START_CLOCK,false);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BowlingApplication.getContext().startActivity(intent);
            activity.finish();
            mIsloading = true;
            Log.d(TAG,"jump to splashActivity");
        }else{
            /*Intent intent = new Intent(BowlingApplication.getContext(),SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BowlingApplication.getContext().startActivity(intent);*/
        }
    }

    public void connect(){
            if(socket == null || !socket.isConnected()){
                LocalExecutor.getInstance().addWork(startConnect);
            }
            handler.removeMessages(MSG_CONNECT_RETRY);
            handler.sendEmptyMessageDelayed(MSG_CONNECT_RETRY,5000);

    }


    public void getBowlerInfo(int LaneNumber){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode","120");
            jsonObject.put("Id",UUID.randomUUID().toString());
            jsonObject.put("LaneNumber",LaneNumber + "");
            jsonObject.put("Name","CurrentBowlerInfo");
            jsonObject.put("Type","0");
            JSONObject child = new JSONObject();
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public void getScore(int LaneNumber){
        GetScoreBean bean = new GetScoreBean();
        bean.AuthCode = "120";
        bean.Id = UUID.randomUUID().toString();
        bean.LaneNumber = LaneNumber + "";
        bean.Name = bean.getName();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode",bean.AuthCode);
            jsonObject.put("Id",bean.Id);
            jsonObject.put("LaneNumber",bean.LaneNumber);
            jsonObject.put("Name",bean.Name);
            jsonObject.put("Type",bean.Type);
            JSONObject child = new JSONObject();
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void connectRequest(){
        ChannelRequestBean bean = new ChannelRequestBean();
        bean.AuthCode = "120";
        bean.Id = UUID.randomUUID().toString();
        bean.LaneNumber = "1";
        bean.Name = bean.getName();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode",bean.AuthCode);
            jsonObject.put("Id",bean.Id);
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER + "");
            jsonObject.put("Name",bean.Name);
            jsonObject.put("Type",bean.Type);
            JSONObject child = new JSONObject();
            child.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER + "");
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 获取config配置
    private boolean mShouldGetConfig = true;
    public void shouldGetConfig(){
        if(mShouldGetConfig){
            mShouldGetConfig = false;
            connectConfig();
        }
    }

    public void checkDevice(){
        PublicBean bean = new PublicBean();
        bean.AuthCode = "120";
        bean.Id = UUID.randomUUID().toString();
        bean.LaneNumber = "1";
        bean.Name = "ValidateApp";
        bean.Type = "0";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode",bean.AuthCode);
            jsonObject.put("Id",bean.Id);
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER + "");
            jsonObject.put("Name",bean.Name);
            jsonObject.put("Type",bean.Type);
            JSONObject child = new JSONObject();
            jsonObject.put("Data",child);
            child.put("DeviceInfo", DeviceIdUtil.getDeviceId(BowlingApplication.getContext()));
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void connectConfig(){
        PublicBean bean = new PublicBean();
        bean.AuthCode = "120";
        bean.Id = UUID.randomUUID().toString();
        bean.LaneNumber = "1";
        bean.Name = "GetScoreConfiguration";
        bean.Type = "0";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode",bean.AuthCode);
            jsonObject.put("Id",bean.Id);
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER + "");
            jsonObject.put("Name",bean.Name);
            jsonObject.put("Type",bean.Type);
            JSONObject child = new JSONObject();
            child.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER + "");
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleMsg(final String str){
            LocalExecutor.getInstance().addWork(new Runnable() {
                @Override
                public void run() {
                    synchronized (BowlingClient.class){
                        try {
                            if(socket != null && socket.isConnected() && os != null &&
                                    str != null){
                                os.write(toBinary(str.getBytes().length));
                                os.write(str.getBytes());
                                //刷新输出流，使Server马上收到该字符串
                                os.flush();
                                //在系统标准输出上打印读入的字符串
                                Log.d(TAG,"Client:" + toBinary(getReadLineLength(str)) +str);
                                LogcatFileManager.getInstance().writeLog(TAG,"Client:" + toBinary(getReadLineLength(str)) +str);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            StackTraceElement[] trace = e.getStackTrace();
                            for (StackTraceElement traceElement : trace)
                                Log.e(TAG, traceElement.toString());
                            close();
                            jumpToLoadingActivity();
                        }
                    }
                }
            });

    }

    public void getGameInfo(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode","120");
            jsonObject.put("Id",UUID.randomUUID().toString());
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("Name","CurrentGameInfo");
            jsonObject.put("Type","1");
            JSONObject child = new JSONObject();
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buildRemoteRoundInfo(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode","120");
            jsonObject.put("Id",UUID.randomUUID().toString());
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("Name","QueryOnlineGame");
            jsonObject.put("Type","1");
            JSONObject child = new JSONObject();
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        if(socket != null){
            try {
                LocalExecutor.getInstance().removeWork(startConnect);
                os.close(); //关闭Socket输出流
                is.close(); //关闭Socket输入流
                socket.close(); //关闭Socket
                socket = null;
            }catch (Exception e){
                e.printStackTrace();
                socket = null;
            }
        }
    }


    /**
     * 将一个int数字转换为二进制的字符串形式。
     * @param num 需要转换的int类型数据
     * @return 二进制的字符串形式
     */
    public static byte[] toBinary(int num) {
        byte[] bytes5 = new byte[4];
        bytes5[0] = (byte)(num & 0xFF);
        bytes5[1] = (byte)(num >> 8 & 0xFF);
        bytes5[2] = (byte)(num >> 16 & 0xFF);
        bytes5[3] = (byte)(num >> 24 & 0xFF);
        return bytes5;
    }

    private static int getReadLineLength(String str){

        int length = 0;
        if(str == null ){
            length = 0;
        }else {
            length = str.length();
        }
        return length;
    }


}
