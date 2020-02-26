package com.cloudysea.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.LocalExecutor;
import com.cloudysea.utils.BowlingUtils;

import java.lang.ref.WeakReference;

/**
 * @author roof 2019/10/18.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingQRcodeScanDialog extends BowlingCommonDialog {

    private TextView mTvQrcodeScanBack;
    private TextView mTvQrcodeName;
    public  ImageView  mIvQrcode;
    private static QRcodeScanHandler sHandler;
    private  QrcodeRunnable mRunnable;

    class QRcodeScanHandler extends Handler {
        private WeakReference<BowlingQRcodeScanDialog> mReference;
        public QRcodeScanHandler(WeakReference<BowlingQRcodeScanDialog> weakReference){
            mReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            BowlingQRcodeScanDialog dialog = mReference.get();
            if(dialog != null && dialog.isShowing()){
                Bitmap bitmap = (Bitmap) msg.obj;
                if(bitmap != null){
                    dialog.mIvQrcode.setImageBitmap(bitmap);
                }
            }
        }
    }


    public BowlingQRcodeScanDialog(@NonNull Context context) {
        super(context);
        mRunnable = new QrcodeRunnable();
        sHandler = new QRcodeScanHandler(new WeakReference<BowlingQRcodeScanDialog>(this));
    }

    public BowlingQRcodeScanDialog setName(String name){
        mTvQrcodeName.setText(name);
        return this;
    }

    public BowlingQRcodeScanDialog setQrcode(String httpUrl){
        mRunnable.setContent(httpUrl);
        LocalExecutor.getInstance().removeWork(mRunnable);
        LocalExecutor.getInstance().addWork(mRunnable);
        return this;
    }

    public class QrcodeRunnable implements Runnable {
        private String mContent;
        public void setContent(String content){
            mContent = content;
        }

        @Override
        public void run() {
            Bitmap bitmap = BowlingUtils.createQRCode(mContent, (int) (150 * DeviceUtils.getDestiny()));
            Message message = Message.obtain();
            message.obj = bitmap;
            if(sHandler != null){
                sHandler.sendMessage(message);
            }
        }
    }

    @Override
    protected int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        mTvQrcodeScanBack = (TextView) findViewById(R.id.tv_qrcode_scan_back);
        mTvQrcodeScanBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvQrcodeName = (TextView) findViewById(R.id.tv_qrcode_scan_name);
        mIvQrcode = (ImageView) findViewById(R.id.iv_qrcode_scan_icon);
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_qrcode_scan;
    }
}
