package com.cloudysea.utils;

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
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.views.BowlingUpdateApkDialog;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.cloudysea.utils.SharedPreferencesUtils.CHANNEL_IP;

/*******************************************************************************
 * 功能说明: 从ftp服务器指定目录复制文件到本地指定路径
 *
 * @author qianshu
 * @time 2012-6-25
 ******************************************************************************/
public class FtpDownFiles {

    public static final int STYLE_UPLOAD_ANIMATION = 1;
    public static final int STYLE_UPLOAD_AD = 2;
    public static final int STYLE_UPLOAD_ALL = 3;
    public static final int STYLE_UPLOAD_TEST = 4;
    public static final String BOWLING_AD = "/bowling/Ad/";
    public static final String BOWLING_ANIMATION = "/bowling/Animation/";
    private static final String USER_NAME = "BowlingApp";
    private static final String PASS_WORD = "Bowling123";
    private static final String DEFAULT_IP = "192.168.1.199";
    private static final String PATTERN = "^cloudysea_[0-9]*_.apk$";
    private static ArrayList<String> mWhiteList = new ArrayList<>();
    private static FtpDownFiles sFtpDownFiles;

    static {
        mWhiteList.add("avi");
        mWhiteList.add("jpg");
        mWhiteList.add("jepg");
        mWhiteList.add("mpg");
        mWhiteList.add("AVI");
        mWhiteList.add("apk");
    }

    private ExecutorService mAnimationExectors = Executors.newSingleThreadExecutor();
    private ExecutorService mApkExectors = Executors.newSingleThreadExecutor();
    private FTPClient ftpClient;
    private HashSet<String> mRemoteSet = new HashSet<>(50);
    private HashSet<String> mLocalSet = new HashSet<>(50);

    private FtpDownFiles() {
        this.ftpClient = new FTPClient();
    }


    public static FtpDownFiles getInstance() {
        if (sFtpDownFiles == null) {
            sFtpDownFiles = new FtpDownFiles();
        }
        return sFtpDownFiles;
    }

