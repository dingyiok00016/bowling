package com.cloudysea.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;

/**
 * @author roof 2019/11/29.
 * @email lyj@yhcs.com
 * @detail
 */
public class ToastUtil extends Toast {
    private static ToastUtil toast;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastUtil(Context context) {
        super(context);
    }

    /**
     * 显示一个纯文本吐司
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    public static void showText(Context context, CharSequence text) {
        showToast(context, text);
    }

    public static void showText(Context context, int resId) {
        showToast(context, BowlingApplication.getContext().getResources().getString(resId));
    }

    /**
     * 显示Toast
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    private static void showToast(Context context, CharSequence text) {
        // 初始化一个新的Toast对象
        initToast(context, text);
        // 设置显示时长
        toast.setDuration(Toast.LENGTH_SHORT);
        // 显示Toast
        toast.show();
    }

    /**
     * 初始化Toast
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    private static void initToast(Context context, CharSequence text) {
        try {
            cancelToast();

            toast = new ToastUtil(context);

            // 获取LayoutInflater对象
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 由layout文件创建一个View对象
            // inflater.inflate(R.layout.layout,null);
            View layout = inflater.inflate(R.layout.layout, null);
            layout.getBackground().setAlpha(180);
            // 吐司上的文字
            TextView toast_text = (TextView) layout.findViewById(R.id.message);
            toast_text.setTextSize(20 * BowlingUtils.Glbal_SIZE_RADIO);
            toast_text.setText(text);
            toast.setView(layout);
            //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移180个单位，
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 180);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 显示toast
     */
    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏当前Toast
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
    /**
     * 当前Toast消失
     */
    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {

        }
    }

    /**
     * 显示一个富文本吐司
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    public static void showSpannableText(Context context, CharSequence text) {
        showToast(context, text);
    }

}
