package com.cloudysea.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudysea.coinfig.DeviceUtils;

/**
 * @author roof 2019/9/18.
 * @email lyj@yhcs.com
 * @detail decoration
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mItemRight;
    public SpacesItemDecoration(float horizontalSet){
        mItemRight = (int) (horizontalSet * DeviceUtils.getDestiny());
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //只是添加下面这一行代码
        outRect.set(0, 0, mItemRight, 0);
    }
}
