package com.foodweb.pojo;

import java.io.Serializable;
import java.util.Date;

public class Favarites implements Serializable {
    private int id;//索引
    private String user_id;
    private String food_id;
    private Date derive_time;
    private String favorite;

    public Favarites() {
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

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Favarites{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", food_id='" + food_id + '\'' +
                ", derive_time=" + derive_time +
                ", favorite='" + favorite + '\'' +
                '}';
    }
}
