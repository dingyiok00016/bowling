package com.cloudysea.bean;

import com.cloudysea.utils.PermissionUtils;

/**
 * @author roof 2019/10/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class HasOnlineGameResult extends PublicBean {
    public Data Data;
    public static class Data{
        public boolean HasOnlineGame;
        public String OnlineGameId;
        public int LaneNumber;
    }
}
