package com.cloudysea.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author roof 2019/10/5.
 * @email lyj@yhcs.com
 * @detail
 */
public abstract class BaseFragment extends Fragment {
    private boolean mIsDataInited;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewCreated();
    }

    protected void onViewCreated() {
        initView();
        initListener();
        if (!mIsDataInited) {
            if (getUserVisibleHint()) {
                initData();
                mIsDataInited = true;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //防止数据预加载, 只预加载View，不预加载数据
        if (isVisibleToUser && isVisible() && !mIsDataInited) {
            initData();
            mIsDataInited = true;
        }
    }

    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();
}
