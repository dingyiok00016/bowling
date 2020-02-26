package com.cloudysea.ui;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.fragment.LocalScoreFragment;
import com.cloudysea.net.WebSocketClientService;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;
import com.cloudysea.views.BowlingRemoteScoreDiloag;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author roof 2019/10/27.
 * @email lyj@yhcs.com
 * @detail
 */
public class RemoteMatchFragment extends Fragment implements NetView {
    private static final String TAG = "RemoteMatchFragment";
    private BowlingRemoteScoreDiloag mScoreDialog;
    private ImageView mIvArrowLeft;
    private ImageView mIvArrowRight;
    private ViewPager mVpContainer;
    private TabPageIndicator mIndicator;
    public static final String EXTRA_LANE = "extra_lane";
    public static final String EXTRA_OBJECT = "extra_object";
    private ImageView mIvSwitch;
    private NetPresenter mPresenter;
    public static GameBasicInfo.DataBean mDatabean;
    private List<RemoteScoreFragment> fragments = new ArrayList<>();
    private static final int REQUEST_PERMISSION_CODE = 0x999;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static int mMaxCount;
    private BaseListener baseListener;


    private void getBaseListener() {
        if (baseListener == null) {
            baseListener = new BaseListener<BowlerGameSingLine>() {
                @Override
                public void execute(BowlerGameSingLine o) {
                    if (mVpContainer == null) {
                        return;
                    }
                    final RemoteScoreFragment scoreFragment = fragments.get(o.getData().getNumberInTurn());
                    BowlerGameForTurn mTurn = scoreFragment.getRemoteInfo();
                    if (mTurn != null && mTurn.getData() != null && mTurn.getData().getBowlerGames() != null
                            && mTurn.getData().getBowlerGames().size() > 0) {
                        // 遍历寻找需要修改的id
                        for (int i = 0; i < mTurn.getData().getBowlerGames().size(); i++) {
                            if (mTurn.getData().getBowlerGames().get(i).getId().equals(o.getData().getId())) {
                                final int n = i;
                                // 获取到当前i
                                if (mTurn.getData().getBowlerGames().get(i).getScore() == null) {
                                    mTurn.getData().getBowlerGames().get(i).setScore(new BowlerGameForTurn.DataBean.BowlerGamesBean.ScoreBean());
                                }
                                mTurn.getData().getBowlerGames().get(i).getScore().setScores(o.getData().getScore().getScores());
                                mTurn.getData().getBowlerGames().get(i).getScore().setRoundTotalScores(o.getData().getScore().getRoundTotalScores());
                                mTurn.getData().getBowlerGames().get(i).getScore().setTotalScore(o.getData().getScore().getTotalScore());
                                getView().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scoreFragment.setSingleInfo(n);
                                    }
                                });
                                break;
                            }

                        }
                    }
                }
            };
        }
    }

    private int mScoreListBean;
    private BaseListener<Integer> remoteNumbers = new BaseListener<Integer>() {
        @Override
        public void execute(final Integer scoreListBean) {
            if (!isDetached()) {
                // 刷新球局信息
                getView().post(new Runnable() {
                    @Override
                    public void run() {
                        initTab(scoreListBean);
                    }
                });
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_remote_match, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mPresenter = new NetPresenter(this);
        MainActivity activity = (MainActivity) getActivity();
        mPresenter.getBowlerBasicInfo(activity.getRemoteOnlineId());
        BowlingManager.getInstance().remoteGameNumbers.addListener(remoteNumbers);
        getBaseListener();
        BowlingManager.getInstance().singleGame.addListener(baseListener);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BowlingManager.getInstance().remoteGameNumbers.removeListener(remoteNumbers);
        BowlingManager.getInstance().singleGame.removeListener(baseListener);
        WebSocketClientService.getInstance().onStop();
    }


    private boolean isFromDialog = false;

    private void setArrowShowState(int position, int count) {
        if (position == 0) {
            mIvArrowLeft.getBackground().setAlpha(127);
            if (count > 1) {
                mIvArrowRight.getBackground().setAlpha(255);
            }
        } else if (position == count - 1) {
            mIvArrowRight.getBackground().setAlpha(127);
            mIvArrowLeft.getBackground().setAlpha(255);
        } else {
            mIvArrowLeft.getBackground().setAlpha(255);
            mIvArrowRight.getBackground().setAlpha(255);
        }
    }


    private void initView() {
        mIvArrowLeft = (ImageView) getView().findViewById(R.id.iv_channel_left);
        mIvArrowLeft.getBackground().setAlpha(127);
        mIvArrowRight = (ImageView) getView().findViewById(R.id.iv_channel_right);
        mIvArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVpContainer.getCurrentItem() > 0) {
                    mVpContainer.setCurrentItem(mVpContainer.getCurrentItem() - 1);
                }
            }
        });
        mIvArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVpContainer.getCurrentItem() < fragments.size() - 1) {
                    mVpContainer.setCurrentItem(mVpContainer.getCurrentItem() + 1);
                }
            }
        });
        mVpContainer = (ViewPager) getView().findViewById(R.id.vp_score_list_container);
        // 手机适配
        if(!BowlingUtils.isPad()){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVpContainer.getLayoutParams();
            params.topMargin = (int) (40 * DeviceUtils.getDestiny());
        }
        mIndicator = (TabPageIndicator) getView().findViewById(R.id.tabIndicator);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null && !mainActivity.isFinishing()) {
                mPresenter.getBowlerBasicInfo(mainActivity.getRemoteOnlineId());
            }
        } else {
            if (mVpContainer != null) {
                setHiddenStatus(true);
            }
        }
        super.onHiddenChanged(hidden);
    }

    private ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d(TAG, "viewpager滑动..." + positionOffset);
            setRemoteoffset(positionOffset);
            if (positionOffset == 0F || positionOffset == 1F) {
                if (mVpContainer.getCurrentItem() != mLastItem) {
                    mIndicator.setSelectedTabIndex(mVpContainer.getCurrentItem());
                    mLastItem = mVpContainer.getCurrentItem();
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mPresenter.getBowlerGameForTurn(mainActivity.getRemoteOnlineId(), mVpContainer == null ? 0 : mVpContainer.getCurrentItem());
                }

            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mTvCurrentView != null) {
                mTvCurrentView.setTextColor(Color.GRAY);
            }
            TextView view = (TextView) mIndicator.getChildView(position);
            if (view != null) {
                view.setTextColor(Color.WHITE);
            }
            mTvCurrentView = view;
            setArrowShowState(position, fragments.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    // 显示隐藏状态
    private void setHiddenStatus(boolean hidden){
        if(hidden){
            mVpContainer.setVisibility(View.GONE);
            mIndicator.setVisibility(View.GONE);
            mIvArrowLeft.setVisibility(View.GONE);
            mIvArrowRight.setVisibility(View.GONE);
        }else{
            mVpContainer.setVisibility(View.VISIBLE);
            mIndicator.setVisibility(View.VISIBLE);
        }
    }

    // 初始化当前列表
    private void reInitTab(int tabCount, String[] arrarys) {
        mTabs.clear();
        if (tabCount >= 2) {
            mIvArrowLeft.setVisibility(View.VISIBLE);
            mIvArrowRight.setVisibility(View.VISIBLE);
        }
        // sort
        final int[] ints = new int[tabCount];
        try {
            if (tabCount > 0) {
                for (int i = 0; i < ints.length; i++) {
                    ints[i] = Integer.valueOf(arrarys[i]) + 1;
                }
                Arrays.sort(ints);
            }
        } catch (Exception e) {
            tabCount = 0;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        fragments.clear();
        for (int i = 0; i < tabCount; i++) {
            try {
                RemoteScoreFragment listFragment = new RemoteScoreFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_OBJECT,mDatabean);
                bundle.putInt(EXTRA_LANE,i);
                listFragment.setArguments(bundle);
                fragments.add(listFragment);
            } catch (Exception e) {

            }
        }
        mAdapte = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }


            @Override
            public CharSequence getPageTitle(int position) {
                if(BowlingUtils.currentLanguageIsSimpleChinese(BowlingApplication.getContext())){
                    return (ints[position] + " " + getResources().getString(R.string.game));
                }else{
                    return (getResources().getString(R.string.game) + " "  + ints[position]);
                }
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mVpContainer.setOnPageChangeListener(mChangeListener);
        mVpContainer.setAdapter(mAdapte);
        if (arrarys == null) {
            return;
        }
        mIndicator.setOnPageChangeListener(mChangeListener);
        /*mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        mIndicator.setViewPager(mVpContainer);
        if (mIndicator.getViewPager() != mVpContainer) {
            mIndicator.setViewPager(mVpContainer);
        } else {
            mIndicator.notifyDataSetChanged();
        }
        for (int m = 0; m < mIndicator.getTabChildCount(); m++) {
            final int n = m;
            TextView currentView = (TextView) mIndicator.getChildView(m);
            currentView.setTypeface(TypefaceUtil.getStyleOneInstance());
            if(BowlingUtils.isPad()){
                currentView.setTextSize(25);
            }else{
                currentView.setTextSize(20);
            }
            currentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIndicator.setCurrentItem(n);
                }
            });
            if (m == 0 && mVpContainer.getCurrentItem() == 0) {
                currentView.setTextColor(Color.WHITE);
                mTvCurrentView = currentView;
            }
        }
        setHiddenStatus(false);
    }


    public void resetCurrentMatch(){
        if(mIndicator != null){
            mIndicator.setSelectedTabIndex(0);
        }
    }

    private List<TextView> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapte;

    private void initTab(int count) {
        // 默认1道
        String[] strings = new String[count];
        for (int i = 0; i < count; i++) {
            strings[i] = i + "";
        }
        int newCount = strings.length;
        reInitTab(newCount, strings);

    }

    private int mLastItem = 0;

    private static float mOfffset;
    private static float mOffRemoteSet;

    public static boolean vpIsAllScreen() {
        return mOfffset == 0F || mOfffset == 1F;
    }

    public static void setVpOffset(float offset) {
        mOfffset = offset;
    }

    public static void setRemoteoffset(float offset){
        mOffRemoteSet = mOffRemoteSet;
    }

    private TextView mTvCurrentView;

    @Override
    public void getBasicGameInfo(PlayingGame gameBasicInfo) {

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
        mDatabean = gameBasicInfo.getData();
        if (mDatabean != null) {
            BowlingManager.getInstance().remoteGameNumbers.executeListeners(gameBasicInfo.getData().getTurnCount());
            mPresenter.getBowlerGameForTurn(onLineId, mVpContainer == null ? 0 : mVpContainer.getCurrentItem());
        }
    }
}
