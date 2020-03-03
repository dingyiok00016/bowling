package com.cloudysea.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.bean.CurrentBowlerInfo;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.controller.ActivityStacks;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.cloudysea.utils.SharedPreferencesUtils.LANGUAGE;

/**
 * @author roof 2019/9/24.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingUtils {


    // 当前的球道连接
    public static int Global_LANE_NUMBER = 1;
    public static float Glbal_SIZE_RADIO = 1F;
    public static float Gobal_SIZE_SCORE_RADIO = 1F;
    public static float Global_SIZE_BOTTOM = 1F;
    public static boolean HAS_MATCH;
    // 当前球员id
    public static String CURRENT_BOWLER_ID;
    public static CurrentBowlerInfo.DataBean[] CURRENT_EXCHANGE;
    public static int Global_CURRENT_REMOTE_MATCH;
    public static String CURRENT_TURN_ID = "";
    public static int Global_ME_VIP_POSITION;
    public static String LAST_SUB_ID = "";
    private static final String TAG = "BowlingUtils";
    public static HashMap<String,String> mHeads = new HashMap<>();
    public static HashMap<String,String> mRemoteHeads = new HashMap<>();
    public static HashMap<String,String> mSources = new HashMap<>();
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void setImageBitmap(PlayerBean item,ImageView ivAvatar){
        if(item.HeadPortrait != null){
            Bitmap bitmap = BowlingUtils.stringToBitmap((String) item.HeadPortrait);
            ivAvatar.setImageBitmap(CropSquareTransformation.getRoundedCornerBitmap(bitmap, (int) (DeviceUtils.getDestiny() * 7.5)));
        }else{
            if(BowlingUtils.getHead(item.Id) != null){
                Bitmap bitmap = BowlingUtils.stringToBitmap(BowlingUtils.getHead(item.Id));
                ivAvatar.setImageBitmap(CropSquareTransformation.getRoundedCornerBitmap(bitmap, (int) (DeviceUtils.getDestiny() * 7.5)));
            }else{
                try{
                    if(item.resourcePath != null){
                        String reourcePath = item.resourcePath;
                        ivAvatar.setImageResource(BowlingApplication.getContext().getResources().getIdentifier(reourcePath,"drawable",BowlingApplication.getContext().getPackageName()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static String sCurrentUUID;

    public static MainActivity getMainActivity() {
        Activity activity = ActivityStacks.getInstance().getTop();
        if (activity != null && !activity.isFinishing() && activity instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity) activity;
            return mainActivity;
        }
        return null;
    }

    public static Locale getLanuage(){
        int language = (int) SharedPreferencesUtils.getParam(LANGUAGE, 1);
        Locale lang = Locale.SIMPLIFIED_CHINESE;
        if (language == 1) {
            lang = Locale.SIMPLIFIED_CHINESE;
        } else if (language == 2) {
            lang = Locale.ENGLISH;
        } else if (language == 3) {
            lang = Locale.KOREAN;
        }
        return lang;
    }

    public static Context initAppLanguage(Context context, String language) {
        if (currentLanguageIsSimpleChinese(context)) {
            Log.e("LanguageUtil", "当前是简体中文");
            return context;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0及以上的方法
            Log.e("LanguageUtil", "7.0及以上");
            return createConfiguration(context, language);
        } else {
            Log.e("LanguageUtil", "7.0以下");
            updateConfiguration(context, language);
            return context;
        }
    }

    /**
     * 7.0及以上的修改app语言的方法
     *
     * @param context  context
     * @param language language
     * @return context
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Context createConfiguration(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = new Locale(language);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        return context.createConfigurationContext(configuration);
    }

    /**
     * 7.0以下的修改app语言的方法
     *
     * @param context  context
     * @param language language
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void updateConfiguration(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, displayMetrics);
    }

    /**
     * 判断当前的语言是否是简体中文
     *
     * @param context context
     * @return boolean
     */
    public static boolean currentLanguageIsSimpleChinese(Context context) {
        String language;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            language = context.getResources().getConfiguration().locale.getLanguage();
        }
        return TextUtils.equals("zh", language);
    }

    public static SpannableString getSpannableString(int size,int resId){
        SpannableString ss = new SpannableString(BowlingApplication.getContext().getResources().getString(resId));//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size,true);//设置字体大小 true表示单位是
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static Activity getTopActivity(){
        Activity activity = ActivityStacks.getInstance().getTop();
        if (activity != null && !activity.isFinishing()) {
            return activity;
        }
        return null;
    }

    public static void showImage(final ImageView ivPhoto, String imagePath, final PlayerBean playerBean) {
        Picasso.with(BowlingApplication.getContext()).
                load(new File(imagePath)).resize(200,200).centerCrop().
                into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        String datas = bitmapToString(bitmap);
                        playerBean.hasChange = true;
                        playerBean.HeadPortrait = datas;
                        ivPhoto.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        Log.i(TAG, "showImage: " + imagePath);
    }

    public static void showImage(final ImageView ivPhoto, String imagePath, final PlayerBean playerBean, final View.OnClickListener listener) {
        Picasso.with(BowlingApplication.getContext()).
                load(new File(imagePath)).resize(200,200).centerCrop().
                into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        String datas = bitmapToString(bitmap);
                        playerBean.hasChange = true;
                        playerBean.HeadPortrait = datas;
                        ivPhoto.setImageBitmap(bitmap);
                        if(listener != null){
                            listener.onClick(null);
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        Log.i(TAG, "showImage: " + imagePath);
    }

    public static boolean isAndroidQ(){
        return Build.VERSION.SDK_INT >= 29;
    }

    public static void showImage(final ImageView ivPhoto, String imagePath, final PlayerBean playerBean, final View.OnClickListener listener,final int radius) {
        Picasso.with(BowlingApplication.getContext()).
                load(new File(imagePath)).resize(200,200).centerCrop().
                into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        String datas = bitmapToString(bitmap);
                        playerBean.hasChange = true;
                        playerBean.HeadPortrait = datas;
                        ivPhoto.setImageBitmap(CropSquareTransformation.getRoundedCornerBitmap(bitmap,radius));
                        if(listener != null){
                            listener.onClick(null);
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        Log.i(TAG, "showImage: " + imagePath);
    }


    public void saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory() + " " + System.currentTimeMillis() + ".jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public static Bitmap createQRCode(@Nullable String content,int widthAndHeight){
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();//定义二维码参数
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置字符
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q); // 容错级别设置 L,H,M,Q
    //    hints.put(EncodeHintType.MARGIN, "4"); // 空白边距设置
        try {
//    生成二维码
            BitMatrix matrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight,hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = Color.WHITE;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 图片转换成base64字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgBytes, Base64.NO_WRAP);
    }

    public static char[] getRealScore(int score){
        String scores = Integer.toBinaryString(score);
        StringBuilder sb = new StringBuilder(scores);
        for(int i = scores.length(); i < 13;i++){
            sb.insert(0,'0');
        }
        char[] datas = sb.toString().toCharArray();
        return datas;
    }

    public static boolean isPad() {
        return (BowlingApplication.getContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static HashMap<String,PlayerBean> maps = new HashMap<>();

    public static List<String> lists = new ArrayList<>();

    public static void addPwdEntry(String string,PlayerBean playerBean){
        maps.put(string,playerBean);
    }

    public static void removePwdEntry(String string){
        maps.remove(string);
    }
    public static PlayerBean getPlayBeanByString(String string){
        return maps.get(string);
    }

    public static String getHead(String id){
        return mHeads.get(id);
    }

    public static void addRemoteHead(String id,String head){
        mRemoteHeads.put(id,head);
    }

    public static String getRemoteHead(String id){
        return mRemoteHeads.get(id);
    }

    public static void addHead(String id,String head){
        mHeads.put(id,head);
    }

    public static String getResource(String id){
        return mSources.get(id);
    }

    public static void addResource(String id,String head){
        mSources.put(id,head);
    }
}
