package com.cloudysea.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.cloudysea.utils.SSlUtiles;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author roof 2019/11/24.
 * @email lyj@yhcs.com
 * @detail
 */
public class CustomerVideoView extends VideoView {
    public CustomerVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setVideoURI(Uri uri) {
        super.setVideoURI(uri);
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(SSlUtiles.createSSLSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new SSlUtiles.TrustAllHostnameVerifier());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
