package com.cloudysea.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.utils.TypefaceUtil;

import java.util.Locale;

/**
 * @author roof 2019/11/2.
 * @email lyj@yhcs.com
 * @detail
 */
public class BaseScoreFragment extends Fragment {

    private static final int DEFAULT_SCORE_COUNT = 10;
    protected boolean  mNowMode;

    protected void addTitle(LinearLayout linearLayout, boolean isNewMode){
        if(linearLayout == null){
            linearLayout = (LinearLayout) getView().findViewById(R.id.ll_cover_for_score_title);
        }
        linearLayout.removeAllViews();
        mNowMode = isNewMode;
        View view  = new View(getContext());
        linearLayout.addView(view,getTitleParams(false,1.0F));
        for(int i = 0; i < DEFAULT_SCORE_COUNT;i++){
            View item = View.inflate(getContext(),R.layout.item_ball_score_title,null);
            TextView textView = (TextView) item.findViewById(R.id.tv_ball_score_title);
            textView.setText(String.format(Locale.getDefault(),"%d",i+1));
            textView.setTypeface(TypefaceUtil.getStyleOneInstance());
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            if(!isNewMode && i == 9){
                linearLayout.addView(item,getTitleParams(false,1.5F));
            }else{
                linearLayout.addView(item,getTitleParams(false,1.0F));
            }
        }
        View total = View.inflate(getContext(),R.layout.item_ball_score_title,null);
        TextView titleItem = (TextView) total.findViewById(R.id.tv_ball_score_title);
        if(titleItem != null){
            titleItem.setBackgroundColor(Color.parseColor("#0F082A"));
            titleItem.setTypeface(TypefaceUtil.getStyleOneInstance());
        }
        linearLayout.addView(total,getTitleParams(false,1.5F));
    }

    protected LinearLayout.LayoutParams getTitleParams(boolean hasTitle, float weight){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, (int) (29 * DeviceUtils.getDestiny()),weight);
        return params;
    }
}
