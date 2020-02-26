package com.cloudysea.bean;

/**
 * @author roof 2019/9/14.
 * @email lyj@yhcs.com
 * @detail 修改球员姓名bean
 */
public class ModifyPlayderNameBean extends PublicBean {
    public ModifyPlayerNameData Data;

    @Override
    public String getName() {
        return "UpdateBowlerName";
    }

    public static class ModifyPlayerNameData {
        public String BowlerId;
        public String Name;
    }
}
