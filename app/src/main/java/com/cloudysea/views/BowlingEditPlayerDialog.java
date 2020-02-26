package com.cloudysea.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.GetMembership;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.bean.ImageSetBean;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.controller.ImageSetListener;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.CropSquareTransformation;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.utils.TypefaceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author roof 2019/9/20.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingEditPlayerDialog extends BowlingCommonDialog {
    private PlayerBean mPlayerBean;
    private TextView textView;
    private TextView tvNickName;
    private ImageView ivAvatar;
    private ImageView mIvIsVip;
    private EditText tvEditName;
    private RelativeLayout RlAvatarContainer;
    private final String UUID = java.util.UUID.randomUUID().toString();
    public BowlingEditPlayerDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        initTypeface();
    }

    private void initTypeface(){
        ImageSetListener listener = new ImageSetListener() {

            @Override
            public void execute(ImageSetBean imageSetBean) {
                MainActivity mainActivity = BowlingUtils.getMainActivity();
                if(mainActivity != null){
                    if (imageSetBean.uuid.equals(UUID)) {
                        if(!BowlingUtils.isAndroidQ()){
                            BowlingUtils.showImage(ivAvatar,mainActivity.getRealImagePathFromUri(imageSetBean.uri),mPlayerBean);
                        }else{
                            ivAvatar.setImageURI(imageSetBean.uri);
                        }
                    }
                }
            }
        };
        BowlingManager.getInstance().imageListener.addListener(listener);
        textView = (TextView) findViewById(R.id.tv_title_edit_player);
        textView.setTypeface(TypefaceUtil.getStyleOneInstance());
        TextView textViewComplete = (TextView) findViewById(R.id.tv_complete_action);
        textViewComplete.setTypeface(TypefaceUtil.getStyleTwoInstance());
        tvNickName = (TextView) findViewById(R.id.tv_photo_name);
        mIvIsVip = (ImageView) findViewById(R.id.iv_vip_icon);
        RlAvatarContainer = (RelativeLayout) findViewById(R.id.rl_avatar_container);
        tvEditName = (EditText) findViewById(R.id.tv_edit_name);
        TextView tvToPhoto = (TextView) findViewById(R.id.tv_to_photo_action);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar_to_photo);
        tvNickName.setTypeface(TypefaceUtil.getStyleOneInstance());
        tvEditName.setTypeface(TypefaceUtil.getStyleOneInstance());
        tvToPhoto.setTypeface(TypefaceUtil.getStyleOneInstance());
        TextView tvMeVip = (TextView) findViewById(R.id.tv_me_is_vip);
        tvMeVip.setTypeface(TypefaceUtil.getStyleTwoInstance());
        tvMeVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BowlingMeVipDialog dialog = new BowlingMeVipDialog(getContext());
                dialog.show();
            }
        });
        RlAvatarContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BowlingUtils.sCurrentUUID = UUID;
                MainActivity mainActivity = BowlingUtils.getMainActivity();
                if(mainActivity != null){
                    mainActivity.openCamera();
                }
            }
        });
        textViewComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvEditName.getText() == null || tvEditName.getText().toString().length() == 0){
                    ToastUtil.showText(BowlingApplication.getContext(),R.string.player_is_blank);
                    return;
                }
                if(!tvEditName.getText().toString().equals(mPlayerBean.BowlerName)){
                    mPlayerBean.BowlerName = tvEditName.getText().toString();
                    mPlayerBean.hasChange = true;
                }
                if(mPlayerBean.hasChange){
                    BowlingClient.getInstance().handleMsg(buildPlayer(mPlayerBean));
                }
                dismiss();
            }
        });

    }

    private String buildPlayer(PlayerBean playerBean){
        JSONObject jsonObject = new JSONObject();
        JSONObject Data =new JSONObject();
        try {
            jsonObject.put("Data",Data);
            JSONObject BowlerInfos = new JSONObject();
            JSONArray bowlerArray = new JSONArray();
            bowlerArray.put(BowlerInfos);
            Data.put("BowlerInfos",bowlerArray);
            BowlerInfos.put("BowlerId",playerBean.Id);
            BowlerInfos.put("Name",playerBean.BowlerName);
            BowlerInfos.put("HeadPortrait",mPlayerBean.HeadPortrait);
            BowlerInfos.put("IsMembership",mPlayerBean.IsMembership);
            BowlerInfos.put("MembershipNumber",mPlayerBean.MembershipNumber);
            jsonObject.put("Id", java.util.UUID.randomUUID().toString());
            jsonObject.put("Name","UpdateBowlerInfo");
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("AuthCode",120);
            jsonObject.put("Type","0");
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  "";
    }

    public void setVipInfo(GetMembership getMembership){
        mPlayerBean.BowlerName = getMembership.getData().getNameX();
        mPlayerBean.HeadPortrait = getMembership.getData().getHeadPortrait();
        mPlayerBean.IsMembership = true;
        mPlayerBean.MembershipNumber = getMembership.getData().getMembershipNumber();
        mPlayerBean.hasChange = true;
        tvEditName.setText(mPlayerBean.BowlerName);
        Bitmap bitmap = BowlingUtils.stringToBitmap((String) mPlayerBean.HeadPortrait);
        mIvIsVip.setVisibility(View.VISIBLE);
        ivAvatar.setImageBitmap(CropSquareTransformation.getRoundedCornerBitmap(bitmap, (int) (DeviceUtils.getDestiny() * 9)));

    }


    public void setPlayBean(PlayerBean playBean){
        mPlayerBean = playBean;
        mPlayerBean.hasChange = false;
        tvEditName.setText(playBean.BowlerName);
        Bitmap bitmap = BowlingUtils.stringToBitmap((String) playBean.HeadPortrait);
        ivAvatar.setImageBitmap(CropSquareTransformation.getRoundedCornerBitmap(bitmap, (int) (DeviceUtils.getDestiny() * 9)));
        if(playBean.IsMembership){
            mIvIsVip.setVisibility(View.VISIBLE);
        }else{
            mIvIsVip.setVisibility(View.GONE);
        }
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_edit_player;
    }

}
