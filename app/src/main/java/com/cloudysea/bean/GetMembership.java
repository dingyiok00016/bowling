package com.cloudysea.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author roof 2019/11/10.
 * @email lyj@yhcs.com
 * @detail
 */
public class GetMembership extends PublicBean {

    /**
     * Data : {"HasMemberhsip":false,"Name":"","HeadPortrait":null}
     */

    private DataBean Data;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * HasMemberhsip : false
         * Name :
         * HeadPortrait : null
         */

        private boolean HasMemberhsip;
        @SerializedName("Name")
        private String NameX;
        private Object HeadPortrait;

        public String getMembershipNumber() {
            return MembershipNumber;
        }

        public void setMembershipNumber(String membershipNumber) {
            MembershipNumber = membershipNumber;
        }

        private String MembershipNumber;

        public boolean isHasMemberhsip() {
            return HasMemberhsip;
        }

        public void setHasMemberhsip(boolean HasMemberhsip) {
            this.HasMemberhsip = HasMemberhsip;
        }

        public String getNameX() {
            return NameX;
        }

        public void setNameX(String NameX) {
            this.NameX = NameX;
        }

        public Object getHeadPortrait() {
            return HeadPortrait;
        }

        public void setHeadPortrait(Object HeadPortrait) {
            this.HeadPortrait = HeadPortrait;
        }
    }
}
