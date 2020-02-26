package com.cloudysea.bean;

/**
 * @author roof 2019/10/26.
 * @email lyj@yhcs.com
 * @detail
 */
public class CreateAndJoinGameResult extends PublicBean {
    public Data Data;
    public static class Data{
        public boolean IsSuccessful;
        public String OnlineGameId;
        public int LaneNumber;
        public String ErrorMessage;
    }
}
