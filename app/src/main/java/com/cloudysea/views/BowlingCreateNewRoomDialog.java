package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.utils.BowlingChoiceViewManager;
import com.cloudysea.utils.BowlingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author roof 2019/10/19.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingCreateNewRoomDialog extends BowlingCommonDialog {
    // 球道标准: 0- NotStandard, 1-Standard
    // 设备类型： 0-Brunswick, 1-Amf, 2-Xima, 3-Zhonglu, 4-Other
    // 积分类型: NEW,OLD
    private int mCurrentRound = 2; // 当前局数，默认为2
    private boolean mCanWatch; // 允许旁观
    private boolean mOverThenNext; // 结束再开一局
    private String mPwd;
    private EditText mEditText;
    private List<Integer> mDeviceCategory;
    // 设备
    private List<Integer> mLaneCategory; // 赛道
    private String mScoreCategory = "New"; // 积分

    private Comparator<Integer> mComparators = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 > o2) {
                return 1;
            } else if (o1 < o2) {
                return -1;
            } else {
                return 0;
            }
        }
    };


    public BowlingCreateNewRoomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyle() {
        return REMOTE_CEATE_STYLE;
    }

    @Override
    protected void initView(View view) {
        // 初始化选择信息
        mDeviceCategory = new ArrayList<>();
        mLaneCategory = new ArrayList<>();
        // 设备
        mDeviceCategory.add(0);
        mDeviceCategory.add(1);
        mDeviceCategory.add(2);
        mDeviceCategory.add(3);
        mDeviceCategory.add(4);
        // 赛道
        mLaneCategory.add(1);

        LinearLayout linearLayoutStatus = (LinearLayout) findViewById(R.id.ll_status_set);
        LinearLayout linearLayoutWatch = (LinearLayout) findViewById(R.id.ll_watch_set);
        BowlingChoiceViewManager.init(linearLayoutWatch, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    mCanWatch = false;
                    v.setTag(Boolean.FALSE);
                } else {
                    mCanWatch = true;
                    v.setTag(null);
                }
            }
        }, BowlingApplication.getContext().getResources().getString(R.string.can_watch));
        BowlingChoiceViewManager.init(linearLayoutStatus, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    mOverThenNext = false;
                    v.setTag(Boolean.FALSE);
                } else {
                    mOverThenNext = true;
                    v.setTag(null);
                }
            }
        }, BowlingApplication.getContext().getResources().getString(R.string.another_game));

        findViewById(R.id.tv_create_a_new_room_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    createGame();
                    dismiss();
            }
        });
        final TextView mTvRound = (TextView) findViewById(R.id.tv_round_text);
        findViewById(R.id.rl_add_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int round = Integer.valueOf(mTvRound.getText().toString());
                if (round == 6) {
                    return;
                }
                round++;
                mCurrentRound = round;
                mTvRound.setText(String.format(Locale.getDefault(), "%d", round));
            }
        });
        findViewById(R.id.rl_delete_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int round = Integer.valueOf(mTvRound.getText().toString());
                if (round == 1) {
                    return;
                }
                round--;
                mCurrentRound = round > 0 ? round : round;
                mTvRound.setText(String.format(Locale.getDefault(), "%d", round > 0 ? round : 0));
            }
        });
        mEditText = (EditText) findViewById(R.id.et_create_room_pwd);
        final RemoteBattlerTopView remoteBattlerTopView = (RemoteBattlerTopView) findViewById(R.id.rt_create_new_room);
        remoteBattlerTopView.setBattlerButtonVisible(View.GONE);
        remoteBattlerTopView.setScoreCategoryVisible(View.GONE);
        remoteBattlerTopView.setTopViewListener(new RemoteBattlerTopView.TopViewSelectListener() {
            @Override
            public void selectText(int style, String str, int position, boolean select, boolean all) {
                if (style == RemoteBattlerTopView.STYLE_DEVICE) {
                    if (select) {
                        if (!mDeviceCategory.contains(position)) {
                            mDeviceCategory.add(position);
                        }
                    } else {
                        mDeviceCategory.remove(Integer.valueOf(position));
                    }
                    StringBuilder sb = new StringBuilder();
                    if (mDeviceCategory.contains(0)) {
                        sb.append(BowlingApplication.getContext().getResources().getString(R.string.bin_area) + "，");
                    }
                    if (mDeviceCategory.contains(1)) {
                        sb.append("AMF,");
                    }
                    if (mDeviceCategory.contains(2)) {
                        sb.append( BowlingApplication.getContext().getResources().getString(R.string.xima_area)+ ",");
                    }
                    if (mDeviceCategory.contains(3)) {
                        sb.append(BowlingApplication.getContext().getResources().getString(R.string.mid_area) + ",");
                    }
                    if(mDeviceCategory.contains(4)){
                        sb.append(BowlingApplication.getContext().getResources().getString(R.string.other_area) + ",");
                    }
                    if(sb.length() > 0){
                        sb.delete(sb.length() - 1,sb.length());
                    }
                    remoteBattlerTopView.setDeviceContent(sb.toString());

                } else if (style == RemoteBattlerTopView.STYLE_CHANNEL) {
                    if (select) {
                        mLaneCategory.clear();
                        if (position <= 1) {
                            mLaneCategory.add(position);
                        } else {
                            mLaneCategory.add(0);
                            mLaneCategory.add(1);
                        }
                    }
                } else if (style == RemoteBattlerTopView.STYLE_SCORE) {
                    if (select) {
                        if (position == 0) {
                            mScoreCategory = "New";
                        } else {
                            mScoreCategory = "Old";
                        }
                    }
                }
            }
        });
    }


    // 获取参数
    private String getConfig(List list) {
        Collections.sort(list, mComparators);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(",");
            }

        }
        return sb.toString();
    }

    private String getPwd() {
        if (!TextUtils.isEmpty(mEditText.getText())) {
            mPwd = mEditText.getText().toString();
        } else {
            mPwd = null;
        }
        return mPwd;
    }


    public void createGame() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("AuthCode", "120");
            jsonObject.put("Id", UUID.randomUUID().toString());
            jsonObject.put("LaneNumber", BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("Name", "CreateAndJoinOnlineGame");
            jsonObject.put("Type", "0");
            JSONObject child = new JSONObject();
            jsonObject.put("Data", child);
            child.put("AllowWatch", mCanWatch);
            child.put("AllowedDeviceCategory", getConfig(mDeviceCategory)); // 设备
            child.put("AllowedLaneCategory", getConfig(mLaneCategory)); // 赛道
            child.put("EntryPassword", getPwd()); // 密码
            child.put("NeedAllBowlerDoneToNextTurn", mOverThenNext); // 是否自动结束
            child.put("TurnCount", mCurrentRound); // 球局数目
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_create_new_room;
    }
}
