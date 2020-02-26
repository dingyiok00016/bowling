package com.cloudysea.adapter.holder;

import android.view.View;

import com.cloudysea.bean.PlayerBean;

/**
 * @author roof 2019/9/8.
 * @email lyj@yhcs.com
 * @detail
 */
public abstract class BaseHolder<T> {
    private View covertView;
    private int position;
    public BaseHolder(View v){
        covertView = v;
    }

    public void bind(int p, T t){
        position = p;
        bindData(position,t);
    }

    public abstract void bindData(int position,T t);

    public View findViewById(int id){
        return covertView.findViewById(id);
    }
}
