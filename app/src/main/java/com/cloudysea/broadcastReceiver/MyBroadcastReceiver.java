package com.cloudysea.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cloudysea.ui.SplashActivity;
import com.cloudysea.utils.ToastUtil;

/**
 * @author roof 2019/10/7.
 * @email lyj@yhcs.com
 * @detail
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private String TAG = "MyBroadcastReceiver";
   private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"MyBroadcastReceiver");
        if (ACTION_BOOT.equals(intent.getAction())) {
            Intent intentMainActivity = new Intent(context, SplashActivity.class);
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentMainActivity);
            ToastUtil.showText(context,"开机完毕~");
        }
    }
}
