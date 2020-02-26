package com.cloudysea.utils;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.CurrentBowlerInfo;
import com.cloudysea.bean.CurrentSpeed;
import com.cloudysea.bean.LocalGameInfo;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.ui.MainActivity;

import java.nio.channels.FileLock;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author roof 2019/11/2.
 * @email lyj@yhcs.com
 * @detail
 */
public class BottomPersonInfoHelper {
    private static final String TAG = "BottomPersonInfoHelper";
    private static BottomPersonInfoHelper mHelper;
    private ViewGroup mRootView;
    private TextView mTvInfoTime;
    private TextView mTvInfoTurn;
    private TextView mTvSpeed;
    private TextView mTvTeamScore;
    private TextView mTvName;
    private TextView mTvHdp;
    private ImageView mIvAvatar;
    private TextView mTvLaneInfo;
    private TextView mTvGameInfo;
    private TextView mTvSelfSocre;
    private ImageView mIvVip;
    private CurrentBowlerInfo mCurrentBowlerInfo;
    private List<PlayerBean> mPlayerBean;
    private boolean mZeroFirst = true;
    private boolean mSecondFist = true;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat systemSdf = new SimpleDateFormat("yyyy-mm-ss HH:mm:ss", Locale.getDefault());
    private int mCurrentTime;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Date sourceAdd = (Date) msg.obj;
                    Message messageAdd = Message.obtain();
                    Calendar calendarAdd = Calendar.getInstance();
                    calendarAdd.setTime(sourceAdd);
                    calendarAdd.add(Calendar.HOUR, -8);
                    mTvInfoTime.setText(sdf.format(calendarAdd.getTime()));
                    calendarAdd.add(Calendar.SECOND, 1);
                    calendarAdd.add(Calendar.HOUR, 8);
                    Date resultAdd = calendarAdd.getTime();
                    messageAdd.what = 0;
                    messageAdd.obj = resultAdd;
                    sendMessageDelayed(messageAdd, 1000);
                    break;
                case 1:
                    Date source = (Date) msg.obj;
                    Message message = Message.obtain();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(source);
                    calendar.add(Calendar.HOUR, -8);
                    mTvInfoTime.setText(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.SECOND, -1);
                    calendar.add(Calendar.HOUR, 8);
                    Date result = calendar.getTime();
                    message.what = 1;
                    message.obj = result;
                    sendMessageDelayed(message, 1000);
                    break;
            }
        }
    };

    public BottomPersonInfoHelper(View rootView) {
        mRootView = (ViewGroup) rootView;
        mTvInfoTime = (TextView) mRootView.findViewById(R.id.tv_person_info_time);
        mTvInfoTurn = (TextView) mRootView.findViewById(R.id.tv_person_info_turn);
        mTvSpeed = (TextView) mRootView.findViewById(R.id.tv_person_info_speed);
        mTvName = (TextView) mRootView.findViewById(R.id.tv_person_info_name);
        mTvHdp = (TextView) mRootView.findViewById(R.id.tv_person_info_hdp);
        mIvAvatar = (ImageView) mRootView.findViewById(R.id.iv_person_avatar);
        mTvTeamScore = (TextView) mRootView.findViewById(R.id.tv_person_info_timescore);
        mTvLaneInfo = (TextView) mRootView.findViewById(R.id.tv_person_info_lane_info);
        mTvGameInfo = (TextView) mRootView.findViewById(R.id.tv_person_info_game_name);
        mTvSelfSocre = (TextView) mRootView.findViewById(R.id.tv_person_info_self_score);
        mIvVip = (ImageView) mRootView.findViewById(R.id.iv_vip_icon);
        DisplayMetrics displayMetrics = BowlingApplication.getContext().getResources().getDisplayMetrics();
        int dy = (int) ((int) ((int) (90 * (displayMetrics.widthPixels / 960F)) - 90 * displayMetrics.density) * 0.5);
        mIvAvatar.getLayoutParams().height += dy;
        mIvAvatar.getLayoutParams().width += dy;

        // 手机适配
        if (!BowlingUtils.isPad()) {
            mTvInfoTime.setTextSize(16);
            mTvInfoTurn.setTextSize(16);
            mTvSpeed.setTextSize(16);
            mTvName.setTextSize(16);
            mTvHdp.setTextSize(16);
            mTvTeamScore.setTextSize(16);
            mTvLaneInfo.setTextSize(16);
            mTvGameInfo.setTextSize(16);
            mTvSelfSocre.setTextSize(16);
            LinearLayout linearLayoutCenter = (LinearLayout) mRootView.findViewById(R.id.tl_person_info_center);
            LinearLayout linearLayoutBottom = (LinearLayout) mRootView.findViewById(R.id.tl_person_info_bottom);
            linearLayoutBottom.getLayoutParams().height *= BowlingUtils.Global_SIZE_BOTTOM;
            linearLayoutCenter.getLayoutParams().height *= BowlingUtils.Global_SIZE_BOTTOM;
            View functionImage = mRootView.findViewById(R.id.view_function_img);
            functionImage.getLayoutParams().height *= BowlingUtils.Global_SIZE_BOTTOM;
            ImageView imageView = (ImageView) mRootView.findViewById(R.id.iv_person_avatar);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.topMargin = 0;
            params.leftMargin = (int) (50 * DeviceUtils.getDestiny());

        }

    }

    public static BottomPersonInfoHelper getInstance(View rootView) {
        if (mHelper == null) {
            mHelper = new BottomPersonInfoHelper(rootView);
        }
        return mHelper;
    }

    public static BottomPersonInfoHelper getInstance() {
        return mHelper;
    }

    public static void destory() {
        mHelper = null;
    }

    public static CurrentBowlerInfo.DataBean getCurrentDataBean(CurrentBowlerInfo currentBowlerInfo) {
        if (currentBowlerInfo == null || currentBowlerInfo.getData() == null || currentBowlerInfo.getData().size() == 0) {
            return null;
        }
        CurrentBowlerInfo.DataBean dataBean = null;
        // 判断当前球员数目
        if (currentBowlerInfo.getData().size() == 1) {
            dataBean = currentBowlerInfo.getData().get(0);
        } else {
            for (int i = 0; i < currentBowlerInfo.getData().size(); i++) {
                if (currentBowlerInfo.getData().get(i).IsLocalLane) {
                    dataBean = currentBowlerInfo.getData().get(i);
                }
            }
        }
        return dataBean;
    }

    public void setPlayBean(List<PlayerBean> playBean) {
        if (mHelper == null) {
            return;
        }
        if (playBean != null) {
            mPlayerBean = playBean;
        }
    }

    public void setSpeed(CurrentSpeed currentSpeed) {
        if (currentSpeed.getData() == null) {
            return;
        }
        // 速度信息
        if (currentSpeed.getData().getSpeed() != 0) {
            mTvSpeed.setVisibility(View.VISIBLE);
            mTvSpeed.setText(currentSpeed.getData().getSpeed() + "KM/H");
        } else {
            mTvSpeed.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 该方面进行3个操作
     * 1.设置交换道信息
     * 2.设置当前球员信息，分数列表中突出
     * 3.设置当前球员信息，在底部显示
     */
    public void setCurrentBowlerInfo() {
        if (mHelper == null) {
            return;
        }
        CurrentBowlerInfo currentBowlerInfo = mCurrentBowlerInfo;
        if (currentBowlerInfo == null || currentBowlerInfo.getData() == null || currentBowlerInfo.getData().size() == 0) {
            return;
        }
        MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
        CurrentBowlerInfo.DataBean dataBean = null;
        // 判断当前球员数目
        if (currentBowlerInfo.getData().size() == 1) {
            dataBean = currentBowlerInfo.getData().get(0);
        } else {
            for (int i = 0; i < currentBowlerInfo.getData().size(); i++) {
                if (currentBowlerInfo.getData().get(i).IsLocalLane) {
                    dataBean = currentBowlerInfo.getData().get(i);
                }
            }
        }
        // 设置交换道信息
        if (mainActivity != null) {
            mainActivity.setExchangeViewForScoreAdapter();
        }
        if (dataBean == null) {
            return;
        }

        BowlingUtils.CURRENT_BOWLER_ID = dataBean.getBowlerId();
        if (mainActivity != null) {
            mainActivity.setCurrentViewForScoreAdapter();
        }
        // 非速度信息
        if (!TextUtils.isEmpty(BowlingUtils.CURRENT_BOWLER_ID)) {
            // 遍历获取名字
            for (PlayerBean playerBean : mPlayerBean) {
                Log.d(TAG, "playerBean-id：" + playerBean.Id);
                Log.d(TAG, "currentBowler-id：" + dataBean.getBowlerId());
                if (dataBean.getBowlerId().equals(playerBean.Id)) {
                    mTvName.setVisibility(View.VISIBLE);
                    mTvName.setText(playerBean.BowlerName);
                    BowlingUtils.setImageBitmap(playerBean, mIvAvatar);
                    mTvSelfSocre.setVisibility(View.VISIBLE);
                    if (playerBean.TotalScore != null) {
                        mTvSelfSocre.setText(String.format(Locale.getDefault(), "%d", playerBean.TotalScore));
                    } else {
                        mTvSelfSocre.setText(String.format(Locale.getDefault(), "%d", 0));
                    }
                    if (playerBean.IsMembership) {
                        mIvVip.setVisibility(View.VISIBLE);
                    } else {
                        mIvVip.setVisibility(View.GONE);
                    }
                    break;
                }
            }
            mTvTeamScore.setVisibility(View.VISIBLE);
            mTvTeamScore.setText(String.format(Locale.getDefault(), "T:%d", dataBean.getTeamScore()));
            mTvHdp.setVisibility(View.VISIBLE);
            mTvHdp.setText(String.format(Locale.getDefault(), "HDP:%d", dataBean.getHDP()));
        } else {
            // 全部隐藏
            setDefaultConfigForBolwerInfo();
        }
        mTvSpeed.setText("0");
    }


    /**
     * 该方面进行3个操作
     * 1.设置交换道信息
     * 2.设置当前球员信息，分数列表中突出
     * 3.设置当前球员信息，在底部显示
     *
     * @param currentBowlerInfo
     */
    public void setCurrentBowlerInfo(CurrentBowlerInfo currentBowlerInfo) {
        if (mHelper == null) {
            return;
        }
        BowlingUtils.CURRENT_EXCHANGE = null;
        mCurrentBowlerInfo = currentBowlerInfo;
        if (currentBowlerInfo == null || currentBowlerInfo.getData() == null || currentBowlerInfo.getData().size() == 0) {
            return;
        }
        CurrentBowlerInfo.DataBean dataBean = null;
        // 判断当前球员数目
        if (currentBowlerInfo.getData().size() == 1) {
            dataBean = currentBowlerInfo.getData().get(0);
            BowlingUtils.CURRENT_EXCHANGE = new String[1];
            BowlingUtils.CURRENT_EXCHANGE[0] = dataBean.getBowlerId();
        } else {
            BowlingUtils.CURRENT_EXCHANGE = new String[2];
            for (int i = 0; i < currentBowlerInfo.getData().size(); i++) {
                if (i <= 1) {
                    BowlingUtils.CURRENT_EXCHANGE[i] = currentBowlerInfo.getData().get(i).getBowlerId();
                }
                if (currentBowlerInfo.getData().get(i).IsLocalLane) {
                    dataBean = currentBowlerInfo.getData().get(i);
                }
            }
        }
        // 设置交换道信息
        MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
        if (mainActivity != null) {
            mainActivity.setExchangeViewForScoreAdapter();
        }
        if (dataBean == null) {
            return;
        }
        BowlingUtils.CURRENT_BOWLER_ID = dataBean.getBowlerId();
        // 显示当前球员信息
        if (mainActivity != null) {
            mainActivity.setCurrentViewForScoreAdapter();
        }
        // 非速度信息
        if (!TextUtils.isEmpty(BowlingUtils.CURRENT_BOWLER_ID)) {
            // 遍历获取名字
            for (PlayerBean playerBean : mPlayerBean) {
                Log.d(TAG, "playerBean-id：" + playerBean.Id);
                Log.d(TAG, "currentBowler-id：" + dataBean.getBowlerId());
                if (dataBean.getBowlerId().equals(playerBean.Id)) {
                    mTvName.setVisibility(View.VISIBLE);
                    mTvName.setText(playerBean.BowlerName);
                    BowlingUtils.setImageBitmap(playerBean, mIvAvatar);
                    mTvSelfSocre.setVisibility(View.VISIBLE);
                    if (playerBean.TotalScore != null) {
                        mTvSelfSocre.setText(String.format(Locale.getDefault(), "%d", playerBean.TotalScore));
                    } else {
                        mTvSelfSocre.setText(String.format(Locale.getDefault(), "%d", 0));
                    }
                    if (playerBean.IsMembership) {
                        mIvVip.setVisibility(View.VISIBLE);
                    } else {
                        mIvVip.setVisibility(View.GONE);
                    }
                    break;
                }
            }
            mTvTeamScore.setVisibility(View.VISIBLE);
            mTvTeamScore.setText(String.format(Locale.getDefault(), "T:%d", dataBean.getTeamScore()));
            mTvHdp.setVisibility(View.VISIBLE);
            mTvHdp.setText(String.format(Locale.getDefault(), "HDP:%d", dataBean.getHDP()));
        } else {
            // 全部隐藏
            setDefaultConfigForBolwerInfo();
        }


    }

    private void setDefaultConfigForBolwerInfo() {
        mTvHdp.setVisibility(View.INVISIBLE);
        mTvTeamScore.setVisibility(View.INVISIBLE);
        mTvName.setVisibility(View.INVISIBLE);
        mTvSelfSocre.setVisibility(View.INVISIBLE);
        mIvAvatar.setImageResource(R.drawable.head1);
        mIvVip.setVisibility(View.GONE);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setGameInfo(LocalGameInfo localGameInfo, boolean isExchangeMode) {
        if (localGameInfo == null) {
            return;
        }

        // 对局信息，优先处理交换道信息
        if(isExchangeMode) {
            Drawable left = BowlingApplication.getContext().getDrawable(R.drawable.icon_left);
            left.setBounds(0,0,(int)(23 * DeviceUtils.getDestiny()),(int)(15 * DeviceUtils.getDestiny()));
            mTvGameInfo.setText(null);
            mTvGameInfo.setCompoundDrawables(left,null,null,null);
            mTvGameInfo.setVisibility(View.VISIBLE);
        }
        else if (!TextUtils.isEmpty(localGameInfo.getGameName())) {
            mTvGameInfo.setCompoundDrawables(null,null,null,null);
            mTvGameInfo.setText(localGameInfo.getGameName());
            mTvGameInfo.setVisibility(View.VISIBLE);
        } else {
            mTvGameInfo.setVisibility(View.INVISIBLE);
        }

        // 赛道信息
        if (!TextUtils.isEmpty(localGameInfo.getLaneInfo())) {
            mTvLaneInfo.setText(localGameInfo.getLaneInfo());
            mTvLaneInfo.setVisibility(View.VISIBLE);
        } else {
            mTvLaneInfo.setVisibility(View.INVISIBLE);
        }
        mHandler.removeCallbacksAndMessages(null);
        // 时间和轮次的处理逻辑，这里需要计算
        float endValueFloat = Float.valueOf(localGameInfo.getEndValue());
        double startValueFloat = localGameInfo.getEllipsedTurn();
        float value = (float) (endValueFloat - startValueFloat);
        if (localGameInfo.isIsPrePay()) { // 预付费
            if (localGameInfo.isIsByTime()) { // 按时间计费
                mTvInfoTurn.setText((startValueFloat >= 0 ? startValueFloat : 0) + "");
                Log.d("BottomPersonInfoHelper", "currentTime=" + sdf.format(new Date()));
                sendTimeSubStyle(localGameInfo);
            } else { // 按局计费
                mTvInfoTurn.setText(value + "");
                sendTimeAddStyle(localGameInfo);
            }

        } else { // 后付费
            if (localGameInfo.isIsByTime()) { // 按时间计费
                mTvInfoTurn.setText(startValueFloat + "");
            } else { // 按局计费
                mTvInfoTurn.setText(startValueFloat + "");
            }
            sendTimeAddStyle(localGameInfo);
        }
    }

    private void sendTimeAddStyle(LocalGameInfo localGameInfo) {
        try {
            Date startDate = new Date(System.currentTimeMillis() - systemSdf.parse(localGameInfo.getStartDateTime()).getTime());
            Message message = Message.obtain();
            message.obj = startDate;
            message.what = 0;
            mHandler.sendMessageDelayed(message, 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void sendTimeSubStyle(LocalGameInfo localGameInfo) {
        try {
            Date systemDate = systemSdf.parse(localGameInfo.getEndDateTime());
            long remindTime = systemDate.getTime() - System.currentTimeMillis();
            Date remindDtae = new Date(remindTime);
            Message message = Message.obtain();
            message.what = 1;
            message.obj = remindDtae;
            mHandler.sendMessageDelayed(message, 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void setExchangeMode(boolean isExchangeMode) {
        if (isExchangeMode) {

        } else {

        }
    }


    public void hide() {
        if (mHelper == null) {
            return;
        }
        mRootView.setVisibility(View.GONE);
    }

    public void show() {
        if (mHelper == null) {
            return;
        }
        MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
        if (mainActivity != null) {
            if (mainActivity.isRemote()) {
                mRootView.setVisibility(View.GONE);
                return;
            }
        }
        mRootView.setVisibility(View.VISIBLE);
    }
}
