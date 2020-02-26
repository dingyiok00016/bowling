package com.cloudysea.net;

/**
 * @author roof 2020-02-25.
 * @email lyj@yhcs.com
 * @detail
 */

import android.util.Log;

import com.cloudysea.controller.LocalExecutor;
import com.cloudysea.utils.LogcatFileManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/***
 * UDP Client端
 ***/
public class BowlingUdpClient {
    private static BowlingUdpClient sUpdateClient;
    private static final int PORT = 9877;
    private DatagramSocket client;

    public static BowlingUdpClient getInstance(){
        if(sUpdateClient == null){
            sUpdateClient = new BowlingUdpClient();
        }
        return sUpdateClient;
    }

    public void connect() {
        Log.d("BowlingUdpClient","connect:" );
        LocalExecutor.getInstance().addWork(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //创建客户端的DatagramSocket对象，不必传入地址和对象
                        client = new DatagramSocket(PORT);
                        byte[] responseBytes = new byte[1024];
                        Log.d("BowlingUdpClient","start:" );

                        //创建响应信息的DatagramPacket对象
                        DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length);
                        try {
                            Log.d("BowlingUdpClient","阻塞:" );
                            //等待响应信息，同服务端一样，客户端也会在这一步阻塞，直到收到一个数据包
                            client.receive(responsePacket);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //解析数据包内容
                        String responseMsg = new String(responsePacket.getData(), 0, responsePacket.getLength());
                        Log.d("BowlingUdpClient","responseMsg:"  + responseMsg);
                        LogcatFileManager.getInstance().writeLog("BowlingUdpClient",responseMsg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //关闭客户端
                        if (client != null) {
                            client.close();
                            client = null;
                        }
                    }
                }
            }
        });

    }
}
