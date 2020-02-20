package com.foodweb.pojo;

import com.utils.MethodStringUtils;

import java.io.Serializable;
import java.util.*;

public class FoodsAll implements Serializable {
    private int id;//索引id
    private String foodid;//食物id
    private String foodname;//食物名
    private String foodpic;//主题图片路径
    private String introduce;//食物介绍
    private String ingredients;//食物材料
    private Map<String, List<String>> methods;//操作步骤
    private String bigclass;//大分类
    private int bigclassid;//大分类
    private String smallclass;//小分类
    private int smallclassid;//小分类
    private Date derive_time;//日期

    public FoodsAll() {
    }

    public Date getDerive_time() {
        return derive_time;
    }

    public void setDerive_time(Date derive_time) {
        this.derive_time = derive_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodpic() {
        return foodpic;
    }

    public void setFoodpic(String foodpic) {
        this.foodpic = foodpic;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIngredients() {
        return ingredients;
    }


    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, List<String>> getMethods() {
        return methods;
    }

    public void setMethods(String methods) {

        this.methods = MethodStringUtils.splitOneWord(methods);
    }
    public void setMethods01(Map<String, List<String>> methods) {

        this.methods = methods;
    }


    public String getBigclass() {
        return bigclass;
    }

    public void setBigclass(String bigclass) {
        this.bigclass = bigclass;
    }

    public int getBigclassid() {
        return bigclassid;
    }

    public void setBigclassid(int bigclassid) {
        this.bigclassid = bigclassid;
    }

    public String getSmallclass() {
        return smallclass;
    }

    public void setSmallclass(String smallclass) {
        this.smallclass = smallclass;
    }

    public int getSmallclassid() {
        return smallclassid;
    }

    public void setSmallclassid(int smallclassid) {
        this.smallclassid = smallclassid;
    }

    @Override
    public String toString() {
        return "FoodsAll{" +
                "id=" + id +
                ", foodid=" + foodid +
                ", foodname='" + foodname + '\'' +
                ", foodpic='" + foodpic + '\'' +
                ", introduce='" + introduce + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", methods='" + methods + '\'' +
                ", bigclass='" + bigclass + '\'' +
                ", bigclassid=" + bigclassid +
                ", smallclass='" + smallclass + '\'' +
                ", smallclassid=" + smallclassid +
                ", derive_time=" + derive_time +
                '}';
    }


}
