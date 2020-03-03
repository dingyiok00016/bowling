package com.cloudysea;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cloudysea.utils.FtpDownFiles;
import com.cloudysea.utils.JCifsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2020-02-14.
 * @email lyj@yhcs.com
 * @detail
 */
public class UploadShareService extends IntentService {
    public static boolean canUpload = true;

    // 下载后台的图片以及动画
    public static final int UPLOAD_IMAGE_AND_ANIMATION = 0;
    // 更新apk任务
    public static final int GET_UPDATE_APK = 1;
    public static final String EXTRA_TASK_NAME = "extra_task_name";
    public static final String EXTRA_URL = "extra_task_url";
    public static final String EXTRA_VERSION_NAME = "extra_version_name";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadShareService(String name) {
        super(name);
    }


    public UploadShareService() {
        super("pcShare");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null ){
            int taskId = intent.getIntExtra(EXTRA_TASK_NAME,-1);
            switch (taskId){
                case UPLOAD_IMAGE_AND_ANIMATION:
                    if(canUpload){
                        Log.d("JCifsUtil","线程开始启动");
                        FtpDownFiles.getInstance().uploadImageAndAnimation(JCifsUtil.STYLE_UPLOAD_ALL);
                        canUpload =false;
                    }
                    break;
                case GET_UPDATE_APK:
                    Log.d("UploadShareService","service启动");
                    String aplUrl = intent.getStringExtra(EXTRA_URL);
                    String versionName = intent.getStringExtra(EXTRA_VERSION_NAME);
                    FtpDownFiles.getInstance().updateApk(aplUrl,versionName);
                    break;
            }
        }
    }
}
