package com.cloudysea.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.ui.SplashActivity;
import com.cloudysea.adapter.ChannelSetAdapter;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.JCifsUtil;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.utils.TypefaceUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import static com.cloudysea.coinfig.DeviceUtils.getDestiny;
import static com.cloudysea.utils.SharedPreferencesUtils.CHANNEL_IP;
import static com.cloudysea.utils.SharedPreferencesUtils.HOST_IP;

/**
 * @author roof 2019/9/25.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingFunctionSetDialog extends BowlingCommonDialog implements View.OnClickListener {

    private static final String IP_DEFAULT = "192.168.1.199";
    private static final int SPAN_COUNT_PAD = 8;
    private static final int SPAN_COUNT_MOBILE = 6;
    private ViewGroup linearLayoutLanguage;
    private HashMap<Integer,Integer> languages = new HashMap<>();
    private ViewGroup linearLayoutChannel;
    private ChannelSetAdapter mAdapter;
    private ViewGroup mRoot;
    private EditText mEtIp;
    private int mCount;
    private TextView mTvCheck;
    public BowlingFunctionSetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyle() {
        return ALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        mRoot = (ViewGroup) findViewById(R.id.rl_function_container);
        initTypeface(mRoot);
        TextView textViewLagauageSet = (TextView) findViewById(R.id.tv_language_set);
        TextView textViewChannelSet = (TextView) findViewById(R.id.tv_channel_set);
        TextView textViewComplete = (TextView) findViewById(R.id.tv_complete_action);
        textViewComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete();
            }
        });
        textViewLagauageSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinearLayoutVisibleOrGone(R.id.ll_language_set_container);
            }
        });
        textViewChannelSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinearLayoutVisibleOrGone(R.id.ll_channel_container);
            }
        });
    }

    private boolean mShoundChange = true;
    private TextView mLastEnvText;
    private int mCurrentEvn = 0;
    private int mOriginalEvn = 0;


    private void setLinearLayoutVisibleOrGone(int id){
        if(id == R.id.ll_language_set_container && linearLayoutLanguage == null){
            if(linearLayoutChannel != null && linearLayoutChannel.getVisibility() == View.VISIBLE){
                linearLayoutChannel.setVisibility(View.GONE);
            }
            ViewStub viewStub = (ViewStub) findViewById(id);
            linearLayoutLanguage = (LinearLayout) viewStub.inflate();
            initListener(linearLayoutLanguage);
            int language = (int) SharedPreferencesUtils.getParam(SharedPreferencesUtils.LANGUAGE,1);
            mCurrentLanguage = language;
            Drawable myImage = getContext().getResources().getDrawable(R.drawable.shape_check_button_selector);
            initLanguages();
            myImage.setBounds(0, 0, (int) (25.5 * getDestiny()), (int) (25.5 * getDestiny()));
            RadioButton radioButton = (RadioButton) findViewById(languages.get(language));
            radioButton.setCompoundDrawables(myImage,null,null,null);
            initTypeface(linearLayoutLanguage);
            return;
        }
        if(id == R.id.ll_channel_container && linearLayoutChannel == null){
            if(linearLayoutLanguage != null && linearLayoutLanguage.getVisibility() == View.VISIBLE){
                linearLayoutLanguage.setVisibility(View.GONE);
            }
            ViewStub viewStub = (ViewStub) findViewById(id);
            linearLayoutChannel = (ViewGroup) viewStub.inflate();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rl_channel_container);
            mCurrentEvn = (int) SharedPreferencesUtils.getParam(HOST_IP,0);
            mOriginalEvn = mCurrentEvn;
            final TextView tvDebug = (TextView) findViewById(R.id.tv_debug_env);
            final TextView tvRelease = (TextView) findViewById(R.id.tv_release_env);
            final TextView tvLocal = (TextView) findViewById(R.id.tv_local_env);
            final TextView tvCheck = (TextView) findViewById(R.id.tv_animation_check);
            final TextView tvTvMode = (TextView) findViewById(R.id.tv_tv_mode);
            boolean isTvMode = SharedPreferencesUtils.isTvMode();
            if(isTvMode){
                tvTvMode.setTextColor(Color.WHITE);
            }else{
                tvTvMode.setTextColor(Color.GRAY);
            }
            tvTvMode.setTag(isTvMode);
            tvTvMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean currentMode = (boolean) v.getTag();
                    currentMode = !currentMode;
                    v.setTag(currentMode);
                    if(currentMode){
                        tvTvMode.setTextColor(Color.WHITE);
                    }else{
                        tvTvMode.setTextColor(Color.GRAY);
                    }
                    SharedPreferencesUtils.setParam(SharedPreferencesUtils.IsTvMode,currentMode);
                    BowlingApplication.sIsTvMode = currentMode;
                    ToastUtil.showText(BowlingApplication.getContext(),R.string.host_env_change);
                    mRoot.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ActivityStacks.getInstance().exit();
                        }
                    },1000);

                }
            });

            tvCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JCifsUtil.uploadImageAndAnimation(JCifsUtil.STYLE_UPLOAD_TEST);
                }
            });
            if(mCurrentEvn == 0){
                tvDebug.setTextColor(Color.WHITE);
                mLastEnvText = tvDebug;
            }else if(mCurrentEvn == 1){
                tvRelease.setTextColor(Color.WHITE);
                mLastEnvText = tvRelease;
            }else if (mCurrentEvn == 2){
                tvLocal.setTextColor(Color.WHITE);
                mLastEnvText = tvLocal;
            }
            tvDebug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mLastEnvText != null){
                        mLastEnvText.setTextColor(Color.GRAY);
                    }
                    tvDebug.setTextColor(Color.WHITE);
                    mLastEnvText = tvDebug;
                    mCurrentEvn = 0;
                }
            });
            tvRelease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mLastEnvText != null){
                        mLastEnvText.setTextColor(Color.GRAY);
                    }
                    tvRelease.setTextColor(Color.WHITE);
                    mLastEnvText = tvRelease;
                    mCurrentEvn = 1;
                }
            });
            tvLocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mLastEnvText != null){
                        mLastEnvText.setTextColor(Color.GRAY);
                    }
                    tvLocal.setTextColor(Color.WHITE);
                    mLastEnvText = tvLocal;
                    mCurrentEvn = 2;
                }
            });

            mEtIp = (EditText) findViewById(R.id.et_channel_ip_count);
            mEtIp.setText((String)SharedPreferencesUtils.getParam(CHANNEL_IP,IP_DEFAULT));
            // 手机适配
            if(!BowlingUtils.isPad()){
                mEtIp.getLayoutParams().width = (int) (150 * DeviceUtils.getDestiny());
            }
            final EditText editText = (EditText) findViewById(R.id.et_channel_container_count);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(mShoundChange && s != null){
                        if(s.length() == 0){

                        }else{
                            try{
                                int count = Integer.parseInt(s.toString());
                                if(count > 200){
                                    editText.setText(String.format(Locale.getDefault(),"%d",200));
                                }else{
                                    if(mAdapter != null){
                                        mAdapter.setHoldreCount(count);
                                        mAdapter.setArrays("");
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    mCount = count;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            editText.setFilters(new InputFilter[]{new NumberKeyListener() {
                @NonNull
                @Override
                protected char[] getAcceptedChars() {
                    return new char[]{'0','1','2','3','4','5','6','7','8','9','.'};
                }

                @Override
                public int getInputType() {
                    return InputType.TYPE_MASK_VARIATION;
                }
            }
            });
            int spanCount = BowlingUtils.isPad() ? SPAN_COUNT_PAD : SPAN_COUNT_MOBILE;
            recyclerView.setLayoutManager(new FunctionGridViewManager(getContext(),spanCount, (int) (DeviceUtils.getDestiny() * 200)));
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.bottom = (int) (getDestiny() * 19.5F);
                }
            });
           final  LinearLayout linearLayout = (LinearLayout) linearLayoutChannel.findViewById(R.id.ll_channel_all_set);
            final ImageView imageView = (ImageView) linearLayout.findViewById(R.id.iv_checked_icon_1);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAllSelect(linearLayout,imageView);
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAllSelect(linearLayout,imageView);
                }
            });
            RelativeLayout relativeLayout = (RelativeLayout) linearLayoutChannel.findViewById(R.id.rl_image_check);
            TextView textView = (TextView) findViewById(R.id.tv_checked_icon_text_left);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAllSelect(linearLayout,imageView);
                }
            });

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAllSelect(linearLayout,imageView);
                }
            });
            mAdapter = new ChannelSetAdapter();
            int count = (int) SharedPreferencesUtils.getParam(SharedPreferencesUtils.CHANNEL_COUNT,1);
            mShoundChange = false;
            mCount = count;
            editText.setText(String.format(Locale.getDefault(),"%d",count));
            mShoundChange = true;
            mAdapter.setHoldreCount(count);
            String string = (String) SharedPreferencesUtils.getParam(SharedPreferencesUtils.CHANNEL_ARRAYS,"0");
            mAdapter.setArrays(string);
            recyclerView.setAdapter(mAdapter);
            initTypeface(linearLayoutChannel);
            return;
        }

        if(id == R.id.ll_language_set_container){
            if(linearLayoutChannel != null && linearLayoutChannel.getVisibility() == View.VISIBLE){
                linearLayoutChannel.setVisibility(View.GONE);
            }
            if(linearLayoutLanguage.getVisibility() == View.VISIBLE){
                linearLayoutLanguage.setVisibility(View.GONE);
            }else{
                linearLayoutLanguage.setVisibility(View.VISIBLE);
            }
        }
        if(id == R.id.ll_channel_container){
            if(linearLayoutLanguage != null && linearLayoutLanguage.getVisibility() == View.VISIBLE){
                linearLayoutLanguage.setVisibility(View.GONE);
            }
            if(linearLayoutChannel.getVisibility() == View.VISIBLE){
                linearLayoutChannel.setVisibility(View.GONE);
            }else{
                linearLayoutChannel.setVisibility(View.VISIBLE);
            }
        }
    }

    private void clickAllSelect(LinearLayout linearLayout,ImageView imageView){
        if(linearLayout.getTag() == null){
            imageView.setImageResource(R.drawable.icon_tick);
            linearLayout.setTag(Boolean.TRUE);

            // 全选
            StringBuffer sb  = new StringBuffer();
            for(int i = 0; i < mAdapter.getItemCount();i++){
                sb.append(i);
                if(i != mAdapter.getItemCount() - 1){
                    sb.append(",");
                }
            }
            mAdapter.setArrays(sb.toString());
            mAdapter.notifyDataSetChanged();
        }else{
            imageView.setImageResource(0);
            linearLayout.setTag(null);
            mAdapter.setArrays("");
            mAdapter.notifyDataSetChanged();
        }
    }


    private void setChannel(){
        SharedPreferencesUtils.setParam(SharedPreferencesUtils.CHANNEL_COUNT,mCount);
    }


    private void initLanguages(){
        languages.put(1,R.id.rb_language_chi);
        languages.put(2,R.id.rb_language_eng);
        languages.put(3,R.id.rb_language_kor);
        languages.put(4,R.id.rb_language_jap);
        languages.put(5,R.id.rb_language_ger);
    }

    private void initTypeface(ViewGroup linearLayout){
        for(int i = 0; i < linearLayout.getChildCount();i++){
            View view = linearLayout.getChildAt(i);
            if(view instanceof ViewGroup){
                initTypeface((ViewGroup) view);
            }else{
                if(view instanceof TextView){
                    TextView textView = (TextView) view;
                    textView.setTypeface(TypefaceUtil.getStyleOneInstance());
                }
            }
        }
    }

    private void initListener(ViewGroup viewGroup){
        for(int i = 0; i < viewGroup.getChildCount();i++){
            View view = viewGroup.getChildAt(i);
            if(view instanceof ViewGroup){
                initListener((ViewGroup) view);
            }else{
                if(view instanceof RadioButton){
                    view.setOnClickListener(this);
                }
            }
        }
    }

    @Override
    public int getChildViewLayout() {
        return
                BowlingUtils.isPad() ? R.layout.dialog_function_set : R.layout.dialog_function_set_mobile;
    }




    @Override
    public void onClick(View v) {
        if(v instanceof  RadioButton){
            Drawable myImageSelect = getContext().getResources().getDrawable(R.drawable.shape_check_button_normal);
            myImageSelect.setBounds(0, 0, (int) (25.5 * getDestiny()), (int) (25.5 * getDestiny()));
            RadioButton radioButtonSelect = (RadioButton) findViewById(languages.get(mCurrentLanguage));
            radioButtonSelect.setCompoundDrawables(myImageSelect,null,null,null);
            RadioButton radioButton = (RadioButton) v;
            Drawable myImage = getContext().getResources().getDrawable(R.drawable.shape_check_button_selector);
            myImage.setBounds(0, 0, (int) (25.5 * getDestiny()), (int) (25.5 * getDestiny()));
            radioButton.setCompoundDrawables(myImage,null,null,null);
            switch (v.getId()){
                case R.id.rb_language_chi:
                    mCurrentLanguage = 1;
                    break;
                case R.id.rb_language_eng:
                    mCurrentLanguage = 2;
                    break;
                case R.id.rb_language_kor:
                    mCurrentLanguage = 3;
                    break;
                case R.id.rb_language_jap:
                    mCurrentLanguage = 4;
                    break;
                case R.id.rb_language_ger:
                    mCurrentLanguage = 5;
                    break;
            }
        }
    }
    private int mCurrentLanguage = 1;

    private void complete(){
        if(linearLayoutChannel != null){
            // ip修改
            String initIp = (String) SharedPreferencesUtils.getParam(CHANNEL_IP,"192.168.1.199");
            if(mEtIp != null && mEtIp.getText().toString() != null){
                if(!mEtIp.getText().toString().equals(initIp)){
                    SharedPreferencesUtils.setParam(CHANNEL_IP,mEtIp.getText().toString());
                    BowlingClient.getInstance().close();
                    BowlingClient.getInstance().connect();
                    BowlingClient.getInstance().connectRequest();
                }
            }
            // 球道修改
            String newString = buildNewChannel();
            String oldString = (String) SharedPreferencesUtils.getParam(SharedPreferencesUtils.CHANNEL_ARRAYS,"0");
            if(!newString.equals(oldString)){
                setChannel();
                SharedPreferencesUtils.setParam(SharedPreferencesUtils.CHANNEL_ARRAYS,newString);
                BowlingManager.getInstance().changeChannelListener.executeListeners(new Object());
            }
        }

        // 语言修改
        int lan = (int) SharedPreferencesUtils.getParam(SharedPreferencesUtils.LANGUAGE,1);
        boolean shouldExit =false;
        if(lan != mCurrentLanguage){
            SharedPreferencesUtils.setParam(SharedPreferencesUtils.LANGUAGE,mCurrentLanguage);
            shouldExit = true;
        }
        // host修改
        if(mCurrentEvn != mOriginalEvn){
            SharedPreferencesUtils.setParam(HOST_IP,mCurrentEvn);
            shouldExit =true;
        }
        if(shouldExit){
            ToastUtil.showText(BowlingApplication.getContext(),R.string.host_env_change);
            mRoot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityStacks.getInstance().exit();
                }
            },1000);
        }
        dismiss();
    }



    private String buildNewChannel(){
        if(mAdapter != null && mAdapter.getArrays() != null && mAdapter.getArrays().size() > 0){
            StringBuilder sb = new StringBuilder();
            final int[] ints = new int[mAdapter.getArrays().size()];
            for(int i = 0; i < ints.length;i++){
                ints[i] = Integer.valueOf(mAdapter.getArrays().get(i));
            }
            Arrays.sort(ints);
            for(int i = 0; i < ints.length;i++){
                if(i != ints.length - 1){
                    sb.append(ints[i]);
                    sb.append(",");
                }else{
                    sb.append(ints[i]);
                }
            }
            return sb.toString();
        }

        return "";
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
