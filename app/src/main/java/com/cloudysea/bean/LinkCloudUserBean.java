package com.cloudysea.bean;

import java.io.Serializable;

/**
 * @author roof 2019/11/19.
 * @email lyj@yhcs.com
 * @detail
 */
public class LinkCloudUserBean extends PublicBean  implements Serializable {
    /**
     * Data : {"IsSuccessful":false,"ErrorMessage":"","Data":null}
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
         * IsSuccessful : false
         * ErrorMessage :
         * Data : null
         */

        private boolean IsSuccessful;
        private String ErrorMessage;
        private Object Data;

        public boolean isIsSuccessful() {
            return IsSuccessful;
        }

        public void setIsSuccessful(boolean IsSuccessful) {
            this.IsSuccessful = IsSuccessful;
        }

        public String getErrorMessage() {
            return ErrorMessage;
        }

        public void setErrorMessage(String ErrorMessage) {
            this.ErrorMessage = ErrorMessage;
        }

        public Object getData() {
            return Data;
        }

        public void setData(Object Data) {
            this.Data = Data;
        }
    }
}
