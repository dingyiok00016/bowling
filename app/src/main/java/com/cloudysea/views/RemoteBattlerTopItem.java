package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.utils.BowlingUtils;

/**
 * @author roof 2019/10/17.
 * @email lyj@yhcs.com
 * @detail battler头部item
 */
public class RemoteBattlerTopItem extends FrameLayout {
    private ViewGroup mRootView;
    private TextView mTvTitle;
    private ImageView mIvPullDown;
    private String[] mSelectArrays;
    private String mStrContent;
    private String mStrTitle;
    private TextView mTvContent;
    private MultipleChoicePopWindow mPopWindow;
    private int mStyle;

    public RemoteBattlerTopItem(@NonNull Context context) {
        super(context);
        addChildView();
    }

    public RemoteBattlerTopItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addChildView();
    }


    private void addChildView(){
        mRootView = (ViewGroup) View.inflate(getContext(),R.layout.layout_remote_battler_top_item,null);
        addView(mRootView);
        initView();
        initListener();
    }

    private void initView(){
        mTvTitle = (TextView) mRootView.findViewById(R.id.tv_top_item_title);
        mIvPullDown = (ImageView) mRootView.findViewById(R.id.iv_top_item_select);
        mTvContent = (TextView) mRootView.findViewById(R.id.tv_top_item_content);
        mTvTitle.setTextSize(20 * BowlingUtils.Glbal_SIZE_RADIO);
        mTvContent.setTextSize(20 * BowlingUtils.Glbal_SIZE_RADIO);
    }

    private void initListener(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(mPopWindow == null){
                        mPopWindow = new MultipleChoicePopWindow(getContext())
                                .setTitle(mStrTitle)
                                .setSelectArray(mSelectArrays)
                                .setChoice(mStyle)
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopWindow.dismiss();
                                        setVisibility(View.VISIBLE);
                                    }
                                })
                                .setSelectListener(mListener);
                        mPopWindow.setOutsideTouchable(false);
                        mPopWindow.setWidth((int) (getWidth() + 2 * DeviceUtils.getDestiny()));

                    }
                    mPopWindow.setContent(mStrContent);
                    mIvPullDown.setTag(Boolean.TRUE);
                    mPopWindow.showAsDropDown(RemoteBattlerTopItem.this);
                    setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setPopwindowContent(String str){
        if(mPopWindow != null){
            mPopWindow.setContent(str);
        }
    }

    public RemoteBattlerTopItem setTitle(CharSequence charSequence){
        if(TextUtils.isEmpty(charSequence)){
            mStrTitle = charSequence + "|";
            mTvTitle.setText(mStrTitle);
        }
        return this;
    }

    public RemoteBattlerTopItem setStyle(int style){
        mStyle= style;
        return this;
    }

    public RemoteBattlerTopItem setTitle(int resource){
        if(resource != 0){
            mStrTitle = getContext().getResources().getText(resource) + "|";
            mTvTitle.setText(mStrTitle);
        }
        return this;
    }

    public RemoteBattlerTopItem setContent(String[] contents){
        if(contents.length > 0){
            StringBuilder sb = new StringBuilder();
            for(int i = 0 ; i < contents.length;i++){
                sb.append(contents[i] + " ");
            }
            mStrContent = sb.toString();
            mTvContent.setText(mStrContent);
        }
        return this;
    }

    public RemoteBattlerTopItem setContent(String content){
        mStrContent = content;
        mTvContent.setText(mStrContent);
        return this;
    }

    public RemoteBattlerTopItem setSelectArray(String[] selectArray){
        mSelectArrays = selectArray;
        return this;
    }

    private RemoteBattlerTopItemSelectListener mListener;
    public interface RemoteBattlerTopItemSelectListener{
        void select(String str,int positon,boolean select,boolean all);
    }

    public void setOnSelectLisenter(RemoteBattlerTopItemSelectListener l){
        mListener = l;
    }


}
