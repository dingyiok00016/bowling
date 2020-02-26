package com.cloudysea.bean;

/**
 * @author roof 2019/9/14.
 * @email lyj@yhcs.com
 * @detail 修改球员头像bean
 */
public class ModifyPlayderBean extends PublicBean {
    public ModifyPlayderBean BowlerInfos;
    public ModifyPlayderBean Data;

    @Override
    public String getName() {
        return "UpdateBowlerInfo";
    }

    public static class ModifyPlayerAvatarData {
        public String BowlerId;
        public String HeadPortrait;
        public String Name;
    }
}
