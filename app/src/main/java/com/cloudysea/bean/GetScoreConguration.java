package com.cloudysea.bean;

/**
 * @author roof 2020-02-16.
 * @email lyj@yhcs.com
 * @detail
 */
public class GetScoreConguration extends PublicBean {

    public Data DataBean;

    public static class Data{
        public boolean EnabledAd; // 是否允许用广告
        public boolean EnableAnimation; // 是否允许用动画
        public boolean EnabledLargePinState; // 是否允许用相位图
        public boolean UseCloudAdAndAnimation; // 是否允许用广告
    }
}