    public  void updateApk(String apkUrl,String apkCode){
        mApkExectors.submit(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(apkUrl)) {
                    FtpDownFiles ftpDownFiles = new FtpDownFiles();
                    try {
                        String ip = (String) SharedPreferencesUtils.getParam(CHANNEL_IP, DEFAULT_IP);
                        ftpDownFiles.connectServer(ip,USER_NAME,PASS_WORD);
                        String versionName = BuildConfig.VERSION_NAME;
                        Log.d("FtpDownFiles-apk","run");
                        //    float currentVersion = Float.parseFloat(apkCode);
                        //    float updateVersion = Float.parseFloat(versionName);
                        //    Log.d("FtpDownFiles","currentVersion=" + currentVersion + ",updateVersion=" + updateVersion);
                        //    if(updateVersion != currentVersion){
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
                        ftpDownFiles.ftpDownFiles(apkUrl, storageDir + "/bowling" + apkUrl, new OnUploadListener() {
                            @Override
                            public void onSuccess() {
                                LogcatFileManager.getInstance().writeLog("FtpDownFiles-file", "install action");
                                ftpDownFiles.install(BowlingApplication.getContext(),storageDir + "/bowling" + apkUrl);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        BowlingUpdateApkDialog.getInstance(activity).dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(String progress) {

                            }

                            @Override
                            public void onFailuer(Throwable e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        BowlingUpdateApkDialog.getInstance(activity).dismiss();
                                        Toast.makeText(BowlingApplication.getContext(), R.string.install_apk_failure,Toast.LENGTH_SHORT).show();
                                    }});
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        ftpDownFiles.closeServer();
                    }
                }
            }
        });
    }

    public  boolean install(Context con, String filePath) {
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

    public  Uri getPathUri(Context context, String filePath) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String packageName = context.getPackageName();
            uri = FileProvider.getUriForFile(context, packageName + ".fileProvider", new File(filePath));
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        return uri;
    }


    /**
     * 功能说明：根据ftp文件30秒刷新一次本地文件
     */
    public void uploadImageAndAnimation(int style) {

        mAnimationExectors.submit(new Runnable() {
            @Override
            public void run() {
                String ip = (String) SharedPreferencesUtils.getParam(CHANNEL_IP, DEFAULT_IP);
                String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();//pc地址
                FtpDownFiles ftpDownFiles = new FtpDownFiles();
                try{
                    ftpDownFiles.connectServer(ip, USER_NAME, PASS_WORD);

                    /** 下载过程 **/
                    if (style == STYLE_UPLOAD_AD || style == STYLE_UPLOAD_ALL) {
                        // 广告获取图片
                        Log.d("FtpDownFiles", "start ad下载");
                        // 下载过程
                        ftpDownFiles.ftpDownFiles("/Ad", storageDir + BOWLING_AD);

                    }
                    if (style == STYLE_UPLOAD_ANIMATION || style == STYLE_UPLOAD_ALL) {
                        // 获取动画
                        Log.d("FtpDownFiles", "start animation下载");
                        LogcatFileManager.getInstance().writeLog("FtpDownFiles", "start animation下载");
                        ftpDownFiles.ftpDownFiles("/Animation", storageDir + BOWLING_ANIMATION);
                    }
                    /** 本地去多过程 **/
                    if (style == STYLE_UPLOAD_AD || style == STYLE_UPLOAD_ALL) {
                        Log.d("FtpDownFiles-ad", "ad");
                        ftpDownFiles.compareAndDelete("/Ad", storageDir + BOWLING_AD, mRemoteSet, mLocalSet);

                    }
                    if (style == STYLE_UPLOAD_ANIMATION || style == STYLE_UPLOAD_ALL) {
                        Log.d("FtpDownFiles-animation", "animation");
                        ftpDownFiles.compareAndDelete("/Animation", storageDir + BOWLING_ANIMATION, mRemoteSet, mLocalSet);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    ftpDownFiles.closeServer();
                }
            }
        });

    }

    /**
     * 功能说明：通过递归实现ftp目录文件与本地文件同步更新
     *
     * @param ftpfilepath 当前ftp目录
     * @param localpath   当前本地目录
     */
    public void ftpDownFiles(String ftpfilepath, String localpath) {

        try {
            FTPFile[] ff = ftpClient.listFiles(ftpfilepath);
            // 得到当前ftp目录下的文件列表

            if (ff != null) {
                for (int i = 0; i < ff.length; i++) {
                    String localfilepath = localpath + ff[i].getName();
                    File localFile = new File(localfilepath);
                    // 根据ftp文件生成相应本地文件
                    Date fflastModifiedDate = ff[i].getTimestamp().getTime();
                    // 获取ftp文件最后修改时间
                    Date localLastModifiedDate = new Date(localFile
                            .lastModified());
                    // 获取本地文件的最后修改时间
                    int result = localLastModifiedDate
                            .compareTo(fflastModifiedDate);
                    // result=0，两文件最后修改时间相同；result<0，本地文件的最后修改时间早于ftp文件最后修改时间；result>0，则相反
                    int size = ftpfilepath.lastIndexOf("/");
                    LogcatFileManager.getInstance().writeLog("FtpDownFiles", "child=" + ftpfilepath);
                    if (ff[i].isDirectory()) {
                        // 如果是目录
                        boolean mktrue = localFile.mkdirs();
                        // 如果本地文件夹不存在就创建
                        String ftpfp = ftpfilepath + "/" + ff[i].getName() + "/";
                        // 转到ftp文件夹目录下
                        String localfp = localfilepath + "/";
                        // 转到本地文件夹目录下
                        this.ftpDownFiles(ftpfp, localfp);
                    }

                    if (ff[i].isFile()) {
                        String suffix = ff[i].getName().substring(ff[i].getName().lastIndexOf(".") + 1);
                        if (!mWhiteList.contains(suffix)) {
                            return;
                        }
                        // 如果是文件
                        File lFile = new File(localpath);
                        lFile.mkdir();
                        // 如果文件所在的文件夹不存在就创建
                        if (!lFile.exists()) {
                            return;
                        }
                        LogcatFileManager.getInstance().writeLog("FtpDownFiles-file", "child=" + lFile.getAbsolutePath());
                        if (ff[i].getSize() != localFile.length() || result < 0) {
                            // 如果ftp文件和本地文件大小不一样或者本地文件不存在或者ftp文件有更新，就进行创建、覆盖
                            String filepath = ftpfilepath + ff[i].getName();
                            // 目标ftp文件下载路径
                            FileOutputStream fos = new FileOutputStream(
                                    localFile);
                            boolean boo;
                            try {
                                boo = ftpClient.retrieveFile(new String(
                                        filepath.getBytes("UTF-8"),
                                        "ISO-8859-1"), fos);
                                // 从FTP服务器上取回一个文件
                            } catch (Exception e) {
                                boo = false;
                                e.printStackTrace();
                            }

                            if (boo == true) {
                                String name = ff[i].getName();
                                String dir = localpath;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time = sdf.format(localFile.lastModified());
                            } else {

                            }

                            fos.flush();
                            // 将缓冲区中的数据全部写出
                            fos.close();
                            // 关闭流
                        } else {
                            //	System.out.println("两个文件相同！");
                        }
                    }

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }
    public interface  OnUploadListener{
        void onSuccess();
        void onProgress(String progress);
        void onFailuer(Throwable e);
    }


    public void ftpDownFiles(String ftpfilepath, String localpath,OnUploadListener listener) {

        try {
            FTPFile[] ff = ftpClient.listFiles(ftpfilepath);
            // 得到当前ftp目录下的文件列表
            if(ff != null && ff.length != 0){
                LogcatFileManager.getInstance().writeLog("FtpDownFiles-file", "ftpfilepath=" + ftpfilepath + ",localpath=" + localpath);
                LogcatFileManager.getInstance().writeLog("FtpDownFiles-file", "ff[0]_path=" + ff[0].getName() + ",ff[0]_isFile=" + ff[0].isFile());
                if (ff[0].isFile()) {
                    File localFile = new File(localpath);
                    LogcatFileManager.getInstance().writeLog("FtpDownFiles-file", "maybe_createNew");
                    if((localFile.exists() && localFile.isDirectory()) || !localFile.exists()){
                        localFile.delete();
                        boolean mkDir = localFile.getParentFile().mkdirs();
                        LogcatFileManager.getInstance().writeLog("FtpDownFiles-file", "mkDir=" + mkDir);
                    }
                    // 目标ftp文件下载路径
                    FileOutputStream fos = new FileOutputStream(
                            localFile);
                    boolean boo;
                    try {
                        boo = ftpClient.retrieveFile(new String(
                                ftpfilepath.getBytes("UTF-8"),
                                "ISO-8859-1"), fos);
                        LogcatFileManager.getInstance().writeLog("FtpDownFiles-file", "retrieveFile");
                        // 从FTP服务器上取回一个文件
                    } catch (Exception e) {
                        boo = false;
                        e.printStackTrace();
                        if(listener != null){
                            listener.onFailuer(e);
                        }
                    }
                    fos.flush();
                    // 将缓冲区中的数据全部写出
                    fos.close();
                    // 关闭流
                    if(listener != null){
                        listener.onSuccess();
                    }
                }

            }
        }catch (Exception e){
            if(listener != null){
                listener.onFailuer(e);
            }
        }



    }

    /**
     * 功能说明：连接ftp服务器
     *
     * @param hostip   服务器地址
     * @param username 用户名
     * @param password 密码
     */
    public void connectServer(String hostip, String username, String password) {

        try {
            ftpClient.connect(hostip, 21);
            // 连接服务器
            ftpClient.login(username, password);
            // 登录
            // 检测是否连接成功
            int reply = ftpClient.getReplyCode();
            // 看返回的值是不是230，如果是，表示登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                // 返回的code>=200&&code<300return
                ftpClient.disconnect();
                // 关闭FTP连接
            }
            ftpClient.setControlEncoding("UTF-8");
            // 设置字符编码
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置文件传输格式

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

    public void closeServer(){
        try {
            if(ftpClient.isConnected()){
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void compareAndDelete(String ftpfilepath, String localFilePath, HashSet<String> remote, HashSet<String> local) {
        Log.d("FtpDownFiles-ad", "compare_set");
        remote.clear();
        local.clear();
        addAllRemotePath(ftpfilepath, remote);
        Log.d("FtpDownFiles-ad", "all_local");
        addAllLocalPath(localFilePath, local);
        for (String string : local) {
            String tagetString = ftpfilepath + string.substring(localFilePath.length());
            Log.d("FtpDownFiles-target", tagetString);
            if (!remote.contains(tagetString)) {
                File file = new File(string);
                if(file.exists()){
                    file.delete();
                }
            }
        }
    }

    private void addAllLocalPath(String localFilePath, HashSet<String> list) {
        File file = new File(localFilePath);
        File[] ff = file.listFiles();
        if (ff == null) {
            return;
        }
        for (int i = 0; i < ff.length; i++) {
            if (ff[i].isDirectory()) {
                String ftpfp = localFilePath + "/" + ff[i].getName() + "/";
                list.add(ftpfp);
                addAllLocalPath(ftpfp, list);

            } else {
                list.add(localFilePath + ff[i].getName());
                Log.d("FtpDownFiles-ad", localFilePath + ff[i].getName());
            }
        }
    }

    private void addAllRemotePath(String ftpFilePath, HashSet<String> list) {
        try {
            FTPFile[] ff = ftpClient.listFiles(ftpFilePath);
            if (ff != null) {
                for (int i = 0; i < ff.length; i++) {
                    if (ff[i].isDirectory()) {
                        String ftpfp = ftpFilePath + "/" + ff[i].getName() + "/";
                        list.add(ftpfp);
                        Log.d("FtpDownFiles-ad", ftpfp);
                        addAllRemotePath(ftpfp, list);
                    } else {
                        list.add(ftpFilePath + ff[i].getName());
                        Log.d("FtpDownFiles-ad", ftpFilePath + ff[i].getName());
                    }
                }
            }
        } catch (IOException e) {
            Log.d("FtpDownFiles-ad", "exception" + e.getMessage());
            e.printStackTrace();
        }
    }


}
