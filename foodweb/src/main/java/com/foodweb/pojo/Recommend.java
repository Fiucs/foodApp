package com.foodweb.pojo;

import java.io.Serializable;
import java.util.Date;

public class Recommend implements Serializable {


    private int id;//索引
    private String user_id;//用户id
    private String food_id;//菜id
    private Date derive_time;//时间戳 生成
    private String feedback;//用户反馈
    private String recommend_value;//推荐值 相似度

    public Recommend(String user_id, String food_id, Date derive_time) {
        this.user_id = user_id;
        this.food_id = food_id;
        this.derive_time = derive_time;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public Date getDerive_time() {
        return derive_time;
    }

    public void setDerive_time(Date derive_time) {
        this.derive_time = derive_time;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRecommend_value() {
        return recommend_value;
    }

    public void setRecommend_value(String recommend_value) {
        this.recommend_value = recommend_value;
    }

    @Override
    public String toString() {
        return "Recommend{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", food_id='" + food_id + '\'' +
                ", derive_time=" + derive_time +
                ", feedback='" + feedback + '\'' +
                ", recommend_value='" + recommend_value + '\'' +
                '}';
    }
}
