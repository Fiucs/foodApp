package com.foodweb.pojo;

import java.io.Serializable;

public class MsgInfo implements Serializable
{
    private boolean success;//操作是否成功
    private String msg;//操作结果信息

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MsgInfo(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MsgInfo{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
