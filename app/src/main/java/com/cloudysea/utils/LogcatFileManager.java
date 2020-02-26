package com.cloudysea.utils;

/**
 * @author roof 2019/11/16.
 * @email lyj@yhcs.com
 * @detail
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;


public class LogcatFileManager
{
    private static LogcatFileManager INSTANCE = null;
    private static String PATH_LOGCAT;
    private LogDumper mLogDumper = null;
    private int mPId;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static class WorkThread extends HandlerThread {
        public WorkThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            super.run();
            //执行业务操作
        }

        @Override
        protected void onLooperPrepared() {
            //按需选择是否覆写，在loop之前想做的操作
        }
    }



    public static LogcatFileManager getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new LogcatFileManager();
        }
        return INSTANCE;
    }


    private LogcatFileManager()
    {
        mPId = android.os.Process.myPid();
        mHandlerThread = new WorkThread("Logcat");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(mLogDumper != null){
                    String line = (String) msg.obj;
                    Log.d(getClass().getSimpleName(),"line:" + line);
                    mLogDumper.writeLog(line);
                }
            }
        };
    }


    public void startLogcatManager(Context context)
    {
        String folderPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MMF-Logcat";
        }
        else
        {
            folderPath = context.getFilesDir().getAbsolutePath() + File.separator + "MMF-Logcat";
        }
        LogcatFileManager.getInstance().start(folderPath);
    }


    public void stopLogcatManager()
    {
        LogcatFileManager.getInstance().stop();
    }


    private boolean setFolderPath(String folderPath)
    {
        File folder = new File(folderPath);
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        if (!folder.isDirectory())
        {
            return  false;
        //    throw new IllegalArgumentException("The logcat folder path is not a directory: " + folderPath);
        }


        PATH_LOGCAT = folderPath.endsWith("/") ? folderPath : folderPath + "/";
        Log.d(getClass().getSimpleName(),PATH_LOGCAT);
        return true;
    }


    public void start(String saveDirectoy)
    {
        if(setFolderPath(saveDirectoy)){
            if (mLogDumper == null)
            {
                mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT);
            }
        }
    }

    public void writeLog(String tag,String line){
        String result = simpleDateFormat2.format(new Date()) + "  " + tag + ": "  + line + "\n";
        Message message = Message.obtain();
        message.obj = result;
        mHandler.sendMessage(message);
    }


    public void stop()
    {
        if (mLogDumper != null)
        {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }


    private class LogDumper
    {
        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String cmds = null;
        private String mPID;
        private FileOutputStream out = null;

        public void writeLog(String line){
            if (out != null)
            {
                Log.d("LogcatFileManager",line);
                try {
                    out.write(line.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        public LogDumper(String pid, String dir)
        {
            mPID = pid;
            try
            {
                out = new FileOutputStream(new File(dir, "logcat-" + simpleDateFormat1.format(new Date()) + ".txt"), true);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }


            /**
             * * * log level：*:v , *:d , *:w , *:e , *:f , *:s * * Show the
             * current mPID process level of E and W log. * *
             */
            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
            cmds = "logcat -s com.cloudysea";
        }


        public void stopLogs()
        {
            mRunning = false;
        }


        /*@Override
        public void run()
        {
            try
            {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
                String line = null;
                while (mRunning && (line = mReader.readLine()) != null)
                {
                    if (!mRunning)
                    {
                        break;
                    }
                    if (line.length() == 0)
                    {
                        continue;
                    }
                    if (out != null && (line.contains("BallSocketServer") || line.contains("HubConnection") || line.contains("BowlingClient")))
                    {
                        Log.d("LogcatFileManager",line);
                        out.write((simpleDateFormat2.format(new Date()) + "  " + line + "\n").getBytes());
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (logcatProc != null)
                {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null)
                {
                    try
                    {
                        mReader.close();
                        mReader = null;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    out = null;
                }
            }
        }*/


    }

}
