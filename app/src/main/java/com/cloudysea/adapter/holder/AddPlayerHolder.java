package com.cloudysea.adapter.holder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.adapter.AddPlayerAdapter;
import com.cloudysea.bean.ImageSetBean;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.controller.ImageSetListener;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.CropSquareTransformation;
import com.cloudysea.utils.TypefaceUtil;
import com.cloudysea.views.BowlingMeVipDialog;

/**
 * @author roof 2019/9/18.
 * @email lyj@yhcs.com
 * @detail
 */
public class AddPlayerHolder extends RecyclerView.ViewHolder {
    private final String UUID= java.util.UUID.randomUUID().toString();
    private TextView mTvNickName;
    private EditText mTvEditName;
    private TextView mTvToPhoto;
    private ImageView mIvAddPlayers;
    private ViewGroup mContainer;
    private ImageView mMeIsVip;
    private TextView mTvMeisVip;
    private ImageView mIvAvatar;
    private RelativeLayout mRlDelete;
    private View.OnClickListener mAddListener;
    private PlayerBean mPlayerBean;
    private AddPlayerAdapter mAdapter;
    private int mPositon;
    ImageSetListener listener = new ImageSetListener() {

        @Override
        public void execute(ImageSetBean imageSetBean) {
            MainActivity mainActivity = BowlingUtils.getMainActivity();
            if(mainActivity != null){
                if (imageSetBean.uuid.equals(UUID)) {
                    if(BowlingUtils.isAndroidQ()){
                        mIvAvatar.setImageURI(imageSetBean.uri);
                    }else{
                        BowlingUtils.showImage(mIvAvatar, mainActivity.getRealImagePathFromUri(imageSetBean.uri), mPlayerBean
                                , new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(mAdapter != null){
                                            mAdapter.setNewPlayer(mPositon,mPlayerBean);
                                        }

                                    }
                                }, (int) (DeviceUtils.getDestiny() * 10.5));
                    }
                }
            }
        }
    };
    public AddPlayerHolder(View itemView) {
        super(itemView);
        BowlingManager.getInstance().imageListener.addListener(listener);
        mTvNickName = (TextView) itemView.findViewById(R.id.tv_photo_name);
        mContainer = (ViewGroup) itemView.findViewById(R.id.rl_avatar_container);
        mTvEditName = (EditText) itemView.findViewById(R.id.tv_edit_name);
        mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar_to_photo);
        mTvToPhoto = (TextView) itemView.findViewById(R.id.tv_to_photo_action);
        mTvNickName.setTypeface(TypefaceUtil.getStyleOneInstance());
        mTvMeisVip = (TextView) itemView.findViewById(R.id.tv_me_is_vip);
        mRlDelete = (RelativeLayout) itemView.findViewById(R.id.rl_delete_or_add);
        mTvMeisVip.setTypeface(TypefaceUtil.getStyleTwoInstance());
        mTvEditName.setTypeface(TypefaceUtil.getStyleOneInstance());
        mTvToPhoto.setTypeface(TypefaceUtil.getStyleOneInstance());
        mMeIsVip = (ImageView) itemView.findViewById(R.id.iv_vip_icon);
        mIvAddPlayers = (ImageView) itemView.findViewById(R.id.iv_delete_or_add_icon);
        mRlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddListener != null){
                    mAddListener.onClick(v);
                }
            }
        });
        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BowlingUtils.sCurrentUUID = UUID;
                MainActivity mainActivity = BowlingUtils.getMainActivity();
                if(mainActivity != null){
                    mainActivity.openCamera();
                }
            }
        });
        mTvEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s == null || s.length() == 0){

                }else{
                    mPlayerBean.BowlerName = s.toString();
                    if(mAdapter != null){
                        mAdapter.setNewPlayer(mPositon,mPlayerBean);
                    }
                }
            }
        });
        mTvMeisVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = BowlingUtils.getMainActivity();
                if(mainActivity != null){
                    BowlingMeVipDialog dialog = new BowlingMeVipDialog(mainActivity);
                    BowlingUtils.Global_ME_VIP_POSITION = mPositon;
                    dialog.show();
                }
            }
        });
        if(!BowlingUtils.isPad()){

        }

    }

    public int getPositon() {
        return mPositon;
    }

    public void addAddListener(View.OnClickListener listener){
        mAddListener = listener;
    }


    public void setAdapter(AddPlayerAdapter adapter){
        mAdapter = adapter;
    }


    public void bindData(PlayerBean playerBean,int position){
        mPositon = position;
        mPlayerBean = playerBean;
        mTvEditName.setText(playerBean.BowlerName);
        if(playerBean.HeadPortrait != null && !TextUtils.isEmpty((CharSequence) playerBean.HeadPortrait)){
            Bitmap bitmap = BowlingUtils.stringToBitmap((String) playerBean.HeadPortrait);
            mIvAvatar.setImageBitmap(CropSquareTransformation.getRoundedCornerBitmap(bitmap, (int) (DeviceUtils.getDestiny() * 10.5)));
        }else{
            mIvAvatar.setImageResource(R.drawable.head_default);
        }
        if(mPlayerBean.IsMembership){
            mMeIsVip.setVisibility(View.VISIBLE);
        }else{
            mMeIsVip.setVisibility(View.GONE);
        }
    }
}
