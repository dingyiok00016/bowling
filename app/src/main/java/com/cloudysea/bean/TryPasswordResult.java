package com.cloudysea.bean;

/**
 * @author roof 2019/10/3.
 * @email lyj@yhcs.com
 * @detail
 */
public class TryPasswordResult extends PublicBean {
    public Data Data;

    public static class Data{
        public boolean IsSuccessful;
        public String Action;
    }
}
