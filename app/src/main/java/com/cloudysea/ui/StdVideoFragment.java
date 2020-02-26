package com.cloudysea.ui;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author roof 2020/1/8.
 * @email lyj@yhcs.com
 * @detail
 */
public class StdVideoFragment extends Fragment {


    private TabPageIndicator mIndicator;
    private List<TextView> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapte;
    private ViewPager mVpContainer;
    private List<VideoChildFragment> fragments = new ArrayList<>();

    private static float mOffRemoteSet;
    private int mLastItem = 0;
    private TextView mTvCurrentView;

    public static void setRemoteoffset(float offset){
        mOffRemoteSet = mOffRemoteSet;
    }

    private ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setRemoteoffset(positionOffset);
            if (positionOffset == 0F || positionOffset == 1F) {
                if (mVpContainer.getCurrentItem() != mLastItem) {
                    mIndicator.setSelectedTabIndex(mVpContainer.getCurrentItem());
                    mLastItem = mVpContainer.getCurrentItem();
                    MainActivity mainActivity = (MainActivity) getActivity();
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_std_video, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("StdVideoFragment","走这里了");
        mIndicator = (TabPageIndicator) getView().findViewById(R.id.tabIndicator);
        mVpContainer = (ViewPager) getView().findViewById(R.id.vp_score_list_container);
        String[] arrays = new String[]{"推荐","热点","新时代","电视剧","电影","综艺","动漫","儿童","小视频","北京","搞笑"};
        reInitTab(arrays.length,arrays);
        super.onActivityCreated(savedInstanceState);
    }

    // 初始化当前列表
    private void reInitTab(int tabCount, String[] arrarys) {
        mTabs.clear();
        // sort
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        fragments.clear();
        for (int i = 0; i < tabCount; i++) {
            try {
                VideoChildFragment listFragment = new VideoChildFragment();
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
                return arrarys[position];
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
    }
}
