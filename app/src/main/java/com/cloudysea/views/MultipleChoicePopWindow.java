package com.cloudysea.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.utils.BowlingChoiceViewManager;
import com.cloudysea.utils.BowlingUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/10/17.
 * @email lyj@yhcs.com
 * @detail
 */
public class MultipleChoicePopWindow extends PopupWindow {
    private TextView mTvTitle;
    private TextView mTvContent;
    private String[] mContents;
    private Context mContext;
    private View mView;
    private LinearLayout mLLContainer;
    private View.OnClickListener mListener;
    private RemoteBattlerTopItem.RemoteBattlerTopItemSelectListener mSelectListner;



    public MultipleChoicePopWindow(Context context) {
        super(context);
        mContext = context;
        initPopWindow(context);
    }



    public void initPopWindow(Context context){
        View view = View.inflate(context,R.layout.layout_pop_window_common,null);
        mView = view.findViewById(R.id.layout_pop_window_common);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_top_item_select);
        imageView.setImageResource(R.drawable.btn_retract);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onClick(v);
                }
            }
        });
        mView.setBackground(null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_top_item_title);
        mTvContent = (TextView) view.findViewById(R.id.tv_top_item_content);
        mTvTitle.setTextSize(20 * BowlingUtils.Glbal_SIZE_RADIO);
        mTvContent.setTextSize(20 * BowlingUtils.Glbal_SIZE_RADIO);
        mLLContainer = (LinearLayout) view.findViewById(R.id.ll_pop_window_common_container);
        setContentView(view);
    }


    public MultipleChoicePopWindow setTitle(CharSequence charSequence){
        mTvTitle.setText(charSequence);
        return this;
    }

    public MultipleChoicePopWindow setContent(String charSequence){
        mTvContent.setText(charSequence);
        return this;
    }


    public MultipleChoicePopWindow setSelectArray(String[] charSequence){
        mContents = charSequence;
        // mTvContent.setText(charSequence);
        return this;
    }

    public MultipleChoicePopWindow setOnClickListener(View.OnClickListener l){
        mListener = l;
        // mTvContent.setText(charSequence);
        return this;
    }

    public MultipleChoicePopWindow setSelectListener(RemoteBattlerTopItem.RemoteBattlerTopItemSelectListener l){
        mSelectListner = l;
        // mTvContent.setText(charSequence);
        return this;
    }

    public MultipleChoicePopWindow setChoice(int choiceStyle){
        mLLContainer.removeAllViews();
        switch (choiceStyle){
            case RemoteBattlerTopView.STYLE_DEVICE:
                addMultiChoice();
                break;
            case RemoteBattlerTopView.STYLE_CHANNEL:
                addSingleChoice();
                break;
            case RemoteBattlerTopView.STYLE_SCORE:
                addSingleChoice();
                break;
        }
        return this;
    }

    @Override
    public void showAsDropDown(View anchor) {
        showAsDropDown(anchor,0, (int) (-37 * DeviceUtils.getDestiny()));
    }

    private void addSingleChoice(){
        if(mContents == null){
            return;
        }
        for(int i = 0; i < mContents.length;i++){
            final int m = i;
            TextView view = (TextView) View.inflate(mContext,R.layout.item_single_choice,null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) (10F * DeviceUtils.getDestiny());
            params.bottomMargin = (int) (22.5F * DeviceUtils.getDestiny());
            view.setLayoutParams(params);
            view.setText(mContents[i]);
            mLLContainer.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mSelectListner != null){
                        mSelectListner.select(mContents[m],m,true,mContents[m].endsWith(BowlingApplication.getContext().getResources().getString(R.string.all)));
                    }
                    mListener.onClick(v);
                }
            });
        }
    }
    private List<String> mSelectList = new ArrayList<>();
    private void addMultiChoice(){
        if(mContents == null){
            return;
        }
        for(int i = 0; i < mContents.length;i++){
            final int m = i;
            ViewGroup view = (ViewGroup) View.inflate(mContext,R.layout.item_mulpti_choice,null);
            BowlingChoiceViewManager.init(view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getTag() == null){
                        mSelectList.remove(mContents[m]);
                    }else{
                        mSelectList.add(mContents[m]);
                    }
                    if(mSelectListner != null){
                        mSelectListner.select(mContents[m],m,v.getTag() != null,mContents[m].endsWith(BowlingApplication.getContext().getResources().getString(R.string.all)));
                    }
                }
            },mContents[i]);
            // 默认是被选择的
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_checked_icon_1);
            imageView.setTag(Boolean.TRUE);
            imageView.setImageResource(R.drawable.icon_tick);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) (10F * DeviceUtils.getDestiny());
            params.bottomMargin = (int) (22.5F * DeviceUtils.getDestiny());
            view.setLayoutParams(params);
            mLLContainer.addView(view);
        }
    }



}
