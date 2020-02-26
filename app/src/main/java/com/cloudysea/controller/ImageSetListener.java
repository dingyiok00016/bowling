package com.cloudysea.controller;

import android.net.Uri;
import android.widget.ImageView;

import com.cloudysea.bean.ImageSetBean;

/**
 * @author roof 2019/9/14.
 * @email lyj@yhcs.com
 * @detail
 */
public abstract class ImageSetListener extends BaseListener<ImageSetBean> {

    private ImageView mImageView;
    private int mPosition;
    private String uuid;

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid(){
        return uuid;
    }

    public int getPosition(){
        return mPosition;
    }

    public ImageView getImageView(){
        return mImageView;
    }
    public void setImageView(ImageView imageView){
        mImageView = imageView;
    }


}
