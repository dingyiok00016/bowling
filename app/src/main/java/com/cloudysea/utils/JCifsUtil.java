package com.cloudysea.utils;

/**
 * @author roof 2020-02-13.
 * @email lyj@yhcs.com
 * @detail
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.BuildConfig;
import com.cloudysea.R;
import com.cloudysea.bean.SeriableBean;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.views.BowlingCommonDialog;
import com.cloudysea.views.BowlingUpdateApkDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import static com.cloudysea.utils.SharedPreferencesUtils.CHANNEL_IP;

public class JCifsUtil {

    // 广告路径
    private static final String AD_PATH = "/ManagerData/Ad/Image/";
    // 动画路径
    private static final String ANIMATION_PATH = "/ManagerData/Animation/";
    private static final String SERAIL_TXT_PATH = "/ManagerData/serializab.txt";
    private static final String APK_PATH = "/ManagerData/APK/";
    private static final String DEFAULT_IP = "192.168.1.199";
    private static final String USER_NAME = "BowlingApp";
    private static final String PASS_WORD = "Bowling123";
    private static final String PATTERN = "^cloudysea_[0-9]*_.apk$";
    public static final int STYLE_UPLOAD_ANIMATION = 1;
    public static final int STYLE_UPLOAD_AD = 2;
    public static final int STYLE_UPLOAD_ALL = 3;
    public static final int STYLE_UPLOAD_TEST = 4;
    private static ExecutorService mExectors = new ThreadPoolExecutor(4, 4,
            0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(512), // 使用有界队列，避免OOM
            new ThreadPoolExecutor.DiscardPolicy());

    private static HashMap<String,Long> mStrings;
    private static ArrayList<String> mWhiteList = new ArrayList<>();
    static {
        mWhiteList.add("avi");
        mWhiteList.add("jpg");
        mWhiteList.add("jepg");
        mWhiteList.add("mpg");
        mWhiteList.add("AVI");
    }


    public static void uploadImageAndAnimation(int style) {
        mExectors.submit(new Runnable() {
            @Override
            public void run() {

                String ip = (String) SharedPreferencesUtils.getParam(CHANNEL_IP, DEFAULT_IP);
                ;//pc地址
                String username = USER_NAME;//账户密码
                String password = PASS_WORD;
                // 获取mouted目录
                String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                // 创建序列化文件
                createSerializableTxt(storageDir + SERAIL_TXT_PATH);
                if(style == STYLE_UPLOAD_AD || style == STYLE_UPLOAD_ALL){
                    // 广告获取图片
                    getRemoteFile(username, password, ip + AD_PATH, storageDir + AD_PATH);
                }
                if(style == STYLE_UPLOAD_ANIMATION || style == STYLE_UPLOAD_ALL){
                    // 获取动画
                    getRemoteFile(username, password, ip + ANIMATION_PATH, storageDir + ANIMATION_PATH);
                }
                if(style == STYLE_UPLOAD_TEST){
                    try {
                        Log.d("JCifsUtil","animation_test_start");
                        SmbFile smbFile = new SmbFile("smb://" + username + ":" + password + "@" + ip + ANIMATION_PATH + "Double/Cdouble.avi");
                        copyRemoteTestFile(smbFile, storageDir + ANIMATION_PATH + "Double/", new OnUploadListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("JCifsUtil","测试onSuccess");
                                Activity activity = ActivityStacks.getInstance().getTop();
                                if(activity != null){
                                    activity.getWindow().getDecorView().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(BowlingApplication.getContext(),"动画下载完成",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onProgress(String progress) {

                            }

                            @Override
                            public void onFailuer(Throwable e) {
                                Activity activity = ActivityStacks.getInstance().getTop();
                                if(activity != null){
                                    activity.getWindow().getDecorView().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(BowlingApplication.getContext(),"动画下载失败" + e.getMessage(),Toast.LENGTH_LONG).show();
                                            LogcatFileManager.getInstance().writeLog("JCifsUtil","动画下载失败" + e.getMessage());
                                        }
                                    });
                                }
                            }
                        });
                    } catch (MalformedURLException e) {
                        Toast.makeText(BowlingApplication.getContext(),"建立连接失败" + e.getMessage(),Toast.LENGTH_LONG).show();
                        LogcatFileManager.getInstance().writeLog("JCifsUtil","建立连接失败" + e.getMessage());
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    private static void createSerializableTxt(String path)  {
        File file = new File(path);
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void rewriteSeariliableTxt(){
        SerializableUtils<SeriableBean> serializableUtils = new SerializableUtils<SeriableBean>();
        SeriableBean seriableBean = serializableUtils.deserialize("");
        mStrings = seriableBean.hashMaps;
    }


    private static void updateStepTwo(String version1,SmbFile file){
        if(shouldUpdateApk(version1)){
            Log.d("JCifsUtil","有需要更新的apk");
            Activity activity = ActivityStacks.getInstance().getTop();
            if(activity == null){
                return;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BowlingUpdateApkDialog.getInstance(activity).show();
                }
            });
            String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            final String storageApkName = storageDir + APK_PATH + file.getName();
            Log.d("JCifsUtil","从远程获取apk中");
            copyRemoteFile(file, storageApkName,
                    new OnUploadListener() {
                        @Override
                        public void onSuccess() {
                            Log.d("JCifsUtil","获取完毕,开始安装");
                            install(BowlingApplication.getContext(),storageApkName);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BowlingUpdateApkDialog.getInstance(activity).dismiss();
                                }
                            });
                        }

                        @Override
                        public void onProgress(String progress) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BowlingUpdateApkDialog.getInstance(activity).setProgress(progress);
                                }
                            });
                        }

                        @Override
                        public void onFailuer(Throwable e) {
                            Toast.makeText(BowlingApplication.getContext(), R.string.install_apk_failure,Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Log.d("JCifsUtil","已经是最新版本的apk");
        }
    }


    public static void updateApk(String apkUrl,String apkCode){
        mExectors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if(TextUtils.isEmpty(apkUrl)){
                        Log.d("JCifsUtil","apk任务检测");
                        String ip = (String) SharedPreferencesUtils.getParam(CHANNEL_IP, DEFAULT_IP);
                        Log.d("JCifsUtil","ip=" + ip);
                        SmbFile smbFileDir = new SmbFile("smb://" + USER_NAME + ":" + PASS_WORD + "@" + ip + APK_PATH);
                        if(smbFileDir.isDirectory()){
                            for (SmbFile file : smbFileDir.listFiles()) {
                                if(file.getName().matches(PATTERN)){
                                    String fileName = file.getName();
                                    String versionName = getCodeByFile(fileName);
                                    updateStepTwo(versionName,file);
                                    break;
                                }
                            }
                        }
                    }else{
                        updateStepTwo(apkCode,new SmbFile(apkUrl));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.d("HomePageActivity", "verTag2=2222="+version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    public static boolean install(Context con, String filePath) {
        try {
            if(TextUtils.isEmpty(filePath))
                return false;
            File file = new File(filePath);
            if(!file.exists()){
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//增加读写权限
            }
            intent.setDataAndType(getPathUri(con, filePath), "application/vnd.android.package-archive");
            con.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(con, "安装失败，请重新下载", Toast.LENGTH_LONG).show();
            return false;
        } catch (Error error) {
            error.printStackTrace();
            Toast.makeText(con, "安装失败，请重新下载", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    public static Uri getPathUri(Context context, String filePath) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String packageName = context.getPackageName();
            uri = FileProvider.getUriForFile(context, packageName + ".fileProvider", new File(filePath));
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        return uri;
    }

    private static String getCodeByFile(String fileName){
        String[] strs = fileName.split(".a");
        if(strs.length > 1){
            return  strs[0];
        }
        return null;
    }

    private static boolean shouldUpdateApk(String version1){
       return  compareVersion(version1,BuildConfig.VERSION_NAME) == 1;
    }

    public interface  OnUploadListener{
        void onSuccess();
        void onProgress(String progress);
        void onFailuer(Throwable e);
    }


    private static void copyRemoteTestFile(SmbFile smbFile, String localDirectory,OnUploadListener onUploadListener) {
        Activity activity = ActivityStacks.getInstance().getTop();
        if(activity != null){
            activity.getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BowlingApplication.getContext(),"开始下载",Toast.LENGTH_SHORT).show();
                }
            });
        }
        String suffix = smbFile.getName().substring(smbFile.getName().lastIndexOf(".") + 1);
        if(!mWhiteList.contains(suffix)){
            return;
        }
        mExectors.submit(new Runnable() {
            @Override
            public void run() {
                Log.d("JCifsUtil","inputstream初始化");
                SmbFileInputStream in = null;

                FileOutputStream out = null;

                try {
                    File[] localFiles = new File(localDirectory).listFiles();
                    if (null == localFiles) {
                        new File(localDirectory).mkdirs();

                    } else {
                        // 远程文件和本地文件进行比对,大小一致的情况下,不更新
                        File localFile = new File(localDirectory + smbFile.getName());
                        if(localFile.exists()){
                            if(!localFile.getName().equalsIgnoreCase("Cdouble.avi")){
                                if(localFile.length() == smbFile.getContentLength()){
                                    if(onUploadListener != null){
                                        onUploadListener.onFailuer(null);
                                    }
                                    return;
                                }
                            }
                        }

                    }
                    in = new SmbFileInputStream(smbFile);
                    out = new FileOutputStream(localDirectory + smbFile.getName());

                    byte[] buffer = new byte[1024 * 64];

                    int len = -1;
                    int currentLen = 0;
                    String progress = "0.0";
                    DecimalFormat fnum = new DecimalFormat("##0.0");
                    Float length = Float.valueOf(smbFile.getContentLength());

                    while ((len = in.read(buffer)) != -1) {
                        currentLen += len;
                        out.write(buffer, 0, len);
                        if(length != 0){
                            Log.d("JCifsUtil", "当前进度：" + currentLen   + "/" + length);
                            final int logLength = currentLen;
                            activity.getWindow().getDecorView().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BowlingApplication.getContext(),"当前进度：" + logLength   + "/" + length,Toast.LENGTH_LONG).show();
                                }
                            });
                            LogcatFileManager.getInstance().writeLog("JCifsUtil","当前进度：" + currentLen   + "/" + length);
                            float per = currentLen / length * 100;

                            if(onUploadListener != null){
                                String newProgress = fnum.format(per);
                                Log.d("JCifsUtil", "当前进度：" + per);
                                if(!newProgress.equals(progress)){
                                    progress = newProgress;
                                    onUploadListener.onProgress(progress);
                                }
                            }
                        }

                    }
                    if(onUploadListener != null){
                        onUploadListener.onSuccess();
                    }

                } catch (Exception e) {
                    if(onUploadListener != null){
                        onUploadListener.onFailuer(e);
                    }
                    e.printStackTrace();
                } finally {

                    if (null != out) {

                        try {

                            out.close();

                        } catch (IOException e) {

                            e.printStackTrace();

                        }

                    }

                    if (null != in) {

                        try {

                            in.close();

                        } catch (IOException e) {

                            e.printStackTrace();

                        }

                    }

                }
            }
        });
    }


    /**
     * 拷贝远程文件到本地目录
     *
     * @param smbFile        远程SmbFile
     * @param localDirectory 本地存储目录,本地目录不存在时会自动创建,本地目录存在时可自行选择是否清空该目录下的文件,默认为不清空
     * @return boolean 是否拷贝成功
     */

    private static void copyRemoteFile(SmbFile smbFile, String localDirectory,OnUploadListener onUploadListener) {
        String suffix = smbFile.getName().substring(smbFile.getName().lastIndexOf(".") + 1);
        if(!mWhiteList.contains(suffix)){
            return;
        }
        mExectors.submit(new Runnable() {
            @Override
            public void run() {
                Log.d("JCifsUtil","inputstream初始化");
                SmbFileInputStream in = null;

                FileOutputStream out = null;

                try {
                    File[] localFiles = new File(localDirectory).listFiles();
                    if (null == localFiles) {
                        new File(localDirectory).mkdirs();

                    } else {
                        // 远程文件和本地文件进行比对,大小一致的情况下,不更新
                        File localFile = new File(localDirectory + smbFile.getName());
                        if(localFile.exists()){
                            if(localFile.length() == smbFile.getContentLength()){
                                if(onUploadListener != null){
                                    onUploadListener.onFailuer(null);
                                }
                                return;
                            }
                        }

                    }
                    in = new SmbFileInputStream(smbFile);
                    out = new FileOutputStream(localDirectory + smbFile.getName());

                    byte[] buffer = new byte[1024 * 64];

                    int len = -1;
                    int currentLen = 0;
                    String progress = "0.0";
                    DecimalFormat fnum = new DecimalFormat("##0.0");
                    Float length = Float.valueOf(smbFile.getContentLength());

                    while ((len = in.read(buffer)) != -1) {
                        currentLen += len;
                        out.write(buffer, 0, len);
                        if(length != 0){
                            Log.d("JCifsUtil", "当前进度：" + currentLen   + "/" + length);
                            float per = currentLen / length * 100;

                            if(onUploadListener != null){
                                String newProgress = fnum.format(per);
                                Log.d("JCifsUtil", "当前进度：" + per);
                                if(!newProgress.equals(progress)){
                                    progress = newProgress;
                                    onUploadListener.onProgress(progress);
                                }
                            }
                        }

                    }
                    if(onUploadListener != null){
                        onUploadListener.onSuccess();
                    }

                } catch (Exception e) {
                    if(onUploadListener != null){
                        Toast.makeText(BowlingApplication.getContext(),"动画下载失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        onUploadListener.onFailuer(e);
                    }
                    e.printStackTrace();
                } finally {

                    if (null != out) {

                        try {

                            out.close();

                        } catch (IOException e) {

                            e.printStackTrace();

                        }

                    }

                    if (null != in) {

                        try {

                            in.close();

                        } catch (IOException e) {

                            e.printStackTrace();

                        }

                    }

                }
            }
        });
    }


    /**
     * 获取远程文件
     *
     * @param remoteUsername 远程目录访问用户名
     * @param remotePassword 远程目录访问密码
     * @param remoteFilepath 远程文件地址,该参数需以IP打头,如'192.168.8.2/aa/bb.java'或者'192.168.8.2/aa/',如'192.168.8.2/aa'是不对的
     * @param localDirectory 本地存储目录,该参数需以'/'结尾,如'D:/'或者'D:/mylocal/'
     * @return boolean 是否获取成功
     */

    public static boolean getRemoteFile(String remoteUsername, String remotePassword, String remoteFilepath, String localDirectory) {

        boolean isSuccess = false;

        if (remoteFilepath.startsWith("/") || remoteFilepath.startsWith("\\")) {

            return isSuccess;

        }

        if (!(localDirectory.endsWith("/") || localDirectory.endsWith("\\"))) {
            return isSuccess;
        }
        try {

            SmbFile smbFile = new SmbFile("smb://" + remoteUsername + ":" + remotePassword + "@" + remoteFilepath);

            if (smbFile.isDirectory()) {

                for (SmbFile file : smbFile.listFiles()) {
                    if (file.isDirectory()) {
                        if (remoteFilepath.endsWith("/")) {
                            remoteFilepath = remoteFilepath.substring(0, remoteFilepath.length() - 1);
                        }
                        if (localDirectory.endsWith("/")) {
                            localDirectory = localDirectory.substring(0, localDirectory.length() - 1);
                        }
                        Log.d("JCifsUtil", "CifsUtil下载文件--" + localDirectory);
                        isSuccess = getRemoteFile(remoteUsername, remotePassword, remoteFilepath + "/" + file.getName(), localDirectory + "/" + file.getName());
                    } else {
                        Log.d("JCifsUtil下载文件--", localDirectory);
                        copyRemoteFile(file, localDirectory,null);
                    }

                }

            } else if (smbFile.isFile()) {
                Log.d("JCifsUtil下载文件--", localDirectory);
                copyRemoteFile(smbFile, localDirectory,null);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return isSuccess;

    }

}
