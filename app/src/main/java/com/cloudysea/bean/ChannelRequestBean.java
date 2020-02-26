package com.cloudysea.bean;

/**
 * @author roof 2019/9/22.
 * @email lyj@yhcs.com
 * @detail
 */
public class ChannelRequestBean extends PublicBean {

    @Override
    public String getName() {
        return "ConnectRequest";
    }

    public static class Data{
        public String LaneNumber;
    }
}
