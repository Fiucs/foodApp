package com.lee.repository.javabean;

import java.io.Serializable;

public class MsgInfo implements Serializable
{
    private Boolean success;//操作是否成功
    private String msg;//操作结果信息

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MsgInfo() {
    }

    public MsgInfo(Boolean success, String msg) {
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
