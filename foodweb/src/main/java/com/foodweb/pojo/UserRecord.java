package com.foodweb.pojo;

import java.io.Serializable;
import java.util.Date;

public class UserRecord implements Serializable {


    private int id;//索引
    private String food_id ;//食物id
    private String user_id;//用户id
    private String food_bigclass;//食物大分类
    private String food_smallclass;
    private Date derive_time;//记录 生成时间戳
    private String status;//记录使用状态 0为未被推荐系统使用 1 代表使用了

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFood_smallclass() {
        return food_smallclass;
    }

    public void setFood_smallclass(String food_smallclass) {
        this.food_smallclass = food_smallclass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFood_bigclass() {
        return food_bigclass;
    }

    public void setFood_bigclass(String food_bigclass) {
        this.food_bigclass = food_bigclass;
    }

    public Date getDerive_time() {
        return derive_time;
    }

    public void setDerive_time(Date derive_time) {
        this.derive_time = derive_time;
    }

    @Override
    public String toString() {
        return "UserRecord{" +
                "food_id='" + food_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", food_bigclass='" + food_bigclass + '\'' +
                ", derive_time=" + derive_time +
                ", status='" + status + '\'' +
                '}';
    }
}
