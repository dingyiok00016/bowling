package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.utils.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author roof 2019/10/3.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingMeVipDialog extends BowlingCommonDialog {

    private EditText mEtInputNumber;
    private int mStyle;
    public static final int STYLE_MEMBER_VIP = 0x00;
    public static final int STYLE_CONNECT_VIP = 0x01;
    private TextView mTvTitle;
    private String mId;
    public BowlingMeVipDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        mTvTitle = (TextView) findViewById(R.id.tv_me_is_vip_title);
        mEtInputNumber = (EditText) findViewById(R.id.et_input_vip_number);
        Button mButton = (Button) findViewById(R.id.btn_me_is_vip_confirm);

        mTvTitle.setTypeface(TypefaceUtil.getStyleOneInstance());
        mEtInputNumber.setTypeface(TypefaceUtil.getStyleOneInstance());
        if(!BowlingUtils.currentLanguageIsSimpleChinese(BowlingApplication.getContext())){
            setSpanHintString(R.string.please_input_vip_number);
        }
        mButton.setTypeface(TypefaceUtil.getStyleTwoInstance());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildMeIsVip();
                dismiss();
            }
        });
    }

    private void setSpanHintString(int resId){
        SpannableString ss = new SpannableString(getContext().getResources().getString(resId));//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(10,true);//设置字体大小 true表示单位是
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEtInputNumber.setHint(new SpannedString(ss));
    }


    public void setId(String id){
        mId = id;
    }

    public void setStyle(int style){
        mStyle = style;
        if(mStyle == STYLE_CONNECT_VIP){
            mEtInputNumber.setHint("请输入识别码");
            mEtInputNumber.setInputType(InputType.TYPE_CLASS_TEXT);
            mTvTitle.setText("识别码");
        }
    }

    private void buildMeIsVip(){
        if(mStyle == STYLE_MEMBER_VIP){
            if(TextUtils.isEmpty(mEtInputNumber.getText())){
                ToastUtil.showText(getContext(),R.string.no_member_number);
                return;
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("AuthCode","120");
                jsonObject.put("Id",UUID.randomUUID().toString());
                jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
                jsonObject.put("Name","GetMembership");
                jsonObject.put("Type","1");
                JSONObject child = new JSONObject();
                child.put("MembershipNumber",mEtInputNumber.getText().toString());
                jsonObject.put("Data",child);
                BowlingClient.getInstance().handleMsg(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            if(TextUtils.isEmpty(mEtInputNumber.getText())){
                ToastUtil.showText(getContext(),R.string.no_cloud_number);
                return;
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("AuthCode","120");
                jsonObject.put("Id",UUID.randomUUID().toString());
                jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
                jsonObject.put("Name","LinkCloudUser");
                jsonObject.put("Type","1");
                JSONObject child = new JSONObject();
                child.put("IdCode",mEtInputNumber.getText().toString());
                child.put("BowlerId",mId);
                jsonObject.put("Data",child);
                BowlingClient.getInstance().handleMsg(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_me_is_vip;
    }
}
