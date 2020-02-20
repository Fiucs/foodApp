package com.foodweb.pojo;

import java.io.Serializable;
import java.util.Map;

public class FoodSimInfo implements Serializable {

    private String foodid;
    private String ingredients;
    private Map<String,Integer> divres;

    public FoodSimInfo(String foodid , String ingredients) {
        this.foodid = foodid;
        this.ingredients = ingredients;
    }

    public FoodSimInfo(String foodid , Map<String,Integer> divres) {
        this.foodid = foodid;
        this.divres = divres;
    }



    public Map<String, Integer> getDivres() {
        return divres;
    }

    @Override
    public String toString() {
        return "FoodSimInfo{" +
                "id=" + foodid +
                ", ingredients='" + ingredients + '\'' +
                ", divres=" + divres +
                '}';
    }

    public void setDivres(Map<String, Integer> divres) {
        this.divres = divres;
    }

    public FoodSimInfo() {
    }

    public String getfoodid() {
        return foodid;
    }

    public void setfoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getingredients() {
        return ingredients;
    }

    public void setingredients(String ingredients) {
        this.ingredients = ingredients;
    }

    //重写 equals 重写 比较 foodid
    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(!(obj instanceof FoodSimInfo))
        {
            return false;//不是foodSimIfo类或子类 返回false
        }

        FoodSimInfo simInfo=(FoodSimInfo)obj;

        if (this.getfoodid().equals(simInfo.getfoodid()))
        {
            System.out.println("foodid重复了："+this.getfoodid());
            return true;//foodid相同 则两个对象相等
        }
        else
        {
            return false;
        }


    }
}
