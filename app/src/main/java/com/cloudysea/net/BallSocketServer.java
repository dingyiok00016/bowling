package com.cloudysea.net;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.UploadShareService;
import com.cloudysea.bean.AddNewScore;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.CreateAndJoinGameResult;
import com.cloudysea.bean.CurrentBowlerInfo;
import com.cloudysea.bean.CurrentSpeed;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.GetMembership;
import com.cloudysea.bean.GetScoreConguration;
import com.cloudysea.bean.HasOnlineGameResult;
import com.cloudysea.bean.LinkCloudUserBean;
import com.cloudysea.bean.NewVersionBean;
import com.cloudysea.bean.OnLineGameIdBean;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.bean.PublicBean;
import com.cloudysea.bean.ScoreListBean;
import com.cloudysea.bean.TryPasswordResult;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.controller.LocalExecutor;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.ui.NetPresenter;
import com.cloudysea.ui.NetView;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.JCifsUtil;
import com.cloudysea.utils.LogcatFileManager;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.views.BowlingChangeScoreDialog;
import com.cloudysea.views.BowlingFunctionSetDialog;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BallSocketServer implements NetView {

    private static final int SERVER_PORT = 8556;
    private static final String TAG = "BallSocketServer";
    private static final int MSG_SHOW_DIALOG = 0x02;
    private static final int MSG_NO_RESULT = MSG_SHOW_DIALOG + 1;
    private static final int MSG_SHOW_FUNCTION_DIALOG = MSG_NO_RESULT + 1;
    private static final int MSG_GET_GAME_INFO = MSG_SHOW_FUNCTION_DIALOG + 1;
    private static final int MSG_SHOW_REMOTE_SCORE = MSG_GET_GAME_INFO + 1;
    private static final int MSG_CREATE_REMOTE_FAILTED = MSG_SHOW_REMOTE_SCORE + 1;
    private static final int MSG_JOIN_GAME_FAILED = MSG_CREATE_REMOTE_FAILTED + 1;
    private static final int MSG_BASIC_INFO = MSG_JOIN_GAME_FAILED + 1;
    private static final int MSG_CURRENT_BOWLER_INFO = MSG_BASIC_INFO + 1;
    private static final int MSG_CURRENT_SPEED = MSG_CURRENT_BOWLER_INFO + 1;
    private static final int MSG_NOT_VIP = MSG_CURRENT_SPEED + 1;
    private static final int MSG_IS_VIP = MSG_NOT_VIP + 1;
    private static final int MSG_CONNECT_VIP_SUC = MSG_IS_VIP + 1;
    private static final int MSG_CONNECT_VIP_FAIL = MSG_CONNECT_VIP_SUC + 1;
    private static final int MSG_GET_CONFIG = MSG_CONNECT_VIP_FAIL + 1;
    private static final int MSG_ADD_NEW_SCORE = MSG_GET_CONFIG + 1;
    private static final int MSG_UPDATE_VERSION = MSG_ADD_NEW_SCORE + 1;
    private static final int MSG_UPDATE_ANIMATION = MSG_UPDATE_VERSION + 1;
    private static final int MSG_UPDATE_AD = MSG_UPDATE_ANIMATION + 1;
    private static BallSocketServer sInstance;
    // server返回 事件
    private static String EVENT_GAME_END = "GameEnded"; // 对局结束
    private static String EVENT_UPDATE_ONLINE_INFO = "UpdateOnlineGameInfo"; // 更新线上游戏信息
    private static String EVENT_QUERY_ONLINE_GAME = "QueryOnlineGame"; // 查找线上对局
    private static String EVENT_CREATE_GAME = "CreateAndJoinOnlineGame"; //创建线上对局
    private static String EVENT_GET_MEMBER_SHIP = "GetMembership"; // 获取vip信息
    private static String EVENT_JOIN_GAME = "JoinOnlineGame";
    private static String EVENT_CONNECT_VIP = "LinkCloudUser";
    private static String EVENT_CURRENT_BOWLER = "CurrentBowlerInfo"; // 当前球员信息
    private static String EVENT_CURRENT_SPEED = "CurrentSpeed";
    private static String EVENT_SCORE_CONFIG = "GetScoreConfiguration";
    private static String EVENT_ADD_SCORE = "AddNewScore";
    private static String EVENT_UPDATE_VERSION = "UpdateNewVersion";
    private static String EVENT_UPDATE_ANIMATION = "NotifyAnimationUpdated";
    private static String EVENT_UPDATE_AD = "NotifyAdUpdated";
    private NetPresenter mPresenter;
    private Handler mHandler;
    private ServerSocket server;
    private boolean isRunning = true;
    private Runnable serverConnect = new Runnable() {
        @Override
        public void run() {
            try {
                server = null;
                try {
                    Log.d(TAG, "server start to connect...");
                    //创建一个ServerSocket在端口4700监听客户请求
                    server = new ServerSocket(SERVER_PORT);
                } catch (Exception e) {
                    e.printStackTrace();//出错，打印出错信息
                }
                Log.d(TAG, "server create ...");
                while (true) {
                    Log.d(TAG, "create a new socket ...");
                    Socket socket = server.accept();
                    new SocketRunnable(socket).run();
                    //使用accept()阻塞等待客户请求，有客户socket=server.accept();//请求到来则产生一个Socket对象，并继续执行
                }

            } catch (Exception e) {
                Log.e(TAG, "excpetion");
                e.printStackTrace();//出错，打印出错信息
                StackTraceElement[] trace = e.getStackTrace();
                for (StackTraceElement traceElement : trace)
                    Log.e(TAG, traceElement.toString());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                run();
            }
        }
    };

    private BallSocketServer() {
        mPresenter = new NetPresenter(this);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SHOW_DIALOG:
                        PlayerBean playerBean = (PlayerBean) msg.obj;
                        MainActivity mainActivity = BowlingUtils.getMainActivity();
                        if (mainActivity != null) {
                            BowlingChangeScoreDialog changeScoreDialog = new BowlingChangeScoreDialog(mainActivity);
                            changeScoreDialog.setPlayBean(playerBean, playerBean.index);
                            changeScoreDialog.show();
                        }
                        break;
                    case MSG_NO_RESULT:
                        ToastUtil.showText(BowlingApplication.getContext(),R.string.wrong_pwd);
                        break;
                    case MSG_SHOW_FUNCTION_DIALOG:
                        MainActivity activity = BowlingUtils.getMainActivity();
                        if (activity != null) {
                            BowlingFunctionSetDialog functionSetDialog = new BowlingFunctionSetDialog(activity);
                            functionSetDialog.show();
                        }
                        break;
                    case MSG_GET_GAME_INFO:
                        HasOnlineGameResult hasOnlineGameResult = (HasOnlineGameResult) msg.obj;
                        if (mPresenter != null) {
                            mPresenter.getGameInfo(hasOnlineGameResult.Data.OnlineGameId, null);
                        }
                        break;
                    case MSG_SHOW_REMOTE_SCORE:
                        // 同步订阅远程id
                        HasOnlineGameResult hasOnlineGameResult1 = (HasOnlineGameResult) msg.obj;
                        WebSocketClientService.getInstance().handleMsg(hasOnlineGameResult1.Data.OnlineGameId);
                        mPresenter.getBowlerGameForTurn(hasOnlineGameResult1.Data.OnlineGameId, 0);
                        break;
                    case MSG_CREATE_REMOTE_FAILTED:
                        ToastUtil.showText(BowlingApplication.getContext(),BowlingApplication.getContext().getResources().getString(R.string.create_remote_failed) + msg.obj);
                        break;
                    case MSG_JOIN_GAME_FAILED:
                        ToastUtil.showText(BowlingApplication.getContext(),R.string.join_game_failed);
                        break;
                    case MSG_BASIC_INFO:
                        GameBasicInfo gameBasicInfo = (GameBasicInfo) msg.obj;
                        BowlingManager.getInstance().remoteGameNumbers.executeListeners(gameBasicInfo.getData().getTurnCount());
                        mPresenter.getBowlerGameForTurn(gameBasicInfo.getOnLineId(), 0);
                        break;
                    case MSG_CURRENT_BOWLER_INFO:
                        CurrentBowlerInfo currentBowlerInfo = (CurrentBowlerInfo) msg.obj;
                        BowlingManager.getInstance().currentBowlerInfo.executeListeners(currentBowlerInfo);
                        break;
                    case MSG_CURRENT_SPEED:
                        CurrentSpeed currentSpeed = (CurrentSpeed) msg.obj;
                        BowlingManager.getInstance().currentBowlerInfo.executeListeners(currentSpeed);
                        break;
                    case MSG_NOT_VIP:
                        ToastUtil.showText(BowlingApplication.getContext(),R.string.not_vip_member);
                        break;
                    case MSG_IS_VIP:
                        GetMembership getMembership = (GetMembership) msg.obj;
                        MainActivity getMainActivity = (MainActivity) BowlingUtils.getMainActivity();
                        if (getMainActivity != null) {
                            getMainActivity.setVipInfoInEditPlayerDialog(getMembership);
                            getMainActivity.setVipInfoInAddDialog(getMembership);
                        }
                        break;
                    case MSG_CONNECT_VIP_SUC:
                        ToastUtil.showText(BowlingApplication.getContext(),R.string.cloudy_vip_suc);
                        break;
                    case MSG_CONNECT_VIP_FAIL:
                        ToastUtil.showText(BowlingApplication.getContext(),R.string.cloudy_vip_fail);
                        break;
                    case MSG_GET_CONFIG:
                        GetScoreConguration getScoreConguration = (GetScoreConguration) msg.obj;
                        SharedPreferencesUtils.setAllConfigParam(getScoreConguration);
                        break;
                    case MSG_ADD_NEW_SCORE: // 添加分数
                        Log.d("BallSocketServer","msg_add_new_score");
                        AddNewScore addNewScore = (AddNewScore) msg.obj;
                        BowlingManager.getInstance().addScoreThenController.executeListeners(addNewScore);
                        break;
                    case MSG_UPDATE_VERSION: // 更新版本
                        NewVersionBean bean = (NewVersionBean) msg.obj;
                        Intent intent = new Intent(BowlingApplication.getContext(), UploadShareService.class);
                        intent.putExtra(UploadShareService.EXTRA_TASK_NAME,UploadShareService.GET_UPDATE_APK);
                        if(bean != null && bean.Data != null){
                            intent.putExtra(UploadShareService.EXTRA_URL,bean.Data.AppUrl);
                            intent.putExtra(UploadShareService.EXTRA_VERSION_NAME,bean.Data.VersionNumber);
                        }
                        BowlingApplication.getContext().startService(intent);
                        break;
                    case MSG_UPDATE_ANIMATION: // 更新动画
                        JCifsUtil.uploadImageAndAnimation(JCifsUtil.STYLE_UPLOAD_ANIMATION);
                        break;
                    case MSG_UPDATE_AD:
                        JCifsUtil.uploadImageAndAnimation(JCifsUtil.STYLE_UPLOAD_AD);
                        break;
                }

            }
        };
    }

    public static BallSocketServer getInstance() {
        if (sInstance == null) {

            sInstance = new BallSocketServer();
        }
        return sInstance;
    }

    public static int bytes2Int(byte[] bytes) {
        int value = 0;
        value = ((bytes[3] & 0xff) << 24) |
                ((bytes[2] & 0xff) << 16) |
                ((bytes[1] & 0xff) << 8) |
                (bytes[0] & 0xff);
        return value;
    }

    @Override
    public void getBasicGameInfo(PlayingGame gameBasicInfo) {
        BowlingManager.getInstance().gameForTurn.executeListeners(gameBasicInfo);
    }

    @Override
    public void getBowlerGameForTurn(BowlerGameForTurn bowlerGameForTurn) {
        BowlingManager.getInstance().currentGame.executeListeners(bowlerGameForTurn);
    }

    @Override
    public void getBowlerGame(BowlerGameSingLine singLine) {

    }

    @Override
    public void getBowlerBasicInfo(GameBasicInfo gameBasicInfo, String onLineId) {
        GameBasicInfo.DataBean dataBean = gameBasicInfo.getData();
        if (dataBean != null) {
            Message message = Message.obtain();
            message.what = MSG_BASIC_INFO;
            message.obj = gameBasicInfo;
            gameBasicInfo.setOnLineId(onLineId);
            mHandler.sendMessage(message);
        }
    }

    public void connect() {
        LocalExecutor.getInstance().addWork(serverConnect);
    }

    private byte[] recvBytes(InputStream is, int length) throws IOException {
        int tmpLength = 512; // 每次读取最大缓冲区大小
        byte[] ret = new byte[length];
        int readed = 0, offset = 0, left = length;
        byte[] bs = new byte[tmpLength];
        while (left > 0) {
            try {
                readed = is.read(bs, 0, Math.min(tmpLength, left));
                if (readed == -1)
                    break;
                System.arraycopy(bs, 0, ret, offset, readed);
            } finally {
                offset += readed;
                left -= readed;
            }
        }
        return ret;
    }

    public void onStop() {
        try {
            if (server != null) {
                LocalExecutor.getInstance().removeWork(serverConnect);
                server.close(); //关闭ServerSocket
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SocketRunnable implements Runnable {

        public Socket mSocket;
        public PrintWriter os;
        public InputStream is;

        public SocketRunnable(Socket socket) {
            mSocket = socket;
        }

        @Override
        public void run() {
            try {
                Log.d(TAG, "server success to connect step1...");
                LogcatFileManager.getInstance().writeLog(TAG, "server success to connect step1...");
                String line;
                //由Socket对象得到输入流，并构造相应的BufferedReader对象
                is = mSocket.getInputStream();
                //由Socket对象得到输出流，并构造PrintWriter对象
                os = new PrintWriter(mSocket.getOutputStream());
                //由系统标准输入设备构造BufferedReader对象
                BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
                Log.d(TAG, "server success to connect...");
                LogcatFileManager.getInstance().writeLog(TAG, "server success to connect...");
                byte[] lengthRet = new byte[4];
                is.read(lengthRet);
                int length = bytes2Int(lengthRet);
                Log.d(TAG, "length=" + length);
                byte[] bytes = recvBytes(is, length);
                String string = new String(bytes, "UTF-8");
                if (string.length() > 2) {
                    PublicBean publicBean = new Gson().fromJson(string, PublicBean.class);
                    Log.d(TAG, "getMsgFromClient:" + publicBean.Name + ":" + string);
                    LogcatFileManager.getInstance().writeLog(TAG, "getMsgFromClient:" + publicBean.Name + ":" + string);
                    if (publicBean != null && !TextUtils.isEmpty(publicBean.Name)) {
                        if (publicBean.Name.equalsIgnoreCase("VerifyPasswordResult")) {
                            TryPasswordResult result = new Gson().fromJson(string, TryPasswordResult.class);
                            if (result != null && result.Data != null) {
                                if (result.Data.IsSuccessful) {
                                    if (!TextUtils.isEmpty(result.Data.Action) && result.Data.Action.equalsIgnoreCase("ModifyConfig")) {
                                        if (BowlingUtils.lists.contains(result.Id)) {
                                            mHandler.sendEmptyMessage(MSG_SHOW_FUNCTION_DIALOG);
                                            BowlingUtils.lists.remove(result.Id);
                                        }
                                    } else {
                                        PlayerBean playerBean = BowlingUtils.getPlayBeanByString(result.Id);
                                        if (playerBean != null) {
                                            BowlingUtils.removePwdEntry(result.Id);
                                            Message message = Message.obtain();
                                            message.what = MSG_SHOW_DIALOG;
                                            message.obj = playerBean;
                                            mHandler.sendMessage(message);
                                        }
                                    }
                                } else {
                                    mHandler.sendEmptyMessage(MSG_NO_RESULT);
                                    BowlingUtils.removePwdEntry(result.Id);
                                }
                            }
                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_GAME_END)) {
                            // 当前本地界面重新获取一次数据
                            BowlingClient.getInstance().getScore(BowlingUtils.Global_LANE_NUMBER);
                            // 当前远程球局重新获取一次数据
                            BowlingClient.getInstance().buildRemoteRoundInfo();

                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_UPDATE_ONLINE_INFO)) {
                            //   WebSocketClientService.getInstance().handleMsg(publicBean.Id);
                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_QUERY_ONLINE_GAME)) {
                            HasOnlineGameResult hasOnlineGameResult = new Gson().fromJson(string, HasOnlineGameResult.class);
                            if (hasOnlineGameResult.Data != null && !hasOnlineGameResult.Data.HasOnlineGame) {
                                MainActivity mainActivity1 = (MainActivity) BowlingUtils.getMainActivity();
                                if (mainActivity1 != null) {
                                    mainActivity1.isRemoteShowAndSwitch();
                                    WebSocketClientService.getInstance().handleMsg("");
                                    OnLineGameIdBean gameIdBean = new OnLineGameIdBean();
                                    try {
                                        gameIdBean.laneNumber = Integer.valueOf(hasOnlineGameResult.LaneNumber);
                                        gameIdBean.onLineGameId = "";
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    BowlingManager.getInstance().onLineId.executeListeners(gameIdBean);
                                }

                            } else {
                                Log.d(TAG, "getMsgFromClient:" + publicBean.Name + ":" + string);
                                OnLineGameIdBean onLineGameIdBean = new OnLineGameIdBean();
                                onLineGameIdBean.onLineGameId = hasOnlineGameResult.Data.OnlineGameId;
                                try {
                                    onLineGameIdBean.laneNumber = Integer.valueOf(hasOnlineGameResult.LaneNumber);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                BowlingManager.getInstance().onLineId.executeListeners(onLineGameIdBean);
                                WebSocketClientService.getInstance().handleMsg(hasOnlineGameResult.Data.OnlineGameId);
                                BowlingUtils.LAST_SUB_ID = hasOnlineGameResult.Data.OnlineGameId;


                            }

                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_CREATE_GAME)) {
                            CreateAndJoinGameResult createAndJoinGameResult = new Gson().fromJson(string, CreateAndJoinGameResult.class);
                            MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
                            if (createAndJoinGameResult.Data != null && createAndJoinGameResult.Data.IsSuccessful) {
                                // 建立新对战成功,直接获取一次当前轮次的比赛
                                // 直接跳转远程界面
                                OnLineGameIdBean onLineGameIdBean = new OnLineGameIdBean();
                                onLineGameIdBean.onLineGameId = createAndJoinGameResult.Data.OnlineGameId;
                                WebSocketClientService.getInstance().handleMsg(createAndJoinGameResult.Data.OnlineGameId);
                                try {
                                    onLineGameIdBean.laneNumber = Integer.valueOf(createAndJoinGameResult.LaneNumber);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                BowlingManager.getInstance().onLineId.executeListeners(onLineGameIdBean);
                                if (mainActivity != null) {
                                    mainActivity.switchToRemotePage();
                                }
                            } else if (createAndJoinGameResult.Data != null && !createAndJoinGameResult.Data.IsSuccessful) {
                                Message message = Message.obtain();
                                message.what = MSG_CREATE_REMOTE_FAILTED;
                                message.obj = createAndJoinGameResult.Data.ErrorMessage;
                                mHandler.sendMessage(message);
                            }
                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_GET_MEMBER_SHIP)) {
                            GetMembership getMembership = new Gson().fromJson(string, GetMembership.class);
                            if (getMembership.getData() != null && !getMembership.getData().isHasMemberhsip()) {
                                mHandler.sendEmptyMessage(MSG_NOT_VIP);
                            } else {
                                Message message = Message.obtain();
                                message.obj = getMembership;
                                message.what = MSG_IS_VIP;
                                mHandler.sendMessage(message);
                            }

                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_JOIN_GAME)) {
                            CreateAndJoinGameResult createAndJoinGameResult = new Gson().fromJson(string, CreateAndJoinGameResult.class);
                            if (createAndJoinGameResult.Data != null && createAndJoinGameResult.Data.IsSuccessful) {
                                // join game 逻辑处理与加入对战一致
                                // 建立新对战成功,直接获取一次当前轮次的比赛
                                // 直接跳转远程界面
                                MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
                                OnLineGameIdBean onLineGameIdBean = new OnLineGameIdBean();
                                onLineGameIdBean.onLineGameId = createAndJoinGameResult.Data.OnlineGameId;
                                WebSocketClientService.getInstance().handleMsg(createAndJoinGameResult.Data.OnlineGameId);
                                try {
                                    onLineGameIdBean.laneNumber = Integer.valueOf(createAndJoinGameResult.LaneNumber);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                BowlingManager.getInstance().onLineId.executeListeners(onLineGameIdBean);
                                if (mainActivity != null) {
                                    mainActivity.switchToRemotePage();
                                }
                            } else {
                                mHandler.sendEmptyMessage(MSG_JOIN_GAME_FAILED);
                            }
                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_CONNECT_VIP)) {
                            LinkCloudUserBean linkCloudUserBean = new Gson().fromJson(string, LinkCloudUserBean.class);
                            if (linkCloudUserBean.getData() != null && linkCloudUserBean.getData().isIsSuccessful()) {
                                mHandler.sendEmptyMessage(MSG_CONNECT_VIP_SUC);
                            } else {
                                mHandler.sendEmptyMessage(MSG_CONNECT_VIP_FAIL);
                            }

                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_CURRENT_BOWLER)) {
                            CurrentBowlerInfo bowlerInfo = new Gson().fromJson(string, CurrentBowlerInfo.class);
                            Message message = Message.obtain();
                            message.what = MSG_CURRENT_BOWLER_INFO;
                            message.obj = bowlerInfo;
                            mHandler.sendMessage(message);

                        } else if (publicBean.Name.equalsIgnoreCase(EVENT_CURRENT_SPEED)) {
                            CurrentSpeed currentSpeed = new Gson().fromJson(string, CurrentSpeed.class);
                            Message message = Message.obtain();
                            message.what = MSG_CURRENT_SPEED;
                            message.obj = currentSpeed;
                            mHandler.sendMessage(message);
                        } else if(publicBean.Name.equalsIgnoreCase(EVENT_SCORE_CONFIG))
                        {
                            GetScoreConguration currentSpeed = new Gson().fromJson(string, GetScoreConguration.class);
                            Message message = Message.obtain();
                            message.what = MSG_GET_CONFIG;
                            message.obj = currentSpeed;
                            mHandler.sendMessage(message);
                        }else if(publicBean.Name.equalsIgnoreCase(EVENT_ADD_SCORE)){
                            Log.d("BallSocketServer","event_add_new_score_start");
                            AddNewScore addNewScore = new Gson().fromJson(string, AddNewScore.class);
                            Message message = Message.obtain();
                            message.what = MSG_ADD_NEW_SCORE;
                            message.obj = addNewScore;
                            mHandler.sendMessage(message);
                            Log.d("BallSocketServer","event_add_new_score_end");
                        }else if(publicBean.Name.equalsIgnoreCase(EVENT_UPDATE_VERSION)){
                            NewVersionBean newVersionBean = new Gson().fromJson(string,NewVersionBean.class);
                            Message message = Message.obtain();
                            message.what = MSG_UPDATE_VERSION;
                            message.obj = newVersionBean;
                            mHandler.sendMessage(message);
                        }else if(publicBean.Name.equalsIgnoreCase(EVENT_UPDATE_ANIMATION)){
                            Message message = Message.obtain();
                            message.what = MSG_UPDATE_ANIMATION;
                            mHandler.sendMessage(message);
                        }else if(publicBean.Name.equalsIgnoreCase(EVENT_UPDATE_AD)){
                            Message message = Message.obtain();
                            message.what = MSG_UPDATE_AD;
                            mHandler.sendMessage(message);
                        }
                        else {
                            ScoreListBean scoreListBean = new Gson().fromJson(string, ScoreListBean.class);
                            Log.d(TAG, "gson after:" + scoreListBean);
                            BowlingManager.getInstance().getScoreListListener.executeListeners(scoreListBean);
                        }
                    }

                }
                os.close(); //关闭Socket输出流
                is.close(); //关闭Socket输入流
                mSocket.close(); //关闭Socket
            } catch (Exception e) {
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (mSocket != null) {
                        mSocket.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                Log.e(TAG, "run excpetion");
                e.printStackTrace();//出错，打印出错信息
                StackTraceElement[] trace = e.getStackTrace();
                for (StackTraceElement traceElement : trace)
                    Log.e(TAG, traceElement.toString());
            }
        }
    }
}
