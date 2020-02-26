package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.bean.SwitchBean;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author roof 2019/9/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingSwitchDialog extends BowlingCommonDialog {
    public BowlingSwitchDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        initTypeFace();
    }

    private void initTypeFace(){
        TextView textViewTitle = (TextView) findViewById(R.id.tv_switch_title);
        TextView textViewContent = (TextView) findViewById(R.id.tv_switch_content);
        TextView textViewCancel = (TextView) findViewById(R.id.tv_switch_cancel);
        TextView textViewConfirm = (TextView) findViewById(R.id.tv_switch_confirm);

        textViewTitle.setTypeface(TypefaceUtil.getStyleOneInstance());
        textViewContent.setTypeface(TypefaceUtil.getStyleOneInstance());
        textViewCancel.setTypeface(TypefaceUtil.getStyleTwoInstance());
        textViewConfirm.setTypeface(TypefaceUtil.getStyleTwoInstance());

        textViewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildSwitchBean();
                dismiss();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void buildSwitchBean(){
        SwitchBean bean = new SwitchBean();
        bean.Id = UUID.randomUUID().toString();
        bean.AuthCode = "120";
        bean.LaneNumber = "1";
        bean.Name = bean.getName();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode",bean.AuthCode);
            jsonObject.put("Id",bean.Id);
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("Name",bean.Name);
            jsonObject.put("Type",bean.Type);
            JSONObject child = new JSONObject();
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_switch;
    }
}
