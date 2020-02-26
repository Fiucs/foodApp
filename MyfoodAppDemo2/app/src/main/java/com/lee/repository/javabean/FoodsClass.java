package com.lee.repository.javabean;

import java.io.Serializable;

public class FoodsClass implements Serializable {
    private int id;//// 索引
    private String smallclass;//小分类
    private  String bigclass;//大分类
    private String smallclass_pic;//小图标

    public FoodsClass() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSmallclass() {
        return smallclass;
    }

    public void setSmallclass(String smallclass) {
        this.smallclass = smallclass;
    }

    public String getBigclass() {
        return bigclass;
    }

    public void setBigclass(String bigclass) {
        this.bigclass = bigclass;
    }

    public String getSmallclass_pic() {
        return smallclass_pic;
    }

    public void setSmallclass_pic(String smallclass_pic) {
        this.smallclass_pic = smallclass_pic;
    }

    @Override
    public String toString() {
        return "FoodsClass{" +
                "id=" + id +
                ", smallclass='" + smallclass + '\'' +
                ", bigclass='" + bigclass + '\'' +
                ", smallclass_pic='" + smallclass_pic + '\'' +
                '}';
    }
}
