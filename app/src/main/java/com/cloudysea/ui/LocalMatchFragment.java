package com.cloudysea.ui;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.BallScoreAdapter;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.bean.ScoreListBean;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.controller.AddOrDeleteListener;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.controller.GetScoreListListener;
import com.cloudysea.fragment.LocalScoreFragment;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.net.WebSocketClientService;
import com.cloudysea.utils.BottomPersonInfoHelper;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.LogcatFileManager;
import com.cloudysea.utils.PermissionUtils;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.utils.TypefaceUtil;
import com.cloudysea.views.BowlingAddorDeletePlayerDialog;
import com.cloudysea.views.BowlingRemoteScoreDiloag;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author roof 2019/10/27.
 * @email lyj@yhcs.com
 * @detail
 */
public class LocalMatchFragment extends Fragment {
    private static final String TAG = "LocalMatchFragment";
    private ImageView mIvArrowLeft;
    private ImageView mIvArrowRight;
    private ViewPager mVpContainer;
    private TabPageIndicator mIndicator;
    private ImageView mIvSwitch;
    private List<LocalScoreFragment> fragments = new ArrayList<>();
    private static final int REQUEST_PERMISSION_CODE = 0x999;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static int mMaxCount;
    private boolean mBound; // 是否bound

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(),R.layout.fragment_local_match,null);
    }

    private GetScoreListListener getScoreListListener = new GetScoreListListener() {
        @Override
        public void execute(ScoreListBean scoreListBean) {

        }
    };



    public void showAddOrDeletePlayerDialog(){
        if (mVpContainer != null && mVpContainer.getChildCount() > 0) {
            LocalScoreFragment fragment = fragments.get(mVpContainer.getCurrentItem());
            if(fragment.hasScore()){
                BowlingManager.getInstance().addOrDeleteListener.addListener(new AddOrDeleteListener() {
                    @Override
                    public void execute(List<PlayerBean> playerBeans) {

                    }
                });
                BowlingAddorDeletePlayerDialog dialog = new BowlingAddorDeletePlayerDialog(getContext())
                        .setPlayerList(fragment.getPlayers());
                dialog.show();
                MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
                if(mainActivity != null){
                    mainActivity.setAddorDeleteDialog(dialog);
                }
            }
        }
    }

    public ListView getListView(){
        if(mVpContainer != null && mVpContainer.getChildCount() > 0) {
            LocalScoreFragment fragment = fragments.get(mVpContainer.getCurrentItem());
            return fragment.getListView();
        }
        return null;
    }

    public List<PlayerBean> getPlayBean(){
        if(mVpContainer != null && mVpContainer.getChildCount() > 0) {
            LocalScoreFragment fragment = fragments.get(mVpContainer.getCurrentItem());
            return fragment.getPlayBeans();
        }
        return null;
    }

    public boolean isWatchStatus(){
        if(mVpContainer != null && mVpContainer.getChildCount() > 0){
            LocalScoreFragment fragment = fragments.get(mVpContainer.getCurrentItem());
            if(fragment.isWatch()){
                return true;
            }
            return false;
        }else{
            return false;
        }
    }

    public boolean hasMatch(){
        if(mVpContainer != null && mVpContainer.getChildCount() > 0){
            LocalScoreFragment fragment = fragments.get(mVpContainer.getCurrentItem());
            if(fragment.hasScore()){
                return true;
            }
            if(!fragment.isWatch()){
                ToastUtil.showText(getContext(),R.string.current_channel_no_start);
            }
            return false;
        }else{
            ToastUtil.showText(getContext(),R.string.current_channel_no_start);
            return false;
        }
    }


    private BaseListener changeChannel = new BaseListener() {
        @Override
        public void execute(Object o) {

            String string = (String) SharedPreferencesUtils.getParam(SharedPreferencesUtils.CHANNEL_ARRAYS,"0");
            String[] strings = string.split(",");
            int newCount = strings.length;
            // 添加新fragment
            if(TextUtils.isEmpty(string)){
                newCount = 0;
            }
            reInitTab(newCount,strings);
        }
    };
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkMaxTabCount();
        initView();
        checkPermission();
        int height = getResources().getDisplayMetrics().heightPixels;
        int width = getResources().getDisplayMetrics().widthPixels;
        float destiny = getResources().getDisplayMetrics().density;
        BowlingManager.getInstance().getScoreListListener.addListener(getScoreListListener);
        BowlingManager.getInstance().changeChannelListener.addListener(changeChannel);
        Log.d(TAG,"heightdpi=" + height / destiny + "widthdpi=" + width / destiny + "destiny=" + destiny);
    }

    private void checkPermission(){
        PermissionUtils.checkAndRequestMorePermissions(getContext(),permissions,REQUEST_PERMISSION_CODE);
    }

    private void checkMaxTabCount(){
        mMaxCount = (int)DeviceUtils.getScreenWidth() / 230;
    }

    private void setArrowShowState(int position,int count){
        if(position == 0){
            mIvArrowLeft.getBackground().setAlpha(127);
            if(count > 1){
                mIvArrowRight.getBackground().setAlpha(255);
            }
        }else if(position == count -1){
            mIvArrowRight.getBackground().setAlpha(127);
            mIvArrowLeft.getBackground().setAlpha(255);
        }else{
            mIvArrowLeft.getBackground().setAlpha(255);
            mIvArrowRight.getBackground().setAlpha(255);
        }
    }

    public boolean hasRemoteMatch(){
        if(mVpContainer != null){
            if(fragments == null || fragments.size() == 0){
                return false;
            }
            LocalScoreFragment LocalScoreFragment = fragments.get(mVpContainer.getCurrentItem());
            return LocalScoreFragment.hasRemoteGame();
        }
        return false;
    }

    public String getRemoteOnlineId(){
        if(mVpContainer != null){
            if(fragments == null || fragments.size() == 0){
                return  "";
            }
            LocalScoreFragment LocalScoreFragment = fragments.get(mVpContainer.getCurrentItem());
            return LocalScoreFragment.getRemoteOnLineId();
        }
        return "";
    }




    private void initView(){
        mIvArrowLeft = (ImageView) getView().findViewById(R.id.iv_channel_left);
        mIvArrowLeft.getBackground().setAlpha(127);
        mIvArrowRight = (ImageView) getView().findViewById(R.id.iv_channel_right);
        mIvArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVpContainer.getCurrentItem() > 0){
                    mVpContainer.setCurrentItem(mVpContainer.getCurrentItem() - 1);
                }
            }
        });
        mIvArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVpContainer.getCurrentItem() < fragments.size() - 1){
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
        initTab();
    }

    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d(TAG,"viewpager滑动..." + positionOffset);
            setVpOffset(positionOffset);
            if(positionOffset == 0F || positionOffset == 1F){
                if(mVpContainer.getCurrentItem() != mLastItem){
                    MainActivity activity = (MainActivity) BowlingUtils.getMainActivity();
                    if(activity != null){
                        activity.resetRemoteFragment();
                    }
                    LogcatFileManager.getInstance().writeLog(TAG,"界面发声了切换");
                    mLastItem = mVpContainer.getCurrentItem();
                    BottomPersonInfoHelper.getInstance().hide();
                    // 获取对应fragment
                    LocalScoreFragment LocalScoreFragment = fragments.get(mLastItem);
                    if(!LocalScoreFragment.isCreated()){
                        return;
                    }
                    LocalScoreFragment.getScore();
                }

            }
        }

        @Override
        public void onPageSelected(int position) {
            if(position == mLastItem){
                return;
            }
            if(mTvCurrentView != null){
                mTvCurrentView.setTextColor(Color.GRAY);
            }
            TextView view = (TextView) mIndicator.getChildView(position);
            if(view != null){
                view.setTextColor(Color.WHITE);
            }
            mTvCurrentView = view;
            setArrowShowState(position,fragments.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    // 初始化当前列表
    private void reInitTab(int tabCount,String[] arrarys){
        mLastItem = 0;
        mTabs.clear();
        if(tabCount >= 2){
            mIvArrowLeft.setVisibility(View.VISIBLE);
            mIvArrowRight.setVisibility(View.VISIBLE);
        }
        // sort
        final int[] ints = new int[tabCount];
        try{
            if(tabCount > 0){
                for(int i = 0; i < ints.length;i++){
                    ints[i] = Integer.valueOf(arrarys[i]) + 1;
                }
                Arrays.sort(ints);
            }
        }catch (Exception e){
            tabCount = 0;
        }
        LocalScoreFragment.mIsLoading = false;
        if(tabCount >= mMaxCount){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL,0);
        }else{
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        }
        fragments.clear();
        for(int i = 0;i < tabCount;i++){
            try {
                LocalScoreFragment listFragment = new LocalScoreFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(LocalScoreFragment.EXTRA_LANE_NUMBER,ints[i]);
                listFragment.setArguments(bundle);
                fragments.add(listFragment);
            }catch (Exception e){

            }
        }
        FragmentStatePagerAdapter mAdapte = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }


            @Override
            public CharSequence getPageTitle(int position) {
                if(BowlingUtils.currentLanguageIsSimpleChinese(BowlingApplication.getContext())){
                    return (ints[position] + " " + getResources().getString(R.string.channel));
                }else{
                    return (getResources().getString(R.string.channel) + " " + ints[position]);
                }
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mVpContainer.setOnPageChangeListener(mListener);
        mVpContainer.setAdapter(mAdapte);
        if(arrarys == null){
            return;
        }
        mIndicator.setOnPageChangeListener(mListener);
        if(mIndicator.getViewPager() != mVpContainer){
            mIndicator.setViewPager(mVpContainer);
        }else {
            mIndicator.notifyDataSetChanged();
        }
        for(int m = 0; m < mIndicator.getTabChildCount();m++){
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
            if(m == 0){
                currentView.setTextColor(Color.WHITE);
                mTvCurrentView = currentView;
            }
        }

    }
    private List<TextView> mTabs = new ArrayList<>();
    private void initTab(){
        // 默认1道
        String string = (String) SharedPreferencesUtils.getParam(SharedPreferencesUtils.CHANNEL_ARRAYS,"0");
        String[] strings = string.split(",");
        int newCount = strings.length;
        reInitTab(newCount,strings);
    }
    private int mLastItem = 0;

    private static float mOfffset;

    public static boolean vpIsAllScreen(){
        return mOfffset == 0F || mOfffset == 1F;
    }

    public static void setVpOffset(float offset){
        mOfffset = offset;
    }

    private TextView mTvCurrentView;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BowlingManager.getInstance().getScoreListListener.removeListener(getScoreListListener);
        BowlingManager.getInstance().changeChannelListener.removeListener(changeChannel);
        WebSocketClientService.getInstance().onStop();
        super.onDestroy();
    }



    private boolean isFromDialog = false;

    private void verifyStoragePermission(Activity activity) {
        isFromDialog = true;
        //1.检测权限
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PermissionChecker.PERMISSION_GRANTED) {
            //2.没有权限，弹出对话框申请
            ActivityCompat.requestPermissions(activity,permissions,REQUEST_PERMISSION_CODE);
        }else{
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openCamera();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            //权限申请成功
            //  Toast.makeText(this, "授权SD卡权限成功", Toast.LENGTH_SHORT).show();
            if(isFromDialog){
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.openCamera();
            }
        } else {
            //权限申请失败
            ToastUtil.showText(getContext(),R.string.sd_failure);
        }
        isFromDialog = false;
    }
}
