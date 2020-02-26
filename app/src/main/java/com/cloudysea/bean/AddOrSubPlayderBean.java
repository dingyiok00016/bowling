package com.cloudysea.bean;

import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/9/14.
 * @email lyj@yhcs.com
 * @detail 增删球员bean
 */
@Keep
public class AddOrSubPlayderBean extends PublicBean {
    public AddOrSubPlayderData Data;

    @Override
    public String getName() {
        return "ChangeBowlers";
    }

    public static class AddOrSubPlayderData {
        public int AddCount;
        public List<String> RemovedBowlerIds = new ArrayList<>();
    }
}
