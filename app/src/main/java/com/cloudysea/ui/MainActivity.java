package com.cloudysea.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.AddOrSubPlayderBean;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.CurrentBowlerInfo;
import com.cloudysea.bean.CurrentSpeed;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.GetMembership;
import com.cloudysea.bean.ImageSetBean;
import com.cloudysea.bean.LocalGameInfo;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlerGameForTurnListener;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.net.WebSocketClientService;
import com.cloudysea.utils.BottomPersonInfoHelper;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.DoubleCheckUtils;
import com.cloudysea.utils.LogcatFileManager;
import com.cloudysea.utils.PermissionUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.views.BottomFunctionLinerLayout;
import com.cloudysea.views.BowlingAdDialog;
import com.cloudysea.views.BowlingAddorDeletePlayerDialog;
import com.cloudysea.views.BowlingConnectVipDialog;
import com.cloudysea.views.BowlingEditPlayerDialog;
import com.cloudysea.views.BowlingPasswordDialog;
import com.cloudysea.views.BowlingRemoteScoreDiloag;
import com.cloudysea.views.BowlingResetDeviceDialog;
import com.cloudysea.views.BowlingSeaVideoView;
import com.cloudysea.views.BowlingSwitchDialog;
import com.cloudysea.views.BowlingVideoPlayDialog;
import com.cloudysea.views.HorizontalScoreContainer;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends BasePhotoActivity implements NetView {

    private static final String TAG = "MainActivity";
    private NetPresenter mPresenter;
    private ImageView mIvSwitch;
    private BottomPersonInfoHelper mHelper;
    private static final int REQUEST_PERMISSION_CODE = 0x999;
    private static final int REQUEST_PERMISSION_CODE_CAMERA = 0x998;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] permissions_camera = new String[]{Manifest.permission.CAMERA};

    private RelativeLayout mRlShowBottonFunction; // 显示底部按钮
    private TextView mTvSwichText;
    private BottomFunctionLinerLayout mLlBottonFunction; // 底部功能
    private BowlingSeaVideoView mSeaVideoView;
    private static int mMaxCount;
    private boolean mBound; // 是否bound
    RemoteBattlerDialog mDialog;
    private BowlingRemoteScoreDiloag mScoreDialog;
    private boolean mIsSetMode;
    private BowlingAdDialog mAdDialog;

    // 更新当前球员信息
    private BaseListener currentBowlerInfoBaseListener = new BaseListener<Object>() {
        @Override
        public void execute(Object object) {
            if(mHelper != null){
                // 当前球员信息
                if(object instanceof CurrentBowlerInfo){
                    mHelper.setPlayBean(mLocalMatchFragment.getPlayBean());
                    CurrentBowlerInfo currentBowlerInfo1 = (CurrentBowlerInfo) object;
                    CurrentBowlerInfo.DataBean dataBean = BottomPersonInfoHelper.getCurrentDataBean(currentBowlerInfo1);
                    if(dataBean == null){
                        if(!BowlingApplication.sIsTvMode){
                            mHelper.hide();
                        }
                    }else if(dataBean.isHasBowler()){
                        mHelper.show();
                    }
                    mHelper.setCurrentBowlerInfo(currentBowlerInfo1);
                }else if(object instanceof CurrentSpeed){
                    CurrentSpeed currentSpeed = (CurrentSpeed) object;
                    mHelper.setSpeed(currentSpeed);
                }
            }
        }
    };


    private AnimationConfigHolder mHolder;
    private void initAnimation(View view, Context context){
        if(BowlingApplication.sIsTvMode){
            mHolder = new AnimationConfigHolder(this);
        }
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BowlingClient.getInstance().checkDevice();
        WebSocketClientService.getInstance().beginConnect();
        setContentView(R.layout.activity_main);
        initView();
        // 初始化动画及其瓶位图显示
        initAnimation(getWindow().getDecorView(),this);
        initListener();
        checkPermission();
        int height = getResources().getDisplayMetrics().heightPixels;
        int width = getResources().getDisplayMetrics().widthPixels;
        float destiny = getResources().getDisplayMetrics().density;
        Log.d(TAG, "heightdpi=" + height / destiny + "widthdpi=" + width / destiny + "destiny=" + destiny);
    }

    private void checkPermission() {
        PermissionUtils.checkAndRequestMorePermissions(this, permissions, REQUEST_PERMISSION_CODE);
    }

    public String getRemoteOnlineId() {
        return mLocalMatchFragment.getRemoteOnlineId();
    }

    public boolean hasRemoetOnlineId() {
        return !TextUtils.isEmpty(mLocalMatchFragment.getRemoteOnlineId());
    }

    public boolean isWatchStatus(){
        return mLocalMatchFragment.isWatchStatus();
    }

    public void resetRemoteFragment(){
        if(mRemoteMatchFragment != null && mRemoteMatchFragment.isAdded() && !mRemoteMatchFragment.isDetached()){
            mRemoteMatchFragment.resetCurrentMatch();
        }
    }



    private void initView() {
        mTvSwichText = (TextView) findViewById(R.id.tv_activity_main_text);
        mRlShowBottonFunction = (RelativeLayout) findViewById(R.id.rl_show_function);
        mRlShowBottonFunction.getLayoutParams().width *= BowlingUtils.Glbal_SIZE_RADIO;
        mRlShowBottonFunction.getLayoutParams().height *= BowlingUtils.Glbal_SIZE_RADIO;
        mLlBottonFunction = (BottomFunctionLinerLayout) findViewById(R.id.ll_bottom_function);
        mLlBottonFunction.getLayoutParams().width *= BowlingUtils.Glbal_SIZE_RADIO;
        mLlBottonFunction.getLayoutParams().height *= BowlingUtils.Glbal_SIZE_RADIO;
        mIvSwitch = (ImageView) findViewById(R.id.iv_main_switch);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_main_switch);
        if(!BowlingUtils.isPad()){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            params.width *=BowlingUtils.Glbal_SIZE_RADIO;
            params.height *=BowlingUtils.Glbal_SIZE_RADIO;
            params.bottomMargin = (int) (24 * DeviceUtils.getDestiny());
        }
        mIvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogcatFileManager.getInstance().writeLog(TAG,"近远程界面发生了切换,切换到" + (mCurrentFragment == mLocalMatchFragment ? "近程" : "远程"));
                if (mIvSwitch.getTag() == null) {
                    if (mLocalMatchFragment.hasRemoteMatch()) {
                        // 切换到远程球局的同时，重新做一次订阅
                        WebSocketClientService.getInstance().handleMsg(mLocalMatchFragment.getRemoteOnlineId());
                        mHelper.hide();
                        mIvSwitch.setTag(Boolean.TRUE);
                        mIvSwitch.setImageResource(R.drawable.btn_remote);
                        mTvSwichText.setText(R.string.remote);
                        if (mRemoteMatchFragment == null) {
                            mRemoteMatchFragment = new RemoteMatchFragment();
                        }
                        mLlBottonFunction.setVisibility(View.GONE);
                        mRlShowBottonFunction.setVisibility(View.GONE);
                        mHelper.hide();
                        showFragment(mRemoteMatchFragment);
                    } else {
                        ToastUtil.showText(getApplicationContext(),R.string.no_remote_match);
                    }
                } else {
                    mIvSwitch.setTag(null);
                    mRlShowBottonFunction.setVisibility(View.VISIBLE);
                    mIvSwitch.setImageResource(R.drawable.btn_local);
                    mTvSwichText.setText(R.string.local);
                    showFragment(mLocalMatchFragment);
                    mHelper.show();
                }
            }
        });
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_person_info_container);
        addBottomLayoutInfo(frameLayout);
        DisplayMetrics displayMetrics = BowlingApplication.getContext().getResources().getDisplayMetrics();
        int dy = (int) ((int) ((int) (90 * (displayMetrics.widthPixels / 960F)) - 90 * displayMetrics.density) * 0.5);
        frameLayout.getLayoutParams().height += dy;
        mHelper = BottomPersonInfoHelper.getInstance(frameLayout);
        mHelper.hide();
        mLocalMatchFragment = new LocalMatchFragment();
        View functionView = findViewById(R.id.view_to_function_dialog);
        functionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nineClick();
            }
        });
        // 手机适配
        if(!BowlingUtils.isPad()){
            functionView.getLayoutParams().width *= 0.5F;
        }
        showFragment(mLocalMatchFragment);
    }

    private void addBottomLayoutInfo(FrameLayout frameLayout){
        if(BowlingUtils.isPad()){
            View view = View.inflate(this,R.layout.layout_person_info_bottom,null);
            frameLayout.addView(view,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        }else{
            frameLayout.getLayoutParams().height = (int) (frameLayout.getLayoutParams().height *(BowlingUtils.Global_SIZE_BOTTOM) + 12 * DeviceUtils.getDestiny());
            View view = View.inflate(this,R.layout.layout_person_info_bottom_mobile,null);
            frameLayout.addView(view,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public boolean isRemoteShowAndSwitch() {
        if (mCurrentFragment == mRemoteMatchFragment) {
            mIvSwitch.post(new Runnable() {
                @Override
                public void run() {
                    mIvSwitch.performClick();
                }
            });
            return true;
        }
        return false;
    }

    public boolean isRemote() {
        if (mCurrentFragment == mRemoteMatchFragment) {
            return true;
        }
        return false;
    }


    private Fragment mCurrentFragment;
    private LocalMatchFragment mLocalMatchFragment;
    private RemoteMatchFragment mRemoteMatchFragment;

    private void showFragment(Fragment fragment) {
        if (mCurrentFragment != fragment) {//  判断传入的fragment是不是当前的currentFragmentgit
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);//  不是则隐藏
            }
            mCurrentFragment = fragment;  //  然后将传入的fragment赋值给currentFragment
            if (!fragment.isAdded()) { //  判断传入的fragment是否已经被add()过
                transaction.add(R.id.fl_main_fragment_container, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }


    private static float mOfffset;

    public static boolean vpIsAllScreen() {
        return mOfffset == 0F || mOfffset == 1F;
    }


    public void switchToRemotePage() {
        mIvSwitch.post(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                if (mCurrentFragment != mRemoteMatchFragment) {
                    hideBottonFunction();
                    mIvSwitch.performClick();
                    if(mLocalMatchFragment.hasMatch()){
                        mHelper.show();
                    }
                }
            }
        });
    }

    public void showOrHideBottomBowlerInfo(boolean hasSocre){
        if(hasSocre){
            mHelper.show();
        }else{
            mHelper.hide();
        }
    }

    private BowlingEditPlayerDialog mEditPlayerDialog;
   // 人员编辑逻辑
   public void setEditPlayerDialog(BowlingEditPlayerDialog dialog){
       mEditPlayerDialog = dialog;
   }

   public void setVipInfoInEditPlayerDialog(GetMembership getMembership){
       if(mEditPlayerDialog != null && mEditPlayerDialog.isShowing()){
           mEditPlayerDialog.setVipInfo(getMembership);
       }
   }

   private BowlingAddorDeletePlayerDialog mAddDeleteDialog;
   public void setAddorDeleteDialog(BowlingAddorDeletePlayerDialog addDeleteDialog){
       mAddDeleteDialog = addDeleteDialog;
   }

   public void setVipInfoInAddDialog(GetMembership getMembership){
       if(mAddDeleteDialog != null && mAddDeleteDialog.isShowing()){
           mAddDeleteDialog.setVipInfo(getMembership);
       }
   }

   public void setExchangeViewForScoreAdapter(){


       List<PlayerBean> playerBeans = mLocalMatchFragment.getPlayBean();
        boolean isExchange = false;
       if(playerBeans != null && playerBeans.size() > 0){
           isExchange = playerBeans.get(0).IsExchangeMode;
       }
       List<Integer> targets = new ArrayList<>();
       if(BowlingUtils.CURRENT_EXCHANGE != null){
           if(isExchange){
               for(int i = 0; i < BowlingUtils.CURRENT_EXCHANGE.length;i++){
                   int targetPosition = setSingleExchangeView(playerBeans,BowlingUtils.CURRENT_EXCHANGE[i]);
                   if(targetPosition != -1){
                       targets.add(targetPosition);
                   }
               }
           }
       }
       setOtherExchangeView(targets);
   }

   private void setOtherExchangeView(List<Integer> targets){
       ListView listView = mLocalMatchFragment.getListView();
       int firstPosition = listView.getFirstVisiblePosition();
       int lastPosition = listView.getLastVisiblePosition();
       for(int i = 0; i <= lastPosition - firstPosition;i++){
           if(!targets.contains(i + firstPosition)){
               HorizontalScoreContainer view = (HorizontalScoreContainer) listView.getChildAt(i);
               if(view != null){
                   view.dispatchExchangeEndEvent();
               }
           }
       }
   }

   private int setSingleExchangeView(List<PlayerBean> playerBeans,CurrentBowlerInfo.DataBean bowler_info){
       int targetPosition = -1;
       for(int i = 0; i < playerBeans.size();i++){
           if(bowler_info.getBowlerId().equalsIgnoreCase(playerBeans.get(i).Id)){
               targetPosition = i;
               break;
           }
       }

       if(targetPosition != -1){
           ListView listView = mLocalMatchFragment.getListView();
           int visiblePosition = listView.getFirstVisiblePosition();
           if (targetPosition - visiblePosition >= 0) {
               //得到要更新的item的view
               HorizontalScoreContainer view = (HorizontalScoreContainer) listView.getChildAt(targetPosition - visiblePosition);
               Log.d("MainActivity","view==null"  + (view != null) + ",fianlPositon=" + (targetPosition - visiblePosition));
               if(view != null){
                   view.dispatchExchangeStartEvent(bowler_info.IsLeftLane);
               }
           }
       }
       return targetPosition;
   }

   public void setCurrentViewForScoreAdapter(){
       if(BowlingUtils.CURRENT_BOWLER_ID == null){
           ListView listView = mLocalMatchFragment.getListView();
           if(listView != null){

           }
           return;
       }
       List<PlayerBean> playerBeans = mLocalMatchFragment.getPlayBean();
       int targetPosition = -1;
       for(int i = 0; i < playerBeans.size();i++){
           if(BowlingUtils.CURRENT_BOWLER_ID.equalsIgnoreCase(playerBeans.get(i).Id)){
               targetPosition = i;
               break;
           }
       }

       if(targetPosition != -1){
           ListView listView = mLocalMatchFragment.getListView();
           int visiblePosition = listView.getFirstVisiblePosition();
           if (targetPosition - visiblePosition >= 0) {
               //得到要更新的item的view
               HorizontalScoreContainer view = (HorizontalScoreContainer) listView.getChildAt(targetPosition - visiblePosition);
               if(view != null){
                   view.dispatchCoverEvent();
               }
           }
       }

   }




    private void initListener() {
        mRlShowBottonFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottonFunction();
            }
        });
        mLlBottonFunction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (DoubleCheckUtils.isCanAble()) {
                            hideBottonFunction();
                            if(mLocalMatchFragment.hasMatch()){
                                mHelper.show();
                            }
                        }
                        break;
                    case 1:
                        remoteBattler();
                        break;
                    case 2:
                        if(mCurrentFragment == mLocalMatchFragment){
                            if(hasLocalMatch()){
                                BowlingConnectVipDialog connectVipDialog = new BowlingConnectVipDialog(MainActivity.this);
                                connectVipDialog.setPlayBeans(mLocalMatchFragment.getPlayBean());
                                connectVipDialog.show();
                            }
                        }else{
                            ToastUtil.showText(MainActivity.this,R.string.you_in_a_remote_game);
                        }
                        break;
                    case 3:
                        if(mCurrentFragment == mRemoteMatchFragment){
                            ToastUtil.showText(MainActivity.this,R.string.you_in_a_remote_game);
                            return;
                        }
                        if(DoubleCheckUtils.isCanAble()){
                            showAddOrDeletePlayerDialog();
                        }
                        break;
                    case 4:
                        if(mCurrentFragment == mRemoteMatchFragment){
                            ToastUtil.showText(MainActivity.this,R.string.you_in_a_remote_game);
                            return;
                        }
                        ToastUtil.showText(MainActivity.this,R.string.long_click_to_edit);
                        break;
                    case 5:
                        if(hasRemoetOnlineId()){
                            ToastUtil.showText(MainActivity.this,R.string.you_in_a_remote_game);
                            return;
                        }
                        ToastUtil.showText(MainActivity.this,R.string.long_click_to_edit_avator);
                        break;
                    case 6:
                        if (DoubleCheckUtils.isCanAble()) {
                            resetDevice();
                        }
                        break;
                    case 7:
                        if (DoubleCheckUtils.isCanAble()) {
                            changswitch();
                        }
                        break;
                }
            }
        });
        mPresenter = new NetPresenter(this);
        BowlingManager.getInstance().gameForTurn.addListener(runnable);
        BowlingManager.getInstance().currentBowlerInfo.addListener(currentBowlerInfoBaseListener);
    }

    public void buildRemoteRoundInfo() {
        BowlingClient.getInstance().buildRemoteRoundInfo();
    }
    private BowlerGameForTurnListener runnable = new BowlerGameForTurnListener() {

        @Override
        public void execute(final PlayingGame scoreListBean) {
            if (!isFinishing()) {
                mIvSwitch.post(new Runnable() {
                    @Override
                    public void run() {
                        // 刷新球局信息
                        if (mDialog == null) {
                            mDialog = new RemoteBattlerDialog(MainActivity.this);
                        }
                        if(mDialog.isShowing()){
                            mDialog.setPlayingGame(scoreListBean);
                        }
                    }
                });
            }
        }
    };

    public void getPlayingGame(JSONObject jsonObject){
        mPresenter.getGameInfo("",jsonObject);
    }



    // 添加或者删减球员
    public void showAddOrDeletePlayerDialog() {
        if(mLocalMatchFragment != null && !mLocalMatchFragment.isDetached()){
            mLocalMatchFragment.showAddOrDeletePlayerDialog();
        }
    }

    // 重置装备
    private void resetDevice() {
        if(mLocalMatchFragment != null && !mLocalMatchFragment.isDetached()){
            if(mLocalMatchFragment.hasMatch()){
                BowlingResetDeviceDialog dialog = new BowlingResetDeviceDialog(this);
                dialog.show();
            }
        }
    }


    private void remoteBattler() {
        if (mDialog == null) {
            mDialog = new RemoteBattlerDialog(this);
        }
        mDialog.show();
        mPresenter.getGameInfo("",null);
    }

    public boolean hasLocalMatch() {
        if (mLocalMatchFragment != null) {
            return mLocalMatchFragment.hasMatch();
        }
        return false;
    }

    // 切换交换
    private void changswitch() {
        if(mLocalMatchFragment != null && !mLocalMatchFragment.isDetached()){
            if(mLocalMatchFragment.hasMatch()){
                BowlingSwitchDialog dialog = new BowlingSwitchDialog(this);
                dialog.show();
            }
        }
    }

    // 构建添加球员data
    private void buildAddPlayerData() {
        AddOrSubPlayderBean bean = new AddOrSubPlayderBean();
        bean.Id = UUID.randomUUID().toString();
        bean.AuthCode = "120";
        bean.Name = bean.getName();
        AddOrSubPlayderBean.AddOrSubPlayderData data = new AddOrSubPlayderBean.AddOrSubPlayderData();
        data.AddCount = 1;
        bean.Data = data;
        BowlingClient.getInstance().handleMsg(bean.toString());
    }


    @Override
    protected void onDestroy() {
        BowlingManager.getInstance().getScoreListListener.clearListener();
        BowlingManager.getInstance().changeChannelListener.clearListener();
        BowlingManager.getInstance().gameForTurn.removeListener(runnable);
        BowlingManager.getInstance().currentBowlerInfo.removeListener(currentBowlerInfoBaseListener);
        if(mHolder != null){
            mHolder.onDestory();
        }
        WebSocketClientService.getInstance().onStop();
        BottomPersonInfoHelper.destory();
        super.onDestroy();
    }

    @Override
    public void onPickPhotoResult(Uri uri) {

    }

    private boolean isFromDialog = false;

    private void verifyStoragePermission(Activity activity) {
        isFromDialog = true;
        //1.检测权限
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PermissionChecker.PERMISSION_GRANTED) {
            //2.没有权限，弹出对话框申请
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION_CODE);
        } else {
            // 检测相机权限
            checkCamera();
        }
    }

    private void checkCamera(){
        int permissionCamera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCamera != PermissionChecker.PERMISSION_GRANTED) {
            //2.没有权限，弹出对话框申请
            ActivityCompat.requestPermissions(this, permissions_camera, REQUEST_PERMISSION_CODE_CAMERA);
        }else{
            super.openCamera();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            return;
        }
        if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            //权限申请成功
            //  Toast.makeText(this, "授权SD卡权限成功", Toast.LENGTH_SHORT).show();
            if (isFromDialog && requestCode == REQUEST_PERMISSION_CODE) {
                checkCamera();
            }else if(isFromDialog && requestCode == REQUEST_PERMISSION_CODE_CAMERA){
                super.openCamera();
            }
        } else {
            //权限申请失败
            ToastUtil.showText(this,R.string.sd_failure);
        }
        isFromDialog = false;
    }


    @Override
    public void openCamera() {
        verifyStoragePermission(this);
    }

    @Override
    public void onTakePhotoResult(Uri uri, @NonNull File tempFile) {
        ImageSetBean imageSetBean = new ImageSetBean();
        imageSetBean.uuid = BowlingUtils.sCurrentUUID;
        imageSetBean.uri = uri;
        BowlingManager.getInstance().imageListener.executeListeners(imageSetBean);
    }


    @Override
    public void onCropPhotoResult(@Nullable Uri uri, @Nullable File tempFile) {

    }

    public void showImage(ImageView ivPhoto, String imagePath) {
        Picasso.with(MainActivity.this).load(new File(imagePath)).resize(200, 200).centerCrop().into(ivPhoto);
        Log.i(getClass().getSimpleName(), "showImage: " + imagePath);
    }


    // 显示底部功能
    private void showBottonFunction() {
        mHelper.hide();
        mLlBottonFunction.setVisibility(View.VISIBLE);
        mRlShowBottonFunction.setVisibility(View.GONE);
    }

    // 隐藏底部功能
    private void hideBottonFunction() {
        mLlBottonFunction.setVisibility(View.GONE);
        mRlShowBottonFunction.setVisibility(View.VISIBLE);
    }

    private int COUNTS = 5;// 点击次数
    private long[] mHits = new long[COUNTS];//记录点击次数
    private long DURATION = 2000;//有效时间

    //点击9次
    private final int CLICK_NUM = 7;
    //点击时间间隔5秒
    private final int CLICK_INTERVER_TIME = 500;
    //上一次的点击时间
    private long lastClickTime = 0;
    //记录点击次数
    private int clickNum = 0;

    /**
     * 点击9次
     */
    public void nineClick() {
        //点击的间隔时间不能超过5秒
        long currentClickTime = SystemClock.uptimeMillis();
        if (currentClickTime - lastClickTime <= CLICK_INTERVER_TIME || lastClickTime == 0) {
            lastClickTime = currentClickTime;
            clickNum = clickNum + 1;
        } else {
            //超过5秒的间隔
            //重新计数 从1开始
            clickNum = 1;
            lastClickTime = 0;
            return;
        }
        if (clickNum == CLICK_NUM) {
            //重新计数
            clickNum = 0;
            lastClickTime = 0;
            /*实现点击多次后的事件*/
            BowlingPasswordDialog dialog = new BowlingPasswordDialog(this, BowlingPasswordDialog.SOURCE_MODIFY_SETTING);
            dialog.show();
        }
    }

    public void showVideoDialog(String urlLeft,String urlRight){
        BowlingVideoPlayDialog mVideoPlayDialog = new BowlingVideoPlayDialog(this,urlLeft,urlRight);
        mVideoPlayDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setGameInfo(LocalGameInfo localGameInfo, boolean exchangeMode){
        mHelper.setGameInfo(localGameInfo,exchangeMode);
    }

    private static final int TIME_EXIT = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_EXIT > System.currentTimeMillis()) {
            BowlingApplication.getContext().onTerminate();
            return;
        } else {
            ToastUtil.showText(this,R.string.click_again_finish);
            mBackPressed = System.currentTimeMillis();

        }
    }


    public void showAdDialog(){
        if(BowlingApplication.sIsTvMode){
            if(mAdDialog == null){
                mAdDialog = new BowlingAdDialog(this);
            }
            mAdDialog.show();
        }
    }

    public void dismissDialog(){
        if(BowlingApplication.sIsTvMode){
            if(mAdDialog != null){
                mAdDialog.dismiss();
            }
        }
    }

    @Override
    public void getBasicGameInfo(PlayingGame gameBasicInfo) {
        BowlingManager.getInstance().gameForTurn.executeListeners(gameBasicInfo);
    }

    @Override
    public void getBowlerGameForTurn(BowlerGameForTurn bowlerGameForTurn) {

    }

    @Override
    public void getBowlerGame(BowlerGameSingLine singLine) {

    }

    @Override
    public void getBowlerBasicInfo(GameBasicInfo gameBasicInfo, String onLineId) {

    }
}
