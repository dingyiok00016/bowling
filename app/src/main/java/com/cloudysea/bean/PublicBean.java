package com.cloudysea.bean;

import java.io.Serializable;
import java.net.PortUnreachableException;

/**
 * @author roof 2019/9/14.
 * @email lyj@yhcs.com
 * @detail 公有bean 也可以认为是基bean
 */
public class PublicBean implements Serializable {
    public String Id;
    public String Name;
    public String Type = "0";
    public String LaneNumber;
    public String AuthCode;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
